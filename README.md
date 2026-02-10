# Involvest

Sistema de gerenciamento de finanças familiares com metas financeiras.

## 1) Visão geral da arquitetura

- **Backend**: Spring Boot 3 (Java 17), JDBC puro, MVC, autenticação JWT com refresh token e segurança stateless.
- **Frontend**: Vue 3 + Quasar, mobile-first, organizado por camadas (pages, components, services, stores).
- **Banco**: PostgreSQL (DDL em `backend/db/schema.sql`).
- **Mobile**: Capacitor (Android/iOS) com SQLite para cache local.

**Decisões**
- JDBC puro garante controle total de SQL e desempenho previsível.
- JWT manual evita “bibliotecas mágicas” e explicita o fluxo de segurança.
- Stores (Pinia) centralizam estado e regras de negócio do frontend.
- Separação por camadas mantém código testável e SOLID.

## 2) Estrutura de pastas (backend)

```
backend/
  db/
    schema.sql
  src/main/java/com/involvest/
    config/
    controller/
    dto/
    exception/
    model/
    repository/
    security/
    service/
    util/
  src/main/resources/
    application.yml
  src/test/java/com/involvest/
    security/
    util/
  pom.xml
```

## 3) Estrutura de pastas (frontend)

```
frontend/
  src/
    assets/
    boot/
    components/
    layouts/
    pages/
    router/
    services/
    stores/
    utils/
    App.vue
    main.ts
    app.css
  quasar.config.js
  capacitor.config.ts
  package.json
  tsconfig.json
```

## 4) Modelagem do banco (DDL SQL)

Veja `backend/db/schema.sql`.

## 5) Backend (código)

### Configuração do `application.yml`
Arquivo completo em `backend/src/main/resources/application.yml`.

### Endpoints REST

**Auth**
- `POST /auth/register` -> cria família e usuário
- `POST /auth/login` -> retorna `accessToken` e `refreshToken`
- `POST /auth/refresh` -> troca refresh token por novos tokens

**Famílias**
- `POST /families` -> cria família
- `GET /families/me` -> obtém família do usuário

**Tipos de movimentação**
- `POST /types`
- `GET /types`

**Movimentações**
- `POST /transactions`
- `GET /transactions?month=YYYY-MM`

**Metas**
- `POST /goals`
- `GET /goals`

**Dashboard**
- `GET /dashboard?month=YYYY-MM`

### Exemplo de payloads

**Cadastro**
```json
{
  "name": "Ana",
  "email": "ana@involvest.com",
  "password": "123456",
  "familyName": "Silva"
}
```

**Login**
```json
{
  "email": "ana@involvest.com",
  "password": "123456"
}
```

**Criar movimentação**
```json
{
  "typeId": 1,
  "amount": 250.50,
  "description": "Mercado",
  "occurredAt": "2026-02-10"
}
```

### Testes unitários (JUnit)
Exemplos em:
- `backend/src/test/java/com/involvest/security/JwtUtilTest.java`
- `backend/src/test/java/com/involvest/util/HashUtilTest.java`

## 6) Frontend (código)

### Telas
- Login: `frontend/src/pages/LoginPage.vue`
- Cadastro: `frontend/src/pages/RegisterPage.vue`
- Dashboard: `frontend/src/pages/DashboardPage.vue`
- Receitas/Despesas: `frontend/src/pages/TransactionsPage.vue`
- Tipos: `frontend/src/pages/TypesPage.vue`
- Metas: `frontend/src/pages/GoalsPage.vue`

### Consumo de API (Axios)
Arquivo: `frontend/src/boot/axios.ts`

### SQLite (cache local)
Arquivo: `frontend/src/services/sqlite.ts`

### Guards de rota
Arquivo: `frontend/src/router/index.ts`

## 7) Integração Front x Back

- Frontend envia `Authorization: Bearer <accessToken>`
- Em `401`, o interceptor chama `/auth/refresh` e reenvia a requisição.
- Tokens são persistidos em `localStorage`.

## 8) Build mobile com Capacitor

1. Instalar dependências
   - `cd frontend`
   - `npm install`
2. Build do SPA
   - `npm run build`
3. Inicializar Capacitor
   - `npx cap add android`
   - `npx cap add ios`
4. Sincronizar
   - `npx cap sync`
5. Abrir projeto
   - `npx cap open android`
   - `npx cap open ios`

## 9) Checklist final de produção

- Configurar `security.jwt.secret` com valor forte (64+ chars)
- Criar usuário e banco no PostgreSQL
- Executar DDL `backend/db/schema.sql`
- Revisar CORS conforme domínio real
- Monitorar logs e configurar observabilidade
- Garantir HTTPS no ambiente de produção

## Como rodar local

**Backend**
1. Criar banco e usuário no PostgreSQL
2. Ajustar `backend/src/main/resources/application.yml`
3. `cd backend`
4. `mvn spring-boot:run`

**Frontend**
1. `cd frontend`
2. `npm install`
3. `npm run dev`

## Como gerar token JWT

- `AuthService` gera `accessToken` e `refreshToken` via `JwtUtil`.
- O `JwtUtil` assina com HMAC-SHA256 usando o segredo de `application.yml`.

## Fluxo de autenticação

1. `POST /auth/login` retorna tokens.
2. Requests subsequentes usam `Authorization: Bearer`.
3. Em expiração, `POST /auth/refresh` emite novo par de tokens.

## Diagrama textual da arquitetura

```
[Vue 3 + Quasar] --JWT--> [Spring Boot MVC]
        |                        |
        |--SQLite (cache)         |--JDBC
        |                        |
        +------ REST ------------+--PostgreSQL
```
