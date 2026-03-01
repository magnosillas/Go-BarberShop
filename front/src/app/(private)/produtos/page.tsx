"use client";

import GoBarberLayout from "@/components/Layout/GoBarberLayout";
import Modal from "@/components/Modal/Modal";
import React, { useEffect, useState } from "react";
import { generica } from "@/api/api";
import { toast } from "react-toastify";
import {
  FaPlus,
  FaEdit,
  FaTrash,
  FaBoxes,
  FaChevronDown,
  FaChevronUp,
  FaExclamationTriangle,
  FaEye,
  FaInfoCircle,
} from "react-icons/fa";
import Swal from "sweetalert2";

interface Product {
  id: number;
  name?: string;
  description?: string;
  brand?: string;
  price?: number;
  size?: string;
  quantity?: number; // computed from stock entries
}

interface StockEntry {
  idStock: number;
  batchNumber?: string;
  quantity: number;
  expirationDate?: string;
  acquisitionDate?: string;
  idProduct: number;
}

const initialForm = { name: "", brand: "", description: "", price: 0, size: "" };
const initialStockForm = {
  batchNumber: "",
  quantity: 1,
  expirationDate: "",
  acquisitionDate: new Date().toISOString().split("T")[0],
  idProduct: 0,
};

export default function ProdutosPage() {
  const [products, setProducts] = useState<Product[]>([]);
  const [loading, setLoading] = useState(true);
  const [modalOpen, setModalOpen] = useState(false);
  const [saving, setSaving] = useState(false);
  const [form, setForm] = useState(initialForm);
  const [editingId, setEditingId] = useState<number | null>(null);

  // Stock state
  const [expandedProduct, setExpandedProduct] = useState<number | null>(null);
  const [stockEntries, setStockEntries] = useState<StockEntry[]>([]);
  const [loadingStock, setLoadingStock] = useState(false);
  const [stockModalOpen, setStockModalOpen] = useState(false);
  const [stockForm, setStockForm] = useState(initialStockForm);
  const [editingStockId, setEditingStockId] = useState<number | null>(null);
  const [savingStock, setSavingStock] = useState(false);

  // Detail modals
  const [detailProduct, setDetailProduct] = useState<Product | null>(null);
  const [detailStock, setDetailStock] = useState<StockEntry | null>(null);
  const [loadingDetail, setLoadingDetail] = useState(false);

  useEffect(() => {
    loadProducts();
  }, []);

  async function loadProducts() {
    setLoading(true);
    try {
      const response = await generica({
        metodo: "GET",
        uri: "/product",
        params: { page: 0, size: 100 },
      });
      const data = response?.data?.content || response?.data || [];
      const prods: Product[] = Array.isArray(data) ? data : [];

      // Fetch stock totals for each product in parallel
      const withStock = await Promise.all(
        prods.map(async (p) => {
          try {
            const sRes = await generica({
              metodo: "GET",
              uri: `/stock/product/${p.id}`,
              params: { page: 0, size: 100 },
            });
            const stockData = sRes?.data?.content || sRes?.data || [];
            const entries = Array.isArray(stockData) ? stockData : [];
            const totalQty = entries.reduce((sum: number, e: any) => sum + (e.quantity || 0), 0);
            return { ...p, quantity: totalQty };
          } catch {
            return { ...p, quantity: 0 };
          }
        })
      );
      setProducts(withStock);
    } catch {
      toast.error("Erro ao carregar produtos");
    } finally {
      setLoading(false);
    }
  }

  async function loadStock(productId: number) {
    setLoadingStock(true);
    try {
      const res = await generica({
        metodo: "GET",
        uri: `/stock/product/${productId}`,
        params: { page: 0, size: 100 },
      });
      const data = res?.data?.content || res?.data || [];
      setStockEntries(Array.isArray(data) ? data : []);
    } catch {
      setStockEntries([]);
    } finally {
      setLoadingStock(false);
    }
  }

  // GET /product/{id} — fetch single product detail (enriched with stock qty)
  async function viewProductDetail(id: number) {
    setLoadingDetail(true);
    setDetailProduct(null);
    try {
      const [prodRes, stockRes] = await Promise.all([
        generica({ metodo: "GET", uri: `/product/${id}` }),
        generica({ metodo: "GET", uri: `/stock/product/${id}`, params: { page: 0, size: 100 } }).catch(() => null),
      ]);
      const prod = prodRes?.data || null;
      if (prod) {
        const stockData = stockRes?.data?.content || stockRes?.data || [];
        const entries = Array.isArray(stockData) ? stockData : [];
        const totalQty = entries.reduce((sum: number, e: any) => sum + (e.quantity || 0), 0);
        setDetailProduct({ ...prod, quantity: totalQty });
      }
    } catch {
      toast.error("Erro ao carregar detalhe do produto");
    } finally {
      setLoadingDetail(false);
    }
  }

  // GET /stock/{id} — fetch single stock entry detail
  async function viewStockDetail(id: number) {
    setLoadingDetail(true);
    setDetailStock(null);
    try {
      const res = await generica({ metodo: "GET", uri: `/stock/${id}` });
      setDetailStock(res?.data || null);
    } catch {
      toast.error("Erro ao carregar detalhe do estoque");
    } finally {
      setLoadingDetail(false);
    }
  }

  function toggleExpand(productId: number) {
    if (expandedProduct === productId) {
      setExpandedProduct(null);
    } else {
      setExpandedProduct(productId);
      loadStock(productId);
    }
  }

  function openCreate() {
    setForm(initialForm);
    setEditingId(null);
    setModalOpen(true);
  }

  function openEdit(p: Product) {
    setForm({
      name: p.name || "",
      brand: p.brand || "",
      description: p.description || "",
      price: p.price || 0,
      size: p.size || "",
    });
    setEditingId(p.id);
    setModalOpen(true);
  }

  async function handleSubmit(e: React.FormEvent) {
    e.preventDefault();
    if (!form.name) {
      toast.error("Preencha o nome do produto");
      return;
    }
    setSaving(true);
    try {
      if (editingId) {
        const res = await generica({
          metodo: "PUT",
          uri: `/product/${editingId}`,
          data: form,
        });
        if (res?.status === 200) {
          toast.success("Produto atualizado!");
          setModalOpen(false);
          loadProducts();
        } else toast.error("Erro ao atualizar produto");
      } else {
        const res = await generica({
          metodo: "POST",
          uri: "/product",
          data: form,
        });
        if (res?.status === 200 || res?.status === 201) {
          toast.success("Produto cadastrado!");
          setModalOpen(false);
          loadProducts();
        } else toast.error(res?.data?.message || "Erro ao cadastrar produto");
      }
    } catch {
      toast.error("Erro ao salvar produto");
    } finally {
      setSaving(false);
    }
  }

  async function handleDelete(id: number) {
    const result = await Swal.fire({
      title: "Tem certeza?",
      text: "Deseja realmente excluir este produto?",
      icon: "warning",
      showCancelButton: true,
      confirmButtonColor: "#E94560",
      cancelButtonColor: "#6c757d",
      confirmButtonText: "Sim, excluir!",
      cancelButtonText: "Cancelar",
    });
    if (!result.isConfirmed) return;
    try {
      const res = await generica({ metodo: "DELETE", uri: `/product/${id}` });
      if (res?.status === 200 || res?.status === 204) {
        toast.success("Produto excluído!");
        loadProducts();
      } else toast.error("Erro ao excluir produto");
    } catch {
      toast.error("Erro ao excluir produto");
    }
  }

  // Stock CRUD
  function openStockCreate(productId: number) {
    setStockForm({ ...initialStockForm, idProduct: productId });
    setEditingStockId(null);
    setStockModalOpen(true);
  }

  function openStockEdit(entry: StockEntry) {
    setStockForm({
      batchNumber: entry.batchNumber || "",
      quantity: entry.quantity,
      expirationDate: entry.expirationDate || "",
      acquisitionDate: entry.acquisitionDate || "",
      idProduct: entry.idProduct,
    });
    setEditingStockId(entry.idStock);
    setStockModalOpen(true);
  }

  async function handleStockSubmit(e: React.FormEvent) {
    e.preventDefault();
    setSavingStock(true);
    try {
      const body = {
        ...stockForm,
        expirationDate: stockForm.expirationDate || null,
        acquisitionDate: stockForm.acquisitionDate || null,
      };
      if (editingStockId) {
        const res = await generica({
          metodo: "PUT",
          uri: `/stock/${editingStockId}`,
          data: body,
        });
        if (res?.status === 200) {
          toast.success("Estoque atualizado!");
          setStockModalOpen(false);
          loadStock(stockForm.idProduct);
          loadProducts();
        } else toast.error("Erro ao atualizar estoque");
      } else {
        const res = await generica({
          metodo: "POST",
          uri: "/stock",
          data: body,
        });
        if (res?.status === 200 || res?.status === 201) {
          toast.success("Entrada de estoque criada!");
          setStockModalOpen(false);
          loadStock(stockForm.idProduct);
          loadProducts();
        } else toast.error("Erro ao criar entrada de estoque");
      }
    } catch {
      toast.error("Erro ao salvar estoque");
    } finally {
      setSavingStock(false);
    }
  }

  async function handleStockDelete(id: number, productId: number) {
    const result = await Swal.fire({
      title: "Excluir estoque?",
      text: "Deseja remover esta entrada de estoque?",
      icon: "warning",
      showCancelButton: true,
      confirmButtonColor: "#E94560",
      cancelButtonColor: "#6c757d",
      confirmButtonText: "Sim, excluir!",
      cancelButtonText: "Cancelar",
    });
    if (!result.isConfirmed) return;
    try {
      await generica({ metodo: "DELETE", uri: `/stock/${id}` });
      toast.success("Entrada removida!");
      loadStock(productId);
      loadProducts();
    } catch {
      toast.error("Erro ao excluir estoque");
    }
  }

  function isExpiringSoon(date?: string) {
    if (!date) return false;
    const exp = new Date(date);
    const now = new Date();
    const diffDays = (exp.getTime() - now.getTime()) / (1000 * 60 * 60 * 24);
    return diffDays <= 30 && diffDays > 0;
  }

  function isExpired(date?: string) {
    if (!date) return false;
    return new Date(date) < new Date();
  }

  return (
    <GoBarberLayout>
      <div className="space-y-6">
        <div className="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4">
          <h1 className="text-2xl font-bold text-[#1A1A2E]">
            Produtos & Estoque
          </h1>
          <button
            onClick={openCreate}
            className="gobarber-btn-primary flex items-center gap-2"
          >
            <FaPlus /> Novo Produto
          </button>
        </div>

        {/* Produtos */}
        <div className="space-y-3">
          {loading ? (
            Array.from({ length: 3 }).map((_, i) => (
              <div key={i} className="gobarber-card animate-pulse">
                <div className="h-5 bg-gray-200 rounded w-1/3 mb-2" />
                <div className="h-4 bg-gray-200 rounded w-1/2" />
              </div>
            ))
          ) : products.length === 0 ? (
            <div className="gobarber-card text-center py-8 text-gray-400">
              Nenhum produto cadastrado
            </div>
          ) : (
            products.map((p) => (
              <div key={p.id} className="gobarber-card">
                <div className="flex items-center justify-between">
                  <div className="flex items-center gap-4 flex-1 min-w-0">
                    <div className="w-10 h-10 rounded-lg bg-gradient-to-br from-[#E94560] to-[#0F3460] flex items-center justify-center text-white text-sm font-bold shrink-0">
                      {p.name?.charAt(0)?.toUpperCase() || "P"}
                    </div>
                    <div className="min-w-0">
                      <p className="font-medium text-[#1A1A2E] truncate">
                        {p.name || "—"}
                      </p>
                      <p className="text-xs text-gray-500">
                        {p.brand || "Sem marca"} {p.size ? `• ${p.size}` : ""}
                      </p>
                    </div>
                  </div>
                  <div className="flex items-center gap-3 shrink-0">
                    <span className="text-[#E94560] font-bold text-sm">
                      R$ {p.price?.toFixed(2) || "0.00"}
                    </span>
                    <span
                      className={`px-2 py-1 text-xs rounded-full font-medium ${
                        (p.quantity ?? 0) > 5
                          ? "bg-green-100 text-green-700"
                          : (p.quantity ?? 0) > 0
                            ? "bg-yellow-100 text-yellow-700"
                            : "bg-red-100 text-red-700"
                      }`}
                    >
                      {p.quantity ?? 0} un
                    </span>
                    <button
                      onClick={() => viewProductDetail(p.id)}
                      className="p-1.5 text-blue-600 hover:bg-blue-50 rounded"
                      title="Ver detalhes"
                    >
                      <FaEye />
                    </button>
                    <button
                      onClick={() => toggleExpand(p.id)}
                      className="p-1.5 text-gray-500 hover:bg-gray-100 rounded"
                      title="Ver estoque"
                    >
                      {expandedProduct === p.id ? (
                        <FaChevronUp />
                      ) : (
                        <FaChevronDown />
                      )}
                    </button>
                    <button
                      onClick={() => openEdit(p)}
                      className="p-1.5 text-yellow-600 hover:bg-yellow-50 rounded"
                    >
                      <FaEdit />
                    </button>
                    <button
                      onClick={() => handleDelete(p.id)}
                      className="p-1.5 text-red-600 hover:bg-red-50 rounded"
                    >
                      <FaTrash />
                    </button>
                  </div>
                </div>

                {/* Stock entries panel */}
                {expandedProduct === p.id && (
                  <div className="mt-4 pt-4 border-t border-gray-100">
                    <div className="flex items-center justify-between mb-3">
                      <h3 className="text-sm font-semibold text-gray-700 flex items-center gap-2">
                        <FaBoxes className="text-[#0F3460]" /> Lotes em Estoque
                      </h3>
                      <button
                        onClick={() => openStockCreate(p.id)}
                        className="text-xs px-3 py-1.5 bg-[#0F3460] text-white rounded-lg hover:bg-[#1A1A2E] flex items-center gap-1"
                      >
                        <FaPlus /> Entrada
                      </button>
                    </div>
                    {loadingStock ? (
                      <p className="text-sm text-gray-400 py-2">
                        Carregando...
                      </p>
                    ) : stockEntries.length === 0 ? (
                      <p className="text-sm text-gray-400 py-2">
                        Nenhum lote no estoque
                      </p>
                    ) : (
                      <div className="overflow-x-auto">
                        <table className="w-full text-xs">
                          <thead>
                            <tr className="border-b border-gray-200 text-gray-500">
                              <th className="text-left py-2 px-2">Lote</th>
                              <th className="text-left py-2 px-2">Qtd</th>
                              <th className="text-left py-2 px-2 hidden sm:table-cell">
                                Aquisição
                              </th>
                              <th className="text-left py-2 px-2">Validade</th>
                              <th className="text-left py-2 px-2">Ações</th>
                            </tr>
                          </thead>
                          <tbody>
                            {stockEntries.map((s) => (
                              <tr
                                key={s.idStock}
                                className="border-b border-gray-50 hover:bg-gray-50"
                              >
                                <td className="py-2 px-2 font-medium">
                                  {s.batchNumber || "—"}
                                </td>
                                <td className="py-2 px-2">{s.quantity}</td>
                                <td className="py-2 px-2 hidden sm:table-cell">
                                  {s.acquisitionDate
                                    ? new Date(
                                        s.acquisitionDate
                                      ).toLocaleDateString("pt-BR")
                                    : "—"}
                                </td>
                                <td className="py-2 px-2">
                                  {s.expirationDate ? (
                                    <span
                                      className={`inline-flex items-center gap-1 ${isExpired(s.expirationDate) ? "text-red-600" : isExpiringSoon(s.expirationDate) ? "text-yellow-600" : "text-gray-700"}`}
                                    >
                                      {(isExpired(s.expirationDate) ||
                                        isExpiringSoon(s.expirationDate)) && (
                                        <FaExclamationTriangle className="text-xs" />
                                      )}
                                      {new Date(
                                        s.expirationDate
                                      ).toLocaleDateString("pt-BR")}
                                    </span>
                                  ) : (
                                    "—"
                                  )}
                                </td>
                                <td className="py-2 px-2">
                                  <div className="flex items-center gap-1">
                                    <button
                                      onClick={() => viewStockDetail(s.idStock)}
                                      className="p-1 text-blue-600 hover:bg-blue-50 rounded"
                                      title="Ver detalhe"
                                    >
                                      <FaInfoCircle />
                                    </button>
                                    <button
                                      onClick={() => openStockEdit(s)}
                                      className="p-1 text-yellow-600 hover:bg-yellow-50 rounded"
                                    >
                                      <FaEdit />
                                    </button>
                                    <button
                                      onClick={() =>
                                        handleStockDelete(s.idStock, p.id)
                                      }
                                      className="p-1 text-red-600 hover:bg-red-50 rounded"
                                    >
                                      <FaTrash />
                                    </button>
                                  </div>
                                </td>
                              </tr>
                            ))}
                          </tbody>
                        </table>
                      </div>
                    )}
                  </div>
                )}
              </div>
            ))
          )}
        </div>
      </div>

      {/* Modal Produto */}
      <Modal
        isOpen={modalOpen}
        onClose={() => setModalOpen(false)}
        title={editingId ? "Editar Produto" : "Novo Produto"}
      >
        <form onSubmit={handleSubmit} className="space-y-4">
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">
              Nome *
            </label>
            <input
              type="text"
              value={form.name}
              onChange={(e) => setForm({ ...form, name: e.target.value })}
              className="gobarber-input"
              required
            />
          </div>
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">
              Marca
            </label>
            <input
              type="text"
              value={form.brand}
              onChange={(e) => setForm({ ...form, brand: e.target.value })}
              className="gobarber-input"
            />
          </div>
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">
              Descrição
            </label>
            <textarea
              value={form.description}
              onChange={(e) =>
                setForm({ ...form, description: e.target.value })
              }
              className="gobarber-input"
              rows={2}
            />
          </div>
          <div className="grid grid-cols-2 gap-4">
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">
                Preço (R$) *
              </label>
              <input
                type="number"
                step="0.01"
                value={form.price}
                onChange={(e) =>
                  setForm({ ...form, price: parseFloat(e.target.value) || 0 })
                }
                className="gobarber-input"
                required
              />
            </div>
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">
                Tamanho
              </label>
              <input
                type="text"
                value={form.size}
                onChange={(e) => setForm({ ...form, size: e.target.value })}
                className="gobarber-input"
                placeholder="Ex: 250ml"
              />
            </div>
          </div>
          <div className="flex justify-end gap-3 pt-4 border-t">
            <button
              type="button"
              onClick={() => setModalOpen(false)}
              className="px-4 py-2 text-gray-600 hover:bg-gray-100 rounded-lg"
            >
              Cancelar
            </button>
            <button type="submit" disabled={saving} className="gobarber-btn-primary">
              {saving ? "Salvando..." : editingId ? "Atualizar" : "Cadastrar"}
            </button>
          </div>
        </form>
      </Modal>

      {/* Modal Estoque */}
      <Modal
        isOpen={stockModalOpen}
        onClose={() => setStockModalOpen(false)}
        title={editingStockId ? "Editar Entrada" : "Nova Entrada de Estoque"}
      >
        <form onSubmit={handleStockSubmit} className="space-y-4">
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">
              Número do Lote
            </label>
            <input
              type="text"
              value={stockForm.batchNumber}
              onChange={(e) =>
                setStockForm({ ...stockForm, batchNumber: e.target.value })
              }
              className="gobarber-input"
              placeholder="Ex: LOT-2024-001"
            />
          </div>
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">
              Quantidade *
            </label>
            <input
              type="number"
              min={1}
              value={stockForm.quantity}
              onChange={(e) =>
                setStockForm({
                  ...stockForm,
                  quantity: Number(e.target.value),
                })
              }
              className="gobarber-input"
              required
            />
          </div>
          <div className="grid grid-cols-2 gap-4">
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">
                Data de Aquisição
              </label>
              <input
                type="date"
                value={stockForm.acquisitionDate}
                onChange={(e) =>
                  setStockForm({
                    ...stockForm,
                    acquisitionDate: e.target.value,
                  })
                }
                className="gobarber-input"
              />
            </div>
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">
                Data de Validade
              </label>
              <input
                type="date"
                value={stockForm.expirationDate}
                onChange={(e) =>
                  setStockForm({
                    ...stockForm,
                    expirationDate: e.target.value,
                  })
                }
                className="gobarber-input"
              />
            </div>
          </div>
          <div className="flex justify-end gap-3 pt-4 border-t">
            <button
              type="button"
              onClick={() => setStockModalOpen(false)}
              className="px-4 py-2 text-gray-600 hover:bg-gray-100 rounded-lg"
            >
              Cancelar
            </button>
            <button type="submit" disabled={savingStock} className="gobarber-btn-primary">
              {savingStock
                ? "Salvando..."
                : editingStockId
                  ? "Atualizar"
                  : "Registrar"}
            </button>
          </div>
        </form>
      </Modal>

      {/* Modal Detalhe Produto (GET /product/{id}) */}
      <Modal isOpen={!!detailProduct} onClose={() => setDetailProduct(null)} title="Detalhe do Produto">
        {loadingDetail ? (
          <div className="flex justify-center py-8"><span className="animate-spin rounded-full h-8 w-8 border-b-2 border-[#E94560]" /></div>
        ) : detailProduct && (
          <div className="space-y-3">
            <div className="flex items-center gap-3">
              <div className="w-12 h-12 rounded-lg bg-gradient-to-br from-[#E94560] to-[#0F3460] flex items-center justify-center text-white text-lg font-bold">
                {detailProduct.name?.charAt(0)?.toUpperCase() || "P"}
              </div>
              <div>
                <h3 className="font-semibold text-lg text-[#1A1A2E]">{detailProduct.name}</h3>
                <p className="text-sm text-gray-500">{detailProduct.brand || "Sem marca"}</p>
              </div>
            </div>
            {detailProduct.description && <p className="text-gray-600 text-sm">{detailProduct.description}</p>}
            <div className="grid grid-cols-2 gap-3 pt-2">
              <div className="bg-gray-50 p-3 rounded-lg">
                <p className="text-xs text-gray-500">Preço</p>
                <p className="text-lg font-bold text-[#E94560]">R$ {detailProduct.price?.toFixed(2) || "0.00"}</p>
              </div>
              <div className="bg-gray-50 p-3 rounded-lg">
                <p className="text-xs text-gray-500">Estoque Total</p>
                <p className="text-lg font-bold text-[#1A1A2E]">{detailProduct.quantity ?? 0} un</p>
              </div>
              {detailProduct.size && (
                <div className="bg-gray-50 p-3 rounded-lg">
                  <p className="text-xs text-gray-500">Tamanho</p>
                  <p className="font-medium">{detailProduct.size}</p>
                </div>
              )}
              <div className="bg-gray-50 p-3 rounded-lg">
                <p className="text-xs text-gray-500">ID</p>
                <p className="font-medium">#{detailProduct.id}</p>
              </div>
            </div>
          </div>
        )}
      </Modal>

      {/* Modal Detalhe Estoque (GET /stock/{id}) */}
      <Modal isOpen={!!detailStock} onClose={() => setDetailStock(null)} title="Detalhe do Lote">
        {loadingDetail ? (
          <div className="flex justify-center py-8"><span className="animate-spin rounded-full h-8 w-8 border-b-2 border-[#E94560]" /></div>
        ) : detailStock && (
          <div className="space-y-3">
            <div className="grid grid-cols-2 gap-3">
              <div className="bg-gray-50 p-3 rounded-lg">
                <p className="text-xs text-gray-500">Número do Lote</p>
                <p className="font-medium">{detailStock.batchNumber || "—"}</p>
              </div>
              <div className="bg-gray-50 p-3 rounded-lg">
                <p className="text-xs text-gray-500">Quantidade</p>
                <p className="text-lg font-bold text-[#1A1A2E]">{detailStock.quantity}</p>
              </div>
              <div className="bg-gray-50 p-3 rounded-lg">
                <p className="text-xs text-gray-500">Data de Aquisição</p>
                <p className="font-medium">{detailStock.acquisitionDate ? new Date(detailStock.acquisitionDate).toLocaleDateString("pt-BR") : "—"}</p>
              </div>
              <div className="bg-gray-50 p-3 rounded-lg">
                <p className="text-xs text-gray-500">Validade</p>
                <p className={`font-medium ${isExpired(detailStock.expirationDate) ? "text-red-600" : isExpiringSoon(detailStock.expirationDate) ? "text-yellow-600" : ""}`}>
                  {detailStock.expirationDate ? new Date(detailStock.expirationDate).toLocaleDateString("pt-BR") : "—"}
                </p>
              </div>
              <div className="bg-gray-50 p-3 rounded-lg">
                <p className="text-xs text-gray-500">ID Produto</p>
                <p className="font-medium">#{detailStock.idProduct}</p>
              </div>
              <div className="bg-gray-50 p-3 rounded-lg">
                <p className="text-xs text-gray-500">ID Estoque</p>
                <p className="font-medium">#{detailStock.idStock}</p>
              </div>
            </div>
          </div>
        )}
      </Modal>
    </GoBarberLayout>
  );
}
