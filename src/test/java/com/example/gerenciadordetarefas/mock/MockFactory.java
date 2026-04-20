package com.example.gerenciadordetarefas.mock;

import com.example.gerenciadordetarefas.domain.entity.Tarefa;
import com.example.gerenciadordetarefas.domain.entity.Usuario;
import com.example.gerenciadordetarefas.domain.entity.enums.Prioridade;
import com.example.gerenciadordetarefas.domain.entity.enums.Status;
import com.example.gerenciadordetarefas.presentation.dto.TarefaDto;
import com.example.gerenciadordetarefas.presentation.dto.UsuarioResponseDto;
import com.example.gerenciadordetarefas.presentation.mapper.TarefaMapper;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicLong;

@Component
public class MockFactory {

    private final AtomicLong sequencial = new AtomicLong(1);

    public Long novoSequencial() {
        return sequencial.getAndIncrement();
    }

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
