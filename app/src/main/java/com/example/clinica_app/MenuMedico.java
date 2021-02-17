package com.example.clinica_app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MenuMedico extends AppCompatActivity {
    ListView lcitas;
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
                        //String
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
}