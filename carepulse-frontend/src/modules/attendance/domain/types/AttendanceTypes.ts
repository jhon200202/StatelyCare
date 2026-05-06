export interface AttendanceRecord {
  id: string;
  employeeId: string;
  shiftDate: string;
  actualClockIn: string | null;
  actualClockOut: string | null;
  status: 'PRESENT' | 'ABSENT' | 'LATE' | 'LEFT_EARLY';
  lateMinutes: number;
  totalHours?: number;
  notes?: string;
}
