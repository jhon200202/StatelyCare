import React, { useState } from 'react';
import { useQuery, useMutation } from '@tanstack/react-query';
import { treatmentService } from '../../application/services/treatmentService';
import { residentService } from '../../../residents/application/services/residentService';
import { employeeService } from '../../../employee/application/services/employeeService';
import { useAuth } from '../../../../shared/application/context/AuthContext';
import { DataTable } from '../../../../components/ui/DataTable';
import { Modal } from '../../../../components/ui/Modal';
import { TreatmentForm } from '../components/TreatmentForm';
import type { Treatment } from '../../domain/types/TreatmentTypes';
import { Pill, Plus, CheckCircle, Trash2, Edit2 } from 'lucide-react';

export const TreatmentList: React.FC = () => {
  const { user } = useAuth();
  const [selectedResidentId, setSelectedResidentId] = useState<string>('');
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [editingTreatment, setEditingTreatment] = useState<Treatment | undefined>(undefined);

  const { data: residents = [] } = useQuery({
    queryKey: ['residents'],
    queryFn: residentService.getAll,
  });

  const { data: treatments = [], isLoading, refetch } = useQuery({
    queryKey: ['treatments', selectedResidentId],
    queryFn: () => treatmentService.getActiveByResident(selectedResidentId),
    enabled: !!selectedResidentId,
  });

  const { data: employees = [] } = useQuery({
    queryKey: ['employees'],
    queryFn: employeeService.getAll,
  });

  const createMutation = useMutation({
    mutationFn: treatmentService.create,
    onSuccess: () => {
      setIsModalOpen(false);
      refetch();
    },
    onError: () => alert('Error al crear el tratamiento'),
  });

  const updateMutation = useMutation({
    mutationFn: ({ id, data }: { id: string; data: any }) => treatmentService.update(id, data),
    onSuccess: () => {
      setIsModalOpen(false);
      setEditingTreatment(undefined);
      refetch();
    },
    onError: () => alert('Error al actualizar el tratamiento'),
  });

  const deleteMutation = useMutation({
    mutationFn: treatmentService.delete,
    onSuccess: () => {
      refetch();
    },
    onError: () => alert('Error al eliminar el tratamiento'),
  });

  const administerMutation = useMutation({
    mutationFn: treatmentService.administerMedication,
    onSuccess: () => {
      alert('Administración registrada exitosamente.');
      refetch();
    },
    onError: () => alert('Error al registrar la administración'),
  });

  const handleAdminister = (treatmentId: string) => {
    if (!user) return;
    administerMutation.mutate({
      treatmentId,
      administeredBy: user.id,
      notes: 'Administración de rutina registrada desde el listado',
      dosageGiven: 'Dosis prescrita', // Default value
      route: 'ORAL', // Default value
    });
  };

  const getDoctorName = (id: string) => {
    const doc = employees.find(e => e.id === id);
    return doc ? `${doc.firstName} ${doc.lastName}` : 'Desconocido';
  };

  const handleEdit = (treatment: Treatment) => {
    setEditingTreatment(treatment);
    setIsModalOpen(true);
  };

  const handleCloseModal = () => {
    setIsModalOpen(false);
    setEditingTreatment(undefined);
  };

  const handleFormSubmit = (data: any) => {
    if (editingTreatment) {
      updateMutation.mutate({ id: editingTreatment.id, data });
    } else {
      createMutation.mutate(data);
    }
  };

  const columns = [
    { key: 'treatmentName' as keyof Treatment, header: 'Tratamiento' },
    { 
      key: 'treatmentType' as keyof Treatment, 
      header: 'Tipo',
      render: (val: string) => {
        const labels: Record<string, string> = {
          'MEDICATION': '💊 Medicamento',
          'THERAPY': '🧠 Terapia',
          'PROCEDURE': '🩺 Procedimiento',
          'CHECKUP': '📅 Chequeo'
        };
        return labels[val] || val;
      }
    },
    { key: 'prescribedBy' as keyof Treatment, header: 'Prescrito por', render: (val: string) => getDoctorName(val) },
    { 
      key: 'frequency' as keyof Treatment, 
      header: 'Frecuencia',
      render: (val: string) => {
        const labels: Record<string, string> = {
          'ONCE': 'Una vez',
          'DAILY': 'Diario',
          'WEEKLY': 'Semanal',
          'MONTHLY': 'Mensual',
          'AS_NEEDED': 'Si es necesario'
        };
        return labels[val] || val;
      }
    },
    { 
      key: 'scheduledTime' as keyof Treatment, 
      header: 'Hora',
      render: (val: string) => val ? val.substring(0, 5) : '-'
    },
    { 
      key: 'status' as keyof Treatment, 
      header: 'Estado',
      render: (val: string) => (
        <span className={`px-2 py-1 rounded-full text-xs font-bold ${
          val === 'ACTIVE' ? 'bg-primary-container text-on-primaryContainer' : 'bg-surface-variant text-on-surfaceVariant'
        }`}>
          {val}
        </span>
      )
    },
    { 
      key: 'id' as keyof Treatment, 
      header: 'Acción',
      render: (val: string, row: Treatment) => (
        <div className="flex items-center gap-2">
          <button
            onClick={() => handleAdminister(val)}
            disabled={administerMutation.isPending || row.status !== 'ACTIVE'}
            className="flex items-center gap-2 text-xs font-bold px-4 py-1.5 bg-secondary text-on-secondary rounded-full hover:shadow-md hover:bg-secondary-container transition-all"
          >
            <CheckCircle size={14} />
            ADMINISTRAR
          </button>
          <button
            onClick={() => handleEdit(row)}
            className="p-1.5 text-primary hover:bg-primary-container rounded"
            title="Editar"
          >
            <Edit2 size={16} />
          </button>
          <button
            onClick={() => {
              if (confirm('¿Estás seguro de eliminar este tratamiento?')) {
                deleteMutation.mutate(val);
              }
            }}
            className="p-1.5 text-error hover:bg-error-container rounded"
            title="Eliminar"
          >
            <Trash2 size={16} />
          </button>
        </div>
      )
    },
  ];

  return (
    <div className="space-y-6">
      <div className="flex justify-between items-center">
        <div>
          <h1 className="text-h2 text-on-surface mb-2">Gestión de Tratamientos</h1>
          <p className="text-on-surfaceVariant text-body-md">Seguimiento clínico y administración de medicamentos.</p>
        </div>
      </div>

      <div className="bg-surface-containerLowest p-6 rounded-lg border border-outline-variant shadow-sm flex gap-4 items-end">
        <div className="flex-1">
          <label className="block text-sm font-medium text-on-surface mb-2">Seleccionar Residente</label>
          <select 
            value={selectedResidentId}
            onChange={(e) => setSelectedResidentId(e.target.value)}
            className="w-full px-4 py-2.5 rounded-md border border-outline-variant focus:ring-2 focus:ring-primary outline-none transition-all"
          >
            <option value="">Seleccione residente...</option>
            {residents.map(r => (
              <option key={r.id} value={r.id}>{r.firstName} {r.lastName} (Código: {r.residentCode})</option>
            ))}
          </select>
        </div>
        <button 
          onClick={() => {
            setEditingTreatment(undefined);
            setIsModalOpen(true);
          }}
          disabled={!selectedResidentId}
          className="flex items-center gap-2 h-11 px-6 bg-primary text-on-primary rounded-md font-bold hover:shadow-lg hover:bg-primary-container transition-all disabled:opacity-50"
        >
          <Plus size={20} />
          NUEVO TRATAMIENTO
        </button>
      </div>

      {selectedResidentId && (
        <div className="bg-surface-containerLowest rounded-lg shadow-sm border border-outline-variant overflow-hidden">
          <div className="p-4 border-b border-outline-variant flex items-center gap-2 bg-surface-containerLow">
            <Pill size={20} className="text-primary" />
            <h2 className="text-h3 text-on-surface">Tratamientos Médicos Activos</h2>
          </div>
          {isLoading ? (
            <div className="p-8 text-on-surfaceVariant">Cargando tratamientos...</div>
          ) : treatments.length === 0 ? (
            <div className="p-8 text-on-surfaceVariant italic text-center">No hay tratamientos activos para este residente.</div>
          ) : (
            <DataTable data={treatments} columns={columns} />
          )}
        </div>
      )}

      <Modal isOpen={isModalOpen} onClose={handleCloseModal} title={editingTreatment ? 'Editar Tratamiento' : 'Prescribir Nuevo Tratamiento'}>
        <TreatmentForm 
          residentId={selectedResidentId}
          initialData={editingTreatment}
          onSubmit={handleFormSubmit} 
          onCancel={handleCloseModal} 
          isLoading={createMutation.isPending || updateMutation.isPending} 
        />
      </Modal>
    </div>
  );
};
