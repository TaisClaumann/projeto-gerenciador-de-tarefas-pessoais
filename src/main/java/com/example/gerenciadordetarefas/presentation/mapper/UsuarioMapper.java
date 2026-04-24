package com.example.gerenciadordetarefas.presentation.mapper;

import com.example.gerenciadordetarefas.domain.entity.Usuario;
import com.example.gerenciadordetarefas.presentation.dto.UsuarioRequestDto;
import com.example.gerenciadordetarefas.presentation.dto.UsuarioResponseDto;
import org.springframework.stereotype.Component;

@Component
public class UsuarioMapper {

    public Usuario toEntity(UsuarioRequestDto dto) {
        return Usuario.builder()
                .nome(dto.getNome())
                .email(dto.getEmail())
                .senha(dto.getSenha())
                .build();
    }

    public Usuario toEntity(UsuarioResponseDto dto) {
        return Usuario.builder()
                .nome(dto.getNome())
                .email(dto.getEmail())
                .ativo(dto.isAtivo())
                .build();
    }

    public UsuarioResponseDto toResponseDto(Usuario usuario) {
        return UsuarioResponseDto.builder()
                .nome(usuario.getNome())
                .email(usuario.getEmail())
                .ativo(usuario.isAtivo())
                .build();
    }
}
