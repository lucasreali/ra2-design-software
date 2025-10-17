# RA2 Avaliação - Sistema de Gerenciamento de Projetos

## 📋 Descrição do Projeto

Este é um sistema de gerenciamento de projetos desenvolvido em Spring Boot que implementa funcionalidades de colaboração em equipe, organizando o trabalho através de projetos, colunas e cartões (similar ao Trello/Kanban). O projeto demonstra a aplicação de padrões de design, incluindo **Builder**, **Strategy** e **State**.

## 🏗️ Arquitetura e Padrões de Design

### 1. Padrão Builder
Utilizado na criação de entidades complexas como `Project`, `User`, `Card` e `Column`, proporcionando uma interface fluente e flexível para construção de objetos.

### 2. Padrão Strategy
Implementado no sistema de permissões através das classes:
- `PermissionStrategy` (interface)
- `CreatorPermissionStrategy`
- `AdminPermissionStrategy` 
- `MemberPermissionStrategy`
- `PermissionStrategyFactory`

### 3. Padrão State (Implementação Recente)
Aplicado na gestão de papéis (roles) dos participantes do projeto:
- `ParticipantState` (interface)
- `CreatorState` - Estado para criadores do projeto
- `AdminState` - Estado para administradores
- `MemberState` - Estado para membros básicos

O padrão State controla as transições entre diferentes níveis de permissão, encapsulando as regras de promoção e rebaixamento de usuários.

## 🚀 Tecnologias Utilizadas

- **Java 24**
- **Spring Boot 3.5.6**
- **Spring Security** - Autenticação e autorização
- **Spring Data JPA** - Persistência de dados
- **PostgreSQL** - Banco de dados
- **Lombok** - Redução de boilerplate
- **Gradle** - Gerenciamento de dependências
- **Docker** - Containerização

## 📊 Estrutura do Banco de Dados

### Entidades Principais

1. **User** - Usuários do sistema
   - `id`, `name`, `email`, `password`
   - Timestamps de criação e atualização

2. **Project** - Projetos colaborativos
   - `id`, `name`, `description`
   - Timestamps de criação e atualização

3. **ProjectParticipant** - Relacionamento usuário-projeto
   - `id`, `project_id`, `user_id`, `role`
   - Papéis: CREATOR, ADMIN, MEMBER
   - **Inclui implementação do padrão State**

4. **Column** - Colunas do quadro Kanban
   - `id`, `name`, `project_id`, `position`
   - Ordenação por posição

5. **Card** - Cartões/tarefas
   - `id`, `title`, `content`, `column_id`
   - Timestamps de criação e atualização

6. **Tag** - Etiquetas para organização
   - `id`, `name`, `color`

7. **CardTags** - Relacionamento cartão-etiqueta
   - Chave composta: `card_id`, `tag_id`

8. **Session** - Sessões de autenticação
   - `id`, `user_id`, `token`, `expires_at`

## 🔐 Sistema de Permissões

### Hierarquia de Papéis
1. **CREATOR** - Criador do projeto
   - Todas as permissões
   - Não pode ser promovido ou rebaixado
   - Pode deletar o projeto

2. **ADMIN** - Administrador
   - Pode gerenciar participantes
   - Pode editar projeto
   - Pode ser rebaixado para MEMBER

3. **MEMBER** - Membro básico
   - Acesso limitado ao projeto
   - Pode ser promovido para ADMIN

### Implementação com Padrão State
```java
// Exemplo de uso do padrão State
participant.promote(); // Transição de estado controlada pela própria classe
participant.demote();  // Regras encapsuladas nos estados concretos
```

## 🛠️ Funcionalidades Principais

### 👥 Gestão de Usuários
- Registro e autenticação
- Perfis de usuário
- Sistema de sessões

### 📁 Gestão de Projetos
- **Criar projeto** - Usuário se torna CREATOR automaticamente
- **Visualizar projetos** - Lista projetos do usuário
- **Editar projeto** - Apenas CREATOR e ADMIN
- **Deletar projeto** - Apenas CREATOR
- **Gerenciar participantes** - Adicionar, remover, alterar papéis

### 📋 Sistema Kanban
- **Colunas** - Criar, editar, reordenar, deletar
- **Cartões** - Criar, editar, mover entre colunas, deletar
- **Tags** - Criar, aplicar aos cartões, filtrar

### 🔄 Gestão de Participantes (com Padrão State)
- **Adicionar participantes** - Entram como MEMBER
- **Promover usuários** - MEMBER → ADMIN
- **Rebaixar usuários** - ADMIN → MEMBER
- **Remover participantes** - Exceto CREATOR

## 📡 API Endpoints

### Autenticação
```
POST /auth/login          # Login do usuário
POST /auth/logout         # Logout do usuário
```

### Usuários
```
GET  /users               # Listar usuários
POST /users               # Criar usuário
PUT  /users/{id}          # Atualizar usuário
```

### Projetos
```
GET    /projects                           # Listar projetos do usuário
POST   /projects                           # Criar projeto
GET    /projects/{id}                      # Detalhes do projeto
PUT    /projects/{id}                      # Atualizar projeto
DELETE /projects/{id}                      # Deletar projeto
POST   /projects/{id}/participants         # Adicionar participante
PUT    /projects/{id}/participants/{userId} # Alterar papel (usa padrão State)
DELETE /projects/{id}/participants/{userId} # Remover participante
```

### Colunas
```
GET    /projects/{projectId}/columns       # Listar colunas
POST   /projects/{projectId}/columns       # Criar coluna
PUT    /columns/{id}                       # Atualizar coluna
DELETE /columns/{id}                       # Deletar coluna
PUT    /columns/{id}/reorder               # Reordenar colunas
```

### Cartões
```
GET    /columns/{columnId}/cards           # Listar cartões
POST   /columns/{columnId}/cards           # Criar cartão
PUT    /cards/{id}                         # Atualizar cartão
DELETE /cards/{id}                         # Deletar cartão
PUT    /cards/{id}/move                    # Mover cartão
```

### Tags
```
GET    /tags                               # Listar tags
POST   /tags                               # Criar tag
PUT    /tags/{id}                          # Atualizar tag
DELETE /tags/{id}                          # Deletar tag
```

## 🔧 Configuração e Execução

### Pré-requisitos
- Java 24+
- PostgreSQL
- Docker (opcional)

### Configuração do Banco
```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/ra2_avaliacao
    username: postgres
    password: postgres
```

### Executar com Docker
```bash
docker-compose up -d
```

### Executar localmente
```bash
./gradlew bootRun
```

## 📂 Estrutura do Projeto

```
src/main/java/dev/project/ra2avaliacao/
├── config/                    # Configurações de segurança
├── controllers/               # Controllers REST
├── dtos/                     # Data Transfer Objects
├── models/                   # Entidades JPA
├── repositories/             # Repositórios Spring Data
├── services/                 # Lógica de negócio
├── strategies/               # Padrão Strategy (Permissões)
└── state/participant/        # Padrão State (Papéis)
    ├── ParticipantState.java
    ├── CreatorState.java
    ├── AdminState.java
    └── MemberState.java
```

## 🎯 Benefícios da Implementação do Padrão State

1. **Encapsulamento de Regras**: Cada estado gerencia suas próprias regras de transição
2. **Extensibilidade**: Fácil adição de novos estados sem modificar código existente
3. **Manutenibilidade**: Elimina condicionais complexas no service
4. **Aderência ao OCP**: Aberto para extensão, fechado para modificação

## 🚀 Próximos Passos

- [ ] Implementar notificações em tempo real
- [ ] Adicionar sistema de comentários nos cartões
- [ ] Implementar anexos de arquivos
- [ ] Dashboard com métricas do projeto
- [ ] API para integração com ferramentas externas

---

**Desenvolvido para a disciplina de Design de Software - PUCPR**
