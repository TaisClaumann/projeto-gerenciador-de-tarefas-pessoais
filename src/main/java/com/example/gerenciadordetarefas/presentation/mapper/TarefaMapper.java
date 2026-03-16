package com.example.gerenciadordetarefas.presentation.mapper;

import com.example.gerenciadordetarefas.domain.entity.Tarefa;
import com.example.gerenciadordetarefas.presentation.dto.TarefaDto;
import org.springframework.stereotype.Component;

@Component
public class TarefaMapper {

    private final UsuarioMapper usuarioMapper;

    public TarefaMapper(UsuarioMapper usuarioMapper) {
        this.usuarioMapper = usuarioMapper;
    }

    public Tarefa toEntity(TarefaDto dto) {
        return Tarefa.builder()
                .titulo(dto.getTitulo())
                .descricao(dto.getDescricao())
                .status(dto.getStatus())
                .prioridade(dto.getPrioridade())
                .usuario(usuarioMapper.toEntity(dto.getUsuario()))
                .build();
    }

    public TarefaDto toResponseDto(Tarefa tarefa) {
        return TarefaDto.builder()
                .id(tarefa.getId())
                .titulo(tarefa.getTitulo())
                .descricao(tarefa.getDescricao())
                .status(tarefa.getStatus())
                .prioridade(tarefa.getPrioridade())
                .usuario(usuarioMapper.toResponseDto(tarefa.getUsuario()))
                .build();
    }
}
