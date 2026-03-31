package com.example.gerenciadordetarefas.domain.service;

import com.example.gerenciadordetarefas.domain.exceptions.RegistroNaoEncontradoException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;

public abstract class AbstractService<T, ID extends Serializable, R extends JpaRepository<T, ID>>
        implements BaseService<T, ID> {

    protected final R repository;
    private final Class<T> entityClass;

    protected AbstractService(R repository, Class<T> entityClass) {
        this.repository = repository;
        this.entityClass = entityClass;
    }

    @Override
    @Transactional
    public T salvar(T entity) {
        return repository.save(entity);
    }

    @Override
    public T buscarPorId(ID id) {
        return repository.findById(id)
                .orElseThrow(() -> new RegistroNaoEncontradoException(entityClass.getSimpleName(), id));
    }

    @Override
    public Page<T> listarTodos(Pageable pageable) {
        return repository.findAll(pageable);
    }
}
