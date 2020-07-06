package com.example.medfounder.objetos;

import java.util.List;

public class Mensagens {
    private String mensagem;
    private boolean lado;

    public Mensagens (String mensagem, boolean lado) { this.mensagem = mensagem; this.lado = lado; }

    public String getMensagem() {
        return mensagem;
    }

    public boolean getLado() { return lado; }

}
