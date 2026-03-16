package com.example.gerenciadordetarefas.presentation.dto;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioRequestDto {

    private String nome;
    private String email;
    private String senha;
    private boolean ativo;
}
