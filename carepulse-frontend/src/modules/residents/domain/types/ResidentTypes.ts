export type ResidentStatus = 'ON_SITE' | 'HOSPITALIZED' | 'OUT_ON_LEAVE' | 'DECEASED';
export type Gender = 'MALE' | 'FEMALE' | 'OTHER';

export interface Resident {
  id: string;
  residentCode: string;
  firstName: string;
  lastName: string;
  dateOfBirth: string;
  gender: Gender;
  roomId?: string;
  admissionDate: string;
  primaryPhysicianId?: string;
  carePlan?: string;
  medicalHistorySummary?: string;
  status: ResidentStatus;
  createdAt: string;
  updatedAt: string;
}

export type CreateResidentData = Pick<Resident, 'firstName' | 'lastName' | 'dateOfBirth' | 'gender' | 'roomId' | 'admissionDate'>;
