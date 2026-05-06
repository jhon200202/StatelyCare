# CarePulse ERP - Sistema de Gestión para Hogar de Ancianos

CarePulse ERP es una solución integral de software diseñada para modernizar y optimizar la gestión operativa, clínica y administrativa de hogares de cuidado para adultos mayores. El sistema facilita el seguimiento detallado de residentes, tratamientos médicos, personal, finanzas y nutrición bajo una arquitectura robusta y escalable.

## Características Principales

### Gestión Clínica y de Residentes
- **Censo de Residentes**: Registro completo de datos personales, historial médico y planes de cuidado.
- **Control de Tratamientos**: Prescripción y seguimiento de medicamentos, terapias y chequeos.
- **Recordatorios Médicos**: Alertas en tiempo real para la administración de medicación programada.

### Recursos Humanos y Asistencia
- **Gestión de Empleados**: Perfiles detallados de personal médico, enfermería y administrativo.
- **Control de Asistencia**: Registro de entradas, salidas y cumplimiento de turnos.

### Administración y Finanzas
- **Gestión Financiera**: Registro de ingresos y egresos, control de facturación y categorías de gastos.
- **Reportes Financieros**: Generación de resúmenes mensuales automáticos en PDF.

### Nutrición y Servicios
- **Planificación de Menús**: Gestión de catálogo de alimentos y planificación de comidas diarias según tipos de dieta y texturas.

### Reportes y Auditoría
- **Generación de Documentos**: Exportación de censos y estados financieros en formatos PDF y Excel.
- **Niveles de Acceso (RBAC)**: Seguridad basada en roles (ADMIN, DOCTOR, NURSE, STAFF, FAMILY).

---

## Stack Tecnológico

### Backend
- **Lenguaje**: Java 17
- **Framework**: Spring Boot 3.x
- **Persistencia**: Spring Data JPA / Hibernate
- **Base de Datos**: MySQL 8.0
- **Seguridad**: JWT (JSON Web Tokens)
- **Migraciones**: Flyway

### Frontend
- **Framework**: React 18 (TypeScript)
- **Estado**: TanStack Query (React Query)
- **Estilos**: CSS Moderno (Variables, Flexbox/Grid)
- **Iconos**: Lucide React
- **Formularios**: React Hook Form + Zod

---

## Configuración e Instalación

### Requisitos Previos
- **Java JDK 17** o superior.
- **Node.js** (v16+) y **npm**.
- **MySQL 8.0**.

### 1. Configuración de la Base de Datos
Cree una base de datos en MySQL:
```sql
CREATE DATABASE carepulse_db;
```
Configure las credenciales en `carepulse-backend/src/main/resources/application.properties` (o mediante variables de entorno).

### 2. Ejecutar el Backend
Navegue a la carpeta del backend y ejecute:
```bash
cd carepulse-backend
mvn spring-boot:run
```
*Flyway ejecutará automáticamente las migraciones para crear las tablas.*

### 3. Ejecutar el Frontend
Navegue a la carpeta del frontend e instale las dependencias:
```bash
cd carepulse-frontend
npm install
npm run dev
```
La aplicación estará disponible en `http://localhost:5173`.

---

## Arquitectura

El proyecto sigue los principios de **Clean Architecture**, separando las responsabilidades en capas:
- **Domain**: Entidades de negocio y contratos de repositorio (Inmune a frameworks externos).
- **Application**: Casos de uso y lógica de aplicación.
- **Infrastructure**: Implementaciones de persistencia, seguridad y controladores Web.

---

## Roles de Usuario

1. **ADMIN**: Control total del sistema, gestión de usuarios, finanzas y empleados.
2. **DOCTOR**: Gestión clínica de residentes, prescripción de tratamientos y reportes médicos.
3. **NURSE**: Seguimiento de tratamientos, administración de medicación y asistencia.
4. **STAFF**: Gestión de servicios (Menú, Habitaciones) y censo básico.
5. **FAMILY**: Visualización limitada de información relevante del residente.

---

## 📄 Licencia
Este proyecto es de uso privado para prácticas profesionales. Todos los derechos reservados.
