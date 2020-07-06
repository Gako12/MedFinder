package com.example.medfounder.objetos;


import java.util.ArrayList;
import java.util.List;

// objeto dos hospitais que fica guardado no Banco de dados
public class hospBD {

    String nome;
    String endereco;
    String convenio;
    String conta;
    String mensagens;
    String consultas;

    public String getConvenio() {
        return convenio;
    }

    public void setConvenio(String convenio) {
        this.convenio = convenio;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getConta() {
        return conta;
    }

    public void setConta(String conta) {
        this.conta = conta;
    }

    public String getMensagens() {
        return mensagens;
    }

    public void setMensagens(String mensagens) { this.mensagens = mensagens; }

    public String getConsultas() {
        return consultas;
    }

    public void setConsultas(String consultas) {
        this.consultas = consultas;
    }

}
