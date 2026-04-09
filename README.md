# Requests Application

Aplicação Spring Boot para gerenciamento de solicitações de usuários. O projeto segue uma arquitetura em camadas (controllers, services, repositories) e utiliza DTOs e mappers para isolar as entidades do domínio das representações expostas na API.

Principais tecnologias:

- **Java 21**
- **Spring Boot 3.5.x**
- **Spring Data JPA**
- **Flyway** para versionamento do banco de dados
- **H2** em memória para desenvolvimento
- **PostgreSQL** para produção
- **SpringDoc OpenAPI** para documentação interativa (Swagger)

## Funcionalidades

### Usuários
- Cadastro completo de usuários (CRUD)
- Cada requisição pertence a um usuário

### Requisições
- Registro de texto da requisição, data e resposta
- Relacionadas a um usuário e contendo uma lista de produtos
- Endpoints permitem filtrar por `userId`

### Produtos
- Produtos associados a uma requisição
- Endpoints permitem filtrar por `requestId`

## Estrutura da API

Todas as entidades possuem endpoints REST que seguem o mesmo padrão:

```
GET    /<entidade>            -> lista todos
GET    /<entidade>/{id}       -> busca por id
POST   /<entidade>            -> cria registro
PUT    /<entidade>/{id}       -> atualiza registro
DELETE /<entidade>/{id}       -> remove registro
```

Rotas principais:
- `/users`
- `/requests`
- `/products`

Filtros opcionais podem ser passados como parâmetros de query (`userId` em `/requests`, `requestId` em `/products`).

## Autenticação (JWT)

O projeto usa autenticação **stateless** com JWT.

Rotas públicas:
- `POST /auth/login`
- `POST /auth/validate`
- Swagger (`/swagger-ui/**` e `/v3/api-docs/**`)

Usuários iniciais (seed via Flyway):
- **admin / secret** (perfil `ADMIN`)
- **user / secret** (perfil `USER`)

## Postman

Existe uma coleção pronta para testes no arquivo `postman_collection.json` (na raiz do projeto).

- Rode **Auth - Login (sets token)** para salvar o JWT automaticamente na variável `token`.
- Os demais requests já enviam `Authorization: Bearer {{token}}`.

## Migrations e Banco de Dados

O esquema é gerenciado pelo Flyway. As migrations criam as tabelas `users`, `requests`, `product` e `requestproduct` e inserem dados iniciais de usuários.

As migrations ficam em:
- `src/main/resources/db/migration/common` (comum para H2 e PostgreSQL)
- `src/main/resources/db/migration/h2` (específico para H2)
- `src/main/resources/db/migration/postgresql` (específico para PostgreSQL)

Arquivos de configuração relevantes:
- `src/main/resources/application.properties` (H2)
- `src/main/resources/application-prod.properties` (PostgreSQL)

## Executando

Para rodar em desenvolvimento (H2):

```bash
./mvnw spring-boot:run
```

Para rodar apontando para o PostgreSQL (perfil `prod`):

```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=prod
```

A aplicação sobe por padrão em `http://localhost:8085`.

A documentação Swagger estará disponível em `http://localhost:8085/swagger-ui.html`.


## Testes

```
./mvnw test
```



