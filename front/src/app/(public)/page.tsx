"use client";

import React from "react";
import Link from "next/link";
import {
  FaCalendarCheck,
  FaCut,
  FaUsers,
  FaStar,
  FaChartLine,
  FaCreditCard,
} from "react-icons/fa";

const features = [
  {
    icon: <FaCalendarCheck className="text-3xl" />,
    title: "Agendamentos",
    desc: "Gestão completa de horários e serviços com notificações automáticas.",
  },
  {
    icon: <FaCut className="text-3xl" />,
    title: "Barbeiros",
    desc: "Cadastro de profissionais, especialidades e controle de agenda.",
  },
  {
    icon: <FaUsers className="text-3xl" />,
    title: "Clientes",
    desc: "Programa de fidelidade, preferências e histórico completo.",
  },
  {
    icon: <FaStar className="text-3xl" />,
    title: "Avaliações",
    desc: "Sistema de reviews multi-critério e ranking de barbeiros.",
  },
  {
    icon: <FaChartLine className="text-3xl" />,
    title: "Dashboard",
    desc: "Métricas em tempo real, KPIs e relatórios gerenciais.",
  },
  {
    icon: <FaCreditCard className="text-3xl" />,
    title: "Pagamentos",
    desc: "Múltiplos métodos: PIX, cartão, dinheiro. Cupons e promoções.",
  },
];

export default function HomePage() {
  return (
    <div className="min-h-screen bg-gray-50">
      {/* Header */}
      <header className="gobarber-gradient text-white">
        <nav className="max-w-7xl mx-auto px-6 py-4 flex items-center justify-between">
          <div className="flex items-center gap-3">
            <span className="text-3xl">💈</span>
            <h1 className="text-2xl font-bold tracking-tight">GoBarber</h1>
          </div>
          <div className="flex items-center gap-4">
            <Link
              href="/login"
              className="px-5 py-2 text-white/90 hover:text-white font-medium transition-colors"
            >
              Entrar
            </Link>
            <Link
              href="/register"
              className="px-5 py-2 bg-white text-[#1A1A2E] rounded-lg font-medium 
                         hover:bg-gray-100 transition-colors"
            >
              Cadastrar
            </Link>
          </div>
        </nav>

        {/* Hero */}
        <div className="max-w-7xl mx-auto px-6 py-20 text-center">
          <h2 className="text-5xl font-extrabold mb-6 leading-tight">
            Gerencie sua barbearia
            <br />
            <span className="text-[#E94560]">de forma inteligente</span>
          </h2>
          <p className="text-xl text-white/80 max-w-2xl mx-auto mb-10">
            Agendamentos, clientes, barbeiros, pagamentos e muito mais. Tudo em
            um único lugar para sua barbearia crescer.
          </p>
          <div className="flex items-center justify-center gap-4">
            <Link
              href="/register"
              className="gobarber-btn-secondary text-lg px-8 py-3"
            >
              Começar Agora
            </Link>
            <Link
              href="/login"
              className="gobarber-btn-outline border-white text-white hover:bg-white hover:text-[#1A1A2E] text-lg px-8 py-3"
            >
              Já tenho conta
            </Link>
          </div>
        </div>
      </header>

      {/* Features */}
      <section className="max-w-7xl mx-auto px-6 py-20">
        <div className="text-center mb-16">
          <h3 className="text-3xl font-bold text-[#1A1A2E] mb-4">
            Tudo que sua barbearia precisa
          </h3>
          <p className="text-gray-600 text-lg max-w-xl mx-auto">
            Funcionalidades completas para gerenciar e fazer seu negócio
            crescer.
          </p>
        </div>

        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-8">
          {features.map((feature, i) => (
            <div
              key={i}
              className="gobarber-card group hover:border-[#E94560]/30"
            >
              <div
                className="w-14 h-14 bg-[#1A1A2E]/10 rounded-xl flex items-center justify-center mb-4 
                              text-[#1A1A2E] group-hover:bg-[#E94560]/10 group-hover:text-[#E94560] transition-colors"
              >
                {feature.icon}
              </div>
              <h4 className="text-xl font-semibold mb-2 text-[#1A1A2E]">
                {feature.title}
              </h4>
              <p className="text-gray-600">{feature.desc}</p>
            </div>
          ))}
        </div>
      </section>

      {/* CTA */}
      <section className="bg-[#1A1A2E] text-white py-20">
        <div className="max-w-4xl mx-auto text-center px-6">
          <h3 className="text-3xl font-bold mb-4">
            Pronto para modernizar sua barbearia?
          </h3>
          <p className="text-white/70 text-lg mb-8">
            Cadastre-se gratuitamente e comece a gerenciar seus agendamentos
            hoje mesmo.
          </p>
          <Link
            href="/register"
            className="gobarber-btn-secondary text-lg px-10 py-3 inline-block"
          >
            Criar Conta Grátis
          </Link>
        </div>
      </section>

      {/* Footer */}
      <footer className="bg-[#16162A] text-white/60 py-8">
        <div className="max-w-7xl mx-auto px-6 text-center">
          <div className="flex items-center justify-center gap-2 mb-4">
            <span className="text-2xl">💈</span>
            <span className="text-white font-bold text-lg">GoBarber</span>
          </div>
          <p className="text-sm">
            © {new Date().getFullYear()} GoBarber — Sistema de Gerenciamento
            para Barbearias
          </p>
          <p className="text-xs mt-2">
            Desenvolvido por alunos da UFAPE — Universidade Federal do Agreste
            de Pernambuco
          </p>
        </div>
      </footer>
    </div>
  );
}
