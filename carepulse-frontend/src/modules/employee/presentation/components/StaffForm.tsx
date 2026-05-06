import React from 'react';
import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import { z } from 'zod';
import type { CreateEmployeeData } from '../../domain/types/EmployeeTypes';

const employeeSchema = z.object({
  id: z.string().optional(),
  userId: z.string().uuid('Debe seleccionar un usuario válido').optional(),
  firstName: z.string().min(1, 'El nombre es obligatorio'),
  lastName: z.string().min(1, 'El apellido es obligatorio'),
  department: z.enum(['NURSING', 'MEDICAL', 'ADMIN', 'MAINTENANCE', 'KITCHEN']),
  roleTitle: z.string().min(1, 'El cargo es obligatorio'),
  hireDate: z.string().min(1, 'La fecha de contratación es obligatoria'),
}).passthrough(); 

interface StaffFormProps {
  onSubmit: (data: any) => void;
  onCancel: () => void;
  isLoading: boolean;
  initialData?: CreateEmployeeData;
}

export const StaffForm: React.FC<StaffFormProps> = ({ onSubmit, onCancel, isLoading, initialData }) => {
  const { register, handleSubmit, formState: { errors } } = useForm<any>({
    resolver: zodResolver(employeeSchema),
    defaultValues: initialData,
    values: initialData,
  });

  const onValidSubmit = (data: any) => {
    console.log("Formulario enviado con éxito:", data);
    onSubmit(data);
  };

  return (
    <form onSubmit={handleSubmit(onValidSubmit)} className="space-y-4">
      <div className="grid grid-cols-2 gap-4">
        <div className="space-y-1">
          <label className="block text-sm font-medium text-on-surface">Nombre</label>
          <input type="text" {...register('firstName')} className="w-full px-4 py-2 rounded-md border border-outline-variant focus:ring-2 focus:ring-primary focus:border-transparent outline-none transition-all" />
          {errors.firstName && <span className="text-xs text-error">{errors.firstName.message as string}</span>}
        </div>
        <div className="space-y-1">
          <label className="block text-sm font-medium text-on-surface">Apellido</label>
          <input type="text" {...register('lastName')} className="w-full px-4 py-2 rounded-md border border-outline-variant focus:ring-2 focus:ring-primary focus:border-transparent outline-none transition-all" />
          {errors.lastName && <span className="text-xs text-error">{errors.lastName.message as string}</span>}
        </div>
      </div>
      
      <div className="grid grid-cols-2 gap-4">
        <div className="space-y-1">
          <label className="block text-sm font-medium text-on-surface">Departamento</label>
          <select {...register('department')} className="w-full px-4 py-2 rounded-md border border-outline-variant focus:ring-2 focus:ring-primary focus:border-transparent outline-none">
            <option value="NURSING">Enfermería</option>
            <option value="MEDICAL">Médico</option>
            <option value="ADMIN">Administración</option>
            <option value="MAINTENANCE">Mantenimiento</option>
            <option value="KITCHEN">Cocina</option>
          </select>
          {errors.department && <span className="text-xs text-error">{errors.department.message as string}</span>}
        </div>
        <div className="space-y-1">
          <label className="block text-sm font-medium text-on-surface">Cargo</label>
          <input type="text" {...register('roleTitle')} placeholder="Ej: Jefe de Enfermería" className="w-full px-4 py-2 rounded-md border border-outline-variant focus:ring-2 focus:ring-primary focus:border-transparent outline-none transition-all" />
          {errors.roleTitle && <span className="text-xs text-error">{errors.roleTitle.message as string}</span>}
        </div>
      </div>

      <div className="grid grid-cols-2 gap-4 pt-4 border-t border-outline-variant">
        <div className="space-y-1">
          <label className="block text-sm font-medium text-on-surface">Fecha de Contratación</label>
          <input type="date" {...register('hireDate')} className="w-full px-4 py-2 rounded-md border border-outline-variant focus:ring-2 focus:ring-primary focus:border-transparent outline-none" />
          {errors.hireDate && <span className="text-xs text-error">{errors.hireDate.message as string}</span>}
        </div>
        <div className="space-y-1">
          <label className="block text-sm font-medium text-on-surface">ID de Usuario (UUID)</label>
          <input type="text" {...register('userId')} placeholder="UUID del usuario vinculado" className="w-full px-4 py-2 rounded-md border border-outline-variant focus:ring-2 focus:ring-primary focus:border-transparent outline-none" />
          {errors.userId && <span className="text-xs text-error">{errors.userId.message as string}</span>}
        </div>
      </div>

      <div className="flex justify-end gap-3 pt-6">
        <button type="button" onClick={onCancel} className="px-4 py-2 text-primary font-medium hover:bg-surface-container rounded-md transition-colors">
          Cancelar
        </button>
        <button type="submit" disabled={isLoading} className="px-6 py-2 bg-primary text-on-primary font-bold rounded-md hover:shadow-lg hover:bg-primary-container transition-all disabled:opacity-50">
          {isLoading ? 'Guardando...' : 'Guardar Empleado'}
        </button>
      </div>
    </form>
  );
};
