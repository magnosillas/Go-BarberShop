/**
 * @file Hook para chamadas de API
 * @description Gerencia estado de loading, erro e dados para requisições
 */

import { useState, useCallback } from 'react';
import { AxiosError, AxiosResponse } from 'axios';

interface UseApiState<T> {
  data: T | null;
  error: string | null;
  isLoading: boolean;
}

interface UseApiReturn<T, P extends unknown[]> extends UseApiState<T> {
  execute: (...params: P) => Promise<T | null>;
  reset: () => void;
  setData: React.Dispatch<React.SetStateAction<T | null>>;
}

interface ApiError {
  message?: string;
  error?: string;
}

/**
 * Hook para gerenciar chamadas de API com estados de loading, erro e dados
 * 
 * @example
 * const { data, isLoading, error, execute } = useApi(
 *   (id: string) => api.get(`/users/${id}`)
 * );
 * 
 * useEffect(() => {
 *   execute('123');
 * }, [execute]);
 */
export function useApi<T, P extends unknown[] = []>(
  apiFunction: (...params: P) => Promise<AxiosResponse<T>>
): UseApiReturn<T, P> {
  const [state, setState] = useState<UseApiState<T>>({
    data: null,
    error: null,
    isLoading: false,
  });

  const execute = useCallback(
    async (...params: P): Promise<T | null> => {
      setState((prev) => ({ ...prev, isLoading: true, error: null }));
      
      try {
        const response = await apiFunction(...params);
        setState({ data: response.data, error: null, isLoading: false });
        return response.data;
      } catch (err) {
        const axiosError = err as AxiosError<ApiError>;
        const errorMessage = 
          axiosError.response?.data?.message ||
          axiosError.response?.data?.error ||
          axiosError.message ||
          'Erro desconhecido';
        
        setState({ data: null, error: errorMessage, isLoading: false });
        return null;
      }
    },
    [apiFunction]
  );

  const reset = useCallback(() => {
    setState({ data: null, error: null, isLoading: false });
  }, []);

  const setData = useCallback((value: React.SetStateAction<T | null>) => {
    setState((prev) => ({
      ...prev,
      data: typeof value === 'function' ? (value as (prev: T | null) => T | null)(prev.data) : value,
    }));
  }, []);

  return {
    ...state,
    execute,
    reset,
    setData,
  };
}

/**
 * Hook para chamadas de API com execução imediata
 * 
 * @example
 * const { data, isLoading, error, refetch } = useApiOnMount(
 *   () => api.get('/users')
 * );
 */
export function useApiLazy<T>(
  apiFunction: () => Promise<AxiosResponse<T>>
): UseApiReturn<T, []> & { refetch: () => Promise<T | null> } {
  const result = useApi(apiFunction);
  
  return {
    ...result,
    refetch: result.execute,
  };
}

/**
 * Hook para mutações (POST, PUT, DELETE)
 * 
 * @example
 * const { mutate, isLoading, error } = useMutation(
 *   (data: UserData) => api.post('/users', data)
 * );
 * 
 * const handleSubmit = async () => {
 *   const result = await mutate({ name: 'John' });
 *   if (result) {
 *     toast.success('Usuário criado!');
 *   }
 * };
 */
export function useMutation<T, P extends unknown[] = []>(
  apiFunction: (...params: P) => Promise<AxiosResponse<T>>
): Omit<UseApiReturn<T, P>, 'execute'> & { mutate: (...params: P) => Promise<T | null> } {
  const { execute, ...rest } = useApi(apiFunction);
  
  return {
    ...rest,
    mutate: execute,
  };
}
