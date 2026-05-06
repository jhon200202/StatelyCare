import React, { useState } from 'react';
import { useQuery } from '@tanstack/react-query';
import { roomService } from '../../application/services/roomService';
import { Modal } from '../../../../components/ui/Modal';
import { DataTable } from '../../../../components/ui/DataTable';
import { Search, DoorOpen } from 'lucide-react';
import type { Room } from '../../domain/types/RoomTypes';

interface RoomPickerModalProps {
  isOpen: boolean;
  onClose: () => void;
  onSelect: (room: Room) => void;
}

const wingLabels: Record<string, string> = {
  EAST: 'Este',
  WEST: 'Oeste',
  MEMORY_CARE: 'Cuidados de Memoria',
  REHABILITATION: 'Rehabilitación'
};

const roomTypeLabels: Record<string, string> = {
  PRIVATE: 'Privada',
  SHARED: 'Compartida',
  SUITE: 'Suite'
};

export const RoomPickerModal: React.FC<RoomPickerModalProps> = ({ 
  isOpen, 
  onClose, 
  onSelect
}) => {
  const [searchTerm, setSearchTerm] = useState('');

  const { data: rooms = [], isLoading } = useQuery({
    queryKey: ['rooms'],
    queryFn: roomService.getAll,
  });

  const availableRooms = rooms.filter(room => {
    const matchesSearch = room.roomNumber.toLowerCase().includes(searchTerm.toLowerCase()) ||
                         wingLabels[room.wing]?.toLowerCase().includes(searchTerm.toLowerCase()) ||
                         roomTypeLabels[room.roomType]?.toLowerCase().includes(searchTerm.toLowerCase());
    
    return room.isActive && room.currentOccupancy < room.capacity && matchesSearch;
  });

  const columns = [
    { key: 'roomNumber' as keyof Room, header: 'Número' },
    { 
      key: 'roomType' as keyof Room, 
      header: 'Tipo',
      render: (val: string) => roomTypeLabels[val] || val
    },
    { 
      key: 'wing' as keyof Room, 
      header: 'Ala',
      render: (val: string) => wingLabels[val] || val
    },
    { key: 'floor' as keyof Room, header: 'Piso' },
    { 
      key: 'capacity' as keyof Room, 
      header: 'Ocupación',
      render: (val: number, row: Room) => (
        <span className={`font-bold ${row.currentOccupancy >= row.capacity ? 'text-error' : 'text-success'}`}>
          {row.currentOccupancy}/{val}
        </span>
      )
    },
    { 
      key: 'id' as keyof Room, 
      header: 'Seleccionar',
      render: (_: any, row: Room) => (
        <button
          onClick={() => {
            onSelect(row);
            onClose();
          }}
          className="p-2 text-primary hover:bg-primary-container rounded-full transition-colors"
          title="Seleccionar habitación"
        >
          <DoorOpen size={20} />
        </button>
      )
    }
  ];

  return (
    <Modal isOpen={isOpen} onClose={onClose} title="Explorar Habitaciones Disponibles">
      <div className="space-y-4">
        <div className="relative">
          <Search className="absolute left-3 top-1/2 -translate-y-1/2 text-on-surfaceVariant" size={18} />
          <input
            type="text"
            placeholder="Buscar por número, ala o tipo..."
            value={searchTerm}
            onChange={(e) => setSearchTerm(e.target.value)}
            className="w-full pl-10 pr-4 py-2 rounded-md border border-outline-variant focus:ring-2 focus:ring-primary outline-none transition-all"
          />
        </div>

        {availableRooms.length === 0 ? (
          <div className="p-8 text-center text-on-surfaceVariant">
            No hay habitaciones disponibles.
          </div>
        ) : (
          <div className="border border-outline-variant rounded-lg overflow-hidden max-h-[60vh] overflow-y-auto">
            <DataTable 
              data={availableRooms} 
              columns={columns} 
              onRowDoubleClick={(room) => {
                onSelect(room);
                onClose();
              }}
            />
          </div>
        )}
      </div>
    </Modal>
  );
};