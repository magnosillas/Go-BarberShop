# 💈 GoBarber - Estratégias de Negócio para sua Barbearia

<div align="center">
    <img height=150 src="https://media.tenor.com/QckgX69_GBoAAAAi/berlin-funny-haircut.gif">
    <h3>Guia Prático para Crescer seu Negócio com o GoBarber</h3>
</div>

---

## 📋 Índice

- [Como Usar o Sistema](#-como-usar-o-sistema)
- [Estratégias para Aumentar Clientes](#-estratégias-para-aumentar-clientes)
- [Fidelização de Clientes](#-fidelização-de-clientes)
- [Gestão Financeira](#-gestão-financeira)
- [Controle de Estoque](#-controle-de-estoque)
- [Dicas de Sucesso](#-dicas-de-sucesso)

---

## 🖥️ Como Usar o Sistema

### Primeiros Passos

```
1️⃣ Cadastre sua barbearia e barbeiros
2️⃣ Configure os serviços oferecidos (corte, barba, etc.)
3️⃣ Defina os horários de funcionamento
4️⃣ Cadastre seus produtos em estoque
5️⃣ Compartilhe o link de agendamento com seus clientes
```

### Módulos do Sistema

| Módulo | O que faz | Como usar |
|--------|-----------|-----------|
| **📅 Agendamentos** | Gerencia horários | Clientes agendam online, você só confirma |
| **👥 Clientes** | Cadastro completo | Salve preferências, pontos e histórico de cada cliente |
| **💈 Barbeiros** | Gestão da equipe | Defina especialidades, horários e bloqueios de agenda |
| **✂️ Serviços** | Catálogo de serviços | Liste preços e tempo de cada serviço |
| **📦 Estoque** | Controle de produtos | Saiba quando repor produtos, controle lotes |
| **⭐ Avaliações** | Feedback dos clientes | Melhore com base nas opiniões e notas |
| **⏳ Lista de Espera** | Horários lotados | Cliente entra na fila com prioridade e é notificado |
| **💰 Promoções** | Cupons e descontos | Crie ofertas para atrair e fidelizar clientes |
| **💳 Pagamentos** | Controle financeiro | Registre pagamentos via Pix, cartão ou dinheiro |
| **🔔 Notificações** | Comunicação automática | Lembretes, confirmações e avisos por email |
| **📊 Dashboard** | Visão gerencial | Métricas de faturamento, agendamentos e performance |

---

## 📈 Estratégias para Aumentar Clientes

### 1. Agendamento Online 24/7

**Problema:** Cliente quer agendar às 22h, mas você não atende WhatsApp.

**Solução com GoBarber:**
```
✅ Link de agendamento disponível 24 horas
✅ Cliente escolhe barbeiro, serviço e horário
✅ Confirmação automática por email
✅ Lembrete antes do horário marcado
```

**Como fazer:**
1. Acesse `Configurações > Link de Agendamento`
2. Copie seu link personalizado
3. Divulgue nas redes sociais e cartão de visita

---

### 2. Reduza o No-Show (Faltas)

**Problema:** 30% dos clientes não aparecem no horário marcado.

**Solução com GoBarber:**
```
✅ Lembretes automáticos por email (24h e 2h antes)
✅ Histórico completo de agendamentos por cliente
✅ Lista de espera inteligente com prioridades
✅ Sistema de avaliações mostra clientes confiáveis
✅ Controle de pontos de fidelidade incentiva retorno
```

**Como fazer:**
1. O sistema de notificações envia lembretes automáticos
2. Consulte o histórico do cliente antes de confirmar
3. Use a lista de espera para preencher cancelamentos
4. Clientes com mais pontos têm menor índice de falta

---

### 3. Use a Lista de Espera Inteligente

**Problema:** Horários de pico sempre lotados, clientes desistem.

**Solução com GoBarber:**
```
✅ Cliente entra na fila com data preferida
✅ Sistema de prioridades (normal, alta, urgente)
✅ Flexibilidade de datas configurável
✅ Notificação automática quando abre vaga
✅ Conversão direta para agendamento
✅ Notas personalizadas para preferências
```

**Como fazer:**
1. Acesse `/api/waitlist` para gerenciar a fila
2. Defina prioridade baseada no histórico do cliente
3. Quando houver cancelamento, notifique pela lista
4. Cliente Gold tem prioridade automática

---

### 4. Promoções Estratégicas

**Problema:** Dias de semana vazios, fim de semana lotado.

**Solução com GoBarber:**
```
✅ Crie cupons de desconto para dias fracos
✅ Promoções para horários específicos
✅ Desconto para primeira visita
```

**Exemplos de promoções:**

| Promoção | Cupom | Desconto |
|----------|-------|----------|
| Segunda-feira | `SEGUNDA20` | 20% off |
| Primeira visita | `BEMVINDO` | 15% off |
| Aniversariante | `NIVER` | Corte grátis |
| Pacote 5 cortes | `PACOTE5` | 1 grátis |

**Como fazer:**
1. Acesse `Vendas > Nova Promoção`
2. Defina nome, cupom e período
3. Divulgue nas redes sociais

---

## ⭐ Fidelização de Clientes

### Programa de Fidelidade

O GoBarber possui sistema de pontos e níveis automático:

```
🥉 BRONZE (0-99 pontos)
   └── Benefício: Acesso ao sistema de agendamento
   └── Ganho: 10 pontos por agendamento

🥈 SILVER (100-499 pontos)
   └── Benefício: 5% de desconto em produtos
   └── Ganho: 12 pontos por agendamento (+20%)

🥇 GOLD (500+ pontos)
   └── Benefício: 10% de desconto + prioridade na lista de espera
   └── Ganho: 15 pontos por agendamento (+50%)
```

**Como funciona:**
- Cliente ganha **pontos** a cada agendamento realizado
- Pontos acumulam e sobem o **nível de fidelidade**
- Sistema de **avaliações** aumenta engajamento
- Clientes podem deixar **reviews** com nota e comentário

**Endpoints disponíveis:**
- `GET /api/clients/{id}` - Ver pontos e nível do cliente
- `PUT /api/clients/{id}/points` - Atualizar pontos
- `GET /api/reviews/client/{id}` - Histórico de avaliações

---

### Avaliações e Reputação

**Por que é importante:** Clientes confiam em avaliações online.

**Sistema de Reviews:**
```
⭐ Notas de 1 a 5 estrelas
💬 Comentários opcionais detalhados
📅 Data do atendimento registrada
💈 Avaliação por barbeiro específico
📊 Média calculada automaticamente
```

**Endpoints disponíveis:**
- `POST /api/reviews` - Cliente deixa avaliação
- `GET /api/reviews/barber/{id}` - Avaliações do barbeiro
- `GET /api/reviews/barber/{id}/average` - Média de notas
- `GET /api/reviews/barber/{id}/stats` - Estatísticas completas

**Dica:** Peça para clientes satisfeitos deixarem avaliação! Barbeiros bem avaliados atraem mais clientes.

---

### Conheça seu Cliente

O cadastro completo permite personalizar o atendimento:

| Informação | Como usar |
|------------|-----------|
| **Barbeiro preferido** | Sugira agendamento com o favorito |
| **Último corte** | Pergunte se quer repetir |
| **Aniversário** | Envie promoção especial |
| **Histórico** | Saiba exatamente o que ele gosta |

---

## 💰 Gestão Financeira

### Dashboard Inteligente

Acompanhe seu faturamento em tempo real com o módulo de Dashboard:

```
📊 Dashboard Gerencial
├── 💵 Receita total e por período
├── 📅 Agendamentos de hoje/semana/mês
├── 💈 Performance por barbeiro
├── 👥 Métricas de clientes (novos, recorrentes)
├── ⭐ Média de avaliações
└── 📎 Relatórios exportáveis
```

**Endpoints do Dashboard:**
- `GET /api/dashboard/summary` - Visão geral
- `GET /api/dashboard/appointments/today` - Agendamentos do dia
- `GET /api/dashboard/revenue` - Faturamento por período
- `GET /api/dashboard/barbers/performance` - Ranking de barbeiros

### Sistema de Pagamentos

Controle completo de pagamentos:

| Método | Status | Rastreamento |
|--------|--------|-------------|
| 🟢 **PIX** | Instantâneo | ID da transação |
| 🟡 **Cartão Crédito** | Confirmado | Últimos 4 dígitos |
| 🟡 **Cartão Débito** | Confirmado | Últimos 4 dígitos |
| 🟢 **Dinheiro** | Imediato | Registro manual |
| 🔵 **Pendente** | Aguardando | Notificação automática |

### Relatórios Disponíveis

| Relatório | Endpoint | Informação |
|-----------|----------|------------|
| **Sumário** | `/api/dashboard/summary` | Visão geral da barbearia |
| **Receita** | `/api/dashboard/revenue` | Faturamento por período |
| **Barbeiros** | `/api/dashboard/barbers/performance` | Ranking e métricas |
| **Pagamentos** | `/api/payments/by-date-range` | Histórico de pagamentos |
| **Agendamentos** | `/api/appointments` | Lista paginada e filtrada |

---

## 📦 Controle de Estoque

### Cadastro de Produtos

Registre todos os produtos da sua barbearia com controle de lotes:

```
📦 Produto: Pomada Modeladora
├── 🏷️ Marca: HairStyle
├── 💵 Preço de venda: R$ 45,00
├── 📏 Tamanho: 150g
└── 📝 Descrição: Pomada efeito matte

📦 Estoque (Lote LOTE2024-001):
├── 📊 Quantidade: 25 unidades
├── 📅 Aquisição: 15/06/2024
└── ⏰ Validade: 15/06/2026
```

### API de Estoque

| Operação | Endpoint | Método |
|----------|----------|--------|
| Listar produtos | `/api/products` | GET |
| Criar produto | `/api/products` | POST |
| Ver estoque | `/api/product-stock` | GET |
| Adicionar lote | `/api/product-stock` | POST |
| Baixa no estoque | `/api/product-stock/{id}` | PUT |

### Alertas e Gestão de Lotes

O sistema controla múltiplos lotes por produto:

```
⚠️ Alerta: Estoque baixo (menos de 5 unidades)
🚨 Crítico: Produto próximo da validade
📦 Info: Hora de fazer novo pedido
✅ OK: Estoque normal
```

**Funcionalidades:**
- Controle de **múltiplos lotes** por produto
- **Número do lote** para rastreabilidade
- **Data de aquisição** e **validade** por lote
- **FIFO automático** (primeiro a vencer, primeiro a sair)

### Dicas de Gestão de Estoque

| Situação | Ação |
|----------|------|
| Produto encalhado | Crie promoção para girar estoque |
| Produto sempre acaba | Aumente estoque mínimo |
| Validade próxima | Ofereça desconto para vender rápido |
| Novo lote chegou | Cadastre com número do lote |

---

## 📅 Gestão de Agenda dos Barbeiros

### Bloqueios de Horário

Configure a disponibilidade de cada barbeiro:

```
🔒 Tipos de Bloqueio Disponíveis:
├── 🏖️ Férias (período completo)
├── 📅 Folga (dia inteiro)
├── 🍽️ Horário de almoço (recorrente)
├── 🚫 Bloqueio específico (horário pontual)
└── 🔄 Bloqueios recorrentes (semanal/diário)
```

**Endpoints de Agenda:**
- `POST /api/barber-schedule/vacation` - Cadastrar férias
- `POST /api/barber-schedule/day-off` - Cadastrar folga
- `POST /api/barber-schedule/lunch-break` - Horário de almoço
- `GET /api/barber-schedule/{barberId}/available-slots` - Horários disponíveis
- `GET /api/barber-schedule/available-barbers` - Barbeiros disponíveis no horário

### Verificação de Disponibilidade

Antes de confirmar agendamento, verifique:

| Verificação | Endpoint |
|-------------|----------|
| Barbeiro disponível? | `/api/barber-schedule/check-availability` |
| Slots livres no dia | `/api/barber-schedule/{id}/available-slots` |
| Quem atende no horário | `/api/barber-schedule/available-barbers` |

---

## 🔔 Sistema de Notificações

### Tipos de Notificação

O GoBarber envia notificações automáticas:

```
📧 EMAIL:
├── ✅ Confirmação de agendamento
├── ⏰ Lembrete 24h antes
├── ⏰ Lembrete 2h antes
├── ❌ Cancelamento
├── 📋 Vaga na lista de espera
└── 🎉 Promoções e cupons
```

**Endpoints de Notificação:**
- `POST /api/notifications/send` - Enviar notificação
- `GET /api/notifications/client/{id}` - Notificações do cliente
- `PUT /api/notifications/{id}/read` - Marcar como lida

---

## 🎯 Dicas de Sucesso

### Checklist Diário

```
☐ Verificar agendamentos do dia
☐ Confirmar com clientes (se necessário)
☐ Checar estoque de produtos
☐ Responder avaliações pendentes
☐ Atualizar redes sociais
```

### Checklist Semanal

```
☐ Analisar relatório de faturamento
☐ Verificar lista de espera
☐ Planejar promoções da semana
☐ Revisar avaliações e feedbacks
☐ Ajustar horários se necessário
```

### Checklist Mensal

```
☐ Relatório financeiro completo
☐ Análise de clientes inativos
☐ Revisão de preços e serviços
☐ Planejamento de promoções
☐ Treinamento da equipe
```

---

## 📱 Divulgação do Sistema

### Onde Compartilhar seu Link de Agendamento

| Canal | Como usar |
|-------|-----------|
| **Instagram** | Link na bio + stories |
| **WhatsApp** | Status + mensagem automática |
| **Google Meu Negócio** | Botão de agendamento |
| **Cartão de visita** | QR Code |
| **Barbearia** | Cartaz com QR Code |

### Modelo de Mensagem para Clientes

```
📱 AGENDAMENTO ONLINE

Olá! Agora você pode agendar seu horário 
na [Nome da Barbearia] pelo nosso sistema online!

✅ Escolha o barbeiro
✅ Escolha o serviço
✅ Escolha o melhor horário

Acesse: [seu-link-gobarber.com]

Mais praticidade para você! 💈
```

---

## ❓ Dúvidas Frequentes

### Como cliente agenda?
1. Acessa a API via `/api/appointments`
2. Escolhe serviço, barbeiro e horário
3. Sistema verifica disponibilidade automaticamente
4. Recebe confirmação por email

### Como cancelo um agendamento?
1. Acesse `DELETE /api/appointments/{id}`
2. Ou atualize status via `PUT /api/appointments/{id}`
3. Sistema notifica o cliente automaticamente

### Como adiciono novo barbeiro?
1. Crie usuário: `POST /api/auth/register`
2. Cadastre barbeiro: `POST /api/barbers`
3. Configure horários: `POST /api/barber-schedule`
4. Associe serviços que ele realiza

### Como crio uma promoção?
1. Acesse `POST /api/sales`
2. Defina nome, cupom e período de validade
3. Configure desconto (percentual ou valor fixo)
4. Divulgue o código do cupom para seus clientes

### Como funciona o programa de fidelidade?
1. Cliente é cadastrado: `POST /api/clients`
2. A cada agendamento, ganha pontos automaticamente
3. Consulte pontos: `GET /api/clients/{id}`
4. Níveis: Bronze (0-99), Silver (100-499), Gold (500+)

### Como consulto o dashboard?
1. Visão geral: `GET /api/dashboard/summary`
2. Receita: `GET /api/dashboard/revenue?startDate=X&endDate=Y`
3. Performance: `GET /api/dashboard/barbers/performance`

---

## 🔗 API Reference

### Principais Endpoints

| Recurso | Método | Endpoint | Descrição |
|---------|--------|----------|-----------|
| **Agendamentos** | GET | `/api/appointments` | Listar agendamentos |
| | POST | `/api/appointments` | Criar agendamento |
| **Barbeiros** | GET | `/api/barbers` | Listar barbeiros |
| | GET | `/api/barbers/{id}/services` | Serviços do barbeiro |
| **Clientes** | GET | `/api/clients` | Listar clientes |
| | PUT | `/api/clients/{id}/points` | Atualizar pontos |
| **Pagamentos** | POST | `/api/payments` | Registrar pagamento |
| | GET | `/api/payments/appointment/{id}` | Pagamentos do agendamento |
| **Dashboard** | GET | `/api/dashboard/summary` | Visão geral |
| **Avaliações** | POST | `/api/reviews` | Criar avaliação |
| **Lista Espera** | POST | `/api/waitlist` | Entrar na fila |

**Documentação completa:** Acesse `/swagger-ui.html` após iniciar o servidor.

---

<div align="center">

## 💈 Pronto para Crescer?

**Use todas as ferramentas do GoBarber e transforme sua barbearia!**

📧 Suporte: suporte@gobarber.com.br
📚 Documentação: `/swagger-ui.html`
🐙 GitHub: [Go-BarberShop](https://github.com/seu-usuario/Go-BarberShop)

</div>
