import apiClient from '../../../../shared/infrastructure/http/axiosConfig';
import type { Employee, CreateEmployeeData } from '../../domain/types/EmployeeTypes';

export const employeeService = {
  getAll: async (query?: string, sortBy?: string, direction?: string): Promise<Employee[]> => {
    const { data } = await apiClient.get<Employee[]>('/employees', {
      params: { query, sortBy, direction }
    });
    return data;
  },
  create: async (employeeData: CreateEmployeeData): Promise<Employee> => {
    const { data } = await apiClient.post<Employee>('/employees', employeeData);
    return data;
  },
  update: async (id: string, employeeData: CreateEmployeeData): Promise<Employee> => {
    const { data } = await apiClient.put<Employee>(`/employees/${id}`, employeeData);
    return data;
  },
  delete: async (id: string): Promise<void> => {
    await apiClient.delete(`/employees/${id}`);
  },
};
