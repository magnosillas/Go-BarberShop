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
} from "react-icons/fa";

interface DashboardData {
  totalRevenue?: number;
  totalAppointments?: number;
  totalClients?: number;
  averageRating?: number;
  appointmentsByMonth?: { month: string; count: number }[];
  revenueByMonth?: { month: string; total: number }[];
  topServices?: { name: string; count: number }[];
  topBarbers?: { name: string; count: number; revenue: number }[];
}

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

  return (
    <GoBarberLayout>
      <div className="space-y-6">
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
            },
            {
              label: "Avaliação Média",
              value: (data.averageRating ?? 0).toFixed(1),
              icon: <FaStar />,
              color: "text-yellow-600",
              bg: "bg-yellow-50",
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
                </div>
              </div>
            </div>
          ))}
        </div>

        <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
          {/* Top Serviços */}
          <div className="gobarber-card">
            <h3 className="font-semibold text-[#1A1A2E] mb-4 flex items-center gap-2">
              <FaChartBar className="text-[#E94560]" /> Serviços Mais Populares
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
                    ...data.topServices!.map((x) => x.count),
                  );
                  return (
                    <div key={i}>
                      <div className="flex justify-between text-sm mb-1">
                        <span className="text-gray-700">{s.name}</span>
                        <span className="font-medium text-[#1A1A2E]">
                          {s.count}
                        </span>
                      </div>
                      <div className="w-full bg-gray-100 rounded-full h-2">
                        <div
                          className="gobarber-gradient h-2 rounded-full transition-all"
                          style={{ width: `${(s.count / max) * 100}%` }}
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
                        {b.name}
                      </p>
                      <p className="text-xs text-gray-500">
                        {b.count} atendimentos
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
        </div>
      </div>
    </GoBarberLayout>
  );
}
