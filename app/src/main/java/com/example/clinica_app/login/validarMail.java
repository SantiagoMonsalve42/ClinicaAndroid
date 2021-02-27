package com.example.clinica_app.login;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.clinica_app.MainActivity;
import com.example.clinica_app.R;

import org.json.JSONException;

public class validarMail extends AppCompatActivity implements View.OnClickListener{
    EditText txtcorreo;
    Button btnactualizar;
    String correo;
    RequestQueue requestQueue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_validar_mail);
        txtcorreo=(EditText) findViewById(R.id.txt_mail_v);
        btnactualizar=(Button)findViewById(R.id.btn_validate);
        btnactualizar.setOnClickListener(this);
        Intent intent = new Intent(getApplicationContext(), validarMail.class);
        requestQueue = Volley.newRequestQueue(this);
        Bundle miBundle = new Bundle();
        miBundle.putString("ask", "");
        miBundle.putString("mail", "");
        intent.putExtras(miBundle);

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == R.id.btn_validate){
            correo=txtcorreo.getText().toString();
            if(!correo.isEmpty()){

                ValidarMail(correo);
            }else{
                Toast.makeText(validarMail.this,"No dejes campos vacios..",Toast.LENGTH_SHORT).show();
            }
        }
    }
    public void ValidarMail(String mail){

     //String URL="https://clinica-service.000webhostapp.com/clinica_service/paciente/read.php?idpaciente="+mail;//WEB
     String URL="http://192.168.0.21/clinica_service/paciente/read.php?idpaciente="+mail;
     JsonObjectRequest jsonObjectRequest= new JsonObjectRequest(


                Request.Method.GET, URL, null,
                response -> {
                    String ask, mail1;
                    Intent intent = new Intent(getApplicationContext(), checkAsk.class);
                    System.out.println(response.toString());
                    try {

                            ask = response.getString("pregunta");
                            mail1 = response.getString("correo");
                            System.out.println(ask);
                            System.out.println(correo);
                            Bundle miBundle = new Bundle();
                            miBundle.putString("ask", ask);
                            miBundle.putString("mail", mail1);
                            intent.putExtras(miBundle);

                    } catch (JSONException e) {
                        e.printStackTrace();

                    }

                    startActivity(intent);
                }, error -> this.crearalerta()
        );
        requestQueue.add(jsonObjectRequest);

    }

public void crearalerta(){
    AlertDialog.Builder alerta= new AlertDialog.Builder(validarMail.this);
    alerta.setMessage("El correo no esta mal o no existe en el sistema..")
            .setCancelable(false)
            .setPositiveButton("Vale", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
    AlertDialog titulo= alerta.create();
    titulo.setTitle("Error al verificar email");
    titulo.show();
}
}