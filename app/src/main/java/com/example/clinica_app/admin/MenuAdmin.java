package com.example.clinica_app.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.clinica_app.MainActivity;
import com.example.clinica_app.R;
import com.example.clinica_app.login.UpdatePass;

public class MenuAdmin extends AppCompatActivity implements View.OnClickListener {
    Button btnSalir,btnbuscar;
    SearchView txt_buscar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_admin);
        btnbuscar= (Button)findViewById(R.id.btn_buscar);
        txt_buscar=(SearchView)findViewById(R.id.txt_buscar);
        btnbuscar.setOnClickListener(this);
        btnSalir= (Button)findViewById(R.id.btnSalirA);
        btnSalir.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.btnSalirA){
            SharedPreferences preferences=getSharedPreferences("datosLogin", Context.MODE_PRIVATE);
            preferences.edit().clear().commit();//Limpiar datos
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }
        if(v.getId()==R.id.btn_buscar){

        }
    }
}