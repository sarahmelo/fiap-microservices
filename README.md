# Microserviços FIAP

Este projeto implementa uma arquitetura de microserviços composta pelos serviços `users` e `sprinkler`, com fluxos automatizados de CI/CD para ambientes de staging e produção.

## Como Inicializar e Executar o Projeto

### Pré-requisitos

- [Java Development Kit (JDK) 21](https://www.oracle.com/java/technologies/downloads/#java21)
- [Maven](https://maven.apache.org/download.cgi) (3.8+)
- [Docker](https://www.docker.com/products/docker-desktop/) e Docker Compose
- [Git](https://git-scm.com/downloads)

### Clonando o Repositório

```bash
# Clone o repositório
git clone https://github.com/sarahmelo/fiap-microservices.git
cd fiap-microservices
```

### Compilando os Microserviços

```bash
# Compile o serviço de usuários
cd users
mvn clean package

# Compile o serviço de aspersores
cd ../sprinkler
mvn clean package

# Volte para a pasta raiz
cd ..
```

### Construindo as Imagens Docker Localmente

```bash
# Construói a imagem Docker do serviço de usuários
docker build -t fiap-users:local ./users

# Construói a imagem Docker do serviço de aspersores
docker build -t fiap-sprinkler:local ./sprinkler
```

### Executando em Ambiente Local

```bash
# Execute apenas o banco de dados MySQL para desenvolvimento local
docker-compose up -d mysql-staging

# Inicie o serviço de usuários na porta 8080
java -jar -Dspring.profiles.active=staging users/target/users-service.jar

# Em outro terminal, inicie o serviço de aspersores na porta 8081
java -jar -Dspring.profiles.active=staging sprinkler/target/sprinkler-service.jar
```

### Executando com Docker Compose

#### Ambiente de Staging

```bash
# Executa todos os serviços no ambiente de staging
docker-compose -f docker-compose-staging.yml up -d

# Verificar logs do serviço de usuários
docker logs -f fiap-users-staging

# Executar apenas o serviço de usuários na porta 8080
docker run -p 8080:8080 sarahmelo/fiap-users:staging
```

#### Ambiente de Produção

```bash
# Executa todos os serviços no ambiente de produção
docker-compose -f docker-compose-prod.yml up -d

# Verificar logs do serviço de usuários
docker logs -f fiap-users-prod

# Executar apenas o serviço de usuários na porta 8080
docker run -p 8080:8080 sarahmelo/fiap-users:prod
```

### Endpoints Disponíveis

#### Serviço de Usuários (porta 8080)

- **Autenticação**: `POST /api/auth/login`
- **Cadastro de Usuários**: `POST /api/auth/register`
- **Listar Usuários**: `GET /api/users`
- **Obter Usuário por ID**: `GET /api/users/{id}`

#### Serviço de Aspersores (porta 8081)

- **Listar Aspersores**: `GET /api/sprinklers`
- **Obter Aspersor por ID**: `GET /api/sprinklers/{id}`
- **Ativar Aspersor**: `POST /api/sprinklers/{id}/activate`
- **Desativar Aspersor**: `POST /api/sprinklers/{id}/deactivate`

### Testando via Swagger UI

- Serviço de Usuários: `http://localhost:8080/swagger-ui/index.html`
- Serviço de Aspersores: `http://localhost:8081/swagger-ui/index.html`

## Pipeline de CI/CD

O projeto utiliza GitHub Actions para implementar um pipeline completo de CI/CD (Integração Contínua e Entrega Contínua), dividido em duas partes principais:

### 1. Integração Contínua (CI)

O pipeline de CI é executado automaticamente a cada push ou pull request e realiza as seguintes etapas:

1. **Checkout do código**: Obtém a versão mais recente do código-fonte
2. **Configuração do ambiente Java**: Configura o JDK 21 para compilação
3. **Compilação dos serviços**: Compila os microserviços `users` e `sprinkler`
4. **Upload de artefatos**: Armazena os JARs gerados como artefatos
5. **Login no Docker Hub**: Autentica no Docker Hub para publicação de imagens
6. **Definição de ambiente**: Define as tags das imagens com base na branch:
   - Branch `master`: gera imagens com tag `prod`
   - Branch `developer` ou `develop`: gera imagens com tag `staging`
   - Outras branches: gera imagens com tag `dev`
7. **Build e Push das imagens**: Constrói e publica as imagens Docker no Docker Hub

### 2. Entrega Contínua (CD)

O pipeline de CD é acionado após o sucesso do CI e realiza o deploy em diferentes ambientes:

1. **Deploy para Staging**: Quando o fluxo de CI é concluído com sucesso na branch `develop`
2. **Deploy para Produção**: Quando o fluxo de CI é concluído com sucesso na branch `master`

As imagens Docker são publicadas no Docker Hub com as seguintes tags:
- `sarahmelo/fiap-users:staging` e `sarahmelo/fiap-users:staging-{COMMIT_SHA}`
- `sarahmelo/fiap-sprinkler:staging` e `sarahmelo/fiap-sprinkler:staging-{COMMIT_SHA}`
- `sarahmelo/fiap-users:prod` e `sarahmelo/fiap-users:prod-{COMMIT_SHA}`
- `sarahmelo/fiap-sprinkler:prod` e `sarahmelo/fiap-sprinkler:prod-{COMMIT_SHA}`

## Estratégia de Containerização e sua Relevância para DevOps

Este projeto adota uma estratégia de containerização baseada em Docker, o que proporciona diversos benefícios para as práticas de DevOps:

### 1. Consistência entre Ambientes

A containerização garante que o software funcione de forma consistente em qualquer ambiente (desenvolvimento, staging, produção), eliminando problemas do tipo "funciona na minha máquina". Os contêineres encapsulam a aplicação e todas as suas dependências, garantindo comportamento idêntico em diferentes ambientes.

### 2. Isolamento e Escalabilidade

Cada microserviço é isolado em seu próprio contêiner, permitindo:
- Escalabilidade independente de cada serviço
- Melhor gerenciamento de recursos
- Facilidade para implementar estratégias de resiliência

### 3. Deploy Rápido e Confiável

A estratégia de containerização permite:
- Implantações mais rápidas e com menor downtime
- Facilidade para rollbacks em caso de problemas
- Capacidade de testar exatamente a mesma imagem em diferentes ambientes

### 4. Automação e Orquestração

A containerização facilita a automação de todo o ciclo de vida da aplicação:
- CI/CD automatizado para build e deploy de contêineres
- Possibilidade de orquestração com ferramentas como Kubernetes ou Docker Swarm
- Monitoramento e logging centralizados dos contêineres

### 5. Configuração como Código

Toda a configuração dos contêineres e ambientes está definida como código (Dockerfiles e arquivos docker-compose), proporcionando:
- Versionamento da infraestrutura junto com o código
- Facilidade para replicar ambientes
- Auditoria de mudanças na configuração


