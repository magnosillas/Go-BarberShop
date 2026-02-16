"use client";

import GoBarberLayout from "@/components/Layout/GoBarberLayout";
import React, { useEffect, useState } from "react";
import { generica } from "@/api/api";
import { toast } from "react-toastify";
import { FaPlus, FaSearch, FaTrash, FaEdit, FaEye } from "react-icons/fa";

interface Barber {
  id: number;
  name?: string;
  email?: string;
  phone?: string;
  specialties?: string[];
  active?: boolean;
}

export default function BarbeirosPage() {
  const [barbers, setBarbers] = useState<Barber[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    loadBarbers();
  }, []);

  async function loadBarbers() {
    setLoading(true);
    try {
      const response = await generica({
        metodo: "GET",
        uri: "/barber",
        params: { page: 0, size: 20 },
      });
      const data = response?.data?.content || response?.data || [];
      setBarbers(Array.isArray(data) ? data : []);
    } catch {
      toast.error("Erro ao carregar barbeiros");
    } finally {
      setLoading(false);
    }
  }

  return (
    <GoBarberLayout>
      <div className="space-y-6">
        <div className="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4">
          <h1 className="text-2xl font-bold text-[#1A1A2E]">Barbeiros</h1>
          <button className="gobarber-btn-primary flex items-center gap-2">
            <FaPlus /> Novo Barbeiro
          </button>
        </div>

        <div className="gobarber-card">
          <div className="relative">
            <FaSearch className="absolute left-3 top-1/2 -translate-y-1/2 text-gray-400" />
            <input
              type="text"
              placeholder="Buscar barbeiros..."
              className="gobarber-input pl-10"
            />
          </div>
        </div>

        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
          {loading ? (
            Array.from({ length: 6 }).map((_, i) => (
              <div key={i} className="gobarber-card animate-pulse">
                <div className="flex items-center gap-4">
                  <div className="w-16 h-16 bg-gray-200 rounded-full" />
                  <div className="flex-1">
                    <div className="h-4 bg-gray-200 rounded w-3/4 mb-2" />
                    <div className="h-3 bg-gray-200 rounded w-1/2" />
                  </div>
                </div>
              </div>
            ))
          ) : barbers.length === 0 ? (
            <div className="col-span-full text-center py-12 text-gray-400">
              Nenhum barbeiro cadastrado
            </div>
          ) : (
            barbers.map((barber) => (
              <div
                key={barber.id}
                className="gobarber-card hover:border-[#E94560]/30"
              >
                <div className="flex items-center gap-4 mb-4">
                  <div className="w-14 h-14 bg-[#1A1A2E] rounded-full flex items-center justify-center text-white font-bold text-lg">
                    {barber.name?.charAt(0)?.toUpperCase() || "?"}
                  </div>
                  <div>
                    <h3 className="font-semibold text-[#1A1A2E]">
                      {barber.name || "Sem nome"}
                    </h3>
                    <p className="text-sm text-gray-500">
                      {barber.email || ""}
                    </p>
                  </div>
                </div>
                {barber.phone && (
                  <p className="text-sm text-gray-600 mb-3">
                    📞 {barber.phone}
                  </p>
                )}
                <div className="flex items-center justify-between pt-3 border-t border-gray-100">
                  <span
                    className={`px-2 py-1 text-xs rounded-full ${barber.active !== false ? "bg-green-100 text-green-700" : "bg-gray-100 text-gray-500"}`}
                  >
                    {barber.active !== false ? "Ativo" : "Inativo"}
                  </span>
                  <div className="flex gap-2">
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
                </div>
              </div>
            ))
          )}
        </div>
      </div>
    </GoBarberLayout>
  );
}
