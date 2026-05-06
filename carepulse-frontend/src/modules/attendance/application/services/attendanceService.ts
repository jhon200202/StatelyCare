import apiClient from '../../../../shared/infrastructure/http/axiosConfig';
import type { AttendanceRecord } from '../../domain/types/AttendanceTypes';

export const attendanceService = {
  getDaily: async (date?: string): Promise<AttendanceRecord[]> => {
    const query = date ? `?date=${date}` : '';
    const { data } = await apiClient.get<AttendanceRecord[]>(`/attendance/daily${query}`);
    return data;
  },
  clockIn: async (employeeId: string): Promise<AttendanceRecord> => {
    const { data } = await apiClient.post<AttendanceRecord>(`/attendance/clock-in/${employeeId}`);
    return data;
  },
  clockOut: async (employeeId: string): Promise<AttendanceRecord> => {
    const { data } = await apiClient.post<AttendanceRecord>(`/attendance/clock-out/${employeeId}`);
    return data;
  },
};
