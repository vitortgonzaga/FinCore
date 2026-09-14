# FinCore — Agent Instructions

## Project context

FinCore is an educational financial backend project built with:

- Java 25
- Spring Boot
- Maven
- PostgreSQL
- JPA/Hibernate

The project is intentionally developed incrementally.

Read `PROJECT_GUIDE.md` before making architectural or structural changes.

## Primary rule

Do not skip project phases defined in `PROJECT_GUIDE.md`.

Do not implement features from future phases unless the user explicitly requests it.

## Architectural decisions

Never make significant architectural decisions silently.

Before introducing or changing:

- architectural patterns
- modules
- databases
- brokers
- caches
- frameworks
- libraries
- infrastructure
- communication protocols

present:

1. the problem;
2. viable alternatives;
3. trade-offs;
4. your recommendation.

Wait for the developer's decision when the choice is significant.

## Learning-first behavior

This project is primarily for learning backend engineering.

Do not implement important concepts entirely for the developer when doing so would remove the learning opportunity.

For important concepts:

1. explain the problem;
2. explain the relevant concept;
3. present alternatives;
4. let the developer implement the core logic when reasonable;
5. review the implementation afterward.

Boilerplate may be generated normally.

## Avoid premature complexity

Do not introduce without a concrete need:

- microservices
- Kafka
- Redis
- CQRS
- Event Sourcing
- Kubernetes
- Terraform
- additional abstraction layers
- design patterns

Always ask:

"What current problem does this solve?"

## Code quality

Prefer:

- readable Java
- explicit code
- small cohesive classes
- strong domain invariants
- appropriate tests
- clear naming

Avoid:

- unnecessary abstractions
- speculative generic code
- excessive inheritance
- premature optimization

## Testing

Tests are part of the implementation.

When modifying behavior:

- identify relevant tests;
- add or update tests when appropriate;
- run the smallest relevant test suite;
- report failures clearly.

Do not change tests merely to make failing code pass.

## Database

PostgreSQL is the source database.

Use Flyway for schema migrations.

Do not modify an existing migration after it has been considered applied.
Create a new migration instead.

Do not use database-specific behavior without explaining it.

## JPA / Hibernate

Do not hide database behavior behind JPA assumptions.

Watch for:

- N+1 queries
- lazy loading
- transaction boundaries
- cascading
- locking
- generated SQL

When relevant, explain what Hibernate is actually doing.

## Financial correctness

Financial invariants have priority over convenience.

Never use `double` or `float` for monetary values.

For changes involving transfers, ledger entries or balances, consider:

- atomicity
- concurrency
- idempotency
- consistency
- auditability

## Current phase

Before starting substantial work:

1. determine the current FinCore phase;
2. read that section of `PROJECT_GUIDE.md`;
3. stay within its scope.

If the current phase is unclear, ask the developer.

## Communication

When a task exposes a useful engineering concept, mention its proper technical name so the developer can research it.

Examples:

- lost update
- optimistic locking
- transaction isolation
- idempotency
- dual-write problem
- eventual consistency
- backpressure

Do not turn every simple task into a lecture.

## Completion

Before considering a phase complete, check the Definition of Done from `PROJECT_GUIDE.md`.

Never automatically advance to the next phase.