package com.example.gerenciadordetarefas.domain.service.impl;

import com.example.gerenciadordetarefas.domain.entity.Usuario;
import com.example.gerenciadordetarefas.domain.repository.UsuarioRepository;
import com.example.gerenciadordetarefas.domain.service.AbstractService;
import com.example.gerenciadordetarefas.domain.service.UsuarioService;
import org.springframework.stereotype.Service;

@Service
public class UsuarioServiceImpl extends AbstractService<Usuario, String, UsuarioRepository> implements UsuarioService {

    public UsuarioServiceImpl(UsuarioRepository usuarioRepository) {
        super(usuarioRepository, Usuario.class);
    }

    @Override
    public void excluir(String email) {
        Usuario usuario = buscarPorId(email);
        usuario.setAtivo(false);
        repository.save(usuario);
    }
}
