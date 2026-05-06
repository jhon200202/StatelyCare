import React, { useState } from 'react';
import type { MenuItem, MealType, DailyMenu, Resident } from '../../domain/types/NutritionTypes';
import { Modal } from '../../../../components/ui/Modal';
import { User, Trash2, Edit2 } from 'lucide-react';

interface DailyMealPlannerProps {
  mealType: MealType;
  mealLabel: string;
  menuItems: MenuItem[];
  plannedItems: DailyMenu[];
  residents: Resident[];
  onAddItem: (menuItemId: string, servings: number, residentId?: string) => void;
  onUpdateItem: (id: string, menuItemId: string, servings: number, residentId?: string) => void;
  onDeleteItem: (id: string) => void;
  isLoading?: boolean;
}

export const DailyMealPlanner: React.FC<DailyMealPlannerProps> = ({
  mealType,
  mealLabel,
  menuItems,
  plannedItems,
  residents,
  onAddItem,
  onUpdateItem,
  onDeleteItem,
  isLoading = false
}) => {
  const [selectedItemId, setSelectedItemId] = useState<string>('');
  const [servings, setServings] = useState<number>(1);
  const [selectedResidentId, setSelectedResidentId] = useState<string>('');
  const [isResidentModalOpen, setIsResidentModalOpen] = useState(false);
  const [editingItem, setEditingItem] = useState<DailyMenu | null>(null);

  const handleAddItem = () => {
    if (selectedItemId) {
      onAddItem(selectedItemId, servings, selectedResidentId || undefined);
      setSelectedItemId('');
      setServings(1);
      setSelectedResidentId('');
    }
  };

  const handleEditItem = (item: DailyMenu) => {
    setEditingItem(item);
    setSelectedItemId(item.menuItemId);
    setServings(item.servingsPlanned);
    setSelectedResidentId(item.residentId || '');
  };

  const handleUpdateItem = () => {
    if (editingItem && selectedItemId) {
      onUpdateItem(editingItem.id, selectedItemId, servings, selectedResidentId || undefined);
      setEditingItem(null);
      setSelectedItemId('');
      setServings(1);
      setSelectedResidentId('');
    }
  };

  const handleCancelEdit = () => {
    setEditingItem(null);
    setSelectedItemId('');
    setServings(1);
    setSelectedResidentId('');
  };

  const itemsForMealType = menuItems.filter(item => 
    item.mealType === mealType && item.isActive
  );

  const plannedForThisMeal = plannedItems.filter(item => item.mealType === mealType);

  const getResidentName = (residentId?: string) => {
    if (!residentId) return 'Sin residente';
    const resident = residents.find(r => r.id === residentId);
    return resident ? `${resident.firstName} ${resident.lastName}` : 'Sin residente';
  };

  return (
    <div className="p-5 rounded-lg border border-outline-variant bg-surface-containerLow hover:shadow-md transition-all">
      <h4 className="font-bold text-primary mb-4">{mealLabel}</h4>
      
      {/* Items planificados */}
      <div className="mb-4 space-y-2 max-h-[150px] overflow-y-auto">
        {plannedForThisMeal.length > 0 ? (
          plannedForThisMeal.map(item => {
            const menuItem = menuItems.find(m => m.id === item.menuItemId);
            return (
              <div 
                key={item.id}
                className="flex items-center justify-between p-2 rounded bg-surface-container text-sm"
              >
                <div className="flex-1">
                  <span className="text-on-surface font-medium">{menuItem?.name} ({item.servingsPlanned} porciones)</span>
                  <div className="text-xs text-on-surfaceVariant flex items-center gap-1 mt-1">
                    <User size={12} />
                    {getResidentName(item.residentId)}
                  </div>
                </div>
                <div className="flex items-center gap-1">
                  <button
                    onClick={() => handleEditItem(item)}
                    className="p-1 text-on-surfaceVariant hover:text-primary"
                    title="Editar"
                  >
                    <Edit2 size={14} />
                  </button>
                  <button
                    onClick={() => onDeleteItem(item.id)}
                    className="p-1 text-on-surfaceVariant hover:text-error"
                    title="Eliminar"
                  >
                    <Trash2 size={14} />
                  </button>
                </div>
              </div>
            );
          })
        ) : (
          <p className="text-xs text-on-surfaceVariant italic">No hay platos asignados</p>
        )}
      </div>

      {/* Selector para agregar platos */}
      <div className="space-y-2 pt-3 border-t border-outline-variant">
        <div className="grid grid-cols-2 gap-2">
          <select
            value={selectedItemId}
            onChange={(e) => setSelectedItemId(e.target.value)}
            disabled={isLoading}
            className="col-span-1 px-2 py-1.5 text-sm rounded border border-outline-variant bg-surface focus:ring-1 focus:ring-primary outline-none"
          >
            <option value="">
              {editingItem ? 'Cambiar plato' : 'Seleccionar plato'}
            </option>
            {itemsForMealType.map(item => (
              <option key={item.id} value={item.id}>
                {item.name}
              </option>
            ))}
          </select>
          
          <input
            type="number"
            min="1"
            max="20"
            value={servings}
            onChange={(e) => setServings(Math.max(1, parseInt(e.target.value) || 1))}
            disabled={isLoading}
            className="col-span-1 px-2 py-1.5 text-sm rounded border border-outline-variant bg-surface focus:ring-1 focus:ring-primary outline-none"
            placeholder="Porciones"
          />
        </div>

        {/* Botón para seleccionar residente */}
        <button
          type="button"
          onClick={() => setIsResidentModalOpen(true)}
          disabled={isLoading}
          className="w-full px-3 py-1.5 text-sm border border-outline-variant rounded flex items-center justify-center gap-2 hover:bg-surface-container transition-colors"
        >
          <User size={14} />
          {selectedResidentId 
            ? getResidentName(selectedResidentId) 
            : 'Seleccionar residente (opcional)'}
        </button>
        
        <button
          onClick={editingItem ? handleUpdateItem : handleAddItem}
          disabled={!selectedItemId || isLoading}
          className="w-full px-3 py-1.5 text-sm bg-primary text-on-primary font-bold rounded hover:bg-primary-container transition-colors disabled:opacity-50 disabled:cursor-not-allowed"
        >
          {isLoading 
            ? (editingItem ? 'Actualizando...' : 'Agregando...') 
            : (editingItem ? 'Actualizar' : 'Agregar a menú')}
        </button>

        {editingItem && (
          <button
            onClick={handleCancelEdit}
            className="w-full px-3 py-1.5 text-sm text-on-surfaceVariant hover:text-error transition-colors"
          >
            Cancelar edición
          </button>
        )}
      </div>

      {/* Modal para seleccionar residente */}
      <Modal
        isOpen={isResidentModalOpen}
        onClose={() => setIsResidentModalOpen(false)}
        title="Seleccionar Residente"
      >
        <div className="space-y-2 max-h-[300px] overflow-y-auto">
          <button
            onClick={() => {
              setSelectedResidentId('');
              setIsResidentModalOpen(false);
            }}
            className={`w-full p-3 text-left rounded border transition-colors ${
              selectedResidentId === '' 
                ? 'bg-primary-container border-primary' 
                : 'border-outline-variant hover:bg-surface-container'
            }`}
          >
            <span className="font-medium">Sin residente específico</span>
          </button>
          {(residents || []).filter(r => r.status === 'ON_SITE').map(resident => (
            <button
              key={resident.id}
              onClick={() => {
                setSelectedResidentId(resident.id);
                setIsResidentModalOpen(false);
              }}
              className={`w-full p-3 text-left rounded border transition-colors ${
                selectedResidentId === resident.id 
                  ? 'bg-primary-container border-primary' 
                  : 'border-outline-variant hover:bg-surface-container'
              }`}
            >
              <span className="font-medium">{resident.firstName} {resident.lastName}</span>
              <span className="text-sm text-on-surfaceVariant ml-2">({resident.residentCode})</span>
            </button>
          ))}
        </div>
      </Modal>
    </div>
  );
};
