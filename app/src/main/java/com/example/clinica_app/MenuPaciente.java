package com.example.clinica_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MenuPaciente extends AppCompatActivity implements View.OnClickListener {
    Button btnSalir;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_paciente);
        btnSalir= (Button)findViewById(R.id.btnSalirPa);
        btnSalir.setOnClickListener(this);
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
    }



}