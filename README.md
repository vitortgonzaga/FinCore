# FinCore

Backend financeiro educacional desenvolvido de forma incremental para estudar engenharia de software com problemas próximos de sistemas reais.

O projeto começa como um monólito modular e só recebe novas tecnologias quando existe um problema concreto que justifique seu uso.

## Fase atual

**Fase 0 — Setup e engenharia básica**

Implementado até o momento:

- Java 25 e Maven Wrapper;
- Spring Boot 4.1.1;
- Spring Web MVC;
- Spring Data JPA e Hibernate;
- PostgreSQL 18.6;
- Flyway;
- Bean Validation;
- Spring Boot Actuator;
- Testcontainers;
- teste de inicialização e health check;
- CI mínimo com GitHub Actions;
- configuração local por profile e variáveis de ambiente.

O roteiro completo está em [FinCore-Project-Guide.md](FinCore-Project-Guide.md).

## Requisitos

- JDK 25;
- Docker;
- Git.

Não é necessário instalar o Maven globalmente, pois o projeto utiliza Maven Wrapper.

## Executar com PostgreSQL temporário

Para desenvolvimento rápido, execute pela IDE:

```text
src/test/java/com/vitortgonzaga/fincore/TestFinCoreApplication.java
```

Essa classe inicia a aplicação e cria um PostgreSQL temporário com Testcontainers. O banco é removido quando a aplicação é encerrada.

## Executar com PostgreSQL local

Crie o container:

```bash
docker run \
  --name fincore-postgres \
  -e POSTGRES_DB=fincore \
  -e POSTGRES_USER=fincore \
  -e POSTGRES_PASSWORD=fincore_local \
  -p 5432:5432 \
  -d postgres:18.6
```

Inicie a aplicação com o profile local:

```bash
SPRING_PROFILES_ACTIVE=local \
DB_PASSWORD=YourPassword \
./mvnw spring-boot:run
```

Variáveis suportadas:

| Variável | Obrigatória | Padrão |
|---|---:|---|
| `DB_URL` | Não | `jdbc:postgresql://localhost:5432/fincore` |
| `DB_USERNAME` | Não | `fincore` |
| `DB_PASSWORD` | Sim | Sem padrão |

Credenciais reais não devem ser adicionadas ao repositório.

## Health check

Com a aplicação iniciada:

```bash
curl http://localhost:8080/actuator/health
```

Resposta esperada:

```json
{"status":"UP"}
```

O Actuator agrega o estado da aplicação e do datasource configurado.

## Migrations

As migrations ficam em:

```text
src/main/resources/db/migration
```

Convenção de nomes:

```text
V<versão>__<descrição>.sql
```

Uma migration aplicada não deve ser alterada. Mudanças posteriores devem ser adicionadas em uma nova versão.

## Testes

Com o Docker ativo:

```bash
./mvnw verify
```

Os testes de integração iniciam um PostgreSQL descartável, executam as migrations e carregam o contexto Spring.

## CI

O workflow em `.github/workflows/ci.yml` executa o build e os testes em pushes e pull requests para a branch principal.

## Estrutura

```text
src/
├── main/
│   ├── java/com/vitortgonzaga/fincore/
│   └── resources/
│       ├── application.properties
│       ├── application-local.properties
│       └── db/migration/
└── test/
    └── java/com/vitortgonzaga/fincore/
```

Pacotes de negócio serão criados somente quando as respectivas funcionalidades entrarem no projeto.
