package com.example.gerenciadordetarefas.presentation.dto;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioResponseDto {

    private String nome;
    private String email;
    private boolean ativo;
}
