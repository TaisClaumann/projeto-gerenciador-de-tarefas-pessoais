package com.example.gerenciadordetarefas.domain.service.impl;

import com.example.gerenciadordetarefas.domain.entity.Usuario;
import com.example.gerenciadordetarefas.domain.exceptions.RegistroJaCadastradoException;
import com.example.gerenciadordetarefas.domain.exceptions.RegistroNaoEncontradoException;
import com.example.gerenciadordetarefas.domain.service.UsuarioService;
import com.example.gerenciadordetarefas.mock.MockFactory;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class UsuarioServiceImplTest {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private MockFactory mockFactory;

    private static final String EMAIL_USUARIO = "teste@email.com";
    private static final String MENSAGEM_ERRO_404 = "Usuario com ID " + EMAIL_USUARIO + " não encontrado";

    private Usuario usuario;
    private Usuario usuarioRetornado;

    @BeforeEach
    void setUp() {
        usuario = mockFactory.fabricarUsuario(EMAIL_USUARIO);
    }

    @Nested
    class Dado_um_usuario_inexistente {

        @Nested
        class Quando_salvar_com_todos_os_campos_obrigatorios_preenchidos {

            @BeforeEach
            void setUp() {
                usuarioRetornado = usuarioService.salvar(usuario);
            }

            @Test
            void Entao_deve_salvar_usuario_com_sucesso() {
                assertNotNull(usuarioRetornado);
                assertEquals(usuario.getEmail(), usuarioRetornado.getEmail());
                assertEquals(usuario.getNome(), usuarioRetornado.getNome());
                assertTrue(usuarioRetornado.isAtivo());
            }
        }

        @Nested
        class Quando_buscar_por_email {

            @Test
            void Entao_nao_deve_encontrar_usuario() {
                RegistroNaoEncontradoException exception = assertThrows(
                        RegistroNaoEncontradoException.class,
                        () -> usuarioService.buscarPorId(usuario.getEmail())
                );

                assertEquals(MENSAGEM_ERRO_404, exception.getMessage());
            }
        }

        @Nested
        class Quando_alterar {

            @Test
            void Entao_deve_gerar_erro() {
                RegistroNaoEncontradoException exception = assertThrows(
                        RegistroNaoEncontradoException.class,
                        () -> usuarioService.atualizar(usuario.getEmail(), usuario)
                );

                assertEquals(MENSAGEM_ERRO_404, exception.getMessage());
            }
        }

        @Nested
        class Quando_excluir {

            @Test
            void Entao_deve_gerar_erro() {
                RegistroNaoEncontradoException exception = assertThrows(
                        RegistroNaoEncontradoException.class,
                        () -> usuarioService.excluir(usuario.getEmail())
                );

                assertEquals(MENSAGEM_ERRO_404, exception.getMessage());
            }
        }
    }

    @Nested
    class Dado_um_usuario_existente {

        @Nested
        class Quando_ativo {

            @BeforeEach
            void setUp() {
                usuario.setAtivo(true);
                usuarioService.salvar(usuario);
            }

            @Nested
            class Quando_salvar_novo_usuario_com_mesmo_email {

                private String mensagemErroEsperada;

                @BeforeEach
                void setUp() {
                    mensagemErroEsperada = "Usuario com ID " + usuario.getEmail() + " já existente";
                }

                @Test
                void Entao_deve_gerar_erro() {
                    RegistroJaCadastradoException exception = assertThrows(
                            RegistroJaCadastradoException.class,
                            () -> usuarioService.salvar(UsuarioServiceImplTest.this.usuario)
                    );

                    assertEquals(mensagemErroEsperada, exception.getMessage());
                }
            }

            @Nested
            class Quando_buscar_por_email {

                @BeforeEach
                void setUp() {
                    usuarioRetornado = usuarioService.buscarPorId(usuario.getEmail());
                }

                @Test
                void Entao_deve_retornar_usuario() {
                    assertNotNull(usuarioRetornado);
                    assertEquals(usuario.getEmail(), usuarioRetornado.getEmail());
                    assertEquals(usuario.getNome(), usuarioRetornado.getNome());
                }
            }

            @Nested
            class Quando_alterar {

                @BeforeEach
                void setUp() {
                    usuario.setNome("Nome atualizado");
                    usuarioRetornado = usuarioService.atualizar(usuario.getEmail(), usuario);
                }

                @Test
                void Entao_deve_atualizar_usuario() {
                    assertNotNull(usuarioRetornado);
                    assertEquals(usuario.getEmail(), usuarioRetornado.getEmail());
                    assertEquals(usuario.getNome(), usuarioRetornado.getNome());
                }
            }

            @Nested
            class Quando_inativar {

                @BeforeEach
                void setUp() {
                    usuarioService.excluir(usuario.getEmail());
                    usuarioRetornado = usuarioService.buscarPorId(usuario.getEmail());
                }

                @Test
                void Entao_usuario_deve_ser_inativado() {
                    assertFalse(usuarioRetornado.isAtivo());
                }
            }

            @Nested
            class Quando_listar_todos_os_usuarios {

                private Page<Usuario> usuariosRetornados;

                @BeforeEach
                void setUp() {
                    usuariosRetornados = usuarioService.listarTodos(PageRequest.of(0, 10));
                }

                @Test
                void Entao_deve_retornar_pagina_com_usuarios() {
                    assertNotNull(usuariosRetornados);
                    assertEquals(1, usuariosRetornados.getTotalElements());
                }
            }
        }

        @Nested
        class Quando_inativo {

            @BeforeEach
            void setUp() {
                usuarioService.salvar(usuario);
                usuarioService.excluir(usuario.getEmail());
            }

            @Nested
            class Quando_salvar_novo_usuario_com_mesmo_email {

                private String mensagemErroEsperada;

                @BeforeEach
                void setUp() {
                    mensagemErroEsperada = "Usuario com ID " + usuario.getEmail() + " já existente";
                }

                @Test
                void Entao_deve_gerar_erro() {
                    RegistroJaCadastradoException exception = assertThrows(
                            RegistroJaCadastradoException.class,
                            () -> usuarioService.salvar(UsuarioServiceImplTest.this.usuario)
                    );

                    assertEquals(mensagemErroEsperada, exception.getMessage());
                }
            }

            @Nested
            class Quando_buscar_por_email {

                @BeforeEach
                void setUp() {
                    usuarioRetornado = usuarioService.buscarPorId(usuario.getEmail());
                }

                @Test
                void Entao_deve_retornar_usuario() {
                    assertNotNull(usuarioRetornado);
                    assertEquals(usuario.getEmail(), usuarioRetornado.getEmail());
                    assertEquals(usuario.getNome(), usuarioRetornado.getNome());
                }
            }

            @Nested
            class Quando_alterar {

                @BeforeEach
                void setUp() {
                    usuario.setNome("Nome atualizado");
                    usuarioRetornado = usuarioService.atualizar(usuario.getEmail(), usuario);
                }

                @Test
                void Entao_deve_atualizar_usuario() {
                    assertNotNull(usuarioRetornado);
                    assertEquals(usuario.getEmail(), usuarioRetornado.getEmail());
                    assertEquals(usuario.getNome(), usuarioRetornado.getNome());
                }
            }

            @Nested
            class Quando_inativar {

                @BeforeEach
                void setUp() {
                    usuarioService.excluir(usuario.getEmail());
                    usuarioRetornado = usuarioService.buscarPorId(usuario.getEmail());
                }

                @Test
                void Entao_usuario_deve_ser_inativado() {
                    assertFalse(usuarioRetornado.isAtivo());
                }
            }

            @Nested
            class Quando_listar_todos_os_usuarios {

                private Page<Usuario> usuariosRetornados;

                @BeforeEach
                void setUp() {
                    usuariosRetornados = usuarioService.listarTodos(PageRequest.of(0, 10));
                }

                @Test
                void Entao_deve_retornar_pagina_com_usuarios() {
                    assertNotNull(usuariosRetornados);
                    assertEquals(1, usuariosRetornados.getTotalElements());
                }
            }
        }
    }
}
