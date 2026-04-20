package com.example.gerenciadordetarefas.presentation.handler;

import com.example.gerenciadordetarefas.domain.exceptions.RegistroJaCadastradoException;
import com.example.gerenciadordetarefas.domain.exceptions.RegistroNaoEncontradoException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ResourceExceptionHandler {

    @ExceptionHandler(RegistroNaoEncontradoException.class)
    public ResponseEntity<StandardError> registroNaoEncontrado(RegistroNaoEncontradoException ex, HttpServletRequest request){
        StandardError error = new StandardError(
                LocalDateTime.now(), HttpStatus.NOT_FOUND.value(), ex.getMessage(), request.getRequestURI());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(RegistroJaCadastradoException.class)
    public ResponseEntity<StandardError> registroJaCadastrado(RegistroJaCadastradoException ex, HttpServletRequest request){
        StandardError error = new StandardError(
                LocalDateTime.now(), HttpStatus.BAD_REQUEST.value(), ex.getMessage(), request.getRequestURI());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<StandardError> validationError(MethodArgumentNotValidException ex, HttpServletRequest request){
        String mensagens = ex.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(", "));
        StandardError error = new StandardError(
                LocalDateTime.now(), HttpStatus.BAD_REQUEST.value(), mensagens, request.getRequestURI());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }
}
