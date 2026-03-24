package com.example.gerenciadordetarefas.mock;

import com.example.gerenciadordetarefas.domain.entity.Tarefa;
import com.example.gerenciadordetarefas.domain.entity.Usuario;
import com.example.gerenciadordetarefas.domain.entity.enums.Prioridade;
import com.example.gerenciadordetarefas.domain.entity.enums.Status;
import org.springframework.stereotype.Component;

@Component
public class MockFactory {

    public Tarefa fabricarTarefa(Long id, Usuario usuario) {
        return Tarefa.builder()
                .id(id)
                .usuario(usuario)
                .titulo("Título")
                .status(Status.EM_ANDAMENTO)
                .prioridade(Prioridade.MEDIA)
                .descricao("Descrição")
                .build();
    }

    public Usuario fabricarUsuario(String email) {
        return Usuario.builder()
                .email(email)
                .nome("Nome")
                .senha("Senha")
                .ativo(true)
                .build();
    }
}
