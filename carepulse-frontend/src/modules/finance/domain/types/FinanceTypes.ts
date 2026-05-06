export type TransactionType = 'INCOME' | 'EXPENSE';
export type TransactionCategory = 'CARE_FEE' | 'MEDICAL_SUPPLY' | 'PAYROLL' | 'UTILITY' | 'MAINTENANCE' | 'FOOD' | 'OTHER';
export type PaymentMethod = 'CASH' | 'CHECK' | 'BANK_TRANSFER' | 'CREDIT_CARD' | 'INSURANCE';
export type TransactionStatus = 'PENDING' | 'COMPLETED' | 'OVERDUE' | 'CANCELLED';

export interface FinancialTransaction {
  id: string;
  transactionCode: string;
  transactionType: TransactionType;
  category: TransactionCategory;
  amount: number;
  currency: string;
  description?: string;
  residentId?: string;
  vendorName?: string;
  invoiceNumber?: string;
  paymentMethod: PaymentMethod;
  status: TransactionStatus;
  transactionDate: string;
  dueDate?: string;
  settledDate?: string;
  createdAt: string;
  updatedAt: string;
}

export type CreateTransactionData = Pick<FinancialTransaction, 
  'transactionType' | 'category' | 'amount' | 'description' | 'residentId' | 'vendorName' | 'invoiceNumber' | 'paymentMethod' | 'transactionDate'
>;

export type UpdateTransactionData = Partial<Pick<FinancialTransaction, 
  'transactionType' | 'category' | 'amount' | 'description' | 'residentId' | 'vendorName' | 'invoiceNumber' | 'paymentMethod' | 'transactionDate'
>>;
