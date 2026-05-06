import React from 'react';
import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import { z } from 'zod';
import type { CreateUserData, UpdateUserData, User } from '../../domain/types/UserTypes';

const userSchema = z.object({
  username: z.string().min(3, 'El nombre de usuario debe tener al menos 3 caracteres'),
  email: z.string().email('Correo electrónico inválido'),
  passwordHash: z.string().min(6, 'La contraseña debe tener al menos 6 caracteres'),
  role: z.enum(['ADMIN', 'NURSE', 'DOCTOR', 'STAFF', 'FAMILY']),
  isActive: z.boolean(),
});

const userSchemaEdit = z.object({
  username: z.string().min(3, 'El nombre de usuario debe tener al menos 3 caracteres').optional(),
  email: z.string().email('Correo electrónico inválido').optional(),
  passwordHash: z.string().min(6, 'La contraseña debe tener al menos 6 caracteres').optional(),
  role: z.enum(['ADMIN', 'NURSE', 'DOCTOR', 'STAFF', 'FAMILY']).optional(),
  isActive: z.boolean().optional(),
});

interface UserFormProps {
  onSubmit: (data: CreateUserData | UpdateUserData) => void;
  onCancel: () => void;
  isLoading: boolean;
  editData?: User;
}

export const UserForm: React.FC<UserFormProps> = ({ onSubmit, onCancel, isLoading, editData }) => {
  const schema = editData ? userSchemaEdit : userSchema;

  const { register, handleSubmit, formState: { errors } } = useForm<CreateUserData | UpdateUserData>({
    resolver: zodResolver(schema) as any,
    defaultValues: editData ? {
      username: editData.username,
      email: editData.email,
      role: editData.role,
      isActive: editData.isActive,
    } : {
      role: 'STAFF',
      isActive: true,
    }
  });

  const getRoleLabel = (role: string): string => {
    const labels: Record<string, string> = {
      'ADMIN': 'Administrador',
      'NURSE': 'Enfermero',
      'DOCTOR': 'Médico',
      'STAFF': 'Personal',
      'FAMILY': 'Familia',
    };
    return labels[role] || role;
  };

  return (
    <form onSubmit={handleSubmit(onSubmit)} className="space-y-4">
      <div className="grid grid-cols-2 gap-4">
        <div className="space-y-1">
          <label className="block text-sm font-medium text-on-surface">Nombre de Usuario</label>
          <input 
            type="text"
            {...register('username')}
            className="w-full px-4 py-2 rounded-md border border-outline-variant focus:ring-2 focus:ring-primary outline-none"
          />
          {errors.username && <span className="text-xs text-error">{errors.username.message as string}</span>}
        </div>
        <div className="space-y-1">
          <label className="block text-sm font-medium text-on-surface">Correo Electrónico</label>
          <input 
            type="email"
            {...register('email')}
            className="w-full px-4 py-2 rounded-md border border-outline-variant focus:ring-2 focus:ring-primary outline-none"
          />
          {errors.email && <span className="text-xs text-error">{errors.email.message as string}</span>}
        </div>
      </div>

      {!editData && (
        <div className="space-y-1">
          <label className="block text-sm font-medium text-on-surface">Contraseña</label>
          <input 
            type="password"
            {...register('passwordHash')}
            className="w-full px-4 py-2 rounded-md border border-outline-variant focus:ring-2 focus:ring-primary outline-none"
          />
          {errors.passwordHash && <span className="text-xs text-error">{errors.passwordHash.message as string}</span>}
        </div>
      )}

      {editData && (
        <div className="space-y-1">
          <label className="block text-sm font-medium text-on-surface">Nueva Contraseña (dejar en blanco para mantener)</label>
          <input 
            type="password"
            {...register('passwordHash')}
            placeholder="Ingrese solo si desea cambiar"
            className="w-full px-4 py-2 rounded-md border border-outline-variant focus:ring-2 focus:ring-primary outline-none"
          />
        </div>
      )}

      <div className="grid grid-cols-2 gap-4">
        <div className="space-y-1">
          <label className="block text-sm font-medium text-on-surface">Rol</label>
          <select {...register('role')} className="w-full px-4 py-2 rounded-md border border-outline-variant focus:ring-2 focus:ring-primary outline-none bg-surface-containerLowest">
            <option value="ADMIN">{getRoleLabel('ADMIN')}</option>
            <option value="NURSE">{getRoleLabel('NURSE')}</option>
            <option value="DOCTOR">{getRoleLabel('DOCTOR')}</option>
            <option value="STAFF">{getRoleLabel('STAFF')}</option>
            <option value="FAMILY">{getRoleLabel('FAMILY')}</option>
          </select>
        </div>
        <div className="space-y-1">
          <label className="block text-sm font-medium text-on-surface">Estado</label>
          <div className="flex items-center gap-4 mt-2">
            <label className="flex items-center gap-2 cursor-pointer">
              <input 
                type="checkbox"
                {...register('isActive')}
                className="w-4 h-4 rounded border-outlineVariant text-primary focus:ring-primary"
              />
              <span className="text-sm text-on-surface">Activo</span>
            </label>
          </div>
        </div>
      </div>

      <div className="flex justify-end gap-3 pt-4">
        <button type="button" onClick={onCancel} className="px-4 py-2 text-primary font-medium hover:bg-surface-container rounded-md transition-colors">
          Cancelar
        </button>
        <button type="submit" disabled={isLoading} className="px-6 py-2 bg-primary text-on-primary font-bold rounded-md hover:shadow-lg transition-all disabled:opacity-50">
          {isLoading ? 'Procesando...' : editData ? 'Actualizar Usuario' : 'Crear Usuario'}
        </button>
      </div>
    </form>
  );
};