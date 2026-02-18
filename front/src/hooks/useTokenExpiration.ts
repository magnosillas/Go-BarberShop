"use client";

import { useEffect, useState, useCallback, useRef } from "react";
import { usePathname } from "next/navigation";
import { AuthTokenService } from "@/lib/services/authToken";
import { AuthState } from "@/lib/services/authState";
import Swal from "sweetalert2";

const WARNING_TIME_MS = 1 * 60 * 1000;
const CHECK_INTERVAL_MS = 3 * 1000;

const PUBLIC_ROUTES = ["/login", "/register", "/", "/sobre"];

interface UseTokenExpirationOptions {
  onExpired?: () => void;
}

export function useTokenExpiration(options?: UseTokenExpirationOptions) {
  const pathname = usePathname();
  const [isWarningShown, setIsWarningShown] = useState(false);
  const [timeRemaining, setTimeRemaining] = useState<number | null>(null);
  const warningShownRef = useRef(false);

  const isPublicRoute = PUBLIC_ROUTES.some((route) => pathname === route);

  const showExpirationWarning = useCallback(async () => {
    if (warningShownRef.current || isPublicRoute) return;
    warningShownRef.current = true;
    setIsWarningShown(true);

    const result = await Swal.fire({
      title: "Sessão expirando",
      html: "Sua sessão irá expirar em breve.<br/>Faça login novamente para continuar.",
      icon: "warning",
      confirmButtonColor: "#1A1A2E",
      confirmButtonText: "Ir para Login",
      allowOutsideClick: false,
      allowEscapeKey: false,
    });

    if (result.isConfirmed) {
      AuthState.clearAll();
      options?.onExpired?.();
      window.location.href = "/login";
    }
  }, [isPublicRoute, options]);

  useEffect(() => {
    if (isPublicRoute) return;

    const checkTokenExpiration = () => {
      try {
        const expiresAt = AuthTokenService.getTokenExpiry();
        if (!expiresAt) return;

        const remaining = expiresAt - Date.now();
        setTimeRemaining(remaining);

        if (
          remaining <= WARNING_TIME_MS &&
          remaining > 0 &&
          !warningShownRef.current
        ) {
          showExpirationWarning();
          return;
        }

        if (remaining <= 0) {
          if (warningShownRef.current) return;
          AuthState.clearAll();
          options?.onExpired?.();
          window.location.href = "/login";
        }
      } catch (error) {
        console.error("[TokenExpiration] Erro:", error);
      }
    };

    checkTokenExpiration();
    const interval = setInterval(checkTokenExpiration, CHECK_INTERVAL_MS);
    return () => clearInterval(interval);
  }, [isPublicRoute, showExpirationWarning, options]);

  return { isWarningShown, timeRemaining };
}

export default useTokenExpiration;
