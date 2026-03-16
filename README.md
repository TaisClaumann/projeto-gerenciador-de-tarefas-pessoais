# 🗂️ Sistema de Gestão de Usuários e Tarefas

Aplicação backend desenvolvida com **Spring Boot** para revisão e prática de conceitos essenciais de sistemas reais, como **autenticação**, **segurança**, **CRUD**, **boas práticas**, **testes** e **arquitetura em camadas**.

A ideia é funcionar como um **mini Trello / To-Do List**, onde cada usuário gerencia suas próprias tarefas de forma segura.

---

## 🧱 Base do Projeto

- **Spring Boot**
    - Auto-configuração
    - Profiles
    - `application.yml`
- **Spring Data JPA**
    - CRUD com banco de dados
- **Hibernate**
    - Entidades
    - Relacionamentos
- **Banco em memória (H2)**
    - Para testes rápidos
---

## 🔐 Segurança

- **Spring Security**
    - Autenticação e autorização
- **JWT (JSON Web Token)**
    - Login stateless
- **UserDetailsService**
- **PasswordEncoder**
- **Roles e Authorities**
- **Filtro de segurança customizado**
    - Intercepta requisições
    - Valida token JWT

---

## ✅ Validação e Boas Práticas
- **Bean Validation (javax.validation / jakarta.validation)**
    - `@NotNull`
    - `@Email`
    - `@Pattern`
    - Validações customizadas
- **Tratamento global de exceções**
    - `@ControllerAdvice`
    - Respostas padronizadas (400, 401, 403, 404, etc)
- **DTOs**
    - Separação entre entidades e API
- **MapStruct ou ModelMapper**
    - Conversão entre DTOs e entidades

---

## 🧪 Testes

- **JUnit 5**
- **Mockito**
- Testes de:
    - Serviço (com mocks)
    - Repositório (usando H2)
    - Controller (`@WebMvcTest`)
    - Integração (subindo o contexto Spring)
- **Cobertura de código com Jacoco**

---

## 🌐 API REST

- Controllers REST com `@RestController`
- Uso de `ResponseEntity`
- Status HTTP adequados
- Paginação e filtros com Spring Data
- Documentação com **Swagger / OpenAPI**

---

## 🚀 Extras (nível avançado)

- **Spring Cache**
    - Cache em memória com `@Cacheable`
- **Spring Scheduler**
    - Tarefas agendadas
- **Spring Events**
    - Eventos internos da aplicação
- **Spring AOP**
    - Logs
    - Auditoria
    - Performance
- Upload e download de arquivos
- Integração com APIs externas
    - `RestTemplate` ou `WebClient`

---

## 🧱 Arquitetura: Clean-ish (pragmática, com JPA no domínio)”

```text
controller  → recebe as requisições HTTP
service     → regras de negócio
repository  → acesso ao banco de dados
dto         → objetos de entrada e saída da API
exception   → tratamento global de erros
