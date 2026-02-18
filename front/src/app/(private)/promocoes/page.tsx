"use client";

import GoBarberLayout from "@/components/Layout/GoBarberLayout";
import React, { useEffect, useState } from "react";
import { generica } from "@/api/api";
import { toast } from "react-toastify";
import { FaPlus, FaTag, FaEdit, FaTrash } from "react-icons/fa";

interface Sale {
  id: number;
  title?: string;
  description?: string;
  coupon?: string;
  discountPercentage?: number;
  startDate?: string;
  endDate?: string;
  active?: boolean;
}

export default function PromocoesPage() {
  const [sales, setSales] = useState<Sale[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    loadSales();
  }, []);

  async function loadSales() {
    setLoading(true);
    try {
      const response = await generica({
        metodo: "GET",
        uri: "/sale",
        params: { page: 0, size: 20 },
      });
      const data = response?.data?.content || response?.data || [];
      setSales(Array.isArray(data) ? data : []);
    } catch {
      toast.error("Erro ao carregar promoções");
    } finally {
      setLoading(false);
    }
  }

  return (
    <GoBarberLayout>
      <div className="space-y-6">
        <div className="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4">
          <h1 className="text-2xl font-bold text-[#1A1A2E]">
            Promoções & Cupons
          </h1>
          <button className="gobarber-btn-primary flex items-center gap-2">
            <FaPlus /> Nova Promoção
          </button>
        </div>

        <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
          {loading ? (
            Array.from({ length: 4 }).map((_, i) => (
              <div key={i} className="gobarber-card animate-pulse">
                <div className="h-5 bg-gray-200 rounded w-3/4 mb-3" />
                <div className="h-4 bg-gray-200 rounded w-1/2" />
              </div>
            ))
          ) : sales.length === 0 ? (
            <div className="col-span-full gobarber-card text-center py-12 text-gray-400">
              Nenhuma promoção cadastrada
            </div>
          ) : (
            sales.map((sale) => (
              <div
                key={sale.id}
                className="gobarber-card hover:border-[#E94560]/30"
              >
                <div className="flex items-start justify-between mb-3">
                  <div>
                    <h3 className="font-semibold text-[#1A1A2E]">
                      {sale.title || "Promoção"}
                    </h3>
                    {sale.description && (
                      <p className="text-sm text-gray-500 mt-1">
                        {sale.description}
                      </p>
                    )}
                  </div>
                  <span className="text-2xl font-bold text-[#E94560]">
                    {sale.discountPercentage || 0}%
                  </span>
                </div>
                {sale.coupon && (
                  <div className="flex items-center gap-2 mb-3">
                    <FaTag className="text-[#E94560]" />
                    <code className="bg-gray-100 px-3 py-1 rounded text-sm font-mono">
                      {sale.coupon}
                    </code>
                  </div>
                )}
                <div className="flex items-center justify-between pt-3 border-t border-gray-100">
                  <span
                    className={`px-2 py-1 text-xs rounded-full ${sale.active !== false ? "bg-green-100 text-green-700" : "bg-gray-100 text-gray-500"}`}
                  >
                    {sale.active !== false ? "Ativa" : "Inativa"}
                  </span>
                  <div className="flex gap-2">
                    <button className="p-1.5 text-yellow-600 hover:bg-yellow-50 rounded">
                      <FaEdit />
                    </button>
                    <button className="p-1.5 text-red-600 hover:bg-red-50 rounded">
                      <FaTrash />
                    </button>
                  </div>
                </div>
              </div>
            ))
          )}
        </div>
      </div>
    </GoBarberLayout>
  );
}
