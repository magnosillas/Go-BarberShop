"use client";

import GoBarberLayout from "@/components/Layout/GoBarberLayout";
import React, { useEffect, useState } from "react";
import { generica } from "@/api/api";
import { toast } from "react-toastify";
import {
  FaChartBar,
  FaCalendarAlt,
  FaDollarSign,
  FaUsers,
  FaStar,
  FaCut,
} from "react-icons/fa";
import {
  Chart as ChartJS,
  CategoryScale,
  LinearScale,
  BarElement,
  ArcElement,
  Title,
  Tooltip,
  Legend,
} from "chart.js";
import { Bar, Doughnut } from "react-chartjs-2";

ChartJS.register(
  CategoryScale,
  LinearScale,
  BarElement,
  ArcElement,
  Title,
  Tooltip,
  Legend,
);

interface BarberPerformance {
  barberId: number;
  barberName: string;
  revenue: number;
  appointments: number | null;
  averageRating: number;
  commission: number;
}

interface ServicePerformance {
  serviceId: number;
  serviceName: string;
  timesBooked: number;
  revenue: number | null;
}

interface DailyRevenue {
  date: string;
  revenue: number;
}

interface DashboardData {
  totalRevenue?: number;
  totalAppointments?: number;
  totalClients?: number;
  averageRating?: number;
  totalReviews?: number;
  newClients?: number;
  revenueGrowth?: number;
  topBarbers?: BarberPerformance[];
  topServices?: ServicePerformance[];
  revenueByPaymentMethod?: Record<string, number>;
  dailyRevenue?: DailyRevenue[];
}

const PAYMENT_COLORS = ["#E94560", "#0F3460", "#16213E", "#1A1A2E", "#533483"];

const PAYMENT_LABELS: Record<string, string> = {
  PIX: "PIX",
  CASH: "Dinheiro",
  CREDIT_CARD: "Crédito",
  DEBIT_CARD: "Débito",
  BANK_TRANSFER: "Transferência",
};

export default function RelatoriosPage() {
  const [data, setData] = useState<DashboardData>({});
  const [loading, setLoading] = useState(true);
  const [period, setPeriod] = useState("month");

  useEffect(() => {
    loadReport();
  }, [period]);

  async function loadReport() {
    setLoading(true);
    try {
      const response = await generica({
        metodo: "GET",
        uri: `/dashboard/${period}`,
      });
      setData(response?.data || {});
    } catch {
      toast.error("Erro ao carregar relatórios");
    } finally {
      setLoading(false);
    }
  }

  const formatCurrency = (v?: number) =>
    (v ?? 0).toLocaleString("pt-BR", { style: "currency", currency: "BRL" });

  // Dados do gráfico de receita diária
  const dailyRevenueChart = {
    labels: (data.dailyRevenue ?? []).map((d) => {
      const [, m, day] = d.date.split("-");
      return `${day}/${m}`;
    }),
    datasets: [
      {
        label: "Receita (R$)",
        data: (data.dailyRevenue ?? []).map((d) => d.revenue),
        backgroundColor: "rgba(233, 69, 96, 0.7)",
        borderColor: "#E94560",
        borderWidth: 1,
        borderRadius: 4,
      },
    ],
  };

  // Dados do gráfico de pagamentos
  const paymentEntries = Object.entries(data.revenueByPaymentMethod ?? {});
  const paymentChart = {
    labels: paymentEntries.map(([k]) => PAYMENT_LABELS[k] ?? k),
    datasets: [
      {
        data: paymentEntries.map(([, v]) => v),
        backgroundColor: PAYMENT_COLORS.slice(0, paymentEntries.length),
        borderWidth: 0,
      },
    ],
  };

  const chartOptions = {
    responsive: true,
    maintainAspectRatio: false,
    plugins: { legend: { display: false } },
    scales: {
      x: { grid: { display: false } },
      y: {
        grid: { color: "#f3f4f6" },
        ticks: { callback: (v: unknown) => `R$ ${v}` },
      },
    },
  };

  return (
    <GoBarberLayout>
      <div className="space-y-6">
        {/* Header */}
        <div className="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4">
          <h1 className="text-2xl font-bold text-[#1A1A2E]">Relatórios</h1>
          <div className="flex gap-2">
            {[
              { key: "week", label: "Semana" },
              { key: "month", label: "Mês" },
              { key: "year", label: "Ano" },
            ].map((p) => (
              <button
                key={p.key}
                onClick={() => setPeriod(p.key)}
                className={`px-4 py-2 rounded-lg text-sm font-medium transition ${period === p.key ? "bg-[#1A1A2E] text-white" : "bg-gray-100 text-gray-600 hover:bg-gray-200"}`}
              >
                {p.label}
              </button>
            ))}
          </div>
        </div>

        {/* KPIs */}
        <div className="grid grid-cols-2 lg:grid-cols-4 gap-4">
          {[
            {
              label: "Receita Total",
              value: formatCurrency(data.totalRevenue),
              icon: <FaDollarSign />,
              color: "text-green-600",
              bg: "bg-green-50",
              sub:
                data.revenueGrowth != null
                  ? `${data.revenueGrowth >= 0 ? "+" : ""}${data.revenueGrowth.toFixed(1)}% vs período anterior`
                  : undefined,
            },
            {
              label: "Agendamentos",
              value: data.totalAppointments ?? 0,
              icon: <FaCalendarAlt />,
              color: "text-blue-600",
              bg: "bg-blue-50",
            },
            {
              label: "Clientes",
              value: data.totalClients ?? 0,
              icon: <FaUsers />,
              color: "text-purple-600",
              bg: "bg-purple-50",
              sub:
                data.newClients != null
                  ? `${data.newClients} novos`
                  : undefined,
            },
            {
              label: "Avaliação Média",
              value: (data.averageRating ?? 0).toFixed(1),
              icon: <FaStar />,
              color: "text-yellow-600",
              bg: "bg-yellow-50",
              sub:
                data.totalReviews != null
                  ? `${data.totalReviews} avaliações`
                  : undefined,
            },
          ].map((kpi) => (
            <div key={kpi.label} className="gobarber-card">
              <div className="flex items-center gap-3">
                <div
                  className={`w-10 h-10 rounded-lg ${kpi.bg} ${kpi.color} flex items-center justify-center`}
                >
                  {kpi.icon}
                </div>
                <div>
                  <p className="text-xs text-gray-500">{kpi.label}</p>
                  <p className="text-xl font-bold text-[#1A1A2E]">
                    {loading ? "..." : kpi.value}
                  </p>
                  {!loading && kpi.sub && (
                    <p className="text-xs text-gray-400">{kpi.sub}</p>
                  )}
                </div>
              </div>
            </div>
          ))}
        </div>

        {/* Gráfico receita diária */}
        {!loading && (data.dailyRevenue ?? []).length > 0 && (
          <div className="gobarber-card">
            <h3 className="font-semibold text-[#1A1A2E] mb-4 flex items-center gap-2">
              <FaDollarSign className="text-[#E94560]" /> Receita por Dia
            </h3>
            <div className="h-52">
              <Bar data={dailyRevenueChart} options={chartOptions as never} />
            </div>
          </div>
        )}

        <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
          {/* Top Serviços */}
          <div className="gobarber-card">
            <h3 className="font-semibold text-[#1A1A2E] mb-4 flex items-center gap-2">
              <FaCut className="text-[#E94560]" /> Serviços Mais Populares
            </h3>
            {loading ? (
              <div className="space-y-3">
                {Array.from({ length: 5 }).map((_, i) => (
                  <div
                    key={i}
                    className="animate-pulse flex items-center gap-3"
                  >
                    <div className="h-3 bg-gray-200 rounded w-1/3" />
                    <div className="h-3 bg-gray-200 rounded flex-1" />
                  </div>
                ))}
              </div>
            ) : data.topServices && data.topServices.length > 0 ? (
              <div className="space-y-3">
                {data.topServices.map((s, i) => {
                  const max = Math.max(
                    ...data.topServices!.map((x) => x.timesBooked),
                  );
                  return (
                    <div key={i}>
                      <div className="flex justify-between text-sm mb-1">
                        <span className="text-gray-700">{s.serviceName}</span>
                        <span className="font-medium text-[#1A1A2E]">
                          {s.timesBooked}x
                        </span>
                      </div>
                      <div className="w-full bg-gray-100 rounded-full h-2">
                        <div
                          className="gobarber-gradient h-2 rounded-full transition-all"
                          style={{ width: `${(s.timesBooked / max) * 100}%` }}
                        />
                      </div>
                    </div>
                  );
                })}
              </div>
            ) : (
              <p className="text-gray-400 text-sm text-center py-6">
                Sem dados no período
              </p>
            )}
          </div>

          {/* Top Barbeiros */}
          <div className="gobarber-card">
            <h3 className="font-semibold text-[#1A1A2E] mb-4 flex items-center gap-2">
              <FaUsers className="text-[#0F3460]" /> Barbeiros Destaque
            </h3>
            {loading ? (
              <div className="space-y-3">
                {Array.from({ length: 5 }).map((_, i) => (
                  <div
                    key={i}
                    className="animate-pulse flex items-center gap-3"
                  >
                    <div className="w-8 h-8 bg-gray-200 rounded-full" />
                    <div className="h-3 bg-gray-200 rounded w-1/3" />
                  </div>
                ))}
              </div>
            ) : data.topBarbers && data.topBarbers.length > 0 ? (
              <div className="space-y-3">
                {data.topBarbers.map((b, i) => (
                  <div
                    key={i}
                    className="flex items-center gap-3 p-2 rounded-lg hover:bg-gray-50"
                  >
                    <div className="w-8 h-8 rounded-full bg-[#1A1A2E] text-white flex items-center justify-center text-xs font-bold">
                      {i + 1}º
                    </div>
                    <div className="flex-1">
                      <p className="text-sm font-medium text-[#1A1A2E]">
                        {b.barberName}
                      </p>
                      <p className="text-xs text-gray-500">
                        {b.appointments ?? 0} atendimentos
                        {b.averageRating > 0 &&
                          ` · ★ ${b.averageRating.toFixed(1)}`}
                      </p>
                    </div>
                    <span className="text-sm font-semibold text-green-600">
                      {formatCurrency(b.revenue)}
                    </span>
                  </div>
                ))}
              </div>
            ) : (
              <p className="text-gray-400 text-sm text-center py-6">
                Sem dados no período
              </p>
            )}
          </div>

          {/* Receita por Forma de Pagamento */}
          {paymentEntries.length > 0 && (
            <div className="gobarber-card">
              <h3 className="font-semibold text-[#1A1A2E] mb-4 flex items-center gap-2">
                <FaDollarSign className="text-[#0F3460]" /> Receita por
                Pagamento
              </h3>
              <div className="flex gap-6 items-center">
                <div className="h-40 w-40 flex-shrink-0">
                  <Doughnut
                    data={paymentChart}
                    options={{
                      responsive: true,
                      maintainAspectRatio: false,
                      plugins: { legend: { display: false } },
                    }}
                  />
                </div>
                <div className="space-y-2 flex-1">
                  {paymentEntries.map(([key, val], i) => (
                    <div
                      key={key}
                      className="flex items-center justify-between text-sm"
                    >
                      <div className="flex items-center gap-2">
                        <div
                          className="w-3 h-3 rounded-full"
                          style={{ backgroundColor: PAYMENT_COLORS[i] }}
                        />
                        <span className="text-gray-600">
                          {PAYMENT_LABELS[key] ?? key}
                        </span>
                      </div>
                      <span className="font-medium text-[#1A1A2E]">
                        {formatCurrency(val)}
                      </span>
                    </div>
                  ))}
                </div>
              </div>
            </div>
          )}
        </div>
      </div>
    </GoBarberLayout>
  );
}
