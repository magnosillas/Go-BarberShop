"use client";

import GoBarberLayout from "@/components/Layout/GoBarberLayout";
import React, { useEffect, useState, useContext } from "react";
import { AuthContext } from "@/contexts/AuthContext";
import { generica } from "@/api/api";
import {
  FaCalendarCheck,
  FaDollarSign,
  FaUsers,
  FaCut,
  FaArrowUp,
  FaArrowDown,
} from "react-icons/fa";

interface DashboardData {
  totalRevenue?: number;
  totalAppointments?: number;
  totalClients?: number;
  totalBarbers?: number;
  revenueGrowth?: number;
  appointmentsToday?: number;
}

export default function DashboardPage() {
  const auth = useContext(AuthContext);
  const [data, setData] = useState<DashboardData>({});
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    async function loadDashboard() {
      try {
        const response = await generica({
          metodo: "GET",
          uri: "/dashboard/month",
        });
        if (response?.data) {
          setData(response.data);
        }
      } catch (err) {
        console.error("Erro ao carregar dashboard:", err);
      } finally {
        setLoading(false);
      }
    }
    loadDashboard();
  }, []);

  const cards = [
    {
      title: "Receita do Mês",
      value:
        data.totalRevenue != null
          ? `R$ ${data.totalRevenue.toLocaleString("pt-BR", { minimumFractionDigits: 2 })}`
          : "—",
      icon: <FaDollarSign />,
      color: "bg-green-500",
      growth: data.revenueGrowth,
    },
    {
      title: "Agendamentos Hoje",
      value: data.appointmentsToday ?? "—",
      icon: <FaCalendarCheck />,
      color: "bg-blue-500",
    },
    {
      title: "Total de Clientes",
      value: data.totalClients ?? "—",
      icon: <FaUsers />,
      color: "bg-purple-500",
    },
    {
      title: "Barbeiros Ativos",
      value: data.totalBarbers ?? "—",
      icon: <FaCut />,
      color: "bg-orange-500",
    },
  ];

  return (
    <GoBarberLayout>
      <div className="space-y-8">
        {/* Saudação */}
        <div>
          <h1 className="text-2xl font-bold text-[#1A1A2E]">
            Olá, {auth?.user?.nome?.split(" ")[0] || "Usuário"} 👋
          </h1>
          <p className="text-gray-500 mt-1">
            Veja como está o movimento da sua barbearia hoje.
          </p>
        </div>

        {/* Cards de Métricas */}
        <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-6">
          {cards.map((card, i) => (
            <div key={i} className="gobarber-card">
              <div className="flex items-center justify-between">
                <div>
                  <p className="text-sm text-gray-500 mb-1">{card.title}</p>
                  <p className="text-2xl font-bold text-[#1A1A2E]">
                    {loading ? (
                      <span className="animate-pulse bg-gray-200 rounded h-8 w-24 inline-block" />
                    ) : (
                      card.value
                    )}
                  </p>
                  {card.growth != null && (
                    <p
                      className={`text-xs mt-1 flex items-center gap-1 ${card.growth >= 0 ? "text-green-600" : "text-red-500"}`}
                    >
                      {card.growth >= 0 ? <FaArrowUp /> : <FaArrowDown />}
                      {Math.abs(card.growth).toFixed(1)}% vs mês anterior
                    </p>
                  )}
                </div>
                <div
                  className={`w-12 h-12 ${card.color} rounded-xl flex items-center justify-center text-white text-xl`}
                >
                  {card.icon}
                </div>
              </div>
            </div>
          ))}
        </div>

        {/* Próximos Agendamentos */}
        <div className="gobarber-card">
          <h3 className="text-lg font-semibold text-[#1A1A2E] mb-4">
            Próximos Agendamentos
          </h3>
          <p className="text-gray-500 text-sm">
            Conecte-se com a API para visualizar os agendamentos do dia.
          </p>
        </div>
      </div>
    </GoBarberLayout>
  );
}
