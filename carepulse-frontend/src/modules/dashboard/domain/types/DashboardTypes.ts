export interface FinancialTrend {
  month: string;
  revenue: number;
  expenses: number;
}

export interface DashboardMetrics {
  totalResidents: number;
  activeStaff: number;
  availableRooms: number;
  monthlyRevenue: number;
  financialTrends: FinancialTrend[];
}
