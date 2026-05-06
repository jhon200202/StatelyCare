import React, { useState } from 'react';
import { useQuery, useMutation } from '@tanstack/react-query';
import { financeService } from '../../application/services/financeService';
import { residentService } from '../../../residents/application/services/residentService';
import { DataTable } from '../../../../components/ui/DataTable';
import { Modal } from '../../../../components/ui/Modal';
import { TransactionForm } from '../components/TransactionForm';
import type { FinancialTransaction } from '../../domain/types/FinanceTypes';
import { BadgeDollarSign, Plus, ArrowUpCircle, ArrowDownCircle, Trash2, Edit2 } from 'lucide-react';

export const FinanceOverview: React.FC = () => {
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [editTransaction, setEditTransaction] = useState<FinancialTransaction | null>(null);

  const { data: residents = [] } = useQuery({
    queryKey: ['residents'],
    queryFn: residentService.getAll,
  });

  const { data: transactions = [], isLoading, refetch } = useQuery({
    queryKey: ['transactions'],
    queryFn: financeService.getAllTransactions,
  });

  const createMutation = useMutation({
    mutationFn: financeService.createTransaction,
    onSuccess: () => {
      setIsModalOpen(false);
      setEditTransaction(null);
      refetch();
    },
    onError: () => alert('Error al registrar la transacción'),
  });

  const deleteMutation = useMutation({
    mutationFn: financeService.deleteTransaction,
    onSuccess: () => {
      refetch();
    },
  });

  const updateMutation = useMutation({
    mutationFn: ({ id, data }: { id: string; data: any }) => financeService.updateTransaction(id, data),
    onSuccess: () => {
      setIsModalOpen(false);
      setEditTransaction(null);
      refetch();
    },
    onError: () => alert('Error al actualizar la transacción'),
  });

  const handleEdit = (transaction: FinancialTransaction) => {
    setEditTransaction(transaction);
    setIsModalOpen(true);
  };

  const getResidentName = (id: string | null) => {
    if (!id) return '-';
    const r = residents.find(res => res.id === id);
    return r ? `${r.firstName} ${r.lastName}` : 'Desconocido';
  };

  const columns = [
    { 
      key: 'transactionType' as keyof FinancialTransaction, 
      header: 'Tipo',
      render: (val: string) => (
        <div className="flex items-center gap-2">
          {val === 'INCOME' ? (
            <ArrowUpCircle size={18} className="text-primary" />
          ) : (
            <ArrowDownCircle size={18} className="text-error" />
          )}
          <span className="font-medium">{val === 'INCOME' ? 'Ingreso' : 'Egreso'}</span>
        </div>
      )
    },
    { key: 'category' as keyof FinancialTransaction, header: 'Categoría' },
    { key: 'description' as keyof FinancialTransaction, header: 'Descripción' },
    { key: 'residentId' as keyof FinancialTransaction, header: 'Residente', render: (val: any) => getResidentName(val) },
    { 
      key: 'amount' as keyof FinancialTransaction, 
      header: 'Monto', 
      render: (val: number, row: FinancialTransaction) => (
        <span className={`font-bold ${row.transactionType === 'INCOME' ? 'text-primary' : 'text-error'}`}>
          {row.transactionType === 'INCOME' ? '+' : '-'}${val.toLocaleString()}
        </span>
      )
    },
    { key: 'transactionDate' as keyof FinancialTransaction, header: 'Fecha' },
    {
      key: 'actions' as keyof FinancialTransaction,
      header: '',
      render: (_: any, t: FinancialTransaction) => (
        <>
          <button
            onClick={() => {
              handleEdit(t);
            }}
            className="p-1 hover:text-primary"
            title="Editar"
          >
            <Edit2 size={16} />
          </button>
          <button
            onClick={() => {
              if (confirm('¿Estás seguro de eliminar esta transacción?')) {
                deleteMutation.mutate(t.id);
              }
            }}
            className="p-1 hover:text-error ms-2"
            title="Eliminar"
          >
            <Trash2 size={16} />
          </button>
        </>
      ),
    },
  ];

  const balance = transactions.reduce((acc, t) => 
    t.transactionType === 'INCOME' ? acc + t.amount : acc - t.amount, 0
  );

  return (
    <div className="space-y-6">
      <div className="flex justify-between items-center">
        <div>
          <h1 className="text-h2 text-on-surface mb-2">Gestión Financiera</h1>
          <p className="text-on-surfaceVariant text-body-md">Control de ingresos, egresos y facturación centralizada.</p>
        </div>
        <button 
          onClick={() => setIsModalOpen(true)}
          className="flex items-center gap-2 bg-primary text-on-primary px-6 py-2.5 rounded-md font-bold hover:shadow-lg hover:bg-primary-container transition-all"
        >
          <Plus size={18} />
          <span>Nueva Transacción</span>
        </button>
      </div>

      <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
        <div className="bg-surface-containerLowest p-6 rounded-lg border border-outline-variant shadow-sm">
          <p className="text-sm text-on-surfaceVariant font-medium mb-1">Balance General</p>
          <h3 className={`text-3xl font-bold ${balance >= 0 ? 'text-primary' : 'text-error'}`}>
            ${balance.toLocaleString()}
          </h3>
        </div>
      </div>

      <div className="bg-surface-containerLowest rounded-lg shadow-sm border border-outline-variant overflow-hidden">
        <div className="p-4 border-b border-outline-variant flex items-center gap-2 bg-surface-containerLow">
          <BadgeDollarSign size={20} className="text-primary" />
          <h2 className="text-h3 text-on-surface">Historial de Transacciones</h2>
        </div>
        {isLoading ? (
          <div className="p-8 text-on-surfaceVariant">Cargando transacciones...</div>
        ) : (
          <DataTable data={transactions} columns={columns} />
        )}
      </div>

      <Modal isOpen={isModalOpen} onClose={() => {
        setIsModalOpen(false);
        setEditTransaction(null);
      }} 
      title={editTransaction ? 'Editar Transacción' : 'Registrar Transacción'}
      >
        <TransactionForm 
          residents={residents}
          editData={editTransaction || undefined}
          onSubmit={(data) => {
            if (editTransaction) {
              updateMutation.mutate({ id: editTransaction.id, data });
            } else {
              createMutation.mutate(data as any);
            }
          }} 
          onCancel={() => {
            setIsModalOpen(false);
            setEditTransaction(null);
          }} 
          isLoading={createMutation.isPending || updateMutation.isPending} 
        />
      </Modal>
    </div>
  );
};
