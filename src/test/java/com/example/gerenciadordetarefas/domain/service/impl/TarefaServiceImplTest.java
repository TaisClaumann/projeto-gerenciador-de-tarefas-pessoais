package com.example.gerenciadordetarefas.domain.service.impl;

import com.example.gerenciadordetarefas.domain.entity.Tarefa;
import com.example.gerenciadordetarefas.domain.entity.Usuario;
import com.example.gerenciadordetarefas.domain.exceptions.RegistroNaoEncontradoException;
import com.example.gerenciadordetarefas.domain.service.TarefaService;
import com.example.gerenciadordetarefas.domain.service.UsuarioService;
import com.example.gerenciadordetarefas.mock.MockFactory;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class TarefaServiceImplTest {

    @Autowired
    private TarefaService tarefaService;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private MockFactory mockFactory;

    private Tarefa tarefa;
    private Tarefa tarefaRetornada;
    private Usuario usuario;

    @BeforeEach
    void setUp() {
        String email = "teste" + mockFactory.novoSequencial() + "@gmail.com";
        usuario = mockFactory.fabricarUsuario(email);
        usuario = usuarioService.salvar(usuario);
        tarefa = mockFactory.fabricarTarefa(null, usuario);
    }

    @Nested
    class Dado_uma_tarefa_inexistente {

        @Nested
        class Quando_buscar_tarefas_vinculadas_ao_usuario {

            private Page<Tarefa> tarefasRetornadas;

            @BeforeEach
            void setUp() {
                tarefasRetornadas = tarefaService.buscarPorUsuario(usuario.getEmail(), PageRequest.of(0, 10));
            }

            @Test
            void Entao_nao_deve_retornar_nada() {
                assertNotNull(tarefasRetornadas);
                assertEquals(0, tarefasRetornadas.getTotalElements());
                assertTrue(tarefasRetornadas.getContent().isEmpty());
            }
        }

        @Nested
        class Quando_salvar_com_todos_os_campos_obrigatorios_preenchidos {

            @BeforeEach
            void setUp() {
                tarefaRetornada = tarefaService.salvar(tarefa);
            }

            @Test
            void Entao_deve_salvar_tarefa() {
                assertNotNull(tarefaRetornada);
                assertNotNull(tarefaRetornada.getId());
                assertEquals(tarefa.getTitulo(), tarefaRetornada.getTitulo());
                assertEquals(tarefa.getPrioridade(), tarefaRetornada.getPrioridade());
                assertEquals(tarefa.getStatus(), tarefaRetornada.getStatus());
                assertEquals(tarefa.getUsuario().getEmail(), tarefaRetornada.getUsuario().getEmail());
                assertEquals(tarefa.getDescricao(), tarefaRetornada.getDescricao());
            }
        }

        @Nested
        class Quando_buscar_por_id {

            private String mensagemErroEsperada;

            @BeforeEach
            void setUp() {
                tarefa.setId(999L);
                mensagemErroEsperada = "Tarefa com ID " + tarefa.getId()  + " não encontrado";
            }

            @Test
            void Entao_nao_deve_encontrar_tarefa() {
                RegistroNaoEncontradoException exception = assertThrows(RegistroNaoEncontradoException.class,
                        () -> tarefaService.buscarPorId(tarefa.getId()));
                assertEquals(mensagemErroEsperada, exception.getMessage());
            }
        }
    }

    @Nested
    class Dado_uma_tarefa_existente {

        private Tarefa tarefaSalva;

        @BeforeEach
        void setUp() {
            tarefaSalva = tarefaService.salvar(tarefa);
        }

        @Nested
        class Quando_buscar_por_id {

            @BeforeEach
            void setUp() {
                tarefaRetornada = tarefaService.buscarPorId(tarefaSalva.getId());
            }

            @Test
            void Entao_deve_retornar_tarefa() {
                assertNotNull(tarefaRetornada);
                assertEquals(tarefaSalva.getId(), tarefaRetornada.getId());
                assertEquals(tarefaSalva.getTitulo(), tarefaRetornada.getTitulo());
            }
        }

        @Nested
        class Quando_alterar {

            private static final String TITULO_ATUALIZADO = "Título Atualizado";

            @BeforeEach
            void setUp() {
                tarefa.setTitulo(TITULO_ATUALIZADO);
                tarefaRetornada = tarefaService.atualizar(tarefaSalva.getId(), tarefa);
            }

            @Test
            void Entao_deve_alterar_com_sucesso() {
                assertNotNull(tarefaRetornada);
                assertEquals(tarefaSalva.getId(), tarefaRetornada.getId());
                assertEquals(TITULO_ATUALIZADO, tarefaRetornada.getTitulo());
            }
        }

        @Nested
        class Quando_excluir {

            @BeforeEach
            void setUp() {
                tarefaService.excluir(tarefaSalva.getId());
            }

            @Test
            void Entao_deve_excluir_tarefa() {
                RegistroNaoEncontradoException exception = assertThrows(
                        RegistroNaoEncontradoException.class,
                        () -> tarefaService.buscarPorId(tarefaSalva.getId())
                );

                assertTrue(exception.getMessage().contains("Tarefa"));
            }
        }

        @Nested
        class Quando_buscar_tarefas_por_usuario {

            private Page<Tarefa> tarefasRetornadas;

            @BeforeEach
            void setUp() {
                tarefasRetornadas = tarefaService.buscarPorUsuario(usuario.getEmail(), PageRequest.of(0, 10));
            }

            @Test
            void Entao_deve_retornar_tarefas() {
                assertNotNull(tarefasRetornadas);
                assertTrue(tarefasRetornadas.getTotalElements() >= 1);
            }
        }

        @Nested
        class Quando_listar_todas_as_tarefas {

            private Page<Tarefa> tarefasRetornadas;

            @BeforeEach
            void setUp() {
                tarefasRetornadas = tarefaService.listarTodos(PageRequest.of(0, 10));
            }

            @Test
            void Entao_deve_retornar_pagina_de_tarefas() {
                assertNotNull(tarefasRetornadas);
                assertTrue(tarefasRetornadas.getTotalElements() >= 1);
            }
        }
    }
}
