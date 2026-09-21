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
- UUID, status inicial e data de criação definidos pelo domínio;
- DTOs imutáveis com records e Bean Validation;
- contrato JSON estrito, rejeitando campos desconhecidos;
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

## Stack

- Java 25 e Maven Wrapper;
- Spring Boot 4.1.1;
- Spring Web MVC;
- Spring Data JPA e Hibernate;
- PostgreSQL 18.6;
- Flyway;
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

Para executar os testes:

```bash
./mvnw --batch-mode verify
```

Os testes de integração iniciam um PostgreSQL real e descartável, aplicam as migrations e exercitam a aplicação sem depender do banco local.

## Evolução

O desenvolvimento segue fases com experimentos práticos, registradas em [FinCore-Project-Guide.md](FinCore-Project-Guide.md). Tecnologias como mensageria, cache e infraestrutura cloud entram apenas quando o domínio apresentar a necessidade correspondente.
