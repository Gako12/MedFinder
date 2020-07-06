package com.example.medfounder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class EscolherConversa extends AppCompatActivity {
    public static final String EXTRA_USUARIO = "com.example.busca.example.EXTRA_USUARIO";
    public static final String EXTRA_NOMEHOSPITAL = "com.example.busca.example.EXTRA_NOME";
    public static final String EXTRA_NOMECONVERSA = "com.example.busca.example.EXTRA_NOMECONVERSA";
    public static final String EXTRA_POSICAO = "com.example.busca.example.EXTRA_POSICAO";

    ListView lvConversas;
    List<String> pessoas = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_escolher_conversa);

        final DAO dao = new DAO(getApplicationContext());

        lvConversas = findViewById(R.id.lvFuncoes);

        if(dao.pegarTipo(getIntent().getStringExtra(Funcoes.EXTRA_USUARIO)) == 1) {
            pessoas = dao.conversasParaCliente(getIntent().getStringExtra(Funcoes.EXTRA_USUARIO), getIntent().getStringExtra(Funcoes.EXTRA_NOMEHOSPITAL));
        }
        else if(dao.pegarTipo(getIntent().getStringExtra(Funcoes.EXTRA_USUARIO)) == 2){
            pessoas = dao.conversasParaMedico(getIntent().getStringExtra(Funcoes.EXTRA_USUARIO));
        }
        else if(dao.pegarTipo(getIntent().getStringExtra(Funcoes.EXTRA_USUARIO)) == 3){
            pessoas = dao.conversasParaFunc(getIntent().getStringExtra(Funcoes.EXTRA_NOMEHOSPITAL));
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, pessoas);
        lvConversas.setAdapter(arrayAdapter);

        lvConversas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent it = new Intent(getApplicationContext(), conversar.class);

                it.putExtra(EXTRA_NOMEHOSPITAL, getIntent().getStringExtra(Funcoes.EXTRA_NOMEHOSPITAL));
                it.putExtra(EXTRA_USUARIO, getIntent().getStringExtra(Funcoes.EXTRA_USUARIO));
                it.putExtra(EXTRA_NOMECONVERSA, pessoas.get(position));
                it.putExtra(EXTRA_POSICAO, String.valueOf(position));

                startActivity(it);
            }
        });
    }

}