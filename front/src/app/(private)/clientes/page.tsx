"use client";

import GoBarberLayout from "@/components/Layout/GoBarberLayout";
import React, { useEffect, useState } from "react";
import { generica } from "@/api/api";
import { toast } from "react-toastify";
import { FaPlus, FaSearch, FaTrash, FaEdit, FaEye } from "react-icons/fa";

interface Client {
  id: number;
  name?: string;
  email?: string;
  phone?: string;
  cpf?: string;
  loyaltyPoints?: number;
  loyaltyTier?: string;
}

export default function ClientesPage() {
  const [clients, setClients] = useState<Client[]>([]);
  const [loading, setLoading] = useState(true);
  const [search, setSearch] = useState("");

  useEffect(() => {
    loadClients();
  }, []);

  async function loadClients() {
    setLoading(true);
    try {
      const response = await generica({
        metodo: "GET",
        uri: "/client",
        params: { page: 0, size: 20 },
      });
      const data = response?.data?.content || response?.data || [];
      setClients(Array.isArray(data) ? data : []);
    } catch {
      toast.error("Erro ao carregar clientes");
    } finally {
      setLoading(false);
    }
  }

  return (
    <GoBarberLayout>
      <div className="space-y-6">
        <div className="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4">
          <h1 className="text-2xl font-bold text-[#1A1A2E]">Clientes</h1>
          <button className="gobarber-btn-primary flex items-center gap-2">
            <FaPlus /> Novo Cliente
          </button>
        </div>

        <div className="gobarber-card">
          <div className="relative">
            <FaSearch className="absolute left-3 top-1/2 -translate-y-1/2 text-gray-400" />
            <input
              type="text"
              placeholder="Buscar clientes por nome, email ou CPF..."
              value={search}
              onChange={(e) => setSearch(e.target.value)}
              className="gobarber-input pl-10"
            />
          </div>
        </div>

        <div className="gobarber-card overflow-x-auto">
          <table className="w-full text-sm">
            <thead>
              <tr className="border-b border-gray-200">
                <th className="text-left py-3 px-4 font-semibold text-gray-600">
                  Nome
                </th>
                <th className="text-left py-3 px-4 font-semibold text-gray-600">
                  Email
                </th>
                <th className="text-left py-3 px-4 font-semibold text-gray-600">
                  Telefone
                </th>
                <th className="text-left py-3 px-4 font-semibold text-gray-600">
                  Fidelidade
                </th>
                <th className="text-left py-3 px-4 font-semibold text-gray-600">
                  Ações
                </th>
              </tr>
            </thead>
            <tbody>
              {loading ? (
                <tr>
                  <td colSpan={5} className="text-center py-8 text-gray-400">
                    Carregando...
                  </td>
                </tr>
              ) : clients.length === 0 ? (
                <tr>
                  <td colSpan={5} className="text-center py-8 text-gray-400">
                    Nenhum cliente encontrado
                  </td>
                </tr>
              ) : (
                clients.map((client) => (
                  <tr
                    key={client.id}
                    className="border-b border-gray-100 hover:bg-gray-50"
                  >
                    <td className="py-3 px-4 font-medium">
                      {client.name || "—"}
                    </td>
                    <td className="py-3 px-4 text-gray-600">
                      {client.email || "—"}
                    </td>
                    <td className="py-3 px-4 text-gray-600">
                      {client.phone || "—"}
                    </td>
                    <td className="py-3 px-4">
                      <span className="px-2 py-1 text-xs rounded-full bg-yellow-100 text-yellow-700">
                        {client.loyaltyPoints ?? 0} pts
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
