package com.example.medfounder;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class info extends AppCompatActivity {

    TextView nome, end, conv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        nome = findViewById(R.id.nome);
        end = findViewById(R.id.end);
        conv = findViewById(R.id.conv);

        nome.setText(getIntent().getStringExtra(Funcoes.EXTRA_NOMEHOSPITAL));
        end.setText(getIntent().getStringExtra(Funcoes.EXTRA_END));
        conv.setText(getIntent().getStringExtra(Funcoes.EXTRA_CONV));
    }
}