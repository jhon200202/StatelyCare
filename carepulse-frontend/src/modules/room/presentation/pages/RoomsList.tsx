import React, { useState } from 'react';
import { useQuery, useMutation } from '@tanstack/react-query';
import { roomService } from '../../application/services/roomService';
import { DataTable } from '../../../../components/ui/DataTable';
import { Modal } from '../../../../components/ui/Modal';
import { DoorOpen, Plus, Building2, Pencil, PowerOff } from 'lucide-react';
import type { Room, CreateRoomRequest } from '../../domain/types/RoomTypes';

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

export const RoomsList: React.FC = () => {
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [isEditModalOpen, setIsEditModalOpen] = useState(false);
  const [editingRoom, setEditingRoom] = useState<Room | null>(null);
  const [filterStatus, setFilterStatus] = useState<'ALL' | 'AVAILABLE' | 'OCCUPIED' | 'INACTIVE'>('ALL');
  const [formData, setFormData] = useState<CreateRoomRequest>({
    roomNumber: '',
    wing: 'EAST',
    floor: 1,
    capacity: 1,
    roomType: 'PRIVATE'
  });

  const { data: rooms = [], isLoading, refetch } = useQuery({
    queryKey: ['rooms'],
    queryFn: roomService.getAll,
  });

  // Filtering logic
  const filteredRooms = rooms.filter(r => {
    if (filterStatus === 'ALL') return true;
    if (filterStatus === 'AVAILABLE') return r.isActive && r.currentOccupancy < r.capacity;
    if (filterStatus === 'OCCUPIED') return r.isActive && r.currentOccupancy >= r.capacity;
    if (filterStatus === 'INACTIVE') return !r.isActive;
    return true;
  });

  const availableRoomsCount = rooms.filter(r => r.isActive && r.currentOccupancy < r.capacity).length;
  const occupiedRoomsCount = rooms.filter(r => r.isActive && r.currentOccupancy >= r.capacity).length;
  const inactiveRoomsCount = rooms.filter(r => !r.isActive).length;

  const createMutation = useMutation({
    mutationFn: roomService.create,
    onSuccess: () => {
      setIsModalOpen(false);
      setFormData({ roomNumber: '', wing: 'EAST', floor: 1, capacity: 1, roomType: 'PRIVATE' });
      refetch();
    },
    onError: () => alert('Error al crear la habitación'),
  });

  const updateMutation = useMutation({
    mutationFn: ({ id, data }: { id: string; data: CreateRoomRequest }) => roomService.update(id, data),
    onSuccess: () => {
      setIsEditModalOpen(false);
      setEditingRoom(null);
      refetch();
    },
    onError: () => alert('Error al actualizar la habitación'),
  });

  const deactivateMutation = useMutation({
    mutationFn: roomService.deactivate,
    onSuccess: () => {
      refetch();
    },
    onError: () => alert('Error al desactivar la habitación'),
  });

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    createMutation.mutate(formData);
  };

  const handleEdit = (room: Room) => {
    setEditingRoom(room);
    setFormData({
      roomNumber: room.roomNumber,
      wing: room.wing,
      floor: room.floor,
      capacity: room.capacity,
      roomType: room.roomType
    });
    setIsEditModalOpen(true);
  };

  const handleUpdate = (e: React.FormEvent) => {
    e.preventDefault();
    if (editingRoom) {
      updateMutation.mutate({ id: editingRoom.id, data: formData });
    }
  };

  const columns = [
    { key: 'roomNumber' as keyof Room, header: 'Número' },
    { 
      key: 'roomType' as keyof Room, 
      header: 'Tipo',
      render: (val: string) => <span className="px-2 py-1 rounded-full text-xs font-bold bg-secondary-container text-on-secondaryContainer">{roomTypeLabels[val] || val}</span>
    },
    { 
      key: 'wing' as keyof Room, 
      header: 'Ala',
      render: (val: string) => wingLabels[val] || val
    },
    { key: 'floor' as keyof Room, header: 'Piso' },
    { 
      key: 'capacity' as keyof Room, 
      header: 'Capacidad',
      render: (val: number, row: Room) => (
        <span className={row.currentOccupancy >= row.capacity ? 'text-error font-bold' : ''}>
          {row.currentOccupancy}/{val}
        </span>
      )
    },
    { 
      key: 'isActive' as keyof Room, 
      header: 'Estado',
      render: (val: boolean) => (
        <span className={`px-2 py-1 rounded-full text-xs font-bold ${val ? 'bg-success-container text-on-successContainer' : 'bg-error-container text-on-errorContainer'}`}>
          {val ? 'Activa' : 'Inactiva'}
        </span>
      )
    },
    { 
      key: 'id' as keyof Room, 
      header: 'Acciones',
      render: (_: any, row: Room) => (
        <div className="flex gap-2">
          <button
            onClick={() => handleEdit(row)}
            className="p-2 text-primary hover:bg-primary-container rounded-md transition-colors"
            title="Editar habitación"
          >
            <Pencil size={16} />
          </button>
          {row.isActive && row.currentOccupancy === 0 && (
            <button
              onClick={() => {
                if (confirm('¿Está seguro que desea desactivar esta habitación?')) {
                  deactivateMutation.mutate(row.id);
                }
              }}
              disabled={deactivateMutation.isPending}
              className="p-2 text-error hover:bg-error-container rounded-md transition-colors"
              title="Desactivar habitación"
            >
              <PowerOff size={16} />
            </button>
          )}
        </div>
      )
    },
  ];

  return (
    <div className="space-y-6">
      <div className="flex justify-between items-center">
        <div>
          <h1 className="text-h2 text-onSurface mb-2">Gestión de Habitaciones</h1>
          <p className="text-onSurfaceVariant text-body-md">Administra las habitaciones del hogar.</p>
        </div>
        <button 
          onClick={() => setIsModalOpen(true)}
          className="flex items-center gap-2 h-11 px-6 bg-primary text-onPrimary rounded-md font-bold hover:shadow-lg hover:bg-primary-container transition-all text-white"
        >
          <Plus size={20} />
          NUEVA HABITACIÓN
        </button>
      </div>

      <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
        <button 
          onClick={() => setFilterStatus('AVAILABLE')}
          className={`text-left transition-all p-4 rounded-lg border ${filterStatus === 'AVAILABLE' ? 'border-primary ring-1 ring-primary bg-primary-container/20' : 'border-outline-variant bg-surface-containerLow hover:bg-surface-container'}`}
        >
          <div className="flex items-center gap-3">
            <div className="w-12 h-12 rounded-full bg-primary-container flex items-center justify-center">
              <DoorOpen className="text-primary" size={24} />
            </div>
            <div>
              <p className="text-2xl font-bold text-onSurface">{availableRoomsCount}</p>
              <p className="text-sm text-onSurfaceVariant">Disponibles</p>
            </div>
          </div>
        </button>
        <button 
          onClick={() => setFilterStatus('OCCUPIED')}
          className={`text-left transition-all p-4 rounded-lg border ${filterStatus === 'OCCUPIED' ? 'border-error ring-1 ring-error bg-error-container/20' : 'border-outline-variant bg-surface-containerLow hover:bg-surface-container'}`}
        >
          <div className="flex items-center gap-3">
            <div className="w-12 h-12 rounded-full bg-error-container flex items-center justify-center">
              <Building2 className="text-error" size={24} />
            </div>
            <div>
              <p className="text-2xl font-bold text-onSurface">{occupiedRoomsCount}</p>
              <p className="text-sm text-onSurfaceVariant">Ocupadas (Llenas)</p>
            </div>
          </div>
        </button>
        <button 
          onClick={() => setFilterStatus('INACTIVE')}
          className={`text-left transition-all p-4 rounded-lg border ${filterStatus === 'INACTIVE' ? 'border-outline ring-1 ring-outline bg-surface-variant/20' : 'border-outline-variant bg-surface-containerLow hover:bg-surface-container'}`}
        >
          <div className="flex items-center gap-3">
            <div className="w-12 h-12 rounded-full bg-surface-variant flex items-center justify-center">
              <DoorOpen className="text-onSurfaceVariant" size={24} />
            </div>
            <div>
              <p className="text-2xl font-bold text-onSurface">{inactiveRoomsCount}</p>
              <p className="text-sm text-onSurfaceVariant">Inactivas</p>
            </div>
          </div>
        </button>
      </div>

      <div className="bg-surface-containerLowest rounded-lg shadow-sm border border-outline-variant overflow-hidden">
        <div className="p-4 border-b border-outline-variant flex flex-col md:flex-row justify-between items-start md:items-center gap-4 bg-surface-containerLow">
          <div className="flex items-center gap-2">
            <DoorOpen size={20} className="text-primary" />
            <h2 className="text-h3 text-onSurface">Habitaciones</h2>
          </div>
          
          <div className="flex items-center gap-2 w-full md:w-auto">
            <span className="text-sm font-medium text-onSurfaceVariant whitespace-nowrap">Filtrar:</span>
            <select
              value={filterStatus}
              onChange={(e) => setFilterStatus(e.target.value as any)}
              className="px-3 py-1.5 rounded-md border border-outline-variant bg-surface-containerLowest text-sm focus:ring-2 focus:ring-primary outline-none min-w-[140px]"
            >
              <option value="ALL">Todas</option>
              <option value="AVAILABLE">Disponibles</option>
              <option value="OCCUPIED">Ocupadas (Llenas)</option>
              <option value="INACTIVE">Inactivas</option>
            </select>
          </div>
        </div>
        {isLoading ? (
          <div className="p-8 text-onSurfaceVariant text-center">Cargando habitaciones...</div>
        ) : filteredRooms.length === 0 ? (
          <div className="p-8 text-onSurfaceVariant italic text-center">No hay habitaciones que coincidan con el filtro.</div>
        ) : (
          <DataTable data={filteredRooms} columns={columns} />
        )}
      </div>

      <Modal isOpen={isModalOpen} onClose={() => setIsModalOpen(false)} title="Crear Nueva Habitación">
        <form onSubmit={handleSubmit} className="space-y-4">
          <div>
            <label className="block text-sm font-medium text-onSurface mb-1">Número de Habitación</label>
            <input
              type="text"
              value={formData.roomNumber}
              onChange={(e) => setFormData({ ...formData, roomNumber: e.target.value })}
              className="w-full px-4 py-2.5 rounded-md border border-outline-variant focus:ring-2 focus:ring-primary outline-none"
              required
            />
          </div>
          
          <div className="grid grid-cols-2 gap-4">
            <div>
              <label className="block text-sm font-medium text-onSurface mb-1">Tipo</label>
              <select
                value={formData.roomType}
                onChange={(e) => setFormData({ ...formData, roomType: e.target.value as any })}
                className="w-full px-4 py-2.5 rounded-md border border-outline-variant focus:ring-2 focus:ring-primary outline-none"
              >
                <option value="PRIVATE">Privada</option>
                <option value="SHARED">Compartida</option>
                <option value="SUITE">Suite</option>
              </select>
            </div>
            <div>
              <label className="block text-sm font-medium text-onSurface mb-1">Ala</label>
              <select
                value={formData.wing}
                onChange={(e) => setFormData({ ...formData, wing: e.target.value as any })}
                className="w-full px-4 py-2.5 rounded-md border border-outline-variant focus:ring-2 focus:ring-primary outline-none"
              >
                <option value="EAST">Este</option>
                <option value="WEST">Oeste</option>
                <option value="MEMORY_CARE">Cuidados de Memoria</option>
                <option value="REHABILITATION">Rehabilitación</option>
              </select>
            </div>
          </div>

          <div className="grid grid-cols-2 gap-4">
            <div>
              <label className="block text-sm font-medium text-onSurface mb-1">Piso</label>
              <input
                type="number"
                min="1"
                value={formData.floor}
                onChange={(e) => setFormData({ ...formData, floor: parseInt(e.target.value) })}
                className="w-full px-4 py-2.5 rounded-md border border-outline-variant focus:ring-2 focus:ring-primary outline-none"
                required
              />
            </div>
            <div>
              <label className="block text-sm font-medium text-onSurface mb-1">Capacidad</label>
              <input
                type="number"
                min="1"
                value={formData.capacity}
                onChange={(e) => setFormData({ ...formData, capacity: parseInt(e.target.value) })}
                className="w-full px-4 py-2.5 rounded-md border border-outline-variant focus:ring-2 focus:ring-primary outline-none"
                required
              />
            </div>
          </div>

          <div className="flex justify-end gap-3 pt-4">
            <button
              type="button"
              onClick={() => setIsModalOpen(false)}
              className="px-4 py-2 text-onSurfaceVariant hover:bg-surface-container rounded-md"
            >
              Cancelar
            </button>
            <button
              type="submit"
              disabled={createMutation.isPending}
              className="px-6 py-2 bg-primary text-onPrimary rounded-md font-bold hover:bg-primary-container transition-all disabled:opacity-50"
            >
              {createMutation.isPending ? 'Creando...' : 'Crear'}
            </button>
          </div>
        </form>
      </Modal>

      <Modal isOpen={isEditModalOpen} onClose={() => setIsEditModalOpen(false)} title="Editar Habitación">
        <form onSubmit={handleUpdate} className="space-y-4">
          <div>
            <label className="block text-sm font-medium text-onSurface mb-1">Número de Habitación</label>
            <input
              type="text"
              value={formData.roomNumber}
              onChange={(e) => setFormData({ ...formData, roomNumber: e.target.value })}
              className="w-full px-4 py-2.5 rounded-md border border-outline-variant focus:ring-2 focus:ring-primary outline-none"
              required
            />
          </div>
          
          <div className="grid grid-cols-2 gap-4">
            <div>
              <label className="block text-sm font-medium text-onSurface mb-1">Tipo</label>
              <select
                value={formData.roomType}
                onChange={(e) => setFormData({ ...formData, roomType: e.target.value as any })}
                className="w-full px-4 py-2.5 rounded-md border border-outline-variant focus:ring-2 focus:ring-primary outline-none"
              >
                <option value="PRIVATE">Privada</option>
                <option value="SHARED">Compartida</option>
                <option value="SUITE">Suite</option>
              </select>
            </div>
            <div>
              <label className="block text-sm font-medium text-onSurface mb-1">Ala</label>
              <select
                value={formData.wing}
                onChange={(e) => setFormData({ ...formData, wing: e.target.value as any })}
                className="w-full px-4 py-2.5 rounded-md border border-outline-variant focus:ring-2 focus:ring-primary outline-none"
              >
                <option value="EAST">Este</option>
                <option value="WEST">Oeste</option>
                <option value="MEMORY_CARE">Cuidados de Memoria</option>
                <option value="REHABILITATION">Rehabilitación</option>
              </select>
            </div>
          </div>

          <div className="grid grid-cols-2 gap-4">
            <div>
              <label className="block text-sm font-medium text-onSurface mb-1">Piso</label>
              <input
                type="number"
                min="1"
                value={formData.floor}
                onChange={(e) => setFormData({ ...formData, floor: parseInt(e.target.value) })}
                className="w-full px-4 py-2.5 rounded-md border border-outline-variant focus:ring-2 focus:ring-primary outline-none"
                required
              />
            </div>
            <div>
              <label className="block text-sm font-medium text-onSurface mb-1">Capacidad</label>
              <input
                type="number"
                min="1"
                value={formData.capacity}
                onChange={(e) => setFormData({ ...formData, capacity: parseInt(e.target.value) })}
                className="w-full px-4 py-2.5 rounded-md border border-outline-variant focus:ring-2 focus:ring-primary outline-none"
                required
              />
            </div>
          </div>

          <div className="flex justify-end gap-3 pt-4">
            <button
              type="button"
              onClick={() => setIsEditModalOpen(false)}
              className="px-4 py-2 text-onSurfaceVariant hover:bg-surface-container rounded-md"
            >
              Cancelar
            </button>
            <button
              type="submit"
              disabled={updateMutation.isPending}
              className="px-6 py-2 bg-primary text-onPrimary rounded-md font-bold hover:bg-primary-container transition-all disabled:opacity-50"
            >
              {updateMutation.isPending ? 'Guardando...' : 'Guardar'}
            </button>
          </div>
        </form>
      </Modal>
    </div>
  );
};