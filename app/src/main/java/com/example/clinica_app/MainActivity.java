package com.example.clinica_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.clinica_app.admin.MenuAdmin;
import com.example.clinica_app.login.RegistroPaciente;
import com.example.clinica_app.login.validarMail;
import com.example.clinica_app.medico.MenuMedico;
import com.example.clinica_app.paciente.DatosPaciente;
import com.example.clinica_app.paciente.MenuPaciente;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    EditText txtcorreo,txtclave;

    Button btnIngresar;
    Button btnRegN;
    Button btnRecuperar;
    RequestQueue requestQueue;
    String correo;
    String clave;
    DatosPaciente datPac= new DatosPaciente();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtcorreo=(EditText) findViewById(R.id.edtNombre);
        txtclave=(EditText) findViewById(R.id.edtApellido);
        btnIngresar=(Button)findViewById(R.id.btnLogin);
        btnRegN=(Button)findViewById(R.id.btnRegisN);
        btnIngresar.setOnClickListener(this);

        btnRegN.setOnClickListener(this);

        btnRecuperar=(Button)findViewById(R.id.btn_pass);
        btnRecuperar.setOnClickListener(this);

        RecuperarDatos();


    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == R.id.btn_pass){
            Intent intent = new Intent(getApplicationContext(), validarMail.class);
            startActivity(intent);
        }

        if(id == R.id.btnLogin) {
            correo=txtcorreo.getText().toString();
            clave=txtclave.getText().toString();
            if(!correo.isEmpty() && !clave.isEmpty()){
            //ValidarUser("https://clinica-service.000webhostapp.com/clinica_service/login/verificar.php");//Web
            ValidarUser("http://192.168.0.21/clinica_service/login/verificar.php");
            }
            else{
                Toast.makeText(MainActivity.this,"No dejes campos vacios",Toast.LENGTH_SHORT).show();
            }
        }
        else if(id==R.id.btnRegisN){
            Intent intent= new Intent(getApplicationContext(), RegistroPaciente.class);
            startActivity(intent);
            finish();
        }

    }

    private void ValidarUser(String URL){
        //Validar que no dejar datos en blanco

            StringRequest stringRequest=
                    new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            String op= response.toString();

                            redireccionar(op);
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_SHORT).show();
                        }
                    }){
                        @Override
                        protected Map<String,String>getParams() throws AuthFailureError{
                            Map<String,String> parametros= new HashMap<String, String>();
                            parametros.put("correo",txtcorreo.getText().toString());
                            parametros.put("clave",txtclave.getText().toString());
                            return parametros;
                        }
                    };
            requestQueue= Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);
        }


    public void redireccionar(String op){
        try{
            String opcion=op.trim();
            int ops=Integer.parseInt(opcion);
            Intent intent;
            switch (ops){
                case 0:
                    AlertDialog.Builder alerta= new AlertDialog.Builder(MainActivity.this);//Mensaje en cuadro de texto en alerta
                    alerta.setMessage("Correo o contraseña invalida..")
                            .setCancelable(false)//Paara salir del aleert pulsando fuera de el
                            .setPositiveButton("Vale", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                    AlertDialog titulo= alerta.create();
                    titulo.setTitle("Error al iniciar sesión");
                    titulo.show();
                    break;
                case 1:
                    intent= new Intent(getApplicationContext(), MenuAdmin.class);
                    GuardarDatos();
                    startActivity(intent);
                    break;
                case 2:
                    intent= new Intent(getApplicationContext(), MenuMedico.class);
                    GuardarDatos();
                    startActivity(intent);
                    break;
                case 3:
                    intent= new Intent(getApplicationContext(), MenuPaciente.class);
                    GuardarDatos();
                    startActivity(intent);
                    break;
            }
        }catch(Exception e){
            System.out.println("error"+e.getMessage());
        }


    }
    public void GuardarDatos(){
        SharedPreferences  preferences= getSharedPreferences("datosLogin" , Context.MODE_PRIVATE);//Nombre de donde se guerdarán los datos
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("correo",correo);
        editor.putString("clave",clave);
        editor.putBoolean("sesion",true);//Guardar sesion en caso de que esta sea correcta
        editor.commit();


    }
    public void RecuperarDatos(){
        SharedPreferences  preferences= getSharedPreferences("datosLogin", Context.MODE_PRIVATE);//Nombre de donde se guerdarán los datos
        txtcorreo.setText(preferences.getString("correo",""));//Si no hay preferencia se guarda un correo provisional por defecto
        txtclave.setText(preferences.getString("clave",""));//Si no hay preferencia se guarda un correo provisional por defecto

    }
}