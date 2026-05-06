import React from 'react';
import type { MenuItem, MealType } from '../../domain/types/NutritionTypes';
import { Trash2, Edit2 } from 'lucide-react';

interface MenuItemCardProps {
  item: MenuItem;
  onEdit: (item: MenuItem) => void;
  onDelete: (id: string) => void;
  isDeleting?: boolean;
}

export const MenuItemCard: React.FC<MenuItemCardProps> = ({ 
  item, 
  onEdit, 
  onDelete, 
  isDeleting = false 
}) => {
  const mealTypeLabels: Record<MealType, string> = {
    BREAKFAST: 'Desayuno',
    LUNCH: 'Almuerzo',
    SNACK: 'Merienda',
    DINNER: 'Cena'
  };

  return (
    <div className={`p-4 rounded-lg border transition-all group ${
      item.isActive 
        ? 'border-outline-variant bg-surface-containerLowest hover:border-primary' 
        : 'border-outline bg-surface-variant opacity-60'
    }`}>
      <div className="flex justify-between items-start mb-2">
        <div className="flex-1">
          <p className={`font-bold text-sm transition-colors ${
            item.isActive
              ? 'text-on-surface group-hover:text-primary'
              : 'text-on-surfaceVariant line-through'
          }`}>
            {item.name}
          </p>
          <span className="text-[10px] px-2 py-0.5 rounded-full bg-secondary-container text-on-secondaryContainer font-bold inline-block mt-1">
            {mealTypeLabels[item.mealType]}
          </span>
        </div>
        {item.isActive && (
          <div className="flex gap-2 opacity-0 group-hover:opacity-100 transition-opacity">
            <button
              onClick={() => onEdit(item)}
              className="p-1.5 hover:bg-surface-variant rounded transition-colors text-primary hover:text-primary-container"
              title="Editar"
            >
              <Edit2 size={16} />
            </button>
            <button
              onClick={() => onDelete(item.id)}
              disabled={isDeleting}
              className="p-1.5 hover:bg-error-container rounded transition-colors text-error disabled:opacity-50"
              title="Eliminar"
            >
              <Trash2 size={16} />
            </button>
          </div>
        )}
      </div>
      <p className="text-xs text-on-surfaceVariant line-clamp-2 mb-2">{item.description}</p>
      <div className="flex items-center gap-2">
        <span className="text-[10px] font-medium bg-surface-variant text-on-surfaceVariant px-2 py-0.5 rounded">
          {item.textureModification}
        </span>
      </div>
    </div>
  );
};
