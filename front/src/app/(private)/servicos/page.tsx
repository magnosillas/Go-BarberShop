"use client";

import GoBarberLayout from "@/components/Layout/GoBarberLayout";
import React, { useEffect, useState } from "react";
import { generica } from "@/api/api";
import { toast } from "react-toastify";
import { FaPlus, FaSearch, FaTrash, FaEdit } from "react-icons/fa";

interface Service {
  id: number;
  name?: string;
  description?: string;
  price?: number;
  duration?: number;
  active?: boolean;
}

export default function ServicosPage() {
  const [services, setServices] = useState<Service[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    loadServices();
  }, []);

  async function loadServices() {
    setLoading(true);
    try {
      const response = await generica({
        metodo: "GET",
        uri: "/services",
        params: { page: 0, size: 20 },
      });
      const data = response?.data?.content || response?.data || [];
      setServices(Array.isArray(data) ? data : []);
    } catch {
      toast.error("Erro ao carregar serviços");
    } finally {
      setLoading(false);
    }
  }

  return (
    <GoBarberLayout>
      <div className="space-y-6">
        <div className="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4">
          <h1 className="text-2xl font-bold text-[#1A1A2E]">Serviços</h1>
          <button className="gobarber-btn-primary flex items-center gap-2">
            <FaPlus /> Novo Serviço
          </button>
        </div>

        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
          {loading ? (
            Array.from({ length: 6 }).map((_, i) => (
              <div key={i} className="gobarber-card animate-pulse">
                <div className="h-5 bg-gray-200 rounded w-3/4 mb-3" />
                <div className="h-4 bg-gray-200 rounded w-1/2 mb-2" />
                <div className="h-4 bg-gray-200 rounded w-1/3" />
              </div>
            ))
          ) : services.length === 0 ? (
            <div className="col-span-full text-center py-12 text-gray-400">
              Nenhum serviço cadastrado
            </div>
          ) : (
            services.map((service) => (
              <div
                key={service.id}
                className="gobarber-card hover:border-[#E94560]/30"
              >
                <div className="flex items-center justify-between mb-3">
                  <h3 className="font-semibold text-[#1A1A2E]">
                    {service.name || "Sem nome"}
                  </h3>
                  <span
                    className={`px-2 py-1 text-xs rounded-full ${service.active !== false ? "bg-green-100 text-green-700" : "bg-gray-100 text-gray-500"}`}
                  >
                    {service.active !== false ? "Ativo" : "Inativo"}
                  </span>
                </div>
                {service.description && (
                  <p className="text-sm text-gray-500 mb-3">
                    {service.description}
                  </p>
                )}
                <div className="flex items-center justify-between pt-3 border-t border-gray-100">
                  <div className="flex gap-4">
                    <span className="text-lg font-bold text-[#E94560]">
                      R$ {service.price?.toFixed(2) || "0.00"}
                    </span>
                    {service.duration && (
                      <span className="text-sm text-gray-500 flex items-center">
                        🕒 {service.duration} min
                      </span>
                    )}
                  </div>
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
