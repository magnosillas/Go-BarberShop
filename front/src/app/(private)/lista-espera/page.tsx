"use client";

import GoBarberLayout from "@/components/Layout/GoBarberLayout";
import Modal from "@/components/Modal/Modal";
import React, { useEffect, useState } from "react";
import { generica } from "@/api/api";
import { toast } from "react-toastify";
import {
  FaPlus,
  FaClock,
  FaCheckCircle,
  FaTimesCircle,
  FaUser,
  FaCut,
  FaTrash,
  FaEye,
  FaBell,
  FaExchangeAlt,
  FaChartBar,
  FaStickyNote,
  FaArrowUp,
  FaFilter,
  FaBroom,
} from "react-icons/fa";
import Swal from "sweetalert2";

interface WaitItem {
  idWaitList: number;
  clientName?: string;
  client?: { idClient?: number; name?: string };
  barberName?: string;
  barber?: { idBarber?: number; name?: string };
  serviceName?: string;
  service?: { id?: number; name?: string };
  status?: string;
  position?: number;
  priority?: string;
  notes?: string;
  preferredDate?: string;
  desiredTime?: string;
  desiredDuration?: number;
  createdAt?: string;
  updatedAt?: string;
  estimatedTime?: string;
  notified?: boolean;
  notifiedAt?: string;
  expirationTime?: string;
}

const priorityLabel: Record<string, string> = { LOW: "Baixa", NORMAL: "Normal", HIGH: "Alta", URGENT: "Urgente" };

function formatDateTime(iso?: string): string {
  if (!iso) return "—";
  try {
    const d = new Date(iso);
    return d.toLocaleDateString("pt-BR", { day: "2-digit", month: "2-digit", year: "numeric", hour: "2-digit", minute: "2-digit" });
  } catch { return iso; }
}

interface Barber { idBarber: number; name?: string; }
interface Service { id: number; name?: string; }
interface Client { idClient: number; name?: string; }

const initialForm = { clientId: "", barberId: "", serviceId: "", preferredDate: "", priority: "NORMAL", notes: "" };

export default function ListaEsperaPage() {
  const [items, setItems] = useState<WaitItem[]>([]);
  const [clients, setClients] = useState<Client[]>([]);
  const [barbers, setBarbers] = useState<Barber[]>([]);
  const [services, setServices] = useState<Service[]>([]);
  const [loading, setLoading] = useState(true);
  const [modalOpen, setModalOpen] = useState(false);
  const [saving, setSaving] = useState(false);
  const [form, setForm] = useState(initialForm);
  const [detailModal, setDetailModal] = useState<WaitItem | null>(null);
  const [stats, setStats] = useState<any>(null);
  const [filterBarber, setFilterBarber] = useState("");
  const [filterStatus, setFilterStatus] = useState("");
  const [editNotesId, setEditNotesId] = useState<number | null>(null);
  const [editNotesText, setEditNotesText] = useState("");

  useEffect(() => { loadWaitList(); loadClients(); loadBarbers(); loadServices(); loadStats(); }, []);

  async function loadWaitList() {
    setLoading(true);
    try {
      const response = await generica({ metodo: "GET", uri: "/waitlist", params: { page: 0, size: 100 } });
      const data = response?.data?.content || response?.data || [];
      setItems(Array.isArray(data) ? data : []);
    } catch { toast.error("Erro ao carregar lista de espera"); }
    finally { setLoading(false); }
  }

  async function loadClients() {
    try {
      const res = await generica({ metodo: "GET", uri: "/client", params: { page: 0, size: 100 } });
      const data = res?.data?.content || res?.data || [];
      setClients(Array.isArray(data) ? data : []);
    } catch { /* silencioso */ }
  }

  async function loadBarbers() {
    try {
      const res = await generica({ metodo: "GET", uri: "/barber", params: { page: 0, size: 100 } });
      const data = res?.data?.content || res?.data || [];
      setBarbers(Array.isArray(data) ? data : []);
    } catch { /* silencioso */ }
  }

  async function loadServices() {
    try {
      const res = await generica({ metodo: "GET", uri: "/services", params: { page: 0, size: 100 } });
      const data = res?.data?.content || res?.data || [];
      setServices(Array.isArray(data) ? data : []);
    } catch { /* silencioso */ }
  }

  function openCreate() { setForm(initialForm); setModalOpen(true); }

  async function handleSubmit(e: React.FormEvent) {
    e.preventDefault();
    if (!form.clientId) { toast.error("Selecione um cliente"); return; }
    setSaving(true);
    try {
      // Map string priorities to integers the backend expects
      const priorityMap: Record<string, number> = { LOW: 1, NORMAL: 0, HIGH: 2, URGENT: 3 };
      const params: Record<string, string | number> = { clientId: parseInt(form.clientId) };
      if (form.barberId) params.barberId = parseInt(form.barberId);
      if (form.serviceId) params.serviceId = parseInt(form.serviceId);
      if (form.preferredDate) params.preferredDate = form.preferredDate;
      if (form.priority) params.priority = priorityMap[form.priority] ?? 0;
      if (form.notes) params.notes = form.notes;

      const res = await generica({ metodo: "POST", uri: "/waitlist", params });
      if (res?.status === 200 || res?.status === 201) { toast.success("Adicionado à lista de espera!"); setModalOpen(false); loadWaitList(); }
      else toast.error(res?.data?.message || "Erro ao adicionar à lista");
    } catch { toast.error("Erro ao adicionar à lista de espera"); }
    finally { setSaving(false); }
  }

  async function handleRemove(id: number) {
    const result = await Swal.fire({
      title: "Remover da lista?",
      text: "Deseja remover este item da lista de espera?",
      icon: "warning",
      showCancelButton: true,
      confirmButtonColor: "#E94560",
      cancelButtonColor: "#6c757d",
      confirmButtonText: "Sim, remover!",
      cancelButtonText: "Cancelar",
    });
    if (!result.isConfirmed) return;
    try {
      const res = await generica({ metodo: "DELETE", uri: `/waitlist/${id}` });
      if (res?.status === 200 || res?.status === 204) { toast.success("Removido da lista!"); loadWaitList(); }
      else toast.error("Erro ao remover");
    } catch { toast.error("Erro ao remover da lista"); }
  }

  async function viewDetail(id: number) {
    try {
      const res = await generica({ metodo: "GET", uri: `/waitlist/${id}` });
      if (res?.data) setDetailModal(res.data);
    } catch { toast.error("Erro ao carregar detalhes"); }
  }

  async function loadByBarber(barberId: number) {
    setLoading(true);
    try {
      const res = await generica({ metodo: "GET", uri: `/waitlist/barber/${barberId}`, params: { page: 0, size: 100 } });
      const data = res?.data?.content || res?.data || [];
      setItems(Array.isArray(data) ? data : []);
    } catch { toast.error("Erro ao filtrar por barbeiro"); }
    finally { setLoading(false); }
  }

  async function loadWaitingByBarber(barberId: number) {
    setLoading(true);
    try {
      const res = await generica({ metodo: "GET", uri: `/waitlist/barber/${barberId}/waiting`, params: { page: 0, size: 100 } });
      const data = res?.data?.content || res?.data || [];
      setItems(Array.isArray(data) ? data : []);
    } catch { loadByBarber(barberId); }
    finally { setLoading(false); }
  }

  async function loadByClient(clientId: number) {
    try {
      const res = await generica({ metodo: "GET", uri: `/waitlist/client/${clientId}`, params: { page: 0, size: 100 } });
      const data = res?.data?.content || res?.data || [];
      setItems(Array.isArray(data) ? data : []);
    } catch { /* silencioso */ }
  }

  async function loadByService(serviceId: number) {
    try {
      const res = await generica({ metodo: "GET", uri: `/waitlist/service/${serviceId}`, params: { page: 0, size: 100 } });
      const data = res?.data?.content || res?.data || [];
      setItems(Array.isArray(data) ? data : []);
    } catch { /* silencioso */ }
  }

  async function handleNotify(id: number) {
    try {
      const res = await generica({ metodo: "POST", uri: `/waitlist/${id}/notify` });
      if (res?.status === 200) toast.success("Cliente notificado!");
      else toast.error("Erro ao notificar");
    } catch { toast.error("Erro ao notificar cliente"); }
  }

  async function handleConvert(id: number) {
    const result = await Swal.fire({
      title: "Converter para agendamento?",
      text: "Este item será convertido em um agendamento.",
      icon: "question",
      showCancelButton: true,
      confirmButtonColor: "#E94560",
      cancelButtonColor: "#6c757d",
      confirmButtonText: "Sim, converter!",
      cancelButtonText: "Cancelar",
    });
    if (!result.isConfirmed) return;
    try {
      const res = await generica({ metodo: "POST", uri: `/waitlist/${id}/convert` });
      if (res?.status === 200 || res?.status === 201) { toast.success("Convertido para agendamento!"); loadWaitList(); }
      else toast.error(res?.data?.message || "Erro ao converter");
    } catch { toast.error("Erro ao converter"); }
  }

  async function handleUpdatePriority(id: number, priority: string) {
    const priorityMap: Record<string, number> = { LOW: 1, NORMAL: 0, HIGH: 2, URGENT: 3 };
    try {
      const res = await generica({ metodo: "PUT", uri: `/waitlist/${id}/priority`, params: { priority: priorityMap[priority] ?? 0 } });
      if (res?.status === 200) { toast.success("Prioridade atualizada!"); loadWaitList(); }
      else toast.error("Erro ao atualizar prioridade");
    } catch { toast.error("Erro ao atualizar prioridade"); }
  }

  async function handleUpdateNotes(id: number) {
    try {
      const res = await generica({ metodo: "PUT", uri: `/waitlist/${id}/notes`, params: { notes: editNotesText } });
      if (res?.status === 200) { toast.success("Observações atualizadas!"); setEditNotesId(null); loadWaitList(); }
      else toast.error("Erro ao atualizar");
    } catch { toast.error("Erro ao atualizar observações"); }
  }

  async function handleProcessExpired() {
    try {
      const res = await generica({ metodo: "POST", uri: "/waitlist/process-expired" });
      if (res?.status === 200) { toast.success("Expirados processados!"); loadWaitList(); loadStats(); }
      else toast.error("Erro ao processar");
    } catch { toast.error("Erro ao processar expirados"); }
  }

  async function loadStats() {
    try {
      const res = await generica({ metodo: "GET", uri: "/waitlist/stats" });
      if (res?.data) setStats(res.data);
    } catch { /* silencioso */ }
  }

  async function loadBarberStats(barberId: number) {
    try {
      const res = await generica({ metodo: "GET", uri: `/waitlist/stats/barber/${barberId}` });
      if (res?.data) setStats(res.data);
    } catch { /* silencioso */ }
  }

  function getStatusStyle(status?: string) {
    switch (status?.toUpperCase()) {
      case "WAITING": return { bg: "bg-yellow-100", text: "text-yellow-700", label: "Aguardando", icon: <FaClock /> };
      case "IN_SERVICE": return { bg: "bg-blue-100", text: "text-blue-700", label: "Atendendo", icon: <FaCut /> };
      case "COMPLETED": return { bg: "bg-green-100", text: "text-green-700", label: "Concluído", icon: <FaCheckCircle /> };
      case "CANCELLED": return { bg: "bg-red-100", text: "text-red-700", label: "Cancelado", icon: <FaTimesCircle /> };
      default: return { bg: "bg-gray-100", text: "text-gray-600", label: status || "—", icon: <FaClock /> };
    }
  }

  return (
    <GoBarberLayout>
      <div className="space-y-6">
        <div className="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4">
          <h1 className="text-2xl font-bold text-[#1A1A2E]">Lista de Espera</h1>
          <button onClick={openCreate} className="gobarber-btn-primary flex items-center gap-2">
            <FaPlus /> Adicionar à Fila
          </button>
          <button onClick={handleProcessExpired} className="px-4 py-2 bg-orange-100 text-orange-700 rounded-lg hover:bg-orange-200 flex items-center gap-2 text-sm font-medium">
            <FaBroom /> Processar Expirados
          </button>
        </div>

        {/* Stats */}
        {stats && (
          <div className="gobarber-card bg-gradient-to-r from-[#1A1A2E]/5 to-[#E94560]/5 p-4">
            <h3 className="text-sm font-semibold text-gray-600 mb-2 flex items-center gap-2"><FaChartBar /> Estatísticas</h3>
            <div className="grid grid-cols-2 md:grid-cols-4 gap-3 text-center">
              <div>
                <p className="text-lg font-bold text-[#1A1A2E]">{stats.totalWaiting ?? 0}</p>
                <p className="text-xs text-gray-500">Total Aguardando</p>
              </div>
              {Array.isArray(stats.byBarber) && stats.byBarber.length > 0 ? (
                stats.byBarber.map((entry: any) => {
                  const barberName = Array.isArray(entry) ? entry[1] : entry.barberName || "Barbeiro";
                  const count = Array.isArray(entry) ? entry[2] : entry.count || 0;
                  return (
                    <div key={Array.isArray(entry) ? entry[0] : barberName}>
                      <p className="text-lg font-bold text-[#1A1A2E]">{count}</p>
                      <p className="text-xs text-gray-500">{barberName}</p>
                    </div>
                  );
                })
              ) : (
                <div>
                  <p className="text-lg font-bold text-gray-300">0</p>
                  <p className="text-xs text-gray-500">Por Barbeiro</p>
                </div>
              )}
            </div>
          </div>
        )}

        {/* Filtros */}
        <div className="flex flex-wrap gap-3 items-center">
          <span className="text-sm text-gray-600 flex items-center gap-1"><FaFilter /> Filtrar:</span>
          <select
            value={filterBarber}
            onChange={(e) => {
              const v = e.target.value;
              setFilterBarber(v);
              if (!v) { loadWaitList(); loadStats(); }
              else {
                const bid = parseInt(v);
                if (filterStatus === "WAITING") loadWaitingByBarber(bid);
                else loadByBarber(bid);
                loadBarberStats(bid);
              }
            }}
            className="gobarber-input w-auto text-sm"
          >
            <option value="">Todos barbeiros</option>
            {barbers.map(b => <option key={b.idBarber} value={b.idBarber}>{b.name || `Barbeiro #${b.idBarber}`}</option>)}
          </select>
          <select
            value={filterStatus}
            onChange={(e) => {
              setFilterStatus(e.target.value);
              // refetch based on barber + status
              if (filterBarber && e.target.value === "WAITING") loadWaitingByBarber(parseInt(filterBarber));
              else if (filterBarber) loadByBarber(parseInt(filterBarber));
              else loadWaitList();
            }}
            className="gobarber-input w-auto text-sm"
          >
            <option value="">Todos status</option>
            <option value="WAITING">Aguardando</option>
            <option value="IN_SERVICE">Atendendo</option>
            <option value="COMPLETED">Concluído</option>
            <option value="CANCELLED">Cancelado</option>
          </select>
        </div>

        {/* Resumo */}
        <div className="grid grid-cols-2 md:grid-cols-4 gap-4">
          {[
            { label: "Na fila", value: items.filter(i => i.status?.toUpperCase() === "WAITING").length, color: "text-yellow-600" },
            { label: "Atendendo", value: items.filter(i => i.status?.toUpperCase() === "IN_SERVICE").length, color: "text-blue-600" },
            { label: "Concluídos", value: items.filter(i => i.status?.toUpperCase() === "COMPLETED").length, color: "text-green-600" },
            { label: "Total", value: items.length, color: "text-[#1A1A2E]" },
          ].map((stat) => (
            <div key={stat.label} className="gobarber-card text-center">
              <p className={`text-2xl font-bold ${stat.color}`}>{stat.value}</p>
              <p className="text-xs text-gray-500 mt-1">{stat.label}</p>
            </div>
          ))}
        </div>

        {/* Lista */}
        <div className="space-y-3">
          {loading ? (
            Array.from({ length: 5 }).map((_, i) => (
              <div key={i} className="gobarber-card animate-pulse flex items-center gap-4">
                <div className="w-10 h-10 bg-gray-200 rounded-full" />
                <div className="flex-1">
                  <div className="h-4 bg-gray-200 rounded w-1/3 mb-2" />
                  <div className="h-3 bg-gray-200 rounded w-1/4" />
                </div>
              </div>
            ))
          ) : items.length === 0 ? (
            <div className="gobarber-card text-center py-12 text-gray-400">Lista de espera vazia</div>
          ) : (
            items.map((item, idx) => {
              const st = getStatusStyle(item.status);
              return (
                <div key={item.idWaitList} className="gobarber-card flex items-center gap-4 hover:border-[#E94560]/30 relative">
                  <div className="w-10 h-10 rounded-full bg-[#1A1A2E] text-white flex items-center justify-center font-bold text-sm">
                    {item.position ?? idx + 1}
                  </div>
                  <div className="flex-1 min-w-0">
                    <div className="flex items-center gap-2">
                      <FaUser className="text-gray-400 text-xs" />
                      <span className="font-medium text-[#1A1A2E] truncate">{item.clientName || item.client?.name || "Cliente"}</span>
                    </div>
                    <p className="text-sm text-gray-500">
                      <FaCut className="inline text-xs mr-1" />{item.barberName || item.barber?.name || "Qualquer barbeiro"}
                      {(item.desiredTime || item.preferredDate) && <span className="ml-2 text-gray-400">• {formatDateTime(item.desiredTime || item.preferredDate)}</span>}
                    </p>
                  </div>
                  <span className={`inline-flex items-center gap-1.5 px-3 py-1 rounded-full text-xs font-medium ${st.bg} ${st.text}`}>
                    {st.icon} {st.label}
                  </span>
                  <button onClick={() => viewDetail(item.idWaitList)} className="p-1.5 text-blue-600 hover:bg-blue-50 rounded" title="Detalhes"><FaEye /></button>
                  {item.status?.toUpperCase() === "WAITING" && (
                    <>
                      <button onClick={() => handleNotify(item.idWaitList)} className="p-1.5 text-purple-600 hover:bg-purple-50 rounded" title="Notificar cliente"><FaBell /></button>
                      <button onClick={() => handleConvert(item.idWaitList)} className="p-1.5 text-green-600 hover:bg-green-50 rounded" title="Converter em agendamento"><FaExchangeAlt /></button>
                      <select
                        value={item.priority || "NORMAL"}
                        onChange={(e) => handleUpdatePriority(item.idWaitList, e.target.value)}
                        className="text-xs border rounded px-1 py-0.5"
                        title="Prioridade"
                      >
                        <option value="LOW">Baixa</option>
                        <option value="NORMAL">Normal</option>
                        <option value="HIGH">Alta</option>
                        <option value="URGENT">Urgente</option>
                      </select>
                      <button onClick={() => { setEditNotesId(item.idWaitList); setEditNotesText(item.notes || ""); }} className="p-1.5 text-gray-600 hover:bg-gray-50 rounded" title="Editar observações"><FaStickyNote /></button>
                    </>
                  )}
                  <button onClick={() => handleRemove(item.idWaitList)} className="p-1.5 text-red-600 hover:bg-red-50 rounded" title="Remover">
                    <FaTrash />
                  </button>
                  {editNotesId === item.idWaitList && (
                    <div className="absolute right-0 top-full mt-1 bg-white border rounded-lg shadow-lg p-3 z-10 w-64">
                      <textarea value={editNotesText} onChange={(e) => setEditNotesText(e.target.value)} className="gobarber-input text-sm" rows={2} placeholder="Observações..." />
                      <div className="flex gap-2 mt-2">
                        <button onClick={() => handleUpdateNotes(item.idWaitList)} className="text-xs bg-[#1A1A2E] text-white px-3 py-1 rounded">Salvar</button>
                        <button onClick={() => setEditNotesId(null)} className="text-xs bg-gray-100 px-3 py-1 rounded">Cancelar</button>
                      </div>
                    </div>
                  )}
                </div>
              );
            })
          )}
        </div>
      </div>

      {/* Modal Detalhes */}
      <Modal isOpen={!!detailModal} onClose={() => setDetailModal(null)} title="Detalhes - Lista de Espera">
        {detailModal && (
          <div className="space-y-3">
            <div className="grid grid-cols-2 gap-3">
              <div><span className="text-xs text-gray-500">Cliente</span><p className="font-medium">{detailModal.clientName || detailModal.client?.name || "—"}</p></div>
              <div><span className="text-xs text-gray-500">Barbeiro</span><p className="font-medium">{detailModal.barberName || detailModal.barber?.name || "Qualquer barbeiro"}</p></div>
              <div><span className="text-xs text-gray-500">Status</span><p>{(() => { const s = getStatusStyle(detailModal.status); return <span className={`inline-flex items-center gap-1 px-2 py-0.5 rounded-full text-xs ${s.bg} ${s.text}`}>{s.icon} {s.label}</span>; })()}</p></div>
              <div><span className="text-xs text-gray-500">Posição</span><p className="font-medium">#{detailModal.position || "—"}</p></div>
              <div><span className="text-xs text-gray-500">Prioridade</span><p className="font-medium">{priorityLabel[detailModal.priority || "NORMAL"] || detailModal.priority}</p></div>
              {detailModal.desiredDuration && <div><span className="text-xs text-gray-500">Duração Estimada</span><p className="font-medium">{detailModal.desiredDuration} min</p></div>}
              {(detailModal.preferredDate || detailModal.desiredTime) && <div><span className="text-xs text-gray-500">Data Preferida</span><p className="font-medium">{formatDateTime(detailModal.preferredDate || detailModal.desiredTime)}</p></div>}
              {detailModal.createdAt && <div><span className="text-xs text-gray-500">Criado em</span><p className="font-medium">{formatDateTime(detailModal.createdAt)}</p></div>}
              {detailModal.notifiedAt && <div><span className="text-xs text-gray-500">Notificado em</span><p className="font-medium">{formatDateTime(detailModal.notifiedAt)}</p></div>}
              {detailModal.expirationTime && <div><span className="text-xs text-gray-500">Expira em</span><p className="font-medium">{formatDateTime(detailModal.expirationTime)}</p></div>}
            </div>
            {detailModal.notes && <div><span className="text-xs text-gray-500">Observações</span><p className="text-sm bg-gray-50 rounded p-2 mt-1">{detailModal.notes}</p></div>}
            {detailModal.status?.toUpperCase() === "WAITING" && (
              <div className="flex gap-3 pt-3 border-t">
                <button onClick={() => { handleNotify(detailModal.idWaitList); setDetailModal(null); }} className="flex-1 px-3 py-2 bg-purple-100 text-purple-700 rounded-lg text-sm font-medium flex items-center justify-center gap-2"><FaBell /> Notificar</button>
                <button onClick={() => { handleConvert(detailModal.idWaitList); setDetailModal(null); }} className="flex-1 px-3 py-2 bg-green-100 text-green-700 rounded-lg text-sm font-medium flex items-center justify-center gap-2"><FaExchangeAlt /> Converter</button>
              </div>
            )}
          </div>
        )}
      </Modal>

      <Modal isOpen={modalOpen} onClose={() => setModalOpen(false)} title="Adicionar à Lista de Espera">
        <form onSubmit={handleSubmit} className="space-y-4">
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">Cliente *</label>
            <select value={form.clientId} onChange={(e) => setForm({ ...form, clientId: e.target.value })} className="gobarber-input" required>
              <option value="">Selecione um cliente</option>
              {clients.map(c => <option key={c.idClient} value={c.idClient}>{c.name || `Cliente #${c.idClient}`}</option>)}
            </select>
          </div>
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">Barbeiro (opcional)</label>
            <select value={form.barberId} onChange={(e) => setForm({ ...form, barberId: e.target.value })} className="gobarber-input">
              <option value="">Qualquer barbeiro</option>
              {barbers.map(b => <option key={b.idBarber} value={b.idBarber}>{b.name || `Barbeiro #${b.idBarber}`}</option>)}
            </select>
          </div>
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">Serviço (opcional)</label>
            <select value={form.serviceId} onChange={(e) => setForm({ ...form, serviceId: e.target.value })} className="gobarber-input">
              <option value="">Nenhum serviço específico</option>
              {services.map(s => <option key={s.id} value={s.id}>{s.name || `Serviço #${s.id}`}</option>)}
            </select>
          </div>
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">Data preferida</label>
            <input type="datetime-local" value={form.preferredDate} onChange={(e) => setForm({ ...form, preferredDate: e.target.value })} className="gobarber-input" />
          </div>
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">Prioridade</label>
            <select value={form.priority} onChange={(e) => setForm({ ...form, priority: e.target.value })} className="gobarber-input">
              <option value="LOW">Baixa</option>
              <option value="NORMAL">Normal</option>
              <option value="HIGH">Alta</option>
              <option value="URGENT">Urgente</option>
            </select>
          </div>
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">Observações</label>
            <textarea value={form.notes} onChange={(e) => setForm({ ...form, notes: e.target.value })} className="gobarber-input" rows={2} />
          </div>
          <div className="flex justify-end gap-3 pt-4 border-t">
            <button type="button" onClick={() => setModalOpen(false)} className="px-4 py-2 text-gray-600 hover:bg-gray-100 rounded-lg">Cancelar</button>
            <button type="submit" disabled={saving} className="gobarber-btn-primary">{saving ? "Salvando..." : "Adicionar"}</button>
          </div>
        </form>
      </Modal>
    </GoBarberLayout>
  );
}
