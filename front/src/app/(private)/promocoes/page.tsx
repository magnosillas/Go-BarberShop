"use client";

import GoBarberLayout from "@/components/Layout/GoBarberLayout";
import Modal from "@/components/Modal/Modal";
import React, { useEffect, useState } from "react";
import { generica } from "@/api/api";
import { toast } from "react-toastify";
import { FaPlus, FaTag, FaEdit, FaTrash, FaEnvelope, FaPaperPlane, FaEye, FaSearch } from "react-icons/fa";
import Swal from "sweetalert2";

interface Sale {
  id: number;
  name?: string;
  title?: string;
  description?: string;
  coupon?: string;
  totalPrice?: number;
  discountPercentage?: number;
  startDate?: string;
  endDate?: string;
  active?: boolean;
}

/** Derive active status from dates (API does not return 'active') */
function isSaleActive(sale: Sale): boolean {
  if (sale.active != null) return sale.active;
  const now = new Date();
  if (sale.endDate && new Date(sale.endDate) < now) return false;
  if (sale.startDate && new Date(sale.startDate) > now) return false;
  return true;
}

const initialForm = { name: "", totalPrice: 0, startDate: "", endDate: "", coupon: "" };

export default function PromocoesPage() {
  const [sales, setSales] = useState<Sale[]>([]);
  const [loading, setLoading] = useState(true);
  const [modalOpen, setModalOpen] = useState(false);
  const [emailModalOpen, setEmailModalOpen] = useState(false);
  const [saving, setSaving] = useState(false);
  const [sendingEmail, setSendingEmail] = useState(false);
  const [form, setForm] = useState(initialForm);
  const [editingId, setEditingId] = useState<number | null>(null);
  const [selectedSaleForEmail, setSelectedSaleForEmail] = useState<Sale | null>(null);
  const [detailSale, setDetailSale] = useState<Sale | null>(null);
  const [couponSearch, setCouponSearch] = useState("");
  const [couponResult, setCouponResult] = useState<Sale | null>(null);
  const [searchingCoupon, setSearchingCoupon] = useState(false);

  useEffect(() => { loadSales(); }, []);

  async function loadSales() {
    setLoading(true);
    try {
      const response = await generica({ metodo: "GET", uri: "/sale", params: { page: 0, size: 100 } });
      const data = response?.data?.content || response?.data || [];
      setSales(Array.isArray(data) ? data : []);
    } catch { toast.error("Erro ao carregar promoções"); }
    finally { setLoading(false); }
  }

  function openCreate() { setForm(initialForm); setEditingId(null); setModalOpen(true); }

  function openEdit(s: Sale) {
    setForm({
      name: s.name || s.title || "",
      totalPrice: s.totalPrice || 0,
      startDate: s.startDate ? s.startDate.substring(0, 10) : "",
      endDate: s.endDate ? s.endDate.substring(0, 10) : "",
      coupon: s.coupon || "",
    });
    setEditingId(s.id);
    setModalOpen(true);
  }

  function openEmailModal(sale: Sale) {
    setSelectedSaleForEmail(sale);
    setEmailModalOpen(true);
  }

  // GET /sale/{id} — fetch single sale detail
  async function viewSaleDetail(id: number) {
    try {
      const res = await generica({ metodo: "GET", uri: `/sale/${id}` });
      setDetailSale(res?.data || null);
    } catch {
      toast.error("Erro ao carregar detalhe da promoção");
    }
  }

  // GET /sale/coupon/{coupon} — search sale by coupon code
  async function searchByCoupon() {
    if (!couponSearch.trim()) { toast.error("Digite um cupom"); return; }
    setSearchingCoupon(true);
    setCouponResult(null);
    try {
      const res = await generica({ metodo: "GET", uri: `/sale/coupon/${couponSearch.trim()}` });
      if (res?.data) {
        setCouponResult(res.data);
      } else {
        toast.info("Cupom não encontrado");
      }
    } catch {
      toast.error("Cupom não encontrado");
    } finally {
      setSearchingCoupon(false);
    }
  }

  async function handleSubmit(e: React.FormEvent) {
    e.preventDefault();
    if (!form.name) { toast.error("Preencha o nome da promoção"); return; }
    if (!form.totalPrice || form.totalPrice <= 0) { toast.error("O preço deve ser maior que zero"); return; }
    setSaving(true);
    // Convert empty strings to null/undefined for backend validation
    const payload: Record<string, any> = {
      name: form.name,
      totalPrice: form.totalPrice,
    };
    if (form.startDate) payload.startDate = form.startDate;
    if (form.endDate) payload.endDate = form.endDate;
    if (form.coupon && form.coupon.trim()) payload.coupon = form.coupon.trim();
    try {
      if (editingId) {
        const res = await generica({ metodo: "PUT", uri: `/sale/${editingId}`, data: payload });
        if (res?.status === 200) { toast.success("Promoção atualizada!"); setModalOpen(false); loadSales(); }
        else toast.error(res?.data?.message || "Erro ao atualizar promoção");
      } else {
        if (!form.endDate) { toast.error("Data fim é obrigatória"); setSaving(false); return; }
        const res = await generica({ metodo: "POST", uri: "/sale", data: payload });
        if (res?.status === 200 || res?.status === 201) { toast.success("Promoção cadastrada!"); setModalOpen(false); loadSales(); }
        else toast.error(res?.data?.message || "Erro ao cadastrar promoção");
      }
    } catch { toast.error("Erro ao salvar promoção"); }
    finally { setSaving(false); }
  }

  async function handleDelete(id: number) {
    const result = await Swal.fire({
      title: "Tem certeza?",
      text: "Deseja realmente excluir esta promoção?",
      icon: "warning",
      showCancelButton: true,
      confirmButtonColor: "#E94560",
      cancelButtonColor: "#6c757d",
      confirmButtonText: "Sim, excluir!",
      cancelButtonText: "Cancelar",
    });
    if (!result.isConfirmed) return;
    try {
      const res = await generica({ metodo: "DELETE", uri: `/sale/${id}` });
      if (res?.status === 200 || res?.status === 204) { toast.success("Promoção excluída!"); loadSales(); }
      else toast.error("Erro ao excluir promoção");
    } catch { toast.error("Erro ao excluir promoção"); }
  }

  async function handleSendEmail() {
    if (!selectedSaleForEmail) return;
    setSendingEmail(true);
    try {
      const res = await generica({
        metodo: "POST",
        uri: `/sale/email/notify?idSale=${selectedSaleForEmail.id}`,
      });
      if (res?.status === 200 || res?.status === 201 || res?.status === 202) {
        toast.success("Promoção enviada por email para os clientes!");
        setEmailModalOpen(false);
      } else {
        toast.error(res?.data?.message || "Erro ao enviar email");
      }
    } catch {
      toast.error("Erro ao enviar email de promoção");
    } finally {
      setSendingEmail(false);
    }
  }

  return (
    <GoBarberLayout>
      <div className="space-y-6">
        <div className="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4">
          <h1 className="text-2xl font-bold text-[#1A1A2E]">Promoções & Cupons</h1>
          <button onClick={openCreate} className="gobarber-btn-primary flex items-center gap-2">
            <FaPlus /> Nova Promoção
          </button>
        </div>

        {/* Busca por cupom */}
        <div className="gobarber-card">
          <h3 className="text-sm font-semibold text-gray-700 mb-2 flex items-center gap-2"><FaSearch /> Buscar por Cupom</h3>
          <div className="flex gap-2">
            <input
              type="text"
              value={couponSearch}
              onChange={(e) => setCouponSearch(e.target.value.toUpperCase())}
              className="gobarber-input flex-1"
              placeholder="Digite o código do cupom..."
              onKeyDown={(e) => e.key === "Enter" && searchByCoupon()}
            />
            <button onClick={searchByCoupon} disabled={searchingCoupon} className="gobarber-btn-primary px-4">
              {searchingCoupon ? "..." : "Buscar"}
            </button>
          </div>
          {couponResult && (
            <div className="mt-3 p-3 bg-green-50 border border-green-200 rounded-lg">
              <div className="flex items-center justify-between">
                <div>
                  <p className="font-semibold text-[#1A1A2E]">{couponResult.name || couponResult.title}</p>
                  <p className="text-sm text-gray-500">{couponResult.description}</p>
                  {couponResult.coupon && <code className="bg-white px-2 py-0.5 rounded text-xs font-mono text-[#E94560]">{couponResult.coupon}</code>}
                </div>
                {couponResult.totalPrice != null && couponResult.totalPrice > 0 && (
                  <span className="text-lg font-bold text-[#E94560]">R$ {couponResult.totalPrice.toFixed(2)}</span>
                )}
              </div>
            </div>
          )}
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
            <div className="col-span-full gobarber-card text-center py-12 text-gray-400">Nenhuma promoção cadastrada</div>
          ) : (
            sales.map((sale) => (
              <div key={sale.id} className="gobarber-card hover:border-[#E94560]/30">
                <div className="flex items-start justify-between mb-3">
                  <div className="min-w-0">
                    <h3 className="font-semibold text-[#1A1A2E]">{sale.name || sale.title || "Promoção"}</h3>
                    {sale.description && <p className="text-sm text-gray-500 mt-1">{sale.description}</p>}
                  </div>
                  {sale.totalPrice != null && sale.totalPrice > 0 && (
                    <span className="text-xl font-bold text-[#E94560] shrink-0 ml-2">R$ {sale.totalPrice.toFixed(2)}</span>
                  )}
                  {sale.discountPercentage != null && sale.discountPercentage > 0 && (!sale.totalPrice || sale.totalPrice === 0) && (
                    <span className="text-2xl font-bold text-[#E94560] shrink-0 ml-2">{sale.discountPercentage}%</span>
                  )}
                </div>
                {sale.coupon && (
                  <div className="flex items-center gap-2 mb-3">
                    <FaTag className="text-[#E94560]" />
                    <code className="bg-gray-100 px-3 py-1 rounded text-sm font-mono">{sale.coupon}</code>
                  </div>
                )}
                <div className="text-xs text-gray-400 mb-3">
                  {sale.startDate && <>De {new Date(sale.startDate).toLocaleDateString("pt-BR")}</>}
                  {sale.endDate && <> até {new Date(sale.endDate).toLocaleDateString("pt-BR")}</>}
                </div>
                <div className="flex items-center justify-between pt-3 border-t border-gray-100">
                  <span className={`px-2 py-1 text-xs rounded-full ${isSaleActive(sale) ? "bg-green-100 text-green-700" : "bg-gray-100 text-gray-500"}`}>
                    {isSaleActive(sale) ? "Ativa" : "Inativa"}
                  </span>
                  <div className="flex gap-2">
                    <button onClick={() => viewSaleDetail(sale.id)} className="p-1.5 text-blue-600 hover:bg-blue-50 rounded" title="Detalhes"><FaEye /></button>
                    <button
                      onClick={() => openEmailModal(sale)}
                      className="p-1.5 text-blue-600 hover:bg-blue-50 rounded"
                      title="Enviar por Email"
                    >
                      <FaEnvelope />
                    </button>
                    <button onClick={() => openEdit(sale)} className="p-1.5 text-yellow-600 hover:bg-yellow-50 rounded" title="Editar"><FaEdit /></button>
                    <button onClick={() => handleDelete(sale.id)} className="p-1.5 text-red-600 hover:bg-red-50 rounded" title="Excluir"><FaTrash /></button>
                  </div>
                </div>
              </div>
            ))
          )}
        </div>
      </div>

      {/* Modal Criar/Editar */}
      <Modal isOpen={modalOpen} onClose={() => setModalOpen(false)} title={editingId ? "Editar Promoção" : "Nova Promoção"}>
        <form onSubmit={handleSubmit} className="space-y-4">
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">Nome *</label>
            <input type="text" value={form.name} onChange={(e) => setForm({ ...form, name: e.target.value })} className="gobarber-input" required />
          </div>
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">Preço / Desconto (R$) *</label>
            <input type="number" step="0.01" min="0.01" value={form.totalPrice} onChange={(e) => setForm({ ...form, totalPrice: parseFloat(e.target.value) || 0 })} className="gobarber-input" required />
          </div>
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">Cupom <span className="text-xs text-gray-400">(7 caracteres)</span></label>
            <input type="text" value={form.coupon} onChange={(e) => setForm({ ...form, coupon: e.target.value.toUpperCase() })} className="gobarber-input" placeholder="Ex: PROMO10" maxLength={7} />
          </div>
          <div className="grid grid-cols-2 gap-4">
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">Data Início</label>
              <input type="date" value={form.startDate} onChange={(e) => setForm({ ...form, startDate: e.target.value })} className="gobarber-input" />
            </div>
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">Data Fim *</label>
              <input type="date" value={form.endDate} onChange={(e) => setForm({ ...form, endDate: e.target.value })} className="gobarber-input" required />
            </div>
          </div>
          <div className="flex justify-end gap-3 pt-4 border-t">
            <button type="button" onClick={() => setModalOpen(false)} className="px-4 py-2 text-gray-600 hover:bg-gray-100 rounded-lg">Cancelar</button>
            <button type="submit" disabled={saving} className="gobarber-btn-primary">{saving ? "Salvando..." : editingId ? "Atualizar" : "Cadastrar"}</button>
          </div>
        </form>
      </Modal>

      {/* Modal Enviar Email */}
      <Modal isOpen={emailModalOpen} onClose={() => setEmailModalOpen(false)} title="Enviar Promoção por Email">
        <div className="space-y-5">
          <div className="bg-blue-50 border border-blue-200 rounded-lg p-4">
            <div className="flex items-center gap-3 mb-2">
              <FaEnvelope className="text-blue-500 text-xl" />
              <h4 className="font-semibold text-[#1A1A2E]">
                {selectedSaleForEmail?.name || selectedSaleForEmail?.title || "Promoção"}
              </h4>
            </div>
            {selectedSaleForEmail?.description && (
              <p className="text-sm text-gray-600 mb-2">{selectedSaleForEmail.description}</p>
            )}
            {selectedSaleForEmail?.coupon && (
              <p className="text-sm">
                Cupom: <code className="bg-white px-2 py-0.5 rounded font-mono text-[#E94560]">{selectedSaleForEmail.coupon}</code>
              </p>
            )}
            {selectedSaleForEmail?.totalPrice != null && selectedSaleForEmail.totalPrice > 0 && (
              <p className="text-sm mt-1 font-semibold text-[#E94560]">
                R$ {selectedSaleForEmail.totalPrice.toFixed(2)}
              </p>
            )}
          </div>

          <div className="text-sm text-gray-600">
            <p>Esta promoção será enviada por email para <span className="font-semibold">todos os clientes cadastrados</span> que aceitam receber promoções.</p>
          </div>

          <div className="flex justify-end gap-3 pt-4 border-t">
            <button
              type="button"
              onClick={() => setEmailModalOpen(false)}
              className="px-4 py-2 text-gray-600 hover:bg-gray-100 rounded-lg"
            >
              Cancelar
            </button>
            <button
              onClick={handleSendEmail}
              disabled={sendingEmail}
              className="gobarber-btn-primary flex items-center gap-2"
            >
              {sendingEmail ? (
                <>
                  <span className="animate-spin rounded-full h-4 w-4 border-b-2 border-white" />
                  Enviando...
                </>
              ) : (
                <>
                  <FaPaperPlane /> Enviar Email
                </>
              )}
            </button>
          </div>
        </div>
      </Modal>

      {/* Modal Detalhe Promoção (GET /sale/{id}) */}
      <Modal isOpen={!!detailSale} onClose={() => setDetailSale(null)} title={detailSale?.name || detailSale?.title || "Promoção"}>
        {detailSale && (
          <div className="space-y-4">
            <div className="flex items-center gap-3">
              <div className="w-12 h-12 rounded-full bg-gradient-to-r from-[#E94560] to-[#0F3460] flex items-center justify-center text-white">
                <FaTag />
              </div>
              <div>
                <h3 className="font-semibold text-lg text-[#1A1A2E]">{detailSale.name || detailSale.title}</h3>
                <span className={`px-2 py-0.5 text-xs rounded-full ${isSaleActive(detailSale) ? "bg-green-100 text-green-700" : "bg-gray-100 text-gray-500"}`}>
                  {isSaleActive(detailSale) ? "Ativa" : "Inativa"}
                </span>
              </div>
            </div>
            {detailSale.description && <p className="text-gray-600">{detailSale.description}</p>}
            <div className="grid grid-cols-2 gap-3">
              {detailSale.totalPrice != null && detailSale.totalPrice > 0 && (
                <div className="bg-gray-50 p-3 rounded-lg">
                  <p className="text-xs text-gray-500">Preço</p>
                  <p className="text-lg font-bold text-[#E94560]">R$ {detailSale.totalPrice.toFixed(2)}</p>
                </div>
              )}
              {detailSale.discountPercentage != null && detailSale.discountPercentage > 0 && (
                <div className="bg-gray-50 p-3 rounded-lg">
                  <p className="text-xs text-gray-500">Desconto</p>
                  <p className="text-lg font-bold text-green-600">{detailSale.discountPercentage}%</p>
                </div>
              )}
              {detailSale.coupon && (
                <div className="bg-gray-50 p-3 rounded-lg">
                  <p className="text-xs text-gray-500">Cupom</p>
                  <code className="text-sm font-mono text-[#E94560]">{detailSale.coupon}</code>
                </div>
              )}
              <div className="bg-gray-50 p-3 rounded-lg">
                <p className="text-xs text-gray-500">Período</p>
                <p className="text-sm">
                  {detailSale.startDate ? new Date(detailSale.startDate).toLocaleDateString("pt-BR") : "—"}
                  {" "}→{" "}
                  {detailSale.endDate ? new Date(detailSale.endDate).toLocaleDateString("pt-BR") : "—"}
                </p>
              </div>
            </div>
          </div>
        )}
      </Modal>
    </GoBarberLayout>
  );
}
