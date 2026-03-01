"use client";

import GoBarberLayout from "@/components/Layout/GoBarberLayout";
import Modal from "@/components/Modal/Modal";
import React, { useEffect, useState } from "react";
import { generica } from "@/api/api";
import { toast } from "react-toastify";
import {
  FaPlus,
  FaTrash,
  FaCalendarCheck,
  FaUmbrellaBeach,
  FaCoffee,
  FaBan,
  FaCalendarTimes,
  FaEdit,
  FaPowerOff,
  FaClock,
  FaSearch,
  FaCalendarDay,
} from "react-icons/fa";
import Swal from "sweetalert2";

interface Barber {
  idBarber: number;
  name?: string;
}

interface ScheduleBlock {
  id: number;
  barberId?: number;
  barberName?: string;
  type?: string;
  startDateTime?: string;
  endDateTime?: string;
  reason?: string;
  recurring?: boolean;
  dayOfWeek?: string;
  active?: boolean;
}

type BlockType = "VACATION" | "DAY_OFF" | "LUNCH_BREAK" | "BLOCK";

const blockTypeLabels: Record<BlockType, { label: string; color: string; icon: React.ReactNode }> = {
  VACATION:    { label: "Férias",       color: "bg-blue-100 text-blue-700",   icon: <FaUmbrellaBeach /> },
  DAY_OFF:     { label: "Folga",        color: "bg-green-100 text-green-700", icon: <FaCalendarTimes /> },
  LUNCH_BREAK: { label: "Almoço",       color: "bg-yellow-100 text-yellow-700", icon: <FaCoffee /> },
  BLOCK:       { label: "Bloqueio",      color: "bg-red-100 text-red-700",     icon: <FaBan /> },
};

const daysOfWeek = [
  { value: "MONDAY",    label: "Segunda" },
  { value: "TUESDAY",   label: "Terça" },
  { value: "WEDNESDAY", label: "Quarta" },
  { value: "THURSDAY",  label: "Quinta" },
  { value: "FRIDAY",    label: "Sexta" },
  { value: "SATURDAY",  label: "Sábado" },
  { value: "SUNDAY",    label: "Domingo" },
];

export default function AgendaBarbeiroPage() {
  const [barbers, setBarbers] = useState<Barber[]>([]);
  const [selectedBarber, setSelectedBarber] = useState<number | null>(null);
  const [schedules, setSchedules] = useState<ScheduleBlock[]>([]);
  const [vacations, setVacations] = useState<ScheduleBlock[]>([]);
  const [loading, setLoading] = useState(false);
  const [modalOpen, setModalOpen] = useState(false);
  const [saving, setSaving] = useState(false);
  const [blockType, setBlockType] = useState<BlockType>("BLOCK");
  const [activeTab, setActiveTab] = useState<"blocks" | "vacations" | "recurring" | "availability" | "vacation-days">("blocks");
  const [recurringSchedules, setRecurringSchedules] = useState<ScheduleBlock[]>([]);
  const [availabilitySlots, setAvailabilitySlots] = useState<any[]>([]);
  const [vacationDaysUsed, setVacationDaysUsed] = useState<number | null>(null);
  const [slotDate, setSlotDate] = useState(new Date().toISOString().split("T")[0]);
  const [slotDuration, setSlotDuration] = useState(30);
  const [availableBarbersList, setAvailableBarbersList] = useState<any[]>([]);
  const [checkStart, setCheckStart] = useState("");
  const [checkEnd, setCheckEnd] = useState("");
  const [checkResult, setCheckResult] = useState<boolean | null>(null);
  const [editingSchedule, setEditingSchedule] = useState<ScheduleBlock | null>(null);
  const [editStart, setEditStart] = useState("");
  const [editEnd, setEditEnd] = useState("");
  const [editReason, setEditReason] = useState("");
  const [editModalOpen, setEditModalOpen] = useState(false);

  // Form fields
  const [startDate, setStartDate] = useState("");
  const [endDate, setEndDate] = useState("");
  const [reason, setReason] = useState("");
  const [dayOfWeek, setDayOfWeek] = useState("MONDAY");
  const [startTime, setStartTime] = useState("12:00");
  const [endTime, setEndTime] = useState("13:00");

  useEffect(() => { loadBarbers(); }, []);

  useEffect(() => {
    if (selectedBarber) {
      loadSchedules();
      loadVacations();
      loadRecurring();
      loadVacationDays();
    }
  }, [selectedBarber]);

  async function loadBarbers() {
    try {
      const res = await generica({ metodo: "GET", uri: "/barber", params: { page: 0, size: 100 } });
      const data = res?.data?.content || res?.data || [];
      const list = Array.isArray(data) ? data : [];
      setBarbers(list);
      if (list.length > 0 && !selectedBarber) setSelectedBarber(list[0].idBarber);
    } catch { toast.error("Erro ao carregar barbeiros"); }
  }

  async function loadSchedules() {
    if (!selectedBarber) return;
    setLoading(true);
    try {
      const now = new Date();
      const start = new Date(now.getFullYear(), now.getMonth(), 1).toISOString();
      const end = new Date(now.getFullYear(), now.getMonth() + 3, 0).toISOString();
      const res = await generica({
        metodo: "GET",
        uri: `/barber-schedule/barber/${selectedBarber}`,
        params: { startDate: start, endDate: end },
      });
      setSchedules(Array.isArray(res?.data) ? res.data : []);
    } catch { setSchedules([]); }
    finally { setLoading(false); }
  }

  async function loadVacations() {
    if (!selectedBarber) return;
    try {
      const res = await generica({ metodo: "GET", uri: `/barber-schedule/barber/${selectedBarber}/vacations` });
      setVacations(Array.isArray(res?.data) ? res.data : []);
    } catch { setVacations([]); }
  }

  async function loadRecurring() {
    if (!selectedBarber) return;
    try {
      const res = await generica({ metodo: "GET", uri: `/barber-schedule/barber/${selectedBarber}/recurring` });
      setRecurringSchedules(Array.isArray(res?.data) ? res.data : []);
    } catch { setRecurringSchedules([]); }
  }

  async function loadVacationDays(year?: number) {
    if (!selectedBarber) return;
    try {
      const params: any = {};
      if (year) params.year = year;
      const res = await generica({ metodo: "GET", uri: `/barber-schedule/barber/${selectedBarber}/vacation-days`, params });
      setVacationDaysUsed(typeof res?.data === "number" ? res.data : null);
    } catch { setVacationDaysUsed(null); }
  }

  async function checkAvailability() {
    if (!selectedBarber || !checkStart || !checkEnd) { toast.error("Preencha horários"); return; }
    try {
      const res = await generica({ metodo: "GET", uri: "/barber-schedule/availability/check", params: { barberId: selectedBarber, startTime: checkStart, endTime: checkEnd } });
      setCheckResult(res?.data === true);
      toast.info(res?.data ? "Barbeiro disponível!" : "Barbeiro indisponível");
    } catch { toast.error("Erro ao verificar disponibilidade"); }
  }

  async function loadAvailabilitySlots() {
    if (!selectedBarber || !slotDate) return;
    try {
      const res = await generica({ metodo: "GET", uri: "/barber-schedule/availability/slots", params: { barberId: selectedBarber, date: slotDate, slotDurationMinutes: slotDuration } });
      setAvailabilitySlots(Array.isArray(res?.data) ? res.data : []);
    } catch { setAvailabilitySlots([]); }
  }

  async function loadAvailableBarbers() {
    if (!checkStart || !checkEnd) { toast.error("Preencha horários"); return; }
    try {
      const res = await generica({ metodo: "GET", uri: "/barber-schedule/availability/barbers", params: { startTime: checkStart, endTime: checkEnd } });
      setAvailableBarbersList(Array.isArray(res?.data) ? res.data : []);
    } catch { setAvailableBarbersList([]); }
  }

  async function editScheduleSubmit() {
    if (!editingSchedule) return;
    try {
      const res = await generica({ metodo: "PUT", uri: `/barber-schedule/${editingSchedule.id}`, data: { startDateTime: editStart, endDateTime: editEnd, reason: editReason } });
      if (res?.status === 200) {
        toast.success("Bloqueio atualizado!");
        setEditModalOpen(false);
        loadSchedules(); loadVacations(); loadRecurring();
      } else toast.error("Erro ao atualizar");
    } catch { toast.error("Erro ao atualizar bloqueio"); }
  }

  async function deactivateSchedule(id: number) {
    const result = await Swal.fire({
      title: "Desativar bloqueio?",
      text: "Deseja desativar este bloqueio?",
      icon: "warning",
      showCancelButton: true,
      confirmButtonColor: "#E94560",
      cancelButtonColor: "#6c757d",
      confirmButtonText: "Sim, desativar!",
      cancelButtonText: "Cancelar",
    });
    if (!result.isConfirmed) return;
    try {
      const res = await generica({ metodo: "POST", uri: `/barber-schedule/${id}/deactivate` });
      if (res?.status === 200) {
        toast.success("Bloqueio desativado!");
        loadSchedules(); loadVacations(); loadRecurring();
      } else toast.error("Erro ao desativar");
    } catch { toast.error("Erro ao desativar bloqueio"); }
  }

  function openEditSchedule(s: ScheduleBlock) {
    setEditingSchedule(s);
    setEditStart(s.startDateTime || "");
    setEditEnd(s.endDateTime || "");
    setEditReason(s.reason || "");
    setEditModalOpen(true);
  }

  function openCreateModal(type: BlockType) {
    setBlockType(type);
    setStartDate("");
    setEndDate("");
    setReason("");
    setDayOfWeek("MONDAY");
    setStartTime("12:00");
    setEndTime("13:00");
    setModalOpen(true);
  }

  async function handleSubmit(e: React.FormEvent) {
    e.preventDefault();
    if (!selectedBarber) { toast.error("Selecione um barbeiro"); return; }
    setSaving(true);
    try {
      let res;
      if (blockType === "VACATION") {
        if (!startDate || !endDate) { toast.error("Datas obrigatórias"); setSaving(false); return; }
        res = await generica({
          metodo: "POST",
          uri: "/barber-schedule/vacation",
          params: { barberId: selectedBarber, startDate: `${startDate}T00:00:00`, endDate: `${endDate}T23:59:59`, reason },
        });
      } else if (blockType === "DAY_OFF") {
        if (!startDate) { toast.error("Data obrigatória"); setSaving(false); return; }
        res = await generica({
          metodo: "POST",
          uri: "/barber-schedule/day-off",
          params: { barberId: selectedBarber, date: `${startDate}T00:00:00`, reason },
        });
      } else if (blockType === "LUNCH_BREAK") {
        res = await generica({
          metodo: "POST",
          uri: "/barber-schedule/lunch-break",
          params: { barberId: selectedBarber, dayOfWeek, startTime, endTime },
        });
      } else {
        if (!startDate || !endDate) { toast.error("Datas obrigatórias"); setSaving(false); return; }
        res = await generica({
          metodo: "POST",
          uri: "/barber-schedule/block",
          params: {
            barberId: selectedBarber,
            type: "BLOCK",
            startDateTime: `${startDate}T${startTime}:00`,
            endDateTime: `${endDate}T${endTime}:00`,
            reason,
            recurring: false,
          },
        });
      }
      if (res?.status === 200 || res?.status === 201) {
        toast.success("Bloqueio criado com sucesso!");
        setModalOpen(false);
        loadSchedules();
        loadVacations();
      } else {
        toast.error("Erro ao criar bloqueio");
      }
    } catch (err: any) {
      toast.error(err?.response?.data?.message || "Erro ao criar bloqueio");
    } finally { setSaving(false); }
  }

  async function handleDelete(id: number) {
    const result = await Swal.fire({
      title: "Excluir bloqueio?",
      text: "Deseja realmente excluir este bloqueio?",
      icon: "warning",
      showCancelButton: true,
      confirmButtonColor: "#E94560",
      cancelButtonColor: "#6c757d",
      confirmButtonText: "Sim, excluir!",
      cancelButtonText: "Cancelar",
    });
    if (!result.isConfirmed) return;
    try {
      await generica({ metodo: "DELETE", uri: `/barber-schedule/${id}` });
      toast.success("Bloqueio removido");
      loadSchedules();
      loadVacations();
    } catch { toast.error("Erro ao remover bloqueio"); }
  }

  function formatDate(d?: string) {
    if (!d) return "—";
    return new Date(d).toLocaleDateString("pt-BR");
  }
  function formatTime(d?: string) {
    if (!d) return "";
    return new Date(d).toLocaleTimeString("pt-BR", { hour: "2-digit", minute: "2-digit" });
  }

  const getTypeInfo = (type?: string) => blockTypeLabels[(type || "BLOCK") as BlockType] || blockTypeLabels.BLOCK;

  const currentBarberName = barbers.find((b) => b.idBarber === selectedBarber)?.name || "Barbeiro";

  return (
    <GoBarberLayout>
      <div className="space-y-6">
        <div className="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4">
          <h1 className="text-2xl font-bold text-[#1A1A2E]">Agenda do Barbeiro</h1>
          <div className="flex gap-2 flex-wrap">
            <button onClick={() => openCreateModal("VACATION")} className="px-3 py-2 bg-blue-100 text-blue-700 rounded-lg text-sm font-medium hover:bg-blue-200 flex items-center gap-2">
              <FaUmbrellaBeach /> Férias
            </button>
            <button onClick={() => openCreateModal("DAY_OFF")} className="px-3 py-2 bg-green-100 text-green-700 rounded-lg text-sm font-medium hover:bg-green-200 flex items-center gap-2">
              <FaCalendarTimes /> Folga
            </button>
            <button onClick={() => openCreateModal("LUNCH_BREAK")} className="px-3 py-2 bg-yellow-100 text-yellow-700 rounded-lg text-sm font-medium hover:bg-yellow-200 flex items-center gap-2">
              <FaCoffee /> Almoço
            </button>
            <button onClick={() => openCreateModal("BLOCK")} className="px-3 py-2 bg-red-100 text-red-700 rounded-lg text-sm font-medium hover:bg-red-200 flex items-center gap-2">
              <FaBan /> Bloqueio
            </button>
          </div>
        </div>

        {/* Seletor de barbeiro */}
        <div className="gobarber-card">
          <label className="block text-sm font-medium text-gray-700 mb-2">Barbeiro</label>
          <select
            value={selectedBarber || ""}
            onChange={(e) => setSelectedBarber(Number(e.target.value))}
            className="gobarber-input max-w-xs"
          >
            {barbers.map((b) => (
              <option key={b.idBarber} value={b.idBarber}>{b.name || `Barbeiro #${b.idBarber}`}</option>
            ))}
          </select>
        </div>

        {/* Tabs */}
        <div className="flex gap-2 border-b border-gray-200 flex-wrap">
          {(["blocks", "vacations", "recurring", "availability", "vacation-days"] as const).map((tab) => (
            <button
              key={tab}
              onClick={() => setActiveTab(tab)}
              className={`px-4 py-2 text-sm font-medium border-b-2 transition ${
                activeTab === tab ? "border-[#E94560] text-[#E94560]" : "border-transparent text-gray-500 hover:text-gray-700"
              }`}
            >
              {tab === "blocks" ? "Bloqueios & Folgas" : tab === "vacations" ? "Férias" : tab === "recurring" ? "Recorrentes" : tab === "availability" ? "Disponibilidade" : "Dias de Férias"}
            </button>
          ))}
        </div>

        {/* Content */}
        <div className="space-y-3">
          {loading ? (
            Array.from({ length: 3 }).map((_, i) => (
              <div key={i} className="gobarber-card animate-pulse">
                <div className="h-5 bg-gray-200 rounded w-1/3 mb-2" />
                <div className="h-4 bg-gray-200 rounded w-1/2" />
              </div>
            ))
          ) : activeTab === "vacations" ? (
            vacations.length === 0 ? (
              <div className="gobarber-card text-center py-8 text-gray-400">Nenhuma férias registrada para {currentBarberName}</div>
            ) : (
              vacations.map((v) => {
                const info = getTypeInfo(v.type);
                return (
                  <div key={v.id} className="gobarber-card flex items-center justify-between">
                    <div className="flex items-center gap-3">
                      <span className={`px-2 py-1 text-xs rounded-full ${info.color} flex items-center gap-1`}>{info.icon} {info.label}</span>
                      <div>
                        <p className="text-sm font-medium">{formatDate(v.startDateTime)} — {formatDate(v.endDateTime)}</p>
                        {v.reason && <p className="text-xs text-gray-500">{v.reason}</p>}
                      </div>
                    </div>
                    <button onClick={() => handleDelete(v.id)} className="p-1.5 text-red-600 hover:bg-red-50 rounded"><FaTrash /></button>
                  </div>
                );
              })
            )
          ) : activeTab === "recurring" ? (
            recurringSchedules.length === 0 ? (
              <div className="gobarber-card text-center py-8 text-gray-400">Nenhum horário recorrente para {currentBarberName}</div>
            ) : (
              recurringSchedules.map((s) => {
                const info = getTypeInfo(s.type);
                return (
                  <div key={s.id} className="gobarber-card flex items-center justify-between">
                    <div className="flex items-center gap-3">
                      <span className={`px-2 py-1 text-xs rounded-full ${info.color} flex items-center gap-1`}>{info.icon} {info.label}</span>
                      <div>
                        <p className="text-sm font-medium">
                          {s.dayOfWeek ? daysOfWeek.find((d) => d.value === s.dayOfWeek)?.label || s.dayOfWeek : ""}{" "}
                          {formatTime(s.startDateTime)} - {formatTime(s.endDateTime)}
                        </p>
                        {s.reason && <p className="text-xs text-gray-500">{s.reason}</p>}
                      </div>
                    </div>
                    <div className="flex gap-1">
                      <button onClick={() => openEditSchedule(s)} className="p-1.5 text-blue-600 hover:bg-blue-50 rounded" title="Editar"><FaEdit /></button>
                      <button onClick={() => deactivateSchedule(s.id)} className="p-1.5 text-yellow-600 hover:bg-yellow-50 rounded" title="Desativar"><FaPowerOff /></button>
                      <button onClick={() => handleDelete(s.id)} className="p-1.5 text-red-600 hover:bg-red-50 rounded" title="Excluir"><FaTrash /></button>
                    </div>
                  </div>
                );
              })
            )
          ) : activeTab === "availability" ? (
            <div className="space-y-4">
              <div className="gobarber-card">
                <h4 className="font-medium text-[#1A1A2E] mb-3 flex items-center gap-2"><FaSearch /> Verificar Disponibilidade</h4>
                <div className="grid grid-cols-1 md:grid-cols-3 gap-3">
                  <input type="datetime-local" value={checkStart} onChange={e => setCheckStart(e.target.value)} className="gobarber-input" placeholder="Início" />
                  <input type="datetime-local" value={checkEnd} onChange={e => setCheckEnd(e.target.value)} className="gobarber-input" placeholder="Fim" />
                  <div className="flex gap-2">
                    <button onClick={checkAvailability} className="gobarber-btn-primary text-sm flex-1">Verificar Barbeiro</button>
                    <button onClick={loadAvailableBarbers} className="px-3 py-2 bg-blue-100 text-blue-700 rounded-lg text-sm hover:bg-blue-200">Todos Disponíveis</button>
                  </div>
                </div>
                {checkResult !== null && (
                  <p className={`mt-2 text-sm font-medium ${checkResult ? "text-green-600" : "text-red-600"}`}>
                    {checkResult ? "✓ Barbeiro disponível neste horário" : "✗ Barbeiro indisponível neste horário"}
                  </p>
                )}
                {availableBarbersList.length > 0 && (
                  <div className="mt-3">
                    <p className="text-sm font-medium text-gray-700 mb-2">Barbeiros disponíveis:</p>
                    <div className="flex flex-wrap gap-2">
                      {availableBarbersList.map((b: any, i: number) => (
                        <span key={i} className="px-3 py-1 bg-green-50 text-green-700 rounded-full text-sm">{b.name || b.barberName || `Barbeiro #${b.idBarber || b.barberId}`}</span>
                      ))}
                    </div>
                  </div>
                )}
              </div>
              <div className="gobarber-card">
                <h4 className="font-medium text-[#1A1A2E] mb-3 flex items-center gap-2"><FaClock /> Slots Disponíveis</h4>
                <div className="grid grid-cols-1 md:grid-cols-3 gap-3 mb-3">
                  <input type="date" value={slotDate} onChange={e => setSlotDate(e.target.value)} className="gobarber-input" />
                  <select value={slotDuration} onChange={e => setSlotDuration(Number(e.target.value))} className="gobarber-input">
                    <option value={15}>15 min</option>
                    <option value={30}>30 min</option>
                    <option value={45}>45 min</option>
                    <option value={60}>60 min</option>
                  </select>
                  <button onClick={loadAvailabilitySlots} className="gobarber-btn-primary text-sm">Buscar Slots</button>
                </div>
                {availabilitySlots.length > 0 ? (
                  <div className="flex flex-wrap gap-2">
                    {availabilitySlots.map((slot: any, i: number) => (
                      <span key={i} className="px-3 py-1.5 bg-blue-50 text-blue-700 rounded-lg text-sm border border-blue-200">
                        {typeof slot === "string" ? slot : `${slot.start || slot.startTime || ""} - ${slot.end || slot.endTime || ""}`}
                      </span>
                    ))}
                  </div>
                ) : (
                  <p className="text-sm text-gray-400">Clique em "Buscar Slots" para ver horários disponíveis</p>
                )}
              </div>
            </div>
          ) : activeTab === "vacation-days" ? (
            <div className="gobarber-card">
              <h4 className="font-medium text-[#1A1A2E] mb-3 flex items-center gap-2"><FaCalendarDay /> Dias de Férias Utilizados</h4>
              <div className="text-center py-6">
                <p className="text-4xl font-bold text-[#E94560]">{vacationDaysUsed ?? "—"}</p>
                <p className="text-sm text-gray-500 mt-2">dias de férias usados por {currentBarberName} este ano</p>
              </div>
            </div>
          ) : (
            schedules.filter((s) => !s.recurring).length === 0 ? (
              <div className="gobarber-card text-center py-8 text-gray-400">Nenhum bloqueio ativo para {currentBarberName}</div>
            ) : (
              schedules.filter((s) => !s.recurring).map((s) => {
                const info = getTypeInfo(s.type);
                return (
                  <div key={s.id} className="gobarber-card flex items-center justify-between">
                    <div className="flex items-center gap-3">
                      <span className={`px-2 py-1 text-xs rounded-full ${info.color} flex items-center gap-1`}>{info.icon} {info.label}</span>
                      <div>
                        <p className="text-sm font-medium">
                          {formatDate(s.startDateTime)} {formatTime(s.startDateTime)} — {formatDate(s.endDateTime)} {formatTime(s.endDateTime)}
                        </p>
                        {s.reason && <p className="text-xs text-gray-500">{s.reason}</p>}
                      </div>
                    </div>
                    <div className="flex gap-1">
                      <button onClick={() => openEditSchedule(s)} className="p-1.5 text-blue-600 hover:bg-blue-50 rounded" title="Editar"><FaEdit /></button>
                      <button onClick={() => deactivateSchedule(s.id)} className="p-1.5 text-yellow-600 hover:bg-yellow-50 rounded" title="Desativar"><FaPowerOff /></button>
                      <button onClick={() => handleDelete(s.id)} className="p-1.5 text-red-600 hover:bg-red-50 rounded" title="Excluir"><FaTrash /></button>
                    </div>
                  </div>
                );
              })
            )
          )}
        </div>
      </div>

      {/* Modal Editar Bloqueio */}
      <Modal isOpen={editModalOpen} onClose={() => setEditModalOpen(false)} title="Editar Bloqueio">
        <div className="space-y-4">
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">Início</label>
            <input type="datetime-local" value={editStart} onChange={e => setEditStart(e.target.value)} className="gobarber-input" />
          </div>
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">Fim</label>
            <input type="datetime-local" value={editEnd} onChange={e => setEditEnd(e.target.value)} className="gobarber-input" />
          </div>
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">Motivo</label>
            <input type="text" value={editReason} onChange={e => setEditReason(e.target.value)} className="gobarber-input" />
          </div>
          <div className="flex justify-end gap-3 pt-4 border-t">
            <button onClick={() => setEditModalOpen(false)} className="px-4 py-2 text-gray-600 hover:bg-gray-100 rounded-lg">Cancelar</button>
            <button onClick={editScheduleSubmit} className="gobarber-btn-primary">Salvar</button>
          </div>
        </div>
      </Modal>

      <Modal isOpen={modalOpen} onClose={() => setModalOpen(false)} title={`Novo ${blockTypeLabels[blockType].label}`}>
        <form onSubmit={handleSubmit} className="space-y-4">
          {blockType === "LUNCH_BREAK" ? (
            <>
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-1">Dia da Semana</label>
                <select value={dayOfWeek} onChange={(e) => setDayOfWeek(e.target.value)} className="gobarber-input">
                  {daysOfWeek.map((d) => <option key={d.value} value={d.value}>{d.label}</option>)}
                </select>
              </div>
              <div className="grid grid-cols-2 gap-4">
                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-1">Início</label>
                  <input type="time" value={startTime} onChange={(e) => setStartTime(e.target.value)} className="gobarber-input" required />
                </div>
                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-1">Fim</label>
                  <input type="time" value={endTime} onChange={(e) => setEndTime(e.target.value)} className="gobarber-input" required />
                </div>
              </div>
            </>
          ) : blockType === "DAY_OFF" ? (
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">Data da Folga</label>
              <input type="date" value={startDate} onChange={(e) => setStartDate(e.target.value)} className="gobarber-input" required />
            </div>
          ) : (
            <div className="grid grid-cols-2 gap-4">
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-1">{blockType === "BLOCK" ? "Data/Hora Início" : "Data Início"}</label>
                <input type={blockType === "BLOCK" ? "date" : "date"} value={startDate} onChange={(e) => setStartDate(e.target.value)} className="gobarber-input" required />
              </div>
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-1">{blockType === "BLOCK" ? "Data/Hora Fim" : "Data Fim"}</label>
                <input type={blockType === "BLOCK" ? "date" : "date"} value={endDate} onChange={(e) => setEndDate(e.target.value)} className="gobarber-input" required />
              </div>
              {blockType === "BLOCK" && (
                <>
                  <div>
                    <label className="block text-sm font-medium text-gray-700 mb-1">Hora Início</label>
                    <input type="time" value={startTime} onChange={(e) => setStartTime(e.target.value)} className="gobarber-input" />
                  </div>
                  <div>
                    <label className="block text-sm font-medium text-gray-700 mb-1">Hora Fim</label>
                    <input type="time" value={endTime} onChange={(e) => setEndTime(e.target.value)} className="gobarber-input" />
                  </div>
                </>
              )}
            </div>
          )}
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">Motivo</label>
            <input type="text" value={reason} onChange={(e) => setReason(e.target.value)} className="gobarber-input" placeholder="Opcional" />
          </div>
          <div className="flex justify-end gap-3 pt-4 border-t">
            <button type="button" onClick={() => setModalOpen(false)} className="px-4 py-2 text-gray-600 hover:bg-gray-100 rounded-lg">Cancelar</button>
            <button type="submit" disabled={saving} className="gobarber-btn-primary">{saving ? "Salvando..." : "Criar"}</button>
          </div>
        </form>
      </Modal>
    </GoBarberLayout>
  );
}
