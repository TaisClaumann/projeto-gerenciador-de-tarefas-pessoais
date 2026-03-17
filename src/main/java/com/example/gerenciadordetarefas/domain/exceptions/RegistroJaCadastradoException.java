package com.example.gerenciadordetarefas.domain.exceptions;

public class RegistroJaCadastradoException extends RuntimeException {

    public RegistroJaCadastradoException(String entidade, Object id) {
        super(String.format("Registro %s com ID %s já existente", entidade, id));
    }
}
