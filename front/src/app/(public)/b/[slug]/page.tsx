"use client";

import React, { useEffect, useState } from "react";
import Link from "next/link";
import { useParams } from "next/navigation";
import { genericaPublic } from "@/api/api";
import { FaCut, FaStar, FaCalendarAlt, FaClock, FaPhone, FaTimes } from "react-icons/fa";

interface BarberService {
  id: number;
  name: string;
  value?: number;
}

interface BarberInfo {
  idBarber: number;
  name: string;
  contato?: string;
  start?: string;
  end?: string;
  services?: BarberService[];
}

export default function BarbershopLandingPage() {
  const params = useParams();
  const slug = params?.slug as string;

  const [barbers, setBarbers] = useState<BarberInfo[]>([]);
  const [loading, setLoading] = useState(true);
  const [selectedBarber, setSelectedBarber] = useState<BarberInfo | null>(null);
  const [loadingBarber, setLoadingBarber] = useState(false);

  const shopName = slug
    ? slug.replace(/-/g, " ").replace(/\b\w/g, (c) => c.toUpperCase())
    : "GoBarber";

  useEffect(() => {
    loadBarbers();
  }, [slug]);

  async function loadBarbers() {
    try {
      const res = await genericaPublic({
        metodo: "GET",
        uri: `/public/barbershops/${slug}/barbers`,
      });
      const data = res?.data?.content || res?.data || [];
      setBarbers(Array.isArray(data) ? data : []);
    } catch {
      //
    } finally {
      setLoading(false);
    }
  }

  // GET /public/barbers/{id} — fetch barber detail
  async function viewBarberDetail(id: number) {
    setLoadingBarber(true);
    try {
      const res = await genericaPublic({ metodo: "GET", uri: `/public/barbers/${id}` });
      setSelectedBarber(res?.data || null);
    } catch {
      // fallback: use local
    } finally {
      setLoadingBarber(false);
    }
  }

  return (
    <div className="min-h-screen bg-gray-50">
      {/* Hero */}
      <header className="gobarber-gradient text-white">
        <nav className="max-w-7xl mx-auto px-4 sm:px-6 py-3 sm:py-4 flex items-center justify-between">
          <div className="flex items-center gap-2 sm:gap-3 min-w-0">
            <span className="text-2xl sm:text-3xl shrink-0">💈</span>
            <h1 className="text-lg sm:text-2xl font-bold tracking-tight truncate">{shopName}</h1>
          </div>
          <div className="flex items-center gap-2 sm:gap-4 shrink-0">
            <Link
              href="/login"
              className="px-3 sm:px-5 py-2 text-white/90 hover:text-white font-medium transition-colors text-sm sm:text-base"
            >
              Entrar
            </Link>
            <Link
              href={`/b/${slug}/agendar`}
              className="px-3 sm:px-6 py-2 bg-white text-[#1A1A2E] rounded-lg font-semibold hover:bg-gray-100 transition-colors text-sm sm:text-base"
            >
              Agendar
            </Link>
          </div>
        </nav>

        <div className="max-w-7xl mx-auto px-4 sm:px-6 py-12 sm:py-20 text-center">
          <h2 className="text-3xl sm:text-5xl font-extrabold leading-tight mb-4 sm:mb-6">
            Bem-vindo à{" "}
            <span className="text-[#E94560]">{shopName}</span>
          </h2>
          <p className="text-base sm:text-xl text-white/70 max-w-2xl mx-auto mb-8 sm:mb-10">
            Agende seu corte de forma rápida e prática. Escolha o barbeiro, serviço e horário que mais combina com você.
          </p>
          <Link
            href={`/b/${slug}/agendar`}
            className="inline-block px-8 sm:px-10 py-3 sm:py-4 bg-[#E94560] text-white rounded-xl font-bold text-base sm:text-lg hover:bg-[#d73a52] transition-colors shadow-lg"
          >
            <FaCalendarAlt className="inline mr-2" />
            Agendar Agora
          </Link>
        </div>
      </header>

      {/* Barbeiros */}
      <section className="max-w-7xl mx-auto px-4 sm:px-6 py-10 sm:py-16">
        <h3 className="text-2xl sm:text-3xl font-bold text-[#1A1A2E] text-center mb-8 sm:mb-12">
          Nossos Barbeiros
        </h3>
        {loading ? (
          <div className="text-center py-10 text-gray-400">
            <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-[#E94560] mx-auto mb-3" />
            Carregando...
          </div>
        ) : barbers.length === 0 ? (
          <p className="text-center text-gray-400">Nenhum barbeiro cadastrado</p>
        ) : (
          <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-4 sm:gap-6">
            {barbers.map((barber) => (
              <div key={barber.idBarber} className="gobarber-card hover:shadow-lg transition-shadow cursor-pointer" onClick={() => viewBarberDetail(barber.idBarber)}>
                <div className="flex items-center gap-3 sm:gap-4 mb-4">
                  <div className="w-14 h-14 sm:w-16 sm:h-16 bg-[#1A1A2E] rounded-full flex items-center justify-center text-white text-xl sm:text-2xl shrink-0">
                    <FaCut />
                  </div>
                  <div className="min-w-0">
                    <h4 className="font-bold text-lg text-[#1A1A2E] truncate">{barber.name}</h4>
                    {barber.start && barber.end && (
                      <p className="text-sm text-gray-500 flex items-center gap-1">
                        <FaClock className="text-xs" /> {barber.start} - {barber.end}
                      </p>
                    )}
                    {barber.contato && (
                      <p className="text-xs text-gray-400 flex items-center gap-1 mt-0.5">
                        <FaPhone className="text-xs" /> {barber.contato}
                      </p>
                    )}
                  </div>
                </div>
                {barber.services && barber.services.length > 0 && (
                  <div className="flex flex-wrap gap-1.5 mb-4">
                    {barber.services.slice(0, 4).map((s) => (
                      <span
                        key={s.id}
                        className="px-2 py-0.5 bg-gray-100 text-gray-600 rounded text-xs"
                      >
                        {s.name}
                      </span>
                    ))}
                    {barber.services.length > 4 && (
                      <span className="px-2 py-0.5 text-gray-400 text-xs">
                        +{barber.services.length - 4} mais
                      </span>
                    )}
                  </div>
                )}
                <Link
                  href={`/b/${slug}/agendar`}
                  className="block text-center gobarber-btn-primary w-full py-2 text-sm"
                >
                  Agendar com {barber.name.split(" ")[0]}
                </Link>
              </div>
            ))}
          </div>
        )}
      </section>

      {/* CTA */}
      <section className="gobarber-gradient py-10 sm:py-16 text-center">
        <h3 className="text-2xl sm:text-3xl font-bold text-white mb-4">
          Pronto para renovar o visual?
        </h3>
        <p className="text-white/60 mb-6 sm:mb-8 text-sm sm:text-base px-4">
          Agende agora e garanta o melhor horário para você.
        </p>
        <Link
          href={`/b/${slug}/agendar`}
          className="inline-block px-8 sm:px-10 py-3 bg-[#E94560] text-white rounded-xl font-bold hover:bg-[#d73a52] transition-colors"
        >
          Agendar Agora
        </Link>
      </section>

      {/* Footer */}
      <footer className="bg-[#1A1A2E] text-white/50 text-center py-6 text-sm">
        <p>© {new Date().getFullYear()} {shopName}. Powered by GoBarber.</p>
      </footer>

      {/* Barber Detail Modal (GET /public/barbers/{id}) */}
      {selectedBarber && (
        <div className="fixed inset-0 z-50 flex items-center justify-center bg-black/50 p-4" onClick={() => setSelectedBarber(null)}>
          <div className="bg-white rounded-2xl p-6 max-w-md w-full relative" onClick={(e) => e.stopPropagation()}>
            <button onClick={() => setSelectedBarber(null)} className="absolute top-3 right-3 text-gray-400 hover:text-gray-600"><FaTimes /></button>
            {loadingBarber ? (
              <div className="flex justify-center py-6"><span className="animate-spin rounded-full h-8 w-8 border-b-2 border-[#E94560]" /></div>
            ) : (
              <div className="space-y-4">
                <div className="flex items-center gap-4">
                  <div className="w-16 h-16 bg-[#1A1A2E] rounded-full flex items-center justify-center text-white text-2xl"><FaCut /></div>
                  <div>
                    <h3 className="font-bold text-xl text-[#1A1A2E]">{selectedBarber.name}</h3>
                    {selectedBarber.contato && <p className="text-sm text-gray-500 flex items-center gap-1"><FaPhone className="text-xs" /> {selectedBarber.contato}</p>}
                    {selectedBarber.start && selectedBarber.end && (
                      <p className="text-sm text-gray-500 flex items-center gap-1"><FaClock className="text-xs" /> {selectedBarber.start} - {selectedBarber.end}</p>
                    )}
                  </div>
                </div>
                {selectedBarber.services && selectedBarber.services.length > 0 && (
                  <div>
                    <h4 className="text-sm font-semibold text-gray-700 mb-2">Serviços</h4>
                    <div className="space-y-1">
                      {selectedBarber.services.map((s) => (
                        <div key={s.id} className="flex justify-between items-center py-1.5 px-2 bg-gray-50 rounded">
                          <span className="text-sm">{s.name}</span>
                          {s.value && <span className="text-sm font-bold text-[#E94560]">R$ {s.value.toFixed(2)}</span>}
                        </div>
                      ))}
                    </div>
                  </div>
                )}
                <Link href={`/b/${slug}/agendar`} className="block text-center gobarber-btn-primary w-full py-2">
                  <FaCalendarAlt className="inline mr-2" /> Agendar com {selectedBarber.name.split(" ")[0]}
                </Link>
              </div>
            )}
          </div>
        </div>
      )}
    </div>
  );
}
