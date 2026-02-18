"use client";

import GoBarberLayout from "@/components/Layout/GoBarberLayout";
import Modal from "@/components/Modal/Modal";
import React, { useEffect, useState } from "react";
import { generica } from "@/api/api";
import { toast } from "react-toastify";
import { FaPlus, FaEdit, FaTrash, FaMapMarkerAlt, FaEye } from "react-icons/fa";

interface Address {
  idAddress: number;
  street?: string;
  number?: string;
  complement?: string;
  neighborhood?: string;
  city?: string;
  state?: string;
  cep?: string;
}

const initialForm = { street: "", number: "", complement: "", neighborhood: "", city: "", state: "", cep: "" };

export default function EnderecosPage() {
  const [addresses, setAddresses] = useState<Address[]>([]);
  const [loading, setLoading] = useState(true);
  const [modalOpen, setModalOpen] = useState(false);
  const [saving, setSaving] = useState(false);
  const [form, setForm] = useState(initialForm);
  const [editingId, setEditingId] = useState<number | null>(null);
  const [detailAddress, setDetailAddress] = useState<Address | null>(null);

  useEffect(() => { loadAddresses(); }, []);

  // GET /address — list all addresses
  async function loadAddresses() {
    setLoading(true);
    try {
      const res = await generica({ metodo: "GET", uri: "/address" });
      const data = res?.data || [];
      setAddresses(Array.isArray(data) ? data : []);
    } catch { toast.error("Erro ao carregar endereços"); }
    finally { setLoading(false); }
  }

  // GET /address/{id} — view address detail
  async function viewDetail(id: number) {
    try {
      const res = await generica({ metodo: "GET", uri: `/address/${id}` });
      setDetailAddress(res?.data || null);
    } catch { toast.error("Erro ao carregar detalhe do endereço"); }
  }

  function openCreate() { setForm(initialForm); setEditingId(null); setModalOpen(true); }

  function openEdit(a: Address) {
    setForm({
      street: a.street || "", number: a.number || "", complement: a.complement || "",
      neighborhood: a.neighborhood || "", city: a.city || "", state: a.state || "", cep: a.cep || "",
    });
    setEditingId(a.idAddress);
    setModalOpen(true);
  }

  // POST /address — create new address
  // PUT /address/{id} — update address
  async function handleSubmit(e: React.FormEvent) {
    e.preventDefault();
    if (!form.street || !form.city) { toast.error("Preencha rua e cidade"); return; }
    setSaving(true);
    try {
      if (editingId) {
        const res = await generica({ metodo: "PUT", uri: `/address/${editingId}`, data: { idAddress: editingId, ...form } });
        if (res?.status === 200) { toast.success("Endereço atualizado!"); setModalOpen(false); loadAddresses(); }
        else toast.error("Erro ao atualizar");
      } else {
        const res = await generica({ metodo: "POST", uri: "/address", data: form });
        if (res?.status === 200 || res?.status === 201) { toast.success("Endereço cadastrado!"); setModalOpen(false); loadAddresses(); }
        else toast.error(res?.data?.message || "Erro ao cadastrar");
      }
    } catch { toast.error("Erro ao salvar endereço"); }
    finally { setSaving(false); }
  }

  // DELETE /address/{id} — delete address
  async function handleDelete(id: number) {
    if (!confirm("Excluir este endereço?")) return;
    try {
      const res = await generica({ metodo: "DELETE", uri: `/address/${id}` });
      if (res?.status === 200 || res?.status === 204) { toast.success("Endereço excluído!"); loadAddresses(); }
      else toast.error("Erro ao excluir");
    } catch { toast.error("Erro ao excluir endereço"); }
  }

  return (
    <GoBarberLayout>
      <div className="space-y-6">
        <div className="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4">
          <h1 className="text-2xl font-bold text-[#1A1A2E]">Endereços</h1>
          <button onClick={openCreate} className="gobarber-btn-primary flex items-center gap-2">
            <FaPlus /> Novo Endereço
          </button>
        </div>

        <div className="space-y-3">
          {loading ? (
            Array.from({ length: 3 }).map((_, i) => (
              <div key={i} className="gobarber-card animate-pulse"><div className="h-5 bg-gray-200 rounded w-2/3 mb-2" /><div className="h-4 bg-gray-200 rounded w-1/2" /></div>
            ))
          ) : addresses.length === 0 ? (
            <div className="gobarber-card text-center py-8 text-gray-400">Nenhum endereço cadastrado</div>
          ) : (
            addresses.map((a) => (
              <div key={a.idAddress} className="gobarber-card">
                <div className="flex items-center justify-between">
                  <div className="flex items-center gap-3 flex-1 min-w-0">
                    <div className="w-10 h-10 rounded-lg bg-gradient-to-br from-[#E94560] to-[#0F3460] flex items-center justify-center text-white"><FaMapMarkerAlt /></div>
                    <div className="min-w-0">
                      <p className="font-medium text-[#1A1A2E] truncate">{a.street}{a.number ? `, ${a.number}` : ""}</p>
                      <p className="text-xs text-gray-500">{a.neighborhood ? `${a.neighborhood} - ` : ""}{a.city || ""}{a.state ? ` / ${a.state}` : ""} {a.cep ? `• CEP: ${a.cep}` : ""}</p>
                    </div>
                  </div>
                  <div className="flex items-center gap-2 shrink-0">
                    <button onClick={() => viewDetail(a.idAddress)} className="p-1.5 text-blue-600 hover:bg-blue-50 rounded" title="Detalhes"><FaEye /></button>
                    <button onClick={() => openEdit(a)} className="p-1.5 text-yellow-600 hover:bg-yellow-50 rounded"><FaEdit /></button>
                    <button onClick={() => handleDelete(a.idAddress)} className="p-1.5 text-red-600 hover:bg-red-50 rounded"><FaTrash /></button>
                  </div>
                </div>
              </div>
            ))
          )}
        </div>
      </div>

      {/* Modal Criar/Editar */}
      <Modal isOpen={modalOpen} onClose={() => setModalOpen(false)} title={editingId ? "Editar Endereço" : "Novo Endereço"}>
        <form onSubmit={handleSubmit} className="space-y-4">
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">Rua *</label>
            <input type="text" value={form.street} onChange={(e) => setForm({ ...form, street: e.target.value })} className="gobarber-input" required />
          </div>
          <div className="grid grid-cols-2 gap-4">
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">Número</label>
              <input type="text" value={form.number} onChange={(e) => setForm({ ...form, number: e.target.value })} className="gobarber-input" />
            </div>
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">Complemento</label>
              <input type="text" value={form.complement} onChange={(e) => setForm({ ...form, complement: e.target.value })} className="gobarber-input" />
            </div>
          </div>
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">Bairro</label>
            <input type="text" value={form.neighborhood} onChange={(e) => setForm({ ...form, neighborhood: e.target.value })} className="gobarber-input" />
          </div>
          <div className="grid grid-cols-2 gap-4">
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">Cidade *</label>
              <input type="text" value={form.city} onChange={(e) => setForm({ ...form, city: e.target.value })} className="gobarber-input" required />
            </div>
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">Estado</label>
              <input type="text" value={form.state} onChange={(e) => setForm({ ...form, state: e.target.value })} className="gobarber-input" maxLength={2} placeholder="UF" />
            </div>
          </div>
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">CEP</label>
            <input type="text" value={form.cep} onChange={(e) => setForm({ ...form, cep: e.target.value })} className="gobarber-input" maxLength={8} placeholder="00000000" />
          </div>
          <div className="flex justify-end gap-3 pt-4 border-t">
            <button type="button" onClick={() => setModalOpen(false)} className="px-4 py-2 text-gray-600 hover:bg-gray-100 rounded-lg">Cancelar</button>
            <button type="submit" disabled={saving} className="gobarber-btn-primary">{saving ? "Salvando..." : editingId ? "Atualizar" : "Cadastrar"}</button>
          </div>
        </form>
      </Modal>

      {/* Modal Detalhe (GET /address/{id}) */}
      <Modal isOpen={!!detailAddress} onClose={() => setDetailAddress(null)} title="Detalhe do Endereço">
        {detailAddress && (
          <div className="space-y-3">
            <div className="flex items-center gap-2 mb-2">
              <FaMapMarkerAlt className="text-[#E94560]" />
              <span className="text-xs text-gray-400">ID: #{detailAddress.idAddress}</span>
            </div>
            <div className="grid grid-cols-2 gap-3">
              <div className="bg-gray-50 p-3 rounded-lg"><p className="text-xs text-gray-500">Rua</p><p className="font-medium">{detailAddress.street || "—"}</p></div>
              <div className="bg-gray-50 p-3 rounded-lg"><p className="text-xs text-gray-500">Número</p><p className="font-medium">{detailAddress.number || "—"}</p></div>
              <div className="bg-gray-50 p-3 rounded-lg"><p className="text-xs text-gray-500">Complemento</p><p className="font-medium">{detailAddress.complement || "—"}</p></div>
              <div className="bg-gray-50 p-3 rounded-lg"><p className="text-xs text-gray-500">Bairro</p><p className="font-medium">{detailAddress.neighborhood || "—"}</p></div>
              <div className="bg-gray-50 p-3 rounded-lg"><p className="text-xs text-gray-500">Cidade</p><p className="font-medium">{detailAddress.city || "—"}</p></div>
              <div className="bg-gray-50 p-3 rounded-lg"><p className="text-xs text-gray-500">Estado</p><p className="font-medium">{detailAddress.state || "—"}</p></div>
              <div className="bg-gray-50 p-3 rounded-lg col-span-2"><p className="text-xs text-gray-500">CEP</p><p className="font-medium">{detailAddress.cep || "—"}</p></div>
            </div>
          </div>
        )}
      </Modal>
    </GoBarberLayout>
  );
}
