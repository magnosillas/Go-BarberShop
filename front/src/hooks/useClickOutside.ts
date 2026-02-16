/**
 * @file Hook useClickOutside
 * @description Detecta cliques fora de um elemento e executa callback
 * 
 * @example
 * const ref = useRef<HTMLDivElement>(null);
 * useClickOutside(ref, () => setIsOpen(false));
 */

'use client';

import { useEffect, RefObject } from 'react';

type Handler = (event: MouseEvent | TouchEvent) => void;

/**
 * Hook que detecta cliques fora de um elemento
 * @param ref - Referência ao elemento a ser monitorado
 * @param handler - Função a ser executada quando clicar fora
 * @param enabled - Se o hook está ativo (default: true)
 */
export function useClickOutside<T extends HTMLElement = HTMLElement>(
  ref: RefObject<T>,
  handler: Handler,
  enabled: boolean = true
): void {
  useEffect(() => {
    if (!enabled) return;

    const listener = (event: MouseEvent | TouchEvent) => {
      const el = ref?.current;

      // Não faz nada se clicar dentro do elemento ou seus filhos
      if (!el || el.contains(event.target as Node)) {
        return;
      }

      handler(event);
    };

    document.addEventListener('mousedown', listener);
    document.addEventListener('touchstart', listener);

    return () => {
      document.removeEventListener('mousedown', listener);
      document.removeEventListener('touchstart', listener);
    };
  }, [ref, handler, enabled]);
}

/**
 * Hook que detecta cliques fora de múltiplos elementos
 * @param refs - Array de referências aos elementos
 * @param handler - Função a ser executada quando clicar fora de todos
 * @param enabled - Se o hook está ativo
 */
export function useClickOutsideMultiple<T extends HTMLElement = HTMLElement>(
  refs: RefObject<T>[],
  handler: Handler,
  enabled: boolean = true
): void {
  useEffect(() => {
    if (!enabled) return;

    const listener = (event: MouseEvent | TouchEvent) => {
      // Verifica se clicou dentro de algum dos elementos
      const clickedInside = refs.some((ref) => {
        const el = ref?.current;
        return el && el.contains(event.target as Node);
      });

      if (clickedInside) return;

      handler(event);
    };

    document.addEventListener('mousedown', listener);
    document.addEventListener('touchstart', listener);

    return () => {
      document.removeEventListener('mousedown', listener);
      document.removeEventListener('touchstart', listener);
    };
  }, [refs, handler, enabled]);
}

export default useClickOutside;
