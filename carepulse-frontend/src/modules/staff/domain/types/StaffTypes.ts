export interface Staff {
  id: string;
  firstName: string;
  lastName: string;
  professionalRole: string;
  shift: string;
  hireDate: string;
  isActive: boolean;
}

export type CreateStaffData = Omit<Staff, 'id' | 'isActive'>;
