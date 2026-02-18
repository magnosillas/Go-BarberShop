"use client";

import GoBarberLayout from "@/components/Layout/GoBarberLayout";
import React, { useEffect, useState, useContext } from "react";
import { generica } from "@/api/api";
import { AuthContext } from "@/contexts/AuthContext";
import { useRoles } from "@/hooks/useRoles";
import { toast } from "react-toastify";
import {
  FaBell,
  FaCheck,
  FaTrash,
  FaCalendarAlt,
  FaMoneyBill,
  FaExclamationTriangle,
  FaInfoCircle,
  FaCheckDouble,
  FaRedo,
  FaPaperPlane,
  FaChartBar,
  FaTimesCircle,
} from "react-icons/fa";

interface Notification {
  id: number;
  title?: string;
  message?: string;
  type?: string;
  read?: boolean;
  createdAt?: string;
  clientId?: number;
}

export default function NotificacoesPage() {
  const [notifications, setNotifications] = useState<Notification[]>([]);
  const [loading, setLoading] = useState(true);
  const [filter, setFilter] = useState<"all" | "unread" | "failed">("all");
  const auth = useContext(AuthContext);
  const { isAdmin, isSecretary } = useRoles();
  const userId = auth?.user?.id;
  const [unreadCount, setUnreadCount] = useState<number>(0);
  const [notifStats, setNotifStats] = useState<any>(null);
  const [testTitle, setTestTitle] = useState("");
  const [testMessage, setTestMessage] = useState("");
  const [testClientId, setTestClientId] = useState("");
  const [showTestForm, setShowTestForm] = useState(false);

  useEffect(() => {
    if (userId) {
      loadNotifications();
      loadUnreadCount();
      if (isAdmin || isSecretary) loadNotifStats();
    }
  }, [userId]);

  async function loadNotifications() {
    setLoading(true);
    try {
      let data: Notification[] = [];
      
      if (isAdmin || isSecretary) {
        const [pendingRes, recentRes] = await Promise.all([
          generica({ metodo: "GET", uri: "/notification/pending" }).catch(() => null),
          generica({ metodo: "GET", uri: `/notification/client/${userId}`, params: { page: 0, size: 50 } }).catch(() => null),
        ]);
        const pending = pendingRes?.data || [];
        const recent = recentRes?.data?.content || recentRes?.data || [];
        const allNotifs = [...(Array.isArray(pending) ? pending : []), ...(Array.isArray(recent) ? recent : [])];
        const seen = new Set<number>();
        data = allNotifs.filter((n: Notification) => {
          if (seen.has(n.id)) return false;
          seen.add(n.id);
          return true;
        });
      } else {
        const response = await generica({
          metodo: "GET",
          uri: `/notification/client/${userId}`,
          params: { page: 0, size: 50 },
        });
        const result = response?.data?.content || response?.data || [];
        data = Array.isArray(result) ? result : [];
      }
      
      setNotifications(data);
    } catch {
      try {
        const response = await generica({
          metodo: "GET",
          uri: `/notification/client/${userId}/recent`,
          params: { limit: 50 },
        });
        const data = response?.data || [];
        setNotifications(Array.isArray(data) ? data : []);
      } catch {
        setNotifications([]);
      }
    } finally {
      setLoading(false);
    }
  }

  async function markAsRead(id: number) {
    try {
      await generica({ metodo: "POST", uri: `/notification/${id}/read` });
      setNotifications((prev) =>
        prev.map((n) => (n.id === id ? { ...n, read: true } : n))
      );
    } catch {
      toast.error("Erro ao marcar notificação como lida");
    }
  }

  async function markAllAsRead() {
    if (!userId) return;
    try {
      await generica({ metodo: "POST", uri: `/notification/client/${userId}/read-all` });
      setNotifications((prev) => prev.map((n) => ({ ...n, read: true })));
      toast.success("Todas as notificações marcadas como lidas");
    } catch {
      toast.error("Erro ao marcar todas como lidas");
    }
  }

  async function deleteNotification(id: number) {
    try {
      await generica({ metodo: "DELETE", uri: `/notification/${id}` });
      setNotifications((prev) => prev.filter((n) => n.id !== id));
      toast.success("Notificação removida");
    } catch {
      toast.error("Erro ao remover notificação");
    }
  }

  async function loadUnreadCount() {
    if (!userId) return;
    try {
      const res = await generica({ metodo: "GET", uri: `/notification/client/${userId}/unread/count` });
      if (typeof res?.data === "number") setUnreadCount(res.data);
    } catch { /* silencioso */ }
  }

  async function loadUnread() {
    if (!userId) return;
    setLoading(true);
    try {
      const res = await generica({ metodo: "GET", uri: `/notification/client/${userId}/unread` });
      const data = res?.data || [];
      setNotifications(Array.isArray(data) ? data : []);
    } catch { toast.error("Erro ao carregar não lidas"); }
    finally { setLoading(false); }
  }

  async function loadFailed() {
    setLoading(true);
    try {
      const res = await generica({ metodo: "GET", uri: "/notification/failed" });
      const data = res?.data || [];
      setNotifications(Array.isArray(data) ? data : []);
    } catch { toast.error("Erro ao carregar falhas"); }
    finally { setLoading(false); }
  }

  async function resendNotification(id: number) {
    try {
      const res = await generica({ metodo: "POST", uri: `/notification/${id}/resend` });
      if (res?.status === 200) { toast.success("Notificação reenviada!"); loadNotifications(); }
      else toast.error("Erro ao reenviar");
    } catch { toast.error("Erro ao reenviar notificação"); }
  }

  async function loadNotifStats() {
    try {
      const res = await generica({ metodo: "GET", uri: "/notification/stats" });
      if (res?.data) setNotifStats(res.data);
    } catch { /* silencioso */ }
  }

  async function sendTestNotification() {
    const targetId = testClientId.trim() ? parseInt(testClientId.trim(), 10) : null;
    if (!targetId || !testTitle.trim() || !testMessage.trim()) { toast.error("Preencha ID do cliente, título e mensagem"); return; }
    try {
      const res = await generica({ metodo: "POST", uri: "/notification/send-test", params: { clientId: targetId, title: testTitle, message: testMessage } });
      if (res?.status === 200 || res?.status === 201) {
        toast.success("Notificação de teste enviada!");
        setTestTitle(""); setTestMessage(""); setTestClientId(""); setShowTestForm(false);
        loadNotifications();
      } else {
        const msg = res?.data?.message || res?.data?.error || "Erro ao enviar teste";
        toast.error(typeof msg === "string" ? msg : "Erro ao enviar teste");
      }
    } catch { toast.error("Erro ao enviar notificação de teste"); }
  }

  async function deleteOldNotifications(daysOld: number = 30) {
    if (!userId) return;
    if (!confirm(`Excluir notificações com mais de ${daysOld} dias?`)) return;
    try {
      const res = await generica({ metodo: "DELETE", uri: `/notification/client/${userId}/old`, params: { daysOld } });
      if (res?.status === 200 || res?.status === 204) {
        toast.success("Notificações antigas removidas!");
        loadNotifications();
      } else toast.error("Erro ao remover antigas");
    } catch { toast.error("Erro ao remover notificações antigas"); }
  }

  function getIcon(type?: string) {
    switch (type?.toUpperCase()) {
      case "APPOINTMENT":
        return <FaCalendarAlt className="text-blue-500" />;
      case "PAYMENT":
        return <FaMoneyBill className="text-green-500" />;
      case "WARNING":
        return <FaExclamationTriangle className="text-yellow-500" />;
      default:
        return <FaInfoCircle className="text-[#0F3460]" />;
    }
  }

  const filtered = filter === "unread" ? notifications.filter((n) => !n.read) : notifications;
  const unreadCountLocal = notifications.filter((n) => !n.read).length;

  return (
    <GoBarberLayout>
      <div className="space-y-6">
        <div className="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4">
          <div className="flex items-center gap-3">
            <h1 className="text-2xl font-bold text-[#1A1A2E]">Notificações</h1>
            {(unreadCount || unreadCountLocal) > 0 && (
              <span className="bg-[#E94560] text-white text-xs font-bold px-2 py-0.5 rounded-full">
                {unreadCount || unreadCountLocal}
              </span>
            )}
          </div>
          <div className="flex flex-wrap gap-2">
            {(isAdmin || isSecretary) && (
              <>
                <button onClick={() => setShowTestForm(!showTestForm)} className="px-3 py-2 rounded-lg text-sm font-medium bg-purple-100 text-purple-700 hover:bg-purple-200 transition flex items-center gap-2">
                  <FaPaperPlane /> Teste
                </button>
                <button onClick={() => deleteOldNotifications(30)} className="px-3 py-2 rounded-lg text-sm font-medium bg-red-100 text-red-700 hover:bg-red-200 transition flex items-center gap-2">
                  <FaTrash /> Limpar Antigas
                </button>
              </>
            )}
            {unreadCountLocal > 0 && (
              <button
                onClick={markAllAsRead}
                className="px-4 py-2 rounded-lg text-sm font-medium bg-green-100 text-green-700 hover:bg-green-200 transition flex items-center gap-2"
              >
                <FaCheckDouble /> Marcar todas
              </button>
            )}
            <button onClick={() => { setFilter("all"); loadNotifications(); }} className={`px-4 py-2 rounded-lg text-sm font-medium transition ${filter === "all" ? "bg-[#1A1A2E] text-white" : "bg-gray-100 text-gray-600 hover:bg-gray-200"}`}>Todas</button>
            <button onClick={() => { setFilter("unread"); loadUnread(); }} className={`px-4 py-2 rounded-lg text-sm font-medium transition ${filter === "unread" ? "bg-[#1A1A2E] text-white" : "bg-gray-100 text-gray-600 hover:bg-gray-200"}`}>Não lidas</button>
            {(isAdmin || isSecretary) && (
              <button onClick={() => { setFilter("failed"); loadFailed(); }} className={`px-4 py-2 rounded-lg text-sm font-medium transition ${filter === "failed" ? "bg-[#1A1A2E] text-white" : "bg-gray-100 text-gray-600 hover:bg-gray-200"}`}>
                <FaTimesCircle className="inline mr-1" /> Falhas
              </button>
            )}
          </div>
        </div>

        {/* Stats bar */}
        {notifStats && (isAdmin || isSecretary) && (
          <div className="grid grid-cols-2 md:grid-cols-4 gap-3">
            {Object.entries(notifStats).map(([key, value]) => (
              <div key={key} className="gobarber-card text-center py-3">
                <p className="text-lg font-bold text-[#1A1A2E]">{String(value)}</p>
                <p className="text-xs text-gray-500">{key.replace(/([A-Z])/g, " $1").trim()}</p>
              </div>
            ))}
          </div>
        )}

        {/* Test form */}
        {showTestForm && (
          <div className="gobarber-card space-y-3">
            <h3 className="text-sm font-semibold text-gray-700">Enviar Notificação de Teste</h3>
            <input type="number" placeholder="ID do Cliente (ex: 1)" value={testClientId} onChange={e => setTestClientId(e.target.value)} className="gobarber-input" />
            <input type="text" placeholder="Título" value={testTitle} onChange={e => setTestTitle(e.target.value)} className="gobarber-input" />
            <input type="text" placeholder="Mensagem" value={testMessage} onChange={e => setTestMessage(e.target.value)} className="gobarber-input" />
            <button onClick={sendTestNotification} className="gobarber-btn-primary text-sm">Enviar Teste</button>
          </div>
        )}

        <div className="space-y-3">
          {loading ? (
            Array.from({ length: 5 }).map((_, i) => (
              <div key={i} className="gobarber-card animate-pulse flex gap-4">
                <div className="w-10 h-10 bg-gray-200 rounded-full" />
                <div className="flex-1">
                  <div className="h-4 bg-gray-200 rounded w-1/2 mb-2" />
                  <div className="h-3 bg-gray-200 rounded w-3/4" />
                </div>
              </div>
            ))
          ) : filtered.length === 0 ? (
            <div className="gobarber-card text-center py-12">
              <FaBell className="mx-auto text-4xl text-gray-300 mb-3" />
              <p className="text-gray-400">
                {filter === "unread" ? "Nenhuma notificação não lida" : "Nenhuma notificação"}
              </p>
            </div>
          ) : (
            filtered.map((notif) => (
              <div
                key={notif.id}
                className={`gobarber-card flex items-start gap-4 transition ${!notif.read ? "border-l-4 border-l-[#E94560] bg-red-50/30" : ""}`}
              >
                <div className="w-10 h-10 rounded-full bg-gray-100 flex items-center justify-center text-lg shrink-0">
                  {getIcon(notif.type)}
                </div>
                <div className="flex-1 min-w-0">
                  <p className={`text-sm ${!notif.read ? "font-semibold text-[#1A1A2E]" : "text-gray-700"}`}>
                    {notif.title || notif.message || "Notificação"}
                  </p>
                  {notif.message && notif.title && (
                    <p className="text-xs text-gray-500 mt-1">{notif.message}</p>
                  )}
                  {notif.createdAt && (
                    <p className="text-xs text-gray-400 mt-1">
                      {new Date(notif.createdAt).toLocaleDateString("pt-BR")} às{" "}
                      {new Date(notif.createdAt).toLocaleTimeString("pt-BR", { hour: "2-digit", minute: "2-digit" })}
                    </p>
                  )}
                </div>
                <div className="flex gap-1 shrink-0">
                  {!notif.read && (
                    <button onClick={() => markAsRead(notif.id)} className="p-1.5 text-green-600 hover:bg-green-50 rounded" title="Marcar como lida">
                      <FaCheck />
                    </button>
                  )}
                  {(isAdmin || isSecretary) && (
                    <>
                      <button onClick={() => resendNotification(notif.id)} className="p-1.5 text-blue-500 hover:bg-blue-50 rounded" title="Reenviar">
                        <FaRedo />
                      </button>
                      <button onClick={() => deleteNotification(notif.id)} className="p-1.5 text-red-500 hover:bg-red-50 rounded" title="Excluir">
                        <FaTrash />
                      </button>
                    </>
                  )}
                </div>
              </div>
            ))
          )}
        </div>
      </div>
    </GoBarberLayout>
  );
}
