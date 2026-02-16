"use client";

import React, { useState, useContext } from "react";
import { useRouter } from "next/navigation";
import Link from "next/link";
import { AuthContext } from "@/contexts/AuthContext";
import { toast } from "react-toastify";

export default function LoginPage() {
  const router = useRouter();
  const auth = useContext(AuthContext);
  const [email, setEmail] = useState("");
  const [senha, setSenha] = useState("");
  const [loading, setLoading] = useState(false);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!email || !senha) {
      toast.error("Preencha todos os campos");
      return;
    }
    setLoading(true);
    try {
      await auth?.login({ email, senha });
      toast.success("Login realizado com sucesso!");
      router.push("/dashboard");
    } catch (err: any) {
      toast.error(err?.message || "Erro ao fazer login");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="min-h-screen flex items-center justify-center gobarber-gradient px-4">
      <div className="w-full max-w-md">
        {/* Logo */}
        <div className="text-center mb-8">
          <span className="text-6xl">💈</span>
          <h1 className="text-3xl font-bold text-white mt-4">GoBarber</h1>
          <p className="text-white/60 mt-2">Entre na sua conta</p>
        </div>

        {/* Card de Login */}
        <div className="bg-white rounded-2xl shadow-2xl p-8">
          <form onSubmit={handleSubmit} className="space-y-5">
            <div>
              <label className="gobarber-label">Email ou Login</label>
              <input
                type="text"
                value={email}
                onChange={(e) => setEmail(e.target.value)}
                placeholder="seu@email.com"
                className="gobarber-input"
                autoComplete="email"
              />
            </div>

            <div>
              <label className="gobarber-label">Senha</label>
              <input
                type="password"
                value={senha}
                onChange={(e) => setSenha(e.target.value)}
                placeholder="••••••••"
                className="gobarber-input"
                autoComplete="current-password"
              />
            </div>

            <button
              type="submit"
              disabled={loading}
              className="gobarber-btn-primary w-full py-3 text-lg disabled:opacity-50 disabled:cursor-not-allowed"
            >
              {loading ? (
                <span className="flex items-center justify-center gap-2">
                  <span className="animate-spin rounded-full h-5 w-5 border-b-2 border-white" />
                  Entrando...
                </span>
              ) : (
                "Entrar"
              )}
            </button>
          </form>

          <div className="mt-6 text-center">
            <p className="text-gray-600 text-sm">
              Não tem uma conta?{" "}
              <Link
                href="/register"
                className="text-[#E94560] font-medium hover:underline"
              >
                Cadastre-se
              </Link>
            </p>
          </div>
        </div>

        {/* Voltar */}
        <div className="text-center mt-6">
          <Link
            href="/"
            className="text-white/60 text-sm hover:text-white transition-colors"
          >
            ← Voltar para o início
          </Link>
        </div>
      </div>
    </div>
  );
}
