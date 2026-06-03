# TaskFlow

TaskFlow é uma aplicação full-stack de gerenciamento de tarefas, desenvolvida como projeto de estudo e portfólio. O objetivo é demonstrar a construção de uma aplicação web completa, com frontend em React, API REST em Spring Boot, autenticação com JWT e persistência em PostgreSQL.

O projeto permite que usuários criem uma conta, façam login e gerenciem suas próprias tarefas. Cada tarefa fica vinculada ao usuário autenticado, evitando que uma pessoa acesse ou altere tarefas de outra.

## Problema Resolvido

O TaskFlow resolve um fluxo comum de organização pessoal: registrar tarefas, acompanhar o que ainda está pendente e marcar o que já foi concluído. Para fins de aprendizado, o projeto também trabalha problemas importantes de aplicações reais, como autenticação, autorização, validação de dados, integração frontend/backend, CORS, migrations de banco e configuração por variáveis de ambiente.

## Funcionalidades

- Cadastro de usuário
- Login com geração de token JWT
- Proteção das rotas de tarefas por autenticação
- Criação, listagem, edição e exclusão de tarefas
- Marcação de tarefas como concluídas ou pendentes
- Listagem de tarefas separada por usuário autenticado
- Contadores de tarefas totais, concluídas e pendentes
- Tema claro/escuro com preferência salva no navegador
- Tratamento básico de erros no frontend
- Validações no backend com Jakarta Validation
- Migrations de banco com Flyway
- Documentação interativa da API via Swagger/OpenAPI
- Modo demo no frontend para visualizar a interface sem depender da API
- Dockerfile multi-stage para build e execução do backend

## Tecnologias

### Backend

- Java 21
- Spring Boot 4
- Spring Web MVC
- Spring Security
- OAuth2 Resource Server
- JWT com chaves RSA
- Spring Data JPA
- PostgreSQL
- Flyway
- Jakarta Validation
- Maven
- Springdoc OpenAPI

### Frontend

- React 19
- Vite
- JavaScript
- Axios
- CSS

### Infraestrutura e Ambiente

- PostgreSQL como banco relacional
- Docker no backend
- Configuração por variáveis de ambiente

## Arquitetura e Camadas

O backend segue uma separação simples em camadas:

```text
controller -> service -> repository -> database
```

- `controller`: expõe os endpoints REST e recebe as requisições HTTP.
- `service`: concentra as regras de negócio, como criação de usuários, login e validação de propriedade das tarefas.
- `repository`: acessa o banco de dados usando Spring Data JPA.
- `model`: representa as entidades persistidas no banco.
- `dto`: define os objetos de entrada e saída da API.
- `mapper`: converte entidades em DTOs e DTOs em entidades.
- `config`: centraliza segurança, CORS e configuração de JWT.
- `infra`: trata exceções e padroniza respostas de erro.

No frontend, a estrutura é organizada por páginas e serviços:

```text
frontend/src/
|-- pages/
|   |-- Auth/
|   `-- Home/
|-- services/
|   `-- api.js
|-- App.jsx
`-- main.jsx
```

## Autenticação e Autorização

A autenticação é feita com JWT. No login, a API valida email e senha, gera um token assinado com RSA e retorna o tempo de expiração.

Detalhes implementados:

- Senhas armazenadas com hash usando `BCryptPasswordEncoder`.
- Endpoints `/auth/register` e `/auth/login` públicos.
- Demais endpoints exigem token Bearer.
- O `subject` do JWT armazena o ID do usuário.
- As consultas de tarefas usam o ID do usuário autenticado.
- A API busca tarefas por `taskId` e `userId`, impedindo acesso a tarefas de outros usuários.
- O frontend salva o token no `localStorage` e o envia no header `Authorization`.

## Endpoints Principais

### Autenticação

```http
POST /auth/register
POST /auth/login
```

Exemplo de cadastro:

```json
{
  "name": "Henrique",
  "email": "henrique@example.com",
  "password": "senha123"
}
```

Exemplo de login:

```json
{
  "email": "henrique@example.com",
  "password": "senha123"
}
```

### Tarefas

Os endpoints abaixo exigem autenticação:

```http
GET    /task
GET    /task/{id}
POST   /task
PATCH  /task/{id}
DELETE /task/{id}
```

Exemplo de criação de tarefa:

```json
{
  "title": "Estudar Spring Security",
  "description": "Revisar autenticação com JWT"
}
```

Exemplo de atualização parcial:

```json
{
  "title": "Estudar Spring Security",
  "description": "Revisar autenticação e autorização com JWT",
  "completed": true
}
```

A documentação Swagger fica disponível, com a aplicação rodando, em:

```text
http://localhost:8080/swagger-ui/index.html
```

## Como Rodar Localmente

### Pré-requisitos

- Java 21
- Node.js e npm
- PostgreSQL
- Maven ou o Maven Wrapper incluído no projeto
- Docker, opcional, para rodar o backend containerizado

### Banco de Dados

Crie um banco PostgreSQL:

```sql
CREATE DATABASE taskflow;
```

As tabelas são criadas pelo Flyway a partir da migration em `backend/src/main/resources/db/migration`.

### Backend

Entre na pasta do backend:

```bash
cd backend
```

Crie um arquivo `.env` na pasta `backend`:

```env
DB_URL=jdbc:postgresql://localhost:5432/taskflow
DB_USERNAME=seu_usuario
DB_PASSWORD=sua_senha
APP_CORS_ALLOWED_ORIGINS=http://localhost:5173
```

O projeto possui chaves locais em `backend/src/main/resources/certs/app.key` e `backend/src/main/resources/certs/app.pub`. Para ambientes externos, também é possível informar as chaves por variáveis:

```env
JWT_PUBLIC_KEY_BASE64=chave_publica_em_base64
JWT_PRIVATE_KEY_BASE64=chave_privada_em_base64
```

Execute a aplicação:

```bash
./mvnw spring-boot:run
```

No Windows:

```powershell
.\mvnw.cmd spring-boot:run
```

A API ficará disponível em:

```text
http://localhost:8080
```

### Frontend

Entre na pasta do frontend:

```bash
cd frontend
```

Instale as dependências:

```bash
npm install
```

Crie um arquivo `.env`, se quiser apontar para uma API específica:

```env
VITE_API_URL=http://localhost:8080
VITE_DEMO_MODE=false
```

Rode o frontend:

```bash
npm run dev
```

A aplicação ficará disponível em:

```text
http://localhost:5173
```

Por padrão, em desenvolvimento, o frontend pode entrar em modo demo. Para testar o fluxo real de login e API, use `VITE_DEMO_MODE=false`.

## Docker

O backend já possui um `Dockerfile` multi-stage:

1. Usa uma imagem Maven com Eclipse Temurin 21 para compilar o projeto.
2. Gera o `.jar` com `mvn clean package -DskipTests`.
3. Usa uma imagem menor com JRE 21 para executar a aplicação.

Build da imagem:

```bash
cd backend
docker build -t taskflow-backend .
```

Execução da imagem:

```bash
docker run --rm -p 8080:8080 \
  --env DB_URL=jdbc:postgresql://host.docker.internal:5432/taskflow \
  --env DB_USERNAME=seu_usuario \
  --env DB_PASSWORD=sua_senha \
  --env APP_CORS_ALLOWED_ORIGINS=http://localhost:5173 \
  taskflow-backend
```

A parte de Docker ainda pode evoluir com `docker-compose`, incluindo banco PostgreSQL e frontend em containers.

## Variáveis de Ambiente

### Backend

| Variável | Obrigatória | Descrição |
| --- | --- | --- |
| `DB_URL` | Sim | URL JDBC do PostgreSQL |
| `DB_USERNAME` | Sim | Usuário do banco |
| `DB_PASSWORD` | Sim | Senha do banco |
| `APP_CORS_ALLOWED_ORIGINS` | Não | Origens permitidas pelo CORS. Padrão: `http://localhost:5173` |
| `JWT_PUBLIC_KEY_BASE64` | Não | Chave pública RSA em base64 para ambientes externos |
| `JWT_PRIVATE_KEY_BASE64` | Não | Chave privada RSA em base64 para ambientes externos |

### Frontend

| Variável | Obrigatória | Descrição |
| --- | --- | --- |
| `VITE_API_URL` | Não | URL base da API. Padrão: `http://localhost:8080` |
| `VITE_DEMO_MODE` | Não | Controla o modo demo. Use `false` para testar a API real em desenvolvimento |

## Comandos Úteis

Backend:

```bash
cd backend
./mvnw test
./mvnw spring-boot:run
```

Frontend:

```bash
cd frontend
npm run dev
npm run build
npm run lint
```

Docker do backend:

```bash
cd backend
docker build -t taskflow-backend .
```

## Aprendizados

- Construção de uma API REST em camadas com Spring Boot.
- Uso de DTOs e mappers para separar contrato da API e entidades do banco.
- Implementação de autenticação stateless com JWT.
- Uso de BCrypt para armazenar senhas de forma mais segura.
- Proteção de recursos por usuário autenticado.
- Integração entre React e API usando Axios.
- Tratamento de sessão no frontend com token em `localStorage`.
- Configuração de CORS para integração entre frontend e backend.
- Uso de Flyway para versionar o schema do banco.
- Primeiros passos com Docker em uma aplicação Spring Boot.

## Próximos Passos

- Criar `docker-compose.yml` para subir backend e PostgreSQL juntos.
- Dockerizar também o frontend, se fizer sentido para o ambiente de execução.
- Adicionar testes automatizados para serviços e controllers.
- Adicionar testes de integração para autenticação e isolamento das tarefas por usuário.
- Melhorar a cobertura de validações no frontend.
- Adicionar filtros por status da tarefa.
- Adicionar busca por título ou descrição.
- Adicionar paginação para listas maiores.
- Melhorar exemplos da documentação da API.

## Autor

Desenvolvido por Henrique Brandão como projeto de estudo, prática full-stack e portfólio para oportunidades de estágio ou desenvolvedor júnior.
