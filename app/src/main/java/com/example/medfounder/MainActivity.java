package com.example.medfounder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    public static final String EXTRA_USUARIO = "com.example.busca.example.EXTRA_USUARIO";

    private EditText edtConvenio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // seta qual xml vai ser exibido

        final DAO dao = new DAO(getApplicationContext());

        Button btnListaBD = findViewById(R.id.btnListaBD);
        Button button = findViewById(R.id.button);
        TextView textView = findViewById(R.id.textView);

        // opções de convenios
        Spinner spinner = findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.convenios, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        if (dao.pegarId(getIntent().getStringExtra(LoginActivity.EXTRA_USUARIO)) < 1) {
            Intent it = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(it);
        }
        else {
            if (getIntent().getStringExtra(LoginActivity.EXTRA_USUARIO).equals("admin")){
                btnListaBD.setVisibility(btnListaBD.VISIBLE);
                button.setVisibility(button.INVISIBLE);
                spinner.setVisibility(spinner.INVISIBLE);
                textView.setVisibility(textView.INVISIBLE);
            }
        }

    }

    // método que envia os dados da página para página de busca
    public void enviarDados (View view) {
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        String convenioSelecionado = spinner.getSelectedItem().toString();

        // intent que envia qual foi o item selecionado
        Intent intentEnviadora = new Intent(getApplicationContext(), busca.class);
        Bundle parametros = new Bundle();
        // parametros.putTIPO("chave_qualquer", nomedavariavel)
        parametros.putString("chave_convenio", convenioSelecionado);

        intentEnviadora.putExtra(EXTRA_USUARIO, getIntent().getStringExtra(LoginActivity.EXTRA_USUARIO));

        intentEnviadora.putExtras(parametros);
        startActivity(intentEnviadora);
    }

    public void entrarListaBD (View view) {
        Intent it = new Intent(getApplicationContext(), listabd.class);
        startActivity(it);
    }

}
