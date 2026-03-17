package com.example.gerenciadordetarefas.domain.service.impl;

import com.example.gerenciadordetarefas.domain.entity.Usuario;
import com.example.gerenciadordetarefas.domain.exceptions.RegistroJaCadastradoException;
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
    public Usuario salvar(Usuario usuario) {
        repository.findById(usuario.getEmail()).ifPresent(u -> {
            throw new RegistroJaCadastradoException(Usuario.class.getSimpleName(), usuario.getEmail());
        });
        return super.salvar(usuario);
    }

    @Override
    public void excluir(String email) {
        Usuario usuario = buscarPorId(email);
        usuario.setAtivo(false);
        repository.save(usuario);
    }
}
