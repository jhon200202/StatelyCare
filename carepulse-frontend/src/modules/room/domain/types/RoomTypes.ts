export type Wing = 'EAST' | 'WEST' | 'MEMORY_CARE' | 'REHABILITATION';
export type RoomType = 'PRIVATE' | 'SHARED' | 'SUITE';

export interface Room {
  id: string;
  roomNumber: string;
  wing: Wing;
  floor: number;
  capacity: number;
  currentOccupancy: number;
  roomType: RoomType;
  isActive: boolean;
}

export interface CreateRoomRequest {
  roomNumber: string;
  wing: Wing;
  floor: number;
  capacity: number;
  roomType: RoomType;
}