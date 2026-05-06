import apiClient from '../../../../shared/infrastructure/http/axiosConfig';
import type { LoginResponse } from '../../domain/types/AuthTypes';
import { z } from 'zod';

export const loginSchema = z.object({
  email: z.string().email('Format de email inválido'),
  password: z.string().min(1, 'La contraseña es obligatoria'),
});

export type LoginCredentials = z.infer<typeof loginSchema>;

export const authService = {
  login: async (credentials: LoginCredentials): Promise<LoginResponse> => {
    const { data } = await apiClient.post<LoginResponse>('/auth/login', credentials);
    return data;
  },
};
