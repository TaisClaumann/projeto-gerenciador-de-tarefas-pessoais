package com.example.gerenciadordetarefas.domain.repository;

import com.example.gerenciadordetarefas.domain.entity.Tarefa;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TarefaRepository extends JpaRepository<Tarefa, Long> {

    Page<Tarefa> findByUsuarioEmail(String email, Pageable pageable);
}
