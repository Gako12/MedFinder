package com.example.medfounder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class Funcoes extends AppCompatActivity {
    public static final String EXTRA_NOMEHOSPITAL = "com.example.busca.example.EXTRA_NOME";
    public static final String EXTRA_END = "com.example.busca.example.EXTRA_END";
    public static final String EXTRA_CONV = "com.example.busca.example.EXTRA_CONV";
    public static final String EXTRA_USUARIO = "com.example.busca.example.EXTRA_USUARIO";

    Intent it;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_funcoes);

        final DAO dao = new DAO(getApplicationContext());

        final String nome;
        final String end;
        final String conv;
        final String nomeUsuario;

        if (getIntent().getStringExtra(busca.EXTRA_USUARIO) != null) {
            nome = getIntent().getStringExtra(busca.EXTRA_NOMEHOSPITAL);
            end = getIntent().getStringExtra(busca.EXTRA_END);
            conv = getIntent().getStringExtra(busca.EXTRA_CONV);
            nomeUsuario = getIntent().getStringExtra(busca.EXTRA_USUARIO);
        }
        else {
            nome = getIntent().getStringExtra(LoginActivity.EXTRA_NOMEHOSPITAL);
            end = getIntent().getStringExtra(LoginActivity.EXTRA_END);
            conv = getIntent().getStringExtra(LoginActivity.EXTRA_CONV);
            nomeUsuario = getIntent().getStringExtra(LoginActivity.EXTRA_USUARIO);
        }

        // cria a lista de funções e o objeto dao2
        ListView lista = (ListView) findViewById(R.id.lvFuncoes);

        ArrayList<String> funcoes = new ArrayList<String>();
        funcoes.add("Informações");

        if(dao.pegarTipo(nomeUsuario) == 1) { funcoes.add("Marcar e Checar Consultas"); }
        else { funcoes.add("Checar Consultas"); }

        if(dao.pegarTipo(nomeUsuario) == 1) { funcoes.add("Converse com seu Médico"); }
        else { funcoes.add("Converse com os Pacientes"); }

        if(dao.pegarTipo(nomeUsuario) == 3) { funcoes.add("Assimilar Médicos a Clientes"); }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, funcoes);
        lista.setAdapter(arrayAdapter);

        // vai para atividade da função selecionada
        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch(position) {

                    case 0:
                        it = new Intent(getApplicationContext(), info.class);

                        it.putExtra(EXTRA_NOMEHOSPITAL, nome);
                        it.putExtra(EXTRA_USUARIO, nomeUsuario);
                        it.putExtra(EXTRA_END, end);
                        it.putExtra(EXTRA_CONV, conv);

                        startActivity(it);
                        break;
                    case 1:
                        if(dao.pegarTipo(nomeUsuario) == 1) { it = new Intent(getApplicationContext(), MarcarConsulta.class); }
                        else { it = new Intent(getApplicationContext(), ChecarConsultas.class); }

                        it.putExtra(EXTRA_NOMEHOSPITAL, nome);
                        it.putExtra(EXTRA_USUARIO, nomeUsuario);

                        startActivity(it);
                        break;
                    case 2:
                        it = new Intent(getApplicationContext(), EscolherConversa.class);

                        it.putExtra(EXTRA_NOMEHOSPITAL, nome);
                        it.putExtra(EXTRA_USUARIO, nomeUsuario);

                        startActivity(it);
                        break;
                    case 3:
                        it = new Intent(getApplicationContext(), AssimilarMedicos.class);

                        it.putExtra(EXTRA_NOMEHOSPITAL, nome);
                        it.putExtra(EXTRA_USUARIO, nomeUsuario);

                        startActivity(it);
                        break;

                }
            }
        });


    }

}