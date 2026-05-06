import React from 'react';
import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import { z } from 'zod';
import { useQuery } from '@tanstack/react-query';
import { employeeService } from '../../../employee/application/services/employeeService';
import { Search, Globe } from 'lucide-react';
import { EmployeePickerModal } from '../../../employee/presentation/components/EmployeePickerModal';
import type { CreateTreatmentRequest } from '../../domain/types/TreatmentTypes';
import type { Employee } from '../../../employee/domain/types/EmployeeTypes';

const treatmentSchema = z.object({
  residentId: z.string().uuid(),
  prescribedBy: z.string().uuid('Debe seleccionar un médico válido'),
  treatmentName: z.string().min(1, 'El nombre del tratamiento es obligatorio'),
  treatmentType: z.enum(['MEDICATION', 'THERAPY', 'PROCEDURE', 'CHECKUP']),
  frequency: z.enum(['ONCE', 'DAILY', 'WEEKLY', 'MONTHLY', 'AS_NEEDED']),
  scheduledTime: z.string().min(1, 'La hora programada es obligatoria'),
  startDate: z.string().min(1, 'La fecha de inicio es obligatoria'),
  endDate: z.string().optional(),
});

interface TreatmentFormProps {
  residentId: string;
  onSubmit: (data: CreateTreatmentRequest) => void;
  onCancel: () => void;
  isLoading: boolean;
  initialData?: Treatment;
}

export const TreatmentForm: React.FC<TreatmentFormProps> = ({ residentId, onSubmit, onCancel, isLoading, initialData }) => {
  const [searchTerm, setSearchTerm] = React.useState('');
  const [showSuggestions, setShowSuggestions] = React.useState(false);
  const [isPickerOpen, setIsPickerOpen] = React.useState(false);

  const { data: employees = [] } = useQuery({
    queryKey: ['employees'],
    queryFn: employeeService.getAll,
  });

  const { register, handleSubmit, setValue, watch, formState: { errors } } = useForm<CreateTreatmentRequest>({
    resolver: zodResolver(treatmentSchema),
    defaultValues: initialData ? {
      residentId: initialData.residentId,
      prescribedBy: initialData.prescribedBy,
      treatmentName: initialData.treatmentName,
      treatmentType: initialData.treatmentType as any,
      frequency: initialData.frequency as any,
      scheduledTime: initialData.scheduledTime || '08:00',
      startDate: initialData.startDate,
      endDate: initialData.endDate,
    } : { 
      residentId,
      treatmentType: 'MEDICATION',
      frequency: 'DAILY',
      scheduledTime: '08:00',
      startDate: new Date().toISOString().split('T')[0],
    }
  });

  React.useEffect(() => {
    if (initialData && employees.length > 0) {
      const doc = employees.find(e => e.id === initialData.prescribedBy);
      if (doc) {
        setSearchTerm(`${doc.firstName} ${doc.lastName}`);
      }
    }
  }, [initialData, employees]);

  const prescribedBy = watch('prescribedBy');

  // Filter for medical staff
  const medicalStaff = employees.filter(e => 
    e.isActive && (e.department === 'MEDICAL' || e.department === 'NURSING')
  );

  const filteredStaff = medicalStaff.filter(doc => 
    `${doc.firstName} ${doc.lastName}`.toLowerCase().includes(searchTerm.toLowerCase())
  );

  const handleSelectDoctor = (doc: Employee) => {
    setValue('prescribedBy', doc.id);
    setSearchTerm(`${doc.firstName} ${doc.lastName}`);
    setShowSuggestions(false);
  };

  return (
    <form onSubmit={handleSubmit(onSubmit)} className="space-y-4">
      <input type="hidden" {...register('residentId')} />
      <input type="hidden" {...register('prescribedBy')} />
      
      <div className="space-y-1">
        <label className="block text-sm font-medium text-on-surface">Nombre del Tratamiento / Medicamento</label>
        <input type="text" {...register('treatmentName')} placeholder="Ej: Enalapril 10mg" className="w-full px-4 py-2.5 rounded-md border border-outline-variant focus:ring-2 focus:ring-primary outline-none transition-all" />
        {errors.treatmentName && <span className="text-xs text-error">{errors.treatmentName.message}</span>}
      </div>
      
      <div className="grid grid-cols-2 gap-4">
        <div className="space-y-1">
          <label className="block text-sm font-medium text-on-surface">Tipo</label>
          <select {...register('treatmentType')} className="w-full px-4 py-2 rounded-md border border-outline-variant focus:ring-2 focus:ring-primary outline-none">
            <option value="MEDICATION">Medicamento</option>
            <option value="THERAPY">Terapia</option>
            <option value="PROCEDURE">Procedimiento</option>
            <option value="CHECKUP">Chequeo/Control</option>
          </select>
        </div>
        <div className="space-y-1">
          <label className="block text-sm font-medium text-on-surface">Frecuencia</label>
          <select {...register('frequency')} className="w-full px-4 py-2 rounded-md border border-outline-variant focus:ring-2 focus:ring-primary outline-none">
            <option value="ONCE">Una sola vez</option>
            <option value="DAILY">Diario</option>
            <option value="WEEKLY">Semanal</option>
            <option value="MONTHLY">Mensual</option>
            <option value="AS_NEEDED">Si es necesario (PRN)</option>
          </select>
        </div>
      </div>

      <div className="grid grid-cols-2 gap-4">
        <div className="space-y-1">
          <label className="block text-sm font-medium text-on-surface">Hora Programada</label>
          <input type="time" {...register('scheduledTime')} className="w-full px-4 py-2 rounded-md border border-outline-variant focus:ring-2 focus:ring-primary outline-none" />
        </div>
        <div className="space-y-1 relative">
          <label className="block text-sm font-medium text-on-surface">Médico Prescriptor</label>
          <div className="flex gap-2">
            <div className="relative flex-1">
              <input 
                type="text"
                value={searchTerm}
                onChange={(e) => {
                  setSearchTerm(e.target.value);
                  setShowSuggestions(true);
                  if (prescribedBy) setValue('prescribedBy', ''); // Clear ID if typing
                }}
                onFocus={() => setShowSuggestions(true)}
                onBlur={() => setTimeout(() => setShowSuggestions(false), 200)}
                placeholder="Buscar médico..."
                className={`w-full px-4 py-2 rounded-md border border-outline-variant focus:ring-2 focus:ring-primary outline-none transition-all ${errors.prescribedBy ? 'border-error' : ''}`}
              />
              {showSuggestions && (searchTerm || prescribedBy) && (
                <div className="absolute z-50 w-full mt-1 bg-surface-containerLowest border border-outline-variant rounded-md shadow-xl max-h-48 overflow-y-auto">
                  {filteredStaff.length > 0 ? (
                    filteredStaff.map(doc => (
                      <div 
                        key={doc.id}
                        onMouseDown={(e) => {
                          e.preventDefault(); 
                          handleSelectDoctor(doc);
                        }}
                        className="px-4 py-2 hover:bg-primary-container cursor-pointer text-sm border-b border-outline-variant last:border-0"
                      >
                        <div className="font-bold">{doc.firstName} {doc.lastName}</div>
                        <div className="text-xs text-on-surfaceVariant">{doc.roleTitle} — {doc.department}</div>
                      </div>
                    ))
                  ) : (
                    <div className="px-4 py-3 text-sm text-on-surfaceVariant italic">No se encontraron resultados.</div>
                  )}
                </div>
              )}
            </div>
            <button
              type="button"
              onClick={() => setIsPickerOpen(true)}
              className="px-3 bg-secondary text-on-secondary rounded-md hover:bg-secondary-container transition-all flex items-center gap-1 text-xs font-bold"
              title="Explorar todo el personal"
            >
              <Globe size={16} />
              EXPLORAR
            </button>
          </div>
          {errors.prescribedBy && <span className="text-xs text-error">{errors.prescribedBy.message}</span>}
        </div>
      </div>

      <EmployeePickerModal 
        isOpen={isPickerOpen} 
        onClose={() => setIsPickerOpen(false)} 
        onSelect={handleSelectDoctor}
      />

      <div className="grid grid-cols-2 gap-4">
        <div className="space-y-1">
          <label className="block text-sm font-medium text-on-surface">Fecha de Inicio</label>
          <input type="date" {...register('startDate')} className="w-full px-4 py-2 rounded-md border border-outline-variant focus:ring-2 focus:ring-primary outline-none" />
        </div>
        <div className="space-y-1">
          <label className="block text-sm font-medium text-on-surface">Fecha de Fin (Opcional)</label>
          <input type="date" {...register('endDate')} className="w-full px-4 py-2 rounded-md border border-outline-variant focus:ring-2 focus:ring-primary outline-none" />
        </div>
      </div>

      <div className="flex justify-end gap-3 pt-6">
        <button type="button" onClick={onCancel} className="px-4 py-2 text-primary font-medium hover:bg-surface-container rounded-md transition-colors">
          Cancelar
        </button>
        <button type="submit" disabled={isLoading} className="px-6 py-2 bg-primary text-on-primary font-bold rounded-md hover:shadow-lg hover:bg-primary-container transition-all disabled:opacity-50">
          {isLoading ? (initialData ? 'Guardando...' : 'Asignando...') : (initialData ? 'Guardar Cambios' : 'Asignar Tratamiento')}
        </button>
      </div>
    </form>
  );
};
