package com.example.clinica_app.admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.clinica_app.MainActivity;
import com.example.clinica_app.R;
import com.example.clinica_app.login.UpdatePass;
import com.example.clinica_app.medico.MenuMedico;
import com.example.clinica_app.medico.historialMedico;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MenuAdmin extends AppCompatActivity implements View.OnClickListener {
    Button btnSalir, btnbuscar;
    EditText editBuscar;
    RequestQueue requestQueue;
    ListView filtroMe;
    ArrayList<String> datosMe = new ArrayList<>();
    ArrayAdapter<String> medicoadapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestQueue = Volley.newRequestQueue(this);
        setContentView(R.layout.activity_menu_admin);
        filtroMe = (ListView) findViewById(R.id.lista_filtroMe);
        editBuscar = (EditText) findViewById(R.id.editTextBuscar);
        btnbuscar = (Button) findViewById(R.id.btn_buscar);
        btnbuscar.setOnClickListener(this);
        btnSalir = (Button) findViewById(R.id.btnSalirA);
        btnSalir.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnSalirA) {
            SharedPreferences preferences = getSharedPreferences("datosLogin", Context.MODE_PRIVATE);
            preferences.edit().clear().commit();//Limpiar datos
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }
        if (v.getId() == R.id.btn_buscar) {
            filtroMe.setAdapter(null);
            datosMe.clear();
            String url = "http://192.168.0.21/clinica_service/medico/filtrarMedico.php";
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    JSONArray jsonArray = null;
                    try {
                        jsonArray = response.getJSONArray("medico");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            int id = jsonObject.optInt("idmedico");
                            String nombre = jsonObject.optString("nombre");
                            String apellido = jsonObject.optString("apellido");
                            String correo = jsonObject.optString("correo");
                            String tarjeta = jsonObject.optString("tarjetaProfesional");
                            String mostrar = id + ". " + nombre + "  " + apellido + "\n " + "Correo: " + correo + "    Tarjeta:" + tarjeta;
                            datosMe.add(mostrar);
                            medicoadapter = new ArrayAdapter<>(MenuAdmin.this, android.R.layout.simple_list_item_1, datosMe);
                            medicoadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            filtroMe.setAdapter(medicoadapter);
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
}