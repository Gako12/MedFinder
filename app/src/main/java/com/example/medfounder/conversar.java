package com.example.medfounder;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.example.medfounder.objetos.Mensagens;
import com.example.medfounder.objetos.hospBD;

import java.util.ArrayList;
import java.util.List;

public class conversar extends AppCompatActivity {

    EditText chatBox;
    ImageButton sendButton;
    ListView bolhasDeMensagem;
    hospBD hospBD = new hospBD();
    ArrayList<Mensagens> mensagens;
    int idConversador;
    int idMensageiro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversar);

        final DAO dao = new DAO(getApplicationContext());

        chatBox = findViewById(R.id.chatBox);
        sendButton = findViewById(R.id.sendButton);
        bolhasDeMensagem = findViewById(R.id.mensagens);

        if(dao.pegarTipo(getIntent().getStringExtra(EscolherConversa.EXTRA_USUARIO)) == 1
                && Integer.parseInt(getIntent().getStringExtra(EscolherConversa.EXTRA_POSICAO)) != 0) {
            String nomeConversador = getIntent().getStringExtra(EscolherConversa.EXTRA_NOMECONVERSA);
            nomeConversador = nomeConversador.substring(7, nomeConversador.length());
            idConversador = dao.pegarId(dao.nomeMedicoParaUsuario(nomeConversador));
            idMensageiro = dao.pegarId(getIntent().getStringExtra(EscolherConversa.EXTRA_USUARIO));
        }
        else if(dao.pegarTipo(getIntent().getStringExtra(EscolherConversa.EXTRA_USUARIO)) == 1) {
            idConversador = 0;
            idMensageiro = dao.pegarId(getIntent().getStringExtra(EscolherConversa.EXTRA_USUARIO));
        }
        else if (dao.pegarTipo(getIntent().getStringExtra(EscolherConversa.EXTRA_USUARIO)) == 2) {
            String usuarioCliente = dao.nomeClienteParaUsuario(getIntent().getStringExtra(EscolherConversa.EXTRA_NOMECONVERSA));
            idConversador = dao.pegarId(usuarioCliente);
            idMensageiro = dao.pegarId(getIntent().getStringExtra(EscolherConversa.EXTRA_USUARIO));
        }
        else if (dao.pegarTipo(getIntent().getStringExtra(EscolherConversa.EXTRA_USUARIO)) == 3) {
            String usuarioCliente = dao.nomeClienteParaUsuario(getIntent().getStringExtra(EscolherConversa.EXTRA_NOMECONVERSA));
            idConversador = dao.pegarId(usuarioCliente);
            idMensageiro = 0;
        }

        // recebe os dados do hospital selecionado
        final String nome = getIntent().getStringExtra(Funcoes.EXTRA_NOMEHOSPITAL);

        mensagens = dao.getMessages(idMensageiro, idConversador, nome);
        final ArrayAdapter adapter = new ArrayAdapterChat(getApplicationContext(), android.R.layout.simple_list_item_1, mensagens);

        if(!mensagens.isEmpty()) bolhasDeMensagem.setAdapter(adapter);

        chatBox.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (((keyCode == KeyEvent.KEYCODE_ENTER)) && event.getAction() == KeyEvent.ACTION_DOWN) {
                    return sendMessage(mensagens, dao, nome, adapter);
                }
                return false;
            }
        });

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 sendMessage(mensagens, dao, nome, adapter);
                }
        });

    }


    private boolean sendMessage(ArrayList<Mensagens> mensagens, DAO dao, String nome, ArrayAdapter<String> adapter) {

        if(!chatBox.getText().toString().equals("")) {

            dao.insereMensagens(chatBox.getText().toString(), nome, idMensageiro, idConversador);

            List<String> tempMensagens = dao.getMessagesInString(idMensageiro, nome);
            Mensagens m = new Mensagens(tempMensagens.get(tempMensagens.size()-1), true);
            mensagens.add(m);

            dao.close();

            bolhasDeMensagem.setAdapter(adapter);

            chatBox.setText("");
            return true;
        }

        return false;
    }


}
