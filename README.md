# SMCP Dashboard Backend
 
Backend for a World of Warcraft guild dashboard, built to consume data from the Blizzard Game Data and Profile API and expose guild statistics, character profiles, and Mythic+ dungeon performance through a custom REST API.

## Purpose
 
This project serves the only purpose of learning:
- **Structured Java and Spring Boot learning.** It serves as a controlled environment to practice relational domain modeling, layered architecture, integration with external APIs via a reactive client, and code organization practices in a project of real scope — substantial enough to avoid being trivial, yet bounded enough to be completable.

## Tech Stack
 
- **Language:** Java 25
- **Framework:** Spring Boot (Spring Data JPA, Spring WebFlux, Spring Security OAuth2 Client)
- **Database:** PostgreSQL 16

## Current State
 
- Guild sync (guild data and roster, filtered by rank)
- Character profile sync, including Mythic+ history (seasons and runs)
- Scheduled sync of game reference data, with idempotency checks
- Paginated query endpoints for guild roster, character profile, and Mythic+ history

## Architecture
 
The project follows a conventional layered separation, with a few design decisions specific to this domain:
 
- **Parsers** are pure classes (no service injection) responsible only for transforming JSON returned by the Blizzard API into domain entities. External dependencies (game reference data lookup maps, for example) are built outside the parser and passed in as parameters.
- **Sync services** orchestrate calls to the Blizzard API, resolve game reference data lookups, and persist the result.
- **Query services** are responsible only for paginated, filtered reads, keeping the write layer (sync) and the read layer (frontend queries) decoupled — preventing the API from serving full object trees when the client only needs a slice of the data.
- **Mappers** convert JPA entities into response DTOs, avoiding exposure of internal database structure and preventing serialization cycles in bidirectional relationships.
- **Game reference data** (classes, specializations, races, affixes, Mythic+ seasons) is stored locally and synced by scheduled jobs, eliminating redundant Blizzard API calls when processing character data.
Entity relationships favor direct foreign keys over bidirectional `@OneToMany` collections, to avoid implicit loading of large volumes of historical data.


## Roadmap
 
- **Spring Security:** protect the API's own write endpoints (sync), distinct from the OAuth2 authentication already in place for consuming the Blizzard API.
- **Sync reconciliation:** re-syncing a character can currently duplicate existing seasons and runs; a comparison mechanism is needed before writes.
- **Parallelism and async processing:** guild roster sync is currently sequential and blocking; the goal is to parallelize per-member calls and decouple heavy processing from the HTTP request lifecycle.
- **CI/CD:** automated build and test pipeline via GitHub Actions or Jenkins
- **Code quality:** SonarQube or SonarLint integration for continuous static analysis.
- **Frontend:** an Angular application consuming this API.

## Data Flow
 
```
Blizzard API → Parsers → Entities (full persistence)
Entities → Query Services → Mappers → DTOs → REST Endpoints
```
 
Sync (write) and query (read) are treated as distinct responsibilities, each with its own set of services.
