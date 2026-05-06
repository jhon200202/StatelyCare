import React from 'react';
import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import { LoginPage } from './modules/auth/presentation/pages/LoginPage';
import { useAuth } from './shared/application/context/AuthContext';

import { DashboardPage } from './modules/dashboard/presentation/pages/DashboardPage';
import { ResidentsList } from './modules/residents/presentation/pages/ResidentsList';
import { EmployeesList } from './modules/employee/presentation/pages/EmployeesList';
import { AttendanceTracker } from './modules/attendance/presentation/pages/AttendanceTracker';
import { TreatmentList } from './modules/treatment/presentation/pages/TreatmentList';
import { TreatmentReminders } from './modules/treatment/presentation/pages/TreatmentReminders';
import { RoomsList } from './modules/room/presentation/pages/RoomsList';
import { FinanceOverview } from './modules/finance/presentation/pages/FinanceOverview';
import { NutritionDashboard } from './modules/nutrition/presentation/pages/NutritionDashboard';
import { ReportsDashboard } from './modules/reports/presentation/pages/ReportsDashboard';
import { UsersOverview } from './modules/users/presentation/pages/UsersOverview';
import { MainLayout } from './components/layout/MainLayout';

const ProtectedRoute = ({ children, allowedRoles }: { children: React.ReactNode; allowedRoles?: string[] }) => {
  const { isAuthenticated, user } = useAuth();
  if (!isAuthenticated) {
    return <Navigate to="/login" replace />;
  }
  if (allowedRoles && user && !allowedRoles.includes(user.role)) {
    return <Navigate to="/dashboard" replace />;
  }
  return children;
};

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/login" element={<LoginPage />} />
        
        {/* Protected Routes */}
        <Route
          path="/dashboard"
          element={
            <ProtectedRoute allowedRoles={['ADMIN', 'DOCTOR', 'NURSE', 'STAFF', 'FAMILY']}>
              <MainLayout>
                <DashboardPage />
              </MainLayout>
            </ProtectedRoute>
          }
        />
        <Route
          path="/residents"
          element={
            <ProtectedRoute allowedRoles={['ADMIN', 'DOCTOR', 'NURSE', 'STAFF']}>
              <MainLayout>
                <ResidentsList />
              </MainLayout>
            </ProtectedRoute>
          }
        />
        <Route
          path="/employees"
          element={
            <ProtectedRoute allowedRoles={['ADMIN']}>
              <MainLayout>
                <EmployeesList />
              </MainLayout>
            </ProtectedRoute>
          }
        />
        <Route
          path="/attendance"
          element={
            <ProtectedRoute allowedRoles={['ADMIN', 'DOCTOR', 'NURSE', 'STAFF']}>
              <MainLayout>
                <AttendanceTracker />
              </MainLayout>
            </ProtectedRoute>
          }
        />
        <Route
          path="/rooms"
          element={
            <ProtectedRoute allowedRoles={['ADMIN', 'DOCTOR', 'NURSE', 'STAFF']}>
              <MainLayout>
                <RoomsList />
              </MainLayout>
            </ProtectedRoute>
          }
        />
        <Route
          path="/treatments"
          element={
            <ProtectedRoute allowedRoles={['ADMIN', 'DOCTOR', 'NURSE']}>
              <MainLayout>
                <TreatmentList />
              </MainLayout>
            </ProtectedRoute>
          }
        />
        <Route
          path="/treatments/reminders"
          element={
            <ProtectedRoute allowedRoles={['ADMIN', 'DOCTOR', 'NURSE']}>
              <MainLayout>
                <TreatmentReminders />
              </MainLayout>
            </ProtectedRoute>
          }
        />
        <Route
          path="/finance"
          element={
            <ProtectedRoute allowedRoles={['ADMIN']}>
              <MainLayout>
                <FinanceOverview />
              </MainLayout>
            </ProtectedRoute>
          }
        />
        <Route
          path="/menu"
          element={
            <ProtectedRoute allowedRoles={['ADMIN', 'STAFF']}>
              <MainLayout>
                <NutritionDashboard />
              </MainLayout>
            </ProtectedRoute>
          }
        />
        <Route
          path="/reports"
          element={
            <ProtectedRoute allowedRoles={['ADMIN', 'DOCTOR']}>
              <MainLayout>
                <ReportsDashboard />
              </MainLayout>
            </ProtectedRoute>
          }
        />
        <Route
          path="/users"
          element={
            <ProtectedRoute allowedRoles={['ADMIN']}>
              <MainLayout>
                <UsersOverview />
              </MainLayout>
            </ProtectedRoute>
          }
        />
        
        {/* Default route */}
        <Route path="*" element={<Navigate to="/dashboard" replace />} />
      </Routes>
    </BrowserRouter>
  );
}

export default App;
