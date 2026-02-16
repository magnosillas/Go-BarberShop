/**
 * @file useIsMobile.ts
 * @description Hook para detectar se o dispositivo é mobile (largura < breakpoint)
 */

'use client';

import { useState, useEffect, useCallback } from 'react';

interface UseIsMobileOptions {
  /** Breakpoint em pixels (padrão: 768) */
  breakpoint?: number;
  /** Se deve usar matchMedia API (mais performático) */
  useMatchMedia?: boolean;
}

interface UseIsMobileReturn {
  /** Se a tela é considerada mobile */
  isMobile: boolean;
  /** Largura atual da janela */
  windowWidth: number;
  /** Se é tablet (entre mobile e desktop) */
  isTablet: boolean;
  /** Se é desktop (largura >= 1024px) */
  isDesktop: boolean;
}

/**
 * Hook para detectar dispositivo mobile baseado na largura da janela
 * 
 * @example
 * const { isMobile, isTablet, isDesktop } = useIsMobile();
 * 
 * @example
 * const { isMobile } = useIsMobile({ breakpoint: 640 }); // SM breakpoint
 */
export function useIsMobile(options: UseIsMobileOptions = {}): UseIsMobileReturn {
  const { breakpoint = 768, useMatchMedia = true } = options;
  
  const [state, setState] = useState<UseIsMobileReturn>({
    isMobile: false,
    windowWidth: typeof window !== 'undefined' ? window.innerWidth : 1024,
    isTablet: false,
    isDesktop: true,
  });

  const calculateState = useCallback((width: number): UseIsMobileReturn => ({
    isMobile: width < breakpoint,
    windowWidth: width,
    isTablet: width >= breakpoint && width < 1024,
    isDesktop: width >= 1024,
  }), [breakpoint]);

  useEffect(() => {
    // SSR guard
    if (typeof window === 'undefined') return;

    // Calcula estado inicial
    setState(calculateState(window.innerWidth));

    // Usa matchMedia se disponível (mais performático)
    if (useMatchMedia && window.matchMedia) {
      const mobileQuery = window.matchMedia(`(max-width: ${breakpoint - 1}px)`);
      const tabletQuery = window.matchMedia(`(min-width: ${breakpoint}px) and (max-width: 1023px)`);
      
      const handleChange = () => {
        setState(calculateState(window.innerWidth));
      };

      // Adiciona listeners
      if (mobileQuery.addEventListener) {
        mobileQuery.addEventListener('change', handleChange);
        tabletQuery.addEventListener('change', handleChange);
      } else {
        // Fallback para browsers antigos
        mobileQuery.addListener(handleChange);
        tabletQuery.addListener(handleChange);
      }

      return () => {
        if (mobileQuery.removeEventListener) {
          mobileQuery.removeEventListener('change', handleChange);
          tabletQuery.removeEventListener('change', handleChange);
        } else {
          mobileQuery.removeListener(handleChange);
          tabletQuery.removeListener(handleChange);
        }
      };
    }

    // Fallback: usa resize event
    const handleResize = () => {
      setState(calculateState(window.innerWidth));
    };

    window.addEventListener('resize', handleResize);
    return () => window.removeEventListener('resize', handleResize);
  }, [breakpoint, useMatchMedia, calculateState]);

  return state;
}

/**
 * Hook simplificado que retorna apenas boolean
 * @deprecated Prefira usar useIsMobile() que retorna objeto com mais informações
 */
export function useIsMobileSimple(breakpoint: number = 768): boolean {
  const { isMobile } = useIsMobile({ breakpoint });
  return isMobile;
}

export default useIsMobile;
