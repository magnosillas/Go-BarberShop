"use client";

import React, { useEffect, useContext, useMemo } from "react";
import { useRouter, usePathname } from "next/navigation";
import { AuthContext } from "@/contexts/AuthContext";
import { useTokenExpiration } from "@/hooks/useTokenExpiration";

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

  return <>{children}</>;
}
