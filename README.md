# servidorComSocket

Servidor HTTP simples em Java (Socket puro), com roteamento por anotações e integração com MySQL para gerenciar itens de cardápio.

## Visão geral

Este projeto implementa:

- servidor HTTP com `ServerSocket`
- roteamento por reflexão (`@MyController`, `@GetMapping`, `@PostMapping`, etc.)
- injeção simples por anotação (`@Inject`)
- camada de domínio, serviço e repositório
- persistência em MySQL via JDBC

## Tecnologias

- Java 21
- Maven
- MySQL
- Docker Compose
- Gson
- Reflections

## Estrutura principal

```text
src/main/java/org/example
├── Main.java
├── application
│   ├── controller
│   │   └── ControllerCardapio.java
│   ├── domain
│   │   ├── ItemCardapio.java
│   │   └── enumerator
│   │       └── CategoriaCardapio.java
│   ├── repository
│   │   ├── ConnectionFactory.java
│   │   ├── Database.java
│   │   └── ItemCardapioDAO.java
│   └── service
│       └── ItemCardapioService.java
└── framework
    ├── Router.java
    ├── Dispatcher.java
    ├── ControllerMethod.java
    ├── ControllerMethodMatch.java
    ├── annotation
    │   ├── DeleteMapping.java
    │   ├── GetMapping.java
    │   ├── PatchMapping.java
    │   ├── PostMapping.java
    │   ├── Inject.java
    │   └── MyController.java
    └── http
        ├── HttpMethod.java
        ├── HttpRequest.java
        └── HttpResponse.java

```

## Banco de dados

**Subir o MySQL com Docker Compose**

```text
docker compose up -d
```

Pelo código atual (ConnectionFactory), a conexão usa:

* host: localhost
* porta: 3306
* database: cardapio
* usuário: root
* senha: senha123

**Criar tabela necessária**

O projeto não inclui script SQL no repositório. Use um script compatível com os campos usados no DAO:

```text
CREATE TABLE IF NOT EXISTS item_cardapio (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    nome VARCHAR(120) NOT NULL,
    descricao VARCHAR(255) NOT NULL,
    categoria VARCHAR(40) NOT NULL,
    preco DECIMAL(10,2) NOT NULL,
    preco_promocional DECIMAL(10,2)
);
```

**Endpoints (controller @MyController(path = "/cardapio"))**

1) Listar itens

```text
GET /cardapio
curl -i localhost:8000/cardapio
```

2) Buscar total de itens

```text
GET /cardapio/total-itens
curl -i localhost:8000/cardapio/total-itens
```
 
3) Buscar item por id

```text
GET /cardapio/{id}
curl -i localhost:8000/cardapio/1
```
 
4) Inserir item

```text
POST /cardapio
Content-Type: application/json

Exemplo de body:
{
  "id": null,
  "nome": "Cappuccino",
  "descricao": "Café com leite vaporizado",
  "categoria": "BEBIDAS",
  "preco": 14.90,
  "precoPromocional": 12.90
}

curl -i -X POST localhost:8000/cardapio \
  -H "Content-Type: application/json" \
  -d '{"id":null,"nome":"Cappuccino","descricao":"Café com leite vaporizado","categoria":"BEBIDAS","preco":14.90,"precoPromocional":12.90}'
```
 
5) Remover item por id

```text
DELETE /cardapio/{id}
curl -i -X DELETE localhost:8000/cardapio/1
```
 
6) Atualizar preço

```text
PATCH /cardapio/{id}
Content-Type: application/json
```

No código atual, o PATCH espera o novo preço como JSON simples (ex.: 19.90), não objeto.

```text
curl -i -X PATCH localhost:8000/cardapio/1 \
  -H "Content-Type: application/json" \
  -d '19.90'
```

**Categorias válidas**

Valores de categoria (CategoriaCardapio):

* ENTRADAS
* PRATOS_PRINCIPAIS
* BEBIDAS
* SOBREMESA
