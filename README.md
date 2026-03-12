
# 💈 GoBarber API

[![Codacy Badge](https://app.codacy.com/project/badge/Grade/b03264a7588f4fa68d85a67944a6f4e3)](https://app.codacy.com/gh/Go-BarberShop/Go-Barber/dashboard?utm_source=gh&utm_medium=referral&utm_content=&utm_campaign=Badge_grade)
[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://dev.java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-2.7.2-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16.3-blue.svg)](https://www.postgresql.org/)
[![Tests](https://img.shields.io/badge/Tests-152%20passing-success.svg)]()
[![Coverage](https://img.shields.io/badge/Coverage-85%25-brightgreen.svg)]()
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

<div align="center">
    <img height=200 src="https://media.tenor.com/QckgX69_GBoAAAAi/berlin-funny-haircut.gif">
    <h3>Sistema de Gerenciamento para Barbearias</h3>
</div>

---
## 👥 Equipe

Projeto desenvolvido pelos alunos da equipe **GoBarber**, do curso de Bacharelado em Ciência da Computação da  **UFAPE – Universidade Federal do Agreste de Pernambuco**.

### Integrantes
- Adenilson Ferreira Ramos  
- Magno Sillas Nunes Ramos Gomes  
- Nicoly Lana Lourenço Carvalho  
- Ricaelle Nascimento Teixeira Pontes

---

## 📋 Sobre o Projeto

O **GoBarber** é uma API REST completa para gerenciamento de barbearias, desenvolvida como projeto acadêmico para a disciplina de **Programação Web** da **UFAPE**.

### ✨ Principais Funcionalidades

| Módulo | Descrição |
|--------|-----------|
| 📅 **Agendamentos** | Gestão completa de horários e serviços |
| 💈 **Barbeiros** | Cadastro, especialidades e agenda |
| 👤 **Clientes** | Programa de fidelidade e preferências |
| 📦 **Estoque** | Controle de produtos e lotes |
| ⭐ **Avaliações** | Sistema de reviews multi-critério |
| 📋 **Lista de Espera** | Notificação automática de vagas |
| 💰 **Vendas** | Cupons e promoções |
| 💳 **Pagamentos** | Múltiplos métodos, cupons e reembolso |
| 📊 **Dashboard** | Métricas e relatórios gerenciais |
| 🔔 **Notificações** | Sistema completo de alertas |
| 📧 **Email** | Envio de promoções e avisos |

---

## 🆕 Recursos Recém-Implementados

### 💳 Sistema de Pagamentos
- ✅ Múltiplos métodos: Dinheiro, Cartão de Crédito/Débito, PIX
- ✅ Aplicação de cupons de desconto
- ✅ Validação de cupons expirados/inválidos
- ✅ Confirmação e cancelamento de pagamentos
- ✅ Sistema de reembolso completo
- ✅ Integração com programa de fidelidade

### 📧 Sistema de Email Marketing
- ✅ Envio de emails promocionais com templates FreeMarker
- ✅ Notificação automática de promoções para clientes
- ✅ Templates HTML personalizados
- ✅ Integração com Gmail SMTP

### 🔔 Sistema de Notificações
- ✅ Confirmação e lembretes de agendamento
- ✅ Notificação de cancelamento
- ✅ Alertas de promoções e cupons
- ✅ Notificação de pontos de fidelidade
- ✅ Mensagem de aniversário personalizada
- ✅ Boas-vindas a novos clientes
- ✅ Solicitação de avaliação pós-serviço
- ✅ Marcação de lidas e contagem de não lidas

### 📊 Dashboard Gerencial
- ✅ Receita total e crescimento percentual
- ✅ Ticket médio por período
- ✅ Total de transações e agendamentos
- ✅ Análise de clientes novos vs retornantes
- ✅ Taxa de retenção de clientes
- ✅ Média de avaliações e taxa de recomendação
- ✅ Taxa de ocupação dos barbeiros
- ✅ Top barbeiros, serviços e clientes
- ✅ Distribuição por método de pagamento
- ✅ Série temporal de receita diária

---

## 🛠️ Tecnologias Utilizadas

| Tecnologia | Versão | Descrição |
|------------|--------|-----------|
| ☕ [Java](https://dev.java/learn/language-basics/) | 17 | Linguagem principal |
| 🌿 [Spring Boot](https://spring.io/projects/spring-boot) | 2.7.2 | Framework web |
| 🔐 [Spring Security](https://spring.io/projects/spring-security) | - | Autenticação JWT |
| 📊 [Spring Data JPA](https://spring.io/projects/spring-data-jpa) | - | Persistência de dados |
| 🪶 [Maven](https://maven.apache.org) | 3.8.1+ | Gerenciador de dependências |
| 🐘 [PostgreSQL](https://www.postgresql.org) | 16.3 | Banco de dados |
| 📜 [Liquibase](https://www.liquibase.org/) | 4.16.1 | Versionamento de banco |
| 📖 [Swagger/OpenAPI](https://swagger.io/) | 1.6.8 | Documentação da API |
| 📧 [Spring Mail](https://spring.io/guides/gs/sending-email) | - | Envio de emails |
| 🧪 [JaCoCo](https://www.jacoco.org/jacoco/) | 0.8.11 | Cobertura de testes |

---

## 🚀 Como Executar o Projeto

### 📋 Pré-requisitos

Antes de começar, certifique-se de ter instalado:

| Ferramenta | Versão Mínima | Download |
|------------|---------------|----------|
| ☕ **Java JDK** | 17 | [Download](https://www.oracle.com/java/technologies/downloads/#java17) |
| 🪶 **Maven** | 3.8+ | [Download](https://maven.apache.org/download.cgi) |
| 🐘 **PostgreSQL** | 16+ | [Download](https://www.postgresql.org/download/) |
| 🔧 **Git** | Qualquer | [Download](https://git-scm.com/downloads) |

### 📥 Passo 1: Clone o Repositório

```bash
git clone https://github.com/Go-BarberShop/Go-Barber.git
cd Go-Barber
```

### 🐘 Passo 2: Configure o Banco de Dados

1. **Abra o terminal do PostgreSQL** (psql) ou use uma ferramenta como pgAdmin

2. **Execute os comandos SQL:**
```sql
-- Criar o banco de dados
CREATE DATABASE gobarber;

-- Conectar ao banco
\c gobarber

-- Criar o schema
CREATE SCHEMA gobarber;
```

3. **Configure as credenciais** no arquivo `src/main/resources/application.properties`:
```properties
# Configuração do Banco de Dados
spring.datasource.url=jdbc:postgresql://localhost:5432/gobarber
spring.datasource.username=SEU_USUARIO
spring.datasource.password=SUA_SENHA

# Configuração de Email (opcional)
spring.mail.username=seu_email@gmail.com
spring.mail.password=sua_senha_de_app
```

> ⚠️ **Importante:** Para o email funcionar com Gmail, você precisa gerar uma [senha de app](https://support.google.com/accounts/answer/185833).

### 🔨 Passo 3: Compile o Projeto

```bash
# Compilar o projeto
mvn clean compile

# Ou use o wrapper (não precisa ter Maven instalado)
./mvnw clean compile     # Linux/Mac
mvnw.cmd clean compile   # Windows
```

### ✅ Passo 4: Execute os Testes

```bash
# Executar todos os testes
mvn test

# Resultado esperado: 152 testes passando ✅
```

### 🚀 Passo 5: Inicie a Aplicação

```bash
# Iniciar o servidor
mvn spring-boot:run

# Ou use o wrapper
./mvnw spring-boot:run     # Linux/Mac
mvnw.cmd spring-boot:run   # Windows
```

A aplicação estará disponível em: **http://localhost:8080**

### 🌐 Passo 6: Acesse a API

| Recurso | URL | Descrição |
|---------|-----|-----------|
| 📖 **Swagger UI** | http://localhost:8080/swagger-ui.html | Documentação interativa |
| 🔗 **API Base** | http://localhost:8080 | Endpoint base da API |

---

## � Executando com Docker

### Pré-requisitos Docker

- 🐳 [Docker](https://www.docker.com/get-started) instalado
- 🐙 [Docker Compose](https://docs.docker.com/compose/install/) (geralmente incluído no Docker Desktop)

### 🚀 Quick Start com Docker

```bash
# 1. Clone o repositório
git clone https://github.com/Go-BarberShop/Go-Barber.git
cd Go-Barber

# 2. Copie o arquivo de exemplo de variáveis de ambiente
cp .env.example .env

# 3. (Opcional) Edite o .env com suas configurações
notepad .env   # Windows
nano .env      # Linux/Mac

# 4. Inicie os containers
docker-compose up -d

# A aplicação estará disponível em: http://localhost:8080
```

### 📋 Comandos Docker Úteis

```bash
# Iniciar todos os serviços
docker-compose up -d

# Ver logs em tempo real
docker-compose logs -f

# Ver logs de um serviço específico
docker-compose logs -f api
docker-compose logs -f postgres

# Parar todos os serviços
docker-compose down

# Parar e remover volumes (⚠️ apaga dados do banco)
docker-compose down -v

# Reconstruir as imagens (após alterações no código)
docker-compose up -d --build

# Ver status dos containers
docker-compose ps

# Acessar terminal do container da API
docker exec -it gobarber-api sh

# Acessar terminal do PostgreSQL
docker exec -it gobarber-db psql -U gobarber -d gobarber
```

### 🔧 Com pgAdmin (Interface Gráfica para PostgreSQL)

```bash
# Iniciar com pgAdmin
docker-compose --profile tools up -d

# Acessar pgAdmin em: http://localhost:5050
# Email: admin@gobarber.com
# Senha: admin123

# Configurar conexão no pgAdmin:
# Host: postgres
# Port: 5432
# Database: gobarber
# Username: gobarber
# Password: gobarber123
```

### 🌱 Dados de Exemplo (Seeder)

O projeto inclui um **script de seed** que popula o banco com dados de exemplo. Os dados incluem:

| Entidade | Quantidade | Descrição |
|----------|------------|-----------|
| 👤 Usuários | 7 | Admin + 4 barbeiros + 2 secretárias |
| 💈 Barbeiros | 4 | Com especialidades diferentes |
| ✂️ Serviços | 12 | Cortes, barba, tratamentos |
| 📦 Produtos | 15 | Pomadas, óleos, shampoos |
| 📦 Estoque | 18 | Lotes com validade |
| 💰 Promoções | 8 | Cupons de desconto |
| 📅 Agendamentos | 14 | Para os próximos dias |

**Credenciais de teste:**
```
👤 Admin: admin@gobarber.com / 123456
💈 Barbeiro: carlos.barbeiro@gobarber.com / 123456
```

**⚠️ IMPORTANTE:** O seeder deve ser executado **após** a primeira inicialização da API (quando o Liquibase criar as tabelas).

**Para executar o seeder (após a API iniciar pela primeira vez):**
```bash
# Via Docker
docker exec -it gobarber-db psql -U gobarber -d gobarber -f /seed-data.sql

# Localmente
psql -U gobarber -d gobarber -f docker/seed-data.sql
```

**Para resetar o banco e recarregar os dados:**
```bash
# 1. Remove volumes e recria containers
docker-compose down -v
docker-compose up -d

# 2. Aguarde a API iniciar e criar as tabelas (~30 segundos)

# 3. Execute o seeder
docker exec -it gobarber-db psql -U gobarber -d gobarber -f /seed-data.sql
```

### 📊 Variáveis de Ambiente

| Variável | Padrão | Descrição |
|----------|--------|-----------|
| `DB_USER` | gobarber | Usuário do PostgreSQL |
| `DB_PASSWORD` | gobarber123 | Senha do PostgreSQL |
| `DB_PORT` | 5432 | Porta do PostgreSQL |
| `API_PORT` | 8080 | Porta da API |
| `JWT_SECRET` | - | Chave secreta para JWT |
| `MAIL_USERNAME` | - | Email para envio |
| `MAIL_PASSWORD` | - | Senha de app do Gmail |

---

## �️ Rotas da API

Base URL: `http://localhost:8080`

### 🔐 Autenticação (`/auth`)

| Método | Rota | Descrição |
|--------|------|-----------|
| `POST` | `/auth` | Login (retorna JWT + role) |
| `POST` | `/auth/logout` | Logout (invalida token) |

**Exemplo de login:**
```json
POST /auth
{
  "login": "admin@gobarber.com",
  "password": "123456"
}
```

---

### � Clientes (`/client`)

| Método | Rota | Descrição |
|--------|------|-----------|
| `POST` | `/client` | Cadastrar cliente |
| `PUT` | `/client/{id}` | Atualizar cliente |
| `DELETE` | `/client/{id}` | Deletar cliente |
| `GET` | `/client/{id}` | Buscar por ID |
| `GET` | `/client` | Listar todos (paginado) |
| `GET` | `/client/email/{email}` | Buscar por email |
| `GET` | `/client/{id}/loyalty` | Pontos de fidelidade |
| `POST` | `/client/{id}/loyalty/add` | Adicionar pontos |
| `POST` | `/client/{id}/loyalty/redeem` | Resgatar pontos |

**Níveis de Fidelidade:**
| Nível | Pontos | Benefícios |
|-------|--------|------------|
| 🥉 BRONZE | 0-99 | Desconto 5% |
| 🥈 SILVER | 100-499 | Desconto 10% |
| 🥇 GOLD | 500-999 | Desconto 15% |
| 💎 PLATINUM | 1000+ | Desconto 20% + Brindes |

---

### �💈 Barbeiros (`/barber`)

| Método | Rota | Descrição |
|--------|------|-----------|
| `POST` | `/barber` | Criar barbeiro (multipart com foto) |
| `POST` | `/barber/create-without-photo` | Criar barbeiro sem foto |
| `POST` | `/barber/service` | Adicionar serviço ao barbeiro |
| `POST` | `/barber/service/remove` | Remover serviço do barbeiro |
| `PUT` | `/barber/{id}` | Atualizar barbeiro |
| `DELETE` | `/barber/{id}` | Deletar barbeiro |
| `GET` | `/barber/{id}` | Buscar barbeiro por ID |
| `GET` | `/barber` | Listar todos os barbeiros (paginado) |
| `GET` | `/barber/{id}/photo` | Obter foto do barbeiro |

---

### 📅 Agendamentos (`/appointments`)

| Método | Rota | Descrição |
|--------|------|-----------|
| `POST` | `/appointments` | Criar agendamento |
| `PUT` | `/appointments/{id}` | Atualizar agendamento |
| `DELETE` | `/appointments/{id}` | Cancelar agendamento |
| `GET` | `/appointments/{id}` | Buscar agendamento por ID |
| `GET` | `/appointments` | Listar todos (paginado) |
| `GET` | `/appointments/barber/{barberId}` | Agendamentos por barbeiro |
| `GET` | `/appointments/history/barber` | Histórico por barbeiro |
| `GET` | `/appointments/history` | Histórico do usuário logado |

---

### ✂️ Serviços (`/services`)

| Método | Rota | Descrição |
|--------|------|-----------|
| `POST` | `/services` | Criar serviço |
| `PUT` | `/services/{id}` | Atualizar serviço |
| `DELETE` | `/services/{id}` | Deletar serviço |
| `GET` | `/services/{id}` | Buscar serviço por ID |
| `GET` | `/services` | Listar todos os serviços |

---

### 📦 Produtos (`/product`)

| Método | Rota | Descrição |
|--------|------|-----------|
| `POST` | `/product` | Criar produto |
| `PUT` | `/product/{id}` | Atualizar produto |
| `DELETE` | `/product/{id}` | Deletar produto |
| `GET` | `/product/{id}` | Buscar produto por ID |
| `GET` | `/product` | Listar todos (paginado) |

---

### 📦 Estoque (`/stock`)

| Método | Rota | Descrição |
|--------|------|-----------|
| `POST` | `/stock` | Adicionar item ao estoque |
| `PUT` | `/stock/{id}` | Atualizar item do estoque |
| `DELETE` | `/stock/{id}` | Remover item do estoque |
| `GET` | `/stock/{id}` | Buscar item por ID |
| `GET` | `/stock/product/{id}` | Estoque por produto |

---

### 💰 Vendas/Promoções (`/sale`)

| Método | Rota | Descrição |
|--------|------|-----------|
| `POST` | `/sale` | Criar promoção |
| `POST` | `/sale/email/notify` | Enviar email promocional |
| `PUT` | `/sale/{id}` | Atualizar promoção |
| `DELETE` | `/sale/{id}` | Deletar promoção |
| `GET` | `/sale` | Listar todas (paginado) |
| `GET` | `/sale/valid` | Listar promoções válidas |
| `GET` | `/sale/{id}` | Buscar promoção por ID |
| `GET` | `/sale/coupon/{coupon}` | Buscar por código do cupom |

---

### ⭐ Avaliações (`/review`)

| Método | Rota | Descrição |
|--------|------|-----------|
| `POST` | `/review` | Criar avaliação |
| `POST` | `/review/{id}/reply` | Responder avaliação |
| `POST` | `/review/{id}/hide` | Ocultar avaliação |
| `POST` | `/review/{id}/show` | Mostrar avaliação |
| `GET` | `/review` | Listar avaliações (paginado) |
| `GET` | `/review/{id}` | Buscar avaliação por ID |
| `GET` | `/review/barber/{barberId}` | Avaliações do barbeiro |
| `GET` | `/review/barber/{barberId}/top` | Top avaliações do barbeiro |
| `GET` | `/review/barber/{barberId}/average` | Média do barbeiro |
| `GET` | `/review/barber/{barberId}/count` | Total de avaliações |
| `GET` | `/review/barber/{barberId}/distribution` | Distribuição de notas |
| `GET` | `/review/client/{clientId}` | Avaliações do cliente |
| `GET` | `/review/ranking/barbers` | Ranking de barbeiros |
| `GET` | `/review/stats/average` | Média geral |
| `GET` | `/review/stats/recommendation-rate` | Taxa de recomendação |
| `GET` | `/review/pending-reply` | Avaliações sem resposta |

---

### 💳 Pagamentos (`/payment`)

| Método | Rota | Descrição |
|--------|------|-----------|
| `POST` | `/payment` | Criar pagamento |
| `POST` | `/payment/{id}/confirm` | Confirmar pagamento |
| `POST` | `/payment/{id}/cancel` | Cancelar pagamento |
| `POST` | `/payment/{id}/refund` | Reembolsar pagamento |
| `GET` | `/payment` | Listar todos (paginado) |
| `GET` | `/payment/{id}` | Buscar por ID |
| `GET` | `/payment/client/{clientId}` | Pagamentos do cliente |
| `GET` | `/payment/barber/{barberId}` | Pagamentos do barbeiro |
| `GET` | `/payment/status/{status}` | Pagamentos por status |

**Métodos de Pagamento Suportados:**
- 💵 Dinheiro (CASH)
- 💳 Cartão de Crédito (CREDIT_CARD)
- 💳 Cartão de Débito (DEBIT_CARD)
- 📱 PIX

---

### 🔔 Notificações (`/notification`)

| Método | Rota | Descrição |
|--------|------|-----------|
| `GET` | `/notification/client/{clientId}` | Notificações do cliente |
| `GET` | `/notification/unread/{clientId}` | Não lidas do cliente |
| `GET` | `/notification/count/{clientId}` | Contagem de não lidas |
| `POST` | `/notification/{id}/read` | Marcar como lida |
| `POST` | `/notification/read-all/{clientId}` | Marcar todas como lidas |
| `DELETE` | `/notification/{id}` | Deletar notificação |

**Tipos de Notificação:**
- ✅ Confirmação de agendamento
- ⏰ Lembrete de agendamento
- ❌ Cancelamento de agendamento
- 🎉 Promoções e cupons
- 🏆 Pontos de fidelidade
- 🎂 Aniversário
- 👋 Boas-vindas
- ⭐ Solicitação de avaliação

---

### 📊 Dashboard (`/dashboard`)

| Método | Rota | Descrição |
|--------|------|-----------|
| `GET` | `/dashboard` | Métricas gerais |
| `GET` | `/dashboard/financial` | Relatório financeiro |
| `GET` | `/dashboard/performance` | Desempenho dos barbeiros |

**Métricas Disponíveis:**
- 💰 Receita total e crescimento
- 🎫 Ticket médio
- 📅 Total de agendamentos
- 👥 Clientes novos vs retornantes
- ⭐ Média de avaliações
- 📈 Taxa de recomendação
- 🏆 Top barbeiros e serviços

---

### ⏳ Lista de Espera (`/waitlist`)

| Método | Rota | Descrição |
|--------|------|-----------|
| `POST` | `/waitlist` | Adicionar à lista de espera |
| `POST` | `/waitlist/{id}/notify` | Notificar cliente |
| `POST` | `/waitlist/{id}/convert` | Converter para agendamento |
| `POST` | `/waitlist/process-expired` | Processar expirados |
| `PUT` | `/waitlist/{id}/priority` | Alterar prioridade |
| `PUT` | `/waitlist/{id}/notes` | Adicionar notas |
| `DELETE` | `/waitlist/{id}` | Remover da lista |
| `GET` | `/waitlist` | Listar todos |
| `GET` | `/waitlist/{id}` | Buscar por ID |
| `GET` | `/waitlist/barber/{barberId}` | Por barbeiro |
| `GET` | `/waitlist/barber/{barberId}/waiting` | Aguardando por barbeiro |
| `GET` | `/waitlist/client/{clientId}` | Por cliente |
| `GET` | `/waitlist/service/{serviceId}` | Por serviço |
| `GET` | `/waitlist/stats` | Estatísticas gerais |
| `GET` | `/waitlist/stats/barber/{barberId}` | Estatísticas por barbeiro |

---

### 🗓️ Agenda do Barbeiro (`/barber-schedule`)

| Método | Rota | Descrição |
|--------|------|-----------|
| `POST` | `/barber-schedule/block` | Bloquear horário |
| `POST` | `/barber-schedule/vacation` | Registrar férias |
| `POST` | `/barber-schedule/day-off` | Registrar folga |
| `POST` | `/barber-schedule/lunch-break` | Definir almoço |
| `POST` | `/barber-schedule/{id}/deactivate` | Desativar bloqueio |
| `PUT` | `/barber-schedule/{id}` | Atualizar agenda |
| `DELETE` | `/barber-schedule/{id}` | Deletar agenda |
| `GET` | `/barber-schedule/barber/{barberId}` | Agenda do barbeiro |
| `GET` | `/barber-schedule/barber/{barberId}/vacations` | Férias do barbeiro |
| `GET` | `/barber-schedule/barber/{barberId}/recurring` | Bloqueios recorrentes |
| `GET` | `/barber-schedule/barber/{barberId}/vacation-days` | Dias de férias |
| `GET` | `/barber-schedule/availability/check` | Verificar disponibilidade |
| `GET` | `/barber-schedule/availability/slots` | Slots disponíveis |
| `GET` | `/barber-schedule/availability/barbers` | Barbeiros disponíveis |

---

### 👩‍💼 Secretárias (`/secretary`)

| Método | Rota | Descrição |
|--------|------|-----------|
| `POST` | `/secretary` | Criar secretária (multipart) |
| `POST` | `/secretary/create-without-photo` | Criar sem foto |
| `PUT` | `/secretary/{id}` | Atualizar secretária |
| `DELETE` | `/secretary/{id}` | Deletar secretária |
| `GET` | `/secretary/{id}` | Buscar por ID |
| `GET` | `/secretary` | Listar todas (paginado) |
| `GET` | `/secretary/{id}/photo` | Obter foto |

---

### 📍 Endereços (`/address`)

| Método | Rota | Descrição |
|--------|------|-----------|
| `GET` | `/address/{id}` | Buscar endereço por ID |

---

### 📖 Documentação Swagger

Acesse a documentação interativa da API:
```
http://localhost:8080/swagger-ui.html
```

---

## �🔧 Comandos Maven (Desenvolvimento Local)

```bash
# Compilar sem executar testes
mvn compile -DskipTests

# Executar testes com relatório de cobertura (JaCoCo)
mvn clean test
# Relatório gerado em: target/site/jacoco/index.html

# Gerar pacote JAR
mvn package -DskipTests

# Executar JAR gerado
java -jar target/gobarber-0.0.1-SNAPSHOT.jar

# Limpar arquivos gerados
mvn clean
```

---

## 📁 Estrutura do Projeto

```
src/main/java/br/edu/ufape/gobarber/
├── 📂 config/              # Configurações (Swagger, CORS, etc)
│   └── OpenApiConfig.java
├── 📂 controller/          # REST Controllers (14 endpoints)
│   ├── AddressController.java
│   ├── AppointmentController.java
│   ├── BarberController.java
│   ├── BarberScheduleController.java
│   ├── ClientController.java
│   ├── DashboardController.java
│   ├── NotificationController.java
│   ├── PaymentController.java
│   ├── ProductController.java
│   ├── ProductStockController.java
│   ├── ReviewController.java
│   ├── SaleController.java
│   ├── SecretaryController.java
│   ├── ServicesController.java
│   └── WaitListController.java
├── 📂 dto/                 # Data Transfer Objects
│   ├── address/
│   ├── appointment/
│   ├── barber/
│   ├── client/
│   ├── dashboard/
│   ├── notification/
│   ├── payment/
│   ├── product/
│   ├── review/
│   ├── sale/
│   └── ...
├── 📂 exceptions/          # Exceções customizadas
├── 📂 model/               # Entidades JPA (20 modelos)
│   ├── Appointment.java
│   ├── Barber.java
│   ├── BarberSchedule.java
│   ├── Client.java
│   ├── Notification.java
│   ├── Payment.java
│   ├── Review.java
│   ├── Sale.java
│   ├── Services.java
│   ├── WaitList.java
│   └── ...
├── 📂 repository/          # Interfaces de repositório
├── 📂 security/            # Autenticação JWT
│   ├── JwtAuthenticationFilter.java
│   ├── JwtTokenProvider.java
│   └── SecurityConfig.java
└── 📂 service/             # Lógica de negócio (22 serviços)
    ├── AppointmentService.java
    ├── AuditService.java
    ├── BarberScheduleService.java
    ├── BarberService.java
    ├── ClientService.java
    ├── DashboardService.java      # 🆕 Métricas e relatórios
    ├── EmailService.java          # 🆕 Envio de emails
    ├── NotificationService.java   # 🆕 Sistema de notificações
    ├── PaymentService.java        # 🆕 Processamento de pagamentos
    ├── ProductService.java
    ├── ProductStockService.java
    ├── ReviewService.java
    ├── SaleService.java
    ├── ServicesService.java
    ├── WaitListService.java
    └── ...

src/test/java/br/edu/ufape/gobarber/
└── 📂 service/             # Testes unitários (152 testes)
    ├── AuditServiceTest.java
    ├── BarberScheduleServiceTest.java
    ├── BarberServiceTest.java
    ├── ClientServiceTest.java
    ├── DashboardServiceTest.java   # 🆕 7 testes
    ├── EmailServiceTest.java       # 🆕 5 testes
    ├── NotificationServiceTest.java # 🆕 16 testes
    ├── PaymentServiceTest.java     # 🆕 17 testes
    ├── ProductServiceTest.java
    ├── ReviewServiceTest.java
    ├── SaleServiceTest.java
    ├── WaitListServiceTest.java
    └── ...

src/main/resources/
├── application.properties      # Configurações da aplicação
├── 📂 db/changelog/           # Migrations Liquibase
│   ├── db.changelog-master.xml
│   └── migrations/
└── 📂 templates/              # Templates FreeMarker para emails
    └── sale.ftl               # Template de email promocional
```

---

## 🧪 Testes e Cobertura

```bash
# Executar todos os testes com cobertura
mvn clean test

# Ver relatório de cobertura
# Abra: target/site/jacoco/index.html
```

### 📊 Métricas Atuais

| Métrica | Valor |
|---------|-------|
| **Total de Testes** | 152 ✅ |
| **Cobertura (Instruções)** | 85% |
| **Cobertura (Services)** | 95% |
| **Cobertura (Models)** | 90% |

### 📋 Suites de Teste

| Suite | Testes | Descrição |
|-------|--------|-----------|
| AuditServiceTest | 17 | Auditoria e logs |
| ClientServiceTest | 17 | Gestão de clientes |
| ReviewServiceTest | 21 | Sistema de avaliações |
| BarberScheduleServiceTest | 16 | Agenda dos barbeiros |
| WaitListServiceTest | 16 | Lista de espera |
| ProductServiceTest | 10 | Gestão de produtos |
| SaleServiceTest | 10 | Vendas e promoções |
| PaymentServiceTest | 17 | Pagamentos e cupons |
| NotificationServiceTest | 16 | Sistema de notificações |
| EmailServiceTest | 5 | Envio de emails promocionais |
| DashboardServiceTest | 7 | Métricas e relatórios |

---

## 📊 Status do Projeto

<div align="center">

| Componente | Status |
|------------|--------|
| 🔧 Backend API | ✅ Funcional |
| 📊 Banco de Dados | ✅ Configurado |
| 🔐 Autenticação | ✅ JWT Implementado |
| 📖 Documentação | ✅ Swagger |
| 🧪 Testes | ✅ 152 passando |
| 📈 Cobertura | ✅ 85% |
| 🎨 Frontend | 🚧 Em desenvolvimento |

**Progresso Geral:**

🟩🟩🟩🟩🟩🟩🟩🟩🟩⬜ **90%**

</div>

---

## ❓ Solução de Problemas

### Erro: "Cannot connect to database"
```bash
# Verifique se o PostgreSQL está rodando
# Windows (PowerShell como Admin):
Get-Service postgresql*

# Linux:
sudo systemctl status postgresql
```

### Erro: "JAVA_HOME not set"
```bash
# Windows (PowerShell):
$env:JAVA_HOME = "C:\Program Files\Java\jdk-17"

# Linux/Mac:
export JAVA_HOME=/usr/lib/jvm/java-17
```

### Erro: "Port 8080 already in use"
```bash
# Windows - encontrar processo:
netstat -ano | findstr :8080
taskkill /PID <PID> /F

# Linux:
lsof -i :8080
kill -9 <PID>
```

---

## 🔗 Links Úteis

- 📚 [Documentação Técnica Completa](docs/Relatório%20Programação%20Web.pdf)
- 📖 [Swagger API Docs](http://localhost:8080/swagger-ui.html)
- 🐛 [Reportar Bug](https://github.com/Go-BarberShop/Go-Barber/issues)

---

## � Changelog

### v2.0.0 (Dezembro 2025)
**Novas Funcionalidades:**
- ✨ Sistema de Pagamentos completo (PIX, Cartão, Dinheiro)
- ✨ Dashboard gerencial com métricas em tempo real
- ✨ Sistema de Notificações push para clientes
- ✨ Email Marketing com templates FreeMarker
- ✨ Programa de Fidelidade com níveis (Bronze → Platinum)

**Melhorias:**
- 🧪 Cobertura de testes aumentada de 107 para 152 testes
- 📊 Cobertura de código aumentada para 85%
- 🔧 Refatoração de interfaces e controllers
- 📖 Documentação completa atualizada

**Testes Adicionados:**
- PaymentServiceTest (17 testes)
- NotificationServiceTest (16 testes) 
- DashboardServiceTest (7 testes)
- EmailServiceTest (5 testes)

### v1.0.0 (Novembro 2025)
- 🎉 Lançamento inicial
- ✅ CRUD completo de todas as entidades
- ✅ Autenticação JWT
- ✅ Documentação Swagger
- ✅ Dockerização do projeto

---

## 📄 Licença

Este projeto está sob a licença MIT - veja o arquivo [LICENSE](LICENSE) para detalhes.

---

<div align="center">
    <p>Feito com ☕ e 💈</p>
</div>
