import React from 'react';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { treatmentService } from '../../application/services/treatmentService';
import { useAuth } from '../../../../shared/application/context/AuthContext';
import { Pill, CheckCircle, Clock, AlertCircle } from 'lucide-react';
import type { DailyTreatmentReminder } from '../../domain/types/TreatmentTypes';

export const TreatmentReminders: React.FC = () => {
  const { user } = useAuth();
  const queryClient = useQueryClient();

  const { data: reminders = [], isLoading, refetch } = useQuery({
    queryKey: ['daily-treatments'],
    queryFn: treatmentService.getDailyTreatments,
  });

  const administerMutation = useMutation({
    mutationFn: treatmentService.administerMedication,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['daily-treatments'] });
    },
    onError: () => alert('Error al registrar la administración'),
  });

  const handleAdminister = (treatmentId: string) => {
    if (!user) return;
    administerMutation.mutate({
      treatmentId,
      administeredBy: user.id,
      notes: 'Administración registrada desde recordatorios',
      dosageGiven: 'Dosis prescrita',
      route: 'ORAL',
    });
  };

  const pendingTreatments = reminders.filter(r => !r.administeredToday);
  const completedTreatments = reminders.filter(r => r.administeredToday);

  const getTypeIcon = (type: string) => {
    const icons: Record<string, string> = {
      'MEDICATION': '💊',
      'THERAPY': '🧠',
      'PROCEDURE': '🩺',
      'CHECKUP': '📅'
    };
    return icons[type] || '💊';
  };

  const getTypeLabel = (type: string) => {
    const labels: Record<string, string> = {
      'MEDICATION': 'Medicamento',
      'THERAPY': 'Terapia',
      'PROCEDURE': 'Procedimiento',
      'CHECKUP': 'Chequeo'
    };
    return labels[type] || type;
  };

  const renderTreatmentCard = (reminder: DailyTreatmentReminder) => (
    <div 
      key={reminder.treatmentId}
      className={`flex items-center justify-between p-4 rounded-lg border transition-all ${
        reminder.administeredToday 
          ? 'bg-surface-containerLow border-outline-variant opacity-60' 
          : 'bg-surface-containerHigh border-primary-container'
      }`}
    >
      <div className="flex items-center gap-4">
        <div className="text-2xl">{getTypeIcon(reminder.treatmentType)}</div>
        <div>
          <p className="font-bold text-onSurface">{reminder.treatmentName}</p>
          <p className="text-sm text-onSurfaceVariant">
            {reminder.residentName} ({reminder.residentCode})
          </p>
          <div className="flex items-center gap-2 mt-1">
            <span className="text-xs px-2 py-0.5 rounded bg-surface-container text-onSurfaceVariant">
              {getTypeLabel(reminder.treatmentType)}
            </span>
            {reminder.scheduledTime && (
              <span className="flex items-center gap-1 text-xs text-onSurfaceVariant">
                <Clock size={12} />
                {reminder.scheduledTime.substring(0, 5)}
              </span>
            )}
          </div>
        </div>
      </div>
      
      <button
        onClick={() => handleAdminister(reminder.treatmentId)}
        disabled={reminder.administeredToday || administerMutation.isPending}
        className={`flex items-center gap-2 px-4 py-2 rounded-full font-bold transition-all ${
          reminder.administeredToday
            ? 'bg-surface-container text-onSurfaceVariant cursor-default'
            : 'bg-primary text-onPrimary hover:shadow-lg hover:bg-primary-container'
        }`}
      >
        {reminder.administeredToday ? (
          <>
            <CheckCircle size={18} />
            COMPLETADO
          </>
        ) : (
          <>
            <Pill size={18} />
            ADMINISTRAR
          </>
        )}
      </button>
    </div>
  );

  return (
    <div className="space-y-6">
      <div className="flex justify-between items-center">
        <div>
          <h1 className="text-h2 text-onSurface mb-2">Recordatorio de Tratamientos</h1>
          <p className="text-onSurfaceVariant text-body-md">
            Tratamientos diarios de {new Date().toLocaleDateString('es-ES', { 
              weekday: 'long', 
              year: 'numeric', 
              month: 'long', 
              day: 'numeric' 
            })}
          </p>
        </div>
        <button 
          onClick={() => refetch()}
          className="px-4 py-2 text-sm font-bold text-primary hover:bg-surface-container rounded-md"
        >
          Actualizar
        </button>
      </div>

      {isLoading ? (
        <div className="text-center py-8 text-onSurfaceVariant">Cargando...</div>
      ) : reminders.length === 0 ? (
        <div className="flex flex-col items-center justify-center py-12 text-onSurfaceVariant">
          <AlertCircle size={48} className="mb-4 opacity-50" />
          <p className="text-lg">No hay tratamientos programados para hoy</p>
        </div>
      ) : (
        <>
          {pendingTreatments.length > 0 && (
            <div className="space-y-4">
              <h2 className="text-h3 text-onSurface flex items-center gap-2">
                <span className="w-3 h-3 rounded-full bg-primary animate-pulse"></span>
                Pendientes ({pendingTreatments.length})
              </h2>
              <div className="space-y-3">
                {pendingTreatments.map(renderTreatmentCard)}
              </div>
            </div>
          )}

          {completedTreatments.length > 0 && (
            <div className="space-y-4 mt-8">
              <h2 className="text-h3 text-onSurfaceVariant flex items-center gap-2">
                <CheckCircle size={20} className="text-success" />
                Completados ({completedTreatments.length})
              </h2>
              <div className="space-y-3">
                {completedTreatments.map(renderTreatmentCard)}
              </div>
            </div>
          )}
        </>
      )}
    </div>
  );
};