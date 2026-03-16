package com.example.gerenciadordetarefas.domain.service;

import com.example.gerenciadordetarefas.domain.entity.Tarefa;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TarefaService extends BaseService<Tarefa, Long> {

    Page<Tarefa> buscarPorUsuario(String email, Pageable pageable);
}
