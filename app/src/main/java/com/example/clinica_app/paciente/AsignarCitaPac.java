package com.example.clinica_app.paciente;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class AsignarCitaPac extends AppCompatActivity implements View.OnClickListener {
    Button btnAgigC;
    Button btnfecha;
    Button btnhora;

    RadioButton rbtnMG;
    RadioButton rbtnCard;
    RadioButton rbtnHema;
    RadioButton rbtnDerm;
    RadioButton rbtnNeuro;
    RadioButton rbtnGine;
    RadioButton rbtnOdon;

    RadioButton rbtnOp;
    TextView txtFecha;
    TextView txtTime;
    TextView txtSelM;

    RequestQueue requestQueue;

    Spinner spnMed;
    ArrayList<String> medicosList = new ArrayList<>();
    ArrayAdapter<String> medicosAdapter;

    Spinner spnCon;
    ArrayList<String> consulList = new ArrayList<>();
    ArrayAdapter<String> consulAdapter;

    int aa,mm,dd,hh,min; //TIEMPO Y FECHA


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asignar_cita_pac);
        txtFecha=(TextView)findViewById(R.id.txtDate);
        txtTime=(TextView)findViewById(R.id.txtTime);
        txtSelM= (TextView) findViewById(R.id.txtSelMed);

        btnAgigC= (Button)findViewById(R.id.btnAgendarCitaP);
        btnfecha= (Button)findViewById(R.id.btnDate);
        btnhora= (Button)findViewById(R.id.btnTime);

        rbtnMG=(RadioButton)findViewById(R.id.rbtnMGeneral);
        rbtnCard=(RadioButton)findViewById(R.id.rbtnCardio);
        rbtnHema=(RadioButton)findViewById(R.id.rbtnHemato);
        rbtnDerm=(RadioButton)findViewById(R.id.rbtnDerma);
        rbtnNeuro=(RadioButton)findViewById(R.id.rbtnNeuro);
        rbtnGine=(RadioButton)findViewById(R.id.rbtnGine);
        rbtnOdon=(RadioButton)findViewById(R.id.rtbtnOdonto);
        spnMed=(Spinner)findViewById(R.id.spnMed);
        spnCon=(Spinner)findViewById(R.id.spnCon);
        btnAgigC.setOnClickListener(this);
        btnfecha.setOnClickListener(this);
        btnhora.setOnClickListener(this);

        rbtnMG.setChecked(true);
        MostrarMedico("https://192.168.0.21/clinica_service/cita/selectMed.php");
        MostrarConsul("https://192.168.0.21/clinica_service/cita/selectCon.php");

    }

    @Override
    public void onClick(View v) {
        if(R.id.btnAgendarCitaP==v.getId()){

            AsignarCitaP("https://192.168.0.21/clinica_service/cita/create.php");
        }
        if(R.id.btnDate==v.getId()){

            final Calendar calendar =Calendar.getInstance();
            dd=calendar.get(Calendar.DAY_OF_MONTH);
            mm=calendar.get(Calendar.MONTH);
            aa=calendar.get(Calendar.YEAR);


            DatePickerDialog datePickerDialog= new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    txtFecha.setText(dayOfMonth+"-" + (month)+1+ "-"+year);
                }
            },aa,mm,dd);
            datePickerDialog.show();

        }
        if(R.id.btnTime==v.getId()){
            final Calendar calendar =Calendar.getInstance();
            hh=calendar.get(Calendar.HOUR_OF_DAY);
            min=calendar.get(Calendar.MINUTE);

            TimePickerDialog timePickerDialog= new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    txtTime.setText(hourOfDay+":"+minute);
                }
            },hh,min,false);
            timePickerDialog.show();
        }

    }

    public void MostrarMedico(String URLM){

        requestQueue=Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URLM, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONArray jsonArray = null;
                try {
                    jsonArray = response.getJSONArray("medico");
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


    public void AsignarCitaP(String URL){
        String estadoC="Asignada";

        if( txtFecha.getText().toString().isEmpty() || txtTime.getText().toString().isEmpty() ){
            Toast.makeText(getApplicationContext(), "No deje ningún campo vacio", Toast.LENGTH_SHORT).show();
        }
        else{
            String espe="";
            if(rbtnMG.isChecked())
            {
                espe="Medicina General";
            }
            else if(rbtnCard.isChecked()){
                espe="Cardiología";
            }
            else if(rbtnDerm.isChecked()){
                espe="Dermatología";
            }
            else if(rbtnHema.isChecked()){
                espe="Hematología";
            }
            else if(rbtnGine.isChecked()){
                espe="Ginecología";
            }
            else if(rbtnOdon.isChecked()){
                espe="Odontología";
            }

            String finalEspe = espe;
            StringRequest stringRequest=
                        new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            Toast.makeText(getApplicationContext(), "SE HA REGISTRADO SATISFACTORIAMENTE", Toast.LENGTH_SHORT).show();
                            String aux= response.toString();

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
                            parametros.put("fecha",txtFecha.getText().toString());
                            parametros.put("hora",txtTime.getText().toString());
                            parametros.put("medico_idmedico",txtSelM.getText().toString());
                            parametros.put("paciente_idpaciente",txtSelM.getText().toString());
                            parametros.put("consultorio_idconsultorio",txtSelM.getText().toString());
                            parametros.put("especialidad", finalEspe);
                            parametros.put("estado",estadoC);


                            return parametros;
                        }
                    };
            requestQueue= Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);


        }

    }


}




