package com.example.clinica_app.login;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
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
import com.example.clinica_app.MainActivity;
import com.example.clinica_app.R;

import java.util.HashMap;
import java.util.Map;

public class UpdatePass extends AppCompatActivity implements View.OnClickListener {
    String id_p,pass;
    EditText txt_pass;
    Button update;
    RequestQueue requestQueue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_ask);
        txt_pass=(EditText)findViewById(R.id.txt_pass);
        update=(Button)findViewById(R.id.btn_update);
        update.setOnClickListener(this);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            id_p = bundle.getString("id");
        }
    }

    @Override
    public void onClick(View v) {
        int id =v.getId();
        if(id == R.id.btn_update){
            if(txt_pass.getText().equals("")){
                Toast.makeText(UpdatePass.this,"No dejes campos vacios..",Toast.LENGTH_SHORT).show();
            }else{
                pass=txt_pass.getText().toString();
                updatePass(id_p,pass);
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        }
    }

    private void updatePass(String id, String pass) {
        //String URL="https://clinica-service.000webhostapp.com/clinica_service/paciente/updatepass.php";//WEB
        String URL="http://c192.168.0.21/clinica_service/paciente/updatepass.php";

        StringRequest stringRequest=
                new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getApplicationContext(), "BIEN", Toast.LENGTH_SHORT);
                        AlertDialog.Builder alerta= new AlertDialog.Builder(UpdatePass.this);//Mensaje en cuadro de texto en alerta
                        alerta.setMessage("Contraseña actualizada correctamente")
                                .setCancelable(false)//Paara salir del aleert pulsando fuera de el
                                .setPositiveButton("Vale", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });
                        AlertDialog titulo= alerta.create();
                        titulo.setTitle("Bien hecho!");
                        titulo.show();

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

                        parametros.put("idpaciente",id_p);
                        parametros.put("pass",txt_pass.getText().toString());
                        return parametros;
                    }
                };
        requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }
    }
