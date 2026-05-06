import apiClient from '../../../../shared/infrastructure/http/axiosConfig';
import type { Staff, CreateStaffData } from '../types/StaffTypes';

export const staffService = {
  getAll: async (): Promise<Staff[]> => {
    const { data } = await apiClient.get<Staff[]>('/staff');
    return data;
  },
  create: async (staffData: CreateStaffData): Promise<Staff> => {
    const { data } = await apiClient.post<Staff>('/staff', staffData);
    return data;
  },
};
