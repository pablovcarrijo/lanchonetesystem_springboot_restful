# Lanchonete System API

API REST desenvolvida com Spring Boot para gerenciamento de pedidos de uma lanchonete, com foco em práticas de backend como CRUD, paginação, atualização parcial de recursos, regras de negócio, organização em camadas e retorno com HATEOAS.

Este projeto foi construído com o objetivo de consolidar conhecimentos em desenvolvimento backend com Java e Spring, modelando um fluxo realista de criação de pedidos, associação de itens, controle de status e exclusão de dados de forma consistente.

---

## Sumário

- [Visão geral](#visão-geral)
- [Objetivos do projeto](#objetivos-do-projeto)
- [Funcionalidades implementadas](#funcionalidades-implementadas)
- [Regras de negócio](#regras-de-negócio)
- [Tecnologias utilizadas](#tecnologias-utilizadas)
- [Arquitetura e organização do projeto](#arquitetura-e-organização-do-projeto)
- [Modelo de domínio](#modelo-de-domínio)
- [Status do pedido](#status-do-pedido)
- [Endpoints da API](#endpoints-da-api)
  - [Pedidos](#1-pedidos)
  - [Itens do pedido](#2-itens-do-pedido)
- [Exemplos de uso](#exemplos-de-uso)
- [Paginação](#paginação)
- [HATEOAS](#hateoas)
- [Como executar o projeto](#como-executar-o-projeto)
- [Configuração do banco de dados](#configuração-do-banco-de-dados)
- [Estrutura de pastas](#estrutura-de-pastas)
- [Aprendizados aplicados no projeto](#aprendizados-aplicados-no-projeto)
- [Possíveis melhorias futuras](#possíveis-melhorias-futuras)
- [Autor](#autor)

---

## Visão geral

O **Lanchonete System API** é um backend voltado para o gerenciamento de pedidos em um cenário de lanchonete. A aplicação permite criar pedidos, consultar pedidos por identificador, listar pedidos com paginação, filtrar pedidos por status, adicionar itens a um pedido, remover itens e excluir pedidos completos.

Além da parte básica de CRUD, o projeto também trabalha com regras importantes de negócio, como:

- inicialização de um pedido com status padrão;
- cálculo e atualização do valor total do pedido;
- bloqueio de alteração de status para pedidos cancelados;
- exclusão encadeada de itens antes da exclusão do pedido;
- filtragem por status com paginação;
- navegação com HATEOAS em respostas de pedidos.

Esse projeto não foi feito apenas para “funcionar”, mas para praticar conceitos que aparecem com frequência em APIs reais: separação por camadas, responsabilidade de cada componente, consistência transacional, consulta paginada, atualização parcial e organização do domínio.

---

## Objetivos do projeto

Os principais objetivos deste projeto foram:

- praticar desenvolvimento de APIs REST com Spring Boot;
- aplicar o padrão de arquitetura em camadas (`Controller`, `Service`, `Repository`);
- trabalhar com persistência usando Spring Data JPA;
- implementar consultas paginadas;
- utilizar HATEOAS para enriquecer respostas;
- aplicar regras de negócio no serviço, e não apenas nos controladores;
- estruturar melhor a exclusão de entidades relacionadas;
- praticar atualização de status de pedidos com validações;
- construir um projeto com valor de portfólio no GitHub.

---

## Funcionalidades implementadas

### Gestão de pedidos

- criação de novos pedidos;
- consulta de pedido por ID;
- listagem geral de pedidos com paginação;
- consulta de pedidos por status;
- atualização de status do pedido;
- remoção de pedido.

### Gestão de itens do pedido

- adição de itens em um pedido existente;
- listagem dos itens associados a um pedido;
- remoção de item de pedido;
- atualização automática do valor total do pedido com base nos itens.

---

## Regras de negócio

A API implementa algumas regras centrais para manter a consistência do domínio.

### 1. Pedido novo sempre inicia com status padrão

Ao criar um pedido, o sistema define automaticamente:

- `status = RECEBIDO`
- `total_value = 0.0`

Isso evita que um pedido seja criado em estado inconsistente.

### 2. O preço do item é definido a partir do produto

Ao adicionar um item a um pedido, o sistema busca o produto pelo identificador informado e copia o preço atual para o item, preenchendo o campo `unityPrice`.

Isso garante que o item registre o valor unitário correspondente ao produto no momento da adição.

### 3. O total do pedido é recalculado ao adicionar item

Sempre que um novo item é adicionado ao pedido, o valor total é atualizado com base em:

- `quantity * unityPrice`

---

### 4. Pedido cancelado não pode ter status alterado

Caso um pedido já esteja com status `CANCELADO`, o sistema bloqueia novas alterações de status.

### 5. Itens são removidos antes do pedido

Na exclusão de um pedido, os itens relacionados são removidos antes da exclusão do próprio pedido, evitando inconsistência lógica e problemas de integridade.

---

## Tecnologias utilizadas

Este projeto utiliza as seguintes tecnologias e conceitos:

- **Java**
- **Spring Boot**
- **Spring Web**
- **Spring Data JPA**
- **Hibernate**
- **Spring HATEOAS**
- **Jakarta Transactions**
- **Paginação com `Page` e `Pageable`**
- **Consultas derivadas por método no repositório**
- **JPQL para atualização de status**
- **Banco de dados relacional**
- **Git e GitHub**

---

## Arquitetura e organização do projeto

O projeto segue a separação clássica em camadas.

### `Controller`

Responsável por expor os endpoints HTTP da aplicação.

Exemplos:

- receber requisições;
- capturar parâmetros de rota e query params;
- receber corpos JSON;
- devolver respostas HTTP adequadas.

### `Service`

Responsável pela regra de negócio.

Exemplos:

- validar se um pedido existe;
- impedir atualização de pedido cancelado;
- calcular total do pedido;
- organizar fluxo de deleção;
- enriquecer retorno com HATEOAS.

### `Repository`

Responsável pela comunicação com o banco de dados.

Exemplos:

- buscar pedidos por status;
- buscar itens por ID do pedido;
- persistir atualizações;
- executar deleções.

### `domain`

Responsável pelas entidades e enums do sistema.

---

## Modelo de domínio

A estrutura central do domínio envolve, pelo menos, os seguintes conceitos:

### `FullOrder`

Representa o pedido principal.

Campos principais já utilizados no projeto:

- `id`
- `status`
- `total_value`

Dependendo da evolução da sua entidade, podem existir outros atributos, como dados do cliente, data do pedido ou observações.

### `ItemsOrder`

Representa um item vinculado a um pedido.

Campos principais:

- `id`
- `fullOrderId`
- `productId`
- `quantity`
- `unityPrice`

### `Product`

Representa um produto da lanchonete.

No fluxo atual, ele é usado principalmente para:

- localizar um produto por ID;
- obter o preço do produto;
- preencher o valor unitário do item.

---

## Status do pedido

O projeto trabalha com os seguintes status:

```java
public enum Status {
    RECEBIDO,
    PREPARANDO,
    SAIU_PARA_ENTREGA,
    ENTREGUE,
    CANCELADO
}
```
---

### Significado de cada status

- **RECEBIDO**  
  Pedido recém-criado e registrado no sistema.

- **PREPARANDO**  
  Pedido em fase de produção ou montagem.

- **SAIU_PARA_ENTREGA**  
  Pedido finalizado e encaminhado para entrega.

- **ENTREGUE**  
  Pedido concluído com sucesso.

- **CANCELADO**  
  Pedido encerrado por cancelamento, sem possibilidade de nova mudança de status.

---

## Endpoints da API

> Observação: a aplicação atualmente utiliza rotas em singular (`/order`) para pedidos e em plural (`/orders`) para itens. Essa estrutura reflete o estado atual do projeto.

---

## 1. Pedidos

### Criar pedido

**POST** `/order`

Cria um novo pedido. O sistema inicializa automaticamente:

- status como `RECEBIDO`
- total do pedido como `0.0`

#### Exemplo de requisição

```json
{
  "status": "RECEBIDO",
  "total_value": 0.0
}
```

> Na prática, os campos efetivamente necessários dependem da estrutura atual da entidade `FullOrder`. O serviço define os valores iniciais importantes da regra de negócio.

---

### Buscar pedido por ID

**GET** `/order/{id}`

Retorna um pedido específico com seus links HATEOAS.

#### Exemplo

```http
GET /order/1
```

---

### Listar todos os pedidos com paginação

**GET** `/order/all?page=0&size=5`

Retorna uma coleção paginada de pedidos.

#### Parâmetros

- `page`: número da página
- `size`: quantidade de registros por página

#### Exemplo

```http
GET /order/all?page=0&size=10
```

---

### Buscar pedidos por status

**GET** `/order?statusConsult=RECEBIDO&page=0&size=5`

Filtra pedidos pelo status informado.

#### Parâmetros

- `statusConsult`: status desejado
- `page`: número da página
- `size`: tamanho da página

#### Exemplos

```http
GET /order?statusConsult=RECEBIDO&page=0&size=5
```

```http
GET /order?statusConsult=PREPARANDO&page=0&size=10
```

```http
GET /order?statusConsult=SAIU_PARA_ENTREGA&page=1&size=5
```

> Como o parâmetro é convertido para `enum`, o valor deve seguir o padrão esperado pelo sistema.

---

### Atualizar status do pedido

Este projeto também contempla atualização de status do pedido via service/repository.

Uma convenção REST recomendada para isso seria:

**PATCH** `/order/{id}/status`

#### Exemplo de corpo

```json
{
  "newStatus": "PREPARANDO"
}
```

#### Regras aplicadas

- o pedido precisa existir;
- pedidos cancelados não podem ter status alterado;
- a atualização deve afetar pelo menos uma linha no banco.

> Caso seu controller já exponha essa funcionalidade com outro caminho, ajuste esta seção para refletir exatamente a rota utilizada no código final.

---

### Deletar pedido

**DELETE** `/order/{id}`

Remove um pedido do sistema. Antes da exclusão do pedido, os itens associados são removidos.

#### Exemplo

```http
DELETE /order/1
```

---

## 2. Itens do pedido

### Listar itens de um pedido

**GET** `/orders/{id}/items?page=0&size=5`

Retorna os itens vinculados ao pedido informado.

#### Exemplo

```http
GET /orders/1/items?page=0&size=5
```

---

### Adicionar item ao pedido

**POST** `/orders/{orderId}/items`

Adiciona um novo item a um pedido existente.

#### Regras aplicadas

- o pedido deve existir;
- o produto deve existir;
- o valor unitário do item é preenchido a partir do produto;
- o total do pedido é incrementado com base em `quantity * unityPrice`.

#### Exemplo de corpo

```json
{
  "productId": 2,
  "quantity": 3
}
```

---

### Remover item do pedido

**DELETE** `/orders/{orderId}/items/{itemId}`

Remove um item associado ao pedido.

#### Exemplo

```http
DELETE /orders/1/items/4
```

> Dependendo da implementação final do seu `ItemsOrderService`, essa operação pode ser apenas uma remoção direta ou incluir atualização do valor total do pedido.

---

## Exemplos de uso

### 1. Criando um pedido

```http
POST /order
Content-Type: application/json
```

```json
{}
```

### 2. Adicionando um item ao pedido

```http
POST /orders/1/items
Content-Type: application/json
```

```json
{
  "productId": 3,
  "quantity": 2
}
```

### 3. Buscando pedidos com status `RECEBIDO`

```http
GET /order?statusConsult=RECEBIDO&page=0&size=5
```

### 4. Excluindo um pedido

```http
DELETE /order/1
```

---

## Paginação

A API utiliza paginação em consultas de listagem, com suporte a `page` e `size`.

Esse recurso foi aplicado para:

- listagem geral de pedidos;
- consulta de pedidos por status;
- listagem de itens de um pedido.

Isso melhora a organização da resposta e evita sobrecarga em consultas que podem crescer com o tempo.

---

## HATEOAS

O projeto utiliza **Spring HATEOAS** para enriquecer algumas respostas com links navegáveis.

Essa abordagem ajuda a expor melhor as ações disponíveis sobre um recurso retornado pela API, aproximando o comportamento da ideia de hipermídia em APIs REST.

Exemplos de links que podem ser associados a um pedido:

- link para o próprio recurso;
- link para exclusão do pedido;
- link para listagem geral de pedidos;
- outros links de navegação conforme evolução do projeto.

O uso de HATEOAS também mostra preocupação com organização de resposta e com uma modelagem de API mais rica do ponto de vista arquitetural.

---

## Como executar o projeto

### Pré-requisitos

Antes de rodar o projeto, tenha em seu ambiente:

- JDK compatível com a versão configurada no projeto;
- uma IDE como IntelliJ IDEA, Eclipse ou VS Code;
- banco de dados configurado;
- Git instalado;
- Maven ou Gradle, de acordo com a estrutura do seu projeto.

### Clonar o repositório

```bash
git clone <URL_DO_SEU_REPOSITORIO>
```

```bash
cd <NOME_DA_PASTA_DO_PROJETO>
```

### Executar com Maven

Se o projeto estiver configurado com Maven:

```bash
mvn spring-boot:run
```

ou, caso esteja usando wrapper:

```bash
./mvnw spring-boot:run
```
---

## Configuração do banco de dados

A configuração do datasource deve ser feita no arquivo:

```properties
src/main/resources/application.properties
```

### Exemplo de configuração

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/lanchonete
spring.datasource.username=SEU_USUARIO
spring.datasource.password=SUA_SENHA

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
```

> Ajuste a URL, credenciais e demais propriedades conforme o banco utilizado no seu ambiente.

---

## Estrutura de pastas

Uma organização esperada para o projeto é semelhante a esta:

```text
src
└── main
    ├── java
    │   └── com.example.lanchonetesystem
    │       ├── Controller
    │       │   ├── OrderController.java
    │       │   └── ItemsOrderController.java
    │       ├── Services
    │       │   ├── OrderService.java
    │       │   ├── ItemsOrderService.java
    │       │   └── ProductService.java
    │       ├── Repository
    │       │   ├── OrderRepository.java
    │       │   └── ItemsOrderRepository.java
    │       ├── domain
    │       │   ├── FullOrder.java
    │       │   ├── ItemsOrder.java
    │       │   ├── Product.java
    │       │   └── Status.java
    │       └── LanchonetesystemApplication.java
    └── resources
        └── application.properties
```

---

## Aprendizados aplicados no projeto

Durante o desenvolvimento deste projeto, foram praticados conceitos importantes de backend, como:

- construção de endpoints REST;
- uso de métodos HTTP com responsabilidades adequadas;
- manipulação de entidades com JPA;
- paginação com `Pageable`;
- filtros por status;
- organização de fluxo de negócio na camada de serviço;
- atualização parcial de um recurso;
- uso de `@Transactional` para garantir consistência;
- enriquecimento de resposta com HATEOAS;
- tratamento de erros via exceções de execução;
- raciocínio sobre deleção de registros relacionados.

Além da implementação técnica, o projeto também foi importante para treinar a tomada de decisão sobre onde cada responsabilidade deve ficar: controller, service ou repository.

---

## Possíveis melhorias futuras

Embora o projeto já esteja funcional e bem útil como prática e portfólio, existem várias melhorias que podem levá-lo a um nível ainda mais profissional.

### 1. Uso de DTOs

Atualmente, é comum em projetos de estudo retornar a própria entidade. Em uma evolução natural, o ideal é utilizar DTOs para separar melhor:

- entrada de dados;
- saída de dados;
- modelo persistido.

### 2. Tratamento global de exceções

Adicionar um `@ControllerAdvice` para padronizar respostas de erro, como:

- recurso não encontrado;
- regra de negócio inválida;
- erro de validação.

### 3. Validação com Bean Validation

Aplicar anotações como:

- `@NotNull`
- `@Positive`
- `@NotBlank`

especialmente em campos como quantidade, identificadores e dados de entrada.

### 4. Testes automatizados

Adicionar:

- testes unitários para services;
- testes de integração para controllers;
- testes de repositório.

### 5. Padronização das rotas

Hoje o projeto mistura `/order` e `/orders`. Uma melhoria seria padronizar tudo em plural, por exemplo:

- `/orders`
- `/orders/{id}`
- `/orders/{id}/items`

### 6. Relacionamentos JPA mais explícitos

Uma evolução interessante seria modelar associações com:

- `@OneToMany`
- `@ManyToOne`

em vez de trabalhar apenas com IDs isolados dentro das entidades.

### 7. Documentação automática da API

Integração com Swagger/OpenAPI para documentação interativa.

### 8. Segurança

Implementação futura de autenticação e autorização com Spring Security.

---

## Autor

Desenvolvido por **Pablo Vinicius Carrijo**.

Projeto criado com foco em evolução técnica em backend Java/Spring Boot, prática de arquitetura em camadas, regras de negócio e construção de portfólio no GitHub.

---

## Observação final

Este projeto representa uma etapa importante de prática com Spring Boot, especialmente por não se limitar ao CRUD puro. Ele envolve regras de negócio, decisões de modelagem, paginação, filtros, HATEOAS e persistência organizada por camadas.

Mais do que apenas “fazer funcionar”, o objetivo foi construir uma base sólida para projetos backend mais maduros no futuro.
