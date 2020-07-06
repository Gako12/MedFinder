package com.example.medfounder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MarcarConsulta extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    public static final String EXTRA_USUARIO = "com.example.busca.example.EXTRA_USUARIO";
    public static final String EXTRA_NOMEHOSPITAL = "com.example.busca.example.EXTRA_NOME";

    Spinner spinner;
    Button calendario, relogio, confirmar, checar;
    String dataCalendario, horarioRelogio;

    Context mContext = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marcar_consulta);

        final DAO dao = new DAO(getApplicationContext());

        spinner = findViewById(R.id.spinnermedico);

        List<String> nomesMedicos = dao.pegarMedicosPorHospital(getIntent().getStringExtra(Funcoes.EXTRA_NOMEHOSPITAL));
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, nomesMedicos);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);

        calendario = findViewById(R.id.buttonCalendario);
        relogio = findViewById(R.id.buttonRelogio);
        confirmar = findViewById(R.id.confirmar);
        checar = findViewById(R.id.checar);

        Calendar c = Calendar.getInstance();
        final int hour = c.get(Calendar.HOUR);
        final int minute = c.get(Calendar.MINUTE);

        calendario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(), "Calendário");
            }
        });
        relogio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(mContext, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        horarioRelogio = (hourOfDay + ":" + minute);
                    }
                }, hour, minute, android.text.format.DateFormat.is24HourFormat(mContext));
                timePickerDialog.show();
            }
        });
        confirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dao.insereConsultas("Consulta com: " + spinner.getSelectedItem() + " no dia: " + dataCalendario + " as " + horarioRelogio
                        , dao.pegarId(getIntent().getStringExtra(EscolherConversa.EXTRA_USUARIO)), getIntent().getStringExtra(EscolherConversa.EXTRA_NOMEHOSPITAL)
                            , spinner.getSelectedItem().toString().substring(7, spinner.getSelectedItem().toString().length()));
                Toast.makeText(getApplicationContext(), "Pedido de consulta enviado", Toast.LENGTH_SHORT).show();
            }
        });
        checar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(getApplicationContext(), ChecarConsultas.class);
                it.putExtra(EXTRA_NOMEHOSPITAL, getIntent().getStringExtra(Funcoes.EXTRA_NOMEHOSPITAL));
                it.putExtra(EXTRA_USUARIO, getIntent().getStringExtra(Funcoes.EXTRA_USUARIO));
                startActivity(it);
            }
        });

    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        dataCalendario = DateFormat.getDateInstance().format(c.getTime());
    }

}