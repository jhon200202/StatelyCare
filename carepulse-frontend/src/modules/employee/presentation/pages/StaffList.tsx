import React, { useState } from 'react';
import { useQuery, useMutation } from '@tanstack/react-query';
import { staffService } from '../../../staff/application/services/staffService';
import { DataTable } from '../../../../components/ui/DataTable';
import { Modal } from '../../../../components/ui/Modal';
import { StaffForm } from '../components/StaffForm';
import type { Staff } from '../../../staff/domain/types/StaffTypes';
import { Plus } from 'lucide-react';

export const StaffList: React.FC = () => {
  const [isModalOpen, setIsModalOpen] = useState(false);

  const { data, isLoading, error, refetch } = useQuery({
    queryKey: ['staff'],
    queryFn: staffService.getAll,
  });

  const { mutate, isPending } = useMutation({
    mutationFn: staffService.create,
    onSuccess: () => {
      setIsModalOpen(false);
      refetch();
    },
  });

  const columns = [
    { key: 'firstName' as keyof Staff, header: 'Nombre', render: (_: string, s: Staff) => `${s.firstName} ${s.lastName}` },
    { key: 'professionalRole' as keyof Staff, header: 'Rol Profesional' },
    { key: 'shift' as keyof Staff, header: 'Turno', render: (val: string) => val || '-' },
    { key: 'hireDate' as keyof Staff, header: 'Fecha Contratación' },
    { 
      key: 'isActive' as keyof Staff, 
      header: 'Estado',
      render: (isActive: boolean) => (
        <span className={`px-2 py-1 rounded-full text-xs font-semibold ${isActive ? 'bg-secondary-container text-on-secondaryContainer' : 'bg-surface-variant text-on-surfaceVariant'}`}>
          {isActive ? 'Activo' : 'Inactivo'}
        </span>
      )
    },
  ];

  if (isLoading) return <div className="text-on-surfaceVariant">Cargando personal...</div>;
  if (error) return <div className="text-error">Error al cargar personal</div>;

  return (
    <div className="space-y-6">
      <div className="flex justify-between items-center">
        <div>
          <h1 className="text-h2 text-on-surface mb-2">Personal</h1>
          <p className="text-on-surfaceVariant text-body-md">Gestión del equipo de trabajo de Stately Care.</p>
        </div>
        <button 
          onClick={() => setIsModalOpen(true)}
          className="flex items-center gap-2 bg-primary text-on-primary px-4 py-2 rounded-md hover:bg-primary-container transition-colors"
        >
          <Plus size={18} />
          <span>Nuevo Empleado</span>
        </button>
      </div>

      <DataTable data={data || []} columns={columns} />

      <Modal isOpen={isModalOpen} onClose={() => setIsModalOpen(false)} title="Agregar Nuevo Empleado">
        <StaffForm 
          onSubmit={(formData) => mutate(formData)} 
          onCancel={() => setIsModalOpen(false)} 
          isLoading={isPending} 
        />
      </Modal>
    </div>
  );
};
