import React, { useEffect } from 'react';
import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import { z } from 'zod';
import { useQuery } from '@tanstack/react-query';
import { nutritionService } from '../../application/services/nutritionService';
import type { AssignDietData } from '../../domain/types/NutritionTypes';

const dietSchema = z.object({
  dietType: z.string().min(1, 'El tipo de dieta es obligatorio'),
  observations: z.string().optional(),
});

interface DietAssignmentFormProps {
  residentId: string;
  onSubmit: (data: AssignDietData) => void;
  onCancel: () => void;
  isLoading: boolean;
}

export const DietAssignmentForm: React.FC<DietAssignmentFormProps> = ({ residentId, onSubmit, onCancel, isLoading }) => {
  const { data: currentDiet } = useQuery({
    queryKey: ['diet', residentId],
    queryFn: () => nutritionService.getResidentDiet(residentId),
  });

  const { register, handleSubmit, reset, formState: { errors } } = useForm<AssignDietData>({
    resolver: zodResolver(dietSchema) as any,
    defaultValues: { residentId }
  });

  useEffect(() => {
    if (currentDiet) {
      reset({
        residentId,
        dietType: currentDiet.dietType,
        observations: currentDiet.observations
      });
    }
  }, [currentDiet, reset, residentId]);

  return (
    <form onSubmit={handleSubmit(onSubmit)} className="space-y-4">
      <input type="hidden" {...register('residentId')} />

      <div>
        <label className="block text-sm font-medium text-on-surface mb-1">Tipo de Dieta / Restricción</label>
        <select {...register('dietType')} className="w-full px-4 py-2 rounded-md border border-outline-variant focus:outline-none focus:border-primary">
          <option value="Regular">Regular</option>
          <option value="Baja en Sodio">Baja en Sodio</option>
          <option value="Diabética">Diabética</option>
          <option value="Blanda">Blanda</option>
          <option value="Hipocalórica">Hipocalórica</option>
          <option value="Sin Glúten">Sin Glúten</option>
          <option value="Vegetariana">Vegetariana</option>
        </select>
        {errors.dietType && <span className="text-xs text-error mt-1">{errors.dietType.message}</span>}
      </div>

      <div>
        <label className="block text-sm font-medium text-on-surface mb-1">Observaciones / Alergias Críticas</label>
        <textarea {...register('observations')} placeholder="Ej: Alérgico a las nueces, prefiere frutas de estación" className="w-full px-4 py-2 rounded-md border border-outline-variant focus:outline-none focus:border-primary" rows={4} />
      </div>

      <div className="flex justify-end gap-3 pt-6">
        <button type="button" onClick={onCancel} className="px-4 py-2 text-primary font-medium hover:bg-surface-container rounded-md transition-colors">
          Cancelar
        </button>
        <button type="submit" disabled={isLoading} className="px-6 py-2 bg-primary text-on-primary font-medium rounded-md hover:bg-primary-container transition-colors disabled:opacity-50">
          {isLoading ? 'Guardando...' : 'Asignar Dieta'}
        </button>
      </div>
    </form>
  );
};
