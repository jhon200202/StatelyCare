import React from 'react';
import { useQuery } from '@tanstack/react-query';
import { dashboardService } from '../../application/services/dashboardService';
import { StatCard } from '../../../../components/ui/StatCard';
import { FinancialChart } from '../../../../components/charts/FinancialChart';
import { Users, Bed, Stethoscope, DollarSign } from 'lucide-react';

export const DashboardPage: React.FC = () => {
  const { data, isLoading, error } = useQuery({
    queryKey: ['dashboardMetrics'],
    queryFn: dashboardService.getMetrics,
  });

  if (isLoading) {
    return <div className="flex items-center justify-center h-full text-on-surfaceVariant">Cargando métricas...</div>;
  }

  if (error || !data) {
    return <div className="text-error">Error al cargar el dashboard.</div>;
  }

  return (
    <div className="space-y-8">
      <div>
        <h1 className="text-h2 text-on-surface mb-2">Resumen General</h1>
        <p className="text-on-surfaceVariant text-body-md">Métricas principales de Stately Care al día de hoy.</p>
      </div>

      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
        <StatCard 
          title="Residentes Totales" 
          value={data.totalResidents} 
          icon={<Users size={24} />} 
          trend={{ value: 2.4, isPositive: true }} 
        />
        <StatCard 
          title="Personal Activo" 
          value={data.activeStaff} 
          icon={<Stethoscope size={24} />} 
        />
        <StatCard 
          title="Camas Disponibles" 
          value={data.availableRooms} 
          icon={<Bed size={24} />} 
          trend={{ value: 5.0, isPositive: false }} 
        />
        <StatCard 
          title="Ingresos del Mes" 
          value={`$${data.monthlyRevenue.toLocaleString()}`} 
          icon={<DollarSign size={24} />} 
          trend={{ value: 8.1, isPositive: true }} 
        />
      </div>

      <div className="bg-surface-containerLowest p-6 rounded-lg border border-outline-variant">
        <h3 className="text-h3 text-on-surface mb-6">Tendencia Financiera</h3>
        <FinancialChart data={data.financialTrends} />
      </div>
    </div>
  );
};
