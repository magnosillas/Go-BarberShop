/**
 * @file Layout para área pública (landing page, login, contato)
 * @description Layout sem verificação de autenticação
 * 
 * Este layout é aplicado a todas as rotas dentro de (public)/
 * A URL não conterá "(public)" - é apenas organização interna
 * 
 * @example
 * URL: /contato
 * Arquivo: src/app/(public)/contato/page.tsx
 */

import React from 'react';

export default function PublicLayout({
  children,
}: {
  children: React.ReactNode;
}) {
  return (
    <>
      {children}
    </>
  );
}
