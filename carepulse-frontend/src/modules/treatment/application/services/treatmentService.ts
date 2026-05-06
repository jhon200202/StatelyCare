import apiClient from '../../../../shared/infrastructure/http/axiosConfig';
import type { 
  Treatment, 
  MedicationAdministration, 
  CreateTreatmentRequest, 
  AdministerRequest,
  DailyTreatmentReminder
} from '../../domain/types/TreatmentTypes';

export const treatmentService = {
  getActiveByResident: async (residentId: string): Promise<Treatment[]> => {
    const { data } = await apiClient.get<Treatment[]>(`/treatments/resident/${residentId}`);
    return data;
  },
  getDailyTreatments: async (): Promise<DailyTreatmentReminder[]> => {
    const { data } = await apiClient.get<DailyTreatmentReminder[]>('/treatments/daily');
    return data;
  },
  create: async (treatmentData: CreateTreatmentRequest): Promise<Treatment> => {
    const { data } = await apiClient.post<Treatment>('/treatments', treatmentData);
    return data;
  },
  administerMedication: async (request: AdministerRequest): Promise<MedicationAdministration> => {
    const { data } = await apiClient.post<MedicationAdministration>('/treatments/administer', request);
    return data;
  },
  update: async (id: string, treatmentData: CreateTreatmentRequest): Promise<Treatment> => {
    const { data } = await apiClient.put<Treatment>(`/treatments/${id}`, treatmentData);
    return data;
  },
  delete: async (id: string): Promise<void> => {
    await apiClient.delete(`/treatments/${id}`);
  },
};
