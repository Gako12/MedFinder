package com.example.medfounder;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.List;

public class AssimilarMedicos extends AppCompatActivity {

    Spinner spinner, spinner2;
    Button adicionar, remover;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assimilar_medicos);

        final DAO dao = new DAO(getApplicationContext());

        adicionar = findViewById(R.id.adicionar);
        remover = findViewById(R.id.remover);

        spinner = findViewById(R.id.spinner);
        List<String> nomesMedicos = dao.pegarMedicosPorHospital(getIntent().getStringExtra(Funcoes.EXTRA_NOMEHOSPITAL));
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, nomesMedicos);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);

        spinner2 = findViewById(R.id.spinner2);
        List<String> nomesPaciente = dao.pegarPacientesPorHospital(getIntent().getStringExtra(Funcoes.EXTRA_NOMEHOSPITAL));
        ArrayAdapter<String> spinnerAdapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, nomesPaciente);
        spinnerAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(spinnerAdapter2);

        adicionar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dao.adicionarMPP(spinner.getSelectedItem().toString(), spinner2.getSelectedItem().toString());
                Toast.makeText(getApplicationContext(), "Adicionado com sucesso", Toast.LENGTH_SHORT).show();
            }
        });

        remover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(dao.getNumberOfRows("medicoPorPaciente") > 0) {
                    dao.removerMPP(spinner.getSelectedItem().toString(), spinner2.getSelectedItem().toString());
                    Toast.makeText(getApplicationContext(), "Removido com sucesso", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}