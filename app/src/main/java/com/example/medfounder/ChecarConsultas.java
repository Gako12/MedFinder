package com.example.medfounder;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ChecarConsultas extends AppCompatActivity {

    ListView lvconsultas;
    List<String> consultas = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checar_consultas);

        final DAO dao = new DAO(getApplicationContext());

        final String nomeHospital;
        final int tipo, conta;

        if (getIntent().getStringExtra(Funcoes.EXTRA_USUARIO) == null) {
            conta = dao.pegarId(getIntent().getStringExtra(MarcarConsulta.EXTRA_USUARIO));
            tipo = dao.pegarTipo(getIntent().getStringExtra(MarcarConsulta.EXTRA_USUARIO));
            nomeHospital = getIntent().getStringExtra(MarcarConsulta.EXTRA_NOMEHOSPITAL);
        }
        else {
            conta = dao.pegarId(getIntent().getStringExtra(Funcoes.EXTRA_USUARIO));
            tipo = dao.pegarTipo(getIntent().getStringExtra(Funcoes.EXTRA_USUARIO));
            nomeHospital = getIntent().getStringExtra(Funcoes.EXTRA_NOMEHOSPITAL);
        }

        lvconsultas = findViewById(R.id.lvConsultas);

        if (tipo == 3) { consultas = dao.pegarConsultasParaFunc(nomeHospital); }

        else if (tipo == 2) { consultas = dao.pegarConsultasParaMedico(getIntent().getStringExtra(Funcoes.EXTRA_USUARIO)); }

        else { consultas = dao.pegarConsultas(conta, nomeHospital); }

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, consultas);
        lvconsultas.setAdapter(arrayAdapter);

        if (tipo != 1) {
            lvconsultas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    showDialog(view, consultas.get(position).substring(0, consultas.get(position).length()-19), nomeHospital, dao);
                }
            });
        }
    }

    public void showDialog(View v, final String consulta, final String nomeHospital, final DAO dao) {

        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Escolha:");
        dialog.setMessage("Você deseja confirmar ou negar essa consulta?");
        dialog.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dao.confirmarConsulta(consulta, nomeHospital);
                Toast.makeText(getApplicationContext(), "Consulta confirmada", Toast.LENGTH_SHORT).show();
            }
        });
        dialog.setNegativeButton("Negar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dao.negarConsulta(consulta, nomeHospital);
                Log.d("t", consulta);
                Toast.makeText(getApplicationContext(), "Consulta negada", Toast.LENGTH_SHORT).show();
            }
        });

        dialog.create().show();
    }
}