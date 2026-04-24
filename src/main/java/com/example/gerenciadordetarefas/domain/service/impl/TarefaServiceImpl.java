package com.example.gerenciadordetarefas.domain.service.impl;

import com.example.gerenciadordetarefas.domain.entity.Tarefa;
import com.example.gerenciadordetarefas.domain.repository.TarefaRepository;
import com.example.gerenciadordetarefas.domain.service.AbstractService;
import com.example.gerenciadordetarefas.domain.service.TarefaService;
import com.example.gerenciadordetarefas.domain.service.UsuarioService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Override
    @Transactional
    public Tarefa atualizar(Long id, Tarefa tarefa) {
        buscarPorId(id);
        tarefa.setId(id);
        return repository.save(tarefa);
    }

    @Override
    @Transactional
    public void excluir(Long id) {
        repository.deleteById(id);
    }
}
