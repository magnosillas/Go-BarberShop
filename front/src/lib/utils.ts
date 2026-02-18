/**
 * @file Funções utilitárias gerais
 * @description Utilitários compartilhados pela aplicação
 */

/**
 * Combina classes CSS de forma inteligente
 * 
 * Filtra valores falsy e junta as classes restantes
 * 
 * @example
 * cn('px-4 py-2', isActive && 'bg-blue-500', className)
 * cn('text-red-500', undefined, 'text-lg') // -> 'text-red-500 text-lg'
 */
export function cn(...inputs: (string | boolean | undefined | null)[]): string {
  return inputs
    .filter((input): input is string => typeof input === 'string' && input.length > 0)
    .join(' ')
    .trim();
}

/**
 * Delay assíncrono
 * @param ms Milissegundos para aguardar
 */
export function delay(ms: number): Promise<void> {
  return new Promise((resolve) => setTimeout(resolve, ms));
}

/**
 * Gera um ID único
 */
export function generateId(): string {
  return Math.random().toString(36).substring(2, 9);
}

/**
 * Verifica se está rodando no cliente
 */
export function isClient(): boolean {
  return typeof window !== 'undefined';
}

/**
 * Verifica se está rodando no servidor
 */
export function isServer(): boolean {
  return typeof window === 'undefined';
}

/**
 * Capitaliza a primeira letra
 */
export function capitalize(str: string): string {
  return str.charAt(0).toUpperCase() + str.slice(1);
}

/**
 * Trunca texto com ellipsis
 */
export function truncate(str: string, maxLength: number): string {
  if (str.length <= maxLength) return str;
  return `${str.slice(0, maxLength - 3)}...`;
}
