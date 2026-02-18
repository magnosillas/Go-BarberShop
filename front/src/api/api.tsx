import axios, { AxiosResponse } from "axios";
import { AuthTokenService } from "@/lib/services/authToken";
import type { GenericaOptions } from "./types";

const isBrowser = () => typeof window !== "undefined";

const resolveBaseUrl = () =>
  (
    process.env.NEXT_PUBLIC_API_URL ||
    process.env.NEXT_PUBLIC_GATEWAY_URL ||
    "http://localhost:8080"
  ).replace(/\/+$/, "");

const BASE_URL = resolveBaseUrl();
const AUTH_URL = `${BASE_URL}/auth`;

const buildHeaders = (data?: unknown) => {
  if (!isBrowser()) return null;
  let accessToken: string | null = null;
  try {
    accessToken = AuthTokenService.getAccessToken();
  } catch {
    accessToken = null;
  }
  if (!accessToken) return null;
  // Quando data é FormData (multipart), não definir Content-Type — axios define automaticamente com boundary
  const headers: Record<string, string> = {
    Authorization: `Bearer ${accessToken}`,
  };
  if (!(data instanceof FormData)) {
    headers["Content-Type"] = "application/json";
  }
  return headers;
};

/**
 * Chamada autenticada para a API GoBarber
 */
export const generica = async ({
  metodo = "",
  uri = "",
  params = {},
  data = {},
  responseType = "json",
}: GenericaOptions) => {
  const headers = buildHeaders(data);
  if (!headers) {
    if (isBrowser()) AuthTokenService.redirectToLogin();
    return undefined;
  }
  try {
    const url = `${BASE_URL}${uri}`;
    return await axios({
      method: metodo,
      url,
      params,
      data,
      headers,
      responseType,
    });
  } catch (error: any) {
    if (error?.response?.status === 401) {
      if (isBrowser()) AuthTokenService.redirectToLogin();
    }
    return error?.response;
  }
};

/**
 * Chamada de autenticação (login, register, logout)
 */
export const genericaApiAuth = async ({
  metodo = "",
  uri = "",
  params = {},
  data = {},
}: Record<string, any>) => {
  try {
    const url = `${AUTH_URL}${uri}`;
    return await axios({
      method: metodo,
      url,
      params,
      data,
      headers: { "Content-Type": "application/json" },
    });
  } catch (error: any) {
    if (error?.response) return error.response;
    return {
      status: 500,
      data: { message: "Erro ao configurar a requisição" },
    } as AxiosResponse;
  }
};

/**
 * Chamada pública (sem autenticação) para a API GoBarber
 */
export const genericaPublic = async ({
  metodo = "",
  uri = "",
  params = {},
  data = {},
}: Record<string, any>) => {
  try {
    const url = `${BASE_URL}${uri}`;
    return await axios({
      method: metodo,
      url,
      params,
      data,
      headers: { "Content-Type": "application/json" },
    });
  } catch (error: any) {
    if (error?.response) return error.response;
    return {
      status: error?.code === "ERR_NETWORK" ? 503 : 500,
      data: { message: "Falha ao contactar o serviço" },
    } as AxiosResponse;
  }
};

/**
 * Extrai mensagens de erro de uma resposta da API
 */
const isRecord = (value: unknown): value is Record<string, unknown> =>
  typeof value === "object" && value !== null && !Array.isArray(value);

const coerceToMessages = (value: unknown): string[] => {
  if (value == null) return [];
  if (Array.isArray(value)) return value.flatMap(coerceToMessages);
  if (typeof value === "string") return value.trim() ? [value.trim()] : [];
  if (typeof value === "number" || typeof value === "boolean")
    return [String(value)];
  if (isRecord(value)) {
    const preferredKeys = [
      "message",
      "mensagem",
      "detail",
      "error",
      "descricao",
      "description",
    ];
    for (const key of preferredKeys) {
      const candidate = value[key];
      if (typeof candidate === "string" && candidate.trim())
        return [candidate.trim()];
    }
    return Object.values(value).flatMap(coerceToMessages);
  }
  return [];
};

export const collectApiErrorMessages = (
  response?: AxiosResponse | null,
): string[] => {
  if (!response) return [];
  const messages = new Set<string>();
  for (const msg of coerceToMessages(response.data)) messages.add(msg);
  if (messages.size === 0 && (response.status ?? 0) >= 400) {
    messages.add("Erro ao processar a requisição.");
  }
  return Array.from(messages);
};
