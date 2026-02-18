export type ThemeMode = "light" | "dark";

export interface GoBarberTheme {
  primary: string;
  primaryDark: string;
  primaryLight: string;
  secondary: string;
  secondaryDark: string;
  secondaryLight: string;
  accent: string;
  background: string;
  surface: string;
  text: string;
  textSecondary: string;
  error: string;
  success: string;
  warning: string;
  info: string;
}

export const GOBARBER_THEME: GoBarberTheme = {
  primary: "#1A1A2E",
  primaryDark: "#16162A",
  primaryLight: "#2D2D44",
  secondary: "#E94560",
  secondaryDark: "#C73A52",
  secondaryLight: "#FF6B81",
  accent: "#0F3460",
  background: "#F5F5F5",
  surface: "#FFFFFF",
  text: "#1A1A2E",
  textSecondary: "#6B7280",
  error: "#EF4444",
  success: "#10B981",
  warning: "#F59E0B",
  info: "#3B82F6",
};
