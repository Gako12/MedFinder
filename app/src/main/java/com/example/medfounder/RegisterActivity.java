package com.example.medfounder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static com.example.medfounder.LoginActivity.EXTRA_SENHA;

public class RegisterActivity extends AppCompatActivity {

    TextView textView;
    EditText usernameBox, passwordBox, nome, sobrenome;
    Spinner spinner, spinner2;
    Button confirmar;
    boolean i = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        final DAO dao = new DAO(getApplicationContext());

        usernameBox = findViewById(R.id.username);
        passwordBox = findViewById(R.id.password);
        nome = findViewById(R.id.Nome);
        sobrenome = findViewById(R.id.Sobrenome);
        confirmar = findViewById(R.id.registrar);
        textView = findViewById(R.id.textView2);

        spinner = findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.tiposDeConta, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner2 = findViewById(R.id.spinner2);
        List<String> nomesHospital = dao.pegarNomeHosp();
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, nomesHospital);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(spinnerAdapter);

        usernameBox.setText(getIntent().getStringExtra(LoginActivity.EXTRA_USUARIO));
        passwordBox.setText(getIntent().getStringExtra(EXTRA_SENHA));

        confirmar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(!(usernameBox.getText().toString().equals("") && passwordBox.getText().toString().equals("")
                        && nome.getText().toString().equals("") && sobrenome.getText().toString().equals(""))) {

                    if (dao.getNumberOfRows("contas") > 0) {
                        if (dao.checarContas(usernameBox.getText().toString())) {
                            if(i && (spinner.getSelectedItemPosition() == 1 || spinner.getSelectedItemPosition() == 2)) {

                                spinner2.setVisibility(spinner2.VISIBLE);
                                textView.setVisibility(textView.VISIBLE);

                                Toast.makeText(getApplicationContext(), "Selecione seu hospital", Toast.LENGTH_SHORT).show();

                                i = false;
                            }
                            else { registrar(dao); }
                        }
                        else {
                            Toast.makeText(getApplicationContext(), "Este nome de usuário já está sendo utilizado", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else { registrar(dao); }

                }
                else {
                    Toast.makeText(getApplicationContext(), "Por favor preencha todos os campos", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


    private void registrar(DAO dao) {

        if(!dao.pegarNomeHosp().isEmpty()) {
            dao.insereContas(usernameBox.getText().toString(), passwordBox.getText().toString(),
                    (nome.getText().toString() + " " + sobrenome.getText().toString()),
                    dao.pegarNomeHosp().get(spinner2.getSelectedItemPosition()), spinner.getSelectedItemPosition() + 1);
        }
        else {
            dao.insereContas(usernameBox.getText().toString(), passwordBox.getText().toString(),
                    (nome.getText().toString() + " " + sobrenome.getText().toString()),
                    "", spinner.getSelectedItemPosition() + 1);
        }

        if(dao.getNumberOfRows("hospitalPorConta") > 0) {
            if (dao.checarHPC(usernameBox.getText().toString(), dao.pegarNomeHosp().get(spinner2.getSelectedItemPosition()))) {
                if (spinner.getSelectedItemPosition() == 1 || spinner.getSelectedItemPosition() == 2) {

                    dao.adicionarHPC(usernameBox.getText().toString(), dao.pegarNomeHosp().get(spinner2.getSelectedItemPosition()));

                }
            }
        }

        Intent it = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(it);

    }

}

