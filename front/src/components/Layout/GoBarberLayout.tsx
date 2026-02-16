"use client";

import React, { useState, useContext } from "react";
import Link from "next/link";
import { usePathname } from "next/navigation";
import { AuthContext } from "@/contexts/AuthContext";
import {
  FaCalendarAlt,
  FaCut,
  FaUsers,
  FaBoxes,
  FaStar,
  FaChartBar,
  FaCreditCard,
  FaBell,
  FaClock,
  FaTags,
  FaCog,
  FaSignOutAlt,
  FaBars,
  FaTimes,
  FaUserTie,
  FaHome,
} from "react-icons/fa";

const menuItems = [
  { path: "/dashboard", label: "Dashboard", icon: <FaHome /> },
  { path: "/agendamentos", label: "Agendamentos", icon: <FaCalendarAlt /> },
  { path: "/barbeiros", label: "Barbeiros", icon: <FaCut /> },
  { path: "/clientes", label: "Clientes", icon: <FaUsers /> },
  { path: "/servicos", label: "Serviços", icon: <FaUserTie /> },
  { path: "/produtos", label: "Produtos & Estoque", icon: <FaBoxes /> },
  { path: "/avaliacoes", label: "Avaliações", icon: <FaStar /> },
  { path: "/pagamentos", label: "Pagamentos", icon: <FaCreditCard /> },
  { path: "/promocoes", label: "Promoções & Cupons", icon: <FaTags /> },
  { path: "/lista-espera", label: "Lista de Espera", icon: <FaClock /> },
  { path: "/notificacoes", label: "Notificações", icon: <FaBell /> },
  { path: "/relatorios", label: "Relatórios", icon: <FaChartBar /> },
];

interface GoBarberLayoutProps {
  children: React.ReactNode;
}

export default function GoBarberLayout({ children }: GoBarberLayoutProps) {
  const pathname = usePathname();
  const auth = useContext(AuthContext);
  const [sidebarOpen, setSidebarOpen] = useState(false);

  const handleLogout = async () => {
    await auth?.logout();
    window.location.href = "/login";
  };

  const currentTitle =
    menuItems.find((item) => pathname?.startsWith(item.path))?.label ||
    "GoBarber";

  return (
    <div className="min-h-screen bg-gray-50 flex">
      {/* Overlay mobile */}
      {sidebarOpen && (
        <div
          className="fixed inset-0 bg-black/50 z-40 lg:hidden"
          onClick={() => setSidebarOpen(false)}
        />
      )}

      {/* Sidebar */}
      <aside
        className={`fixed lg:static inset-y-0 left-0 z-50 w-64 bg-[#1A1A2E] text-white 
                     transform transition-transform duration-300 ease-in-out flex flex-col
                     ${sidebarOpen ? "translate-x-0" : "-translate-x-full lg:translate-x-0"}`}
      >
        {/* Logo */}
        <div className="h-16 flex items-center justify-between px-6 border-b border-white/10">
          <Link href="/dashboard" className="flex items-center gap-3">
            <span className="text-2xl">💈</span>
            <span className="text-xl font-bold">GoBarber</span>
          </Link>
          <button
            onClick={() => setSidebarOpen(false)}
            className="lg:hidden text-white/60 hover:text-white"
          >
            <FaTimes />
          </button>
        </div>

        {/* Menu */}
        <nav className="flex-1 overflow-y-auto py-4 px-3">
          <ul className="space-y-1">
            {menuItems.map((item) => {
              const isActive = pathname?.startsWith(item.path);
              return (
                <li key={item.path}>
                  <Link
                    href={item.path}
                    onClick={() => setSidebarOpen(false)}
                    className={`flex items-center gap-3 px-4 py-2.5 rounded-lg text-sm font-medium transition-all
                      ${
                        isActive
                          ? "bg-[#E94560] text-white shadow-lg shadow-[#E94560]/30"
                          : "text-white/70 hover:bg-white/10 hover:text-white"
                      }`}
                  >
                    <span className="text-lg">{item.icon}</span>
                    {item.label}
                  </Link>
                </li>
              );
            })}
          </ul>
        </nav>

        {/* User & Logout */}
        <div className="p-4 border-t border-white/10">
          <div className="flex items-center gap-3 mb-3 px-2">
            <div className="w-9 h-9 rounded-full bg-[#E94560] flex items-center justify-center text-sm font-bold">
              {auth?.user?.nome?.charAt(0)?.toUpperCase() || "U"}
            </div>
            <div className="flex-1 min-w-0">
              <p className="text-sm font-medium truncate">
                {auth?.user?.nome || "Usuário"}
              </p>
              <p className="text-xs text-white/50 truncate">
                {auth?.user?.roles?.[0] || ""}
              </p>
            </div>
          </div>
          <button
            onClick={handleLogout}
            className="flex items-center gap-3 w-full px-4 py-2.5 rounded-lg text-sm text-white/70 
                       hover:bg-red-500/20 hover:text-red-300 transition-all"
          >
            <FaSignOutAlt />
            Sair
          </button>
        </div>
      </aside>

      {/* Main Content */}
      <div className="flex-1 flex flex-col min-h-screen">
        {/* Top Bar */}
        <header className="h-16 bg-white shadow-sm flex items-center justify-between px-6 sticky top-0 z-30">
          <div className="flex items-center gap-4">
            <button
              onClick={() => setSidebarOpen(true)}
              className="lg:hidden text-gray-600 hover:text-gray-900"
            >
              <FaBars className="text-xl" />
            </button>
            <h2 className="text-lg font-semibold text-[#1A1A2E]">
              {currentTitle}
            </h2>
          </div>
          <div className="flex items-center gap-3">
            <Link
              href="/notificacoes"
              className="relative p-2 text-gray-500 hover:text-[#1A1A2E] rounded-lg hover:bg-gray-100 transition-colors"
            >
              <FaBell className="text-lg" />
            </Link>
            <Link
              href="/configuracoes"
              className="p-2 text-gray-500 hover:text-[#1A1A2E] rounded-lg hover:bg-gray-100 transition-colors"
            >
              <FaCog className="text-lg" />
            </Link>
          </div>
        </header>

        {/* Page Content */}
        <main className="flex-1 p-6 animate-fade-in">{children}</main>
      </div>
    </div>
  );
}
