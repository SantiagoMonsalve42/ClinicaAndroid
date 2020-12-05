package com.example.clinica_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class RegistroPaciente extends AppCompatActivity implements View.OnClickListener {

    EditText txtnombre,txtapellido,txtcorreo,txtclave,txtcc,txttelefono,txtpreg,txtresp;
    EditText txtfechaN;
    Button btnIngresar;
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_paciente);
        txtnombre=(EditText) findViewById(R.id.edtNombre);
        txtapellido=(EditText) findViewById(R.id.edtApellido);
        txtcorreo=(EditText) findViewById(R.id.edtCorreo);
        txtclave=(EditText) findViewById(R.id.edtClave);
        txtcc=(EditText) findViewById(R.id.edtCC);
        txttelefono=(EditText) findViewById(R.id.edtTel);
        txtpreg=(EditText) findViewById(R.id.edtPregunta);
        txtresp=(EditText) findViewById(R.id.edtRespuesta);
        txtfechaN = (EditText) findViewById(R.id.edtFechaN);
        txtfechaN.setOnClickListener(this);



        btnIngresar=(Button)findViewById(R.id.btnReg);
        btnIngresar.setOnClickListener(this);





    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(R.id.edtFechaN==id){
            MostrarFecha();
        }
        if(id == R.id.btnReg) {
                RegistarUser("http://192.168.10.104/clinica_service/paciente/create.php");
        }
    }


    private void MostrarFecha() {
        DatePickerFragment Fragmento = DatePickerFragment.newInstance(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                // +1 because January is zero
                final String selectedDate = day + " / " + (month+1) + " / " + year;
                txtfechaN.setText(selectedDate);
            }
        });

        Fragmento.show(getSupportFragmentManager(), "datePicker");
    }

    public void RegistarUser(String URL){

        //Validar que no dejar datos en blanco

        if(txtnombre.getText().toString().isEmpty()){
            txtnombre.setError("DEBE ESCRIBIR SU NOMBRE");
        }else if(txtapellido.getText().toString().isEmpty()){

            txtapellido.setError("DEBE ESCRIBIR SU APELLIDO");
        }
        else if(txtcc.getText().toString().isEmpty()){

            txtcc.setError("DEBE ESCRIBIR SU IDENTIFICACION");
        }
        else if(txttelefono.getText().toString().isEmpty()){

            txttelefono.setError("DEBE ESCRIBIR SU TELEFONO");
        }
        else if(txtcorreo.getText().toString().isEmpty()){

            txtapellido.setError("DEBE ESCRIBIR SU CORREO");
        }
        else if(txtclave.getText().toString().isEmpty()){

            txtclave.setError("DEBE ESCRIBIR SU CLAVE");
        }
        else if(txtpreg.getText().toString().isEmpty()){

            txtpreg.setError("DEBE ESCRIBIR UNA PREGUNTA, PARA RECUPERAR SU CLAVE");
        }
        else if(txtresp.getText().toString().isEmpty()){

            txtresp.setError("DEBE ESCRIBIR SU RESPUESTA A LA PREGUNTA");
        }

        else{

            StringRequest stringRequest=
                    new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Toast.makeText(getApplicationContext(), "SE HA REGISTRADO SATISFACTORIAMENTE", Toast.LENGTH_SHORT).show();
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_SHORT).show();
                        }
                    }){
                        @Override
                        protected Map<String,String> getParams() throws AuthFailureError{
                            Map<String,String> parametros= new HashMap<String, String>();
                            parametros.put("nombre",txtnombre.getText().toString());
                            parametros.put("apellido",txtapellido.getText().toString());
                            parametros.put("correo",txtcorreo.getText().toString());
                            parametros.put("clave",txtclave.getText().toString());
                            parametros.put("cc",txtcc.getText().toString());
                            parametros.put("telefono",txttelefono.getText().toString());
                            parametros.put("pregunta",txtpreg.getText().toString());
                            parametros.put("respuesta",txtresp.getText().toString());
                            parametros.put("fecha_nac",txtfechaN.getText().toString());
                            return parametros;
                        }
                    };
            requestQueue= Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);
        }

    }



}