import type { Metadata } from "next";
import "./globals.css";
import React from "react";
import "tailwindcss/tailwind.css";
import { ToastContainer } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";

import { AuthProvider } from "@/contexts/AuthContext";
import { ThemeProvider } from "@/contexts/ThemeContext";

export const metadata: Metadata = {
  title: "GoBarber",
  description: "Sistema de Gerenciamento para Barbearias",
  icons: {
    icon: "/favicon.ico",
  },
};

export default function RootLayout({
  children,
}: {
  children: React.ReactNode;
}) {
  return (
    <html lang="pt-br">
      <body className="antialiased bg-gray-100">
        <AuthProvider>
          <ThemeProvider>
            {children}
            <ToastContainer
              position="top-right"
              autoClose={3000}
              hideProgressBar={false}
              newestOnTop
              closeOnClick
              pauseOnHover
              theme="colored"
            />
          </ThemeProvider>
        </AuthProvider>
      </body>
    </html>
  );
}
