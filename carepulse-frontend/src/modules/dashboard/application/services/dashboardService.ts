import apiClient from '../../../../shared/infrastructure/http/axiosConfig';
import type { DashboardMetrics } from '../../domain/types/DashboardTypes';

export const dashboardService = {
  getMetrics: async (): Promise<DashboardMetrics> => {
    const { data } = await apiClient.get<DashboardMetrics>('/dashboard/metrics');
    return data;
  },
};
