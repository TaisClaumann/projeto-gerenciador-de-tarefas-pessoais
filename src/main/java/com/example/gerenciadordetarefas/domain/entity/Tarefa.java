package com.example.gerenciadordetarefas.domain.entity;

import com.example.gerenciadordetarefas.domain.entity.enums.Prioridade;
import com.example.gerenciadordetarefas.domain.entity.enums.Status;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class Tarefa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titulo;

    private String descricao;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Prioridade prioridade;

    @JoinColumn(nullable = false)
    @ManyToOne
    private Usuario usuario;
}
