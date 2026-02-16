"use client";

import React, {
  createContext,
  useContext,
  useState,
  useCallback,
  useMemo,
  useEffect,
} from "react";
import type { ThemeMode } from "@/types/theme";
import { GOBARBER_THEME } from "@/types/theme";

interface ThemeContextType {
  mode: ThemeMode;
  toggleMode: () => void;
  theme: typeof GOBARBER_THEME;
}

export const ThemeContext = createContext<ThemeContextType>({
  mode: "light",
  toggleMode: () => {},
  theme: GOBARBER_THEME,
});

export function ThemeProvider({ children }: { children: React.ReactNode }) {
  const [mode, setMode] = useState<ThemeMode>("light");

  useEffect(() => {
    const saved = localStorage.getItem(
      "gobarber_theme_mode",
    ) as ThemeMode | null;
    if (saved) setMode(saved);
  }, []);

  const toggleMode = useCallback(() => {
    setMode((prev) => {
      const next = prev === "light" ? "dark" : "light";
      localStorage.setItem("gobarber_theme_mode", next);
      return next;
    });
  }, []);

  const value = useMemo(
    () => ({ mode, toggleMode, theme: GOBARBER_THEME }),
    [mode, toggleMode],
  );

  return (
    <ThemeContext.Provider value={value}>{children}</ThemeContext.Provider>
  );
}

export const useThemeContext = () => useContext(ThemeContext);
