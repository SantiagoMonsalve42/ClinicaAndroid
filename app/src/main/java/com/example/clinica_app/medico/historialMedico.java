package com.example.clinica_app.medico;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

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

public class historialMedico extends AppCompatActivity implements View.OnClickListener {
    ListView citasL;
    ImageButton update;
    ArrayList<String> citas =new ArrayList<>();
    ArrayAdapter<String> citasadapter;
    RequestQueue requestQueue;
    String email=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial_medico);
        citasL=(ListView)findViewById(R.id.lista_historial);
        update=(ImageButton)findViewById(R.id.btn_actualizarHistorial);
        SharedPreferences prefs = getSharedPreferences("datosLogin",   Context.MODE_PRIVATE);
        email = prefs.getString("correo", "");
        requestQueue= Volley.newRequestQueue(this);
        update.setOnClickListener(this);
        leerCitas();
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.btn_actualizarHistorial){
           leerCitas();
            Toast.makeText(historialMedico.this,"Historial actualizado correctamente..",Toast.LENGTH_SHORT).show();
        }
    }

    public void leerCitas(){

            citasL.setAdapter(null);
            citas.clear();
            String url ="http://192.168.0.21/clinica_service/medico/verCita.php?mail="+email;
            JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    JSONArray jsonArray=null;
                    try {
                        jsonArray = response.getJSONArray("medico");
                        for(int i=0; i<jsonArray.length();i++){
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            int id= jsonObject.optInt("idcita");
                            String hora = jsonObject.optString("hora");
                            String nombre = jsonObject.optString("nombre");
                            String fecha = jsonObject.optString("fecha");
                            String estado = jsonObject.optString("estado");
                            String mostrar=id+". "+hora+ "  "+ fecha+"\n "+"Consultorio: "+nombre+"    Estado: finalizado \n";

                             citas.add(mostrar);
                            citasadapter = new ArrayAdapter<>(historialMedico.this,
                                    android.R.layout.simple_list_item_1,citas);
                            citasadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            citasL.setAdapter(citasadapter);
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