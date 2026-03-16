package com.example.gerenciadordetarefas.presentation.handler;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class StandardError {

    private LocalDateTime data;
    private Integer status;
    private String erro;
    private String url;
}
