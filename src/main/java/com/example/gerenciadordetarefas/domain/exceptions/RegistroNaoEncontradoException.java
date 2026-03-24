package com.example.gerenciadordetarefas.domain.exceptions;

public class RegistroNaoEncontradoException extends RuntimeException {

    public RegistroNaoEncontradoException(String entidade, Object id) {
        super(String.format("%s com ID %s não encontrado", entidade, id));
    }
}
