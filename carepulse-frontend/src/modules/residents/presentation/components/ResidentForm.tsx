import React, { useState } from 'react';
import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import { z } from 'zod';
import { useQuery } from '@tanstack/react-query';
import { roomService } from '../../../room/application/services/roomService';
import { Globe } from 'lucide-react';
import { RoomPickerModal } from '../../../room/presentation/components/RoomPickerModal';
import type { CreateResidentData } from '../../domain/types/ResidentTypes';
import type { Room } from '../../../room/domain/types/RoomTypes';

const residentSchema = z.object({
  firstName: z.string().min(1, 'El nombre es obligatorio'),
  lastName: z.string().min(1, 'El apellido es obligatorio'),
  dateOfBirth: z.string().min(1, 'La fecha de nacimiento es obligatoria'),
  gender: z.enum(['MALE', 'FEMALE', 'OTHER']),
  roomId: z.string().uuid('Debe seleccionar una habitación válida').optional().nullable(),
  admissionDate: z.string().min(1, 'La fecha de ingreso es obligatoria'),
});

interface ResidentFormProps {
  onSubmit: (data: CreateResidentData) => void;
  onCancel: () => void;
  isLoading: boolean;
  initialData?: CreateResidentData;
}

export const ResidentForm: React.FC<ResidentFormProps> = ({ onSubmit, onCancel, isLoading, initialData }) => {
  const [isRoomPickerOpen, setIsRoomPickerOpen] = useState(false);
  const [selectedRoomName, setSelectedRoomName] = useState('');

  const { data: rooms = [] } = useQuery({
    queryKey: ['rooms'],
    queryFn: roomService.getAll,
  });

  const { register, handleSubmit, setValue, watch, formState: { errors } } = useForm<CreateResidentData>({
    resolver: zodResolver(residentSchema) as any,
    defaultValues: initialData,
  });

  const roomId = watch('roomId');

  const selectedRoom = rooms.find(r => r.id === roomId);

  const handleSelectRoom = (room: Room) => {
    setValue('roomId', room.id);
    setSelectedRoomName(room.roomNumber);
  };

  const handleClearRoom = () => {
    setValue('roomId', null);
    setSelectedRoomName('');
  };

  return (
    <form onSubmit={handleSubmit(onSubmit)} className="space-y-4">
      <div className="grid grid-cols-2 gap-4">
        <div className="space-y-1">
          <label className="block text-sm font-medium text-on-surface">Nombre</label>
          <input type="text" {...register('firstName')} className="w-full px-4 py-2 rounded-md border border-outline-variant focus:ring-2 focus:ring-primary focus:border-transparent transition-all outline-none" />
          {errors.firstName && <span className="text-xs text-error">{errors.firstName.message}</span>}
        </div>
        <div className="space-y-1">
          <label className="block text-sm font-medium text-on-surface">Apellido</label>
          <input type="text" {...register('lastName')} className="w-full px-4 py-2 rounded-md border border-outline-variant focus:ring-2 focus:ring-primary focus:border-transparent transition-all outline-none" />
          {errors.lastName && <span className="text-xs text-error">{errors.lastName.message}</span>}
        </div>
      </div>
      
      <div className="grid grid-cols-2 gap-4">
        <div className="space-y-1">
          <label className="block text-sm font-medium text-on-surface">Fecha de Nacimiento</label>
          <input type="date" {...register('dateOfBirth')} className="w-full px-4 py-2 rounded-md border border-outline-variant focus:ring-2 focus:ring-primary focus:border-transparent outline-none" />
          {errors.dateOfBirth && <span className="text-xs text-error">{errors.dateOfBirth.message}</span>}
        </div>
        <div className="space-y-1">
          <label className="block text-sm font-medium text-on-surface">Género</label>
          <select {...register('gender')} className="w-full px-4 py-2 rounded-md border border-outline-variant focus:ring-2 focus:ring-primary focus:border-transparent outline-none bg-surface-containerLowest">
            <option value="MALE">Masculino</option>
            <option value="FEMALE">Femenino</option>
            <option value="OTHER">Otro</option>
          </select>
          {errors.gender && <span className="text-xs text-error">{errors.gender.message}</span>}
        </div>
      </div>

      <div className="grid grid-cols-2 gap-4 pt-4 border-t border-outline-variant">
        <div className="space-y-1">
          <label className="block text-sm font-medium text-on-surface">Fecha de Ingreso</label>
          <input type="date" {...register('admissionDate')} className="w-full px-4 py-2 rounded-md border border-outline-variant focus:ring-2 focus:ring-primary focus:border-transparent outline-none" />
          {errors.admissionDate && <span className="text-xs text-error">{errors.admissionDate.message}</span>}
        </div>
        <div className="space-y-1">
          <label className="block text-sm font-medium text-on-surface">Habitación</label>
          <div className="flex gap-2">
            <div className="relative flex-1">
              <input 
                type="text"
                value={selectedRoom ? `${selectedRoom.roomNumber} (${selectedRoom.currentOccupancy}/${selectedRoom.capacity})` : selectedRoomName}
                readOnly
                placeholder="Seleccionar habitación..."
                className={`w-full px-4 py-2 rounded-md border border-outline-variant bg-surface-containerLow outline-none ${errors.roomId ? 'border-error' : ''}`}
              />
              {selectedRoom && (
                <button
                  type="button"
                  onClick={handleClearRoom}
                  className="absolute right-2 top-1/2 -translate-y-1/2 text-on-surfaceVariant hover:text-error"
                >
                  ✕
                </button>
              )}
            </div>
            <button
              type="button"
              onClick={() => setIsRoomPickerOpen(true)}
              className="px-3 bg-secondary text-on-secondary rounded-md hover:bg-secondary-container transition-all flex items-center gap-1 text-xs font-bold"
            >
              <Globe size={16} />
              EXPLORAR
            </button>
          </div>
          {errors.roomId && <span className="text-xs text-error">{errors.roomId.message}</span>}
        </div>
      </div>

      <input type="hidden" {...register('roomId')} />

      <RoomPickerModal 
        isOpen={isRoomPickerOpen} 
        onClose={() => setIsRoomPickerOpen(false)} 
        onSelect={handleSelectRoom}
      />

      <div className="flex justify-end gap-3 pt-6">
        <button type="button" onClick={onCancel} className="px-4 py-2 text-primary font-medium hover:bg-surface-container rounded-md transition-colors">
          Cancelar
        </button>
        <button type="submit" disabled={isLoading} className="px-6 py-2 bg-primary text-on-primary font-bold rounded-md hover:shadow-lg hover:bg-primary-container transition-all disabled:opacity-50">
          {isLoading ? 'Guardando...' : 'Guardar Residente'}
        </button>
      </div>
    </form>
  );
};
