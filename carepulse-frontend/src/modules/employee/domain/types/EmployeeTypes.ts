export type Department = 'NURSING' | 'MEDICAL' | 'ADMIN' | 'MAINTENANCE' | 'KITCHEN' | 'SECURITY';
export type CertificationStatus = 'VERIFIED' | 'PENDING' | 'EXPIRED';
export type ShiftPreference = 'MORNING' | 'AFTERNOON' | 'NIGHT';

export interface Employee {
  id: string;
  userId?: string;
  employeeCode: string;
  firstName: string;
  lastName: string;
  department: Department;
  roleTitle: string;
  hireDate: string;
  certificationStatus: CertificationStatus;
  phone?: string;
  email?: string;
  shiftPreference?: ShiftPreference;
  isActive: boolean;
  createdAt: string;
  updatedAt: string;
}

export type CreateEmployeeData = Pick<Employee, 'userId' | 'firstName' | 'lastName' | 'department' | 'roleTitle' | 'hireDate'>;
