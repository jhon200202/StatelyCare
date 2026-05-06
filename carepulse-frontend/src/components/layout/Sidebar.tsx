import React from 'react';
import { NavLink } from 'react-router-dom';
import { useAuth } from '../../shared/application/context/AuthContext';
import { LayoutDashboard, Users, UserRound, CalendarClock, Pill, BadgeDollarSign, Utensils, FileBarChart, LogOut, Bell, DoorOpen, UserCog } from 'lucide-react';

export const Sidebar: React.FC = () => {
  const { logout, user } = useAuth();

  const navItems = [
    { name: 'Dashboard', path: '/dashboard', icon: <LayoutDashboard size={20} />, roles: ['ADMIN', 'DOCTOR', 'NURSE', 'STAFF', 'FAMILY'] },
    { name: 'Residentes', path: '/residents', icon: <Users size={20} />, roles: ['ADMIN', 'DOCTOR', 'NURSE', 'STAFF'] },
    { name: 'Habitaciones', path: '/rooms', icon: <DoorOpen size={20} />, roles: ['ADMIN', 'DOCTOR', 'NURSE', 'STAFF'] },
    { name: 'Empleados', path: '/employees', icon: <UserRound size={20} />, roles: ['ADMIN'] },
    { name: 'Asistencia', path: '/attendance', icon: <CalendarClock size={20} />, roles: ['ADMIN', 'DOCTOR', 'NURSE', 'STAFF'] },
    { name: 'Tratamientos', path: '/treatments', icon: <Pill size={20} />, roles: ['ADMIN', 'DOCTOR', 'NURSE'] },
    { name: 'Recordatorios', path: '/treatments/reminders', icon: <Bell size={20} />, roles: ['ADMIN', 'DOCTOR', 'NURSE'] },
    { name: 'Finanzas', path: '/finance', icon: <BadgeDollarSign size={20} />, roles: ['ADMIN'] },
    { name: 'Menú', path: '/menu', icon: <Utensils size={20} />, roles: ['ADMIN', 'STAFF'] },
    { name: 'Reportes', path: '/reports', icon: <FileBarChart size={20} />, roles: ['ADMIN', 'DOCTOR'] },
    { name: 'Usuarios', path: '/users', icon: <UserCog size={20} />, roles: ['ADMIN'] },
  ];

  const filteredItems = navItems.filter(item => 
    user && item.roles.includes(user.role)
  );

  return (
    <aside className="w-[280px] h-screen bg-surface-containerLowest border-r border-outline-variant flex flex-col fixed left-0 top-0">
      <div className="h-16 flex items-center px-6 border-b border-outline-variant">
        <h1 className="text-xl font-headline font-bold text-primary">Stately Care</h1>
      </div>
      
      <nav className="flex-1 px-4 py-6 space-y-2 overflow-y-auto">
        {filteredItems.map((item) => (
          <NavLink
            key={item.path}
            to={item.path}
            className={({ isActive }) =>
              `flex items-center gap-3 px-4 py-3 rounded-md transition-colors font-medium ${
                isActive 
                  ? 'bg-primary-container text-on-primaryContainer' 
                  : 'text-on-surfaceVariant hover:bg-surface-container hover:text-on-surface'
              }`
            }
          >
            {item.icon}
            {item.name}
          </NavLink>
        ))}
      </nav>

      <div className="p-4 border-t border-outline-variant">
        <div className="flex items-center gap-3 px-4 py-3 mb-2">
          <div className="w-8 h-8 rounded-full bg-primary-dim flex items-center justify-center text-primary font-bold">
            {user?.username?.charAt(0).toUpperCase()}
          </div>
          <div className="flex-1 overflow-hidden">
            <p className="text-sm font-semibold truncate text-on-surface">{user?.username}</p>
            <p className="text-xs text-on-surfaceVariant capitalize">{user?.role.toLowerCase()}</p>
          </div>
        </div>
        <button 
          onClick={logout}
          className="w-full flex items-center gap-3 px-4 py-2 text-error hover:bg-error-container hover:text-on-errorContainer rounded-md transition-colors text-sm font-medium"
        >
          <LogOut size={18} />
          Cerrar Sesión
        </button>
      </div>
    </aside>
  );
};
