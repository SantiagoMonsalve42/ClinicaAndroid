package com.example.clinica_app.medico;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.clinica_app.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class perfilMedico extends AppCompatActivity {
    TextView nombretxt,fechatxt,correotxt,preguntatxt;
    RequestQueue requestQueue;
    String email=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_medico);
        requestQueue= Volley.newRequestQueue(this);
        nombretxt=(TextView)findViewById(R.id.txt_fullnamemedico);
        fechatxt=(TextView)findViewById(R.id.txt_fechanacmedico);
        correotxt=(TextView)findViewById(R.id.txt_correomedico);
        preguntatxt=(TextView)findViewById(R.id.txt_preguntamedico);
        SharedPreferences prefs = getSharedPreferences("datosLogin",   Context.MODE_PRIVATE);
        email = prefs.getString("correo", "");
        mostrarDatos();
    }

    public void mostrarDatos(){

        String url ="http://192.168.0.21/clinica_service/medico/read.php?idmedico="+email;
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONArray jsonArray=null;
                try {
                    jsonArray = response.getJSONArray("medico");
                    for(int i=0; i<jsonArray.length();i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String nombre = jsonObject.optString("nombre");
                        String apellido = jsonObject.optString("apellido");
                        String fullname=nombre+" "+apellido;
                        String fecha = jsonObject.optString("fecha_nacimiento");
                        String correo = jsonObject.optString("correo");
                        String pregunta = jsonObject.optString("pregunta");
                        nombretxt.setText(fullname);
                        fechatxt.setText(fecha);
                        correotxt.setText(correo);
                        preguntatxt.setText(pregunta);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(jsonObjectRequest);
    }
}