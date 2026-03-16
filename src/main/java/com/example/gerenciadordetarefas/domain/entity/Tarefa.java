package com.example.gerenciadordetarefas.domain.entity;

import com.example.gerenciadordetarefas.domain.entity.enums.Prioridade;
import com.example.gerenciadordetarefas.domain.entity.enums.Status;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
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

    @NotBlank
    private String titulo;

    private String descricao;

    @NonNull
    @Enumerated(EnumType.STRING)
    private Status status;

    @NonNull
    @Enumerated(EnumType.STRING)
    private Prioridade prioridade;

    @NonNull
    @ManyToOne
    private Usuario usuario;
}
