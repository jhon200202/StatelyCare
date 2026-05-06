import apiClient from '../../../../shared/infrastructure/http/axiosConfig';
import type { User, CreateUserData, UpdateUserData } from '../../domain/types/UserTypes';

export const userService = {
  getAllUsers: async (): Promise<User[]> => {
    const { data } = await apiClient.get<User[]>('/users');
    return data;
  },
  getUserById: async (id: string): Promise<User> => {
    const { data } = await apiClient.get<User>(`/users/${id}`);
    return data;
  },
  createUser: async (userData: CreateUserData): Promise<User> => {
    const { data } = await apiClient.post<User>('/users', userData);
    return data;
  },
  updateUser: async (id: string, userData: UpdateUserData): Promise<User> => {
    const { data } = await apiClient.put<User>(`/users/${id}`, userData);
    return data;
  },
  deleteUser: async (id: string): Promise<void> => {
    await apiClient.delete(`/users/${id}`);
  },
  toggleUserActive: async (id: string): Promise<User> => {
    const { data } = await apiClient.patch<User>(`/users/${id}/toggle-active`);
    return data;
  },
};