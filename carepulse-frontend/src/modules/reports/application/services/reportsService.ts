import apiClient from '../../../../shared/infrastructure/http/axiosConfig';

export const reportsService = {
  downloadResidentCensusPdf: async () => {
    const response = await apiClient.get('/reports/residents/pdf', {
      responseType: 'blob',
    });
    const url = window.URL.createObjectURL(new Blob([response.data]));
    const link = document.createElement('a');
    link.href = url;
    link.setAttribute('download', 'censo_residentes.pdf');
    document.body.appendChild(link);
    link.click();
    link.remove();
  },
  downloadResidentCensusExcel: async () => {
    const response = await apiClient.get('/reports/residents/excel', {
      responseType: 'blob',
    });
    const url = window.URL.createObjectURL(new Blob([response.data]));
    const link = document.createElement('a');
    link.href = url;
    link.setAttribute('download', 'censo_residentes.xlsx');
    document.body.appendChild(link);
    link.click();
    link.remove();
  },
  downloadMonthlyFinancialPdf: async () => {
    const response = await apiClient.get('/reports/financial/pdf', {
      responseType: 'blob',
    });
    const url = window.URL.createObjectURL(new Blob([response.data]));
    const link = document.createElement('a');
    link.href = url;
    link.setAttribute('download', 'resumen_financiero.pdf');
    document.body.appendChild(link);
    link.click();
    link.remove();
  },
};
