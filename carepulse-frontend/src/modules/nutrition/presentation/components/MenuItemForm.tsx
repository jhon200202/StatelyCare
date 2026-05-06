import React, { useEffect } from 'react';
import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import { z } from 'zod';
import type { MenuItemRequest, MenuItem } from '../../domain/types/NutritionTypes';

const menuItemSchema = z.object({
  name: z.string().min(1, 'El nombre es obligatorio'),
  description: z.string().optional(),
  mealType: z.enum(['BREAKFAST', 'LUNCH', 'SNACK', 'DINNER']),
  textureModification: z.enum(['REGULAR', 'SOFT', 'PUREED', 'LIQUID', 'THICKENED_LIQUID']),
});

interface MenuItemFormProps {
  onSubmit: (data: MenuItemRequest) => void;
  onCancel: () => void;
  isLoading: boolean;
  initialData?: MenuItem;
}

export const MenuItemForm: React.FC<MenuItemFormProps> = ({ 
  onSubmit, 
  onCancel, 
  isLoading,
  initialData
}) => {
  const { register, handleSubmit, formState: { errors }, reset } = useForm<MenuItemRequest>({
    resolver: zodResolver(menuItemSchema),
    defaultValues: {
      mealType: 'LUNCH',
      textureModification: 'REGULAR'
    }
  });

  useEffect(() => {
    if (initialData) {
      reset({
        name: initialData.name,
        description: initialData.description,
        mealType: initialData.mealType,
        textureModification: initialData.textureModification,
      });
    }
  }, [initialData, reset]);

  return (
    <form onSubmit={handleSubmit(onSubmit)} className="space-y-4">
      <div className="space-y-1">
        <label className="block text-sm font-medium text-on-surface">Nombre del Plato</label>
        <input 
          type="text" 
          {...register('name')} 
          placeholder="Ej: Puré de papas con pollo" 
          className="w-full px-4 py-2 rounded-md border border-outline-variant focus:ring-2 focus:ring-primary outline-none transition-all" 
        />
        {errors.name && <span className="text-xs text-error">{errors.name.message}</span>}
      </div>

      <div className="grid grid-cols-2 gap-4">
        <div className="space-y-1">
          <label className="block text-sm font-medium text-on-surface">Tipo de Comida</label>
          <select {...register('mealType')} className="w-full px-4 py-2 rounded-md border border-outline-variant focus:ring-2 focus:ring-primary outline-none">
            <option value="BREAKFAST">Desayuno</option>
            <option value="LUNCH">Almuerzo</option>
            <option value="SNACK">Merienda</option>
            <option value="DINNER">Cena</option>
          </select>
        </div>
        <div className="space-y-1">
          <label className="block text-sm font-medium text-on-surface">Textura</label>
          <select {...register('textureModification')} className="w-full px-4 py-2 rounded-md border border-outline-variant focus:ring-2 focus:ring-primary outline-none">
            <option value="REGULAR">Regular</option>
            <option value="SOFT">Blanda</option>
            <option value="PUREED">Puré</option>
            <option value="LIQUID">Líquida</option>
            <option value="THICKENED_LIQUID">Líquida Espesada</option>
          </select>
        </div>
      </div>

      <div className="space-y-1">
        <label className="block text-sm font-medium text-on-surface">Descripción / Ingredientes</label>
        <textarea 
          {...register('description')} 
          rows={3} 
          placeholder="Detalles sobre el plato o restricciones..." 
          className="w-full px-4 py-2 rounded-md border border-outline-variant focus:ring-2 focus:ring-primary outline-none transition-all" 
        />
      </div>

      <div className="flex justify-end gap-3 pt-6">
        <button 
          type="button" 
          onClick={onCancel} 
          className="px-4 py-2 text-primary font-medium hover:bg-surface-container rounded-md transition-colors"
        >
          Cancelar
        </button>
        <button 
          type="submit" 
          disabled={isLoading} 
          className="px-6 py-2 bg-primary text-on-primary font-bold rounded-md hover:shadow-lg hover:bg-primary-container transition-all disabled:opacity-50"
        >
          {isLoading ? (initialData ? 'Actualizando...' : 'Guardando...') : (initialData ? 'Actualizar' : 'Crear Plato')}
        </button>
      </div>
    </form>
  );
};
