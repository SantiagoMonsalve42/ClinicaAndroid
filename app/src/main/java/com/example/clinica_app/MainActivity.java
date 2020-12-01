package com.example.clinica_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
    String rol="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtcorreo=(EditText) findViewById(R.id.edtCorreo);
        txtclave=(EditText) findViewById(R.id.edtPassword);
        btnIngresar=(Button)findViewById(R.id.btnLogin);
        btnIngresar.setOnClickListener(this);



    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == R.id.btnLogin) {
            ValidarUser("http://192.168.10.104/clinica_service/admin/validarAdmin.php");
        }

    }

    public void ValidarUser(String URL){
        StringRequest stringRequest= new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(!response.isEmpty()) {

                        Intent intent = new Intent(getApplicationContext(), MenuAdmin.class);
                        startActivity(intent);


                }else{
                    Toast.makeText(MainActivity.this, "Usuario o clave incorrectas",Toast.LENGTH_SHORT).show();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "Error al tratar de conectar",Toast.LENGTH_SHORT).show();

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param= new HashMap<String, String>();
                param.put("correo",txtcorreo.getText().toString());
                param.put("clave",txtclave.getText().toString());
               // param.put("rol",rol);
                return param;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(this);
         requestQueue.add(stringRequest);
    }
}