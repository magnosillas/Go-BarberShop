// tailwind.config.ts
import type { Config } from "tailwindcss";

const config: Config = {
  content: ["./src/**/*.{js,jsx,ts,tsx}"],
  safelist: [
    "col-span-1",
    "col-span-2",
    "col-span-3",
    "col-span-4",
    "col-span-5",
    "col-span-6",
    "col-span-7",
    "col-span-8",
    "col-span-9",
    "col-span-10",
    "md:col-span-1",
    "md:col-span-2",
    "md:col-span-3",
    "md:col-span-4",
    "md:col-span-5",
    "md:col-span-6",
    "md:col-span-7",
    "md:col-span-8",
    "md:col-span-9",
    "md:col-span-10",
    "md:grid-cols-10",
    "md:grid-cols-12",
  ],
  theme: {
    screens: {
      xs: "480px",
      sm: "640px",
      md: "768px",
      lg: "1024px",
      xl: "1280px",
      "2xl": "1536px",
    },
    extend: {
      animation: {
        "spin-horizontal": "spin-horizontal 1s linear infinite",
      },
      keyframes: {
        "spin-horizontal": {
          "0%": { transform: "rotateY(0deg)" },
          "100%": { transform: "rotateY(360deg)" },
        },
      },
      colors: {
        // ========================================
        // GOBARBER - Paleta Principal
        // ========================================
        gobarber: {
          dark: "#1A1A2E",
          red: "#E94560",
          blue: "#0F3460",
          light: "#F5F5F5",
        },
        primary: {
          50: "#EDE7F6",
          100: "#D1C4E9",
          200: "#B39DDB",
          300: "#9575CD",
          400: "#7E57C2",
          500: "#1A1A2E",
          600: "#16162A",
          700: "#121226",
          800: "#0E0E22",
          900: "#0A0A1E",
        },
        accent: {
          50: "#FCE4EC",
          100: "#F8BBD0",
          200: "#F48FB1",
          300: "#F06292",
          400: "#EC407A",
          500: "#E94560",
          600: "#D81B60",
          700: "#C2185B",
          800: "#AD1457",
          900: "#880E4F",
        },
        neutrals: {
          0: "#000",
          50: "#F2F5F7",
          100: "#E4EAED",
          200: "#BCC8CE",
          300: "#9FAEB5",
          400: "#84949C",
          500: "#6C7B82",
          600: "#556369",
          700: "#2C3438",
          800: "#1E2629",
          900: "#151C1F",
        },
        success: {
          50: "#E8F5EA",
          100: "#B8E1BD",
          200: "#8FD49A",
          300: "#66BF70",
          400: "#40AF4D",
          500: "#1A9F2A",
          600: "#158723",
          700: "#12711E",
          800: "#0E5A17",
          900: "#0B4312",
        },
        warning: {
          50: "#F5F2E8",
          100: "#E1D6B8",
          200: "#D4C18F",
          300: "#BFA766",
          400: "#AF9240",
          500: "#9F7C1A",
          600: "#876916",
          700: "#715812",
          800: "#5A460E",
          900: "#43340B",
        },
        danger: {
          50: "#F5EAE8",
          100: "#E1BDB8",
          200: "#D4968F",
          300: "#BF7066",
          400: "#AF4D40",
          500: "#9F2A1A",
          600: "#872316",
          700: "#711E12",
          800: "#5A180E",
          900: "#43120B",
        },
        info: {
          50: "#E8F4F8",
          100: "#B8DBE8",
          200: "#8FC7DA",
          300: "#66B3CC",
          400: "#4099B8",
          500: "#1A7FA3",
          600: "#166B8A",
          700: "#125771",
          800: "#0E4358",
          900: "#0B3040",
        },
      },
      fontSize: {
        "display-large": [
          "57px",
          { lineHeight: "64px", letterSpacing: "-0.25px", fontWeight: "600" },
        ],
        "display-medium": [
          "45px",
          { lineHeight: "52px", letterSpacing: "0px", fontWeight: "600" },
        ],
        "display-small": [
          "36px",
          { lineHeight: "44px", letterSpacing: "0px", fontWeight: "600" },
        ],
        "headline-large": [
          "32px",
          { lineHeight: "40px", letterSpacing: "0px", fontWeight: "600" },
        ],
        "headline-medium": [
          "28px",
          { lineHeight: "36px", letterSpacing: "0px", fontWeight: "600" },
        ],
        "headline-small": [
          "24px",
          { lineHeight: "32px", letterSpacing: "0px", fontWeight: "600" },
        ],
        "title-large": [
          "22px",
          { lineHeight: "28px", letterSpacing: "0px", fontWeight: "400" },
        ],
        "title-medium": [
          "16px",
          { lineHeight: "24px", letterSpacing: "0.15px", fontWeight: "600" },
        ],
        "title-small": [
          "14px",
          { lineHeight: "20px", letterSpacing: "0.1px", fontWeight: "600" },
        ],
        "label-large": [
          "14px",
          { lineHeight: "20px", letterSpacing: "0.1px", fontWeight: "600" },
        ],
        "label-medium": [
          "12px",
          { lineHeight: "16px", letterSpacing: "0.5px", fontWeight: "600" },
        ],
        "label-small": [
          "11px",
          { lineHeight: "16px", letterSpacing: "0.5px", fontWeight: "600" },
        ],
        "body-large": [
          "16px",
          { lineHeight: "24px", letterSpacing: "0.5px", fontWeight: "400" },
        ],
        "body-medium": [
          "14px",
          { lineHeight: "20px", letterSpacing: "0.25px", fontWeight: "400" },
        ],
        "body-small": [
          "12px",
          { lineHeight: "16px", letterSpacing: "0.4px", fontWeight: "400" },
        ],
      },
    },
  },
  plugins: [],
};

export default config;
