package com.example.clinica_app.login;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.clinica_app.R;

import org.json.JSONException;

public class checkAsk extends AppCompatActivity implements View.OnClickListener {
    EditText txt_answer;
    String mail,ask;
    TextView txt_ask;
    Button btn_submit;
    RequestQueue requestQueue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_ask);
        txt_answer=(EditText)findViewById(R.id.txt_answer);
        txt_ask=(TextView)findViewById(R.id.txt_ask);
        btn_submit=(Button) findViewById(R.id.btn_answer);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            ask = bundle.getString("ask");
            txt_ask.setText(ask);
            mail=bundle.getString("mail");
        }

        btn_submit.setOnClickListener(this);
        requestQueue = Volley.newRequestQueue(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        String answer="";
        System.out.println(mail);
        if(id == R.id.btn_answer){
            answer=txt_answer.getText().toString();
          ValidarRespuesta(mail,answer);
            System.out.println("email hoy dia es"+mail);
            }
        }


    private void ValidarRespuesta(String mail,String answer) {

        //String URL="https://clinica-service.000webhostapp.com/clinica_service/paciente/read.php?idpaciente="+mail; //WEB
       String URL="http://192.168.0.21/clinica_service/paciente/read.php?idpaciente="+mail;//local
        JsonObjectRequest jsonObjectRequest= new JsonObjectRequest(
                Request.Method.GET, URL, null,
                response -> {
                    String ans, ids;


                    try {
                        ids = response.getString("idpaciente");
                        ans = response.getString("respuesta");

                        System.out.println(ans);
                        System.out.println(ids);
                        if(answer.equalsIgnoreCase(ans)) {
                            Intent intent = new Intent(getApplicationContext(), UpdatePass.class);
                            Bundle miBundle= new Bundle();

                            miBundle.putString("id",ids);
                            intent.putExtras(miBundle);
                            startActivity(intent);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();

                    }

                }, error -> System.out.println("ERRRRRRROR11")
        );
        requestQueue.add(jsonObjectRequest);

    }
    public void crearalerta(){
        AlertDialog.Builder alerta= new AlertDialog.Builder(checkAsk.this);
        alerta.setMessage("La respuesta esta mal, vuelva a intentarlo o comuniquese con un admisntrador..")
                .setCancelable(false)
                .setPositiveButton("Vale", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        AlertDialog titulo= alerta.create();
        titulo.setTitle("Error al verificar la respuesta");
        titulo.show();
    }
}