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
    ListView lcitas;
    Button btnSalir;
    ArrayList<String> citaList = new ArrayList<>();
    ArrayAdapter<String> citaAdapter;
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_medico);
        requestQueue = Volley.newRequestQueue(this);
        lcitas = findViewById(R.id.lcita);
        String url = "//clinica-service@files.000webhost.com/public_html/clinica_service/medico/verCita.php";
        btnSalir.setOnClickListener(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONArray jsonArray = null;
                try {
                    jsonArray = response.getJSONArray("cita");
                    for (int i = 0; i<jsonArray.length(); i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String fecha = jsonObject.optString("fecha");
                        citaList.add(fecha);
                        String hora = jsonObject.optString("hora");
                        citaList.add(hora);
                        String nombreCon = jsonObject.optString("nombreCon");
                        citaList.add(nombreCon);
                        String nombre = jsonObject.optString("nombre");
                        citaList.add(nombre);
                        String apellido = jsonObject.optString("apellido");
                        citaList.add(apellido);
                        citaAdapter = new ArrayAdapter<>(MenuMedico.this, android.R.layout.simple_list_item_1, citaList);
                        citaAdapter.setDropDownViewResource(android.R.layout.simple_expandable_list_item_1);
                        lcitas.setAdapter(citaAdapter);
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }

        );
        requestQueue.add(jsonObjectRequest);
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
    }

}