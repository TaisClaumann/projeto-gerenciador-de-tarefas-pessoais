package com.example.gerenciadordetarefas.presentation.controller;

import com.example.gerenciadordetarefas.domain.entity.Tarefa;
import com.example.gerenciadordetarefas.domain.entity.Usuario;
import com.example.gerenciadordetarefas.domain.exceptions.RegistroNaoEncontradoException;
import com.example.gerenciadordetarefas.domain.service.TarefaService;
import com.example.gerenciadordetarefas.mock.MockFactory;
import com.example.gerenciadordetarefas.presentation.dto.TarefaDto;
import com.example.gerenciadordetarefas.presentation.dto.UsuarioResponseDto;
import com.example.gerenciadordetarefas.presentation.mapper.TarefaMapper;
import com.example.gerenciadordetarefas.presentation.mapper.UsuarioMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TarefaController.class)
@Import({TarefaMapper.class, UsuarioMapper.class, MockFactory.class})
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class TarefaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TarefaService tarefaService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockFactory mockFactory;

    @Autowired
    private TarefaMapper tarefaMapper;

    private static final String ROTA = "/tarefas";
    private static final Long TAREFA_ID = 1L;
    private static final String EMAIL_USUARIO = "teste@email.com";
    private static final String MENSAGEM_ERRO_404 = "Tarefa com ID " + TAREFA_ID + " não encontrado";

    @Nested
    class Dado_uma_tarefa {

        private TarefaDto tarefaDto;
        private Tarefa tarefa;
        private ResultActions resultActions;

        @BeforeEach
        void setUp() {
            Usuario usuario = mockFactory.fabricarUsuario(EMAIL_USUARIO);
            tarefa = mockFactory.fabricarTarefa(TAREFA_ID, usuario);
            tarefaDto = tarefaMapper.toDto(tarefa);
        }

        @Nested
        class Quando_salvar_com_todos_os_atributos_obrigatorios_preenchidos {

            @BeforeEach
            void setUp() throws Exception {
                when(tarefaService.salvar(any(Tarefa.class))).thenReturn(tarefa);
                String json = objectMapper.writeValueAsString(tarefaDto);

                resultActions = mockMvc.perform(post(ROTA)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json));
            }

            @Test
            void Entao_deve_salvar_tarefa_com_sucesso() throws Exception {
                resultActions.andExpect(status().isOk())
                        .andExpect(jsonPath("$.id").value(TAREFA_ID))
                        .andExpect(jsonPath("$.titulo").value("Título"))
                        .andExpect(jsonPath("$.status").value("EM_ANDAMENTO"))
                        .andExpect(jsonPath("$.prioridade").value("MEDIA"));

                verify(tarefaService).salvar(any(Tarefa.class));
            }
        }

        @Nested
        class Quando_salvar_sem_titulo {

            @BeforeEach
            void setUp() throws Exception {
                tarefaDto.setTitulo(null);

                String json = objectMapper.writeValueAsString(tarefaDto);
                resultActions = mockMvc.perform(post(ROTA)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json));
            }

            @Test
            void Entao_deve_gerar_erro() throws Exception {
                resultActions.andExpect(status().isBadRequest())
                        .andExpect(jsonPath("$.erro").value("Título é obrigatório"));
                verify(tarefaService, never()).salvar(any());
            }
        }

        @Nested
        class Quando_salvar_sem_status {

            @BeforeEach
            void setUp() throws Exception {
                tarefaDto.setStatus(null);

                String json = objectMapper.writeValueAsString(tarefaDto);
                resultActions = mockMvc.perform(post(ROTA)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json));
            }

            @Test
            void Entao_deve_gerar_erro() throws Exception {
                resultActions.andExpect(status().isBadRequest())
                        .andExpect(jsonPath("$.erro").value("Status é obrigatório"));
                verify(tarefaService, never()).salvar(any());
            }
        }

        @Nested
        class Quando_salvar_sem_prioridade {

            @BeforeEach
            void setUp() throws Exception {
                tarefaDto.setPrioridade(null);

                String json = objectMapper.writeValueAsString(tarefaDto);
                resultActions = mockMvc.perform(post(ROTA)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json));
            }

            @Test
            void Entao_deve_gerar_erro() throws Exception {
                resultActions.andExpect(status().isBadRequest())
                        .andExpect(jsonPath("$.erro").value("Prioridade é obrigatória"));
                verify(tarefaService, never()).salvar(any());
            }
        }

        @Nested
        class Quando_salvar_sem_usuario {

            @BeforeEach
            void setUp() throws Exception {
                tarefaDto.setUsuario(null);

                String json = objectMapper.writeValueAsString(tarefaDto);
                resultActions = mockMvc.perform(post(ROTA)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json));
            }

            @Test
            void Entao_deve_gerar_erro() throws Exception {
                resultActions.andExpect(status().isBadRequest())
                        .andExpect(jsonPath("$.erro").value("Usuário é obrigatório"));
                verify(tarefaService, never()).salvar(any());
            }
        }

        @Nested
        class Quando_buscar_por_id {

            @BeforeEach
            void setUp() throws Exception {
                when(tarefaService.buscarPorId(TAREFA_ID))
                        .thenThrow(new RegistroNaoEncontradoException("Tarefa", TAREFA_ID));

                resultActions = mockMvc.perform(get(ROTA + "/" + TAREFA_ID));
            }

            @Test
            void Entao_nao_deve_encontrar_tarefa() throws Exception {
                resultActions.andExpect(status().isNotFound())
                        .andExpect(jsonPath("$.erro").value(MENSAGEM_ERRO_404));

                verify(tarefaService).buscarPorId(TAREFA_ID);
            }
        }

        @Nested
        class Quando_alterar {

            @BeforeEach
            void setUp() throws Exception {
                when(tarefaService.atualizar(eq(TAREFA_ID), any(Tarefa.class)))
                        .thenThrow(new RegistroNaoEncontradoException("Tarefa", TAREFA_ID));

                String json = objectMapper.writeValueAsString(tarefaDto);
                resultActions = mockMvc.perform(put(ROTA + "/" + TAREFA_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json));
            }

            @Test
            void Entao_nao_deve_encontrar_tarefa() throws Exception {
                resultActions.andExpect(status().isNotFound())
                        .andExpect(jsonPath("$.erro").value(MENSAGEM_ERRO_404));

                verify(tarefaService).atualizar(eq(TAREFA_ID), any(Tarefa.class));
            }
        }

        @Nested
        class Quando_excluir {

            @BeforeEach
            void setUp() throws Exception {
                doThrow(new RegistroNaoEncontradoException("Tarefa", TAREFA_ID))
                        .when(tarefaService).excluir(TAREFA_ID);

                resultActions = mockMvc.perform(delete(ROTA + "/" + TAREFA_ID));
            }

            @Test
            void Entao_nao_deve_encontrar_tarefa() throws Exception {
                resultActions.andExpect(status().isNotFound())
                        .andExpect(jsonPath("$.erro").value(MENSAGEM_ERRO_404));

                verify(tarefaService).excluir(TAREFA_ID);
            }
        }

        @Nested
        class Quando_listar_tarefas_por_usuario {

            @BeforeEach
            void setUp() throws Exception {
                Page<Tarefa> page = new PageImpl<>(List.of(tarefa));
                when(tarefaService.buscarPorUsuario(eq(EMAIL_USUARIO), any(PageRequest.class))).thenReturn(page);

                resultActions = mockMvc.perform(get(ROTA)
                        .param("email", EMAIL_USUARIO)
                        .param("page", "0")
                        .param("size", "10"));
            }

            @Test
            void Entao_deve_retornar_pagina_de_tarefas() throws Exception {
                resultActions.andExpect(status().isOk())
                        .andExpect(jsonPath("$.content[0].id").value(TAREFA_ID))
                        .andExpect(jsonPath("$.content[0].titulo").value("Título"));

                verify(tarefaService).buscarPorUsuario(eq(EMAIL_USUARIO), any(PageRequest.class));
            }
        }

        @Nested
        class Quando_listar_todas_as_tarefas {

            @BeforeEach
            void setUp() throws Exception {
                Page<Tarefa> page = new PageImpl<>(List.of(tarefa));
                when(tarefaService.listarTodos(any(PageRequest.class))).thenReturn(page);

                resultActions = mockMvc.perform(get(ROTA)
                        .param("page", "0")
                        .param("size", "10"));
            }

            @Test
            void Entao_deve_retornar_pagina_de_tarefas() throws Exception {
                resultActions.andExpect(status().isOk())
                        .andExpect(jsonPath("$.content[0].id").value(TAREFA_ID))
                        .andExpect(jsonPath("$.content[0].titulo").value("Título"));

                verify(tarefaService).listarTodos(any(PageRequest.class));
            }
        }
    }
}
