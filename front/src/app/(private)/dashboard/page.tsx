"use client";

import GoBarberLayout from "@/components/Layout/GoBarberLayout";
import React, { useEffect, useState, useContext, useMemo } from "react";
import { AuthContext } from "@/contexts/AuthContext";
import { generica } from "@/api/api";
import { useRoles } from "@/hooks/useRoles";
import {
  FaCalendarCheck,
  FaDollarSign,
  FaUsers,
  FaCut,
  FaArrowUp,
  FaArrowDown,
  FaChevronLeft,
  FaChevronRight,
  FaClock,
  FaUser,
  FaExclamationTriangle,
  FaCheck,
  FaTimes,
  FaChartLine,
  FaHourglassHalf,
  FaChartBar,
  FaFileAlt,
  FaSyncAlt,
  FaCalendarDay,
  FaCalendarWeek,
} from "react-icons/fa";
import { toast } from "react-toastify";

interface DashboardData {
  totalRevenue?: number;
  totalAppointments?: number;
  totalClients?: number;
  totalBarbers?: number;
  revenueGrowth?: number;
  appointmentsToday?: number;
  averageTicket?: number;
}

interface KPIData {
  avgTicket?: number;
  occupancyRate?: number;
  noShowRate?: number;
  clientRetentionRate?: number;
  avgServicesPerAppointment?: number;
  revenuePerBarber?: number;
}

interface ServiceItem {
  id?: number;
  name?: string;
  value?: number;
  time?: string | number;
}

interface BarberInfo {
  idBarber?: number;
  name?: string;
  email?: string;
}

interface Appointment {
  id?: number;
  idAppointment?: number;
  clientName?: string;
  barberName?: string;
  barber?: BarberInfo;
  startTime?: string;
  endTime?: string;
  status?: string;
  serviceNames?: string[];
  serviceTypeNames?: string[];
  serviceType?: ServiceItem[];
}

interface BarberStatus {
  barberId?: number;
  barberName?: string;
  status?: string;
  currentClient?: string;
  nextAppointment?: string;
  appointmentsToday?: number;
  /* real API fields from /dashboard/barbers-status */
  id?: number;
  name?: string;
  active?: boolean;
}

/**
 * Converte "dd/MM/yyyy HH:mm" para "yyyy-MM-dd"
 */
function parseDateStr(raw: string): string | null {
  // Tenta ISO primeiro: "2026-01-05..." → substring(0,10)
  if (/^\d{4}-\d{2}-\d{2}/.test(raw)) return raw.substring(0, 10);
  // Formato backend: "dd/MM/yyyy HH:mm"
  const m = raw.match(/^(\d{2})\/(\d{2})\/(\d{4})/);
  if (m) return `${m[3]}-${m[2]}-${m[1]}`;
  return null;
}

/**
 * Converte "dd/MM/yyyy HH:mm" para objeto Date
 */
function parseDateTime(raw: string): Date | null {
  if (/^\d{4}-/.test(raw)) return new Date(raw);
  const m = raw.match(/^(\d{2})\/(\d{2})\/(\d{4})\s+(\d{2}):(\d{2})/);
  if (m) return new Date(+m[3], +m[2] - 1, +m[1], +m[4], +m[5]);
  return null;
}

const WEEKDAYS = ["Dom", "Seg", "Ter", "Qua", "Qui", "Sex", "Sáb"];
const MONTHS = [
  "Janeiro", "Fevereiro", "Março", "Abril", "Maio", "Junho",
  "Julho", "Agosto", "Setembro", "Outubro", "Novembro", "Dezembro",
];

export default function DashboardPage() {
  const auth = useContext(AuthContext);
  const [data, setData] = useState<DashboardData>({});
  const [kpis, setKpis] = useState<KPIData>({});
  const [loading, setLoading] = useState(true);
  const [appointments, setAppointments] = useState<Appointment[]>([]);
  const [pendingAppointments, setPendingAppointments] = useState<Appointment[]>([]);
  const [todayAppointments, setTodayAppointments] = useState<Appointment[]>([]);
  const [barberStatuses, setBarberStatuses] = useState<BarberStatus[]>([]);
  const [calMonth, setCalMonth] = useState(new Date().getMonth());
  const [calYear, setCalYear] = useState(new Date().getFullYear());
  const [selectedDate, setSelectedDate] = useState<string | null>(null);
  const [activeTab, setActiveTab] = useState<"calendar" | "pending" | "barbers" | "reports">("calendar");

  // Reports state
  const [reportPeriod, setReportPeriod] = useState("month");
  const [reportData, setReportData] = useState<any>(null);
  const [financialData, setFinancialData] = useState<any>(null);
  const [clientsReport, setClientsReport] = useState<any>(null);
  const [barbersReport, setBarbersReport] = useState<any>(null);
  const [servicesReport, setServicesReport] = useState<any>(null);
  const [revenueRealtime, setRevenueRealtime] = useState<any>(null);
  const [trendRevenue, setTrendRevenue] = useState<any[]>([]);
  const [trendAppointments, setTrendAppointments] = useState<any[]>([]);
  const [trendClients, setTrendClients] = useState<any[]>([]);
  const [compareMom, setCompareMom] = useState<any>(null);
  const [compareYoy, setCompareYoy] = useState<any>(null);
  const [selectedBarberKpi, setSelectedBarberKpi] = useState("");
  const [barberKpiData, setBarberKpiData] = useState<any>(null);
  const [customStart, setCustomStart] = useState("");
  const [customEnd, setCustomEnd] = useState("");
  const [reportLoading, setReportLoading] = useState(false);
  const { isAdmin: isAdminRole, isSecretary: isSecretaryRole } = useRoles();
  const userRoles = auth?.user?.roles || [];
  const isOnlyBarber = userRoles.includes("BARBER") && !userRoles.includes("ADMIN") && !userRoles.includes("SECRETARY");

  useEffect(() => {
    async function loadAll() {
      setLoading(true);
      try {
        if (isOnlyBarber) {
          // Barbeiro: carregar apenas seus próprios agendamentos
          const [dashRes, ownRes, todayRes, kpiRes, barbersRes] = await Promise.all([
            generica({ metodo: "GET", uri: "/dashboard/month" }).catch(() => null),
            generica({ metodo: "GET", uri: "/appointments/future/barber/own" }).catch(() => null),
            generica({ metodo: "GET", uri: "/dashboard/appointments-today" }).catch(() => null),
            generica({ metodo: "GET", uri: "/dashboard/kpis" }).catch(() => null),
            generica({ metodo: "GET", uri: "/dashboard/barbers-status" }).catch(() => null),
          ]);

          if (dashRes?.data) setData(dashRes.data);

          const ownData = ownRes?.data?.content || ownRes?.data || [];
          setAppointments(Array.isArray(ownData) ? ownData : []);
          setPendingAppointments([]);

          const todayData = todayRes?.data || [];
          setTodayAppointments(Array.isArray(todayData) ? todayData : []);

          if (kpiRes?.data) setKpis(kpiRes.data);

          const barbData = barbersRes?.data || [];
          setBarberStatuses((Array.isArray(barbData) ? barbData : []).map((b: any) => ({ ...b, barberId: b.barberId ?? b.id, barberName: b.barberName ?? b.name, status: b.status ?? (b.active === true ? 'AVAILABLE' : b.active === false ? 'INACTIVE' : undefined) })));
        } else {
          // Admin/Secretária: carregar tudo
          const [dashRes, appRes, pendRes, todayRes, kpiRes, barbersRes] = await Promise.all([
            generica({ metodo: "GET", uri: "/dashboard/month" }).catch(() => null),
            generica({ metodo: "GET", uri: "/appointments", params: { page: 0, size: 200 } }).catch(() => null),
            generica({ metodo: "GET", uri: "/appointments/pending" }).catch(() => null),
            generica({ metodo: "GET", uri: "/dashboard/appointments-today" }).catch(() => null),
            generica({ metodo: "GET", uri: "/dashboard/kpis" }).catch(() => null),
            generica({ metodo: "GET", uri: "/dashboard/barbers-status" }).catch(() => null),
          ]);

          if (dashRes?.data) setData(dashRes.data);

          const appData = appRes?.data?.content || appRes?.data || [];
          setAppointments(Array.isArray(appData) ? appData : []);

          const pendData = pendRes?.data?.content || pendRes?.data || [];
          setPendingAppointments(Array.isArray(pendData) ? pendData : []);

          const todayData = todayRes?.data || [];
          setTodayAppointments(Array.isArray(todayData) ? todayData : []);

          if (kpiRes?.data) setKpis(kpiRes.data);

          const barbData = barbersRes?.data || [];
          setBarberStatuses((Array.isArray(barbData) ? barbData : []).map((b: any) => ({ ...b, barberId: b.barberId ?? b.id, barberName: b.barberName ?? b.name, status: b.status ?? (b.active === true ? 'AVAILABLE' : b.active === false ? 'INACTIVE' : undefined) })));
        }
      } catch (err) {
        console.error("Erro ao carregar dashboard:", err);
      } finally {
        setLoading(false);
      }
    }
    loadAll();
  }, []);

  // Report loaders
  async function loadReportByPeriod(period: string) {
    setReportLoading(true);
    try {
      const uri = period === "today" ? "/dashboard/today" : period === "week" ? "/dashboard/week" : period === "year" ? "/dashboard/year" : "/dashboard/month";
      const res = await generica({ metodo: "GET", uri });
      if (res?.data) setReportData(res.data);
    } catch { /* silencioso */ }
    finally { setReportLoading(false); }
  }

  async function loadCustomRange() {
    if (!customStart || !customEnd) { toast.error("Informe as datas"); return; }
    setReportLoading(true);
    try {
      const res = await generica({ metodo: "GET", uri: "/dashboard", params: { startDate: `${customStart}T00:00:00`, endDate: `${customEnd}T23:59:59` } });
      if (res?.data) setReportData(res.data);
    } catch { toast.error("Erro ao carregar relatório"); }
    finally { setReportLoading(false); }
  }

  function getReportDateParams() {
    const now = new Date();
    const end = now.toISOString().split("T")[0] + "T23:59:59";
    let start: string;
    switch (reportPeriod) {
      case "today":  start = now.toISOString().split("T")[0] + "T00:00:00"; break;
      case "week":   { const d = new Date(now); d.setDate(d.getDate() - 7); start = d.toISOString().split("T")[0] + "T00:00:00"; break; }
      case "year":   start = `${now.getFullYear()}-01-01T00:00:00`; break;
      default:       start = `${now.getFullYear()}-${String(now.getMonth() + 1).padStart(2, "0")}-01T00:00:00`;
    }
    if (customStart && customEnd) {
      start = `${customStart}T00:00:00`;
      return { startDate: start, endDate: `${customEnd}T23:59:59` };
    }
    return { startDate: start, endDate: end };
  }

  async function loadFinancial() {
    try { const p = getReportDateParams(); const res = await generica({ metodo: "GET", uri: "/dashboard/financial", params: p }); if (res?.data) setFinancialData(res.data); } catch { /* */ }
  }

  async function loadClientsReport() {
    try {
      const p = getReportDateParams();
      const res = await generica({ metodo: "GET", uri: "/dashboard/clients", params: p });
      if (res?.data) {
        const raw = res.data;
        // Filter out object/array values to keep only scalar report metrics
        if (typeof raw === 'object' && !Array.isArray(raw)) {
          const safe: Record<string, any> = {};
          for (const [k, v] of Object.entries(raw)) {
            if (v === null || typeof v !== 'object') safe[k] = v;
            else if (Array.isArray(v)) safe[k + 'Count'] = v.length;
          }
          setClientsReport(safe);
        } else {
          setClientsReport(raw);
        }
      }
    } catch { /* */ }
  }

  async function loadBarbersReport() {
    try {
      const p = getReportDateParams();
      const res = await generica({ metodo: "GET", uri: "/dashboard/barbers", params: p });
      if (res?.data) {
        // API returns { barbers: [raw entities] } — extract flat list
        const raw = res.data;
        if (raw.barbers && Array.isArray(raw.barbers)) {
          const flat = raw.barbers.map((b: any) => ({
            name: b.name || 'Sem nome',
            idBarber: b.idBarber,
            contato: b.contato || '—',
            active: b.active !== false ? 'Ativo' : 'Inativo',
          }));
          setBarbersReport(flat);
        } else {
          setBarbersReport(raw);
        }
      }
    } catch { /* */ }
  }

  async function loadServicesReport() {
    try {
      const p = getReportDateParams();
      const res = await generica({ metodo: "GET", uri: "/dashboard/services-report", params: p });
      if (res?.data) {
        const raw = res.data;
        if (raw.services && Array.isArray(raw.services)) {
          const flat = raw.services.map((s: any) => ({
            name: s.name || s.name_service || 'Sem nome',
            value: s.value ?? s.price_service ?? 0,
            count: s.count ?? 0,
          }));
          setServicesReport(flat);
        } else {
          setServicesReport(raw);
        }
      }
    } catch { /* */ }
  }

  async function loadRevenueRealtime() {
    try { const res = await generica({ metodo: "GET", uri: "/dashboard/revenue-realtime" }); if (res?.data) setRevenueRealtime(res.data); } catch { /* */ }
  }

  async function loadTrends() {
    try {
      const [r1, r2, r3] = await Promise.all([
        generica({ metodo: "GET", uri: "/dashboard/trend/revenue" }).catch(() => null),
        generica({ metodo: "GET", uri: "/dashboard/trend/appointments" }).catch(() => null),
        generica({ metodo: "GET", uri: "/dashboard/trend/clients" }).catch(() => null),
      ]);
      if (r1?.data) setTrendRevenue(Array.isArray(r1.data) ? r1.data : []);
      if (r2?.data) setTrendAppointments(Array.isArray(r2.data) ? r2.data : []);
      if (r3?.data) setTrendClients(Array.isArray(r3.data) ? r3.data : []);
    } catch { /* */ }
  }

  async function loadCompare() {
    if (!customStart || !customEnd) {
      toast.error("Informe as datas de início e fim para comparar");
      return;
    }
    try {
      // O backend espera 4 períodos: período selecionado vs período anterior de mesma duração
      const start = new Date(customStart);
      const end = new Date(customEnd);
      const diffMs = end.getTime() - start.getTime();
      const prevEnd = new Date(start.getTime() - 86400000); // dia anterior ao início
      const prevStart = new Date(prevEnd.getTime() - diffMs);
      const fmt = (d: Date) => d.toISOString().split("T")[0];
      const res = await generica({
        metodo: "GET",
        uri: "/dashboard/compare",
        params: {
          period1Start: `${fmt(prevStart)}T00:00:00`,
          period1End: `${fmt(prevEnd)}T23:59:59`,
          period2Start: `${customStart}T00:00:00`,
          period2End: `${customEnd}T23:59:59`,
        },
      });
      if (res?.data) setReportData(res.data);
    } catch { /* */ }
  }

  async function loadCompareMom() {
    try { const res = await generica({ metodo: "GET", uri: "/dashboard/compare-mom" }); if (res?.data) setCompareMom(res.data); } catch { /* */ }
  }

  async function loadCompareYoy() {
    try { const res = await generica({ metodo: "GET", uri: "/dashboard/compare-yoy" }); if (res?.data) setCompareYoy(res.data); } catch { /* */ }
  }

  async function loadBarberKpis(barberId: number) {
    try { const res = await generica({ metodo: "GET", uri: `/dashboard/barber/${barberId}/kpis` }); if (res?.data) setBarberKpiData(res.data); } catch { /* */ }
  }

  async function loadAllReports() {
    setReportLoading(true);
    await Promise.all([loadReportByPeriod(reportPeriod), loadFinancial(), loadClientsReport(), loadBarbersReport(), loadServicesReport(), loadRevenueRealtime(), loadTrends(), loadCompareMom(), loadCompareYoy()]);
    setReportLoading(false);
  }

  // Gerar dias do calendário
  const calendarDays = useMemo(() => {
    const firstDay = new Date(calYear, calMonth, 1);
    const lastDay = new Date(calYear, calMonth + 1, 0);
    const startPad = firstDay.getDay();
    const days: { date: Date; current: boolean }[] = [];

    // Dias do mês anterior para completar a semana
    for (let i = startPad - 1; i >= 0; i--) {
      const d = new Date(calYear, calMonth, -i);
      days.push({ date: d, current: false });
    }
    // Dias do mês atual
    for (let d = 1; d <= lastDay.getDate(); d++) {
      days.push({ date: new Date(calYear, calMonth, d), current: true });
    }
    // Completar última semana
    while (days.length % 7 !== 0) {
      const last = days[days.length - 1].date;
      const next = new Date(last);
      next.setDate(next.getDate() + 1);
      days.push({ date: next, current: false });
    }
    return days;
  }, [calMonth, calYear]);

  // Mapa de agendamentos por data (yyyy-MM-dd)
  const appointmentsByDate = useMemo(() => {
    const map: Record<string, Appointment[]> = {};
    appointments.forEach((a) => {
      if (!a.startTime) return;
      const dateStr = parseDateStr(a.startTime);
      if (!dateStr) return;
      if (!map[dateStr]) map[dateStr] = [];
      map[dateStr].push(a);
    });
    return map;
  }, [appointments]);

  const today = new Date();
  const todayStr = `${today.getFullYear()}-${String(today.getMonth() + 1).padStart(2, "0")}-${String(today.getDate()).padStart(2, "0")}`;

  function dateToStr(d: Date) {
    return `${d.getFullYear()}-${String(d.getMonth() + 1).padStart(2, "0")}-${String(d.getDate()).padStart(2, "0")}`;
  }

  function prevMonth() {
    if (calMonth === 0) { setCalMonth(11); setCalYear(calYear - 1); }
    else setCalMonth(calMonth - 1);
    setSelectedDate(null);
  }
  function nextMonth() {
    if (calMonth === 11) { setCalMonth(0); setCalYear(calYear + 1); }
    else setCalMonth(calMonth + 1);
    setSelectedDate(null);
  }

  const selectedAppointments = selectedDate ? (appointmentsByDate[selectedDate] || []) : [];

  async function handleApprove(id: number) {
    try {
      const res = await generica({ metodo: "POST", uri: `/appointments/${id}/approve` });
      if (res?.status === 200) {
        toast.success("Agendamento aprovado!");
        setPendingAppointments((prev) => prev.filter((a) => (a.id || a.idAppointment) !== id));
      } else {
        const msg = res?.data?.message || res?.data?.error || "Erro ao aprovar";
        toast.error(msg);
      }
    } catch {
      toast.error("Erro ao aprovar agendamento");
    }
  }

  async function handleReject(id: number) {
    try {
      const res = await generica({
        metodo: "POST",
        uri: `/appointments/${id}/reject`,
        data: { reason: "Rejeitado pelo administrador" },
      });
      if (res?.status === 200) {
        toast.success("Agendamento rejeitado.");
        setPendingAppointments((prev) => prev.filter((a) => (a.id || a.idAppointment) !== id));
      } else {
        const msg = res?.data?.message || res?.data?.error || "Erro ao rejeitar";
        toast.error(msg);
      }
    } catch {
      toast.error("Erro ao rejeitar agendamento");
    }
  }

  const cards = [
    {
      title: "Receita do Mês",
      value: data.totalRevenue != null ? `R$ ${data.totalRevenue.toLocaleString("pt-BR", { minimumFractionDigits: 2 })}` : "—",
      icon: <FaDollarSign />, color: "bg-green-500", growth: data.revenueGrowth,
    },
    { title: "Agendamentos Hoje", value: data.appointmentsToday ?? todayAppointments.length ?? "—", icon: <FaCalendarCheck />, color: "bg-blue-500" },
    { title: "Total de Clientes", value: data.totalClients ?? "—", icon: <FaUsers />, color: "bg-purple-500" },
    { title: "Barbeiros Ativos", value: data.totalBarbers ?? (barberStatuses.filter(b => b.active !== false && b.status !== 'INACTIVE').length || "—"), icon: <FaCut />, color: "bg-orange-500" },
    { title: "Pendentes", value: pendingAppointments.length, icon: <FaHourglassHalf />, color: pendingAppointments.length > 0 ? "bg-yellow-500" : "bg-gray-400" },
    { title: "Ticket Médio", value: (kpis as any).avgTicket != null ? `R$ ${(kpis as any).avgTicket.toFixed(2)}` : data.averageTicket != null ? `R$ ${data.averageTicket.toFixed(2)}` : "—", icon: <FaChartLine />, color: "bg-teal-500" },
  ];

  return (
    <GoBarberLayout>
      <div className="space-y-8">
        {/* Saudação */}
        <div>
          <h1 className="text-2xl font-bold text-[#1A1A2E]">Olá, {auth?.user?.nome?.split(" ")[0] || "Usuário"} 👋</h1>
          <p className="text-gray-500 mt-1">Veja como está o movimento da sua barbearia hoje.</p>
        </div>

        {/* Cards de Métricas */}
        <div className="grid grid-cols-2 sm:grid-cols-3 lg:grid-cols-6 gap-4">
          {cards.map((card, i) => (
            <div key={i} className="gobarber-card">
              <div className="flex items-center justify-between">
                <div className="min-w-0">
                  <p className="text-xs text-gray-500 mb-1 truncate">{card.title}</p>
                  <p className="text-xl font-bold text-[#1A1A2E]">
                    {loading ? <span className="animate-pulse bg-gray-200 rounded h-6 w-16 inline-block" /> : card.value}
                  </p>
                  {card.growth != null && (
                    <p className={`text-xs mt-1 flex items-center gap-1 ${card.growth >= 0 ? "text-green-600" : "text-red-500"}`}>
                      {card.growth >= 0 ? <FaArrowUp /> : <FaArrowDown />}
                      {Math.abs(card.growth).toFixed(1)}%
                    </p>
                  )}
                </div>
                <div className={`w-10 h-10 ${card.color} rounded-xl flex items-center justify-center text-white text-lg shrink-0`}>{card.icon}</div>
              </div>
            </div>
          ))}
        </div>

        {/* Tabs de Navegação */}
        <div className="flex gap-1 bg-gray-100 rounded-xl p-1">
          {([
            { key: "calendar", label: "Calendário", icon: <FaCalendarCheck /> },
            { key: "pending", label: `Pendentes (${pendingAppointments.length})`, icon: <FaHourglassHalf /> },
            { key: "barbers", label: "Status Barbeiros", icon: <FaCut /> },
            { key: "reports", label: "Relatórios", icon: <FaChartBar /> },
          ] as const).map((tab) => (
            <button
              key={tab.key}
              onClick={() => { setActiveTab(tab.key); if (tab.key === "reports" && !financialData) loadAllReports(); }}
              className={`flex-1 py-2.5 text-sm font-medium rounded-lg flex items-center justify-center gap-2 transition-colors
                ${activeTab === tab.key
                  ? "bg-white text-[#1A1A2E] shadow-sm"
                  : "text-gray-500 hover:text-gray-700"
                }`}
            >
              {tab.icon}
              <span className="hidden sm:inline">{tab.label}</span>
            </button>
          ))}
        </div>

        {/* Tab: Calendário */}
        {activeTab === "calendar" && (
        <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
          {/* Calendário */}
          <div className="lg:col-span-2 gobarber-card">
            <div className="flex items-center justify-between mb-4">
              <h3 className="text-lg font-semibold text-[#1A1A2E]">Calendário de Agendamentos</h3>
              <div className="flex items-center gap-3">
                <button onClick={prevMonth} className="p-2 hover:bg-gray-100 rounded-lg transition-colors"><FaChevronLeft className="text-gray-500" /></button>
                <span className="font-semibold text-[#1A1A2E] min-w-[160px] text-center">{MONTHS[calMonth]} {calYear}</span>
                <button onClick={nextMonth} className="p-2 hover:bg-gray-100 rounded-lg transition-colors"><FaChevronRight className="text-gray-500" /></button>
              </div>
            </div>

            {/* Header dias da semana */}
            <div className="grid grid-cols-7 mb-2">
              {WEEKDAYS.map((d) => (
                <div key={d} className="text-center text-xs font-semibold text-gray-400 py-2">{d}</div>
              ))}
            </div>

            {/* Grid dos dias */}
            <div className="grid grid-cols-7 gap-1">
              {calendarDays.map((day, i) => {
                const ds = dateToStr(day.date);
                const appts = appointmentsByDate[ds] || [];
                const isToday = ds === todayStr;
                const isSelected = ds === selectedDate;
                const hasAppts = appts.length > 0;

                return (
                  <button
                    key={i}
                    onClick={() => setSelectedDate(ds === selectedDate ? null : ds)}
                    className={`
                      relative p-2 min-h-[56px] rounded-lg text-sm transition-all
                      ${!day.current ? "text-gray-300" : "text-gray-700 hover:bg-gray-50"}
                      ${isToday ? "ring-2 ring-[#E94560] font-bold" : ""}
                      ${isSelected ? "bg-[#1A1A2E] text-white hover:bg-[#1A1A2E]" : ""}
                    `}
                  >
                    <span className="block">{day.date.getDate()}</span>
                    {hasAppts && day.current && (
                      <div className="flex justify-center gap-0.5 mt-1">
                        {appts.length <= 3 ? (
                          appts.map((_, j) => (
                            <span key={j} className={`w-1.5 h-1.5 rounded-full ${isSelected ? "bg-white" : "bg-[#E94560]"}`} />
                          ))
                        ) : (
                          <>
                            <span className={`w-1.5 h-1.5 rounded-full ${isSelected ? "bg-white" : "bg-[#E94560]"}`} />
                            <span className={`text-[10px] ml-0.5 ${isSelected ? "text-white" : "text-[#E94560]"} font-bold`}>{appts.length}</span>
                          </>
                        )}
                      </div>
                    )}
                  </button>
                );
              })}
            </div>
          </div>

          {/* Lista de agendamentos do dia selecionado */}
          <div className="gobarber-card">
            <h3 className="text-lg font-semibold text-[#1A1A2E] mb-4">
              {selectedDate
                ? `Agendamentos - ${new Date(selectedDate + "T12:00:00").toLocaleDateString("pt-BR", { day: "2-digit", month: "long" })}`
                : "Agendamentos de Hoje"}
            </h3>

            {(() => {
              const list = selectedDate ? selectedAppointments : (appointmentsByDate[todayStr] || []);
              if (list.length === 0) {
                return (
                  <div className="text-center py-8 text-gray-400">
                    <FaCalendarCheck className="mx-auto text-3xl mb-2" />
                    <p className="text-sm">Nenhum agendamento{selectedDate ? " neste dia" : " hoje"}</p>
                    {!selectedDate && <p className="text-xs mt-1">Selecione um dia no calendário</p>}
                  </div>
                );
              }
              return (
                <div className="space-y-3 max-h-[400px] overflow-y-auto pr-1">
                  {list
                    .sort((a, b) => (a.startTime || "").localeCompare(b.startTime || ""))
                    .map((a, i) => {
                      const startDt = a.startTime ? parseDateTime(a.startTime) : null;
                      const endDt = a.endTime ? parseDateTime(a.endTime) : null;
                      const time = startDt ? startDt.toLocaleTimeString("pt-BR", { hour: "2-digit", minute: "2-digit" }) : "";
                      const endTime = endDt ? endDt.toLocaleTimeString("pt-BR", { hour: "2-digit", minute: "2-digit" }) : "";
                      const barber = a.barberName || a.barber?.name || "";
                      const services: string[] = a.serviceNames || a.serviceTypeNames || (a.serviceType?.map((s) => s.name || "").filter(Boolean) as string[]) || [];
                      const statusColor =
                        a.status?.toUpperCase() === "CONFIRMED" ? "border-green-400 bg-green-50" :
                        a.status?.toUpperCase() === "CANCELLED" ? "border-red-300 bg-red-50" :
                        a.status?.toUpperCase() === "PENDING_APPROVAL" ? "border-yellow-400 bg-yellow-50" :
                        a.status?.toUpperCase() === "REJECTED" ? "border-red-500 bg-red-100" :
                        "border-blue-300 bg-blue-50";

                      return (
                        <div key={i} className={`border-l-4 ${statusColor} rounded-lg p-3`}>
                          <div className="flex items-center gap-2 mb-1">
                            <FaClock className="text-gray-400 text-xs" />
                            <span className="text-sm font-semibold text-[#1A1A2E]">{time}{endTime ? ` - ${endTime}` : ""}</span>
                          </div>
                          <div className="flex items-center gap-2">
                            <FaUser className="text-gray-400 text-xs" />
                            <span className="text-sm">{a.clientName || "Sem cliente"}</span>
                          </div>
                          {barber && <p className="text-xs text-gray-500 mt-1"><FaCut className="inline mr-1" />{barber}</p>}
                          {services.length > 0 && (
                            <div className="flex flex-wrap gap-1 mt-2">
                              {services.map((s, j) => (
                                <span key={j} className="text-[10px] px-2 py-0.5 bg-white rounded-full text-gray-600 border">{s}</span>
                              ))}
                            </div>
                          )}
                        </div>
                      );
                    })}
                </div>
              );
            })()}
          </div>
        </div>
        )}

        {/* Tab: Agendamentos Pendentes */}
        {activeTab === "pending" && (
          <div className="gobarber-card">
            <h3 className="text-lg font-semibold text-[#1A1A2E] mb-4 flex items-center gap-2">
              <FaExclamationTriangle className="text-yellow-500" />
              Agendamentos Pendentes de Aprovação
            </h3>
            {pendingAppointments.length === 0 ? (
              <div className="text-center py-12 text-gray-400">
                <FaCheck className="mx-auto text-4xl mb-3 text-green-400" />
                <p className="text-base font-medium">Nenhum agendamento pendente</p>
                <p className="text-sm mt-1">Todos os agendamentos foram processados.</p>
              </div>
            ) : (
              <div className="space-y-3 max-h-[500px] overflow-y-auto">
                {pendingAppointments.map((a, i) => {
                  const startDt = a.startTime ? parseDateTime(a.startTime) : null;
                  const date = startDt
                    ? startDt.toLocaleDateString("pt-BR", { day: "2-digit", month: "short" })
                    : "";
                  const time = startDt
                    ? startDt.toLocaleTimeString("pt-BR", { hour: "2-digit", minute: "2-digit" })
                    : "";
                  const barber = a.barberName || a.barber?.name || "—";
                  const services: string[] =
                    a.serviceNames || a.serviceTypeNames ||
                    (a.serviceType?.map((s) => s.name || "").filter(Boolean) as string[]) || [];
                  const apptId = a.id || a.idAppointment;

                  return (
                    <div key={i} className="border border-yellow-200 bg-yellow-50 rounded-lg p-4">
                      <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-3">
                        <div className="min-w-0">
                          <div className="flex items-center gap-3 mb-1">
                            <span className="text-sm font-bold text-[#1A1A2E]">
                              {date} às {time}
                            </span>
                            <span className="text-xs px-2 py-0.5 bg-yellow-200 text-yellow-800 rounded-full font-medium">
                              Pendente
                            </span>
                          </div>
                          <p className="text-sm text-gray-700">
                            <FaUser className="inline mr-1 text-gray-400" />
                            {a.clientName || "Sem nome"}
                          </p>
                          <p className="text-xs text-gray-500">
                            <FaCut className="inline mr-1" />{barber}
                          </p>
                          {services.length > 0 && (
                            <div className="flex flex-wrap gap-1 mt-2">
                              {services.map((s, j) => (
                                <span key={j} className="text-[10px] px-2 py-0.5 bg-white rounded-full text-gray-600 border">
                                  {s}
                                </span>
                              ))}
                            </div>
                          )}
                        </div>
                        <div className="flex gap-2 shrink-0">
                          <button
                            onClick={() => apptId && handleApprove(apptId)}
                            className="px-4 py-2 bg-green-600 text-white rounded-lg text-sm font-medium hover:bg-green-700 transition-colors flex items-center gap-1"
                          >
                            <FaCheck /> Aprovar
                          </button>
                          <button
                            onClick={() => apptId && handleReject(apptId)}
                            className="px-4 py-2 bg-red-500 text-white rounded-lg text-sm font-medium hover:bg-red-600 transition-colors flex items-center gap-1"
                          >
                            <FaTimes /> Rejeitar
                          </button>
                        </div>
                      </div>
                    </div>
                  );
                })}
              </div>
            )}
          </div>
        )}

        {/* Tab: Status dos Barbeiros */}
        {activeTab === "barbers" && (
          <div className="gobarber-card">
            <h3 className="text-lg font-semibold text-[#1A1A2E] mb-4 flex items-center gap-2">
              <FaCut className="text-[#E94560]" />
              Status dos Barbeiros Hoje
            </h3>
            {barberStatuses.length === 0 ? (
              <div className="text-center py-12 text-gray-400">
                <FaCut className="mx-auto text-4xl mb-3" />
                <p className="text-sm">Nenhum dado de status disponível</p>
              </div>
            ) : (
              <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-4">
                {barberStatuses.map((bs, i) => (
                  <div key={i} className="border rounded-xl p-4">
                    <div className="flex items-center gap-3 mb-3">
                      <div className="w-10 h-10 bg-[#1A1A2E] rounded-full flex items-center justify-center text-white">
                        <FaCut />
                      </div>
                      <div>
                        <p className="font-semibold text-[#1A1A2E] text-sm">{bs.barberName || "Barbeiro"}</p>
                        <span className={`text-xs px-2 py-0.5 rounded-full ${
                          bs.status === "AVAILABLE" ? "bg-green-100 text-green-700" :
                          bs.status === "BUSY" || bs.status === "INACTIVE" ? "bg-red-100 text-red-700" :
                          "bg-gray-100 text-gray-600"
                        }`}>
                          {bs.status === "AVAILABLE" ? "Disponível" :
                           bs.status === "INACTIVE" ? "Inativo" :
                           bs.status === "BUSY" ? "Ocupado" :
                           "—"}
                        </span>
                      </div>
                    </div>
                    {bs.currentClient && (
                      <p className="text-xs text-gray-500">
                        <FaUser className="inline mr-1" />
                        Atendendo: {bs.currentClient}
                      </p>
                    )}
                    <p className="text-xs text-gray-400 mt-1">
                      Agendamentos hoje: {bs.appointmentsToday ?? 0}
                    </p>
                  </div>
                ))}
              </div>
            )}
          </div>
        )}

        {/* Tab: Relatórios */}
        {activeTab === "reports" && (
          <div className="space-y-6">
            {/* Period selector */}
            <div className="gobarber-card">
              <div className="flex flex-wrap gap-3 items-center">
                <span className="text-sm font-medium text-gray-600">Período:</span>
                {[
                  { key: "today", label: "Hoje", icon: <FaCalendarDay /> },
                  { key: "week", label: "Semana", icon: <FaCalendarWeek /> },
                  { key: "month", label: "Mês", icon: <FaCalendarCheck /> },
                  { key: "year", label: "Ano", icon: <FaChartLine /> },
                ].map(p => (
                  <button key={p.key} onClick={() => { setReportPeriod(p.key); loadReportByPeriod(p.key); }} className={`px-3 py-1.5 rounded-lg text-sm flex items-center gap-1.5 ${reportPeriod === p.key ? "bg-[#1A1A2E] text-white" : "bg-gray-100 text-gray-600 hover:bg-gray-200"}`}>
                    {p.icon} {p.label}
                  </button>
                ))}
                <div className="flex items-center gap-2 ml-auto">
                  <input type="date" value={customStart} onChange={(e) => setCustomStart(e.target.value)} className="gobarber-input text-sm w-auto" />
                  <span className="text-gray-400">até</span>
                  <input type="date" value={customEnd} onChange={(e) => setCustomEnd(e.target.value)} className="gobarber-input text-sm w-auto" />
                  <button onClick={loadCustomRange} className="px-3 py-1.5 bg-[#E94560] text-white rounded-lg text-sm">Filtrar</button>
                  <button onClick={loadCompare} className="px-3 py-1.5 bg-blue-100 text-blue-700 rounded-lg text-sm">Comparar</button>
                </div>
              </div>
            </div>

            {reportLoading && <div className="text-center py-8 text-gray-400">Carregando relatórios...</div>}

            {/* Report data cards */}
            {reportData && (
              <div className="gobarber-card">
                <h3 className="text-lg font-semibold text-[#1A1A2E] mb-3 flex items-center gap-2"><FaFileAlt /> Resumo do Período</h3>
                <div className="grid grid-cols-2 md:grid-cols-4 gap-4">
                  {Object.entries(reportData).filter(([, v]) => v === null || typeof v !== 'object').slice(0, 12).map(([key, val]) => (
                    <div key={key} className="text-center p-3 bg-gray-50 rounded-lg">
                      <p className="text-lg font-bold text-[#1A1A2E]">{typeof val === 'number' ? (key.toLowerCase().includes('revenue') || key.toLowerCase().includes('receita') ? `R$ ${val.toFixed(2)}` : val) : String(val ?? '—')}</p>
                      <p className="text-xs text-gray-500 mt-1">{key.replace(/([A-Z])/g, ' $1').trim()}</p>
                    </div>
                  ))}
                </div>
              </div>
            )}

            {/* Revenue realtime */}
            {revenueRealtime && (
              <div className="gobarber-card bg-gradient-to-r from-green-50 to-teal-50">
                <h3 className="text-base font-semibold text-green-800 mb-2 flex items-center gap-2"><FaSyncAlt /> Receita em Tempo Real</h3>
                <div className="grid grid-cols-2 md:grid-cols-4 gap-3">
                  {Object.entries(revenueRealtime).filter(([, v]) => v === null || typeof v !== 'object').slice(0, 8).map(([key, val]) => (
                    <div key={key} className="text-center">
                      <p className="text-lg font-bold text-green-700">{typeof val === 'number' ? `R$ ${val.toFixed(2)}` : String(val ?? '—')}</p>
                      <p className="text-xs text-gray-500">{key.replace(/([A-Z])/g, ' $1').trim()}</p>
                    </div>
                  ))}
                </div>
              </div>
            )}

            {/* Financial, Clients, Barbers, Services reports */}
            <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
              {financialData && (
                <div className="gobarber-card">
                  <h3 className="text-base font-semibold text-[#1A1A2E] mb-3 flex items-center gap-2"><FaDollarSign /> Financeiro</h3>
                  <div className="space-y-2">
                    {Object.entries(financialData).filter(([, val]) => val === null || typeof val !== 'object').slice(0, 10).map(([k, v]) => (
                      <div key={k} className="flex justify-between items-center py-1 border-b border-gray-100">
                        <span className="text-sm text-gray-600">{k.replace(/([A-Z])/g, ' $1').trim()}</span>
                        <span className="text-sm font-semibold text-[#1A1A2E]">{typeof v === 'number' ? (k.toLowerCase().includes('count') || k.toLowerCase().includes('total') && !k.toLowerCase().includes('revenue') ? v : `R$ ${v.toFixed(2)}`) : String(v ?? '—')}</span>
                      </div>
                    ))}
                  </div>
                </div>
              )}
              {clientsReport && (
                <div className="gobarber-card">
                  <h3 className="text-base font-semibold text-[#1A1A2E] mb-3 flex items-center gap-2"><FaUsers /> Clientes</h3>
                  <div className="space-y-2">
                    {Object.entries(clientsReport).filter(([, val]) => val === null || typeof val !== 'object').slice(0, 10).map(([k, v]) => (
                      <div key={k} className="flex justify-between items-center py-1 border-b border-gray-100">
                        <span className="text-sm text-gray-600">{k.replace(/([A-Z])/g, ' $1').trim()}</span>
                        <span className="text-sm font-semibold">{typeof v === 'number' ? v : String(v ?? '—')}</span>
                      </div>
                    ))}
                  </div>
                </div>
              )}
              {barbersReport && (
                <div className="gobarber-card">
                  <h3 className="text-base font-semibold text-[#1A1A2E] mb-3 flex items-center gap-2"><FaCut /> Barbeiros</h3>
                  <div className="space-y-2">
                    {(Array.isArray(barbersReport) ? barbersReport : Object.entries(barbersReport).map(([k, v]) => ({ name: k, value: typeof v === 'object' ? (Array.isArray(v) ? v.length : JSON.stringify(v)) : v }))).slice(0, 10).map((item: any, i: number) => (
                      <div key={i} className="flex justify-between items-center py-1 border-b border-gray-100">
                        <span className="text-sm text-gray-600">{String(item.name || `#${i + 1}`)}</span>
                        <span className="text-sm font-semibold">{typeof (item.value ?? item.appointments ?? item.active) === 'object' ? JSON.stringify(item.value ?? item.active) : String(item.value ?? item.appointments ?? item.active ?? '—')}</span>
                      </div>
                    ))}
                  </div>
                </div>
              )}
              {servicesReport && (
                <div className="gobarber-card">
                  <h3 className="text-base font-semibold text-[#1A1A2E] mb-3 flex items-center gap-2"><FaCut /> Serviços</h3>
                  <div className="space-y-2">
                    {(Array.isArray(servicesReport) ? servicesReport : Object.entries(servicesReport).map(([k, v]) => ({ name: k, value: typeof v === 'object' ? (Array.isArray(v) ? v.length : JSON.stringify(v)) : v }))).slice(0, 10).map((item: any, i: number) => (
                      <div key={i} className="flex justify-between items-center py-1 border-b border-gray-100">
                        <span className="text-sm text-gray-600">{String(item.name || `#${i + 1}`)}</span>
                        <span className="text-sm font-semibold">{typeof (item.count ?? item.revenue ?? item.value) === 'object' ? JSON.stringify(item.count ?? item.revenue ?? item.value) : String(item.count ?? item.revenue ?? item.value ?? '—')}</span>
                      </div>
                    ))}
                  </div>
                </div>
              )}
            </div>

            {/* Comparisons MoM / YoY */}
            <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
              {compareMom && (
                <div className="gobarber-card">
                  <h3 className="text-base font-semibold text-[#1A1A2E] mb-3">Comparação Mês a Mês</h3>
                  <div className="space-y-2">
                    {Object.entries(compareMom).filter(([, val]) => val === null || typeof val !== 'object').slice(0, 8).map(([k, v]) => (
                      <div key={k} className="flex justify-between items-center py-1 border-b border-gray-100">
                        <span className="text-sm text-gray-600">{k.replace(/([A-Z])/g, ' $1').trim()}</span>
                        <span className={`text-sm font-semibold ${typeof v === 'number' && v > 0 ? 'text-green-600' : typeof v === 'number' && v < 0 ? 'text-red-600' : ''}`}>
                          {typeof v === 'number' ? (k.toLowerCase().includes('percent') || k.toLowerCase().includes('growth') ? `${v > 0 ? '+' : ''}${v.toFixed(1)}%` : v) : String(v ?? '—')}
                        </span>
                      </div>
                    ))}
                  </div>
                </div>
              )}
              {compareYoy && (
                <div className="gobarber-card">
                  <h3 className="text-base font-semibold text-[#1A1A2E] mb-3">Comparação Ano a Ano</h3>
                  <div className="space-y-2">
                    {Object.entries(compareYoy).filter(([, val]) => val === null || typeof val !== 'object').slice(0, 8).map(([k, v]) => (
                      <div key={k} className="flex justify-between items-center py-1 border-b border-gray-100">
                        <span className="text-sm text-gray-600">{k.replace(/([A-Z])/g, ' $1').trim()}</span>
                        <span className={`text-sm font-semibold ${typeof v === 'number' && v > 0 ? 'text-green-600' : typeof v === 'number' && v < 0 ? 'text-red-600' : ''}`}>
                          {typeof v === 'number' ? (k.toLowerCase().includes('percent') || k.toLowerCase().includes('growth') ? `${v > 0 ? '+' : ''}${v.toFixed(1)}%` : v) : String(v ?? '—')}
                        </span>
                      </div>
                    ))}
                  </div>
                </div>
              )}
            </div>

            {/* Trends */}
            {(trendRevenue.length > 0 || trendAppointments.length > 0 || trendClients.length > 0) && (
              <div className="gobarber-card">
                <h3 className="text-base font-semibold text-[#1A1A2E] mb-3 flex items-center gap-2"><FaChartLine /> Tendências</h3>
                <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
                  {trendRevenue.length > 0 && (
                    <div>
                      <h4 className="text-sm font-medium text-gray-600 mb-2">Receita</h4>
                      <div className="space-y-1">
                        {trendRevenue.slice(-6).map((t: any, i: number) => (
                          <div key={i} className="flex justify-between text-xs">
                            <span className="text-gray-500">{t.period || t.date || t.month || `#${i + 1}`}</span>
                            <span className="font-semibold">R$ {(t.value ?? t.revenue ?? 0).toFixed?.(2) ?? t.value}</span>
                          </div>
                        ))}
                      </div>
                    </div>
                  )}
                  {trendAppointments.length > 0 && (
                    <div>
                      <h4 className="text-sm font-medium text-gray-600 mb-2">Agendamentos</h4>
                      <div className="space-y-1">
                        {trendAppointments.slice(-6).map((t: any, i: number) => (
                          <div key={i} className="flex justify-between text-xs">
                            <span className="text-gray-500">{t.period || t.date || t.month || `#${i + 1}`}</span>
                            <span className="font-semibold">{t.value ?? t.count ?? 0}</span>
                          </div>
                        ))}
                      </div>
                    </div>
                  )}
                  {trendClients.length > 0 && (
                    <div>
                      <h4 className="text-sm font-medium text-gray-600 mb-2">Clientes</h4>
                      <div className="space-y-1">
                        {trendClients.slice(-6).map((t: any, i: number) => (
                          <div key={i} className="flex justify-between text-xs">
                            <span className="text-gray-500">{t.period || t.date || t.month || `#${i + 1}`}</span>
                            <span className="font-semibold">{t.value ?? t.count ?? 0}</span>
                          </div>
                        ))}
                      </div>
                    </div>
                  )}
                </div>
              </div>
            )}

            {/* Barber KPIs */}
            <div className="gobarber-card">
              <h3 className="text-base font-semibold text-[#1A1A2E] mb-3">KPIs por Barbeiro</h3>
              <div className="flex gap-3 items-center mb-4">
                <select value={selectedBarberKpi} onChange={(e) => { setSelectedBarberKpi(e.target.value); if (e.target.value) loadBarberKpis(parseInt(e.target.value)); else setBarberKpiData(null); }} className="gobarber-input w-auto">
                  <option value="">Selecione um barbeiro</option>
                  {barberStatuses.map(b => <option key={b.barberId} value={b.barberId}>{b.barberName || `Barbeiro #${b.barberId}`}</option>)}
                </select>
              </div>
              {barberKpiData && (
                <div className="grid grid-cols-2 md:grid-cols-4 gap-3">
                  {Object.entries(barberKpiData).slice(0, 12).map(([k, v]) => (
                    <div key={k} className="text-center p-3 bg-gray-50 rounded-lg">
                      <p className="text-lg font-bold text-[#1A1A2E]">{typeof v === 'number' ? (k.toLowerCase().includes('revenue') || k.toLowerCase().includes('ticket') ? `R$ ${v.toFixed(2)}` : k.toLowerCase().includes('rate') ? `${v.toFixed(1)}%` : v) : String(v ?? '—')}</p>
                      <p className="text-xs text-gray-500">{k.replace(/([A-Z])/g, ' $1').trim()}</p>
                    </div>
                  ))}
                </div>
              )}
            </div>
          </div>
        )}
      </div>
    </GoBarberLayout>
  );
}
