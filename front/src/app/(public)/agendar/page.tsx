"use client";

import { useEffect } from "react";
import { useRouter } from "next/navigation";

/**
 * Redireciona /agendar para a landing page com busca de barbearias.
 * O usuário deve primeiro encontrar a barbearia desejada.
 */
export default function AgendarRedirect() {
  const router = useRouter();

  useEffect(() => {
    router.replace("/#buscar");
  }, [router]);

  return (
    <div className="min-h-screen flex items-center justify-center gobarber-gradient">
      <div className="text-center text-white">
        <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-white mx-auto mb-4" />
        <p className="text-white/70">Redirecionando...</p>
      </div>
    </div>
  );
}
