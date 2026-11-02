# FinCore

[![CI](https://github.com/vitortgonzaga/FinCore/actions/workflows/ci.yml/badge.svg)](https://github.com/vitortgonzaga/FinCore/actions/workflows/ci.yml)
![Java](https://img.shields.io/badge/Java-25-orange)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-4.1.1-6DB33F)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-18.6-4169E1)

API REST financeira desenvolvida de forma incremental para estudar desafios reais de engenharia de backend: persistência, transações, consistência, concorrência, idempotência e observabilidade.

O projeto começa como um monólito organizado por feature e só incorpora novas tecnologias quando existe um problema concreto que justifique seu uso.

## Estado atual

**Fase 1 — Accounts (em desenvolvimento)**

- criação de contas com `POST /accounts`;
- consulta de uma conta com `GET /accounts/{id}` e listagem com `GET /accounts`;
- UUID, status inicial e data de criação definidos pelo domínio;
- DTOs imutáveis com records e Bean Validation;
- contrato JSON estrito, rejeitando campos desconhecidos;
- erros padronizados no formato Problem Details (RFC 9457);
- documentação interativa da API com Swagger UI;
- persistência PostgreSQL com JPA/Hibernate;
- schema versionado com Flyway;
- testes unitários, de repository e HTTP;
- PostgreSQL descartável nos testes com Testcontainers;
- pipeline de CI com GitHub Actions.

### Exemplo

```http
POST /accounts
Content-Type: application/json

{
  "ownerName": "Vitor"
}
```

```json
{
  "id": "2f6a4460-7c13-4f27-99db-b188f45ce552",
  "ownerName": "Vitor",
  "status": "ACTIVE",
  "createdAt": "2026-09-20T22:30:00Z"
}
```

### Erros

Os erros seguem o formato [RFC 9457 (Problem Details)](https://www.rfc-editor.org/rfc/rfc9457), com `Content-Type: application/problem+json`. O campo `type` é um código estável para o cliente tratar cada caso, e o `detail` é um texto legível que pode mudar.

| Situação | Status | `type` |
|---|---|---|
| Conta não encontrada | 404 | `/errors/account-not-found` |
| Campos inválidos no corpo | 400 | `/errors/validation` |
| Parâmetro da URL inválido (ex.: id que não é UUID) | 400 | `/errors/invalid-parameter` |
| JSON malformado ou com campo desconhecido | 400 | `/errors/malformed-request` |

Exemplo de erro de validação (`POST /accounts` com `ownerName` em branco):

```json
{
  "type": "/errors/validation",
  "title": "Invalid request",
  "status": 400,
  "detail": "Request has invalid fields",
  "errors": [
    { "field": "ownerName", "message": "must not be blank" }
  ]
}
```

## Stack

- Java 25 e Maven Wrapper;
- Spring Boot 4.1.1;
- Spring Web MVC;
- Spring Data JPA e Hibernate;
- PostgreSQL 18.6;
- Flyway;
- springdoc-openapi (Swagger UI);
- JUnit, AssertJ e Mockito;
- Testcontainers;
- GitHub Actions.

## Arquitetura atual

```text
HTTP / JSON
    ↓
AccountController
    ↓
AccountService
    ↓
AccountRepository
    ↓
JPA / Hibernate
    ↓
PostgreSQL
```

Entidades JPA não são expostas diretamente pela API. Requests e responses usam DTOs próprios, mantendo o contrato HTTP separado da persistência.

## Executar

Requisitos: **JDK 25** e **Docker**.

Com PostgreSQL temporário, execute pela IDE:

```text
src/test/java/com/vitortgonzaga/fincore/TestFinCoreApplication.java
```

Com a aplicação rodando, a documentação interativa fica em `http://localhost:8080/swagger-ui.html`.

Para executar os testes:

```bash
./mvnw --batch-mode verify
```

Os testes de integração iniciam um PostgreSQL real e descartável, aplicam as migrations e exercitam a aplicação sem depender do banco local.

## Evolução

O desenvolvimento segue fases com experimentos práticos, registradas em [FinCore-Project-Guide.md](FinCore-Project-Guide.md). Tecnologias como mensageria, cache e infraestrutura cloud entram apenas quando o domínio apresentar a necessidade correspondente.
