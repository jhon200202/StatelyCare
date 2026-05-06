export interface User {
  id: string;
  username: string;
  role: 'ADMIN' | 'NURSE' | 'DOCTOR' | 'STAFF' | 'FAMILY';
}

export interface LoginResponse {
  token: string;
  userId: string;
  username: string;
  role: User['role'];
}
