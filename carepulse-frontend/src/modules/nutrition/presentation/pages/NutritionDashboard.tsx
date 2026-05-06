import React, { useState } from 'react';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { nutritionService } from '../../application/services/nutritionService';
import { residentService } from '../../../residents/application/services/residentService';
import { Modal } from '../../../../components/ui/Modal';
import { MenuItemForm } from '../components/MenuItemForm';
import { MenuItemCard } from '../components/MenuItemCard';
import { DailyMealPlanner } from '../components/DailyMealPlanner';
import { Plus, Calendar, Salad, Printer } from 'lucide-react';
import type { MenuItem, DailyMenuRequest } from '../../domain/types/NutritionTypes';
import apiClient from '../../../../shared/infrastructure/http/axiosConfig';

export const NutritionDashboard: React.FC = () => {
  const [selectedDate, setSelectedDate] = useState(new Date().toISOString().split('T')[0]);
  const [isItemModalOpen, setIsItemModalOpen] = useState(false);
  const [editingItem, setEditingItem] = useState<MenuItem | undefined>(undefined);
  const queryClient = useQueryClient();

  const { data: menuItems = [] } = useQuery({
    queryKey: ['menuItems'],
    queryFn: nutritionService.getAllMenuItems,
  });

  const { data: dailyMenu = [] } = useQuery({
    queryKey: ['dailyMenu', selectedDate],
    queryFn: () => nutritionService.getMenuByDate(selectedDate),
  });

  const { data: residents = [] } = useQuery({
    queryKey: ['residents'],
    queryFn: residentService.getAll,
  });

  const createItemMutation = useMutation({
    mutationFn: nutritionService.createMenuItem,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['menuItems'] });
      setIsItemModalOpen(false);
      setEditingItem(undefined);
      alert('Plato creado correctamente.');
    },
  });

  const updateItemMutation = useMutation({
    mutationFn: ({ id, data }: { id: string; data: typeof editingItem }) => 
      nutritionService.updateMenuItem(id, data!),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['menuItems'] });
      setIsItemModalOpen(false);
      setEditingItem(undefined);
      alert('Plato actualizado correctamente.');
    },
  });

  const deleteItemMutation = useMutation({
    mutationFn: nutritionService.deleteMenuItem,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['menuItems'] });
      alert('Plato eliminado correctamente.');
    },
  });

  const planMealMutation = useMutation({
    mutationFn: nutritionService.planDailyMenu,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['dailyMenu', selectedDate] });
    },
  });

  const updateDailyMenuMutation = useMutation({
    mutationFn: ({ id, data }: { id: string; data: DailyMenuRequest }) =>
      nutritionService.updateDailyMenu(id, data),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['dailyMenu', selectedDate] });
    },
  });

  const deleteDailyMenuMutation = useMutation({
    mutationFn: nutritionService.deleteDailyMenu,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['dailyMenu', selectedDate] });
    },
  });

  const handleDeleteItem = (id: string) => {
    if (confirm('¿Estás seguro de que deseas eliminar este plato?')) {
      deleteItemMutation.mutate(id);
    }
  };

  const handleEditItem = (item: MenuItem) => {
    setEditingItem(item);
    setIsItemModalOpen(true);
  };

  const handleSubmitForm = (data: any) => {
    if (editingItem) {
      updateItemMutation.mutate({ id: editingItem.id, data });
    } else {
      createItemMutation.mutate(data);
    }
  };

  const handleCloseModal = () => {
    setIsItemModalOpen(false);
    setEditingItem(undefined);
  };

  const handlePrintPdf = async () => {
    try {
      const response = await apiClient.get(`/nutrition/menu/pdf/${selectedDate}`, {
        responseType: 'blob',
      });
      const url = window.URL.createObjectURL(new Blob([response.data]));
      const link = document.createElement('a');
      link.href = url;
      link.setAttribute('download', `menu_plan_${selectedDate}.pdf`);
      document.body.appendChild(link);
      link.click();
      link.remove();
      window.URL.revokeObjectURL(url);
    } catch (error) {
      console.error('Error generating PDF:', error);
      alert('Error al generar el PDF. Verifique la conexión con el servidor.');
    }
  };

  const mealTypeLabels: Record<string, string> = {
    BREAKFAST: 'Desayuno',
    LUNCH: 'Almuerzo',
    SNACK: 'Merienda',
    DINNER: 'Cena'
  };

  return (
    <div className="space-y-6">
      <div className="flex justify-between items-center">
        <div>
          <h1 className="text-h2 text-on-surface mb-2">Menú y Nutrición</h1>
          <p className="text-on-surfaceVariant text-body-md">Planificación de platos y gestión nutricional de los residentes.</p>
        </div>
        <button 
          onClick={() => {
            setEditingItem(undefined);
            setIsItemModalOpen(true);
          }}
          className="flex items-center gap-2 px-6 py-2.5 bg-primary text-on-primary rounded-md font-bold hover:shadow-lg hover:bg-primary-container transition-all"
        >
          <Plus size={18} />
          <span>Nuevo Plato</span>
        </button>
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
        <div className="lg:col-span-2 space-y-6">
          <div className="bg-surface-containerLowest p-6 rounded-lg border border-outline-variant shadow-sm">
            <div className="flex justify-between items-center mb-6">
              <div className="flex items-center gap-2">
                <Calendar size={20} className="text-primary" />
                <h2 className="text-h3 text-on-surface">Planificación del Día</h2>
              </div>
              <div className="flex items-center gap-2">
                <button 
                  onClick={handlePrintPdf}
                  className="flex items-center gap-2 px-4 py-2 bg-secondary text-on-secondary rounded-md font-bold hover:bg-secondary-container transition-all"
                  title="Imprimir PDF"
                >
                  <Printer size={16} />
                  <span>PDF</span>
                </button>
                <input 
                  type="date" 
                  value={selectedDate}
                  onChange={(e) => setSelectedDate(e.target.value)}
                  className="px-4 py-2 rounded-md border border-outline-variant focus:ring-2 focus:ring-primary outline-none"
                />
              </div>
            </div>

            <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
              {Object.entries(mealTypeLabels).map(([type, label]) => (
                <DailyMealPlanner
                  key={type}
                  mealType={type as any}
                  mealLabel={label}
                  menuItems={menuItems}
                  plannedItems={dailyMenu}
                  residents={residents}
                  onAddItem={(itemId, servings, residentId) => {
                    planMealMutation.mutate({
                      date: selectedDate,
                      mealType: type as any,
                      menuItemId: itemId,
                      servingsPlanned: servings,
                      residentId,
                    });
                  }}
                  onUpdateItem={(id, itemId, servings, residentId) => {
                    updateDailyMenuMutation.mutate({
                      id,
                      data: {
                        date: selectedDate,
                        mealType: type as any,
                        menuItemId: itemId,
                        servingsPlanned: servings,
                        residentId,
                      },
                    });
                  }}
                  onDeleteItem={(id) => {
                    if (confirm('¿Estás seguro de que deseas eliminar esta planificación?')) {
                      deleteDailyMenuMutation.mutate(id);
                    }
                  }}
                  isLoading={planMealMutation.isPending || updateDailyMenuMutation.isPending || deleteDailyMenuMutation.isPending}
                />
              ))}
            </div>
          </div>
        </div>

        <div className="space-y-6">
          <div className="bg-surface-containerLowest p-6 rounded-lg border border-outline-variant shadow-sm">
            <div className="flex items-center gap-2 mb-4">
              <Salad size={20} className="text-secondary" />
              <h2 className="text-h3 text-on-surface">Catálogo de Platos</h2>
            </div>
            <div className="space-y-3 max-h-[500px] overflow-y-auto pr-2 custom-scrollbar">
              {menuItems.filter(item => item.isActive).map(item => (
                <MenuItemCard
                  key={item.id}
                  item={item}
                  onEdit={handleEditItem}
                  onDelete={handleDeleteItem}
                  isDeleting={deleteItemMutation.isPending}
                />
              ))}
              {menuItems.filter(item => item.isActive).length === 0 && (
                <p className="text-center text-on-surfaceVariant text-sm py-8 italic">No hay platos registrados.</p>
              )}
              
              {menuItems.filter(item => !item.isActive).length > 0 && (
                <div className="mt-6 pt-4 border-t border-outline-variant">
                  <p className="text-xs font-bold text-on-surfaceVariant mb-2">Platos eliminados:</p>
                  {menuItems.filter(item => !item.isActive).map(item => (
                    <MenuItemCard
                      key={item.id}
                      item={item}
                      onEdit={() => {}}
                      onDelete={() => {}}
                    />
                  ))}
                </div>
              )}
            </div>
          </div>
        </div>
      </div>

      <Modal 
        isOpen={isItemModalOpen} 
        onClose={handleCloseModal} 
        title={editingItem ? 'Editar Plato' : 'Agregar Nuevo Plato al Menú'}
      >
        <MenuItemForm 
          onSubmit={handleSubmitForm}
          onCancel={handleCloseModal}
          isLoading={createItemMutation.isPending || updateItemMutation.isPending}
          initialData={editingItem}
        />
      </Modal>
    </div>
  );
};
