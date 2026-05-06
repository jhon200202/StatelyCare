import React, { useState } from 'react';
import { useQuery } from '@tanstack/react-query';
import { employeeService } from '../../application/services/employeeService';
import { Modal } from '../../../../components/ui/Modal';
import { DataTable } from '../../../../components/ui/DataTable';
import { Search, UserCheck } from 'lucide-react';
import type { Employee } from '../../domain/types/EmployeeTypes';

interface EmployeePickerModalProps {
  isOpen: boolean;
  onClose: () => void;
  onSelect: (employee: Employee) => void;
  departmentFilter?: string[];
}

export const EmployeePickerModal: React.FC<EmployeePickerModalProps> = ({ 
  isOpen, 
  onClose, 
  onSelect,
  departmentFilter = ['MEDICAL', 'NURSING']
}) => {
  const [searchTerm, setSearchTerm] = useState('');

  const { data: employees = [], isLoading } = useQuery({
    queryKey: ['employees'],
    queryFn: employeeService.getAll,
  });

  const filteredEmployees = employees.filter(emp => {
    const matchesSearch = `${emp.firstName} ${emp.lastName}`.toLowerCase().includes(searchTerm.toLowerCase()) ||
                         emp.employeeCode.toLowerCase().includes(searchTerm.toLowerCase()) ||
                         emp.department.toLowerCase().includes(searchTerm.toLowerCase());
    
    const matchesDept = departmentFilter.length === 0 || departmentFilter.includes(emp.department);
    
    return emp.isActive && matchesSearch && matchesDept;
  });

  const columns = [
    { key: 'employeeCode' as keyof Employee, header: 'Código' },
    { 
      key: 'firstName' as keyof Employee, 
      header: 'Nombre Completo', 
      render: (_: any, row: Employee) => `${row.firstName} ${row.lastName}` 
    },
    { key: 'department' as keyof Employee, header: 'Departamento' },
    { key: 'roleTitle' as keyof Employee, header: 'Cargo' },
    { 
      key: 'id' as keyof Employee, 
      header: 'Seleccionar',
      render: (_: any, row: Employee) => (
        <button
          onClick={() => {
            onSelect(row);
            onClose();
          }}
          className="p-2 text-primary hover:bg-primary-container rounded-full transition-colors"
          title="Seleccionar empleado"
        >
          <UserCheck size={20} />
        </button>
      )
    }
  ];

  return (
    <Modal isOpen={isOpen} onClose={onClose} title="Explorar Personal Médico">
      <div className="space-y-4">
        <div className="relative">
          <Search className="absolute left-3 top-1/2 -translate-y-1/2 text-on-surfaceVariant" size={18} />
          <input
            type="text"
            placeholder="Buscar por nombre, código o departamento..."
            value={searchTerm}
            onChange={(e) => setSearchTerm(e.target.value)}
            className="w-full pl-10 pr-4 py-2 rounded-md border border-outline-variant focus:ring-2 focus:ring-primary outline-none transition-all"
          />
        </div>

        <div className="border border-outline-variant rounded-lg overflow-hidden max-h-[60vh] overflow-y-auto">
          {isLoading ? (
            <div className="p-8 text-center text-on-surfaceVariant">Cargando personal...</div>
          ) : (
            <DataTable 
              data={filteredEmployees} 
              columns={columns} 
              onRowDoubleClick={(row) => {
                onSelect(row);
                onClose();
              }}
            />
          )}
        </div>
      </div>
    </Modal>
  );
};
