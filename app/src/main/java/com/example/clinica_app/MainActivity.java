package com.example.clinica_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    EditText txtcorreo,txtclave;

    Button btnIngresar;
    Button btnRegN;
    Button btnRecuperar;

    String rol="";
    String correo;
    String clave;
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
            ValidarUser("http://192.168.0.21/clinica_service/paciente/validarPaciente.php");
            }
            else{
                Toast.makeText(MainActivity.this,"No dejes campos vacios",Toast.LENGTH_SHORT).show();
            }
        }
        else if(id==R.id.btnRegisN){
            Intent intent= new Intent(getApplicationContext(),RegistroPaciente.class);
            startActivity(intent);
            finish();
        }

    }

    public void ValidarUser(String URL){
        StringRequest stringRequest= new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(!response.isEmpty()) {

                        GuardarDatos();
                        Intent intent = new Intent(getApplicationContext(), MenuPaciente.class);
                        startActivity(intent);
                        finish();


                }else{
                    Toast.makeText(MainActivity.this, "Usuario o clave incorrectas",Toast.LENGTH_SHORT).show();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, error.toString(),Toast.LENGTH_SHORT).show();

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param= new HashMap<String, String>();
                param.put("correo",correo);
                param.put("clave",clave);
               // param.put("rol",rol);
                return param;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(this);
         requestQueue.add(stringRequest);
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