package com.example.gerenciadordetarefas.presentation.controller;

import com.example.gerenciadordetarefas.domain.entity.Tarefa;
import com.example.gerenciadordetarefas.domain.service.TarefaService;
import com.example.gerenciadordetarefas.presentation.dto.TarefaDto;
import com.example.gerenciadordetarefas.presentation.mapper.TarefaMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequestMapping("/tarefas")
public class TarefaController {

    private final TarefaService tarefaService;
    private final TarefaMapper tarefaMapper;

    public TarefaController(TarefaService tarefaService, TarefaMapper tarefaMapper) {
        this.tarefaService = tarefaService;
        this.tarefaMapper = tarefaMapper;
    }

    @PostMapping
    public TarefaDto salvar(@RequestBody TarefaDto tarefaDto) {
        Tarefa tarefa = tarefaService.salvar(tarefaMapper.toEntity(tarefaDto));
        return tarefaMapper.toResponseDto(tarefa);
    }

    @GetMapping("/{id}")
    public TarefaDto buscarPorId(@PathVariable Long id) {
        return tarefaMapper.toResponseDto(tarefaService.buscarPorId(id));
    }

    @PutMapping("/{id}")
    public TarefaDto atualizar(@PathVariable Long id, @RequestBody TarefaDto tarefaDto) {
        Tarefa tarefa = tarefaService.atualizar(id, tarefaMapper.toEntity(tarefaDto));
        return tarefaMapper.toResponseDto(tarefa);
    }

    @DeleteMapping("/{id}")
    public void excluir(@PathVariable Long id) {
        tarefaService.excluir(id);
    }

    @GetMapping
    public Page<TarefaDto> listarTodos(@RequestParam(required = false) String email,
                                                 @PageableDefault(page = 0, size = 10) Pageable pageable) {
        return Objects.nonNull(email) ?
                tarefaService.buscarPorUsuario(email, pageable).map(tarefaMapper::toResponseDto) :
                tarefaService.listarTodos(pageable).map(tarefaMapper::toResponseDto);
    }
}
