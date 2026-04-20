package com.example.gerenciadordetarefas.presentation.dto;

import com.example.gerenciadordetarefas.domain.entity.enums.Prioridade;
import com.example.gerenciadordetarefas.domain.entity.enums.Status;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TarefaDto {

    private Long id;
    
    @NotBlank(message = "Título é obrigatório")
    private String titulo;
    
    private String descricao;
    
    @NotNull(message = "Status é obrigatório")
    private Status status;
    
    @NotNull(message = "Prioridade é obrigatória")
    private Prioridade prioridade;
    
    @NotNull(message = "Usuário é obrigatório")
    private UsuarioResponseDto usuario;
}
