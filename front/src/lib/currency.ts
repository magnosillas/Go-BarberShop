/**
 * Utilitários de formatação de moeda brasileira (R$).
 * O input exibe "1.234,56" e internamente o valor é um number (1234.56).
 */

/** Formata um number como string no padrão pt-BR (ex: 33.5 → "33,50") */
export function formatCurrencyDisplay(value: number): string {
  return value.toLocaleString("pt-BR", {
    minimumFractionDigits: 2,
    maximumFractionDigits: 2,
  });
}

/**
 * Converte o texto digitado no input em um number.
 * Lógica "centavos": cada dígito digitado entra pela direita.
 * Ex: digitar "33" → 0.33 | digitar "3300" → 33.00
 */
export function parseCurrencyInput(raw: string): number {
  const digits = raw.replace(/\D/g, "");
  if (!digits) return 0;
  return parseInt(digits, 10) / 100;
}
