import apiClient from '../../../../shared/infrastructure/http/axiosConfig';
import type { 
  MenuItem, 
  DailyMenu, 
  MenuItemRequest, 
  DailyMenuRequest
} from '../../domain/types/NutritionTypes';

export const nutritionService = {
  getAllMenuItems: async (): Promise<MenuItem[]> => {
    const { data } = await apiClient.get<MenuItem[]>('/nutrition/items');
    return data;
  },
  createMenuItem: async (item: MenuItemRequest): Promise<MenuItem> => {
    const { data } = await apiClient.post<MenuItem>('/nutrition/items', item);
    return data;
  },
  updateMenuItem: async (id: string, item: MenuItemRequest): Promise<MenuItem> => {
    const { data } = await apiClient.put<MenuItem>(`/nutrition/items/${id}`, item);
    return data;
  },
  deleteMenuItem: async (id: string): Promise<void> => {
    await apiClient.delete(`/nutrition/items/${id}`);
  },
  getMenuByDate: async (date: string): Promise<DailyMenu[]> => {
    const { data } = await apiClient.get<DailyMenu[]>(`/nutrition/menu/${date}`);
    return data;
  },
  planDailyMenu: async (plan: DailyMenuRequest): Promise<DailyMenu> => {
    const { data } = await apiClient.post<DailyMenu>('/nutrition/menu', plan);
    return data;
  },
  updateDailyMenu: async (id: string, plan: DailyMenuRequest): Promise<DailyMenu> => {
    const { data } = await apiClient.put<DailyMenu>(`/nutrition/menu/${id}`, plan);
    return data;
  },
  deleteDailyMenu: async (id: string): Promise<void> => {
    await apiClient.delete(`/nutrition/menu/${id}`);
  },
  getResidentDiet: async (_residentId: string): Promise<any> => {
    return null;
  }
};
