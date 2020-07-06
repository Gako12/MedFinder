package com.example.medfounder;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.medfounder.objetos.Mensagens;
import com.example.medfounder.objetos.hospBD;

import java.util.ArrayList;
import java.util.List;

public class DAO extends SQLiteOpenHelper {

    public DAO(Context context) {
        super(context, "banco", null, 3);
    } // declara o nome do banco de dados, e a versão (IMPORTANTE: quando vc atualiza a versão, todos os dados contidos nas versões anteriores são deletados)

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE hospital(convenio TEXT, nome TEXT, endereco TEXT, imagem INTEGER);"; //cria a table hosptital dentro do banco de dados com o nome "banco"
        db.execSQL(sql); // diz que essa string é um comando de sql

        String sql2 = "CREATE TABLE contas(usuario TEXT, senha TEXT, nome TEXT, tipo INT);"; //cria a table com as informações das contas
        db.execSQL(sql2);

        String sql3 = "CREATE TABLE mensagens(contaMensageira INT, contaReceptora INT, nomeHospital TEXT, mensagens TEXT);"; //cria a table com as informações do hospital por conta dentro do banco de dados com o nome "banco"
        db.execSQL(sql3);

        String sql4 = "CREATE TABLE hospitalPorConta(usuario TEXT, nomeHospital TEXT);"; //cria a table com as informações do hospital por conta dentro do banco de dados com o nome "banco"
        db.execSQL(sql4);

        String sql5 = "CREATE TABLE medicoPorPaciente(usuarioMedico TEXT, usuarioPaciente TEXT);"; //cria a table com as informações do hospital por conta dentro do banco de dados com o nome "banco"
        db.execSQL(sql5);

        String sql6 = "CREATE TABLE consultas(conta INT, nomeHospital TEXT, consulta TEXT, status TEXT, nomeDoMedico TEXT);";
        db.execSQL(sql6);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) { // checa a versão e caso ela for nova deleta os dados da tabela anterior, assim começando uma nova
        String sql = "DROP TABLE IF EXISTS hospital;";
        db.execSQL(sql);
        onCreate(db);

        String sql2 = "DROP TABLE IF EXISTS mensagens;";
        db.execSQL(sql2);
        onCreate(db);
    }

    // método que insere os hospitais no banco de dados (activity listabd)
    public void insereHospital(hospBD hospBD) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues dados = new ContentValues();

        dados.put("convenio", hospBD.getConvenio());
        dados.put("nome", hospBD.getNome());
        dados.put("endereco", hospBD.getEndereco());

        db.insert("hospital", null, dados);
    }

    // busca os hospitais para mostrar na tela (activity listabd) --- não sei se realmente é necessário
    public List<hospBD> buscaHospital() {

        SQLiteDatabase db = getReadableDatabase();
        String sql = "SELECT * FROM hospital;";
        Cursor c = db.rawQuery(sql, null);

        List<hospBD> hospbds = new ArrayList<hospBD>();

        while (c.moveToNext()) {
            hospBD hospBD = new hospBD();
            hospBD.setConvenio(c.getString(c.getColumnIndex("convenio")));
            hospBD.setNome(c.getString(c.getColumnIndex("nome")));
            hospBD.setEndereco(c.getString(c.getColumnIndex("endereco")));
            hospbds.add(hospBD);
        }
        return hospbds;

    }

    // busca as informações dos hospitais para colocar dentro do array de hospitais (activity busca) - tem como parametro o convênio desejado, porém pode conter mais condições
    public List<hospBD> buscaHospInf(String convenio) {
        SQLiteDatabase db = getReadableDatabase();
        String sql = "SELECT * FROM hospital WHERE convenio = '" + convenio + "';";
        Cursor c = db.rawQuery(sql, null);

        List<hospBD> hospnome = new ArrayList<hospBD>();

        // percorre os dados enquanto é possível
        while (c.moveToNext()) {
            hospBD hospBD = new hospBD();
            hospBD.setNome(c.getString(c.getColumnIndex("nome")));
            hospBD.setEndereco(c.getString(c.getColumnIndex("endereco")));
            hospnome.add(hospBD);
        }
        return hospnome;
    }

    // retorna uma lista com os nomes dos hospitais
    public List<String> pegarNomeHosp() {
        SQLiteDatabase db = getWritableDatabase();
        Cursor a1 =  db.rawQuery("SELECT nome FROM hospital", null);

        a1.moveToFirst();

        List<String> nomesHospital = new ArrayList<String>();

        if (getNumberOfRows("hospital") == 0) { db.close(); a1.close(); return nomesHospital; }

        try {
            do {
                nomesHospital.add(a1.getString(a1.getColumnIndex("nome")));
            } while (a1.moveToNext());
        }
        finally {
            db.close();
            if (!a1.isClosed()) a1.close();
        }

        return nomesHospital;
    }

    // Retorna o nome do hospital relacionado a uma conta de médico ou funcionário
    public String pegarNomeDeHospitalPorConta(String username, DAO dao) {
        SQLiteDatabase db = getWritableDatabase();
        Cursor a1 =  db.rawQuery("SELECT usuario, nomeHospital FROM hospitalPorConta", null);

        a1.moveToFirst();

        try {
            do {
                if (username.equals(a1.getString(a1.getColumnIndex("usuario")))) {
                    if(dao.pegarTipo(username) != 1) {
                        return a1.getString(a1.getColumnIndex("nomeHospital"));
                    }
                }
            } while (a1.moveToNext());

        } finally {
            db.close();
            if (!a1.isClosed()) a1.close();
        }

        return "";
    }

    // Retorna o endereço do hospital buscando pelo nome
    public String pegarEndDeHospital(String nomeHospital) {
        SQLiteDatabase db = getWritableDatabase();
        Cursor a1 =  db.rawQuery("SELECT nome, endereco FROM hospital", null);

        a1.moveToFirst();

        try {
            do {
                if (nomeHospital.equals(a1.getString(a1.getColumnIndex("nome")))) {
                    return a1.getString(a1.getColumnIndex("endereco"));
                }
            } while (a1.moveToNext());

        } finally {
            db.close();
            if (!a1.isClosed()) a1.close();
        }

        return "";
    }

    // Retorna o convênio do hospital buscando pelo nome
    public String pegarConvDeHospital(String nomeHospital) {
        SQLiteDatabase db = getWritableDatabase();
        Cursor a1 =  db.rawQuery("SELECT nome, convenio FROM hospital", null);

        a1.moveToFirst();

        try {
            do {
                if (nomeHospital.equals(a1.getString(a1.getColumnIndex("nome")))) {
                    return a1.getString(a1.getColumnIndex("convenio"));
                }
            } while (a1.moveToNext());

        } finally {
            db.close();
            if (!a1.isClosed()) a1.close();
        }

        return "";
    }

    // insere mensagens do hospital por conta
    public void insereMensagens(String mensagem, String nomeHospital, int contaMensageira, int contaReceptora) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues dados = new ContentValues();

        dados.put("contaMensageira", contaMensageira);
        dados.put("contaReceptora", contaReceptora);
        dados.put("nomeHospital", nomeHospital);
        dados.put("mensagens", mensagem);

        db.insert("mensagens",null, dados);
        db.close();
    }

    // retorna uma lista de objetos de mensagem guardando cada mensagem enviada, filtrando por conta e hospital
    public ArrayList<Mensagens> getMessages(int contaMensageira, int contaReceptora, String nomeHospital) {
        SQLiteDatabase db = getWritableDatabase();
        ArrayList<Mensagens> mensagens = new ArrayList<Mensagens>();

        Cursor a1 =  db.rawQuery("SELECT * FROM mensagens", null);
        a1.moveToFirst();

        if (getNumberOfRows("mensagens") == 0) { db.close(); a1.close(); return mensagens; }

        try {
            do {

                if ( a1.getInt(a1.getColumnIndex("contaMensageira")) == contaMensageira
                        && a1.getInt(a1.getColumnIndex("contaReceptora")) == contaReceptora
                            && a1.getString(a1.getColumnIndex("nomeHospital")).equals(nomeHospital) )
                {
                    Mensagens m = new Mensagens(a1.getString(a1.getColumnIndex("mensagens")), true);
                    mensagens.add(m);
                }
                else if ( a1.getInt(a1.getColumnIndex("contaMensageira")) == contaReceptora
                        && a1.getInt(a1.getColumnIndex("contaReceptora")) == contaMensageira
                        && a1.getString(a1.getColumnIndex("nomeHospital")).equals(nomeHospital) )
                {
                    Mensagens m = new Mensagens(a1.getString(a1.getColumnIndex("mensagens")), false);
                    mensagens.add(m);
                }

            } while (a1.moveToNext());
        }
        finally {
            db.close();
            if (!a1.isClosed()) a1.close();
        }

        return mensagens;
    }

    // Retorna uma lista dos usuarios que podem receber mensagens por conta de cliente
    public List<String> conversasParaCliente(String usuario, String nomeHospital) {
        SQLiteDatabase db = getWritableDatabase();
        List<String> usuarios = new ArrayList<String>();

        Cursor a1 = db.rawQuery("SELECT * FROM medicoPorPaciente, hospitalPorConta", null);
        a1.moveToFirst();

        if (pegarTipo(usuario) == 1) {
            usuarios.add(nomeHospital);
        }

        if (getNumberOfRows("medicoPorPaciente") == 0) {
            db.close();
            a1.close();
            return usuarios;
        }
        try {
            do {

                if (a1.getString(a1.getColumnIndex("usuarioPaciente")).equals(pegarNome(usuario))
                        && ("Doutor " + pegarNome(a1.getString(a1.getColumnIndex("usuario"))))
                        .equals(a1.getString(a1.getColumnIndex("usuarioMedico")))
                        && a1.getString(a1.getColumnIndex("nomeHospital")).equals(nomeHospital)) {
                    usuarios.add(a1.getString(a1.getColumnIndex("usuarioMedico")));
                }

            } while (a1.moveToNext());
        } finally {
            db.close();
            if (!a1.isClosed()) a1.close();
        }

        return usuarios;
    }

    // Retorna uma lista dos usuarios que podem receber mensagens por conta de medico
    public List<String> conversasParaMedico(String usuario) {
        SQLiteDatabase db = getWritableDatabase();
        List<String> usuarios = new ArrayList<String>();

        Cursor a1 = db.rawQuery("SELECT * FROM medicoPorPaciente", null);
        a1.moveToFirst();

        if (getNumberOfRows("medicoPorPaciente") == 0) {
            db.close();
            a1.close();
            return usuarios;
        }

        try {
            do {

                if (a1.getString(a1.getColumnIndex("usuarioMedico")).equals(("Doutor " + pegarNome(usuario)))) {
                    usuarios.add(a1.getString(a1.getColumnIndex("usuarioPaciente")));
                }

            } while (a1.moveToNext());
        } finally {
            db.close();
            if (!a1.isClosed()) a1.close();
        }

        return usuarios;
    }

    // Retorna uma lista dos usuarios que podem receber mensagens por conta de funcionário
    public List<String> conversasParaFunc(String nomeHospital) {
        SQLiteDatabase db = getWritableDatabase();
        List<String> usuarios = new ArrayList<String>();

        Cursor a1 = db.rawQuery("SELECT * FROM hospitalPorConta", null);
        a1.moveToFirst();

        try {
            do {

                if (pegarTipo(a1.getString(a1.getColumnIndex("usuario"))) == 1
                        && a1.getString(a1.getColumnIndex("nomeHospital")).equals(nomeHospital)) {
                    usuarios.add(pegarNome(a1.getString(a1.getColumnIndex("usuario"))));
                }

            } while (a1.moveToNext());
        } finally {
            db.close();
            if (!a1.isClosed()) a1.close();
        }

        return usuarios;
    }

    // retorna uma lista das mensagens, filtrando por conta e hospital
    public List<String> getMessagesInString(int conta, String nomeHospital) {
        SQLiteDatabase db = getWritableDatabase();

        List<String> mensagens = new ArrayList<String>();

        Cursor a1 =  db.rawQuery("SELECT * FROM mensagens", null);
        a1.moveToFirst();

        if (getNumberOfRows("mensagens") == 0) { db.close(); a1.close(); return mensagens; }

        try {
            do {
                if ( a1.getInt(a1.getColumnIndex("contaMensageira")) == conta && a1.getString(a1.getColumnIndex("nomeHospital")).equals(nomeHospital) )
                {
                    mensagens.add(a1.getString(a1.getColumnIndex("mensagens")));
                }
            } while (a1.moveToNext());
        }
        finally {
            db.close();
            if (!a1.isClosed()) a1.close();
        }

        return mensagens;
    }

    // relaciona uma conta com um hospital
    public void adicionarHPC(String username, String nomeHospital) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues dados = new ContentValues();

        dados.put("usuario", username);
        dados.put("nomeHospital", nomeHospital);

        db.insert("hospitalPorConta",null, dados);
        db.close();
    }

    // relaciona um paciente com um médico
    public void adicionarMPP(String medicoUsername, String pacienteUsername) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues dados = new ContentValues();

        dados.put("usuarioMedico", medicoUsername);
        dados.put("usuarioPaciente", pacienteUsername);

        db.insert("medicoPorPaciente",null, dados);
        db.close();
    }

    // remove uma relação da tabela mpp
    public void removerMPP(String medicoUsername, String pacienteUsername) {
        SQLiteDatabase db = getWritableDatabase();
        Cursor a1 =  db.rawQuery("SELECT usuarioMedico, usuarioPaciente, rowid FROM medicoPorPaciente", null);

        a1.moveToFirst();

        try {
            do {
                if (medicoUsername.equals(a1.getString(a1.getColumnIndex("usuarioMedico"))) && pacienteUsername.equals(a1.getString(a1.getColumnIndex("usuarioPaciente")))) {
                    db.execSQL("delete from medicoPorPaciente where rowid='"+a1.getInt(a1.getColumnIndex("rowid"))+"'");
                }
            } while (a1.moveToNext());

        } finally {
            db.close();
            if (!a1.isClosed()) a1.close();
        }

    }

    // checa se a conta já relacionada com o hospital
    public boolean checarHPC(String username, String nomeHospital) {
        SQLiteDatabase db = getWritableDatabase();
        Cursor a1 =  db.rawQuery("SELECT usuario, nomeHospital FROM hospitalPorConta", null);

        a1.moveToFirst();

        if(getNumberOfRows("hospitalPorConta") == 0) { db.close(); a1.close(); return true; }

        try {
            do {
                if (username.equals(a1.getString(a1.getColumnIndex("usuario"))) && nomeHospital.equals(a1.getString(a1.getColumnIndex("nomeHospital")))) {
                    return false;
                }
            } while (a1.moveToNext());

        } finally {
            db.close();
            if (!a1.isClosed()) a1.close();
        }

        return true;
    }

    // insere contas em uma tabela
    public void insereContas(String username, String password, String nome, String nomeHospital, int tipo){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues dados = new ContentValues();

        dados.put("usuario", username);
        dados.put("senha", password);
        dados.put("nome", nome);
        dados.put("tipo", tipo);

        db.insert("contas", null, dados);

        if(tipo > 1) {
            dados.clear();
            dados.put("usuario", username);
            dados.put("nomeHospital", nomeHospital);

            db.insert("hospitalPorConta", null, dados);
        }

        db.close();
    }

    // Checa se o username já existe
    public boolean checarContas(String username) {
        SQLiteDatabase db = getWritableDatabase();
        Cursor a1 =  db.rawQuery("SELECT usuario FROM contas", null);

        a1.moveToFirst();

            try {
                do {
                    if (username.equals(a1.getString(a1.getColumnIndex("usuario")))) {
                        return false;
                    }
                } while (a1.moveToNext());

            } finally {
                db.close();
                if (!a1.isClosed()) a1.close();
            }

        return true;
    }

    // Checa se o username e a senha já existem
    public boolean checarContas(String username, String password) {
        SQLiteDatabase db = getWritableDatabase();
        Cursor a1 =  db.rawQuery("SELECT usuario, senha FROM contas", null);

        a1.moveToFirst();

        try {
            do {
                if (getNumberOfRows("contas") > 0) {
                    if (username.equals(a1.getString(a1.getColumnIndex("usuario"))) && password.equals(a1.getString(a1.getColumnIndex("senha")))) {
                        return true;
                    }
                }
            } while (a1.moveToNext());

        } finally {
            db.close();
            if (!a1.isClosed()) a1.close();
        }

        return false;
    }

    // insere consultas
    public void insereConsultas(String consulta, int conta, String nomeHospital, String nomeDoMedico) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues dados = new ContentValues();

        dados.put("conta", conta);
        dados.put("nomeHospital", nomeHospital);
        dados.put("consulta", consulta);
        dados.put("status", "Pendente");
        dados.put("nomeDoMedico", nomeDoMedico);

        db.insert("consultas",null, dados);
        db.close();
    }

    public List<String> pegarConsultas(int conta, String nomeHospital) {
        SQLiteDatabase db = getWritableDatabase();
        Cursor a1 =  db.rawQuery("SELECT * FROM consultas", null);

        List<String> consultas = new ArrayList<String>();

        a1.moveToFirst();

        if(getNumberOfRows("consultas") == 0) { db.close(); a1.close(); return consultas; }

        try {

            do {
                if (a1.getInt(a1.getColumnIndex("conta")) == conta && a1.getString(a1.getColumnIndex("nomeHospital")).equals(nomeHospital)) {
                    consultas.add(a1.getString(a1.getColumnIndex("consulta"))+" | Status: "+a1.getString(a1.getColumnIndex("status")));
                }
            } while (a1.moveToNext());

        } finally {
            db.close();
            if (!a1.isClosed()) a1.close();
        }

        return consultas;
    }

    public List<String> pegarConsultasParaFunc(String nomeHospital) {
        SQLiteDatabase db = getWritableDatabase();
        Cursor a1 =  db.rawQuery("SELECT * FROM consultas", null);

        List<String> consultas = new ArrayList<String>();

        a1.moveToFirst();

        if(getNumberOfRows("consultas") == 0) { db.close(); a1.close(); return consultas; }

        try {

            do {
                if (a1.getString(a1.getColumnIndex("nomeHospital")).equals(nomeHospital)) {
                    consultas.add(a1.getString(a1.getColumnIndex("consulta"))+" | Status: "+a1.getString(a1.getColumnIndex("status")));
                }
            } while (a1.moveToNext());

        } finally {
            db.close();
            if (!a1.isClosed()) a1.close();
        }

        return consultas;
    }

    public List<String> pegarConsultasParaMedico(String usuario) {
        SQLiteDatabase db = getWritableDatabase();
        Cursor a1 =  db.rawQuery("SELECT * FROM consultas", null);

        List<String> consultas = new ArrayList<String>();

        a1.moveToFirst();

        if(getNumberOfRows("consultas") == 0) { db.close(); a1.close(); return consultas; }

        try {

            do {
                if (a1.getString(a1.getColumnIndex("nomeDoMedico")).equals(pegarNome(usuario))) {
                    consultas.add(a1.getString(a1.getColumnIndex("consulta"))+" | Status: "+a1.getString(a1.getColumnIndex("status")));
                }
            } while (a1.moveToNext());

        } finally {
            db.close();
            if (!a1.isClosed()) a1.close();
        }

        return consultas;
    }

    public void confirmarConsulta(String consulta, String nomeHospital){
        SQLiteDatabase db = getWritableDatabase();
        Cursor a1 =  db.rawQuery("SELECT consulta, nomeHospital, rowid, conta, nomeDoMedico FROM consultas", null);
        ContentValues dados = new ContentValues();

        dados.put("status", "Confirmado");
        dados.put("nomeHospital", nomeHospital);
        dados.put("consulta", consulta);

        a1.moveToFirst();

        try {

            do {
                if (a1.getString(a1.getColumnIndex("nomeHospital")).equals(nomeHospital) && a1.getString(a1.getColumnIndex("consulta")).equals(consulta) ) {
                    dados.put("rowid", a1.getInt(a1.getColumnIndex("rowid")));
                    dados.put("conta", a1.getInt(a1.getColumnIndex("conta")));
                    dados.put("nomeDoMedico", a1.getString(a1.getColumnIndex("nomeDoMedico")));
                    db.replace("consultas", null, dados);
                }
            } while (a1.moveToNext());

        } finally {
            db.close();
            if (!a1.isClosed()) a1.close();
        }

    }

    public void negarConsulta(String consulta, String nomeHospital){
        SQLiteDatabase db = getWritableDatabase();
        Cursor a1 =  db.rawQuery("SELECT consulta, nomeHospital, rowid, conta, nomeDoMedico FROM consultas", null);

        a1.moveToFirst();

        try {

            do {
                if (a1.getString(a1.getColumnIndex("nomeHospital")).equals(nomeHospital) && (a1.getString(a1.getColumnIndex("consulta")).equals(consulta) || a1.getString(a1.getColumnIndex("consulta")).equals(consulta.substring(0, consulta.length()-2))))  {
                    db.execSQL("delete from consultas where rowid='"+a1.getInt(a1.getColumnIndex("rowid"))+"'");
                }
            } while (a1.moveToNext());

        } finally {
            db.close();
            if (!a1.isClosed()) a1.close();
        }

    }

    // Retorna o id da conta
    public int pegarId(String username) {
        SQLiteDatabase db = getWritableDatabase();
        Cursor a1 =  db.rawQuery("SELECT usuario FROM contas", null);

        a1.moveToFirst();

        try {

            do {
                if (username.equals(a1.getString(a1.getColumnIndex("usuario")))) {
                    return a1.getPosition() + 1;
                }
            } while (a1.moveToNext());

        } catch(NullPointerException npe) {
            return 0;
        } catch(RuntimeException re){
            return 0;
        } finally {
            db.close();
            if (!a1.isClosed()) a1.close();
        }

        return 0;
    }

    // Retorna o nome da conta
    public String pegarNome(String username) {
        SQLiteDatabase db = getWritableDatabase();
        Cursor a1 =  db.rawQuery("SELECT usuario, nome FROM contas", null);

        a1.moveToFirst();

        try {
            do {
                if (username.equals(a1.getString(a1.getColumnIndex("usuario")))) {
                    return a1.getString(a1.getColumnIndex("nome"));
                }
            } while (a1.moveToNext());

        } finally {
            db.close();
            if (!a1.isClosed()) a1.close();
        }

        return null;
    }

    // Retorna o tipo da conta
    public int pegarTipo(String username) {
        SQLiteDatabase db = getWritableDatabase();
        Cursor a1 =  db.rawQuery("SELECT usuario, tipo FROM contas", null);

        a1.moveToFirst();

        try {
            do {
                if (username.equals(a1.getString(a1.getColumnIndex("usuario")))) {
                    return Integer.parseInt(a1.getString(a1.getColumnIndex("tipo")));
                }
            } while (a1.moveToNext());

        } finally {
            db.close();
            if (!a1.isClosed()) a1.close();
        }

        return 0;
    }

    // Retorna lista de médicos vinculados ao hospital
    public List<String> pegarMedicosPorHospital(String nomeHospital) {
        SQLiteDatabase db = getWritableDatabase();
        Cursor a1 =  db.rawQuery("SELECT usuario, nomeHospital FROM hospitalPorConta", null);

        List<String> lista = new ArrayList<String>();

        a1.moveToFirst();

        try {
            do {
                if (nomeHospital.equals(a1.getString(a1.getColumnIndex("nomeHospital")))) {
                    if (pegarTipo(a1.getString(a1.getColumnIndex("usuario"))) == 2) {
                        lista.add("Doutor " + pegarNome(a1.getString(a1.getColumnIndex("usuario"))));
                    }
                }
            } while (a1.moveToNext());

        } finally {
            db.close();
            if (!a1.isClosed()) a1.close();
        }

        return lista;
    }

    // Retorna lista de pacientes vinculados ao hospital
    public List<String> pegarPacientesPorHospital(String nomeHospital) {
        SQLiteDatabase db = getWritableDatabase();
        Cursor a1 =  db.rawQuery("SELECT usuario, nomeHospital FROM hospitalPorConta", null);

        List<String> lista = new ArrayList<String>();

        a1.moveToFirst();

        try {
            do {
                if (nomeHospital.equals(a1.getString(a1.getColumnIndex("nomeHospital")))) {
                    if (pegarTipo(a1.getString(a1.getColumnIndex("usuario"))) == 1) {
                        lista.add(pegarNome(a1.getString(a1.getColumnIndex("usuario"))));
                    }
                }
            } while (a1.moveToNext());

        } finally {
            db.close();
            if (!a1.isClosed()) a1.close();
        }

        return lista;
    }

    // Retorna usuário apartir do nome de um medico
    public String nomeMedicoParaUsuario (String nomeMedico) {
        SQLiteDatabase db = getWritableDatabase();
        Cursor a1 =  db.rawQuery("SELECT usuario, tipo FROM contas", null);

        a1.moveToFirst();

        try {

            do {
                if (pegarTipo(a1.getString(a1.getColumnIndex("usuario"))) == 2
                        && pegarNome(a1.getString(a1.getColumnIndex("usuario"))).equals(nomeMedico)) {
                    return a1.getString(a1.getColumnIndex("usuario"));
                }
            } while (a1.moveToNext());

        } finally {
            db.close();
            if (!a1.isClosed()) a1.close();
        }

        return null;
    }

    // Retorna usuário apartir do nome de um cliente
    public String nomeClienteParaUsuario (String nomeCliente) {
        SQLiteDatabase db = getWritableDatabase();
        Cursor a1 =  db.rawQuery("SELECT usuario, tipo FROM contas", null);

        a1.moveToFirst();

        try {

            do {
                if (pegarTipo(a1.getString(a1.getColumnIndex("usuario"))) == 1
                        && pegarNome(a1.getString(a1.getColumnIndex("usuario"))).equals(nomeCliente)) {
                    return a1.getString(a1.getColumnIndex("usuario"));
                }
            } while (a1.moveToNext());

        } finally {
            db.close();
            if (!a1.isClosed()) a1.close();
        }

        return null;
    }

    // retorna o número de linhas de uma tabela
    public long getNumberOfRows(String tableName) {
        SQLiteDatabase db = getReadableDatabase();
        long count = DatabaseUtils.queryNumEntries(db, tableName);
        db.close();
        return count;
    }

}

