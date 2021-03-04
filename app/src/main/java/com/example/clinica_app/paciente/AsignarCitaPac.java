package com.example.clinica_app.paciente;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.clinica_app.R;
import com.example.clinica_app.login.UpdatePass;
import com.example.clinica_app.medico.MenuMedico;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AsignarCitaPac extends AppCompatActivity implements View.OnClickListener {
    Button btnAgigC;
    Button btnfecha;
    Button btnhora;
    String email=null;
    TextView txtFecha;
    TextView txtidespec,txtidmed,txtidpac;
    TextView txtSelM;

    RequestQueue requestQueue;

    Spinner spnMed,spnHoras;
    ArrayList<String> medicosList = new ArrayList<>();
    ArrayAdapter<String> medicosAdapter;
    ArrayAdapter<String> horasAdapter;
    Spinner spnCon;
    ArrayList<String> consulList = new ArrayList<>();
    ArrayAdapter<String> consulAdapter;
    String[] horas = {"10:00:00","11:00:00","12:00:00","13:00:00","14:00:00","15:00:00","16:00:00","17:00:00","18:00:00","19:00:00"};
    int aa,mm,dd,hh,min; //TIEMPO Y FECHA


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asignar_cita_pac);
        txtFecha=(TextView)findViewById(R.id.txtDate);

        txtSelM= (TextView) findViewById(R.id.txtSelMed);

        btnAgigC= (Button)findViewById(R.id.btnAgendarCitaP);
        btnfecha= (Button)findViewById(R.id.btnDate);
        horasAdapter= new ArrayAdapter<>(AsignarCitaPac.this, android.R.layout.simple_list_item_1,horas);

        spnMed=(Spinner)findViewById(R.id.spnMed);
        spnCon=(Spinner)findViewById(R.id.spnCon);
        spnHoras=(Spinner)findViewById(R.id.spiner_hora);
        btnAgigC.setOnClickListener(this);
        btnfecha.setOnClickListener(this);

        txtidespec=(TextView)findViewById(R.id.txt_idespec);
        txtidmed=(TextView)findViewById(R.id.txt_idmed);
        txtidpac=(TextView)findViewById(R.id.txt_idpac);

        SharedPreferences prefs = getSharedPreferences("datosLogin",   Context.MODE_PRIVATE);
        email = prefs.getString("correo", "");

        spnHoras.setAdapter(horasAdapter);

        MostrarEspecialidad("http://192.168.0.21/clinica_service/especialidad/selectEspe.php");
        MostrarConsul("http://192.168.0.21/clinica_service/cita/selectCon.php");

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onClick(View v) {
        if(R.id.btnAgendarCitaP==v.getId()){
            if( txtFecha.getText().toString().isEmpty() ){
                Toast.makeText(getApplicationContext(), "No deje ningún campo vacio", Toast.LENGTH_SHORT).show();
            }
            else{
                String sDate1=txtFecha.getText().toString();
                int actd=0;
                mostrarIdMed(spnMed.getSelectedItem().toString());
                mostrarIdPaciente();
                mostrarIdConsult(spnCon.getSelectedItem().toString());
                AsignarCitaP("http://192.168.0.21/clinica_service/cita/create.php");
            }
        }
        if(R.id.btnDate==v.getId()){

            final Calendar calendar =Calendar.getInstance();
            dd=calendar.get(Calendar.DAY_OF_MONTH);
            mm=calendar.get(Calendar.MONTH);
            aa=calendar.get(Calendar.YEAR);


            DatePickerDialog datePickerDialog= new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    month++;
                    txtFecha.setText(year+"-" + month + "-"+dayOfMonth);
                }
            },aa,mm,dd);
            datePickerDialog.show();

        }


    }

    public void MostrarEspecialidad(String URLM){

        requestQueue=Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URLM, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONArray jsonArray = null;
                try {
                    jsonArray = response.getJSONArray("especialidad");
                    for(int i=0;i<jsonArray.length();i++){
                        JSONObject jsonObject=jsonArray.getJSONObject(i);
                        String nombreM=jsonObject.optString("nombre");

                        medicosList.add(nombreM);
                        medicosAdapter= new ArrayAdapter<>(AsignarCitaPac.this, android.R.layout.simple_list_item_1,medicosList);
                        spnMed.setAdapter(medicosAdapter);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonObjectRequest);
    }


    public void MostrarConsul(String URLC){

        requestQueue=Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URLC ,null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONArray jsonArray = null;
                try {
                    jsonArray = response.getJSONArray("consultorio");
                    for(int i=0;i<jsonArray.length();i++){
                        JSONObject jsonObject=jsonArray.getJSONObject(i);
                        String nombreM=jsonObject.optString("nombre");
                        consulList.add(nombreM);
                        consulAdapter= new ArrayAdapter<>(AsignarCitaPac.this, android.R.layout.simple_list_item_1,consulList);
                        spnCon.setAdapter(consulAdapter);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonObjectRequest);
    }
    public void mostrarIdMed(String nombre){

        String url ="http://192.168.0.21/clinica_service/especialidad/selectId.php?nombre="+nombre;
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONArray jsonArray=null;
                try {
                    jsonArray = response.getJSONArray("especialidad");
                    for(int i=0; i<jsonArray.length();i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String idmedico = jsonObject.optString("idmedico");
                        txtidmed.setText(idmedico);

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
    public void mostrarIdConsult(String nombre){

        String url ="http://192.168.0.21/clinica_service/consultorio/readByName.php?name="+nombre;
        StringRequest stringRequest=
                new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                    @Override


                    public void onResponse(String response) {
                        String aux=response.trim();
                        txtidespec.setText(aux);

                    }

                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();

                    }
                });
        requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
    public void mostrarIdPaciente(){

        String url ="http://192.168.0.21/clinica_service/paciente/readId.php?mail="+email;
        StringRequest stringRequest=
                new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                    @Override


                    public void onResponse(String response) {
                          String aux=response.trim();
                          txtidpac.setText(aux);

                    }

                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();

                    }
                });
        requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
    public void AsignarCitaP(String URL){



            StringRequest stringRequest=
                        new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            String aux= response.toString().trim();
                           int op=Integer.parseInt(aux);
                           if(op == 1){
                               Toast.makeText(AsignarCitaPac.this,"Cita creada correctamente..",Toast.LENGTH_SHORT).show();
                               Intent intent = new Intent(getApplicationContext(), MenuPaciente.class);
                               startActivity(intent);

                           }else{
                               Toast.makeText(AsignarCitaPac.this,"Error al crear la cita..",Toast.LENGTH_SHORT).show();
                               Intent intent = new Intent(getApplicationContext(), MenuPaciente.class);
                               startActivity(intent);
                           }

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_SHORT).show();
                        }
                    }){
                        @Override
                        protected Map<String,String> getParams() throws AuthFailureError {
                            Map<String,String> parametros= new HashMap<String, String>();
                            parametros.put("fecha",txtFecha.getText().toString());//ya
                            parametros.put("hora",spnHoras.getSelectedItem().toString());//ya
                            parametros.put("medico_idmedico",txtidmed.getText().toString());
                            parametros.put("paciente_idpaciente",txtidpac.getText().toString());
                            parametros.put("consultorio_idconsultorio",txtidespec.getText().toString());
                            parametros.put("estado","1");//ya


                            return parametros;
                        }
                    };
            requestQueue= Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);




    }


}




