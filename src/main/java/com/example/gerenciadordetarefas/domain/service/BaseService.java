package com.example.gerenciadordetarefas.domain.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.Serializable;

public interface BaseService<T, ID extends Serializable> {

    T salvar(T entity);
    T atualizar(ID id, T entity);
    void excluir(ID id);
    T buscarPorId(ID id);
    Page<T> listarTodos(Pageable pageable);
}
