package com.example.medfounder.objetos;

// Objeto dos Hospitais que vão ser exibidos na tela de busca
public class Hospital {

    private String nome;
    private String endereco;

    public Hospital (String nome, String endereco) {
        this.nome = nome;
        this.endereco = endereco;
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
}
