import React from 'react';
import { useQuery, useMutation, useQueryClient, keepPreviousData } from '@tanstack/react-query';
import { residentService } from '../../application/services/residentService';
import { roomService } from '../../../room/application/services/roomService';
import { DataTable } from '../../../../components/ui/DataTable';
import { Modal } from '../../../../components/ui/Modal';
import { ResidentForm } from '../components/ResidentForm';
import type { Resident } from '../../domain/types/ResidentTypes';
import { Plus, Trash2, Edit2, Search, ArrowUpDown } from 'lucide-react';

export const ResidentsList: React.FC = () => {
  const [isModalOpen, setIsModalOpen] = React.useState(false);
  const [editingResident, setEditingResident] = React.useState<Resident | undefined>(undefined);
  const [searchTerm, setSearchTerm] = React.useState('');
  const [debouncedSearchTerm, setDebouncedSearchTerm] = React.useState('');
  const [sortBy, setSortBy] = React.useState('firstName');
  const [direction, setDirection] = React.useState('asc');
  const queryClient = useQueryClient();

  React.useEffect(() => {
    const timer = setTimeout(() => {
      setDebouncedSearchTerm(searchTerm);
    }, 400);
    return () => clearTimeout(timer);
  }, [searchTerm]);

  const { data, isLoading, isFetching, error, refetch } = useQuery({
    queryKey: ['residents', debouncedSearchTerm, sortBy, direction],
    queryFn: () => residentService.getAll(debouncedSearchTerm, sortBy, direction),
    placeholderData: keepPreviousData,
  });

  const { data: rooms = [] } = useQuery({
    queryKey: ['rooms'],
    queryFn: roomService.getAll,
  });

  const createMutation = useMutation({
    mutationFn: residentService.create,
    onSuccess: () => {
      setIsModalOpen(false);
      setEditingResident(undefined);
      refetch();
      queryClient.invalidateQueries({ queryKey: ['rooms'] });
    },
  });

  const updateMutation = useMutation({
    mutationFn: ({ id, data }: { id: string; data: any }) => residentService.update(id, data),
    onSuccess: () => {
      setIsModalOpen(false);
      setEditingResident(undefined);
      refetch();
      queryClient.invalidateQueries({ queryKey: ['rooms'] });
    },
  });

  const deleteMutation = useMutation({
    mutationFn: residentService.delete,
    onSuccess: () => {
      refetch();
    },
  });

  const getRoomNumber = (roomId: string | null | undefined) => {
    if (!roomId) return '-';
    const room = rooms.find(r => r.id === roomId);
    return room ? room.roomNumber : '-';
  };

  const handleEdit = (resident: Resident) => {
    setEditingResident(resident);
    setIsModalOpen(true);
  };

  const handleDelete = (id: string) => {
    if (confirm('¿Estás seguro de eliminar este residente?')) {
      deleteMutation.mutate(id);
    }
  };

  const handleCloseModal = () => {
    setIsModalOpen(false);
    setEditingResident(undefined);
  };

  const handleSubmit = (formData: any) => {
    if (editingResident) {
      updateMutation.mutate({ id: editingResident.id, data: formData });
    } else {
      createMutation.mutate(formData);
    }
  };

  const columns = [
    { key: 'residentCode' as keyof Resident, header: 'Código' },
    { key: 'firstName' as keyof Resident, header: 'Nombre', render: (_: any, r: Resident) => `${r.firstName} ${r.lastName}` },
    { key: 'gender' as keyof Resident, header: 'Género' },
    { 
      key: 'roomId' as keyof Resident, 
      header: 'Habitación',
      render: (_: any, r: Resident) => getRoomNumber(r.roomId)
    },
    { key: 'admissionDate' as keyof Resident, header: 'Fecha Ingreso' },
    { 
      key: 'status' as keyof Resident, 
      header: 'Estado',
      render: (status: string) => {
        const colors: Record<string, string> = {
          'ON_SITE': 'bg-secondary-container text-on-secondaryContainer',
          'HOSPITALIZED': 'bg-error-container text-on-errorContainer',
          'OUT_ON_LEAVE': 'bg-tertiary-container text-on-tertiaryContainer',
          'DECEASED': 'bg-surface-variant text-on-surfaceVariant'
        };
        return (
          <span className={`px-2 py-1 rounded-full text-xs font-semibold ${colors[status] || 'bg-surface-variant'}`}>
            {status}
          </span>
        );
      }
    },
    {
      key: 'actions' as keyof Resident,
      header: 'Acciones',
      render: (_: any, r: Resident) => (
        <div className="flex items-center gap-2">
          <button onClick={() => handleEdit(r)} className="p-1 hover:text-primary" title="Editar">
            <Edit2 size={16} />
          </button>
          <button onClick={() => handleDelete(r.id)} className="p-1 hover:text-error" title="Eliminar">
            <Trash2 size={16} />
          </button>
        </div>
      ),
    },
  ];

  if (isLoading) return <div className="text-on-surfaceVariant p-8">Cargando residentes...</div>;
  if (error) return <div className="text-error p-8">Error al cargar residentes</div>;

  return (
    <div className="space-y-6">
      <div className="flex justify-between items-center">
        <div>
          <h1 className="text-h2 text-on-surface mb-2">Residentes</h1>
          <p className="text-on-surfaceVariant text-body-md">Gestión y visualización del padrón de residentes.</p>
        </div>
        <button 
          onClick={() => setIsModalOpen(true)}
          className="flex items-center gap-2 bg-primary text-on-primary px-4 py-2 rounded-md hover:bg-primary-container transition-colors font-medium shadow-md"
        >
          <Plus size={18} />
          <span>Nuevo Residente</span>
        </button>
      </div>
      <div className="flex flex-col md:flex-row gap-4 items-end bg-surface-containerLow p-4 rounded-lg border border-outline-variant">
        <div className="flex-1 w-full">
          <div className="flex justify-between items-center mb-1">
            <label className="block text-sm font-medium text-on-surfaceVariant">Buscar residente</label>
            {isFetching && !isLoading && <span className="text-xs text-primary animate-pulse">Actualizando...</span>}
          </div>
          <div className="relative">
            <Search className="absolute left-3 top-1/2 -translate-y-1/2 text-on-surfaceVariant" size={18} />
            <input
              type="text"
              placeholder="Buscar por nombre o apellido..."
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
              className="w-full pl-10 pr-4 py-2 bg-surface-containerLowest border border-outline rounded-md focus:outline-none focus:ring-2 focus:ring-primary text-on-surface"
            />
          </div>
        </div>
        
        <div className="w-full md:w-48">
          <label className="block text-sm font-medium text-on-surfaceVariant mb-1">Ordenar por</label>
          <select
            value={sortBy}
            onChange={(e) => setSortBy(e.target.value)}
            className="w-full px-3 py-2 bg-surface-containerLowest border border-outline rounded-md focus:outline-none focus:ring-2 focus:ring-primary text-on-surface"
          >
            <option value="firstName">Nombre</option>
            <option value="lastName">Apellido</option>
            <option value="admissionDate">Fecha Ingreso</option>
          </select>
        </div>

        <div className="w-full md:w-40">
          <label className="block text-sm font-medium text-on-surfaceVariant mb-1">Dirección</label>
          <button
            onClick={() => setDirection(prev => prev === 'asc' ? 'desc' : 'asc')}
            className="flex items-center justify-between w-full px-3 py-2 bg-surface-containerLowest border border-outline rounded-md hover:bg-surface-containerLow transition-colors text-on-surface"
          >
            <span>{direction === 'asc' ? 'Ascendente' : 'Descendente'}</span>
            <ArrowUpDown size={16} className="text-on-surfaceVariant" />
          </button>
        </div>
      </div>

      <div className="bg-surface-containerLowest rounded-lg shadow-sm border border-outline-variant overflow-hidden">
        <DataTable data={data || []} columns={columns} />
      </div>

      <Modal isOpen={isModalOpen} onClose={handleCloseModal} title={editingResident ? 'Editar Residente' : 'Agregar Nuevo Residente'}>
        <ResidentForm 
          onSubmit={handleSubmit} 
          onCancel={handleCloseModal} 
          isLoading={createMutation.isPending || updateMutation.isPending} 
          initialData={editingResident}
        />
      </Modal>
    </div>
  );
};