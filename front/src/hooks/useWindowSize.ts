/**
 * @file Hook para detectar tamanho da janela
 * @description Fornece informações sobre o tamanho da viewport
 */

'use client';

import { useState, useEffect } from 'react';

interface WindowSize {
  width: number;
  height: number;
  isMobile: boolean;
  isTablet: boolean;
  isDesktop: boolean;
  isPopup: boolean;
}

const BREAKPOINTS = {
  mobile: 640,
  tablet: 1024,
  popup: { width: 600, height: 700 },
};

/**
 * Hook para obter e monitorar o tamanho da janela
 */
export function useWindowSize(): WindowSize {
  const [windowSize, setWindowSize] = useState<WindowSize>({
    width: typeof window !== 'undefined' ? window.innerWidth : 0,
    height: typeof window !== 'undefined' ? window.innerHeight : 0,
    isMobile: false,
    isTablet: false,
    isDesktop: true,
    isPopup: false,
  });

  useEffect(() => {
    function handleResize() {
      const width = window.innerWidth;
      const height = window.innerHeight;
      
      // Detecta se é um popup baseado no tamanho
      const isPopup = width <= BREAKPOINTS.popup.width && height <= BREAKPOINTS.popup.height;
      
      setWindowSize({
        width,
        height,
        isMobile: width < BREAKPOINTS.mobile,
        isTablet: width >= BREAKPOINTS.mobile && width < BREAKPOINTS.tablet,
        isDesktop: width >= BREAKPOINTS.tablet,
        isPopup,
      });
    }

    // Executa no mount
    handleResize();

    window.addEventListener('resize', handleResize);
    return () => window.removeEventListener('resize', handleResize);
  }, []);

  return windowSize;
}
