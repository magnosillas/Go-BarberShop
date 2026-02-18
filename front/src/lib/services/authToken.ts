const TOKEN_KEY = "gobarber_auth_token";
const REFRESH_TOKEN_KEY = "gobarber_refresh_token";
const USER_KEY = "gobarber_user";

interface StoredToken {
  accessToken: string;
  refreshToken?: string;
  expiresAt: number;
}

export const AuthTokenService = {
  setToken(
    accessToken: string,
    expiresIn: number,
    refreshToken?: string,
  ): void {
    if (typeof window === "undefined") return;
    const tokenData: StoredToken = {
      accessToken,
      refreshToken,
      expiresAt: Date.now() + expiresIn * 1000,
    };
    try {
      sessionStorage.setItem(TOKEN_KEY, JSON.stringify(tokenData));
      if (refreshToken) {
        sessionStorage.setItem(REFRESH_TOKEN_KEY, refreshToken);
      }
    } catch (error) {
      console.error("[AuthToken] Erro ao salvar token:", error);
    }
  },

  getAccessToken(redirect: boolean = false): string | null {
    if (typeof window === "undefined") return null;
    try {
      const tokenData = sessionStorage.getItem(TOKEN_KEY);
      if (!tokenData) return null;
      const parsed: StoredToken = JSON.parse(tokenData);
      return parsed.accessToken;
    } catch (error) {
      console.error("[AuthToken] Erro ao obter token:", error);
      return null;
    }
  },

  getRefreshToken(): string | null {
    if (typeof window === "undefined") return null;
    try {
      return sessionStorage.getItem(REFRESH_TOKEN_KEY);
    } catch {
      return null;
    }
  },

  isTokenValid(): boolean {
    if (typeof window === "undefined") return false;
    try {
      const tokenData = sessionStorage.getItem(TOKEN_KEY);
      if (!tokenData) return false;
      const parsed: StoredToken = JSON.parse(tokenData);
      return parsed.expiresAt > Date.now();
    } catch {
      return false;
    }
  },

  hasToken(): boolean {
    if (typeof window === "undefined") return false;
    return sessionStorage.getItem(TOKEN_KEY) !== null;
  },

  isTokenExpiringSoon(): boolean {
    if (typeof window === "undefined") return true;
    try {
      const tokenData = sessionStorage.getItem(TOKEN_KEY);
      if (!tokenData) return true;
      const parsed: StoredToken = JSON.parse(tokenData);
      const fiveMinutes = 5 * 60 * 1000;
      return parsed.expiresAt - Date.now() < fiveMinutes;
    } catch {
      return true;
    }
  },

  getTokenExpiry(): number | null {
    if (typeof window === "undefined") return null;
    try {
      const tokenData = sessionStorage.getItem(TOKEN_KEY);
      if (!tokenData) return null;
      const parsed: StoredToken = JSON.parse(tokenData);
      return parsed.expiresAt;
    } catch {
      return null;
    }
  },

  clearTokens(): void {
    if (typeof window === "undefined") return;
    try {
      sessionStorage.removeItem(TOKEN_KEY);
      sessionStorage.removeItem(REFRESH_TOKEN_KEY);
      sessionStorage.removeItem(USER_KEY);
    } catch (error) {
      console.error("[AuthToken] Erro ao limpar tokens:", error);
    }
  },

  clearAllAuthData(): void {
    if (typeof window === "undefined") return;
    this.clearTokens();
  },

  setUser(user: unknown): void {
    if (typeof window === "undefined") return;
    try {
      sessionStorage.setItem(USER_KEY, JSON.stringify(user));
    } catch (error) {
      console.error("[AuthToken] Erro ao salvar usuário:", error);
    }
  },

  getUser<T>(): T | null {
    if (typeof window === "undefined") return null;
    try {
      const userData = sessionStorage.getItem(USER_KEY);
      return userData ? JSON.parse(userData) : null;
    } catch {
      return null;
    }
  },

  getAuthHeader(): { Authorization: string } | Record<string, never> {
    const token = this.getAccessToken();
    return token ? { Authorization: `Bearer ${token}` } : {};
  },

  redirectToLogin(customRoute?: string): void {
    if (typeof window === "undefined") return;
    this.clearTokens();
    window.location.href = customRoute || "/login";
  },
};
