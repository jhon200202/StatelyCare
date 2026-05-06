export type TreatmentType = 'MEDICATION' | 'THERAPY' | 'PROCEDURE' | 'CHECKUP';
export type Frequency = 'ONCE' | 'DAILY' | 'WEEKLY' | 'MONTHLY' | 'AS_NEEDED';
export type TreatmentStatus = 'ACTIVE' | 'COMPLETED' | 'DISCONTINUED' | 'ON_HOLD';

export interface DailyTreatmentReminder {
  treatmentId: string;
  residentId: string;
  residentName: string;
  residentCode: string;
  treatmentName: string;
  treatmentType: TreatmentType;
  frequency: Frequency;
  scheduledTime: string;
  administeredToday: boolean;
  status: TreatmentStatus;
}

export interface Treatment {
  id: string;
  residentId: string;
  prescribedBy: string;
  treatmentName: string;
  treatmentType: TreatmentType;
  frequency: Frequency;
  scheduledTime: string;
  startDate: string;
  endDate: string | null;
  status: TreatmentStatus;
}

export interface MedicationAdministration {
  id: string;
  treatmentId: string;
  administeredBy: string;
  administeredAt: string;
  notes?: string;
}

export interface CreateTreatmentRequest {
  residentId: string;
  prescribedBy: string;
  treatmentName: string;
  treatmentType: TreatmentType;
  frequency: Frequency;
  scheduledTime: string;
  startDate: string;
  endDate?: string;
}

export interface AdministerRequest {
  treatmentId: string;
  administeredBy: string;
  dosageGiven: string;
  route: string;
  notes?: string;
}
