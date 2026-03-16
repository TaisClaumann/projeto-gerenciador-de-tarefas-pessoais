package com.example.gerenciadordetarefas.domain.service.impl;

import com.example.gerenciadordetarefas.domain.entity.Tarefa;
import com.example.gerenciadordetarefas.domain.repository.TarefaRepository;
import com.example.gerenciadordetarefas.domain.service.AbstractService;
import com.example.gerenciadordetarefas.domain.service.TarefaService;
import com.example.gerenciadordetarefas.domain.service.UsuarioService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class TarefaServiceImpl extends AbstractService<Tarefa, Long, TarefaRepository> implements TarefaService {

    private final UsuarioService usuarioService;

    public TarefaServiceImpl(TarefaRepository tarefaRepository, UsuarioService usuarioService) {
        super(tarefaRepository, Tarefa.class);
        this.usuarioService = usuarioService;
    }

    @Override
    public Page<Tarefa> buscarPorUsuario(String email, Pageable pageable) {
        usuarioService.buscarPorId(email);
        return repository.findByUsuarioEmail(email, pageable);
    }
}
