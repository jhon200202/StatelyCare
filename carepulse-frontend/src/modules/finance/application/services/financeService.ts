import apiClient from '../../../../shared/infrastructure/http/axiosConfig';
import type { FinancialTransaction, CreateTransactionData, UpdateTransactionData } from '../../domain/types/FinanceTypes';

export const financeService = {
  getAllTransactions: async (): Promise<FinancialTransaction[]> => {
    const { data } = await apiClient.get<FinancialTransaction[]>('/finance/transactions');
    return data;
  },
  getTransactionsByResident: async (residentId: string): Promise<FinancialTransaction[]> => {
    const { data } = await apiClient.get<FinancialTransaction[]>(`/finance/transactions/resident/${residentId}`);
    return data;
  },
  createTransaction: async (transactionData: CreateTransactionData): Promise<FinancialTransaction> => {
    const { data } = await apiClient.post<FinancialTransaction>('/finance/transactions', transactionData);
    return data;
  },
  updateTransaction: async (id: string, transactionData: UpdateTransactionData): Promise<FinancialTransaction> => {
    const { data } = await apiClient.put<FinancialTransaction>(`/finance/transactions/${id}`, transactionData);
    return data;
  },
  getMonthlyMetrics: async () => {
    const { data } = await apiClient.get('/finance/metrics/monthly');
    return data;
  },
  deleteTransaction: async (id: string): Promise<void> => {
    await apiClient.delete(`/finance/transactions/${id}`);
  },
};
