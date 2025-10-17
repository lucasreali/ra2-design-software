# RA2 Avaliação - Sistema de Gerenciamento de Projetos

## 📋 Descrição do Projeto

Este é um sistema de gerenciamento de projetos desenvolvido em Spring Boot que implementa funcionalidades de colaboração em equipe, organizando o trabalho através de projetos, colunas e cartões (similar ao Trello/Kanban). O projeto demonstra a aplicação de padrões de design, incluindo **Builder**, **Strategy**, **State** e **Observer**.

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

### 3. Padrão State
Aplicado na gestão de papéis (roles) dos participantes do projeto:
- `ParticipantState` (interface)
- `CreatorState` - Estado para criadores do projeto
- `AdminState` - Estado para administradores
- `MemberState` - Estado para membros básicos

O padrão State controla as transições entre diferentes níveis de permissão, encapsulando as regras de promoção e rebaixamento de usuários.

### 4. Padrão Observer (Nova Implementação)
Aplicado para notificar automaticamente sobre eventos relacionados aos cartões:

#### Estrutura do Observer:
```
src/main/java/dev/project/ra2avaliacao/observers/
├── Observer.java              # Interface base
├── Subject.java               # Interface do sujeito observável
├── CardSubject.java           # Implementação concreta do sujeito
├── ProjectMetricsObserver.java # Observer para métricas do projeto
├── CardAuditObserver.java     # Observer para auditoria
└── NotificationObserver.java  # Observer para notificações
```

#### Eventos Observáveis:
- `CARD_CREATED` - Criação de novos cartões
- `CARD_UPDATED` - Atualização de cartões existentes
- `CARD_DELETED` - Exclusão de cartões
- `CARD_MOVED` - Movimentação entre colunas
- `TAG_ASSIGNED` - Atribuição de tags
- `TAG_REMOVED` - Remoção de tags

#### Observadores Implementados:
1. **ProjectMetricsObserver**: Coleta métricas e estatísticas do projeto
2. **CardAuditObserver**: Registra logs de auditoria estruturados
3. **NotificationObserver**: Envia notificações aos participantes

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
   - **Eventos observáveis através do padrão Observer**

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

## 🔔 Sistema de Observação de Eventos

### Implementação do Padrão Observer
```java
// No CardService - automaticamente notifica observadores
cardSubject.notifyObservers("CARD_CREATED", savedCard);
cardSubject.notifyObservers("CARD_MOVED", movedCard);
```

### Benefícios do Observer:
- **Desacoplamento**: Observadores independentes do CardService
- **Extensibilidade**: Novos observadores podem ser adicionados facilmente
- **Responsabilidade única**: Cada observador tem uma função específica
- **Notificações automáticas**: Eventos são propagados automaticamente

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

### 📋 Sistema Kanban (com Observer)
- **Colunas** - Criar, editar, reordenar, deletar
- **Cartões** - Criar, editar, mover entre colunas, deletar
  - **Eventos observáveis**: Cada operação gera notificações automáticas
  - **Métricas em tempo real**: Coleta automática de estatísticas
  - **Auditoria completa**: Log de todas as ações
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

### Cartões (com Observer)
```
GET    /cards/column/{columnId}            # Listar cartões
POST   /cards/column/{columnId}            # Criar cartão (→ observadores notificados)
GET    /cards/{id}                         # Detalhes do cartão
PUT    /cards/{id}                         # Atualizar cartão (→ observadores notificados)
DELETE /cards/{id}                         # Deletar cartão (→ observadores notificados)
PUT    /cards/{id}/move/{columnId}         # Mover cartão (→ observadores notificados)
POST   /cards/{id}/tags/{tagId}            # Atribuir tag (→ observadores notificados)
DELETE /cards/{id}/tags/{tagId}            # Remover tag (→ observadores notificados)
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
├── observers/                # Padrão Observer (NOVO)
│   ├── Observer.java
│   ├── Subject.java
│   ├── CardSubject.java
│   ├── ProjectMetricsObserver.java
│   ├── CardAuditObserver.java
│   └── NotificationObserver.java
├── repositories/             # Repositórios Spring Data
├── services/                 # Lógica de negócio
├── strategies/               # Padrão Strategy (Permissões)
└── state/participant/        # Padrão State (Papéis)
    ├── ParticipantState.java
    ├── CreatorState.java
    ├── AdminState.java
    └── MemberState.java
```

## 🎯 Benefícios dos Padrões Implementados

### Padrão State
1. **Encapsulamento de Regras**: Cada estado gerencia suas próprias regras de transição
2. **Extensibilidade**: Fácil adição de novos estados sem modificar código existente
3. **Manutenibilidade**: Elimina condicionais complexas no service

### Padrão Observer
1. **Desacoplamento**: Observadores independentes da lógica de negócio
2. **Extensibilidade**: Novos observadores podem ser adicionados sem modificar código existente
3. **Notificações automáticas**: Eventos são propagados sem intervenção manual
4. **Responsabilidade única**: Cada observador tem função específica

### Ambos seguem o princípio OCP (Open/Closed Principle)
- **Aberto para extensão**: Novos estados/observadores podem ser adicionados
- **Fechado para modificação**: Código existente não precisa ser alterado

## 🚀 Próximos Passos

- [ ] Implementar persistência dos logs de auditoria
- [ ] Adicionar notificações em tempo real via WebSocket
- [ ] Implementar sistema de comentários nos cartões
- [ ] Dashboard com métricas coletadas pelos observadores
- [ ] Sistema de relatórios baseado nos dados de auditoria
- [ ] API para integração com ferramentas externas

---

**Desenvolvido para a disciplina de Design de Software - PUCPR**
