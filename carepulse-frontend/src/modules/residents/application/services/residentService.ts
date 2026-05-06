import apiClient from '../../../../shared/infrastructure/http/axiosConfig';
import type { Resident, CreateResidentData } from '../../domain/types/ResidentTypes';

export const residentService = {
  getAll: async (query?: string, sortBy?: string, direction?: string): Promise<Resident[]> => {
    const { data } = await apiClient.get<Resident[]>('/residents', {
      params: { query, sortBy, direction }
    });
    return data;
  },
  create: async (residentData: CreateResidentData): Promise<Resident> => {
    const { data } = await apiClient.post<Resident>('/residents', residentData);
    return data;
  },update: async (id, data) => {
    const { data: response } = await apiClient.put(`/residents/${id}`, data);
    return response;
  },
  delete: async (id: string): Promise<void> => {
    await apiClient.delete(`/residents/${id}`);
  },
};
