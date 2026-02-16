"use client";

import GoBarberLayout from "@/components/Layout/GoBarberLayout";
import React, { useState, useContext } from "react";
import { AuthContext } from "@/contexts/AuthContext";
import { toast } from "react-toastify";
import { FaUser, FaPalette, FaBell, FaShieldAlt, FaSave } from "react-icons/fa";
import { useThemeContext } from "@/contexts/ThemeContext";

export default function ConfiguracoesPage() {
  const auth = useContext(AuthContext);
  const user = auth?.user;
  const { mode, toggleMode } = useThemeContext();
  const [activeTab, setActiveTab] = useState("perfil");

  const tabs = [
    { key: "perfil", label: "Perfil", icon: <FaUser /> },
    { key: "aparencia", label: "Aparência", icon: <FaPalette /> },
    { key: "notificacoes", label: "Notificações", icon: <FaBell /> },
    { key: "seguranca", label: "Segurança", icon: <FaShieldAlt /> },
  ];

  return (
    <GoBarberLayout>
      <div className="space-y-6">
        <h1 className="text-2xl font-bold text-[#1A1A2E]">Configurações</h1>

        <div className="flex flex-col lg:flex-row gap-6">
          {/* Sidebar */}
          <div className="lg:w-56 shrink-0">
            <nav className="gobarber-card p-2 space-y-1">
              {tabs.map((tab) => (
                <button
                  key={tab.key}
                  onClick={() => setActiveTab(tab.key)}
                  className={`w-full flex items-center gap-3 px-3 py-2.5 rounded-lg text-sm font-medium transition ${
                    activeTab === tab.key
                      ? "bg-[#1A1A2E] text-white"
                      : "text-gray-600 hover:bg-gray-100"
                  }`}
                >
                  {tab.icon} {tab.label}
                </button>
              ))}
            </nav>
          </div>

          {/* Conteúdo */}
          <div className="flex-1 gobarber-card">
            {activeTab === "perfil" && (
              <div className="space-y-6">
                <h2 className="text-lg font-semibold text-[#1A1A2E]">
                  Dados do Perfil
                </h2>
                <div className="flex items-center gap-4 pb-6 border-b border-gray-100">
                  <div className="w-16 h-16 rounded-full bg-gradient-to-br from-[#E94560] to-[#0F3460] flex items-center justify-center text-white text-2xl font-bold">
                    {user?.nome?.charAt(0)?.toUpperCase() || "U"}
                  </div>
                  <div>
                    <p className="font-semibold text-[#1A1A2E]">
                      {user?.nome || "Usuário"}
                    </p>
                    <p className="text-sm text-gray-500">{user?.email || ""}</p>
                    <span className="inline-block mt-1 px-2 py-0.5 bg-[#E94560]/10 text-[#E94560] rounded text-xs font-medium">
                      {user?.roles || "USER"}
                    </span>
                  </div>
                </div>
                <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                  <div>
                    <label className="block text-sm font-medium text-gray-700 mb-1">
                      Nome
                    </label>
                    <input
                      type="text"
                      defaultValue={user?.nome || ""}
                      className="gobarber-input"
                    />
                  </div>
                  <div>
                    <label className="block text-sm font-medium text-gray-700 mb-1">
                      E-mail
                    </label>
                    <input
                      type="email"
                      defaultValue={user?.email || ""}
                      className="gobarber-input"
                    />
                  </div>
                  <div>
                    <label className="block text-sm font-medium text-gray-700 mb-1">
                      Telefone
                    </label>
                    <input
                      type="tel"
                      placeholder="(00) 00000-0000"
                      className="gobarber-input"
                    />
                  </div>
                </div>
                <button
                  className="gobarber-btn-primary flex items-center gap-2"
                  onClick={() => toast.info("Função em desenvolvimento")}
                >
                  <FaSave /> Salvar Alterações
                </button>
              </div>
            )}

            {activeTab === "aparencia" && (
              <div className="space-y-6">
                <h2 className="text-lg font-semibold text-[#1A1A2E]">
                  Aparência
                </h2>
                <div className="flex items-center justify-between p-4 bg-gray-50 rounded-lg">
                  <div>
                    <p className="font-medium text-[#1A1A2E]">Modo escuro</p>
                    <p className="text-sm text-gray-500">
                      Alterne entre tema claro e escuro
                    </p>
                  </div>
                  <button
                    onClick={toggleMode}
                    className={`w-14 h-7 rounded-full relative transition-colors ${mode === "dark" ? "bg-[#E94560]" : "bg-gray-300"}`}
                  >
                    <span
                      className={`absolute top-0.5 w-6 h-6 rounded-full bg-white shadow transition-transform ${mode === "dark" ? "translate-x-7" : "translate-x-0.5"}`}
                    />
                  </button>
                </div>
              </div>
            )}

            {activeTab === "notificacoes" && (
              <div className="space-y-6">
                <h2 className="text-lg font-semibold text-[#1A1A2E]">
                  Notificações
                </h2>
                {[
                  {
                    label: "Novos agendamentos",
                    desc: "Receber alerta quando um cliente agendar",
                    default: true,
                  },
                  {
                    label: "Cancelamentos",
                    desc: "Receber alerta de cancelamentos",
                    default: true,
                  },
                  {
                    label: "Avaliações",
                    desc: "Receber alerta de novas avaliações",
                    default: false,
                  },
                  {
                    label: "Relatórios semanais",
                    desc: "Resumo semanal por e-mail",
                    default: false,
                  },
                ].map((item) => (
                  <div
                    key={item.label}
                    className="flex items-center justify-between p-4 bg-gray-50 rounded-lg"
                  >
                    <div>
                      <p className="font-medium text-[#1A1A2E]">{item.label}</p>
                      <p className="text-sm text-gray-500">{item.desc}</p>
                    </div>
                    <input
                      type="checkbox"
                      defaultChecked={item.default}
                      className="w-5 h-5 accent-[#E94560]"
                    />
                  </div>
                ))}
              </div>
            )}

            {activeTab === "seguranca" && (
              <div className="space-y-6">
                <h2 className="text-lg font-semibold text-[#1A1A2E]">
                  Segurança
                </h2>
                <div className="space-y-4">
                  <div>
                    <label className="block text-sm font-medium text-gray-700 mb-1">
                      Senha atual
                    </label>
                    <input type="password" className="gobarber-input" />
                  </div>
                  <div>
                    <label className="block text-sm font-medium text-gray-700 mb-1">
                      Nova senha
                    </label>
                    <input type="password" className="gobarber-input" />
                  </div>
                  <div>
                    <label className="block text-sm font-medium text-gray-700 mb-1">
                      Confirmar nova senha
                    </label>
                    <input type="password" className="gobarber-input" />
                  </div>
                </div>
                <button
                  className="gobarber-btn-primary flex items-center gap-2"
                  onClick={() => toast.info("Função em desenvolvimento")}
                >
                  <FaShieldAlt /> Alterar Senha
                </button>
              </div>
            )}
          </div>
        </div>
      </div>
    </GoBarberLayout>
  );
}
