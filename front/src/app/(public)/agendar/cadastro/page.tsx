"use client";

import { useEffect } from "react";
import { useRouter } from "next/navigation";

/**
 * Redireciona /agendar/cadastro → landing page com busca de barbearias
 */
export default function CadastroRedirect() {
  const router = useRouter();

  useEffect(() => {
    router.replace("/#buscar");
  }, [router]);

  return (
    <div className="min-h-screen flex items-center justify-center gobarber-gradient px-4">
      <div className="text-center text-white">
        <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-white mx-auto mb-4" />
        <p className="text-white/70">Redirecionando...</p>
      </div>
    </div>
  );
}
