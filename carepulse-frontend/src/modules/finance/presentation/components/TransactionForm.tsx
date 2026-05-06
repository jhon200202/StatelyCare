import React from 'react';
import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import { z } from 'zod';
import type { CreateTransactionData, UpdateTransactionData, FinancialTransaction } from '../../domain/types/FinanceTypes';
import type { Resident } from '../../../residents/domain/types/ResidentTypes';

const transactionSchema = z.object({
  transactionType: z.enum(['INCOME', 'EXPENSE']),
  category: z.enum(['CARE_FEE', 'MEDICAL_SUPPLY', 'PAYROLL', 'UTILITY', 'MAINTENANCE', 'FOOD', 'OTHER']),
  amount: z.number().min(0.01, 'El monto debe ser mayor a 0'),
  description: z.string().min(1, 'La descripción es obligatoria'),
  transactionDate: z.string().min(1, 'La fecha es obligatoria'),
  residentId: z.string().optional().nullable(),
  vendorName: z.string().optional().nullable(),
  invoiceNumber: z.string().optional().nullable(),
  paymentMethod: z.enum(['CASH', 'CHECK', 'BANK_TRANSFER', 'CREDIT_CARD', 'INSURANCE']),
});

interface TransactionFormProps {
  residents: Resident[];
  onSubmit: (data: CreateTransactionData | UpdateTransactionData) => void;
  onCancel: () => void;
  isLoading: boolean;
  editData?: FinancialTransaction; // For editing existing transaction
}

export const TransactionForm: React.FC<TransactionFormProps> = ({ residents, onSubmit, onCancel, isLoading, editData }) => {
  const { register, handleSubmit, watch, formState: { errors }, reset } = useForm<CreateTransactionData | UpdateTransactionData>({
    resolver: zodResolver(transactionSchema) as any,
    defaultValues: {
      transactionType: 'INCOME',
      category: 'CARE_FEE',
      paymentMethod: 'CASH',
      transactionDate: new Date().toISOString().split('T')[0],
      ...(editData ? {
        transactionType: editData.transactionType,
        category: editData.category,
        amount: editData.amount,
        description: editData.description || '',
        residentId: editData.residentId || undefined,
        vendorName: editData.vendorName || undefined,
        invoiceNumber: editData.invoiceNumber || undefined,
        paymentMethod: editData.paymentMethod,
        transactionDate: editData.transactionDate.split('T')[0], // Extract date part from ISO string
      } : {}),
    }
  });

  // Reset form when editData changes
  React.useEffect(() => {
    if (editData) {
      reset({
        transactionType: editData.transactionType,
        category: editData.category,
        amount: editData.amount,
        description: editData.description || '',
        residentId: editData.residentId || undefined,
        vendorName: editData.vendorName || undefined,
        invoiceNumber: editData.invoiceNumber || undefined,
        paymentMethod: editData.paymentMethod,
        transactionDate: editData.transactionDate.split('T')[0],
      });
    }
  }, [editData, reset]);

  const type = watch('transactionType');

  return (
    <form onSubmit={handleSubmit(onSubmit)} className="space-y-4">
      <div className="grid grid-cols-2 gap-4">
        <div className="space-y-1">
          <label className="block text-sm font-medium text-on-surface">Tipo de Transacción</label>
          <select {...register('transactionType')} className="w-full px-4 py-2 rounded-md border border-outline-variant focus:ring-2 focus:ring-primary outline-none bg-surface-containerLowest">
            <option value="INCOME">Ingreso (Entrada)</option>
            <option value="EXPENSE">Egreso (Salida)</option>
          </select>
        </div>
        <div className="space-y-1">
          <label className="block text-sm font-medium text-on-surface">Monto ($)</label>
          <input 
            type="number" 
            step="0.01" 
            {...register('amount', { valueAsNumber: true })} 
            className="w-full px-4 py-2 rounded-md border border-outline-variant focus:ring-2 focus:ring-primary outline-none" 
          />
          {errors.amount && <span className="text-xs text-error">{errors.amount.message}</span>}
        </div>
      </div>

      <div className="grid grid-cols-2 gap-4">
        <div className="space-y-1">
          <label className="block text-sm font-medium text-on-surface">Categoría</label>
          <select {...register('category')} className="w-full px-4 py-2 rounded-md border border-outline-variant focus:ring-2 focus:ring-primary outline-none bg-surface-containerLowest">
            <option value="CARE_FEE">Cuota Mensual (Residente)</option>
            <option value="MEDICAL_SUPPLY">Insumos Médicos</option>
            <option value="PAYROLL">Nómina / Sueldos</option>
            <option value="UTILITY">Servicios (Luz/Agua)</option>
            <option value="MAINTENANCE">Mantenimiento</option>
            <option value="FOOD">Alimentación</option>
            <option value="OTHER">Otros</option>
          </select>
        </div>
        <div className="space-y-1">
          <label className="block text-sm font-medium text-on-surface">Método de Pago</label>
          <select {...register('paymentMethod')} className="w-full px-4 py-2 rounded-md border border-outline-variant focus:ring-2 focus:ring-primary outline-none bg-surface-containerLowest">
            <option value="CASH">Efectivo</option>
            <option value="BANK_TRANSFER">Transferencia Bancaria</option>
            <option value="CHECK">Cheque</option>
            <option value="CREDIT_CARD">Tarjeta de Crédito</option>
            <option value="INSURANCE">Seguro / Obra Social</option>
          </select>
        </div>
      </div>

      {type === 'INCOME' ? (
        <div className="space-y-1">
          <label className="block text-sm font-medium text-on-surface">Residente Relacionado</label>
          <select {...register('residentId')} className="w-full px-4 py-2 rounded-md border border-outline-variant focus:ring-2 focus:ring-primary outline-none bg-surface-containerLowest">
            <option value="">Ninguno</option>
            {residents.map(r => (
              <option key={r.id} value={r.id}>{r.firstName} {r.lastName}</option>
            ))}
          </select>
        </div>
      ) : (
        <div className="grid grid-cols-2 gap-4">
          <div className="space-y-1">
            <label className="block text-sm font-medium text-on-surface">Proveedor</label>
            <input type="text" {...register('vendorName')} placeholder="Nombre del proveedor" className="w-full px-4 py-2 rounded-md border border-outline-variant focus:ring-2 focus:ring-primary outline-none" />
          </div>
          <div className="space-y-1">
            <label className="block text-sm font-medium text-on-surface">Nro. Factura</label>
            <input type="text" {...register('invoiceNumber')} placeholder="0001-00000123" className="w-full px-4 py-2 rounded-md border border-outline-variant focus:ring-2 focus:ring-primary outline-none" />
          </div>
        </div>
      )}

      <div className="space-y-1">
        <label className="block text-sm font-medium text-on-surface">Descripción</label>
        <textarea {...register('description')} rows={2} className="w-full px-4 py-2 rounded-md border border-outline-variant focus:ring-2 focus:ring-primary outline-none"></textarea>
        {errors.description && <span className="text-xs text-error">{errors.description.message}</span>}
      </div>

      <div className="space-y-1">
        <label className="block text-sm font-medium text-on-surface">Fecha de Transacción</label>
        <input type="date" {...register('transactionDate')} className="w-full px-4 py-2 rounded-md border border-outline-variant focus:ring-2 focus:ring-primary outline-none" />
      </div>

      <div className="flex justify-end gap-3 pt-4">
        <button type="button" onClick={onCancel} className="px-4 py-2 text-primary font-medium hover:bg-surface-container rounded-md transition-colors">
          {editData ? 'Cancelar' : 'Cancelar'}
        </button>
        <button type="submit" disabled={isLoading} className={`px-6 py-2 ${type === 'INCOME' ? 'bg-primary' : 'bg-error'} text-on-primary font-bold rounded-md hover:shadow-lg transition-all disabled:opacity-50`}>
          {isLoading ? 'Procesando...' : editData ? 'Actualizar Transacción' : 'Registrar Transacción'}
        </button>
      </div>
    </form>
  );
};
