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
    - Para testes
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

### 🏭 MockFactory - Gerador de Dados de Teste

O projeto utiliza uma classe `MockFactory` para fabricar objetos de teste de forma padronizada e evitar conflitos entre testes.

**Principais recursos:**

- **`novoSequencial()`**: Gera números únicos e incrementais usando `AtomicLong`
  - Thread-safe para execução paralela de testes
  - Evita conflitos de chave única (emails, IDs, etc.)
  - Exemplo: `teste1@gmail.com`, `teste2@gmail.com`, `teste3@gmail.com`

**Por que AtomicLong?**
- Garante operações atômicas (indivisíveis) em ambientes multi-thread
- Evita race conditions quando múltiplos testes acessam o contador
- Performance similar ao `long` comum, mas com segurança adicional

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

---

## 🧱 Cenários de Testes BDD (Behavior-Driven Development)

### Dado: Usuário inexistente

- **Quando:** Salvar com todos os campos obrigatórios preenchidos
  **Então:** Deve salvar usuário com sucesso
  
- **Quando:** Salvar sem email
  **Então:** Deve gerar erro
  
- **Quando:** Salvar sem nome
  **Então:** Deve gerar erro
  
- **Quando:** Salvar sem senha
  **Então:** Deve gerar erro
  
- **Quando:** Salvar sem informar se esta ativo
  **Então:** Deve gerar erro

- **Quando:** Alterar  
  **Então:** Deve gerar erro 404 (usuário não encontrado)

- **Quando:** Inativar  
  **Então:** Deve gerar erro 404 (usuário não encontrado)

- **Quando:** Salvar tarefa para este usuário  
  **Então:** Deve gerar erro 404 (usuário não encontrado)
  
- **Quando:** Buscar por email
  **Então:** Deve gerar erro 404 (usuário não encontrado)  
  
### Dado: Usuário existente e ativo

- **Quando:** Salvar 
  **Então:** Deve gerar erro 400 (registro já cadastrado)

- **Quando:** Alterar  
  **Então:** Deve atualizar usuário com sucesso

- **Quando:** Inativar  
  **Então:** Deve inativar usuário com sucesso

- **Quando:** Salvar tarefa para este usuário  
  **Então:** Deve salvar tarefa

- **Quando:** Buscar por email
  **Então:** Deve retornar usuário
  
### Dado: Usuário existente e inativo

- **Quando:** Alterar  
  **Então:** Deve gerar erro 404 (usuário não encontrado)

- **Quando:** Inativar  
  **Então:** Deve gerar erro 404 (usuário não encontrado)

- **Quando:** Salvar tarefa para este usuário  
  **Então:** Deve gerar erro 404 (usuário não encontrado)

- **Quando:** Buscar por email
  **Então:** Deve gerar erro 404 (usuário não encontrado)

### Dado: Tarefa inexistente

- **Quando:** Salvar com todos os campos obrigatórios preenchidos
  **Então:** Deve salvar tarefa com sucesso
  
- **Quando:** Salvar sem título
  **Então:** Deve gerar erro
  
- **Quando:** Salvar sem status
  **Então:** Deve gerar erro
  
- **Quando:** Salvar sem prioridade
  **Então:** Deve gerar erro
  
- **Quando:** Salvar sem informar usuario
  **Então:** Deve gerar erro

- **Quando:** Alterar  
  **Então:** Deve gerar erro 404 (tarefa não encontrada)

- **Quando:** Excluir  
  **Então:** Deve gerar erro 404 (tarefa não encontrada)
  
- **Quando:** Buscar por id 
  **Então:** Deve gerar erro 404 (tarefa não encontrada)
  
### Dado: Tarefa existente

- **Quando:** Alterar com todos os campos obrigatórios preenchidos
  **Então:** Então deve alterar com sucesso

- **Quando:** Excluir  
  **Então:** Deve excluir tarefa
  
- **Quando:** Buscar por id 
  **Então:** Deve retornar tarefa

- **Quando:** Buscar tarefas vinculadas ao usuário
  **Então:** Deve retornar tarefa

### Dado: Usuario sem tarefas cadastradas

- **Quando:** Buscar tarefas vinculadas ao usuário
  **Então:** Não deve retornar nada
