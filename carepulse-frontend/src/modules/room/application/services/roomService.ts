import apiClient from '../../../../shared/infrastructure/http/axiosConfig';
import type { Room, CreateRoomRequest } from '../../domain/types/RoomTypes';

export const roomService = {
  getAll: async (): Promise<Room[]> => {
    const { data } = await apiClient.get<Room[]>('/rooms');
    return data;
  },
  create: async (roomData: CreateRoomRequest): Promise<Room> => {
    const { data } = await apiClient.post<Room>('/rooms', roomData);
    return data;
  },
  update: async (roomId: string, roomData: CreateRoomRequest): Promise<Room> => {
    const { data } = await apiClient.put<Room>(`/rooms/${roomId}`, roomData);
    return data;
  },
  deactivate: async (roomId: string): Promise<void> => {
    await apiClient.put(`/rooms/${roomId}/deactivate`);
  },
};