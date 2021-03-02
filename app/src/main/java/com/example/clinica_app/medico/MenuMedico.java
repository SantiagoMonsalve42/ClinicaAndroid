package com.example.clinica_app.medico;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.clinica_app.MainActivity;
import com.example.clinica_app.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MenuMedico extends AppCompatActivity implements View.OnClickListener{

    Button btnSalir,actcitas;
    Spinner spcitas;
    ArrayList<String> citas =new ArrayList<>();
    ArrayAdapter<String> citasadapter;
    RequestQueue requestQueue;
    String email=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences prefs = getSharedPreferences("datosLogin",   Context.MODE_PRIVATE);
        email = prefs.getString("correo", "");
        setContentView(R.layout.activity_menu_medico);
        requestQueue=Volley.newRequestQueue(this);
        btnSalir=(Button)findViewById(R.id.buttonSalirM);
        actcitas=(Button)findViewById(R.id.btn_actcitas);
        actcitas.setOnClickListener(this);
        btnSalir.setOnClickListener(this);
        spcitas=(Spinner)findViewById(R.id.spinner_citas);


        mostrarCitas();

    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.buttonSalirM){
            SharedPreferences preferences=getSharedPreferences("datosLogin", Context.MODE_PRIVATE);
            preferences.edit().clear().commit();
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }
        if(v.getId()==R.id.btn_actcitas){
           mostrarCitas();
        }
    }
    public void mostrarCitas(){

        String url ="http://192.168.0.21/clinica_service/cita/read.php?idmedico="+email;
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
             JSONArray jsonArray=null;
                try {
                    jsonArray = response.getJSONArray("cita");
                    for(int i=0; i<jsonArray.length();i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        int id= jsonObject.optInt("idcita");
                        String nombre = jsonObject.optString("nombre");
                        String apellido = jsonObject.optString("apellido");
                        String fecha = jsonObject.optString("fecha");
                        String hora = jsonObject.optString("hora");
                        String resul=nombre+" "+fecha+" "+hora;
                        System.out.println(resul);
                        citas.add(resul);
                        citasadapter = new ArrayAdapter<>(MenuMedico.this,
                                android.R.layout.simple_spinner_item,citas);
                        citasadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spcitas.setAdapter(citasadapter);
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