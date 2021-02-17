package com.example.clinica_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

public class MenuPaciente extends AppCompatActivity implements View.OnClickListener {
    Button btnSalir;
    Button btnAgendarC;
    TextView txtNombre;
    public static final String nombrePac="nombre";
    public static final int idPac=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_paciente);
        btnSalir= (Button)findViewById(R.id.btnSalirPa);
        btnAgendarC= (Button)findViewById(R.id.btnAgendarP);
        //concatenación de nombre paciente
        txtNombre=(TextView)findViewById(R.id.txtNombre);
        String nombreP= getIntent().getStringExtra("nombre");
        txtNombre.setText("Hola "+nombreP+" como vamos?");

        btnSalir.setOnClickListener(this);
        btnAgendarC.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.btnSalirPa){
            SharedPreferences preferences=getSharedPreferences("datosLogin", Context.MODE_PRIVATE);
            preferences.edit().clear().commit();
            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
            startActivity(intent);
            finish();
        }
        else if(v.getId()==R.id.btnAgendarP){

            Intent intent = new Intent(getApplicationContext(),AsignarCitaPac.class);
            startActivity(intent);
            finish();
        }
    }



}