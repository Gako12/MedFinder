package com.example.medfounder;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.medfounder.objetos.hospBD;

import java.util.ArrayList;
import java.util.List;

public class listabd extends AppCompatActivity {

    EditText editTextConv;
    EditText editTextNome;
    EditText editTextEnd;
    Button botaoSalvar;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listadados);

        editTextConv = findViewById(R.id.edtTextConv);
        editTextNome = findViewById(R.id.edtTextNome);
        editTextEnd = findViewById(R.id.edtTextEnd);
        listView = findViewById(R.id.lvHospBD);
        botaoSalvar = findViewById(R.id.botaoSalvar);

        botaoSalvar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                if(!(editTextNome.getText().toString().equals("") || editTextConv.getText().toString().equals("") || editTextEnd.getText().toString().equals(""))) {
                    DAO dao = new DAO(getApplicationContext());
                    hospBD hospBD = new hospBD();
                    hospBD.setConvenio(editTextConv.getText().toString());
                    hospBD.setNome(editTextNome.getText().toString());
                    hospBD.setEndereco(editTextEnd.getText().toString());
                    dao.insereHospital(hospBD);
                    dao.close();

                    limpaFormulario();

                } else {
                    Toast.makeText(getApplicationContext(), "Por favor preencha todos os campos.", Toast.LENGTH_SHORT).show();
                }

                DAO dao2 = new DAO(getApplicationContext());
                List<hospBD> hospBDS = dao2.buscaHospital();
                List<String> nomes = new ArrayList<String>();

                for (hospBD nomeBuscado : hospBDS) {
                    nomes.add(nomeBuscado.getConvenio());
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, nomes);
                listView.setAdapter(adapter);
            }

            private void limpaFormulario() {
                editTextConv.setText("");
                editTextNome.setText("");
                editTextNome.requestFocus();
                editTextEnd.setText("");
            }
        });

        DAO dao2 = new DAO(getApplicationContext());
        List<hospBD> hospBDS = dao2.buscaHospital();
        List<String> conv = new ArrayList<String>();
        List<String> nome = new ArrayList<String>();
        List<String> rua = new ArrayList<String>();


        for (hospBD nomeBuscado : hospBDS) {
            conv.add(nomeBuscado.getConvenio());
            nome.add(nomeBuscado.getNome());
            rua.add(nomeBuscado.getEndereco());

        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, conv);
        listView.setAdapter(adapter);

    }
}
