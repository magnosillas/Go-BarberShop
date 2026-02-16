import type { RoleKey } from "./menu";

export type AuthStatus = "loading" | "authenticated" | "unauthenticated";

export interface User {
  id: number | string;
  nome: string;
  email: string;
  cpf?: string;
  avatar?: string;
  roles: RoleKey[];
  createdAt?: string;
  updatedAt?: string;
}

export interface AuthToken {
  accessToken: string;
  refreshToken?: string;
  expiresIn: number;
  tokenType: string;
}

export interface LoginResponse {
  user: User;
  token: AuthToken;
}

export interface LoginCredentials {
  email?: string;
  cpf?: string;
  senha: string;
}

export interface AuthState {
  user: User | null;
  token: AuthToken | null;
  status: AuthStatus;
  error: string | null;
}

export interface AuthActions {
  login: (credentials: LoginCredentials) => Promise<void>;
  logout: () => void;
  refreshToken: () => Promise<void>;
  updateUser: (user: Partial<User>) => void;
  clearError: () => void;
}

export interface AuthContextType extends AuthState, AuthActions {
  isAuthenticated: boolean;
  hasRole: (role: RoleKey | RoleKey[]) => boolean;
  hasAnyRole: (roles: RoleKey[]) => boolean;
  hasAllRoles: (roles: RoleKey[]) => boolean;
}

export interface ProtectedRouteProps {
  children: React.ReactNode;
  roles?: RoleKey[];
  requireAll?: boolean;
  fallback?: React.ReactNode;
  redirectTo?: string;
}

export interface RegisterData {
  nome: string;
  email: string;
  cpf: string;
  senha: string;
  confirmarSenha: string;
}

export interface PasswordRecoveryData {
  email?: string;
  cpf?: string;
}

export interface PasswordResetData {
  token: string;
  novaSenha: string;
  confirmarSenha: string;
}
