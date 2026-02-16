"use client";

import React, { useState } from "react";
import { useRouter } from "next/navigation";
import Link from "next/link";
import { genericaApiAuth } from "@/api/api";
import { toast } from "react-toastify";

export default function RegisterPage() {
  const router = useRouter();
  const [form, setForm] = useState({
    name: "",
    email: "",
    login: "",
    password: "",
    confirmPassword: "",
  });
  const [loading, setLoading] = useState(false);

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    if (!form.name || !form.email || !form.login || !form.password) {
      toast.error("Preencha todos os campos obrigatórios");
      return;
    }

    if (form.password !== form.confirmPassword) {
      toast.error("As senhas não conferem");
      return;
    }

    if (form.password.length < 6) {
      toast.error("A senha deve ter pelo menos 6 caracteres");
      return;
    }

    setLoading(true);
    try {
      const response = await genericaApiAuth({
        metodo: "POST",
        uri: "/register",
        data: {
          name: form.name,
          email: form.email,
          login: form.login,
          password: form.password,
        },
      });

      if (response && response.status < 400) {
        toast.success("Conta criada com sucesso! Faça login.");
        router.push("/login");
      } else {
        const msg =
          response?.data?.message || response?.data || "Erro ao criar conta";
        toast.error(typeof msg === "string" ? msg : "Erro ao criar conta");
      }
    } catch (err: any) {
      toast.error(err?.message || "Erro ao criar conta");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="min-h-screen flex items-center justify-center gobarber-gradient px-4 py-10">
      <div className="w-full max-w-md">
        {/* Logo */}
        <div className="text-center mb-8">
          <span className="text-6xl">💈</span>
          <h1 className="text-3xl font-bold text-white mt-4">GoBarber</h1>
          <p className="text-white/60 mt-2">Crie sua conta</p>
        </div>

        {/* Card de Registro */}
        <div className="bg-white rounded-2xl shadow-2xl p-8">
          <form onSubmit={handleSubmit} className="space-y-4">
            <div>
              <label className="gobarber-label">Nome completo</label>
              <input
                type="text"
                name="name"
                value={form.name}
                onChange={handleChange}
                placeholder="Seu nome"
                className="gobarber-input"
              />
            </div>

            <div>
              <label className="gobarber-label">Email</label>
              <input
                type="email"
                name="email"
                value={form.email}
                onChange={handleChange}
                placeholder="seu@email.com"
                className="gobarber-input"
              />
            </div>

            <div>
              <label className="gobarber-label">Login (usuário)</label>
              <input
                type="text"
                name="login"
                value={form.login}
                onChange={handleChange}
                placeholder="seu_usuario"
                className="gobarber-input"
              />
            </div>

            <div>
              <label className="gobarber-label">Senha</label>
              <input
                type="password"
                name="password"
                value={form.password}
                onChange={handleChange}
                placeholder="••••••••"
                className="gobarber-input"
              />
            </div>

            <div>
              <label className="gobarber-label">Confirmar Senha</label>
              <input
                type="password"
                name="confirmPassword"
                value={form.confirmPassword}
                onChange={handleChange}
                placeholder="••••••••"
                className="gobarber-input"
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
                  Criando conta...
                </span>
              ) : (
                "Criar Conta"
              )}
            </button>
          </form>

          <div className="mt-6 text-center">
            <p className="text-gray-600 text-sm">
              Já tem uma conta?{" "}
              <Link
                href="/login"
                className="text-[#E94560] font-medium hover:underline"
              >
                Faça login
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
