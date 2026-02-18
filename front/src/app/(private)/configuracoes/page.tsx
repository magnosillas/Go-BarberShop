"use client";

import GoBarberLayout from "@/components/Layout/GoBarberLayout";
import Modal from "@/components/Modal/Modal";
import React, { useState, useContext, useEffect } from "react";
import { AuthContext } from "@/contexts/AuthContext";
import { generica } from "@/api/api";
import { toast } from "react-toastify";
import {
  FaUser,
  FaPalette,
  FaBell,
  FaShieldAlt,
  FaSave,
  FaBan,
  FaEdit,
  FaPlus,
  FaToggleOn,
  FaToggleOff,
  FaEye,
  FaCheckCircle,
} from "react-icons/fa";
import { useThemeContext } from "@/contexts/ThemeContext";
import { useRoles } from "@/hooks/useRoles";

interface CancellationRule {
  id: number;
  cancelDeadlineHours: number;
  cancellationFeePercentage: number;
  noShowFeePercentage: number;
  maxCancellationsPerMonth: number;
  allowReschedule: boolean;
  rescheduleDeadlineHours: number;
  penaltyAfterMaxCancellations: boolean;
  blockDaysAfterMaxCancellations: number;
  active: boolean;
}

export default function ConfiguracoesPage() {
  const auth = useContext(AuthContext);
  const user = auth?.user;
  const { mode, toggleMode } = useThemeContext();
  const { isAdmin } = useRoles();
  const [activeTab, setActiveTab] = useState("perfil");

  // Cancellation Rules state
  const [rules, setRules] = useState<CancellationRule[]>([]);
  const [editingRule, setEditingRule] = useState<CancellationRule | null>(null);
  const [showRuleForm, setShowRuleForm] = useState(false);
  const [savingRule, setSavingRule] = useState(false);
  const [activeRule, setActiveRule] = useState<CancellationRule | null>(null);
  const [detailRule, setDetailRule] = useState<CancellationRule | null>(null);
  const [ruleForm, setRuleForm] = useState({
    cancelDeadlineHours: 24,
    cancellationFeePercentage: 0,
    noShowFeePercentage: 100,
    maxCancellationsPerMonth: 3,
    allowReschedule: true,
    rescheduleDeadlineHours: 12,
    penaltyAfterMaxCancellations: true,
    blockDaysAfterMaxCancellations: 7,
  });

  useEffect(() => {
    if (activeTab === "cancelamento" && isAdmin) {
      loadRules();
      loadActiveRule();
    }
  }, [activeTab, isAdmin]);

  // GET /cancellation-rules/active — load currently active rule
  async function loadActiveRule() {
    try {
      const res = await generica({ metodo: "GET", uri: "/cancellation-rules/active" });
      setActiveRule(res?.data || null);
    } catch {
      setActiveRule(null);
    }
  }

  // GET /cancellation-rules/{id} — view rule detail
  async function viewRuleDetail(id: number) {
    try {
      const res = await generica({ metodo: "GET", uri: `/cancellation-rules/${id}` });
      setDetailRule(res?.data || null);
    } catch {
      toast.error("Erro ao carregar detalhe da regra");
    }
  }

  async function loadRules() {
    try {
      const res = await generica({ metodo: "GET", uri: "/cancellation-rules" });
      setRules(Array.isArray(res?.data) ? res.data : []);
    } catch {
      setRules([]);
    }
  }

  function openNewRule() {
    setEditingRule(null);
    setRuleForm({
      cancelDeadlineHours: 24,
      cancellationFeePercentage: 0,
      noShowFeePercentage: 100,
      maxCancellationsPerMonth: 3,
      allowReschedule: true,
      rescheduleDeadlineHours: 12,
      penaltyAfterMaxCancellations: true,
      blockDaysAfterMaxCancellations: 7,
    });
    setShowRuleForm(true);
  }

  function openEditRule(rule: CancellationRule) {
    setEditingRule(rule);
    setRuleForm({
      cancelDeadlineHours: rule.cancelDeadlineHours,
      cancellationFeePercentage: rule.cancellationFeePercentage,
      noShowFeePercentage: rule.noShowFeePercentage,
      maxCancellationsPerMonth: rule.maxCancellationsPerMonth,
      allowReschedule: rule.allowReschedule,
      rescheduleDeadlineHours: rule.rescheduleDeadlineHours,
      penaltyAfterMaxCancellations: rule.penaltyAfterMaxCancellations,
      blockDaysAfterMaxCancellations: rule.blockDaysAfterMaxCancellations,
    });
    setShowRuleForm(true);
  }

  async function handleSaveRule(e: React.FormEvent) {
    e.preventDefault();
    setSavingRule(true);
    try {
      if (editingRule) {
        await generica({
          metodo: "PUT",
          uri: `/cancellation-rules/${editingRule.id}`,
          data: ruleForm,
        });
        toast.success("Regra atualizada!");
      } else {
        await generica({
          metodo: "POST",
          uri: "/cancellation-rules",
          data: ruleForm,
        });
        toast.success("Regra criada!");
      }
      setShowRuleForm(false);
      loadRules();
    } catch {
      toast.error("Erro ao salvar regra");
    } finally {
      setSavingRule(false);
    }
  }

  async function handleToggleRule(id: number) {
    try {
      await generica({ metodo: "POST", uri: `/cancellation-rules/${id}/toggle` });
      toast.success("Status alterado");
      loadRules();
    } catch {
      toast.error("Erro ao alterar status");
    }
  }

  async function handleDeleteRule(id: number) {
    if (!confirm("Excluir esta regra?")) return;
    try {
      await generica({ metodo: "DELETE", uri: `/cancellation-rules/${id}` });
      toast.success("Regra excluída");
      loadRules();
    } catch {
      toast.error("Erro ao excluir regra");
    }
  }

  const baseTabs = [
    { key: "perfil", label: "Perfil", icon: <FaUser /> },
    { key: "aparencia", label: "Aparência", icon: <FaPalette /> },
    { key: "notificacoes", label: "Notificações", icon: <FaBell /> },
    { key: "seguranca", label: "Segurança", icon: <FaShieldAlt /> },
  ];

  const tabs = isAdmin
    ? [...baseTabs, { key: "cancelamento", label: "Cancelamento", icon: <FaBan /> }]
    : baseTabs;

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

            {activeTab === "cancelamento" && isAdmin && (
              <div className="space-y-6">
                <div className="flex items-center justify-between">
                  <h2 className="text-lg font-semibold text-[#1A1A2E]">Regras de Cancelamento</h2>
                  <button onClick={openNewRule} className="gobarber-btn-primary flex items-center gap-2 text-sm">
                    <FaPlus /> Nova Regra
                  </button>
                </div>

                {/* Active Rule Banner */}
                {activeRule && (
                  <div className="p-4 bg-green-50 border border-green-200 rounded-lg">
                    <div className="flex items-center gap-2 mb-2">
                      <FaCheckCircle className="text-green-600" />
                      <span className="font-semibold text-green-800">Regra Ativa Atual</span>
                      <span className="text-xs text-gray-400">#{activeRule.id}</span>
                    </div>
                    <div className="grid grid-cols-2 md:grid-cols-4 gap-2 text-sm">
                      <div><span className="text-gray-500 text-xs">Antecedência:</span> <span className="font-medium">{activeRule.cancelDeadlineHours}h</span></div>
                      <div><span className="text-gray-500 text-xs">Multa:</span> <span className="font-medium">{activeRule.cancellationFeePercentage}%</span></div>
                      <div><span className="text-gray-500 text-xs">Máx./mês:</span> <span className="font-medium">{activeRule.maxCancellationsPerMonth}</span></div>
                      <div><span className="text-gray-500 text-xs">Reagendar:</span> <span className="font-medium">{activeRule.allowReschedule ? "Sim" : "Não"}</span></div>
                    </div>
                  </div>
                )}

                {showRuleForm && (
                  <form onSubmit={handleSaveRule} className="p-4 bg-gray-50 rounded-lg space-y-4 border border-gray-200">
                    <h3 className="font-medium text-[#1A1A2E]">{editingRule ? "Editar Regra" : "Nova Regra"}</h3>
                    <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                      <div>
                        <label className="block text-sm font-medium text-gray-700 mb-1">
                          Antecedência mínima para cancelar (horas)
                        </label>
                        <input
                          type="number" min={0} value={ruleForm.cancelDeadlineHours}
                          onChange={(e) => setRuleForm({ ...ruleForm, cancelDeadlineHours: Number(e.target.value) })}
                          className="gobarber-input" required
                        />
                      </div>
                      <div>
                        <label className="block text-sm font-medium text-gray-700 mb-1">
                          Multa por cancelamento (%)
                        </label>
                        <input
                          type="number" min={0} max={100} step={0.1} value={ruleForm.cancellationFeePercentage}
                          onChange={(e) => setRuleForm({ ...ruleForm, cancellationFeePercentage: Number(e.target.value) })}
                          className="gobarber-input" required
                        />
                      </div>
                      <div>
                        <label className="block text-sm font-medium text-gray-700 mb-1">
                          Multa por não comparecimento (%)
                        </label>
                        <input
                          type="number" min={0} max={100} step={0.1} value={ruleForm.noShowFeePercentage}
                          onChange={(e) => setRuleForm({ ...ruleForm, noShowFeePercentage: Number(e.target.value) })}
                          className="gobarber-input" required
                        />
                      </div>
                      <div>
                        <label className="block text-sm font-medium text-gray-700 mb-1">
                          Máx. cancelamentos por mês
                        </label>
                        <input
                          type="number" min={0} value={ruleForm.maxCancellationsPerMonth}
                          onChange={(e) => setRuleForm({ ...ruleForm, maxCancellationsPerMonth: Number(e.target.value) })}
                          className="gobarber-input" required
                        />
                      </div>
                      <div>
                        <label className="block text-sm font-medium text-gray-700 mb-1">
                          Antecedência para reagendamento (horas)
                        </label>
                        <input
                          type="number" min={0} value={ruleForm.rescheduleDeadlineHours}
                          onChange={(e) => setRuleForm({ ...ruleForm, rescheduleDeadlineHours: Number(e.target.value) })}
                          className="gobarber-input" required
                        />
                      </div>
                      <div>
                        <label className="block text-sm font-medium text-gray-700 mb-1">
                          Dias de bloqueio após exceder máx.
                        </label>
                        <input
                          type="number" min={0} value={ruleForm.blockDaysAfterMaxCancellations}
                          onChange={(e) => setRuleForm({ ...ruleForm, blockDaysAfterMaxCancellations: Number(e.target.value) })}
                          className="gobarber-input" required
                        />
                      </div>
                    </div>
                    <div className="flex flex-wrap gap-6">
                      <label className="flex items-center gap-2 text-sm">
                        <input
                          type="checkbox" checked={ruleForm.allowReschedule}
                          onChange={(e) => setRuleForm({ ...ruleForm, allowReschedule: e.target.checked })}
                          className="w-4 h-4 accent-[#E94560]"
                        />
                        Permitir reagendamento
                      </label>
                      <label className="flex items-center gap-2 text-sm">
                        <input
                          type="checkbox" checked={ruleForm.penaltyAfterMaxCancellations}
                          onChange={(e) => setRuleForm({ ...ruleForm, penaltyAfterMaxCancellations: e.target.checked })}
                          className="w-4 h-4 accent-[#E94560]"
                        />
                        Penalidade após exceder máx. cancelamentos
                      </label>
                    </div>
                    <div className="flex gap-3 pt-2">
                      <button type="submit" disabled={savingRule} className="gobarber-btn-primary flex items-center gap-2">
                        <FaSave /> {savingRule ? "Salvando..." : "Salvar"}
                      </button>
                      <button type="button" onClick={() => setShowRuleForm(false)} className="px-4 py-2 text-gray-600 hover:bg-gray-100 rounded-lg">
                        Cancelar
                      </button>
                    </div>
                  </form>
                )}

                {rules.length === 0 && !showRuleForm ? (
                  <div className="text-center py-8 text-gray-400">Nenhuma regra de cancelamento cadastrada</div>
                ) : (
                  <div className="space-y-3">
                    {rules.map((rule) => (
                      <div key={rule.id} className={`p-4 rounded-lg border ${rule.active ? "bg-white border-green-200" : "bg-gray-50 border-gray-200 opacity-60"}`}>
                        <div className="flex items-start justify-between mb-3">
                          <div className="flex items-center gap-2">
                            <span className={`px-2 py-0.5 text-xs rounded-full font-medium ${rule.active ? "bg-green-100 text-green-700" : "bg-gray-100 text-gray-500"}`}>
                              {rule.active ? "Ativa" : "Inativa"}
                            </span>
                            <span className="text-xs text-gray-400">#{rule.id}</span>
                          </div>
                          <div className="flex items-center gap-1">
                            <button onClick={() => viewRuleDetail(rule.id)} className="p-1.5 hover:bg-blue-50 rounded text-blue-600" title="Ver detalhe"><FaEye /></button>
                            <button onClick={() => handleToggleRule(rule.id)} className="p-1.5 hover:bg-gray-100 rounded text-lg" title={rule.active ? "Desativar" : "Ativar"}>
                              {rule.active ? <FaToggleOn className="text-green-600" /> : <FaToggleOff className="text-gray-400" />}
                            </button>
                            <button onClick={() => openEditRule(rule)} className="p-1.5 hover:bg-gray-100 rounded text-blue-600"><FaEdit /></button>
                            <button onClick={() => handleDeleteRule(rule.id)} className="p-1.5 hover:bg-red-50 rounded text-red-600"><FaBan /></button>
                          </div>
                        </div>
                        <div className="grid grid-cols-2 md:grid-cols-4 gap-3 text-sm">
                          <div>
                            <p className="text-gray-500 text-xs">Antecedência</p>
                            <p className="font-medium">{rule.cancelDeadlineHours}h</p>
                          </div>
                          <div>
                            <p className="text-gray-500 text-xs">Multa cancel.</p>
                            <p className="font-medium">{rule.cancellationFeePercentage}%</p>
                          </div>
                          <div>
                            <p className="text-gray-500 text-xs">Multa no-show</p>
                            <p className="font-medium">{rule.noShowFeePercentage}%</p>
                          </div>
                          <div>
                            <p className="text-gray-500 text-xs">Máx. p/mês</p>
                            <p className="font-medium">{rule.maxCancellationsPerMonth}</p>
                          </div>
                          <div>
                            <p className="text-gray-500 text-xs">Reagendamento</p>
                            <p className="font-medium">{rule.allowReschedule ? `Sim (${rule.rescheduleDeadlineHours}h)` : "Não"}</p>
                          </div>
                          <div>
                            <p className="text-gray-500 text-xs">Penalidade</p>
                            <p className="font-medium">{rule.penaltyAfterMaxCancellations ? `Bloq. ${rule.blockDaysAfterMaxCancellations}d` : "Não"}</p>
                          </div>
                        </div>
                      </div>
                    ))}
                  </div>
                )}
              </div>
            )}
          </div>
        </div>
      </div>

      {/* Modal Detalhe Regra (GET /cancellation-rules/{id}) */}
      <Modal isOpen={!!detailRule} onClose={() => setDetailRule(null)} title="Detalhe da Regra de Cancelamento">
        {detailRule && (
          <div className="space-y-4">
            <div className="flex items-center gap-2">
              <span className={`px-2 py-0.5 text-xs rounded-full font-medium ${detailRule.active ? "bg-green-100 text-green-700" : "bg-gray-100 text-gray-500"}`}>
                {detailRule.active ? "Ativa" : "Inativa"}
              </span>
              <span className="text-xs text-gray-400">ID: #{detailRule.id}</span>
            </div>
            <div className="grid grid-cols-2 gap-3">
              <div className="bg-gray-50 p-3 rounded-lg"><p className="text-xs text-gray-500">Antecedência para cancelar</p><p className="font-bold">{detailRule.cancelDeadlineHours}h</p></div>
              <div className="bg-gray-50 p-3 rounded-lg"><p className="text-xs text-gray-500">Multa cancelamento</p><p className="font-bold text-[#E94560]">{detailRule.cancellationFeePercentage}%</p></div>
              <div className="bg-gray-50 p-3 rounded-lg"><p className="text-xs text-gray-500">Multa no-show</p><p className="font-bold text-[#E94560]">{detailRule.noShowFeePercentage}%</p></div>
              <div className="bg-gray-50 p-3 rounded-lg"><p className="text-xs text-gray-500">Máx. cancelamentos/mês</p><p className="font-bold">{detailRule.maxCancellationsPerMonth}</p></div>
              <div className="bg-gray-50 p-3 rounded-lg"><p className="text-xs text-gray-500">Reagendamento</p><p className="font-bold">{detailRule.allowReschedule ? `Sim (${detailRule.rescheduleDeadlineHours}h)` : "Não"}</p></div>
              <div className="bg-gray-50 p-3 rounded-lg"><p className="text-xs text-gray-500">Penalidade</p><p className="font-bold">{detailRule.penaltyAfterMaxCancellations ? `Bloq. ${detailRule.blockDaysAfterMaxCancellations}d` : "Não"}</p></div>
            </div>
          </div>
        )}
      </Modal>
    </GoBarberLayout>
  );
}
