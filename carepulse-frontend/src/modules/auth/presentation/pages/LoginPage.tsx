import React, { useState } from 'react';
import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import { loginSchema, type LoginCredentials, authService } from '../../application/services/authService';
import { useAuth } from '../../../../shared/application/context/AuthContext';
import { useNavigate } from 'react-router-dom';

export const LoginPage: React.FC = () => {
  const { login } = useAuth();
  const navigate = useNavigate();
  const [error, setError] = useState('');

  const { register, handleSubmit, formState: { errors, isSubmitting } } = useForm<LoginCredentials>({
    resolver: zodResolver(loginSchema),
  });

  const onSubmit = async (data: LoginCredentials) => {
    try {
      setError('');
      const response = await authService.login(data);
      login(response.token, { id: response.userId, username: response.username, role: response.role });
      navigate('/dashboard');
    } catch (err: any) {
      setError('Credenciales inválidas o cuenta deshabilitada.');
    }
  };

  return (
    <div className="min-h-screen flex items-center justify-center bg-surface-containerLow">
      <div className="w-full max-w-md bg-surface-containerLowest p-8 rounded-lg shadow-sm border border-outline-variant">
        <div className="text-center mb-8">
          <h1 className="text-primary font-headline font-bold text-2xl">Stately Care</h1>
          <p className="text-on-surfaceVariant mt-2">Inicia sesión en tu cuenta</p>
        </div>
        
        {error && (
          <div className="mb-6 p-4 bg-error-container text-on-errorContainer rounded-md text-sm">
            {error}
          </div>
        )}

        <form onSubmit={handleSubmit(onSubmit)} className="space-y-6">
          <div>
            <label className="block text-sm font-medium text-on-surface mb-2">Correo Electrónico</label>
            <input
              type="email"
              {...register('email')}
              className={`w-full px-4 py-3 rounded-md border ${errors.email ? 'border-error' : 'border-outline-variant'} focus:outline-none focus:border-primary`}
              placeholder="admin@statelycare.com"
            />
            {errors.email && <p className="mt-1 text-sm text-error">{errors.email.message}</p>}
          </div>

          <div>
            <label className="block text-sm font-medium text-on-surface mb-2">Contraseña</label>
            <input
              type="password"
              {...register('password')}
              className={`w-full px-4 py-3 rounded-md border ${errors.password ? 'border-error' : 'border-outline-variant'} focus:outline-none focus:border-primary`}
            />
            {errors.password && <p className="mt-1 text-sm text-error">{errors.password.message}</p>}
          </div>

          <button
            type="submit"
            disabled={isSubmitting}
            className="w-full h-12 bg-primary text-on-primary rounded-md font-medium hover:bg-primary-container hover:text-on-primaryContainer transition-colors flex items-center justify-center"
          >
            {isSubmitting ? 'Iniciando...' : 'Ingresar'}
          </button>
        </form>
      </div>
    </div>
  );
};
