package com.statelycare.erp.reports.infrastructure.web;

import com.statelycare.erp.reports.application.service.ReportService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/reports")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/residents/pdf")
    public ResponseEntity<byte[]> downloadResidentCensusPdf() {
        byte[] pdf = reportService.generateResidentCensusPdf();
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "censo_residentes.pdf");
        
        return ResponseEntity.ok().headers(headers).body(pdf);
    }

    @GetMapping("/residents/excel")
    public ResponseEntity<byte[]> downloadResidentCensusExcel() throws IOException {
        byte[] excel = reportService.generateResidentCensusExcel();
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
        headers.setContentDispositionFormData("attachment", "censo_residentes.xlsx");
        
        return ResponseEntity.ok().headers(headers).body(excel);
    }

    @GetMapping("/financial/pdf")
    public ResponseEntity<byte[]> downloadMonthlyFinancialPdf() {
        byte[] pdf = reportService.generateMonthlyFinancialPdf();
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "resumen_financiero.pdf");
        
        return ResponseEntity.ok().headers(headers).body(pdf);
    }
}
