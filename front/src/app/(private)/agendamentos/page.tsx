"use client";

import GoBarberLayout from "@/components/Layout/GoBarberLayout";
import React, { useEffect, useState } from "react";
import { generica } from "@/api/api";
import { toast } from "react-toastify";
import { FaPlus, FaSearch, FaTrash, FaEdit, FaEye } from "react-icons/fa";

interface Appointment {
  id: number;
  clientName?: string;
  barberName?: string;
  serviceName?: string;
  date?: string;
  time?: string;
  status?: string;
}

export default function AgendamentosPage() {
  const [appointments, setAppointments] = useState<Appointment[]>([]);
  const [loading, setLoading] = useState(true);
  const [search, setSearch] = useState("");
  const [page, setPage] = useState(0);

  useEffect(() => {
    loadAppointments();
  }, [page]);

  async function loadAppointments() {
    setLoading(true);
    try {
      const response = await generica({
        metodo: "GET",
        uri: "/appointments",
        params: { page, size: 10 },
      });
      if (response?.data?.content) {
        setAppointments(response.data.content);
      } else if (Array.isArray(response?.data)) {
        setAppointments(response.data);
      }
    } catch (err) {
      toast.error("Erro ao carregar agendamentos");
    } finally {
      setLoading(false);
    }
  }

  return (
    <GoBarberLayout>
      <div className="space-y-6">
        <div className="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4">
          <h1 className="text-2xl font-bold text-[#1A1A2E]">Agendamentos</h1>
          <button className="gobarber-btn-primary flex items-center gap-2">
            <FaPlus /> Novo Agendamento
          </button>
        </div>

        {/* Busca */}
        <div className="gobarber-card">
          <div className="relative">
            <FaSearch className="absolute left-3 top-1/2 -translate-y-1/2 text-gray-400" />
            <input
              type="text"
              placeholder="Buscar agendamentos..."
              value={search}
              onChange={(e) => setSearch(e.target.value)}
              className="gobarber-input pl-10"
            />
          </div>
        </div>

        {/* Tabela */}
        <div className="gobarber-card overflow-x-auto">
          <table className="w-full text-sm">
            <thead>
              <tr className="border-b border-gray-200">
                <th className="text-left py-3 px-4 font-semibold text-gray-600">
                  Cliente
                </th>
                <th className="text-left py-3 px-4 font-semibold text-gray-600">
                  Barbeiro
                </th>
                <th className="text-left py-3 px-4 font-semibold text-gray-600">
                  Serviço
                </th>
                <th className="text-left py-3 px-4 font-semibold text-gray-600">
                  Data/Hora
                </th>
                <th className="text-left py-3 px-4 font-semibold text-gray-600">
                  Status
                </th>
                <th className="text-left py-3 px-4 font-semibold text-gray-600">
                  Ações
                </th>
              </tr>
            </thead>
            <tbody>
              {loading ? (
                <tr>
                  <td colSpan={6} className="text-center py-8 text-gray-400">
                    Carregando...
                  </td>
                </tr>
              ) : appointments.length === 0 ? (
                <tr>
                  <td colSpan={6} className="text-center py-8 text-gray-400">
                    Nenhum agendamento encontrado
                  </td>
                </tr>
              ) : (
                appointments.map((apt) => (
                  <tr
                    key={apt.id}
                    className="border-b border-gray-100 hover:bg-gray-50"
                  >
                    <td className="py-3 px-4">{apt.clientName || "—"}</td>
                    <td className="py-3 px-4">{apt.barberName || "—"}</td>
                    <td className="py-3 px-4">{apt.serviceName || "—"}</td>
                    <td className="py-3 px-4">
                      {apt.date || "—"} {apt.time || ""}
                    </td>
                    <td className="py-3 px-4">
                      <span className="px-2 py-1 text-xs rounded-full bg-blue-100 text-blue-700">
                        {apt.status || "pendente"}
                      </span>
                    </td>
                    <td className="py-3 px-4">
                      <div className="flex items-center gap-2">
                        <button className="p-1.5 text-blue-600 hover:bg-blue-50 rounded">
                          <FaEye />
                        </button>
                        <button className="p-1.5 text-yellow-600 hover:bg-yellow-50 rounded">
                          <FaEdit />
                        </button>
                        <button className="p-1.5 text-red-600 hover:bg-red-50 rounded">
                          <FaTrash />
                        </button>
                      </div>
                    </td>
                  </tr>
                ))
              )}
            </tbody>
          </table>
        </div>
      </div>
    </GoBarberLayout>
  );
}
