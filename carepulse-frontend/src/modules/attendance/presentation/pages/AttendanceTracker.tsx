import React, { useState } from 'react';
import { useQuery, useMutation } from '@tanstack/react-query';
import { attendanceService } from '../../application/services/attendanceService';
import { employeeService } from '../../../employee/application/services/employeeService';
import { DataTable } from '../../../../components/ui/DataTable';
import type { AttendanceRecord } from '../../domain/types/AttendanceTypes';
import { Clock, LogIn, LogOut, AlertCircle } from 'lucide-react';

export const AttendanceTracker: React.FC = () => {
  const [selectedEmployeeId, setSelectedEmployeeId] = useState('');
  
  const { data: employees = [] } = useQuery({
    queryKey: ['employees'],
    queryFn: employeeService.getAll,
  });

  const { data: attendanceList = [], isLoading, refetch } = useQuery({
    queryKey: ['attendance', 'daily'],
    queryFn: () => attendanceService.getDaily(),
  });

  const { mutate: clockIn, isPending: isClockingIn } = useMutation({
    mutationFn: () => attendanceService.clockIn(selectedEmployeeId),
    onSuccess: () => {
      setSelectedEmployeeId('');
      refetch();
    },
    onError: (err: any) => alert(err.response?.data?.message || 'Error al registrar entrada'),
  });

  const { mutate: clockOut, isPending: isClockingOut } = useMutation({
    mutationFn: (id: string) => attendanceService.clockOut(id),
    onSuccess: () => {
      setSelectedEmployeeId('');
      refetch();
    },
    onError: (err: any) => alert(err.response?.data?.message || 'Error al registrar salida'),
  });

  const getEmployeeName = (id: string) => {
    const emp = employees.find(e => e.id === id);
    return emp ? `${emp.firstName} ${emp.lastName}` : 'Desconocido';
  };

  // Find if selected employee has an active clock-in
  const activeRecord = attendanceList.find(r => r.employeeId === selectedEmployeeId && !r.actualClockOut);

  const columns = [
    { key: 'employeeId' as keyof AttendanceRecord, header: 'Empleado', render: (val: string) => getEmployeeName(val) },
    { key: 'shiftDate' as keyof AttendanceRecord, header: 'Fecha' },
    { 
      key: 'actualClockIn' as keyof AttendanceRecord, 
      header: 'Entrada',
      render: (val: string) => val ? new Date(val).toLocaleTimeString([], {hour: '2-digit', minute:'2-digit'}) : '-'
    },
    { 
      key: 'actualClockOut' as keyof AttendanceRecord, 
      header: 'Salida',
      render: (val: string | null) => val ? new Date(val).toLocaleTimeString([], {hour: '2-digit', minute:'2-digit'}) : '-'
    },
    {
      key: 'totalHours' as keyof AttendanceRecord,
      header: 'Horas',
      render: (val: number | null) => val ? <span className="font-bold text-primary">{val}h</span> : '-'
    },
    { 
      key: 'status' as keyof AttendanceRecord, 
      header: 'Estado',
      render: (val: string, row: AttendanceRecord) => (
        <div className="flex items-center gap-3">
          <span className={`px-3 py-1 rounded-full text-xs font-bold ${
            val === 'PRESENT' ? 'bg-primary-container text-on-primaryContainer' : 
            val === 'LATE' ? 'bg-error-container text-on-errorContainer' :
            'bg-surface-variant text-on-surfaceVariant'
          }`}>
            {val}
          </span>
          {!row.actualClockOut && (
            <button 
              onClick={() => clockOut(row.employeeId)}
              className="flex items-center gap-1 text-xs font-bold text-error hover:bg-error-container px-2 py-1 rounded-md transition-all"
              title="Registrar Salida"
            >
              <LogOut size={14} />
              SALIDA
            </button>
          )}
        </div>
      )
    },
    { key: 'lateMinutes' as keyof AttendanceRecord, header: 'Min. Tarde', render: (val: number) => val > 0 ? <span className="text-error font-bold">{val}m</span> : '-' },
  ];

  return (
    <div className="space-y-6">
      <div className="flex justify-between items-center">
        <div>
          <h1 className="text-h2 text-on-surface mb-2">Control de Asistencia</h1>
          <p className="text-on-surfaceVariant text-body-md">Registro en tiempo real de entradas y salidas del personal.</p>
        </div>
      </div>

      <div className="bg-surface-containerLowest p-6 rounded-lg border border-outline-variant shadow-sm flex gap-4 items-end">
        <div className="flex-1">
          <label className="block text-sm font-medium text-on-surface mb-2">Seleccionar Empleado para Fichar</label>
          <select 
            value={selectedEmployeeId}
            onChange={(e) => setSelectedEmployeeId(e.target.value)}
            className="w-full px-4 py-2.5 rounded-md border border-outline-variant focus:ring-2 focus:ring-primary outline-none transition-all"
          >
            <option value="">Seleccione personal...</option>
            {employees.filter(e => e.isActive).map(emp => (
              <option key={emp.id} value={emp.id}>
                {emp.firstName} {emp.lastName} — {emp.roleTitle} ({emp.department})
              </option>
            ))}
          </select>
        </div>
        <div className="flex gap-2">
          {!activeRecord ? (
            <button 
              onClick={() => clockIn()}
              disabled={!selectedEmployeeId || isClockingIn}
              className="flex items-center gap-2 h-11 px-8 bg-primary text-on-primary rounded-md font-bold hover:shadow-lg hover:bg-primary-container transition-all disabled:opacity-50"
            >
              <LogIn size={20} />
              {isClockingIn ? 'PROCESANDO...' : 'REGISTRAR ENTRADA'}
            </button>
          ) : (
            <button 
              onClick={() => clockOut(selectedEmployeeId)}
              disabled={isClockingOut}
              className="flex items-center gap-2 h-11 px-8 bg-error text-on-error rounded-md font-bold hover:shadow-lg hover:bg-error/90 transition-all disabled:opacity-50"
            >
              <LogOut size={20} />
              {isClockingOut ? 'PROCESANDO...' : 'REGISTRAR SALIDA'}
            </button>
          )}
        </div>
      </div>

      <div className="bg-surface-containerLowest rounded-lg shadow-sm border border-outline-variant overflow-hidden">
        <div className="p-4 border-b border-outline-variant flex items-center justify-between bg-surface-containerLow">
          <div className="flex items-center gap-2">
            <Clock size={20} className="text-primary" />
            <h2 className="text-h3 text-on-surface">Registros del Día</h2>
          </div>
          <div className="flex items-center gap-2 text-xs text-on-surfaceVariant bg-surface-variant/20 px-3 py-1 rounded-full">
            <AlertCircle size={14} />
            Los horarios se basan en la zona horaria local.
          </div>
        </div>
        {isLoading ? (
          <div className="p-8 text-on-surfaceVariant">Cargando registros...</div>
        ) : (
          <DataTable data={attendanceList} columns={columns} />
        )}
      </div>
    </div>
  );
};
