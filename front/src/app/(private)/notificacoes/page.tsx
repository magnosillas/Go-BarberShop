"use client";

import GoBarberLayout from "@/components/Layout/GoBarberLayout";
import React, { useEffect, useState } from "react";
import { generica } from "@/api/api";
import { toast } from "react-toastify";
import {
  FaBell,
  FaCheck,
  FaTrash,
  FaCalendarAlt,
  FaMoneyBill,
  FaExclamationTriangle,
  FaInfoCircle,
} from "react-icons/fa";

interface Notification {
  id: number;
  title?: string;
  message?: string;
  type?: string;
  read?: boolean;
  createdAt?: string;
}

export default function NotificacoesPage() {
  const [notifications, setNotifications] = useState<Notification[]>([]);
  const [loading, setLoading] = useState(true);
  const [filter, setFilter] = useState<"all" | "unread">("all");

  useEffect(() => {
    loadNotifications();
  }, []);

  async function loadNotifications() {
    setLoading(true);
    try {
      const response = await generica({
        metodo: "GET",
        uri: "/notification",
        params: { page: 0, size: 50 },
      });
      const data = response?.data?.content || response?.data || [];
      setNotifications(Array.isArray(data) ? data : []);
    } catch {
      toast.error("Erro ao carregar notificações");
    } finally {
      setLoading(false);
    }
  }

  async function markAsRead(id: number) {
    try {
      await generica({ metodo: "PUT", uri: `/notification/${id}/read` });
      setNotifications((prev) =>
        prev.map((n) => (n.id === id ? { ...n, read: true } : n)),
      );
    } catch {
      toast.error("Erro ao atualizar notificação");
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

  const filtered =
    filter === "unread" ? notifications.filter((n) => !n.read) : notifications;
  const unreadCount = notifications.filter((n) => !n.read).length;

  return (
    <GoBarberLayout>
      <div className="space-y-6">
        <div className="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4">
          <div className="flex items-center gap-3">
            <h1 className="text-2xl font-bold text-[#1A1A2E]">Notificações</h1>
            {unreadCount > 0 && (
              <span className="bg-[#E94560] text-white text-xs font-bold px-2 py-0.5 rounded-full">
                {unreadCount}
              </span>
            )}
          </div>
          <div className="flex gap-2">
            <button
              onClick={() => setFilter("all")}
              className={`px-4 py-2 rounded-lg text-sm font-medium transition ${filter === "all" ? "bg-[#1A1A2E] text-white" : "bg-gray-100 text-gray-600 hover:bg-gray-200"}`}
            >
              Todas
            </button>
            <button
              onClick={() => setFilter("unread")}
              className={`px-4 py-2 rounded-lg text-sm font-medium transition ${filter === "unread" ? "bg-[#1A1A2E] text-white" : "bg-gray-100 text-gray-600 hover:bg-gray-200"}`}
            >
              Não lidas
            </button>
          </div>
        </div>

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
                {filter === "unread"
                  ? "Nenhuma notificação não lida"
                  : "Nenhuma notificação"}
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
                  <p
                    className={`text-sm ${!notif.read ? "font-semibold text-[#1A1A2E]" : "text-gray-700"}`}
                  >
                    {notif.title || notif.message || "Notificação"}
                  </p>
                  {notif.message && notif.title && (
                    <p className="text-xs text-gray-500 mt-1">
                      {notif.message}
                    </p>
                  )}
                  {notif.createdAt && (
                    <p className="text-xs text-gray-400 mt-1">
                      {new Date(notif.createdAt).toLocaleDateString("pt-BR")} às{" "}
                      {new Date(notif.createdAt).toLocaleTimeString("pt-BR", {
                        hour: "2-digit",
                        minute: "2-digit",
                      })}
                    </p>
                  )}
                </div>
                <div className="flex gap-1 shrink-0">
                  {!notif.read && (
                    <button
                      onClick={() => markAsRead(notif.id)}
                      className="p-1.5 text-green-600 hover:bg-green-50 rounded"
                      title="Marcar como lida"
                    >
                      <FaCheck />
                    </button>
                  )}
                  <button
                    onClick={() => deleteNotification(notif.id)}
                    className="p-1.5 text-red-500 hover:bg-red-50 rounded"
                    title="Excluir"
                  >
                    <FaTrash />
                  </button>
                </div>
              </div>
            ))
          )}
        </div>
      </div>
    </GoBarberLayout>
  );
}
