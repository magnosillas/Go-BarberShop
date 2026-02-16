"use client";

import GoBarberLayout from "@/components/Layout/GoBarberLayout";
import React, { useEffect, useState } from "react";
import { generica } from "@/api/api";
import { toast } from "react-toastify";
import { FaPlus, FaBoxes, FaEdit, FaTrash } from "react-icons/fa";

interface Product {
  id: number;
  name?: string;
  description?: string;
  brand?: string;
  price?: number;
  quantity?: number;
}

export default function ProdutosPage() {
  const [products, setProducts] = useState<Product[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    loadProducts();
  }, []);

  async function loadProducts() {
    setLoading(true);
    try {
      const response = await generica({
        metodo: "GET",
        uri: "/product",
        params: { page: 0, size: 20 },
      });
      const data = response?.data?.content || response?.data || [];
      setProducts(Array.isArray(data) ? data : []);
    } catch {
      toast.error("Erro ao carregar produtos");
    } finally {
      setLoading(false);
    }
  }

  return (
    <GoBarberLayout>
      <div className="space-y-6">
        <div className="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4">
          <h1 className="text-2xl font-bold text-[#1A1A2E]">
            Produtos & Estoque
          </h1>
          <button className="gobarber-btn-primary flex items-center gap-2">
            <FaPlus /> Novo Produto
          </button>
        </div>

        <div className="gobarber-card overflow-x-auto">
          <table className="w-full text-sm">
            <thead>
              <tr className="border-b border-gray-200">
                <th className="text-left py-3 px-4 font-semibold text-gray-600">
                  Produto
                </th>
                <th className="text-left py-3 px-4 font-semibold text-gray-600">
                  Marca
                </th>
                <th className="text-left py-3 px-4 font-semibold text-gray-600">
                  Preço
                </th>
                <th className="text-left py-3 px-4 font-semibold text-gray-600">
                  Estoque
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
              ) : products.length === 0 ? (
                <tr>
                  <td colSpan={5} className="text-center py-8 text-gray-400">
                    Nenhum produto cadastrado
                  </td>
                </tr>
              ) : (
                products.map((p) => (
                  <tr
                    key={p.id}
                    className="border-b border-gray-100 hover:bg-gray-50"
                  >
                    <td className="py-3 px-4 font-medium">{p.name || "—"}</td>
                    <td className="py-3 px-4 text-gray-600">
                      {p.brand || "—"}
                    </td>
                    <td className="py-3 px-4 text-[#E94560] font-medium">
                      R$ {p.price?.toFixed(2) || "0.00"}
                    </td>
                    <td className="py-3 px-4">
                      <span
                        className={`px-2 py-1 text-xs rounded-full ${(p.quantity ?? 0) > 5 ? "bg-green-100 text-green-700" : (p.quantity ?? 0) > 0 ? "bg-yellow-100 text-yellow-700" : "bg-red-100 text-red-700"}`}
                      >
                        {p.quantity ?? 0} un
                      </span>
                    </td>
                    <td className="py-3 px-4">
                      <div className="flex items-center gap-2">
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
