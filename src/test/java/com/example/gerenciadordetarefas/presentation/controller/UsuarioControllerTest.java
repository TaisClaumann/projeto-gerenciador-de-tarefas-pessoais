package com.example.gerenciadordetarefas.presentation.controller;

import com.example.gerenciadordetarefas.domain.entity.Usuario;
import com.example.gerenciadordetarefas.domain.exceptions.RegistroJaCadastradoException;
import com.example.gerenciadordetarefas.domain.exceptions.RegistroNaoEncontradoException;
import com.example.gerenciadordetarefas.domain.service.UsuarioService;
import com.example.gerenciadordetarefas.mock.MockFactory;
import com.example.gerenciadordetarefas.presentation.dto.UsuarioRequestDto;
import com.example.gerenciadordetarefas.presentation.dto.UsuarioResponseDto;
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

@WebMvcTest(UsuarioController.class)
@Import({UsuarioMapper.class, MockFactory.class})
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UsuarioService usuarioService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockFactory mockFactory;

    private static final String ROTA = "/usuarios";
    private static final String EMAIL_USUARIO = "teste@email.com";
    private static final String MENSAGEM_ERRO_404 = "Usuario com ID " + EMAIL_USUARIO + " não encontrado";

    private UsuarioRequestDto usuarioRequestDto;
    private Usuario usuario;
    private ResultActions resultActions;

    @BeforeEach
    void setUp() {
        usuarioRequestDto = UsuarioRequestDto.builder()
                .email(EMAIL_USUARIO)
                .nome("Nome")
                .senha("Senha")
                .ativo(true)
                .build();

        usuario = mockFactory.fabricarUsuario(EMAIL_USUARIO);
    }

    @Nested
    class Dado_um_usuario {

        @Nested
        class Quando_salvar_com_todos_os_atributos_obrigatorios_preenchidos {

            @BeforeEach
            void setUp() throws Exception {
                when(usuarioService.salvar(any(Usuario.class))).thenReturn(usuario);
                String json = objectMapper.writeValueAsString(usuarioRequestDto);

                resultActions = mockMvc.perform(post(ROTA)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json));
            }

            @Test
            void Entao_deve_salvar_usuario_com_sucesso() throws Exception {
                resultActions.andExpect(status().isOk())
                        .andExpect(jsonPath("$.email").value(EMAIL_USUARIO))
                        .andExpect(jsonPath("$.nome").value("Nome"))
                        .andExpect(jsonPath("$.ativo").value(true));

                verify(usuarioService).salvar(any(Usuario.class));
            }
        }

        @Nested
        class Quando_salvar_sem_email {

            @BeforeEach
            void setUp() throws Exception {
                usuarioRequestDto.setEmail(null);

                String json = objectMapper.writeValueAsString(usuarioRequestDto);
                resultActions = mockMvc.perform(post(ROTA)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json));
            }

            @Test
            void Entao_deve_gerar_erro() throws Exception {
                resultActions.andExpect(status().isBadRequest())
                        .andExpect(jsonPath("$.erro").value("Erro de validação nos campos: email"));
                verify(usuarioService, never()).salvar(any());
            }
        }

        @Nested
        class Quando_salvar_sem_nome {

            @BeforeEach
            void setUp() throws Exception {
                usuarioRequestDto.setNome(null);

                String json = objectMapper.writeValueAsString(usuarioRequestDto);
                resultActions = mockMvc.perform(post(ROTA)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json));
            }

            @Test
            void Entao_deve_gerar_erro() throws Exception {
                resultActions.andExpect(status().isBadRequest());
                verify(usuarioService, never()).salvar(any());
            }
        }

        @Nested
        class Quando_salvar_sem_senha {

            @BeforeEach
            void setUp() throws Exception {
                usuarioRequestDto.setSenha(null);

                String json = objectMapper.writeValueAsString(usuarioRequestDto);
                resultActions = mockMvc.perform(post(ROTA)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json));
            }

            @Test
            void Entao_deve_gerar_erro() throws Exception {
                resultActions.andExpect(status().isBadRequest());
                verify(usuarioService, never()).salvar(any());
            }
        }

        @Nested
        class Quando_buscar_por_email {

            @BeforeEach
            void setUp() throws Exception {
                when(usuarioService.buscarPorId(EMAIL_USUARIO))
                        .thenThrow(new RegistroNaoEncontradoException("Usuario", EMAIL_USUARIO));

                resultActions = mockMvc.perform(get(ROTA + "/" + EMAIL_USUARIO));
            }

            @Test
            void Entao_nao_deve_encontrar_usuario() throws Exception {
                resultActions.andExpect(status().isNotFound())
                        .andExpect(jsonPath("$.erro").value(MENSAGEM_ERRO_404));

                verify(usuarioService).buscarPorId(EMAIL_USUARIO);
            }
        }

        @Nested
        class Quando_alterar {

            @BeforeEach
            void setUp() throws Exception {
                when(usuarioService.atualizar(eq(EMAIL_USUARIO), any(Usuario.class)))
                        .thenThrow(new RegistroNaoEncontradoException("Usuario", EMAIL_USUARIO));

                String json = objectMapper.writeValueAsString(usuarioRequestDto);
                resultActions = mockMvc.perform(put(ROTA + "/" + EMAIL_USUARIO)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json));
            }

            @Test
            void Entao_nao_deve_encontrar_usuario() throws Exception {
                resultActions.andExpect(status().isNotFound())
                        .andExpect(jsonPath("$.erro").value(MENSAGEM_ERRO_404));

                verify(usuarioService).atualizar(eq(EMAIL_USUARIO), any(Usuario.class));
            }
        }

        @Nested
        class Quando_inativar {

            @BeforeEach
            void setUp() throws Exception {
                doThrow(new RegistroNaoEncontradoException("Usuario", EMAIL_USUARIO))
                        .when(usuarioService).excluir(EMAIL_USUARIO);

                resultActions = mockMvc.perform(delete(ROTA + "/" + EMAIL_USUARIO));
            }

            @Test
            void Entao_nao_deve_encontrar_usuario() throws Exception {
                resultActions.andExpect(status().isNotFound())
                        .andExpect(jsonPath("$.erro").value(MENSAGEM_ERRO_404));

                verify(usuarioService).excluir(EMAIL_USUARIO);
            }
        }

        @Nested
        class Quando_listar_todos_os_usuarios {

            @BeforeEach
            void setUp() throws Exception {
                Page<Usuario> page = new PageImpl<>(List.of(usuario));
                when(usuarioService.listarTodos(any(PageRequest.class))).thenReturn(page);

                resultActions = mockMvc.perform(get(ROTA)
                        .param("page", "0")
                        .param("size", "10"));
            }

            @Test
            void Entao_deve_retornar_pagina_de_usuarios() throws Exception {
                resultActions.andExpect(status().isOk())
                        .andExpect(jsonPath("$.content[0].email").value(EMAIL_USUARIO))
                        .andExpect(jsonPath("$.content[0].nome").value("Nome"));

                verify(usuarioService).listarTodos(any(PageRequest.class));
            }
        }
    }
}
