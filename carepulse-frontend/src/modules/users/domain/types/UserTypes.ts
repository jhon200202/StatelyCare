export type UserRole = 'ADMIN' | 'NURSE' | 'DOCTOR' | 'STAFF' | 'FAMILY';

export interface User {
  id: string;
  username: string;
  email: string;
  passwordHash?: string;
  role: UserRole;
  isActive: boolean;
  lastLogin?: string | null;
  createdAt: string;
  updatedAt: string;
  deletedAt?: string | null;
  createdBy?: string | null;
  updatedBy?: string | null;
}

export type CreateUserData = Pick<User, 'username' | 'email' | 'passwordHash' | 'role' | 'isActive'>;

export type UpdateUserData = Partial<Pick<User, 'username' | 'email' | 'passwordHash' | 'role' | 'isActive'>>;