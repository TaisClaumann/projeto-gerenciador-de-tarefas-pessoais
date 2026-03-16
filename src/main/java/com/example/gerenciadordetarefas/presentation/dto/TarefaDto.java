package com.example.gerenciadordetarefas.presentation.dto;

import com.example.gerenciadordetarefas.domain.entity.enums.Prioridade;
import com.example.gerenciadordetarefas.domain.entity.enums.Status;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TarefaDto {

    private Long id;
    private String titulo;
    private String descricao;
    private Status status;
    private Prioridade prioridade;
    private UsuarioResponseDto usuario;
}
