"use client";

import GoBarberLayout from "@/components/Layout/GoBarberLayout";
import React, { useEffect, useState } from "react";
import { generica } from "@/api/api";
import { toast } from "react-toastify";
import {
  FaSearch,
  FaCreditCard,
  FaMoneyBillWave,
  FaQrcode,
} from "react-icons/fa";

interface Payment {
  id: number;
  clientName?: string;
  amount?: number;
  method?: string;
  status?: string;
  createdAt?: string;
}

export default function PagamentosPage() {
  const [payments, setPayments] = useState<Payment[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    loadPayments();
  }, []);

  async function loadPayments() {
    setLoading(true);
    try {
      const response = await generica({
        metodo: "GET",
        uri: "/payment",
        params: { page: 0, size: 20 },
      });
      const data = response?.data?.content || response?.data || [];
      setPayments(Array.isArray(data) ? data : []);
    } catch {
      toast.error("Erro ao carregar pagamentos");
    } finally {
      setLoading(false);
    }
  }

  const getMethodIcon = (method?: string) => {
    switch (method?.toUpperCase()) {
      case "PIX":
        return <FaQrcode className="text-green-500" />;
      case "CREDIT_CARD":
      case "DEBIT_CARD":
        return <FaCreditCard className="text-blue-500" />;
      default:
        return <FaMoneyBillWave className="text-yellow-600" />;
    }
  };

  const getStatusBadge = (status?: string) => {
    switch (status?.toUpperCase()) {
      case "CONFIRMED":
        return "bg-green-100 text-green-700";
      case "PENDING":
        return "bg-yellow-100 text-yellow-700";
      case "CANCELLED":
        return "bg-red-100 text-red-700";
      case "REFUNDED":
        return "bg-purple-100 text-purple-700";
      default:
        return "bg-gray-100 text-gray-600";
    }
  };

  return (
    <GoBarberLayout>
      <div className="space-y-6">
        <h1 className="text-2xl font-bold text-[#1A1A2E]">Pagamentos</h1>

        <div className="gobarber-card overflow-x-auto">
          <table className="w-full text-sm">
            <thead>
              <tr className="border-b border-gray-200">
                <th className="text-left py-3 px-4 font-semibold text-gray-600">
                  Cliente
                </th>
                <th className="text-left py-3 px-4 font-semibold text-gray-600">
                  Valor
                </th>
                <th className="text-left py-3 px-4 font-semibold text-gray-600">
                  Método
                </th>
                <th className="text-left py-3 px-4 font-semibold text-gray-600">
                  Status
                </th>
                <th className="text-left py-3 px-4 font-semibold text-gray-600">
                  Data
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
              ) : payments.length === 0 ? (
                <tr>
                  <td colSpan={5} className="text-center py-8 text-gray-400">
                    Nenhum pagamento registrado
                  </td>
                </tr>
              ) : (
                payments.map((p) => (
                  <tr
                    key={p.id}
                    className="border-b border-gray-100 hover:bg-gray-50"
                  >
                    <td className="py-3 px-4 font-medium">
                      {p.clientName || "—"}
                    </td>
                    <td className="py-3 px-4 text-[#1A1A2E] font-semibold">
                      R$ {p.amount?.toFixed(2) || "0.00"}
                    </td>
                    <td className="py-3 px-4">
                      <span className="flex items-center gap-2">
                        {getMethodIcon(p.method)} {p.method || "—"}
                      </span>
                    </td>
                    <td className="py-3 px-4">
                      <span
                        className={`px-2 py-1 text-xs rounded-full ${getStatusBadge(p.status)}`}
                      >
                        {p.status || "—"}
                      </span>
                    </td>
                    <td className="py-3 px-4 text-gray-500">
                      {p.createdAt
                        ? new Date(p.createdAt).toLocaleDateString("pt-BR")
                        : "—"}
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
