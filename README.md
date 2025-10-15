# RA2 Avaliação - Sistema de Gerenciamento de Projetos Kanban

## 📋 Sobre o Projeto

RA2 Avaliação é uma aplicação web desenvolvida em Spring Boot que implementa um sistema completo de gerenciamento de projetos estilo Kanban. O sistema permite que usuários criem projetos colaborativos, organizem tarefas em colunas customizáveis e gerenciem equipes com diferentes níveis de permissão.

## 🚀 Tecnologias Utilizadas

- **Java 24** - Linguagem de programação principal
- **Spring Boot 3.5.6** - Framework principal
- **Spring Security** - Autenticação e autorização
- **Spring Data JPA** - Persistência de dados
- **Spring Validation** - Validação de dados
- **PostgreSQL** - Banco de dados relacional
- **Lombok** - Redução de código boilerplate
- **Docker Compose** - Containerização do banco de dados
- **Gradle** - Gerenciamento de dependências e build

## 🏗️ Arquitetura do Sistema

O projeto segue uma arquitetura em camadas bem definida:

```
src/main/java/dev/project/ra2avaliacao/
├── Ra2AvaliacaoApplication.java     # Classe principal da aplicação
├── config/                          # Configurações de segurança e filtros
│   ├── SecurityConfig.java
│   └── SessionAuthFilter.java
├── controllers/                     # Controladores REST
│   ├── AuthController.java
│   ├── CardController.java
│   ├── ColumnController.java
│   ├── ProjectController.java
│   ├── TagController.java
│   └── UserController.java
├── dtos/                           # Data Transfer Objects
│   ├── auth/
│   ├── card/
│   ├── column/
│   ├── project/
│   ├── tag/
│   └── user/
├── models/                         # Entidades JPA
│   ├── Card.java
│   ├── CardTags.java
│   ├── CardTagsId.java
│   ├── Column.java
│   ├── ParticipantRole.java
│   ├── Project.java
│   ├── ProjectParticipant.java
│   ├── Session.java
│   ├── Tag.java
│   └── User.java
├── repositories/                   # Repositórios JPA
├── services/                      # Lógica de negócio
└── strategies/                    # Padrões de estratégia
```

## 📊 Modelo de Dados

### Entidades Principais

#### **User (Usuário)**
- `id`: UUID (Primary Key)
- `name`: Nome do usuário
- `email`: Email único do usuário
- `password`: Senha criptografada
- `createdAt`/`updatedAt`: Timestamps automáticos

#### **Project (Projeto)**
- `id`: UUID (Primary Key)
- `name`: Nome do projeto
- `description`: Descrição opcional
- `createdAt`/`updatedAt`: Timestamps automáticos

#### **ProjectParticipant (Participante do Projeto)**
- `id`: UUID (Primary Key)
- `project`: Referência ao projeto
- `user`: Referência ao usuário
- `role`: Papel do usuário (CREATOR, ADMIN, MEMBER)
- `createdAt`/`updatedAt`: Timestamps automáticos

#### **Column (Coluna)**
- `id`: UUID (Primary Key)
- `name`: Nome da coluna
- `project`: Referência ao projeto
- `position`: Posição da coluna no board
- `cards`: Lista de cartões da coluna
- `createdAt`/`updatedAt`: Timestamps automáticos

#### **Card (Cartão)**
- `id`: UUID (Primary Key)
- `title`: Título do cartão
- `content`: Conteúdo/descrição do cartão
- `column`: Referência à coluna
- `cardTags`: Tags associadas ao cartão
- `createdAt`/`updatedAt`: Timestamps automáticos

#### **Tag (Etiqueta)**
- `id`: UUID (Primary Key)
- `name`: Nome da tag
- `project`: Referência ao projeto
- `createdAt`/`updatedAt`: Timestamps automáticos

#### **CardTags (Relacionamento Cartão-Tag)**
- Tabela de relacionamento many-to-many entre Card e Tag
- Utiliza chave composta via `CardTagsId`

## 🔧 Funcionalidades Implementadas

### 🔐 Sistema de Autenticação
- **Login de usuários** com validação de credenciais
- **Autenticação baseada em sessões** com filtro customizado
- **Criptografia de senhas** usando BCrypt
- **Controle de acesso** por endpoints protegidos

### 👥 Gerenciamento de Usuários
- **Criação de novos usuários** com validação de dados
- **Listagem de usuários** do sistema
- **Busca de usuário por ID**
- **Atualização de perfil** do usuário autenticado
- **Exclusão de conta** do usuário

### 📁 Gerenciamento de Projetos
- **Criação de projetos** com usuário como criador automático
- **Listagem de todos os projetos** do usuário autenticado
- **Busca de projeto específico** com validação de permissões
- **Atualização de projetos** (apenas para criadores/admins)
- **Exclusão de projetos** (apenas para criadores)
- **Sistema de participantes** com três níveis de acesso:
  - **CREATOR**: Controle total do projeto
  - **ADMIN**: Pode gerenciar participantes e conteúdo
  - **MEMBER**: Pode visualizar e editar conteúdo

### 📋 Sistema Kanban - Colunas
- **Criação de colunas** em projetos específicos
- **Atualização de colunas** com validação de permissões
- **Reordenação de colunas** com controle de posição
- **Exclusão de colunas** com todas as dependências
- **Controle de posicionamento** automático das colunas

### 🃏 Gerenciamento de Cartões
- **Criação de cartões** em colunas específicas
- **Visualização de cartões** individuais ou por coluna
- **Atualização completa** de título e conteúdo
- **Exclusão de cartões** com limpeza de relacionamentos
- **Sistema de tags** para categorização:
  - Associação de tags a cartões
  - Remoção de tags de cartões
  - Validação de permissões para operações

### 🏷️ Sistema de Tags
- **Criação de tags** por projeto
- **Busca de tags** por ID com validação de acesso
- **Atualização de tags** existentes
- **Exclusão de tags** com limpeza de relacionamentos
- **Listagem de tags** por projeto

### 🛡️ Sistema de Segurança e Validações
- **Filtro de autenticação customizado** (`SessionAuthFilter`)
- **Validação de permissões** em todas as operações
- **Controle de acesso baseado em papéis** de participantes
- **Validação de dados** com Bean Validation
- **Tratamento de exceções** com códigos HTTP apropriados

## ⚙️ Configuração e Instalação

### Pré-requisitos
- Java 24 ou superior
- Docker e Docker Compose
- Gradle (incluído via wrapper)

### Configuração do Banco de Dados

1. **Inicie o PostgreSQL via Docker:**
```bash
docker-compose up -d
```

2. **Configurações do banco** (application.yml):
- **URL**: `jdbc:postgresql://localhost:5432/ra2_avaliacao`
- **Usuário**: `postgres`
- **Senha**: `postgres`
- **DDL**: `update` (criação automática de tabelas)

### Executando a Aplicação

1. **Clone o repositório**
2. **Execute o banco de dados:**
```bash
docker-compose up -d
```

3. **Execute a aplicação:**
```bash
./gradlew bootRun
```

4. **A aplicação estará disponível em:** `http://localhost:8080`

## 📡 API Endpoints

### Autenticação
- `POST /auth/login` - Login do usuário

### Usuários
- `POST /users` - Criar usuário
- `GET /users` - Listar todos os usuários
- `GET /users/{id}` - Buscar usuário por ID
- `PUT /users` - Atualizar usuário atual
- `DELETE /users` - Excluir usuário atual

### Projetos
- `POST /projects` - Criar projeto
- `GET /projects` - Listar projetos do usuário
- `GET /projects/{id}` - Buscar projeto específico
- `PUT /projects/{id}` - Atualizar projeto
- `DELETE /projects/{id}` - Excluir projeto

### Participantes
- `POST /projects/{projectId}/participants` - Adicionar participante
- `PATCH /projects/{projectId}/participants/{userId}/role` - Atualizar papel
- `DELETE /projects/{projectId}/participants/{userId}` - Remover participante

### Colunas
- `POST /columns/project/{projectId}` - Criar coluna
- `PUT /columns/{columnId}` - Atualizar coluna
- `PATCH /columns/{columnId}/position` - Reordenar coluna
- `DELETE /columns/{columnId}` - Excluir coluna

### Cartões
- `POST /cards/column/{columnId}` - Criar cartão
- `GET /cards/{cardId}` - Buscar cartão
- `GET /cards/column/{columnId}` - Listar cartões da coluna
- `PUT /cards/{cardId}` - Atualizar cartão
- `DELETE /cards/{cardId}` - Excluir cartão
- `POST /cards/{cardId}/tags/{tagId}` - Associar tag
- `DELETE /cards/{cardId}/tags/{tagId}` - Remover tag

### Tags
- `POST /tags` - Criar tag
- `GET /tags/{id}` - Buscar tag
- `PUT /tags/{id}` - Atualizar tag
- `DELETE /tags/{id}` - Excluir tag
- `GET /tags/project/{projectId}` - Listar tags do projeto

## 🎯 Características Técnicas

### Padrões Implementados
- **Repository Pattern** - Abstração da camada de dados
- **DTO Pattern** - Transferência segura de dados
- **Strategy Pattern** - Implementação flexível de algoritmos
- **MVC Architecture** - Separação clara de responsabilidades

### Segurança
- **Autenticação baseada em sessões**
- **Controle de acesso granular** por recurso
- **Validação de permissões** em tempo de execução
- **Criptografia de senhas** com BCrypt
- **Proteção contra CSRF** desabilitada para APIs REST

### Performance e Qualidade
- **Lazy Loading** nas relações JPA
- **Timestamps automáticos** com Hibernate
- **Validação de dados** com Bean Validation
- **Lombok** para redução de código boilerplate
- **UUID** como chaves primárias para escalabilidade

## 📈 Possíveis Extensões

O sistema está preparado para futuras extensões como:
- Sistema de notificações em tempo real
- Upload de anexos nos cartões
- Comentários nos cartões
- Dashboard de métricas e relatórios
- API de integração com ferramentas externas
- Sistema de templates de projetos
- Controle de versionamento de cartões

## 🤝 Contribuição

Este projeto foi desenvolvido como parte de uma avaliação acadêmica (RA2) para o curso de Design de Software da PUCPR.

---

**Desenvolvido com ❤️ usando Spring Boot**
