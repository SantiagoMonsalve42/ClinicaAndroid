package com.example.clinica_app.medico;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.clinica_app.MainActivity;
import com.example.clinica_app.R;
import com.example.clinica_app.login.checkAsk;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class atenderCita extends AppCompatActivity implements View.OnClickListener {
    String hora=null, fullname=null,fecha=null,consultorio=null;
    String email=null;
    String idpaciente=null,idcita=null;
    RequestQueue requestQueue;
    TextView nombretxt,fechatxt,horatxt,consultoriotxt,idcitatxt,idpacientetxt;
    Button finalizarC;
    EditText peso,altura,motivos,alergias,medicamentos,antep,antef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atender_cita);
        requestQueue= Volley.newRequestQueue(this);
        //traemos los datos necesarios para hacer la consulta SQL incial
        hora=getIntent().getStringExtra("hora");
        SharedPreferences prefs = getSharedPreferences("datosLogin",   Context.MODE_PRIVATE);
        email = prefs.getString("correo", "");
        //asigancion de elementos XML
        nombretxt=(TextView)findViewById(R.id.txt_nombrecita);
        fechatxt=(TextView)findViewById(R.id.txt_fechacita);
        horatxt=(TextView)findViewById(R.id.txt_horacita);
        consultoriotxt=(TextView)findViewById(R.id.txt_consultoriocita);
        idcitatxt=(TextView)findViewById(R.id.txt_idcita);
        idpacientetxt=(TextView)findViewById(R.id.txt_idpaciente);
        mostrarDatos();
        //botones y acciones
        finalizarC=(Button)findViewById(R.id.btn_finalizarconsulta);
        finalizarC.setOnClickListener(this);
        //asignacion elementos xml
        peso=(EditText)findViewById(R.id.txt_peso);
        altura=(EditText)findViewById(R.id.txt_altura);
        motivos=(EditText)findViewById(R.id.txt_motivos);
        alergias=(EditText)findViewById(R.id.txt_alergias);
        medicamentos=(EditText)findViewById(R.id.txt_medicamentos);
        antep=(EditText)findViewById(R.id.txt_antpersonales);
        antef=(EditText)findViewById(R.id.txt_antfamiliares);
    }
    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.btn_finalizarconsulta){
            if(peso.getText().toString().isEmpty() ||altura.getText().toString().isEmpty() ||motivos.getText().toString().isEmpty()
                    ||alergias.getText().toString().isEmpty() ||medicamentos.getText().toString().isEmpty() ||antep.getText().toString().isEmpty()
                    ||antef.getText().toString().isEmpty()){
                Toast.makeText(getApplicationContext(), "No dejes campos vacios..", Toast.LENGTH_SHORT).show();
            }else{
                actualizarEstadoCita();
            }
        }
    }
    public void ingresarDatosConsulta(){
        String url ="http://192.168.0.21/clinica_service/historiaclinica/create.php";
        StringRequest stringRequest=
                new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String aux=null;
                        String op= response.toString();
                        aux=op.trim();
                        if(aux.equalsIgnoreCase("1")){
                            Toast.makeText(getApplicationContext(), "Cita atendida correctamente..", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), MenuMedico.class);
                            startActivity(intent);
                        }else{
                            Toast.makeText(getApplicationContext(), "No se pudo actualizar el estado de la cita..", Toast.LENGTH_SHORT).show();
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
                        parametros.put("peso",peso.getText().toString());
                        parametros.put("altura",altura.getText().toString());
                        parametros.put("motivo_consulta",motivos.getText().toString());
                        parametros.put("enfermedades",motivos.getText().toString());
                        parametros.put("alergias",alergias.getText().toString());
                        parametros.put("medicamentos",medicamentos.getText().toString());
                        parametros.put("antecedentes_personales",antep.getText().toString());
                        parametros.put("antecedentes_familiares",antef.getText().toString());
                        parametros.put("paciente_idpaciente",idpacientetxt.getText().toString());
                        return parametros;
                    }
                };
        requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
    public void actualizarEstadoCita(){
        String url ="http://192.168.0.21/clinica_service/cita/update.php";
        StringRequest stringRequest=
                new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String aux=null;
                        String op= response.toString();
                        aux=op.trim();
                        if(aux.equalsIgnoreCase("1")){
                            ingresarDatosConsulta();
                        }else{
                            Toast.makeText(getApplicationContext(), "No se pudo actualizar el estado de la cita..", Toast.LENGTH_SHORT).show();
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
                        parametros.put("idcita",idcitatxt.getText().toString());
                        parametros.put("fecha",fechatxt.getText().toString());
                        parametros.put("hora",horatxt.getText().toString());
                        parametros.put("estado","0");
                        return parametros;
                    }
                };
        requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }
    public void mostrarDatos(){

        String url ="http://192.168.0.21/clinica_service/cita/verCita.php?idmedico="+email+"&hora="+hora;
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONArray jsonArray=null;
                try {
                    jsonArray = response.getJSONArray("cita");
                    for(int i=0; i<jsonArray.length();i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        idpaciente = jsonObject.optString("idpaciente");
                        idcita=jsonObject.optString("idcita");
                        String apellido = jsonObject.optString("apellido");
                        String nombre = jsonObject.optString("nombre");
                        fullname=nombre+" "+apellido;
                        fecha = jsonObject.optString("fecha");
                        consultorio = jsonObject.optString("consult");
                        nombretxt.setText(fullname);
                        fechatxt.setText(fecha);
                        horatxt.setText(hora);
                        consultoriotxt.setText(consultorio);
                        idcitatxt.setText(idcita);
                        idpacientetxt.setText(idpaciente);

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