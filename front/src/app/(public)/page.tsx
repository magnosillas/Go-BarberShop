"use client";

import React, { useState } from "react";
import Link from "next/link";
import { useRouter } from "next/navigation";
import { genericaPublic } from "@/api/api";
import {
  FaCalendarCheck,
  FaCut,
  FaStar,
  FaSearch,
  FaClock,
  FaMapMarkerAlt,
  FaMobileAlt,
  FaBell,
  FaGift,
  FaChartLine,
  FaCreditCard,
  FaUsers,
  FaShieldAlt,
  FaArrowRight,
} from "react-icons/fa";

const clientFeatures = [
  {
    icon: <FaCalendarCheck className="text-2xl" />,
    title: "Agende Online 24/7",
    desc: "Escolha barbeiro, serviço e horário sem ligações ou filas. Tudo pelo celular.",
  },
  {
    icon: <FaClock className="text-2xl" />,
    title: "Sem Espera",
    desc: "Chegue no horário certo. Veja a disponibilidade em tempo real antes de agendar.",
  },
  {
    icon: <FaBell className="text-2xl" />,
    title: "Lembretes Automáticos",
    desc: "Receba notificações por email antes do seu horário. Nunca mais esqueça!",
  },
  {
    icon: <FaStar className="text-2xl" />,
    title: "Avalie seu Barbeiro",
    desc: "Deixe sua opinião e ajude outros clientes a escolherem o melhor profissional.",
  },
  {
    icon: <FaGift className="text-2xl" />,
    title: "Programa de Fidelidade",
    desc: "Acumule pontos a cada visita e ganhe descontos e benefícios exclusivos.",
  },
  {
    icon: <FaMobileAlt className="text-2xl" />,
    title: "Sem Cadastro Obrigatório",
    desc: "Agende usando apenas seu email e telefone. Rápido, simples e sem burocracia.",
  },
];

const ownerFeatures = [
  {
    icon: <FaChartLine className="text-2xl" />,
    title: "Dashboard Completo",
    desc: "KPIs, receita, agendamentos e métricas em tempo real para tomar decisões.",
  },
  {
    icon: <FaUsers className="text-2xl" />,
    title: "Gestão de Equipe",
    desc: "Barbeiros, secretárias, agendas, férias e folgas em um só lugar.",
  },
  {
    icon: <FaCreditCard className="text-2xl" />,
    title: "Controle Financeiro",
    desc: "Pagamentos via PIX, cartão e dinheiro. Comissões e relatórios financeiros.",
  },
  {
    icon: <FaShieldAlt className="text-2xl" />,
    title: "Segurança & Controle",
    desc: "Roles de acesso (admin, barbeiro, secretária), auditoria e logs completos.",
  },
];

const stats = [
  { value: "24/7", label: "Agendamento Online" },
  { value: "0%", label: "Taxa de No-Show" },
  { value: "+40%", label: "Aumento de Faturamento" },
  { value: "5★", label: "Avaliações de Clientes" },
];

export default function HomePage() {
  const router = useRouter();
  const [searchQuery, setSearchQuery] = useState("");
  const [searchResults, setSearchResults] = useState<{ idBarbershop: number; name: string; slug: string; description?: string; phone?: string; city?: string; state?: string }[]>([]);
  const [searched, setSearched] = useState(false);
  const [searching, setSearching] = useState(false);

  async function handleSearch(e: React.FormEvent) {
    e.preventDefault();
    const query = searchQuery.trim();
    if (!query) return;
    setSearching(true);
    setSearched(false);
    try {
      const res = await genericaPublic({
        metodo: "GET",
        uri: "/public/barbershops/search",
        params: { name: query },
      });
      const data = res?.data || [];
      setSearchResults(Array.isArray(data) ? data : []);
    } catch {
      setSearchResults([]);
    } finally {
      setSearching(false);
      setSearched(true);
    }
  }

  return (
    <div className="min-h-screen bg-gray-50">
      {/* Header */}
      <header className="gobarber-gradient text-white">
        <nav className="max-w-7xl mx-auto px-4 sm:px-6 py-3 sm:py-4 flex items-center justify-between">
          <div className="flex items-center gap-2 sm:gap-3">
            <span className="text-2xl sm:text-3xl">💈</span>
            <h1 className="text-lg sm:text-2xl font-bold tracking-tight">GoBarber</h1>
          </div>
          <div className="flex items-center gap-2 sm:gap-4">
            <a
              href="#buscar"
              className="px-3 sm:px-5 py-2 text-white/90 hover:text-white font-medium transition-colors text-sm sm:text-base"
            >
              Buscar Barbearia
            </a>
            <Link
              href="/login"
              className="px-3 sm:px-5 py-2 text-white/90 hover:text-white font-medium transition-colors text-sm sm:text-base hidden sm:inline-block"
            >
              Entrar
            </Link>
            <Link
              href="/register"
              className="px-3 sm:px-5 py-2 bg-white text-[#1A1A2E] rounded-lg font-medium 
                         hover:bg-gray-100 transition-colors text-sm sm:text-base"
            >
              <span className="sm:hidden">Entrar</span>
              <span className="hidden sm:inline">Cadastrar</span>
            </Link>
          </div>
        </nav>

        {/* Hero — foco no cliente */}
        <div className="max-w-7xl mx-auto px-4 sm:px-6 py-12 sm:py-24 text-center">
          <div className="inline-flex items-center gap-2 bg-white/10 backdrop-blur rounded-full px-4 py-1.5 text-sm text-white/90 mb-6">
            <FaCut className="text-[#E94560]" />
            <span>A plataforma #1 de agendamento para barbearias</span>
          </div>
          <h2 className="text-3xl sm:text-5xl lg:text-6xl font-extrabold mb-4 sm:mb-6 leading-tight">
            Encontre sua barbearia.
            <br />
            <span className="text-[#E94560]">Agende em segundos.</span>
          </h2>
          <p className="text-base sm:text-xl text-white/80 max-w-2xl mx-auto mb-8 sm:mb-10">
            Chega de ligar, mandar mensagem e esperar resposta. Escolha o barbeiro,
            o serviço e o horário — tudo online e sem burocracia.
          </p>

          {/* Consultar Barbearia — CTA principal */}
          <form
            id="buscar"
            onSubmit={handleSearch}
            className="max-w-lg mx-auto flex flex-col sm:flex-row gap-3 mb-6"
          >
            <div className="relative flex-1">
              <FaSearch className="absolute left-4 top-1/2 -translate-y-1/2 text-gray-400" />
              <input
                type="text"
                value={searchQuery}
                onChange={(e) => setSearchQuery(e.target.value)}
                placeholder="Nome da barbearia (ex: GoBarber)"
                className="w-full pl-11 pr-4 py-3 sm:py-4 rounded-xl text-gray-800 text-base focus:outline-none focus:ring-2 focus:ring-[#E94560] shadow-lg"
              />
            </div>
            <button
              type="submit"
              disabled={searching}
              className="px-6 sm:px-8 py-3 sm:py-4 bg-[#E94560] text-white rounded-xl font-bold text-base hover:bg-[#d73a52] transition-colors shadow-lg flex items-center justify-center gap-2 disabled:opacity-50"
            >
              <FaMapMarkerAlt />
              {searching ? "Buscando..." : "Buscar Barbearia"}
            </button>
          </form>

          {/* Resultados da busca */}
          {searched && (
            <div className="max-w-lg mx-auto mb-8">
              {searchResults.length > 0 ? (
                <div className="bg-white rounded-xl shadow-lg overflow-hidden">
                  {searchResults.map((shop) => (
                    <Link
                      key={shop.idBarbershop}
                      href={`/b/${shop.slug}`}
                      className="flex items-center justify-between px-5 py-4 hover:bg-gray-50 transition-colors border-b last:border-b-0"
                    >
                      <div className="text-left">
                        <p className="font-semibold text-gray-800">{shop.name}</p>
                        {shop.description && (
                          <p className="text-sm text-gray-500 line-clamp-1">{shop.description}</p>
                        )}
                      </div>
                      <FaArrowRight className="text-[#E94560] shrink-0 ml-3" />
                    </Link>
                  ))}
                </div>
              ) : (
                <p className="text-white/70 text-sm">
                  Nenhuma barbearia encontrada. Tente outro nome.
                </p>
              )}
            </div>
          )}

          <div className="flex flex-col sm:flex-row items-center justify-center gap-3 sm:gap-6 text-sm text-white/60">
            <Link href="/login" className="flex items-center gap-2 hover:text-white transition-colors">
              <FaArrowRight className="text-[#E94560]" />
              Já tenho conta — Entrar
            </Link>
          </div>
        </div>
      </header>

      {/* Stats */}
      <section className="bg-white border-b">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 py-8 sm:py-12">
          <div className="grid grid-cols-2 lg:grid-cols-4 gap-6 sm:gap-8">
            {stats.map((stat, i) => (
              <div key={i} className="text-center">
                <p className="text-2xl sm:text-4xl font-extrabold text-[#E94560]">{stat.value}</p>
                <p className="text-xs sm:text-sm text-gray-500 mt-1">{stat.label}</p>
              </div>
            ))}
          </div>
        </div>
      </section>

      {/* Como Funciona */}
      <section className="max-w-7xl mx-auto px-4 sm:px-6 py-12 sm:py-20">
        <div className="text-center mb-10 sm:mb-16">
          <h3 className="text-2xl sm:text-3xl font-bold text-[#1A1A2E] mb-3 sm:mb-4">
            Como funciona?
          </h3>
          <p className="text-gray-600 text-base sm:text-lg max-w-xl mx-auto">
            Três passos para o corte perfeito
          </p>
        </div>

        <div className="grid grid-cols-1 md:grid-cols-3 gap-6 sm:gap-10">
          {[
            { step: "1", title: "Encontre", desc: "Busque a barbearia pelo nome ou acesse o link direto. Veja os barbeiros e serviços disponíveis.", icon: <FaSearch /> },
            { step: "2", title: "Escolha", desc: "Selecione o barbeiro, os serviços desejados e o melhor horário na agenda em tempo real.", icon: <FaCut /> },
            { step: "3", title: "Confirme", desc: "Informe seu email e telefone para confirmar. Sem cadastro complexo, sem senha.", icon: <FaCalendarCheck /> },
          ].map((item, i) => (
            <div key={i} className="text-center group">
              <div className="w-16 h-16 sm:w-20 sm:h-20 mx-auto mb-4 bg-[#1A1A2E] rounded-2xl flex items-center justify-center text-white text-2xl sm:text-3xl group-hover:bg-[#E94560] transition-colors">
                {item.icon}
              </div>
              <div className="text-xs font-bold text-[#E94560] mb-1">PASSO {item.step}</div>
              <h4 className="text-lg sm:text-xl font-bold text-[#1A1A2E] mb-2">{item.title}</h4>
              <p className="text-gray-600 text-sm sm:text-base max-w-xs mx-auto">{item.desc}</p>
            </div>
          ))}
        </div>

        <div className="text-center mt-10 sm:mt-14">
          <a
            href="#buscar"
            className="gobarber-btn-primary text-base sm:text-lg px-8 sm:px-10 py-3 inline-block"
          >
            Buscar Barbearia
          </a>
        </div>
      </section>

      {/* Features para Clientes */}
      <section className="bg-gray-100 py-12 sm:py-20">
        <div className="max-w-7xl mx-auto px-4 sm:px-6">
          <div className="text-center mb-10 sm:mb-16">
            <div className="inline-flex items-center gap-2 bg-[#E94560]/10 text-[#E94560] rounded-full px-4 py-1.5 text-sm font-semibold mb-4">
              <FaStar /> Para Clientes
            </div>
            <h3 className="text-2xl sm:text-3xl font-bold text-[#1A1A2E] mb-3 sm:mb-4">
              A melhor experiência de agendamento
            </h3>
            <p className="text-gray-600 text-base sm:text-lg max-w-xl mx-auto">
              Pensado para ser rápido, fácil e sem complicação para quem quer apenas cortar o cabelo.
            </p>
          </div>

          <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-4 sm:gap-6">
            {clientFeatures.map((feature, i) => (
              <div key={i} className="gobarber-card group hover:border-[#E94560]/30 bg-white">
                <div className="w-12 h-12 bg-[#E94560]/10 rounded-xl flex items-center justify-center mb-3 text-[#E94560] group-hover:bg-[#E94560] group-hover:text-white transition-colors">
                  {feature.icon}
                </div>
                <h4 className="text-base sm:text-lg font-semibold mb-1.5 text-[#1A1A2E]">{feature.title}</h4>
                <p className="text-gray-600 text-sm">{feature.desc}</p>
              </div>
            ))}
          </div>
        </div>
      </section>

      {/* Features para Donos de Barbearia */}
      <section className="max-w-7xl mx-auto px-4 sm:px-6 py-12 sm:py-20">
        <div className="text-center mb-10 sm:mb-16">
          <div className="inline-flex items-center gap-2 bg-[#1A1A2E]/10 text-[#1A1A2E] rounded-full px-4 py-1.5 text-sm font-semibold mb-4">
            <FaChartLine /> Para Donos de Barbearia
          </div>
          <h3 className="text-2xl sm:text-3xl font-bold text-[#1A1A2E] mb-3 sm:mb-4">
            Gerencie seu negócio de forma inteligente
          </h3>
          <p className="text-gray-600 text-base sm:text-lg max-w-xl mx-auto">
            Reduza no-shows em até 80%, aumente o faturamento e tenha controle total da sua barbearia.
          </p>
        </div>

        <div className="grid grid-cols-1 sm:grid-cols-2 gap-4 sm:gap-6">
          {ownerFeatures.map((feature, i) => (
            <div key={i} className="gobarber-card group hover:border-[#1A1A2E]/30">
              <div className="w-12 h-12 bg-[#1A1A2E]/10 rounded-xl flex items-center justify-center mb-3 text-[#1A1A2E] group-hover:bg-[#1A1A2E] group-hover:text-white transition-colors">
                {feature.icon}
              </div>
              <h4 className="text-base sm:text-lg font-semibold mb-1.5 text-[#1A1A2E]">{feature.title}</h4>
              <p className="text-gray-600 text-sm">{feature.desc}</p>
            </div>
          ))}
        </div>

        <div className="text-center mt-10 sm:mt-14 flex flex-col sm:flex-row items-center justify-center gap-3 sm:gap-4">
          <Link
            href="/register"
            className="gobarber-btn-primary text-base px-8 py-3 w-full sm:w-auto text-center"
          >
            Cadastrar minha Barbearia
          </Link>
          <Link
            href="/login"
            className="gobarber-btn-outline text-base px-8 py-3 w-full sm:w-auto text-center"
          >
            Já tenho conta
          </Link>
        </div>
      </section>

      {/* CTA Final */}
      <section className="bg-[#1A1A2E] text-white py-12 sm:py-20">
        <div className="max-w-4xl mx-auto text-center px-4 sm:px-6">
          <h3 className="text-2xl sm:text-4xl font-bold mb-3 sm:mb-4">
            Pronto para agendar seu próximo corte?
          </h3>
          <p className="text-white/70 text-base sm:text-lg mb-6 sm:mb-8 max-w-2xl mx-auto">
            Encontre a barbearia mais perto de você, escolha o barbeiro e agende em menos de 1 minuto.
            Sem cadastro complicado — basta email e telefone.
          </p>
          <div className="flex flex-col sm:flex-row items-center justify-center gap-3 sm:gap-4">
            <a
              href="#buscar"
              className="gobarber-btn-secondary text-base sm:text-lg px-8 sm:px-10 py-3 w-full sm:w-auto text-center inline-block"
            >
              Buscar Barbearia
            </a>
            <Link
              href="/register"
              className="px-8 sm:px-10 py-3 border-2 border-white/30 text-white rounded-xl font-bold hover:bg-white/10 transition-colors w-full sm:w-auto text-center text-base sm:text-lg"
            >
              Sou Dono de Barbearia
            </Link>
          </div>
        </div>
      </section>

      {/* Footer */}
      <footer className="bg-[#16162A] text-white/60 py-6 sm:py-8">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 text-center">
          <div className="flex items-center justify-center gap-2 mb-3 sm:mb-4">
            <span className="text-xl sm:text-2xl">💈</span>
            <span className="text-white font-bold text-base sm:text-lg">GoBarber</span>
          </div>
          <p className="text-xs sm:text-sm">
            © {new Date().getFullYear()} GoBarber — Plataforma de Agendamento
            para Barbearias
          </p>
          <p className="text-xs mt-1 sm:mt-2">
            Desenvolvido por alunos da UFAPE — Universidade Federal do Agreste
            de Pernambuco
          </p>
        </div>
      </footer>
    </div>
  );
}
