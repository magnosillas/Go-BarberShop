"use client";

import React, { useEffect, useContext } from "react";
import { useRouter, usePathname } from "next/navigation";
import { AuthContext } from "@/contexts/AuthContext";
import { useTokenExpiration } from "@/hooks/useTokenExpiration";
import type { RoleKey } from "@/types/menu";

// Mapeamento de rotas para roles permitidas
const routePermissions: Record<string, RoleKey[]> = {
  "/dashboard": ["ADMIN", "SECRETARY", "BARBER"],
  "/agendamentos": ["ADMIN", "SECRETARY", "BARBER"],
  "/barbeiros": ["ADMIN", "SECRETARY"],
  "/clientes": ["ADMIN", "SECRETARY"],
  "/servicos": ["ADMIN"],
  "/produtos": ["ADMIN"],
  "/avaliacoes": ["ADMIN", "BARBER", "CLIENT"],
  "/pagamentos": ["ADMIN", "SECRETARY"],
  "/promocoes": ["ADMIN"],
  "/lista-espera": ["ADMIN", "SECRETARY"],
  "/notificacoes": ["ADMIN", "SECRETARY", "BARBER", "CLIENT"],
  "/relatorios": ["ADMIN"],
  "/configuracoes": ["ADMIN", "SECRETARY", "BARBER", "CLIENT"],
  "/meus-agendamentos": ["CLIENT"],
  "/secretarias": ["ADMIN"],
  "/agenda-barbeiro": ["ADMIN", "BARBER"],
  "/loja": ["CLIENT", "ADMIN"],
  "/barbearias": ["ADMIN"],
  "/enderecos": ["ADMIN", "SECRETARY"],
};

function LoadingScreen() {
  return (
    <div className="min-h-screen flex items-center justify-center bg-gray-100">
      <div className="text-center">
        <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-[#1A1A2E] mx-auto mb-4" />
        <p className="text-gray-600">Verificando autenticação...</p>
      </div>
    </div>
  );
}

function AccessDenied() {
  return (
    <div className="min-h-screen flex items-center justify-center bg-gray-100">
      <div className="text-center gobarber-card max-w-md mx-auto">
        <div className="text-6xl mb-4">🚫</div>
        <h2 className="text-xl font-bold text-[#1A1A2E] mb-2">Acesso Negado</h2>
        <p className="text-gray-500 mb-6">Você não tem permissão para acessar esta página.</p>
        <a href="/dashboard" className="gobarber-btn-primary inline-block">Voltar ao Início</a>
      </div>
    </div>
  );
}

export default function PrivateLayout({
  children,
}: {
  children: React.ReactNode;
}) {
  const router = useRouter();
  const pathname = usePathname();
  const authContext = useContext(AuthContext);
  useTokenExpiration({ onExpired: () => router.push("/login") });

  const status = authContext?.status ?? "loading";
  const isLoading = status === "loading";
  const isAuthenticated = status === "authenticated";
  const userRoles = authContext?.user?.roles || [];

  useEffect(() => {
    if (!isLoading && !isAuthenticated) {
      router.push("/login");
    }
  }, [isLoading, isAuthenticated, router]);

  if (isLoading) {
    return <LoadingScreen />;
  }

  if (!isAuthenticated) {
    return <LoadingScreen />;
  }

  // Verificar permissão por role
  if (pathname) {
    const matchedRoute = Object.keys(routePermissions).find(
      (route) => pathname === route || pathname.startsWith(route + "/")
    );
    if (matchedRoute) {
      const allowedRoles = routePermissions[matchedRoute];
      const hasAccess = userRoles.includes("ADMIN" as RoleKey) || 
        allowedRoles.some((r) => userRoles.includes(r));
      if (!hasAccess) {
        return <AccessDenied />;
      }
    }
  }

  return <>{children}</>;
}
