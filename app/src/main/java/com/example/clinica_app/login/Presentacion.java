package com.example.clinica_app.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ProgressBar;

import com.example.clinica_app.MainActivity;
import com.example.clinica_app.paciente.MenuPaciente;
import com.example.clinica_app.R;

public class Presentacion extends AppCompatActivity {
ProgressBar progressBarra;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_presentacion);
        progressBarra=(ProgressBar)findViewById(R.id.progressBar);
        progressBarra.setVisibility(View.VISIBLE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SharedPreferences preferences=getSharedPreferences("datosLogin",Context.MODE_PRIVATE);
                boolean sesion= preferences.getBoolean("sesion",false);
                if(sesion){
                    Intent intent= new Intent(getApplicationContext(), MenuPaciente.class);
                    startActivity(intent);
                    finish();
                }
                else{
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();
                }

            }
        }, 2000);
    }


}