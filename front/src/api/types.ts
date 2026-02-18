import type { AxiosRequestConfig, AxiosResponse } from "axios";

export type GenericaOptions = {
  metodo?: string;
  uri?: string;
  params?: Record<string, unknown>;
  data?: unknown;
  responseType?: AxiosRequestConfig["responseType"];
};

export type UploadOptions = {
  metodo?: string;
  uri?: string;
  params?: Record<string, unknown>;
  data?: FormData | Record<string, unknown>;
  onProgress?: (progress: number) => void;
};

export type ApiResponse<T = unknown> = AxiosResponse<T> | undefined;

export type ApiError = {
  status: number;
  message?: string;
  details?: Record<string, unknown>;
};

export type ErrorResponse = ApiError;

export type PaginatedResponse<T> = {
  content: T[];
  number: number;
  size: number;
  totalElements: number;
  totalPages: number;
  first: boolean;
  last: boolean;
};

export type PaginationParams = {
  page?: number;
  size?: number;
  sort?: string;
  direction?: "asc" | "desc";
};
