package com.example.medfounder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.medfounder.objetos.Hospital;
import com.example.medfounder.objetos.hospBD;

import java.util.ArrayList;
import java.util.List;

public class busca extends AppCompatActivity {
    public static final String EXTRA_NOMEHOSPITAL = "com.example.busca.example.EXTRA_NOMEHOSPITAL";
    public static final String EXTRA_END = "com.example.busca.example.EXTRA_END";
    public static final String EXTRA_CONV = "com.example.busca.example.EXTRA_CONV";
    public static final String EXTRA_USUARIO = "com.example.busca.example.EXTRA_USUARIO";

    private TextView edtparaConv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_busca);

        final DAO dao = new DAO(getApplicationContext());

        edtparaConv = (TextView) findViewById(R.id.paraConv); // declara o lugar que vai ficar escrito o convenio

        // intent que recebe os dados enviados da main
        Intent intentRecebedora = getIntent();
        Bundle parametros = intentRecebedora.getExtras();
        final String convenio = parametros.getString("chave_convenio");
        edtparaConv.setText("Para o conveio: " + convenio);

        // declara a lista de hospitais que vai ser exibida
        ListView lista = (ListView) findViewById(R.id.lvHosp);
        final ArrayList<Hospital> hospitais = adicionarHospitais();
        ArrayAdapter adapter = new HospitalAdapter(this, android.R.layout.simple_list_item_1, hospitais);
        lista.setAdapter(adapter);

        // vai para atividade com as funções do hospital selecionado
        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent it = new Intent(getApplicationContext(), Funcoes.class);

                if(dao.pegarTipo(getIntent().getStringExtra(MainActivity.EXTRA_USUARIO)) == 1
                        && dao.checarHPC(getIntent().getStringExtra(MainActivity.EXTRA_USUARIO), hospitais.get(position).getNome())) {
                    dao.adicionarHPC(getIntent().getStringExtra(MainActivity.EXTRA_USUARIO), hospitais.get(position).getNome());
                }

                it.putExtra(EXTRA_NOMEHOSPITAL, hospitais.get(position).getNome());
                it.putExtra(EXTRA_END, hospitais.get(position).getEndereco());
                it.putExtra(EXTRA_CONV, convenio);
                it.putExtra(EXTRA_USUARIO, getIntent().getStringExtra(MainActivity.EXTRA_USUARIO));

                startActivity(it);
            }
        });
    }

    // array dos hospitais que aparecerão
    private ArrayList<Hospital> adicionarHospitais() {
        ArrayList<Hospital> hospitais = new ArrayList<Hospital>();
        Intent intentRecebedora = getIntent();
        Bundle parametros = intentRecebedora.getExtras();

        String convenioSelecionado = parametros.getString("chave_convenio"); // pega qual convenio foi selecionado

        // Se o convenio selecionado for algo, retorna uma lista de hospitais daquele convenio
        if (convenioSelecionado.equals("Amil")) {
            DAO dao = new DAO(getApplicationContext()); // chama o banco de dados
            List<hospBD> hospNome = dao.buscaHospInf("Amil"); // usa o método buscanomeHosp para pegar as informações do hospital (nome, endereço) daquele convenio

            for (hospBD nomeBuscado : hospNome) { // percorre todos os hospitais que aceita aquele convenio
                String nome = nomeBuscado.getNome();
                String end = nomeBuscado.getEndereco();
                Hospital h = new Hospital(nome, end);
                hospitais.add(h);
            }

        } else if (convenioSelecionado.equals("Itaúde")) {
            DAO dao = new DAO(getApplicationContext());
            List<hospBD> hospNome = dao.buscaHospInf("Itaúde");

            for (hospBD nomeBuscado : hospNome) {
                String nome = nomeBuscado.getNome();
                String end = nomeBuscado.getEndereco();
                Hospital h = new Hospital(nome, end);
                hospitais.add(h);
            }

        } else if (convenioSelecionado.equals("Saúde Bradesco")) {
            DAO dao = new DAO(getApplicationContext());
            List<hospBD> hospnome = dao.buscaHospInf("Saúde Bradesco");

            for (hospBD nomeBuscado : hospnome) {
                String nome = nomeBuscado.getNome();
                String end = nomeBuscado.getEndereco();
                Hospital h = new Hospital(nome, end);
                hospitais.add(h);
            }

        }

        return hospitais;
    }

    // método que volta pra página inicial
    public void voltarHome(View view) {
        Intent intent2 = new Intent(getApplicationContext(), MainActivity.class); // retorna pra página da MainActivity
        startActivity(intent2);
    }

}
