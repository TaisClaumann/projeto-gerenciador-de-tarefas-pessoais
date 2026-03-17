package com.example.gerenciadordetarefas.presentation.controller;

import com.example.gerenciadordetarefas.domain.entity.Usuario;
import com.example.gerenciadordetarefas.domain.service.UsuarioService;
import com.example.gerenciadordetarefas.presentation.dto.UsuarioRequestDto;
import com.example.gerenciadordetarefas.presentation.dto.UsuarioResponseDto;
import com.example.gerenciadordetarefas.presentation.mapper.UsuarioMapper;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final UsuarioMapper usuarioMapper;

    public UsuarioController(UsuarioService usuarioService, UsuarioMapper usuarioMapper) {
        this.usuarioService = usuarioService;
        this.usuarioMapper = usuarioMapper;
    }

    @PostMapping
    public UsuarioResponseDto salvar(@Valid @RequestBody UsuarioRequestDto usuarioRequestDto) {
        Usuario usuario = usuarioService.salvar(usuarioMapper.toEntity(usuarioRequestDto));
        return usuarioMapper.toResponseDto(usuario);
    }

    @PutMapping("/{email}")
    public UsuarioResponseDto alterar(@PathVariable("email") String email, @Valid @RequestBody UsuarioRequestDto usuarioRequestDto) {
        Usuario usuario = usuarioService.atualizar(email, usuarioMapper.toEntity(usuarioRequestDto));
        return usuarioMapper.toResponseDto(usuario);
    }

    @GetMapping("/{email}")
    public UsuarioResponseDto buscarPorId(@PathVariable String email) {
        return usuarioMapper.toResponseDto(usuarioService.buscarPorId(email));
    }

    @DeleteMapping("/{email}")
    public void inativarPorId(@PathVariable String email) {
        usuarioService.excluir(email);
    }

    @GetMapping
    public Page<UsuarioResponseDto> listarTodos(@PageableDefault(page = 0, size = 10) Pageable pageable) {
        return usuarioService.listarTodos(pageable).map(usuarioMapper::toResponseDto);
    }
}
