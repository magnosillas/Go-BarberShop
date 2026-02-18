"use client";

import GoBarberLayout from "@/components/Layout/GoBarberLayout";
import Modal from "@/components/Modal/Modal";
import React, { useEffect, useState } from "react";
import { generica } from "@/api/api";
import { toast } from "react-toastify";
import { FaPlus, FaTrash, FaEdit, FaEye } from "react-icons/fa";

interface Service {
  id: number;
  name?: string;
  description?: string;
  value?: number;
  time?: number;
  active?: boolean;
}

const initialForm = { name: "", description: "", value: 0, time: 30 };

export default function ServicosPage() {
  const [services, setServices] = useState<Service[]>([]);
  const [loading, setLoading] = useState(true);
  const [modalOpen, setModalOpen] = useState(false);
  const [saving, setSaving] = useState(false);
  const [form, setForm] = useState(initialForm);
  const [editingId, setEditingId] = useState<number | null>(null);
  const [detailService, setDetailService] = useState<Service | null>(null);

  useEffect(() => { loadServices(); }, []);

  async function loadServices() {
    setLoading(true);
    try {
      const response = await generica({ metodo: "GET", uri: "/services", params: { page: 0, size: 100 } });
      const data = response?.data?.content || response?.data || [];
      setServices(Array.isArray(data) ? data : []);
    } catch { toast.error("Erro ao carregar serviços"); }
    finally { setLoading(false); }
  }

  // GET /services/{id} — fetch single service detail
  async function viewServiceDetail(id: number) {
    try {
      const res = await generica({ metodo: "GET", uri: `/services/${id}` });
      setDetailService(res?.data || null);
    } catch {
      toast.error("Erro ao carregar detalhe do serviço");
    }
  }

  function openCreate() { setForm(initialForm); setEditingId(null); setModalOpen(true); }

  function openEdit(s: Service) {
    setForm({ name: s.name || "", description: s.description || "", value: s.value || 0, time: s.time || 30 });
    setEditingId(s.id);
    setModalOpen(true);
  }

  async function handleSubmit(e: React.FormEvent) {
    e.preventDefault();
    if (!form.name) { toast.error("Preencha o nome do serviço"); return; }
    setSaving(true);
    try {
      if (editingId) {
        const res = await generica({ metodo: "PUT", uri: `/services/${editingId}`, data: form });
        if (res?.status === 200) { toast.success("Serviço atualizado!"); setModalOpen(false); loadServices(); }
        else toast.error("Erro ao atualizar serviço");
      } else {
        const res = await generica({ metodo: "POST", uri: "/services", data: form });
        if (res?.status === 200 || res?.status === 201) { toast.success("Serviço cadastrado!"); setModalOpen(false); loadServices(); }
        else toast.error(res?.data?.message || "Erro ao cadastrar serviço");
      }
    } catch { toast.error("Erro ao salvar serviço"); }
    finally { setSaving(false); }
  }

  async function handleDelete(id: number) {
    if (!confirm("Tem certeza que deseja excluir este serviço?")) return;
    try {
      const res = await generica({ metodo: "DELETE", uri: `/services/${id}` });
      if (res?.status === 200 || res?.status === 204) { toast.success("Serviço excluído!"); loadServices(); }
      else toast.error("Erro ao excluir serviço");
    } catch { toast.error("Erro ao excluir serviço"); }
  }

  return (
    <GoBarberLayout>
      <div className="space-y-6">
        <div className="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4">
          <h1 className="text-2xl font-bold text-[#1A1A2E]">Serviços</h1>
          <button onClick={openCreate} className="gobarber-btn-primary flex items-center gap-2">
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
            <div className="col-span-full text-center py-12 text-gray-400">Nenhum serviço cadastrado</div>
          ) : (
            services.map((service) => (
              <div key={service.id} className="gobarber-card hover:border-[#E94560]/30">
                <div className="flex items-center justify-between mb-3">
                  <h3 className="font-semibold text-[#1A1A2E]">{service.name || "Sem nome"}</h3>
                  <span className={`px-2 py-1 text-xs rounded-full ${service.active !== false ? "bg-green-100 text-green-700" : "bg-gray-100 text-gray-500"}`}>
                    {service.active !== false ? "Ativo" : "Inativo"}
                  </span>
                </div>
                {service.description && <p className="text-sm text-gray-500 mb-3">{service.description}</p>}
                <div className="flex items-center justify-between pt-3 border-t border-gray-100">
                  <div className="flex gap-4">
                    <span className="text-lg font-bold text-[#E94560]">R$ {service.value?.toFixed(2) || "0.00"}</span>
                    {service.time && <span className="text-sm text-gray-500 flex items-center">🕒 {service.time} min</span>}
                  </div>
                  <div className="flex gap-2">
                    <button onClick={() => viewServiceDetail(service.id)} className="p-1.5 text-blue-600 hover:bg-blue-50 rounded" title="Detalhes"><FaEye /></button>
                    <button onClick={() => openEdit(service)} className="p-1.5 text-yellow-600 hover:bg-yellow-50 rounded"><FaEdit /></button>
                    <button onClick={() => handleDelete(service.id)} className="p-1.5 text-red-600 hover:bg-red-50 rounded"><FaTrash /></button>
                  </div>
                </div>
              </div>
            ))
          )}
        </div>
      </div>

      <Modal isOpen={modalOpen} onClose={() => setModalOpen(false)} title={editingId ? "Editar Serviço" : "Novo Serviço"}>
        <form onSubmit={handleSubmit} className="space-y-4">
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">Nome *</label>
            <input type="text" value={form.name} onChange={(e) => setForm({ ...form, name: e.target.value })} className="gobarber-input" required />
          </div>
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">Descrição</label>
            <textarea value={form.description} onChange={(e) => setForm({ ...form, description: e.target.value })} className="gobarber-input" rows={2} />
          </div>
          <div className="grid grid-cols-2 gap-4">
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">Preço (R$) *</label>
              <input type="number" step="0.01" value={form.value} onChange={(e) => setForm({ ...form, value: parseFloat(e.target.value) || 0 })} className="gobarber-input" required />
            </div>
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">Duração (min)</label>
              <input type="number" value={form.time} onChange={(e) => setForm({ ...form, time: parseInt(e.target.value) || 0 })} className="gobarber-input" />
            </div>
          </div>
          <div className="flex justify-end gap-3 pt-4 border-t">
            <button type="button" onClick={() => setModalOpen(false)} className="px-4 py-2 text-gray-600 hover:bg-gray-100 rounded-lg">Cancelar</button>
            <button type="submit" disabled={saving} className="gobarber-btn-primary">{saving ? "Salvando..." : editingId ? "Atualizar" : "Cadastrar"}</button>
          </div>
        </form>
      </Modal>

      {/* Modal Detalhe Serviço (GET /services/{id}) */}
      <Modal isOpen={!!detailService} onClose={() => setDetailService(null)} title={detailService?.name || "Serviço"}>
        {detailService && (
          <div className="space-y-4">
            <div className="flex items-center gap-3">
              <div className="w-12 h-12 rounded-full bg-gradient-to-br from-[#E94560] to-[#0F3460] flex items-center justify-center text-white text-lg font-bold">
                {detailService.name?.charAt(0)?.toUpperCase() || "S"}
              </div>
              <div>
                <h3 className="font-semibold text-lg text-[#1A1A2E]">{detailService.name}</h3>
                <span className={`px-2 py-0.5 text-xs rounded-full ${detailService.active !== false ? "bg-green-100 text-green-700" : "bg-gray-100 text-gray-500"}`}>
                  {detailService.active !== false ? "Ativo" : "Inativo"}
                </span>
              </div>
            </div>
            {detailService.description && <p className="text-gray-600 text-sm">{detailService.description}</p>}
            <div className="grid grid-cols-2 gap-3">
              <div className="bg-gray-50 p-3 rounded-lg">
                <p className="text-xs text-gray-500">Preço</p>
                <p className="text-lg font-bold text-[#E94560]">R$ {detailService.value?.toFixed(2) || "0.00"}</p>
              </div>
              <div className="bg-gray-50 p-3 rounded-lg">
                <p className="text-xs text-gray-500">Duração</p>
                <p className="text-lg font-bold text-[#1A1A2E]">{detailService.time || 0} min</p>
              </div>
            </div>
          </div>
        )}
      </Modal>
    </GoBarberLayout>
  );
}
