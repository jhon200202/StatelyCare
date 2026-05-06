import React, { useState } from 'react';
import { useQuery, useMutation, keepPreviousData } from '@tanstack/react-query';
import { employeeService } from '../../application/services/employeeService';
import { DataTable } from '../../../../components/ui/DataTable';
import { Modal } from '../../../../components/ui/Modal';
import { StaffForm } from '../components/StaffForm';
import type { Employee } from '../../domain/types/EmployeeTypes';
import { Plus, Trash2, Edit2, Search, ArrowUpDown } from 'lucide-react';

export const EmployeesList: React.FC = () => {
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [editingEmployee, setEditingEmployee] = useState<Employee | undefined>(undefined);
  const [searchTerm, setSearchTerm] = useState('');
  const [debouncedSearchTerm, setDebouncedSearchTerm] = useState('');
  const [sortBy, setSortBy] = useState('firstName');
  const [direction, setDirection] = useState('asc');

  React.useEffect(() => {
    const timer = setTimeout(() => {
      setDebouncedSearchTerm(searchTerm);
    }, 400);
    return () => clearTimeout(timer);
  }, [searchTerm]);

  const { data, isLoading, isFetching, error, refetch } = useQuery({
    queryKey: ['employees', debouncedSearchTerm, sortBy, direction],
    queryFn: () => employeeService.getAll(debouncedSearchTerm, sortBy, direction),
    placeholderData: keepPreviousData,
  });

  const createMutation = useMutation({
    mutationFn: employeeService.create,
    onSuccess: () => {
      setIsModalOpen(false);
      setEditingEmployee(undefined);
      refetch();
    },
  });

  const updateMutation = useMutation({
    mutationFn: ({ id, data }: { id: string; data: typeof editingEmployee }) => 
      employeeService.update(id, data as any),
    onSuccess: () => {
      setIsModalOpen(false);
      setEditingEmployee(undefined);
      refetch();
    },
  });

  const deleteMutation = useMutation({
    mutationFn: employeeService.delete,
    onSuccess: () => {
      refetch();
    },
  });

  const handleEdit = (employee: Employee) => {
    setEditingEmployee(employee);
    setIsModalOpen(true);
  };

  const handleDelete = (id: string) => {
    if (confirm('¿Estás seguro de eliminar este empleado?')) {
      deleteMutation.mutate(id);
    }
  };

  const handleCloseModal = () => {
    setIsModalOpen(false);
    setEditingEmployee(undefined);
  };

  const handleFormSubmit = (formData: any) => {
    if (editingEmployee) {
      updateMutation.mutate({ id: editingEmployee.id, data: formData });
    } else {
      createMutation.mutate(formData);
    }
  };

  const columns = [
    { key: 'employeeCode' as keyof Employee, header: 'ID Empleado' },
    { key: 'firstName' as keyof Employee, header: 'Nombre', render: (_: any, e: Employee) => `${e.firstName} ${e.lastName}` },
    { key: 'department' as keyof Employee, header: 'Departamento' },
    { key: 'roleTitle' as keyof Employee, header: 'Cargo' },
    { key: 'certificationStatus' as keyof Employee, header: 'Certificación' },
    { 
      key: 'isActive' as keyof Employee, 
      header: 'Estado',
      render: (isActive: boolean) => (
        <span className={`px-2 py-1 rounded-full text-xs font-semibold ${isActive ? 'bg-secondary-container text-on-secondaryContainer' : 'bg-surface-variant text-on-surfaceVariant'}`}>
          {isActive ? 'Activo' : 'Inactivo'}
        </span>
      )
    },
    {
      key: 'actions' as keyof Employee,
      header: 'Acciones',
      render: (_: any, e: Employee) => (
        <div className="flex items-center gap-2">
          <button onClick={() => handleEdit(e)} className="p-1 hover:text-primary" title="Editar">
            <Edit2 size={16} />
          </button>
          <button onClick={() => handleDelete(e.id)} className="p-1 hover:text-error" title="Eliminar">
            <Trash2 size={16} />
          </button>
        </div>
      ),
    },
  ];

  if (isLoading && !data) return <div className="text-on-surfaceVariant p-8 text-center">Cargando empleados...</div>;
  if (error) return <div className="text-error p-8 text-center">Error al cargar empleados</div>;

  return (
    <div className="space-y-6">
      <div className="flex justify-between items-center">
        <div>
          <h1 className="text-h2 text-on-surface mb-2">Empleados</h1>
          <p className="text-on-surfaceVariant text-body-md">Gestión del equipo de trabajo de CarePulse.</p>
        </div>
        <button 
          onClick={() => {
            setEditingEmployee(undefined);
            setIsModalOpen(true);
          }}
          className="flex items-center gap-2 bg-primary text-on-primary px-4 py-2 rounded-md hover:shadow-md hover:bg-primary-container transition-all font-medium"
        >
          <Plus size={18} />
          <span>Nuevo Empleado</span>
        </button>
      </div>
      <div className="flex flex-col md:flex-row gap-4 items-end bg-surface-containerLow p-4 rounded-lg border border-outline-variant">
        <div className="flex-1 w-full">
          <div className="flex justify-between items-center mb-1">
            <label className="block text-sm font-medium text-on-surfaceVariant">Buscar empleado</label>
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
            <option value="hireDate">Fecha Contratación</option>
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

      <Modal isOpen={isModalOpen} onClose={handleCloseModal} title={editingEmployee ? 'Editar Empleado' : 'Agregar Nuevo Empleado'}>
        <StaffForm 
          onSubmit={handleFormSubmit} 
          onCancel={handleCloseModal} 
          isLoading={createMutation.isPending || updateMutation.isPending} 
          initialData={editingEmployee}
        />
      </Modal>
    </div>
  );
};
