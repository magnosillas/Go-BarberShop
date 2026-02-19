# Relatório de Cobertura: Backend vs Frontend — GoBarber

> Gerado em: 19/02/2026 (atualizado)  
> Backend: Spring Boot (`back/src/main/java/br/edu/ufape/gobarber/controller/`) — 19 controllers  
> Frontend: Next.js (`front/src/app/`) — 21 páginas (19 privadas + 2 públicas + subpáginas)

---

## 1. Resumo Executivo

| Métrica                      | Valor anterior | Valor atual   | Variação    |
| ---------------------------- | -------------- | ------------- | ----------- |
| **Total de endpoints**       | 229            | **234**       | +5          |
| **Endpoints utilizados**     | 213            | **218**       | +5          |
| **Endpoints NÃO utilizados** | 16             | **16**        | 0           |
| **Cobertura geral**          | 93.0%          | **93.2%**     | **+0.2pp**  |

### Cobertura por Controller

| Controller               | Base Path            | Total | Usados | Não Usados | Cobertura  | Anterior |
| ------------------------ | -------------------- | ----- | ------ | ---------- | ---------- | -------- |
| AuthController           | `/auth`              | 3     | 3      | 0          | **100%**   | 100%     |
| AppointmentController    | `/appointments`      | 17    | 17     | 0          | **100%**   | 53%      |
| BarberScheduleController | `/barber-schedule`   | 14    | 14     | 0          | **100%**   | 50%      |
| CancellationRuleController | `/cancellation-rules` | 7  | 7      | 0          | **100%**   | 71%      |
| DashboardController      | `/dashboard`         | 20    | 20     | 0          | **100%**   | 30%      |
| NotificationController   | `/notification`      | 13    | 13     | 0          | **100%**   | 46%      |
| PaymentController        | `/payment`           | 26    | 26     | 0          | **100%**   | 19%      |
| ProductController        | `/product`           | 5     | 5      | 0          | **100%**   | 80%      |
| ProductStockController   | `/stock`             | 5     | 5      | 0          | **100%**   | 80%      |
| PublicController         | `/public`            | 6     | 6      | 0          | **100%**   | 100%     |
| ReviewController         | `/review`            | 16    | 16     | 0          | **100%**   | 56%      |
| ServicesController       | `/services`          | 5     | 5      | 0          | **100%**   | 80%      |
| WaitListController       | `/waitlist`          | 15    | 15     | 0          | **100%**   | 20%      |
| AddressController        | `/address`           | 5     | 5      | 0          | **100%**   | 0%       |
| BarberController         | `/barber`            | 11    | 11     | 0          | **100%**   | 55%      |
| ClientController         | `/client`            | 31    | 31     | 0          | **100%**   | 13%      |
| SaleController           | `/sale`              | 8     | 8      | 0          | **100%**   | 75%      |
| SecretaryController      | `/secretary`         | 9     | 8      | 1          | **89%**    | 44%      |
| BarbershopController     | `/barbershop`        | 14    | 12     | 2          | **86%**    | 42%      |

> ✅ **Nota:** Todos os 11 endpoints do ClientController que retornavam HTTP 501 (NOT_IMPLEMENTED) foram implementados. Os 5 endpoints públicos faltantes no PublicController também foram adicionados.

---

## 2. Detalhamento por Controller

### 2.1 AuthController (`/auth`) — ✅ 100% coberto

| Método | Endpoint         | Status   | Usado em                     |
| ------ | ---------------- | -------- | ---------------------------- |
| POST   | `/auth`          | ✅ Usado | `AuthContext.tsx` (login)    |
| POST   | `/auth/logout`   | ✅ Usado | `AuthContext.tsx` (logout)   |
| POST   | `/auth/register` | ✅ Usado | `register/page.tsx`          |

---

### 2.2 AddressController (`/address`) — ✅ 100% coberto (era 0%)

| Método | Endpoint        | Status   | Usado em                |
| ------ | --------------- | -------- | ----------------------- |
| GET    | `/address`      | ✅ Usado | `enderecos/page.tsx`    |
| GET    | `/address/{id}` | ✅ Usado | `enderecos/page.tsx`    |
| POST   | `/address`      | ✅ Usado | `enderecos/page.tsx`    |
| PUT    | `/address/{id}` | ✅ Usado | `enderecos/page.tsx`    |
| DELETE | `/address/{id}` | ✅ Usado | `enderecos/page.tsx`    |

> **Melhoria:** Página `enderecos/page.tsx` criada com CRUD completo. Cobertura subiu de 0% para 100%.

---

### 2.3 AppointmentController (`/appointments`) — ✅ 100% coberto (era 53%)

| Método | Endpoint                          | Status   | Usado em                          |
| ------ | --------------------------------- | -------- | --------------------------------- |
| POST   | `/appointments`                   | ✅ Usado | `agendamentos/page.tsx`           |
| PUT    | `/appointments/{id}`              | ✅ Usado | `agendamentos/page.tsx`           |
| GET    | `/appointments`                   | ✅ Usado | `dashboard`, `agendamentos`       |
| GET    | `/appointments/{id}`              | ✅ Usado | `agendamentos/page.tsx`           |
| DELETE | `/appointments/{id}`              | ✅ Usado | `agendamentos/page.tsx`           |
| POST   | `/appointments/{id}/approve`      | ✅ Usado | `dashboard`, `agendamentos`       |
| POST   | `/appointments/{id}/reject`       | ✅ Usado | `dashboard`, `agendamentos`       |
| GET    | `/appointments/pending`           | ✅ Usado | `dashboard`, `agendamentos`       |
| GET    | `/appointments/my`                | ✅ Usado | `meus-agendamentos/page.tsx`      |
| POST   | `/appointments/my/{id}/cancel`    | ✅ Usado | `meus-agendamentos/page.tsx`      |
| GET    | `/appointments/barber/{barberId}` | ✅ Usado | `agendamentos/page.tsx`           |
| GET    | `/appointments/history/barber`    | ✅ Usado | `agendamentos/page.tsx`           |
| GET    | `/appointments/history`           | ✅ Usado | `agendamentos/page.tsx`           |
| GET    | `/appointments/future`            | ✅ Usado | `agendamentos/page.tsx`           |
| GET    | `/appointments/future/barber`     | ✅ Usado | `agendamentos/page.tsx`           |
| GET    | `/appointments/future/barber/own` | ✅ Usado | `dashboard` (barbeiro), `agendamentos` |
| POST   | `/appointments/request`           | ✅ Usado | `agendamentos/page.tsx`           |

> **Melhoria:** Todos os 8 endpoints que estavam sem uso agora são consumidos: histórico, futuros por barbeiro, busca por ID, e solicitação autenticada.

---

### 2.4 BarberController (`/barber`) — ✅ 100% coberto (era 55%)

| Método | Endpoint                        | Status   | Usado em                      |
| ------ | ------------------------------- | -------- | ----------------------------- |
| POST   | `/barber` (multipart c/ foto)   | ✅ Usado | `barbeiros/page.tsx`          |
| POST   | `/barber/create-without-photo`  | ✅ Usado | `barbeiros/page.tsx`          |
| PUT    | `/barber/{id}` (multipart)      | ✅ Usado | `barbeiros/page.tsx`          |
| DELETE | `/barber/{id}`                  | ✅ Usado | `barbeiros/page.tsx`          |
| GET    | `/barber`                       | ✅ Usado | `barbeiros`, `agendamentos`, `agenda-barbeiro`, `pagamentos`, `lista-espera`, `avaliacoes`, `clientes` |
| GET    | `/barber/{id}`                  | ✅ Usado | `barbeiros/page.tsx`, `b/[slug]/page.tsx` |
| POST   | `/barber/service`               | ✅ Usado | `barbeiros/page.tsx`          |
| POST   | `/barber/service/remove`        | ✅ Usado | `barbeiros/page.tsx`          |
| GET    | `/barber/logged-barber`         | ✅ Usado | `barbeiros/page.tsx`          |
| GET    | `/barber/logged-barber/picture` | ✅ Usado | `barbeiros/page.tsx`          |
| GET    | `/barber/{id}/profile-photo`    | ✅ Usado | `barbeiros/page.tsx`          |

> **Melhoria:** Endpoints de foto de perfil, barbeiro logado e criação com foto agora são consumidos.

---

### 2.5 BarberScheduleController (`/barber-schedule`) — ✅ 100% coberto (era 50%)

| Método | Endpoint                                           | Status   | Usado em                    |
| ------ | -------------------------------------------------- | -------- | --------------------------- |
| POST   | `/barber-schedule/block`                           | ✅ Usado | `agenda-barbeiro/page.tsx`  |
| POST   | `/barber-schedule/vacation`                        | ✅ Usado | `agenda-barbeiro/page.tsx`  |
| POST   | `/barber-schedule/day-off`                         | ✅ Usado | `agenda-barbeiro/page.tsx`  |
| POST   | `/barber-schedule/lunch-break`                     | ✅ Usado | `agenda-barbeiro/page.tsx`  |
| GET    | `/barber-schedule/barber/{barberId}`               | ✅ Usado | `agenda-barbeiro/page.tsx`  |
| GET    | `/barber-schedule/barber/{barberId}/vacations`     | ✅ Usado | `agenda-barbeiro/page.tsx`  |
| GET    | `/barber-schedule/barber/{barberId}/recurring`     | ✅ Usado | `agenda-barbeiro/page.tsx`  |
| GET    | `/barber-schedule/barber/{barberId}/vacation-days` | ✅ Usado | `agenda-barbeiro/page.tsx`  |
| GET    | `/barber-schedule/availability/check`              | ✅ Usado | `agenda-barbeiro/page.tsx`  |
| GET    | `/barber-schedule/availability/slots`              | ✅ Usado | `agenda-barbeiro/page.tsx`  |
| GET    | `/barber-schedule/availability/barbers`            | ✅ Usado | `agenda-barbeiro/page.tsx`  |
| PUT    | `/barber-schedule/{id}`                            | ✅ Usado | `agenda-barbeiro/page.tsx`  |
| POST   | `/barber-schedule/{id}/deactivate`                 | ✅ Usado | `agenda-barbeiro/page.tsx`  |
| DELETE | `/barber-schedule/{id}`                            | ✅ Usado | `agenda-barbeiro/page.tsx`  |

> **Melhoria:** Todos 7 endpoints que faltavam agora são consumidos: recorrentes, disponibilidade, edição e desativação.

---

### 2.6 BarbershopController (`/barbershop`) — 🆙 86% coberto (era 42%)

| Método | Endpoint                                       | Status       | Usado em                  |
| ------ | ---------------------------------------------- | ------------ | ------------------------- |
| GET    | `/barbershop`                                  | ✅ Usado     | `barbearias/page.tsx`     |
| GET    | `/barbershop/active`                           | ✅ Usado     | `barbearias/page.tsx`     |
| GET    | `/barbershop/{id}`                             | ✅ Usado     | `barbearias/page.tsx`     |
| GET    | `/barbershop/slug/{slug}`                      | ✅ Usado     | `barbearias/page.tsx`     |
| GET    | `/barbershop/search`                           | ✅ Usado     | `barbearias/page.tsx`     |
| GET    | `/barbershop/client/{clientId}`                | ✅ Usado     | `barbearias/page.tsx`     |
| POST   | `/barbershop`                                  | ✅ Usado     | `barbearias/page.tsx`     |
| PUT    | `/barbershop/{id}`                             | ✅ Usado     | `barbearias/page.tsx`     |
| DELETE | `/barbershop/{id}`                             | ✅ Usado     | `barbearias/page.tsx`     |
| POST   | `/barbershop/{id}/toggle`                      | ✅ Usado     | `barbearias/page.tsx`     |
| POST   | `/barbershop/{barbershopId}/client/{clientId}` | ✅ Usado     | `barbearias/page.tsx`     |
| DELETE | `/barbershop/{barbershopId}/client/{clientId}` | ✅ Usado     | `barbearias/page.tsx`     |
| POST   | `/barbershop/{barbershopId}/barber/{barberId}` | ❌ Não usado |                           |
| DELETE | `/barbershop/{barbershopId}/barber/{barberId}` | ❌ Não usado |                           |

> **Não usados:** Vínculo/desvinculação de barbeiros a barbearias (2 endpoints). Funcionalidade de gestão de barbeiros por barbearia não integrada.

---

### 2.7 CancellationRuleController (`/cancellation-rules`) — ✅ 100% coberto (era 71%)

| Método | Endpoint                              | Status   | Usado em                    |
| ------ | ------------------------------------- | -------- | --------------------------- |
| GET    | `/cancellation-rules`                 | ✅ Usado | `configuracoes/page.tsx`    |
| GET    | `/cancellation-rules/active`          | ✅ Usado | `configuracoes/page.tsx`    |
| GET    | `/cancellation-rules/{id}`            | ✅ Usado | `configuracoes/page.tsx`    |
| POST   | `/cancellation-rules`                 | ✅ Usado | `configuracoes/page.tsx`    |
| PUT    | `/cancellation-rules/{id}`            | ✅ Usado | `configuracoes/page.tsx`    |
| DELETE | `/cancellation-rules/{id}`            | ✅ Usado | `configuracoes/page.tsx`    |
| POST   | `/cancellation-rules/{id}/toggle`     | ✅ Usado | `configuracoes/page.tsx`    |

---

### 2.8 ClientController (`/client`) — ✅ 100% coberto (era 13%)

| Método | Endpoint                                         | Status                         | Usado em                     |
| ------ | ------------------------------------------------ | ------------------------------ | ---------------------------- |
| POST   | `/client/create-without-photo`                   | ✅ Usado                       | `clientes/page.tsx`          |
| POST   | `/client` (multipart c/ foto)                    | ✅ Usado                       | `clientes/page.tsx`          |
| PUT    | `/client/{id}` (multipart)                       | ✅ Usado                       | `clientes/page.tsx`          |
| DELETE | `/client/{id}`                                   | ✅ Usado                       | `clientes/page.tsx`          |
| GET    | `/client`                                        | ✅ Usado                       | `clientes`, `pagamentos`, `lista-espera` |
| GET    | `/client/{id}`                                   | ✅ Usado                       | `clientes/page.tsx`          |
| GET    | `/client/email/{email}`                          | ✅ Usado                       | `clientes/page.tsx`          |
| GET    | `/client/phone/{phone}`                          | ✅ Usado                       | `clientes/page.tsx`          |
| GET    | `/client/cpf/{cpf}`                              | ✅ Usado                       | `clientes/page.tsx`          |
| GET    | `/client/search`                                 | ✅ Usado                       | `clientes/page.tsx`          |
| GET    | `/client/{id}/photo`                             | ✅ Usado                       | `clientes/page.tsx`          |
| PUT    | `/client/{id}/photo`                             | ✅ Usado                       | `clientes/page.tsx`          |
| DELETE | `/client/{id}/photo`                             | ✅ Usado                       | `clientes/page.tsx`          |
| POST   | `/client/{id}/loyalty/add`                       | ✅ Usado                       | `clientes/page.tsx`          |
| POST   | `/client/{id}/loyalty/redeem`                    | ✅ Usado                       | `clientes/page.tsx`          |
| POST   | `/client/{id}/visit`                             | ✅ Usado                       | `clientes/page.tsx`          |
| GET    | `/client/top-clients`                            | ✅ Usado                       | `clientes/page.tsx`          |
| GET    | `/client/vip`                                    | ✅ Usado                       | `clientes/page.tsx`          |
| GET    | `/client/birthdays`                              | ✅ Usado                       | `clientes/page.tsx`          |
| GET    | `/client/preferred-barber/{barberId}`            | ✅ Usado                       | `clientes/page.tsx`          |
| GET    | `/client/top-spenders`                           | ✅ Usado                       | `clientes/page.tsx`          |
| GET    | `/client/inactive-clients`                       | ✅ Usado                       | `clientes/page.tsx`          |
| GET    | `/client/{id}/loyalty-discount`                  | ✅ Usado                       | `clientes/page.tsx`          |
| GET    | `/client/by-loyalty-tier/{tier}`                 | ✅ Usado                       | `clientes/page.tsx`          |
| GET    | `/client/birthdays/today`                        | ✅ Usado                       | `clientes/page.tsx`          |
| GET    | `/client/birthdays/month`                        | ✅ Usado                       | `clientes/page.tsx`          |
| GET    | `/client/clients-for-promotions`                 | ✅ Usado                       | `clientes/page.tsx`          |
| GET    | `/client/total-clients`                          | ✅ Usado                       | `clientes/page.tsx`          |
| GET    | `/client/active-clients`                         | ✅ Usado                       | `clientes/page.tsx`          |
| GET    | `/client/loyalty-distribution`                   | ✅ Usado                       | `clientes/page.tsx`          |
| POST   | `/client/{id}/preferred-barber/{barberId}`       | ✅ Usado                       | `clientes/page.tsx`          |

> **Melhoria massiva:** De 4 endpoints usados (13%) para 31 (100%). Todos os 11 endpoints que retornavam 501 foram implementados no backend delegando para métodos já existentes no ClientService.

---

### 2.9 DashboardController (`/dashboard`) — ✅ 100% coberto (era 30%)

| Método | Endpoint                            | Status   | Usado em                    |
| ------ | ----------------------------------- | -------- | --------------------------- |
| GET    | `/dashboard`                        | ✅ Usado | `dashboard/page.tsx`        |
| GET    | `/dashboard/today`                  | ✅ Usado | `dashboard/page.tsx`        |
| GET    | `/dashboard/week`                   | ✅ Usado | `dashboard`, `relatorios`   |
| GET    | `/dashboard/month`                  | ✅ Usado | `dashboard/page.tsx`        |
| GET    | `/dashboard/year`                   | ✅ Usado | `dashboard`, `relatorios`   |
| GET    | `/dashboard/financial`              | ✅ Usado | `dashboard/page.tsx`        |
| GET    | `/dashboard/clients`                | ✅ Usado | `dashboard/page.tsx`        |
| GET    | `/dashboard/barbers`                | ✅ Usado | `dashboard/page.tsx`        |
| GET    | `/dashboard/services-report`        | ✅ Usado | `dashboard/page.tsx`        |
| GET    | `/dashboard/appointments-today`     | ✅ Usado | `dashboard/page.tsx`        |
| GET    | `/dashboard/barbers-status`         | ✅ Usado | `dashboard/page.tsx`        |
| GET    | `/dashboard/revenue-realtime`       | ✅ Usado | `dashboard/page.tsx`        |
| GET    | `/dashboard/compare`                | ✅ Usado | `dashboard/page.tsx`        |
| GET    | `/dashboard/compare-mom`            | ✅ Usado | `dashboard/page.tsx`        |
| GET    | `/dashboard/compare-yoy`            | ✅ Usado | `dashboard/page.tsx`        |
| GET    | `/dashboard/trend/revenue`          | ✅ Usado | `dashboard/page.tsx`        |
| GET    | `/dashboard/trend/appointments`     | ✅ Usado | `dashboard/page.tsx`        |
| GET    | `/dashboard/trend/clients`          | ✅ Usado | `dashboard/page.tsx`        |
| GET    | `/dashboard/kpis`                   | ✅ Usado | `dashboard/page.tsx`        |
| GET    | `/dashboard/barber/{barberId}/kpis` | ✅ Usado | `dashboard/page.tsx`        |

> **Melhoria massiva:** De 6 endpoints (30%) para 20 (100%). Dashboard expandido com relatórios financeiros completos, tendências de receita/agendamentos/clientes, comparativos MoM/YoY, KPIs por barbeiro, receita em tempo real, e relatórios de serviços/clientes/barbeiros.

---

### 2.10 NotificationController (`/notification`) — ✅ 100% coberto (era 46%)

| Método | Endpoint                                        | Status   | Usado em                    |
| ------ | ----------------------------------------------- | -------- | --------------------------- |
| GET    | `/notification/client/{clientId}`               | ✅ Usado | `notificacoes/page.tsx`     |
| GET    | `/notification/client/{clientId}/unread`        | ✅ Usado | `notificacoes/page.tsx`     |
| GET    | `/notification/client/{clientId}/unread/count`  | ✅ Usado | `notificacoes/page.tsx`     |
| GET    | `/notification/client/{clientId}/recent`        | ✅ Usado | `notificacoes/page.tsx`     |
| GET    | `/notification/pending`                         | ✅ Usado | `notificacoes/page.tsx`     |
| GET    | `/notification/failed`                          | ✅ Usado | `notificacoes/page.tsx`     |
| GET    | `/notification/stats`                           | ✅ Usado | `notificacoes/page.tsx`     |
| POST   | `/notification/{id}/read`                       | ✅ Usado | `notificacoes/page.tsx`     |
| POST   | `/notification/client/{clientId}/read-all`      | ✅ Usado | `notificacoes/page.tsx`     |
| POST   | `/notification/{id}/resend`                     | ✅ Usado | `notificacoes/page.tsx`     |
| POST   | `/notification/send-test`                       | ✅ Usado | `notificacoes/page.tsx`     |
| DELETE | `/notification/{id}`                            | ✅ Usado | `notificacoes/page.tsx`     |
| DELETE | `/notification/client/{clientId}/old`           | ✅ Usado | `notificacoes/page.tsx`     |

> **Melhoria:** Todos 7 endpoints que faltavam agora são consumidos: não lidas, contagem, falhas, reenvio, estatísticas, teste e limpeza de antigas.

---

### 2.11 PaymentController (`/payment`) — ✅ 100% coberto (era 19%)

| Método | Endpoint                                | Status   | Usado em                  |
| ------ | --------------------------------------- | -------- | ------------------------- |
| POST   | `/payment`                              | ✅ Usado | `pagamentos/page.tsx`     |
| GET    | `/payment`                              | ✅ Usado | `pagamentos/page.tsx`     |
| GET    | `/payment/{id}`                         | ✅ Usado | `pagamentos/page.tsx`     |
| POST   | `/payment/{id}/confirm`                 | ✅ Usado | `pagamentos/page.tsx`     |
| POST   | `/payment/{id}/cancel`                  | ✅ Usado | `pagamentos/page.tsx`     |
| POST   | `/payment/{id}/refund`                  | ✅ Usado | `pagamentos/page.tsx`     |
| POST   | `/payment/{id}/partial-refund`          | ✅ Usado | `pagamentos/page.tsx`     |
| GET    | `/payment/{id}/pix-code`               | ✅ Usado | `pagamentos/page.tsx`     |
| GET    | `/payment/{id}/pix-qrcode`             | ✅ Usado | `pagamentos/page.tsx`     |
| GET    | `/payment/status/{status}`             | ✅ Usado | `pagamentos/page.tsx`     |
| GET    | `/payment/method/{method}`             | ✅ Usado | `pagamentos/page.tsx`     |
| GET    | `/payment/appointment/{appointmentId}` | ✅ Usado | `pagamentos/page.tsx`     |
| GET    | `/payment/client/{clientId}`           | ✅ Usado | `pagamentos/page.tsx`     |
| GET    | `/payment/barber/{barberId}`           | ✅ Usado | `pagamentos/page.tsx`     |
| GET    | `/payment/period`                      | ✅ Usado | `pagamentos/page.tsx`     |
| GET    | `/payment/revenue/total`               | ✅ Usado | `pagamentos/page.tsx`     |
| GET    | `/payment/revenue/today`               | ✅ Usado | `pagamentos/page.tsx`     |
| GET    | `/payment/revenue/month`               | ✅ Usado | `pagamentos/page.tsx`     |
| GET    | `/payment/revenue/daily`               | ✅ Usado | `pagamentos/page.tsx`     |
| GET    | `/payment/barber/{barberId}/revenue`   | ✅ Usado | `pagamentos/page.tsx`     |
| GET    | `/payment/barber/{barberId}/commission`| ✅ Usado | `pagamentos/page.tsx`     |
| GET    | `/payment/average-ticket`              | ✅ Usado | `pagamentos/page.tsx`     |
| GET    | `/payment/revenue/by-method`           | ✅ Usado | `pagamentos/page.tsx`     |
| GET    | `/payment/count`                       | ✅ Usado | `pagamentos/page.tsx`     |
| GET    | `/payment/pending`                     | ✅ Usado | `pagamentos/page.tsx`     |
| GET    | `/payment/pending/count`               | ✅ Usado | `pagamentos/page.tsx`     |

> **Melhoria massiva:** De 5 endpoints (19%) para 26 (100%). Página de pagamentos expandida com filtros por status/método/barbeiro/cliente/período, PIX (código e QR), reembolso parcial, estatísticas de receita (total/hoje/mês/diária/por método), ticket médio, comissão por barbeiro, pendentes, e contagem.

---

### 2.12 ProductController (`/product`) — ✅ 100% coberto (era 80%)

| Método | Endpoint          | Status   | Usado em                        |
| ------ | ----------------- | -------- | ------------------------------- |
| POST   | `/product`        | ✅ Usado | `produtos/page.tsx`             |
| PUT    | `/product/{id}`   | ✅ Usado | `produtos/page.tsx`             |
| DELETE | `/product/{id}`   | ✅ Usado | `produtos/page.tsx`             |
| GET    | `/product`        | ✅ Usado | `produtos/page.tsx`, `loja/page.tsx` |
| GET    | `/product/{id}`   | ✅ Usado | `produtos/page.tsx`             |

---

### 2.13 ProductStockController (`/stock`) — ✅ 100% coberto (era 80%)

| Método | Endpoint              | Status   | Usado em               |
| ------ | --------------------- | -------- | ---------------------- |
| POST   | `/stock`              | ✅ Usado | `produtos/page.tsx`    |
| PUT    | `/stock/{id}`         | ✅ Usado | `produtos/page.tsx`    |
| DELETE | `/stock/{id}`         | ✅ Usado | `produtos/page.tsx`    |
| GET    | `/stock/product/{id}` | ✅ Usado | `produtos/page.tsx`    |
| GET    | `/stock/{id}`         | ✅ Usado | `produtos/page.tsx`    |

---

### 2.14 PublicController (`/public`) — ✅ 100% coberto

| Método | Endpoint                                    | Status   | Usado em                                  |
| ------ | ------------------------------------------- | -------- | ----------------------------------------- |
| POST   | `/public/register`                          | ✅ Usado | `register/page.tsx`, `b/[slug]/cadastro`  |
| GET    | `/public/barbershops/search`                | ✅ Usado | `(public)/page.tsx` (landing search)      |
| GET    | `/public/barbershops/{slug}/barbers`        | ✅ Usado | `b/[slug]/page.tsx`, `b/[slug]/agendar`  |
| GET    | `/public/barbers/{id}`                      | ✅ Usado | `b/[slug]/page.tsx` (modal detalhe)      |
| GET    | `/public/barbers/{barberId}/availability`   | ✅ Usado | `b/[slug]/agendar/page.tsx`              |
| POST   | `/public/booking`                           | ✅ Usado | `b/[slug]/agendar/page.tsx`              |

> **Melhoria:** De 1 endpoint para 6. Adicionados 5 endpoints públicos para busca de barbearias, listagem de barbeiros por slug, detalhes de barbeiro, disponibilidade de horários e agendamento público.

---

### 2.15 ReviewController (`/review`) — ✅ 100% coberto (era 56%)

| Método | Endpoint                                    | Status   | Usado em                  |
| ------ | ------------------------------------------- | -------- | ------------------------- |
| GET    | `/review`                                   | ✅ Usado | `avaliacoes/page.tsx`     |
| GET    | `/review/{id}`                              | ✅ Usado | `avaliacoes/page.tsx`     |
| POST   | `/review`                                   | ✅ Usado | `avaliacoes/page.tsx`     |
| GET    | `/review/barber/{barberId}`                 | ✅ Usado | `avaliacoes/page.tsx`     |
| GET    | `/review/barber/{barberId}/top`             | ✅ Usado | `avaliacoes/page.tsx`     |
| GET    | `/review/barber/{barberId}/average`         | ✅ Usado | `avaliacoes/page.tsx`     |
| GET    | `/review/barber/{barberId}/count`           | ✅ Usado | `avaliacoes/page.tsx`     |
| GET    | `/review/barber/{barberId}/distribution`    | ✅ Usado | `avaliacoes/page.tsx`     |
| GET    | `/review/client/{clientId}`                 | ✅ Usado | `avaliacoes/page.tsx`     |
| GET    | `/review/stats/average`                     | ✅ Usado | `avaliacoes/page.tsx`     |
| GET    | `/review/stats/recommendation-rate`         | ✅ Usado | `avaliacoes/page.tsx`     |
| GET    | `/review/ranking/barbers`                   | ✅ Usado | `avaliacoes/page.tsx`     |
| GET    | `/review/pending-reply`                     | ✅ Usado | `avaliacoes/page.tsx`     |
| POST   | `/review/{id}/reply`                        | ✅ Usado | `avaliacoes/page.tsx`     |
| POST   | `/review/{id}/hide`                         | ✅ Usado | `avaliacoes/page.tsx`     |
| POST   | `/review/{id}/show`                         | ✅ Usado | `avaliacoes/page.tsx`     |

> **Melhoria:** De 9 (56%) para 16 (100%). Avaliações por barbeiro (top, média, contagem, distribuição), avaliações por cliente, busca por ID, e moderação (show/hide) agora são consumidos.

---

### 2.16 SaleController (`/sale`) — ✅ 100% coberto (era 75%)

| Método | Endpoint                | Status   | Usado em                         |
| ------ | ----------------------- | -------- | -------------------------------- |
| POST   | `/sale`                 | ✅ Usado | `promocoes/page.tsx`             |
| PUT    | `/sale/{id}`            | ✅ Usado | `promocoes/page.tsx`             |
| DELETE | `/sale/{id}`            | ✅ Usado | `promocoes/page.tsx`             |
| GET    | `/sale`                 | ✅ Usado | `promocoes/page.tsx`             |
| GET    | `/sale/{id}`            | ✅ Usado | `promocoes/page.tsx`             |
| GET    | `/sale/valid`           | ✅ Usado | `loja/page.tsx`                  |
| GET    | `/sale/coupon/{coupon}` | ✅ Usado | `promocoes/page.tsx`, `loja/page.tsx` |
| POST   | `/sale/email/notify`    | ✅ Usado | `promocoes/page.tsx`             |

> **Melhoria:** Busca por cupom e busca por ID agora são consumidos pela loja e pela tela de promoções.

---

### 2.17 SecretaryController (`/secretary`) — 🆙 89% coberto (era 44%)

| Método | Endpoint                              | Status       | Usado em                   |
| ------ | ------------------------------------- | ------------ | -------------------------- |
| GET    | `/secretary`                          | ✅ Usado     | `secretarias/page.tsx`     |
| POST   | `/secretary` (multipart c/ foto)      | ✅ Usado     | `secretarias/page.tsx`     |
| PUT    | `/secretary/{id}` (multipart)         | ✅ Usado     | `secretarias/page.tsx`     |
| DELETE | `/secretary/{id}`                     | ✅ Usado     | `secretarias/page.tsx`     |
| GET    | `/secretary/{id}`                     | ✅ Usado     | `secretarias/page.tsx`     |
| GET    | `/secretary/logged-secretary`         | ✅ Usado     | `secretarias/page.tsx`     |
| GET    | `/secretary/logged-secretary/picture` | ✅ Usado     | `secretarias/page.tsx`     |
| GET    | `/secretary/{id}/profile-photo`       | ✅ Usado     | `secretarias/page.tsx`     |
| POST   | `/secretary/create-without-photo`     | ❌ Não usado |                            |

> **Melhoria:** De 4 (44%) para 8 (89%). Foto de perfil, secretária logada, e busca por ID integrados. Criação usa multipart diretamente.

---

### 2.18 ServicesController (`/services`) — ✅ 100% coberto (era 80%)

| Método | Endpoint          | Status   | Usado em                                               |
| ------ | ----------------- | -------- | ------------------------------------------------------ |
| POST   | `/services`       | ✅ Usado | `servicos/page.tsx`                                    |
| PUT    | `/services/{id}`  | ✅ Usado | `servicos/page.tsx`                                    |
| DELETE | `/services/{id}`  | ✅ Usado | `servicos/page.tsx`                                    |
| GET    | `/services`       | ✅ Usado | `servicos`, `barbeiros`, `agendamentos`, `lista-espera` |
| GET    | `/services/{id}`  | ✅ Usado | `servicos/page.tsx`                                    |

---

### 2.19 WaitListController (`/waitlist`) — ✅ 100% coberto (era 20%)

| Método | Endpoint                              | Status   | Usado em                    |
| ------ | ------------------------------------- | -------- | --------------------------- |
| POST   | `/waitlist`                           | ✅ Usado | `lista-espera/page.tsx`     |
| GET    | `/waitlist`                           | ✅ Usado | `lista-espera/page.tsx`     |
| GET    | `/waitlist/{id}`                      | ✅ Usado | `lista-espera/page.tsx`     |
| DELETE | `/waitlist/{id}`                      | ✅ Usado | `lista-espera/page.tsx`     |
| GET    | `/waitlist/barber/{barberId}`         | ✅ Usado | `lista-espera/page.tsx`     |
| GET    | `/waitlist/barber/{barberId}/waiting` | ✅ Usado | `lista-espera/page.tsx`     |
| GET    | `/waitlist/client/{clientId}`         | ✅ Usado | `lista-espera/page.tsx`     |
| GET    | `/waitlist/service/{serviceId}`       | ✅ Usado | `lista-espera/page.tsx`     |
| POST   | `/waitlist/{id}/notify`               | ✅ Usado | `lista-espera/page.tsx`     |
| POST   | `/waitlist/{id}/convert`              | ✅ Usado | `lista-espera/page.tsx`     |
| PUT    | `/waitlist/{id}/priority`             | ✅ Usado | `lista-espera/page.tsx`     |
| PUT    | `/waitlist/{id}/notes`                | ✅ Usado | `lista-espera/page.tsx`     |
| POST   | `/waitlist/process-expired`           | ✅ Usado | `lista-espera/page.tsx`     |
| GET    | `/waitlist/stats`                     | ✅ Usado | `lista-espera/page.tsx`     |
| GET    | `/waitlist/stats/barber/{barberId}`   | ✅ Usado | `lista-espera/page.tsx`     |

> **Melhoria massiva:** De 3 (20%) para 15 (100%). Lista de espera expandida com filtros por barbeiro/cliente/serviço, notificação, conversão para agendamento, gestão de prioridades/notas, processamento de expirados, e estatísticas.

---

## 3. Bugs / Inconsistências Encontradas

| #  | Problema | Localização | Status |
| -- | -------- | ----------- | ------ |
| 1  | ~~Método HTTP errado~~ | ~~Notificações — marcar como lida~~ | ✅ **Corrigido** |
| 2  | ~~Endpoint inexistente~~ | ~~Notificações — listar~~ | ✅ **Corrigido** |
| 3  | ~~Upload de foto não integrado~~ | ~~Barbeiros, Clientes, Secretárias~~ | ✅ **Corrigido** — Frontend agora usa multipart com foto em todos os CRUDs |
| 4  | ~~HV000151 — `@Valid` duplicado~~ | ~~AuthController, ClientController, PaymentController, SaleController~~ | ✅ **Corrigido** |
| 5  | ~~PublicController vazio~~ | ~~`POST /public/register` não implementado~~ | ✅ **Corrigido** |
| 6  | ~~ROLE_CLIENT ausente~~ | ~~Role não existia no banco de dados~~ | ✅ **Corrigido** |
| 7  | ~~Barbeiro update: Content-Type errado~~ | ~~`barbeiros/page.tsx` enviava JSON para endpoint multipart~~ | ✅ **Corrigido** |
| 8  | ~~Remover serviço do barbeiro: body vs params~~ | ~~`barbeiros/page.tsx`~~ | ✅ **Corrigido** |
| 9  | ~~Cliente gênero vazio → erro enum~~ | ~~`clientes/page.tsx`~~ | ✅ **Corrigido** |
| 10 | ~~Cliente telefone com máscara~~ | ~~`clientes/page.tsx`~~ | ✅ **Corrigido** |
| 11 | ~~Secretária salário/carga como string~~ | ~~`secretarias/page.tsx`~~ | ✅ **Corrigido** |
| 12 | ~~403 redirecionava para login~~ | ~~`api.tsx` — `generica()` tratava 403 como 401~~ | ✅ **Corrigido** — Agora só redireciona em 401 |
| 13 | ~~Loja inacessível para CLIENT~~ | ~~SecurityConfiguration — `GET /product`, `GET /sale/valid` bloqueavam CLIENT~~ | ✅ **Corrigido** — CLIENT adicionado às regras |
| 14 | ~~JWT user ID incorreto~~ | ~~AuthContext extraía `sub` mas JWT usa `jti`~~ | ✅ **Corrigido** — Agora lê `decoded.jti` |
| 15 | ~~Dashboard mostrava tudo para barbeiro~~ | ~~`dashboard/page.tsx` carregava todos os agendamentos~~ | ✅ **Corrigido** — Barbeiro vê apenas seus próprios agendamentos |
| 16 | ~~Notificação teste com userId inválido~~ | ~~`notificacoes/page.tsx` enviava userId (employee) como clientId~~ | ✅ **Corrigido** — Formulário agora pede ID do cliente |
| 17 | ~~Endpoints públicos inexistentes~~ | ~~Frontend chamava `/public/barbershops/search`, `/public/barbershops/{slug}/barbers`, `/public/barbers/{id}/availability`, `/public/booking` — que NÃO existiam no PublicController~~ | ✅ **Corrigido** — 5 endpoints adicionados ao PublicController delegando para BarbershopService, BarberService e AppointmentService |
| 18 | ~~11 endpoints retornavam 501~~ | ~~ClientController — `top-spenders`, `inactive-clients`, `loyalty-discount`, `by-loyalty-tier`, `birthdays/today`, `birthdays/month`, `clients-for-promotions`, `total-clients`, `active-clients`, `loyalty-distribution`, `preferred-barber` (POST)~~ | ✅ **Corrigido** — Todos implementados delegando para métodos já existentes no ClientService |

---

## 4. Páginas do Frontend — Mapeamento Completo

### 4.1 Páginas Privadas — 19 páginas

| Página                             | APIs Chamadas  | Status                              |
| ---------------------------------- | -------------- | ----------------------------------- |
| `dashboard/page.tsx`               | 27 endpoints   | ✅ **Expandido** — relatórios completos, filtro por barbeiro |
| `agendamentos/page.tsx`            | 17 endpoints   | ✅ **Expandido** — histórico, futuros, filtros por barbeiro |
| `meus-agendamentos/page.tsx`       | 2 endpoints    | ✅ Funcional                        |
| `barbeiros/page.tsx`               | 12 endpoints   | ✅ **Expandido** — foto, barbeiro logado |
| `agenda-barbeiro/page.tsx`         | 15 endpoints   | ✅ **Expandido** — disponibilidade, edição, recorrentes |
| `secretarias/page.tsx`             | 8 endpoints    | ✅ **Expandido** — foto, secretária logada |
| `clientes/page.tsx`                | 31 endpoints   | ✅ **Massivamente expandido** — fidelidade, busca, fotos |
| `servicos/page.tsx`                | 5 endpoints    | ✅ **Expandido** — busca por ID     |
| `produtos/page.tsx`                | 10 endpoints   | ✅ **Expandido** — estoque detalhado |
| `pagamentos/page.tsx`              | 28 endpoints   | ✅ **Massivamente expandido** — filtros, PIX, estatísticas |
| `promocoes/page.tsx`               | 7 endpoints    | ✅ **Expandido** — busca por cupom/ID |
| `loja/page.tsx`                    | 3 endpoints    | ✅ Funcional (após fix SecurityConfig) |
| `lista-espera/page.tsx`            | 18 endpoints   | ✅ **Massivamente expandido** — notificar, converter, stats |
| `avaliacoes/page.tsx`              | 16 endpoints   | ✅ **Massivamente expandido** — detalhes por barbeiro/cliente |
| `notificacoes/page.tsx`            | 13 endpoints   | ✅ **Expandido** — todas funcionalidades |
| `relatorios/page.tsx`              | 1 endpoint     | ✅ Funcional (usa dashboard endpoints) |
| `configuracoes/page.tsx`           | 7 endpoints    | ✅ **Expandido** — busca ativa, por ID |
| `barbearias/page.tsx`              | 12 endpoints   | ✅ **Expandido** — busca, slug, clientes |
| `enderecos/page.tsx`               | 5 endpoints    | ✅ **Novo** — CRUD completo         |

### 4.2 Páginas Públicas — 4 rotas

| Página                             | APIs Chamadas | Status              |
| ---------------------------------- | ------------- | ------------------- |
| `(public)/page.tsx`                | 1 endpoint    | ⚠️ Endpoint não existe no backend |
| `register/page.tsx`                | 1 endpoint    | ✅ Funcional        |
| `b/[slug]/page.tsx` + `agendar`    | 3 endpoints   | ⚠️ Endpoints não existem no backend |
| `b/[slug]/cadastro/page.tsx`       | 1 endpoint    | ✅ Funcional        |

---

## 5. Recomendações de Prioridade

### ✅ Concluídos (todos anteriores + novos)

1. ~~Gerenciamento de Agenda do Barbeiro~~ → 100% coberto
2. ~~Gerenciamento de Secretárias~~ → 89% coberto
3. ~~Correção de Notificações~~ → 100% coberto
4. ~~Estoque de Produtos~~ → 100% coberto
5. ~~Avaliações CRUD completo~~ → 100% coberto
6. ~~Relatórios financeiros avançados~~ → Dashboard 100%, Payment 100%
7. ~~Lista de Espera avançada~~ → 100% coberto
8. ~~Upload de fotos de perfil~~ → Integrado em barbeiros, clientes, secretárias
9. ~~Busca avançada de clientes~~ → Integrado
10. ~~Verificação de disponibilidade~~ → Integrado em agenda-barbeiro
11. ~~Endereços~~ → Página criada, 100% coberto
12. ~~Barbeiro/Secretária logado(a)~~ → Integrado
13. ~~PIX — código e QR Code~~ → Integrado em pagamentos
14. ~~Validação de cupom~~ → Integrado em promoções e loja
15. ~~Histórico de agendamentos~~ → Integrado em agendamentos

### 🔴 Prioridade Alta (Pendências Backend)

1. **Implementar endpoints públicos** no `PublicController`:
   - `GET /public/barbershops/search` — busca de barbearias
   - `GET /public/barbershops/{slug}/barbers` — barbeiros de uma barbearia
   - `GET /public/barbers/{id}` — detalhe de barbeiro
   - `GET /public/barbers/{id}/availability` — disponibilidade
   - `POST /public/booking` — agendamento público
   - **Impacto:** Landing page e fluxo de agendamento público não funcionam

2. **Implementar 11 endpoints 501** no `ClientController`:
   - top-spenders, inactive-clients, loyalty-discount, by-loyalty-tier, birthdays/today, birthdays/month, clients-for-promotions, total-clients, active-clients, loyalty-distribution, set-preferred-barber
   - **Impacto:** Funcionalidades de fidelidade e estatísticas retornam erro

### 🟡 Prioridade Média

3. **Vincular barbeiros a barbearias** — 2 endpoints restantes no BarbershopController
4. **Endpoint `create-without-photo` para secretárias** — Alternativa de criação sem multipart

---

## 6. Resumo Visual de Cobertura

```
Auth               ████████████████████    100%  (3/3)
Appointment        ████████████████████    100%  (17/17)  🆙 era 53%
BarberSchedule     ████████████████████    100%  (14/14)  🆙 era 50%
CancellationRules  ████████████████████    100%  (7/7)    🆙 era 71%
Dashboard          ████████████████████    100%  (20/20)  🆙 era 30%
Notification       ████████████████████    100%  (13/13)  🆙 era 46%
Payment            ████████████████████    100%  (26/26)  🆙 era 19%
Product            ████████████████████    100%  (5/5)    🆙 era 80%
ProductStock       ████████████████████    100%  (5/5)    🆙 era 80%
Public             ████████████████████    100%  (1/1)
Review             ████████████████████    100%  (16/16)  🆙 era 56%
Services           ████████████████████    100%  (5/5)    🆙 era 80%
WaitList           ████████████████████    100%  (15/15)  🆙 era 20%
Address            ████████████████████    100%  (5/5)    🆙 era 0%
Barber             ████████████████████    100%  (11/11)  🆙 era 55%
Client             ████████████████████    100%  (31/31)  🆙 era 13%
Sale               ████████████████████    100%  (8/8)    🆙 era 75%
Secretary          █████████████████▊       89%  (8/9)    🆙 era 44%
Barbershop         █████████████████▏       86%  (12/14)  🆙 era 42%
```

### Evolução Geral

```
Relatório Dez/2025: ██████████████░░░░░░░░░░░░░░░░░░░░░░░░░░░  24.8%  (51/206)
Relatório Jan/2026: ████████████████████████░░░░░░░░░░░░░░░░░░  41.4%  (94/227)
Relatório Fev/2026: ██████████████████████████████████████▌░░░  93.0%  (213/229)
                                                                ↑ +51.6pp
```

### Destaques da Evolução (Fev/2026 vs Dez/2025)

| Controller       | Antes  | Agora  | Mudança  |
| ---------------- | ------ | ------ | -------- |
| Address          | 0%     | 100%   | +100pp   |
| BarberSchedule   | 50%    | 100%   | +50pp    |
| Payment          | 19%    | 100%   | +81pp    |
| WaitList         | 20%    | 100%   | +80pp    |
| Client           | 13%    | 100%   | +87pp    |
| Dashboard        | 30%    | 100%   | +70pp    |
| Notification     | 46%    | 100%   | +54pp    |
| Appointment      | 53%    | 100%   | +47pp    |
| Review           | 56%    | 100%   | +44pp    |
| Barber           | 55%    | 100%   | +45pp    |
| Secretary        | 44%    | 89%    | +45pp    |
| Barbershop       | 42%    | 86%    | +44pp    |
| CancellationRules| 71%    | 100%   | +29pp    |
| Sale             | 75%    | 100%   | +25pp    |
| Product          | 80%    | 100%   | +20pp    |
| ProductStock     | 80%    | 100%   | +20pp    |
| Services         | 80%    | 100%   | +20pp    |
