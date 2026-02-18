"use client";

import React, {
  createContext,
  useState,
  useCallback,
  useMemo,
  useEffect,
} from "react";
import type {
  AuthContextType,
  AuthStatus,
  User,
  AuthToken,
  LoginCredentials,
} from "@/types/auth";
import type { RoleKey } from "@/types/menu";
import { AuthTokenService } from "@/lib/services/authToken";
import { AuthState } from "@/lib/services/authState";
import { genericaApiAuth } from "@/api/api";

export const AuthContext = createContext<AuthContextType | null>(null);

interface AuthProviderProps {
  children: React.ReactNode;
}

export function AuthProvider({ children }: AuthProviderProps) {
  const [user, setUser] = useState<User | null>(null);
  const [token, setToken] = useState<AuthToken | null>(null);
  const [status, setStatus] = useState<AuthStatus>("loading");
  const [error, setError] = useState<string | null>(null);

  const checkAuth = useCallback(async () => {
    try {
      const isAuth = await AuthState.isAuthenticated();
      if (isAuth) {
        const storedUser = AuthTokenService.getUser<User>();
        const storedToken = AuthTokenService.getAccessToken();
        if (storedUser && storedToken) {
          setUser(storedUser);
          setToken({
            accessToken: storedToken,
            expiresIn: 0,
            tokenType: "Bearer",
          });
          setStatus("authenticated");
          return;
        }
      }
      setUser(null);
      setToken(null);
      setStatus("unauthenticated");
    } catch (err) {
      console.error("[AuthContext] Erro ao verificar autenticação:", err);
      setUser(null);
      setToken(null);
      setStatus("unauthenticated");
    }
  }, []);

  useEffect(() => {
    checkAuth();
  }, [checkAuth]);

  useEffect(() => {
    const handleStorageChange = (e: StorageEvent) => {
      if (
        e.key === "gobarber_auth_token" ||
        e.key === "gobarber_user" ||
        e.key === null
      ) {
        checkAuth();
      }
    };
    const handleAuthChange = () => {
      checkAuth();
    };
    window.addEventListener("storage", handleStorageChange);
    window.addEventListener("gobarber:auth-changed", handleAuthChange);
    return () => {
      window.removeEventListener("storage", handleStorageChange);
      window.removeEventListener("gobarber:auth-changed", handleAuthChange);
    };
  }, [checkAuth]);

  const login = useCallback(async (credentials: LoginCredentials) => {
    setError(null);
    setStatus("loading");
    try {
      const response = await genericaApiAuth({
        metodo: "POST",
        uri: "",
        data: {
          login: credentials.email || credentials.cpf,
          password: credentials.senha,
        },
      });

      if (!response || response.status >= 400) {
        const errorMsg =
          response?.data?.message || response?.data || "Erro ao fazer login";
        throw new Error(
          typeof errorMsg === "string" ? errorMsg : "Credenciais inválidas",
        );
      }

      const data = response.data;
      // O backend retorna: { token, role }
      // Precisamos decodificar o JWT para pegar info do user
      const jwtToken = data.token;
      const role = data.role;

      // Decodificar payload do JWT
      const [, payload] = jwtToken.split(".");
      const decoded = JSON.parse(atob(payload));
      const currentTime = Math.floor(Date.now() / 1000);
      const expiresIn = decoded.exp - currentTime;

      const userData: User = {
        id: decoded.sub || decoded.id || "",
        nome: decoded.nome || decoded.name || decoded.sub || "",
        email: decoded.email || credentials.email || "",
        roles: [role as RoleKey],
      };

      AuthTokenService.setToken(jwtToken, expiresIn);
      AuthTokenService.setUser(userData);

      setUser(userData);
      setToken({ accessToken: jwtToken, expiresIn, tokenType: "Bearer" });
      setStatus("authenticated");
    } catch (err) {
      const message =
        err instanceof Error ? err.message : "Erro ao fazer login";
      setError(message);
      setStatus("unauthenticated");
      throw err;
    }
  }, []);

  const logout = useCallback(async () => {
    try {
      const currentToken = AuthTokenService.getAccessToken();
      if (currentToken) {
        await genericaApiAuth({
          metodo: "POST",
          uri: "/logout",
          data: {},
        }).catch(() => {});
      }
    } catch {}
    AuthState.clearAll();
    window.dispatchEvent(new Event("gobarber:auth-logout"));
    setUser(null);
    setToken(null);
    setStatus("unauthenticated");
    setError(null);
  }, []);

  const refreshToken = useCallback(async () => {
    // Backend GoBarber não tem refresh token por enquanto
    await logout();
  }, [logout]);

  const updateUser = useCallback((userData: Partial<User>) => {
    setUser((prev) => {
      if (!prev) return null;
      const updated = { ...prev, ...userData };
      AuthTokenService.setUser(updated);
      return updated;
    });
  }, []);

  const clearError = useCallback(() => setError(null), []);

  const hasRole = useCallback(
    (role: RoleKey | RoleKey[]): boolean => {
      if (!user?.roles) return false;
      if (Array.isArray(role)) return role.some((r) => user.roles.includes(r));
      return user.roles.includes(role);
    },
    [user?.roles],
  );

  const hasAnyRole = useCallback(
    (roles: RoleKey[]): boolean => {
      if (!user?.roles) return false;
      return roles.some((role) => user.roles.includes(role));
    },
    [user?.roles],
  );

  const hasAllRoles = useCallback(
    (roles: RoleKey[]): boolean => {
      if (!user?.roles) return false;
      return roles.every((role) => user.roles.includes(role));
    },
    [user?.roles],
  );

  const contextValue = useMemo<AuthContextType>(
    () => ({
      user,
      token,
      status,
      error,
      isAuthenticated: status === "authenticated" && !!user,
      login,
      logout,
      refreshToken,
      updateUser,
      clearError,
      hasRole,
      hasAnyRole,
      hasAllRoles,
    }),
    [
      user,
      token,
      status,
      error,
      login,
      logout,
      refreshToken,
      updateUser,
      clearError,
      hasRole,
      hasAnyRole,
      hasAllRoles,
    ],
  );

  return (
    <AuthContext.Provider value={contextValue}>{children}</AuthContext.Provider>
  );
}

export default AuthProvider;
