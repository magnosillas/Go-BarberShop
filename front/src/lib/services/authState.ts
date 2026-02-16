import { AuthTokenService } from "./authToken";

class AuthenticationState {
  private static instance: AuthenticationState;
  private checkInProgress = false;
  private refreshInProgress = false;

  private constructor() {}

  static getInstance(): AuthenticationState {
    if (!this.instance) this.instance = new AuthenticationState();
    return this.instance;
  }

  async isAuthenticated(): Promise<boolean> {
    if (this.checkInProgress) {
      await this.waitForCheck();
      return this.isAuthenticated();
    }
    this.checkInProgress = true;
    try {
      const hasToken = AuthTokenService.hasToken();
      if (!hasToken) {
        this.clearAll();
        return false;
      }
      const isValid = AuthTokenService.isTokenValid();
      if (!isValid) {
        this.clearAll();
        return false;
      }
      return true;
    } catch {
      this.clearAll();
      return false;
    } finally {
      this.checkInProgress = false;
    }
  }

  private async waitForCheck(): Promise<void> {
    let attempts = 0;
    while (this.checkInProgress && attempts < 50) {
      await new Promise((resolve) => setTimeout(resolve, 100));
      attempts++;
    }
    if (attempts >= 50) this.checkInProgress = false;
  }

  clearAll(): void {
    if (typeof window === "undefined") return;
    AuthTokenService.clearAllAuthData();
  }

  redirectToLogin(): void {
    if (typeof window === "undefined") return;
    this.clearAll();
    window.location.href = "/login";
  }

  async forceRefresh(): Promise<boolean> {
    // GoBarber backend atualmente não tem refresh token endpoint
    // Quando implementar, adicionar a lógica aqui
    return false;
  }
}

export const AuthState = AuthenticationState.getInstance();
export type { AuthenticationState };
