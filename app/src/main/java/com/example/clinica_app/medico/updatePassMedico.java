package com.example.clinica_app.medico;

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
import com.example.clinica_app.MainActivity;
import com.example.clinica_app.R;
import com.example.clinica_app.login.UpdatePass;

import java.util.HashMap;
import java.util.Map;

public class updatePassMedico extends AppCompatActivity implements View.OnClickListener {
    EditText txt_c1,txt_c2;
    Button updatePM;
    RequestQueue requestQueue;
    String pass=null,email=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_pass_medico);
        requestQueue= Volley.newRequestQueue(this);
        txt_c1=(EditText)findViewById(R.id.txt_passm1);
        txt_c2=(EditText)findViewById(R.id.txt_passm2);
        updatePM=(Button)findViewById(R.id.btn_updatepassM);
        SharedPreferences prefs = getSharedPreferences("datosLogin",   Context.MODE_PRIVATE);
        email = prefs.getString("correo", "");
        updatePM.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id =v.getId();

        if(id == R.id.btn_updatepassM){
            if(txt_c1.getText().toString().isEmpty() || txt_c2.getText().toString().isEmpty()){
                Toast.makeText(this,"No dejes campos vacios..",Toast.LENGTH_SHORT).show();
            }
            else{
                if(txt_c1.getText().toString().equals(txt_c2.getText().toString())){
                    pass=txt_c1.getText().toString();
                    updatePass(email,pass);
                    Toast.makeText(this,"Contraseña actualizada correctamente..",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), perfilMedico.class);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(this,"Las contraseñas no son iguales..",Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void updatePass(String id, String pass) {
        //String URL="https://clinica-service.000webhostapp.com/clinica_service/paciente/updatepass.php";//WEB
        String URL="http://192.168.0.21/clinica_service/medico/updatepass.php";

        StringRequest stringRequest=
                new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                    @Override


                    public void onResponse(String response) {
                        System.out.println(response.toString());
                        if(response.toString().trim().equalsIgnoreCase("1")){
                            Toast.makeText(updatePassMedico.this,"Contraseña actualizada correctamente !!",Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(updatePassMedico.this,"No se pudo actualizar la contraseña, intente mas tarde..",Toast.LENGTH_SHORT).show();
                        }
                    }

                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();

                    }
                }){
                    @Override
                    protected Map<String,String> getParams() throws AuthFailureError {
                        Map<String,String> parametros = new HashMap<String,String>();
                        parametros.put("command", "updateLoc");

                        parametros.put("idmedico",email);
                        parametros.put("pass",txt_c1.getText().toString());
                        return parametros;
                    }
                };
        requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }
}