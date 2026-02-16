"use client";

import GoBarberLayout from "@/components/Layout/GoBarberLayout";
import React, { useEffect, useState } from "react";
import { generica } from "@/api/api";
import { toast } from "react-toastify";
import {
  FaPlus,
  FaClock,
  FaCheckCircle,
  FaTimesCircle,
  FaUser,
  FaCut,
} from "react-icons/fa";

interface WaitItem {
  id: number;
  clientName?: string;
  client?: { name?: string };
  barberName?: string;
  barber?: { name?: string };
  serviceName?: string;
  service?: { name?: string };
  status?: string;
  position?: number;
  createdAt?: string;
  estimatedTime?: string;
}

export default function ListaEsperaPage() {
  const [items, setItems] = useState<WaitItem[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    loadWaitList();
  }, []);

  async function loadWaitList() {
    setLoading(true);
    try {
      const response = await generica({
        metodo: "GET",
        uri: "/waitlist",
        params: { page: 0, size: 50 },
      });
      const data = response?.data?.content || response?.data || [];
      setItems(Array.isArray(data) ? data : []);
    } catch {
      toast.error("Erro ao carregar lista de espera");
    } finally {
      setLoading(false);
    }
  }

  function getStatusStyle(status?: string) {
    switch (status?.toUpperCase()) {
      case "WAITING":
        return {
          bg: "bg-yellow-100",
          text: "text-yellow-700",
          label: "Aguardando",
          icon: <FaClock />,
        };
      case "IN_SERVICE":
        return {
          bg: "bg-blue-100",
          text: "text-blue-700",
          label: "Atendendo",
          icon: <FaCut />,
        };
      case "COMPLETED":
        return {
          bg: "bg-green-100",
          text: "text-green-700",
          label: "Concluído",
          icon: <FaCheckCircle />,
        };
      case "CANCELLED":
        return {
          bg: "bg-red-100",
          text: "text-red-700",
          label: "Cancelado",
          icon: <FaTimesCircle />,
        };
      default:
        return {
          bg: "bg-gray-100",
          text: "text-gray-600",
          label: status || "—",
          icon: <FaClock />,
        };
    }
  }

  return (
    <GoBarberLayout>
      <div className="space-y-6">
        <div className="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4">
          <h1 className="text-2xl font-bold text-[#1A1A2E]">Lista de Espera</h1>
          <button className="gobarber-btn-primary flex items-center gap-2">
            <FaPlus /> Adicionar à Fila
          </button>
        </div>

        {/* Resumo */}
        <div className="grid grid-cols-2 md:grid-cols-4 gap-4">
          {[
            {
              label: "Na fila",
              value: items.filter((i) => i.status?.toUpperCase() === "WAITING")
                .length,
              color: "text-yellow-600",
            },
            {
              label: "Atendendo",
              value: items.filter(
                (i) => i.status?.toUpperCase() === "IN_SERVICE",
              ).length,
              color: "text-blue-600",
            },
            {
              label: "Concluídos",
              value: items.filter(
                (i) => i.status?.toUpperCase() === "COMPLETED",
              ).length,
              color: "text-green-600",
            },
            { label: "Total", value: items.length, color: "text-[#1A1A2E]" },
          ].map((stat) => (
            <div key={stat.label} className="gobarber-card text-center">
              <p className={`text-2xl font-bold ${stat.color}`}>{stat.value}</p>
              <p className="text-xs text-gray-500 mt-1">{stat.label}</p>
            </div>
          ))}
        </div>

        {/* Lista */}
        <div className="space-y-3">
          {loading ? (
            Array.from({ length: 5 }).map((_, i) => (
              <div
                key={i}
                className="gobarber-card animate-pulse flex items-center gap-4"
              >
                <div className="w-10 h-10 bg-gray-200 rounded-full" />
                <div className="flex-1">
                  <div className="h-4 bg-gray-200 rounded w-1/3 mb-2" />
                  <div className="h-3 bg-gray-200 rounded w-1/4" />
                </div>
              </div>
            ))
          ) : items.length === 0 ? (
            <div className="gobarber-card text-center py-12 text-gray-400">
              Lista de espera vazia
            </div>
          ) : (
            items.map((item, idx) => {
              const st = getStatusStyle(item.status);
              return (
                <div
                  key={item.id}
                  className="gobarber-card flex items-center gap-4 hover:border-[#E94560]/30"
                >
                  <div className="w-10 h-10 rounded-full bg-[#1A1A2E] text-white flex items-center justify-center font-bold text-sm">
                    {item.position ?? idx + 1}
                  </div>
                  <div className="flex-1 min-w-0">
                    <div className="flex items-center gap-2">
                      <FaUser className="text-gray-400 text-xs" />
                      <span className="font-medium text-[#1A1A2E] truncate">
                        {item.clientName || item.client?.name || "Cliente"}
                      </span>
                    </div>
                    <p className="text-sm text-gray-500">
                      {item.serviceName || item.service?.name || "—"} •{" "}
                      {item.barberName ||
                        item.barber?.name ||
                        "Qualquer barbeiro"}
                    </p>
                  </div>
                  <span
                    className={`inline-flex items-center gap-1.5 px-3 py-1 rounded-full text-xs font-medium ${st.bg} ${st.text}`}
                  >
                    {st.icon} {st.label}
                  </span>
                </div>
              );
            })
          )}
        </div>
      </div>
    </GoBarberLayout>
  );
}
