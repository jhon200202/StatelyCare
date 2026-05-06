import React, { useState } from 'react';
import { reportsService } from '../../application/services/reportsService';
import { FileText, FileSpreadsheet, Download, Loader2, Info } from 'lucide-react';

export const ReportsDashboard: React.FC = () => {
  const [loading, setLoading] = useState<string | null>(null);

  const handleDownload = async (type: 'pdf' | 'excel', reportName: string) => {
    setLoading(type);
    try {
      if (reportName === 'Censo de Residentes') {
        if (type === 'pdf') await reportsService.downloadResidentCensusPdf();
        else await reportsService.downloadResidentCensusExcel();
      } else if (reportName === 'Resumen Financiero Mensual') {
        await reportsService.downloadMonthlyFinancialPdf();
      }
    } catch (error) {
      console.error('Error downloading report:', error);
      alert('Error al descargar el reporte. Verifique la conexión con el servidor.');
    } finally {
      setLoading(null);
    }
  };

  const reportCards = [
    {
      title: 'Censo de Residentes',
      description: 'Listado completo de residentes activos e inactivos con sus respectivos códigos y habitaciones.',
      icon: <FileText className="text-primary" size={32} />,
      formats: ['pdf', 'excel']
    },
    {
      title: 'Resumen Financiero Mensual',
      description: 'Estado de facturación del mes en curso, incluyendo deudas pendientes y recaudación total.',
      icon: <FileSpreadsheet className="text-secondary" size={32} />,
      formats: ['pdf']
    }
  ];

  return (
    <div className="space-y-6">
      <div>
        <h1 className="text-h2 text-on-surface mb-2">Reportes y Exportación</h1>
        <p className="text-on-surfaceVariant text-body-md">Genera documentos oficiales para administración y auditoría.</p>
      </div>

      <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
        {reportCards.map((report, idx) => (
          <div key={idx} className="bg-surface-containerLowest p-6 rounded-lg border border-outline-variant flex flex-col h-full">
            <div className="flex items-start justify-between mb-4">
              <div className="p-3 bg-surface-container rounded-lg">
                {report.icon}
              </div>
              {report.disabled && (
                <span className="px-2 py-1 bg-surface-containerHigh text-on-surfaceVariant text-[10px] font-bold rounded uppercase">Próximamente</span>
              )}
            </div>
            
            <h3 className="text-h3 text-on-surface mb-2">{report.title}</h3>
            <p className="text-on-surfaceVariant text-sm mb-6 flex-1">{report.description}</p>

            <div className="flex gap-3">
              {report.formats.map(format => (
                <button
                  key={format}
                  disabled={report.disabled || loading !== null}
                  onClick={() => handleDownload(format as any, report.title)}
                  className={`flex-1 flex items-center justify-center gap-2 px-4 py-2 rounded-md font-medium text-sm transition-all
                    ${format === 'pdf' 
                      ? 'bg-primary text-on-primary hover:bg-primary-container disabled:bg-surface-container' 
                      : 'border border-primary text-primary hover:bg-primary-container/10 disabled:border-outline disabled:text-outline'}`}
                >
                  {loading === format ? (
                    <Loader2 size={16} className="animate-spin" />
                  ) : (
                    <Download size={16} />
                  )}
                  {format.toUpperCase()}
                </button>
              ))}
            </div>
          </div>
        ))}
      </div>

      <div className="p-4 bg-primary-container/30 rounded-lg flex gap-3 items-center border border-primary/20">
        <Info className="text-primary shrink-0" size={20} />
        <p className="text-xs text-on-primaryContainer">
          Los reportes se generan en tiempo real con la información más reciente de la base de datos.
        </p>
      </div>
    </div>
  );
};
