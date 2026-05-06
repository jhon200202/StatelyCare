export type MealType = 'BREAKFAST' | 'LUNCH' | 'SNACK' | 'DINNER';
export type TextureModification = 'REGULAR' | 'SOFT' | 'PUREED' | 'LIQUID' | 'THICKENED_LIQUID';

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
  status: ResidentStatus;
}

export interface MenuItem {
  id: string;
  name: string;
  description?: string;
  mealType: MealType;
  textureModification: TextureModification;
  isActive: boolean;
}

export interface DailyMenu {
  id: string;
  date: string;
  mealType: MealType;
  menuItemId: string;
  servingsPlanned: number;
  notes?: string;
  residentId?: string;
}

export interface DailyMenuRequest {
  date: string;
  mealType: MealType;
  menuItemId: string;
  servingsPlanned: number;
  notes?: string;
  residentId?: string;
}

export interface AssignDietData {
  residentId: string;
  dietType: string;
  observations?: string;
}

export interface MenuItemRequest {
  name: string;
  description?: string;
  mealType: MealType;
  textureModification: TextureModification;
}

export interface DailyMenuRequest {
  date: string;
  mealType: MealType;
  menuItemId: string;
  servingsPlanned: number;
  notes?: string;
}
