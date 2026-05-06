import React from 'react';
import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import { z } from 'zod';
import type { CreateInvoiceData } from '../../domain/types/FinanceTypes';

const invoiceSchema = z.object({
  amount: z.coerce.number().min(0.01, 'El monto debe ser mayor a 0'),
  issueDate: z.string().min(1, 'La fecha de emisión es obligatoria'),
  dueDate: z.string().min(1, 'La fecha de vencimiento es obligatoria'),
  description: z.string().min(1, 'La descripción es obligatoria'),
});

interface InvoiceFormProps {
  residentId: string;
  onSubmit: (data: CreateInvoiceData) => void;
  onCancel: () => void;
  isLoading: boolean;
}

export const InvoiceForm: React.FC<InvoiceFormProps> = ({ residentId, onSubmit, onCancel, isLoading }) => {
  const { register, handleSubmit, formState: { errors } } = useForm<CreateInvoiceData>({
    resolver: zodResolver(invoiceSchema) as any,
    defaultValues: { residentId }
  });

  return (
    <form onSubmit={handleSubmit(onSubmit)} className="space-y-4">
      <input type="hidden" {...register('residentId')} />
      
      <div>
        <label className="block text-sm font-medium text-on-surface mb-1">Descripción</label>
        <input type="text" placeholder="Ej: Mensualidad Mayo" {...register('description')} className="w-full px-4 py-2 rounded-md border border-outline-variant focus:outline-none focus:border-primary" />
        {errors.description && <span className="text-xs text-error mt-1">{errors.description.message}</span>}
      </div>
      
      <div>
        <label className="block text-sm font-medium text-on-surface mb-1">Monto ($)</label>
        <input type="number" step="0.01" {...register('amount')} className="w-full px-4 py-2 rounded-md border border-outline-variant focus:outline-none focus:border-primary" />
        {errors.amount && <span className="text-xs text-error mt-1">{errors.amount.message}</span>}
      </div>

      <div className="grid grid-cols-2 gap-4">
        <div>
          <label className="block text-sm font-medium text-on-surface mb-1">Emisión</label>
          <input type="date" {...register('issueDate')} className="w-full px-4 py-2 rounded-md border border-outline-variant focus:outline-none focus:border-primary" />
          {errors.issueDate && <span className="text-xs text-error mt-1">{errors.issueDate.message}</span>}
        </div>
        <div>
          <label className="block text-sm font-medium text-on-surface mb-1">Vencimiento</label>
          <input type="date" {...register('dueDate')} className="w-full px-4 py-2 rounded-md border border-outline-variant focus:outline-none focus:border-primary" />
          {errors.dueDate && <span className="text-xs text-error mt-1">{errors.dueDate.message}</span>}
        </div>
      </div>

      <div className="flex justify-end gap-3 pt-6">
        <button type="button" onClick={onCancel} className="px-4 py-2 text-primary font-medium hover:bg-surface-container rounded-md transition-colors">
          Cancelar
        </button>
        <button type="submit" disabled={isLoading} className="px-6 py-2 bg-primary text-on-primary font-medium rounded-md hover:bg-primary-container transition-colors disabled:opacity-50">
          {isLoading ? 'Guardando...' : 'Generar Factura'}
        </button>
      </div>
    </form>
  );
};
