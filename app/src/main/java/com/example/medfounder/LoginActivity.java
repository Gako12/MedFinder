package com.example.medfounder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {
    public static final String EXTRA_USUARIO = "com.example.busca.example.EXTRA_USUARIO";
    public static final String EXTRA_SENHA = "com.example.busca.example.EXTRA_SENHA";
    public static final String EXTRA_NOMEHOSPITAL = "com.example.busca.example.EXTRA_NOMEHOSPITAL";
    public static final String EXTRA_END = "com.example.busca.example.EXTRA_END";
    public static final String EXTRA_CONV = "com.example.busca.example.EXTRA_CONV";

    EditText usernameBox, passwordBox;
    Button login, register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final DAO dao = new DAO(getApplicationContext());

        usernameBox = findViewById(R.id.username);
        passwordBox = findViewById(R.id.password);
        login = findViewById(R.id.login);
        register = findViewById(R.id.registrar);

        login.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                if(!(usernameBox.getText().toString().equals("") && passwordBox.getText().toString().equals(""))) {
                   if(dao.checarContas(usernameBox.getText().toString(), passwordBox.getText().toString())) {

                       if (dao.pegarTipo(usernameBox.getText().toString()) == 1) {
                           Intent it = new Intent(getApplicationContext(), MainActivity.class);
                           it.putExtra(EXTRA_USUARIO, usernameBox.getText().toString());
                           startActivity(it);
                       }
                       else {
                           Intent it = new Intent(getApplicationContext(), Funcoes.class);
                           String nomeHospital = dao.pegarNomeDeHospitalPorConta(usernameBox.getText().toString(), dao);

                           it.putExtra(EXTRA_USUARIO, usernameBox.getText().toString());
                           it.putExtra(EXTRA_NOMEHOSPITAL, nomeHospital);
                           it.putExtra(EXTRA_END, dao.pegarEndDeHospital(nomeHospital));
                           it.putExtra(EXTRA_CONV, dao.pegarConvDeHospital(nomeHospital));

                           startActivity(it);
                       }

                   }
                   else {
                       Toast.makeText(getApplicationContext(), "Nome de usuário ou senha incorretos", Toast.LENGTH_SHORT).show();
                   }
                }
                else {
                    Toast.makeText(getApplicationContext(), "Por favor preencha todos os campos", Toast.LENGTH_SHORT).show();
                }
            }
        });

        register.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent it = new Intent(getApplicationContext(), RegisterActivity.class);

                if(!(usernameBox.getText().toString() == "" && passwordBox.getText().toString() == "")) {
                    it.putExtra(EXTRA_USUARIO, usernameBox.getText().toString());
                    it.putExtra(EXTRA_SENHA, passwordBox.getText().toString());
                }

                startActivity(it);
            }
        });


    }

    @Override
    public void onBackPressed() {
    }
}
