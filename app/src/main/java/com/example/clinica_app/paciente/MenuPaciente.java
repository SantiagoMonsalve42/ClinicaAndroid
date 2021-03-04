package com.example.clinica_app.paciente;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
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

public class MenuPaciente extends AppCompatActivity implements View.OnClickListener {
    Button btnSalir;
    Button btnAgendarC;
    TextView txtNombre;
    String correo=null;
    Spinner spnCita;
    TextView txtID;
    TextView txtApellido;
    ArrayList<String> citasList = new ArrayList<>();
    ArrayAdapter<String> citasAdapter;
    RequestQueue requestQueue;
    public static final String nombrePac="nombre";
    public static final int idPac=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_paciente);
        SharedPreferences prefs = getSharedPreferences("datosLogin",   Context.MODE_PRIVATE);
        correo = prefs.getString("correo", "");
        btnSalir= (Button)findViewById(R.id.btnSalirPa);
        requestQueue = Volley.newRequestQueue(this);
        btnAgendarC= (Button)findViewById(R.id.btnAgendarP);
        //concatenación de nombre paciente
        txtNombre=(TextView)findViewById(R.id.txtNombre);
        String nombreP= getIntent().getStringExtra("nombre");
        txtNombre.setText(nombreP);
        txtNombre=(TextView)findViewById(R.id.txtNombre);
        txtID=(TextView)findViewById(R.id.txtID);
        txtApellido=(TextView)findViewById(R.id.txtApellido);
        btnSalir.setOnClickListener(this);
        btnAgendarC.setOnClickListener(this);
        spnCita=(Spinner)findViewById(R.id.spnCitas);
        VerPaciente();
        MostrarCita("http://192.168.0.21/clinica_service/paciente/mostrarCitas.php?correo="+correo);
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.btnSalirPa){
            SharedPreferences preferences=getSharedPreferences("datosLogin", Context.MODE_PRIVATE);
            preferences.edit().clear().commit();
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }
        else if(v.getId()==R.id.btnAgendarP){

            Intent intent = new Intent(getApplicationContext(),AsignarCitaPac.class);
            startActivity(intent);
            finish();
        }
    }
    private void VerPaciente() {
        String URL="http://192.168.0.21/clinica_service/paciente/datosSesion.php?correo="+correo;
        JsonObjectRequest jsonObjectRequest= new JsonObjectRequest(
                Request.Method.GET, URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        String  nom,ape,id,clave,telefono,cc,pregunta,respuesta,fechaN;

                        try {
                            nom = response.getString("nombre");
                            ape = response.getString("apellido");
                            id=response.getString("idpaciente");
                            txtNombre.setText(""+nom);
                            txtApellido.setText(""+ape);
                            txtID.setText(""+id);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }
        );
        requestQueue.add(jsonObjectRequest);
    }

    public void MostrarCita(String URLC) {

        requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URLC, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONArray jsonArray = null;
                try {
                    jsonArray = response.getJSONArray("cita");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        int idC = jsonObject.optInt("idcita");
                        String fechaC = jsonObject.optString("fecha");
                        String horaC = jsonObject.optString("hora");

                        citasList.add(fechaC);
                        citasAdapter = new ArrayAdapter<>(MenuPaciente.this, android.R.layout.simple_list_item_1, citasList);
                        spnCita.setAdapter(citasAdapter);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Error al mostrar citas", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonObjectRequest);
    }


}