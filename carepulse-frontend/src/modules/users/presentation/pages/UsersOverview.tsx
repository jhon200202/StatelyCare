import React, { useState } from 'react';
import { useQuery, useMutation } from '@tanstack/react-query';
import { userService } from '../../application/services/userService';
import { DataTable } from '../../../../components/ui/DataTable';
import { Modal } from '../../../../components/ui/Modal';
import { UserForm } from '../components/UserForm';
import type { User } from '../../domain/types/UserTypes';
import { Users, Plus, Edit2, Trash2, Shield, ShieldOff } from 'lucide-react';

export const UsersOverview: React.FC = () => {
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [editUser, setEditUser] = useState<User | null>(null);

  const { data: users = [], isLoading, refetch } = useQuery({
    queryKey: ['users'],
    queryFn: userService.getAllUsers,
  });

  const createMutation = useMutation({
    mutationFn: userService.createUser,
    onSuccess: () => {
      setIsModalOpen(false);
      setEditUser(null);
      refetch();
    },
    onError: () => alert('Error al crear el usuario'),
  });

  const deleteMutation = useMutation({
    mutationFn: userService.deleteUser,
    onSuccess: () => {
      refetch();
    },
    onError: () => alert('Error al eliminar el usuario'),
  });

  const updateMutation = useMutation({
    mutationFn: ({ id, data }: { id: string; data: any }) => userService.updateUser(id, data),
    onSuccess: () => {
      setIsModalOpen(false);
      setEditUser(null);
      refetch();
    },
    onError: () => alert('Error al actualizar el usuario'),
  });

  const handleEdit = (user: User) => {
    setEditUser(user);
    setIsModalOpen(true);
  };

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

  const getRoleBadgeColor = (role: string): string => {
    const colors: Record<string, string> = {
      'ADMIN': 'bg-purple-100 text-purple-800',
      'NURSE': 'bg-blue-100 text-blue-800',
      'DOCTOR': 'bg-green-100 text-green-800',
      'STAFF': 'bg-gray-100 text-gray-800',
      'FAMILY': 'bg-orange-100 text-orange-800',
    };
    return colors[role] || 'bg-gray-100 text-gray-800';
  };

  const columns = [
    { 
      key: 'username' as keyof User, 
      header: 'Usuario',
      render: (val: string, _row: User) => (
        <div className="flex items-center gap-2">
          <div className="w-8 h-8 rounded-full bg-primary/20 flex items-center justify-center">
            <span className="text-primary font-semibold text-sm">{val.charAt(0).toUpperCase()}</span>
          </div>
          <span className="font-medium">{val}</span>
        </div>
      )
    },
    { key: 'email' as keyof User, header: 'Correo' },
    { 
      key: 'role' as keyof User, 
      header: 'Rol',
      render: (val: string) => (
        <span className={`px-2 py-1 rounded-full text-xs font-medium ${getRoleBadgeColor(val)}`}>
          {getRoleLabel(val)}
        </span>
      )
    },
    { 
      key: 'isActive' as keyof User, 
      header: 'Estado',
      render: (val: boolean) => (
        <div className="flex items-center gap-1">
          {val ? (
            <Shield size={16} className="text-primary" />
          ) : (
            <ShieldOff size={16} className="text-error" />
          )}
          <span className={val ? 'text-primary' : 'text-error'}>
            {val ? 'Activo' : 'Inactivo'}
          </span>
        </div>
      )
    },
    { 
      key: 'lastLogin' as keyof User, 
      header: 'Último Acceso',
      render: (val: string | null) => val ? new Date(val).toLocaleDateString() : 'Nunca'
    },
    {
      key: 'actions' as keyof User,
      header: '',
      render: (_: any, u: User) => (
        <>
          <button
            onClick={() => handleEdit(u)}
            className="p-1 hover:text-primary"
            title="Editar"
          >
            <Edit2 size={16} />
          </button>
          <button
            onClick={() => {
              if (confirm('¿Estás seguro de eliminar este usuario?')) {
                deleteMutation.mutate(u.id);
              }
            }}
            className="p-1 hover:text-error ms-2"
            title="Eliminar"
          >
            <Trash2 size={16} />
          </button>
        </>
      ),
    },
  ];

  const activeUsersCount = users.filter(u => u.isActive).length;

  return (
    <div className="space-y-6">
      <div className="flex justify-between items-center">
        <div>
          <h1 className="text-h2 text-on-surface mb-2">Gestión de Usuarios</h1>
          <p className="text-on-surfaceVariant text-body-md">Administración de usuarios y permisos del sistema.</p>
        </div>
        <button 
          onClick={() => setIsModalOpen(true)}
          className="flex items-center gap-2 bg-primary text-on-primary px-6 py-2.5 rounded-md font-bold hover:shadow-lg hover:bg-primary-container transition-all"
        >
          <Plus size={18} />
          <span>Nuevo Usuario</span>
        </button>
      </div>

      <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
        <div className="bg-surface-containerLowest p-6 rounded-lg border border-outline-variant shadow-sm">
          <p className="text-sm text-on-surfaceVariant font-medium mb-1">Total de Usuarios</p>
          <h3 className="text-3xl font-bold text-on-surface">{users.length}</h3>
        </div>
        <div className="bg-surface-containerLowest p-6 rounded-lg border border-outline-variant shadow-sm">
          <p className="text-sm text-on-surfaceVariant font-medium mb-1">Usuarios Activos</p>
          <h3 className="text-3xl font-bold text-primary">{activeUsersCount}</h3>
        </div>
        <div className="bg-surface-containerLowest p-6 rounded-lg border border-outline-variant shadow-sm">
          <p className="text-sm text-on-surfaceVariant font-medium mb-1">Usuarios Inactivos</p>
          <h3 className="text-3xl font-bold text-error">{users.length - activeUsersCount}</h3>
        </div>
      </div>

      <div className="bg-surface-containerLowest rounded-lg shadow-sm border border-outline-variant overflow-hidden">
        <div className="p-4 border-b border-outline-variant flex items-center gap-2 bg-surface-containerLow">
          <Users size={20} className="text-primary" />
          <h2 className="text-h3 text-on-surface">Listado de Usuarios</h2>
        </div>
        {isLoading ? (
          <div className="p-8 text-on-surfaceVariant">Cargando usuarios...</div>
        ) : (
          <DataTable data={users} columns={columns} />
        )}
      </div>

      <Modal isOpen={isModalOpen} onClose={() => {
        setIsModalOpen(false);
        setEditUser(null);
      }} 
      title={editUser ? 'Editar Usuario' : 'Crear Usuario'}
      >
        <UserForm 
          editData={editUser || undefined}
          onSubmit={(data) => {
            if (editUser) {
              updateMutation.mutate({ id: editUser.id, data });
            } else {
              createMutation.mutate(data as any);
            }
          }} 
          onCancel={() => {
            setIsModalOpen(false);
            setEditUser(null);
          }} 
          isLoading={createMutation.isPending || updateMutation.isPending} 
        />
      </Modal>
    </div>
  );
};