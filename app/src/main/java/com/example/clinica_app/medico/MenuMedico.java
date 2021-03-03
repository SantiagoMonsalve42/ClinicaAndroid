package com.example.clinica_app.medico;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class MenuMedico extends AppCompatActivity implements View.OnClickListener{

    Button btnSalir,actcitas,atenderc,perfil,historial;
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
        perfil=(Button)findViewById(R.id.btn_perfilM);
        actcitas=(Button)findViewById(R.id.btn_actcitas);
        atenderc=(Button)findViewById(R.id.btn_atender);
        historial=(Button)findViewById(R.id.btn_hisotoriaCitasM);
        perfil.setOnClickListener(this);
        atenderc.setOnClickListener(this);
        actcitas.setOnClickListener(this);
        btnSalir.setOnClickListener(this);
        historial.setOnClickListener(this);
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
            Toast.makeText(MenuMedico.this,"Citas diarias actualizadas correctamente..",Toast.LENGTH_SHORT).show();
        }
        if(v.getId()==R.id.btn_hisotoriaCitasM){
            Intent intent = new Intent(getApplicationContext(), historialMedico.class);
            startActivity(intent);
        }
        if((v.getId()==R.id.btn_perfilM)){
            Intent intent = new Intent(getApplicationContext(), perfilMedico.class);
            startActivity(intent);
        }
        if(v.getId()==R.id.btn_atender){
            try{


            if (spcitas.getSelectedItem()!=""){
                if(spcitas.getSelectedItem().toString() != null) {


                    String horaux = spcitas.getSelectedItem().toString();

                    if (validarHora(horaux)) {

                        Intent intent = new Intent(getApplicationContext(), atenderCita.class);
                        intent.putExtra("hora",horaux);
                        startActivity(intent);
                    } else {
                        Toast.makeText(MenuMedico.this, "Recuerde que solo puede atender citas en la hora estipulada..", Toast.LENGTH_SHORT).show();
                    }
                }
              //Si el spinner no tiene nada seleccionado
            }else{
                Toast.makeText(getApplicationContext(), "No tiene citas pendientes para el dia de hoy..", Toast.LENGTH_LONG).show();
            }
            }catch(Exception e){
                Toast.makeText(getApplicationContext(), "No tiene citas pendientes para el dia de hoy..", Toast.LENGTH_LONG).show();
            }
        }
    }

    public boolean validarHora(String fecha){
        //fecha actual del sistema
        Calendar calendario = new GregorianCalendar();
        int horaA, minutosA,horaD=0,minutosD=0;
        horaA =calendario.get(Calendar.HOUR_OF_DAY);
        minutosA = calendario.get(Calendar.MINUTE);
        //fecha dada por las citas pendientes
        DateFormat inFormat = new SimpleDateFormat( "HH:mm:ss", Locale.UK);
        Date date = null;
        try {
            date = inFormat.parse(fecha);
            horaD=date.getHours();
            minutosD=date.getMinutes();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        System.out.println("hora actual "+horaA + minutosA);
        System.out.println("hora cita"+horaD + minutosD);
        if(horaA == horaD && minutosD >= minutosA){
            return true;
        }else
        return false;
    }

    public void mostrarCitas(){
        spcitas.setAdapter(null);
        citas.clear();
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
                        citas.add(hora);
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