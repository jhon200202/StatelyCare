package com.statelycare.erp.reports.application.service;

import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.statelycare.erp.finance.domain.model.FinancialTransaction;
import com.statelycare.erp.finance.domain.model.TransactionType;
import com.statelycare.erp.finance.domain.repository.FinancialTransactionRepository;
import com.statelycare.erp.resident.domain.model.Resident;
import com.statelycare.erp.resident.domain.repository.ResidentRepository;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;

@Service
public class ReportService {

    private final ResidentRepository residentRepository;
    private final FinancialTransactionRepository transactionRepository;

    public ReportService(ResidentRepository residentRepository, FinancialTransactionRepository transactionRepository) {
        this.residentRepository = residentRepository;
        this.transactionRepository = transactionRepository;
    }

    public byte[] generateResidentCensusPdf() {
        List<Resident> residents = residentRepository.findAll();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, out);

        document.open();
        
        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
        Paragraph title = new Paragraph("Censo de Residentes - CarePulse ERP", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        title.setSpacingAfter(20);
        document.add(title);

        PdfPTable table = new PdfPTable(4);
        table.setWidthPercentage(100);
        table.setWidths(new float[]{2f, 4f, 2f, 2f});

        addTableHeader(table, "Código");
        addTableHeader(table, "Nombre Completo");
        addTableHeader(table, "Género");
        addTableHeader(table, "Estado");

        for (Resident r : residents) {
            table.addCell(r.residentCode());
            table.addCell(r.firstName() + " " + r.lastName());
            table.addCell(r.gender().toString());
            table.addCell(r.status().toString());
        }

        document.add(table);
        document.close();

        return out.toByteArray();
    }

    public byte[] generateMonthlyFinancialPdf() {
        LocalDate now = LocalDate.now();
        List<FinancialTransaction> allTransactions = transactionRepository.findAll();
        
        List<FinancialTransaction> monthlyTransactions = allTransactions.stream()
                .filter(t -> t.transactionDate().getMonth() == now.getMonth() && t.transactionDate().getYear() == now.getYear())
                .toList();

        BigDecimal totalIncome = monthlyTransactions.stream()
                .filter(t -> t.transactionType() == TransactionType.INCOME)
                .map(FinancialTransaction::amount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalExpense = monthlyTransactions.stream()
                .filter(t -> t.transactionType() == TransactionType.EXPENSE)
                .map(FinancialTransaction::amount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, out);

        document.open();
        
        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
        String monthName = now.getMonth().getDisplayName(TextStyle.FULL, new Locale("es", "ES"));
        Paragraph title = new Paragraph("Resumen Financiero - " + monthName + " " + now.getYear(), titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        title.setSpacingAfter(20);
        document.add(title);

        // Summary Info
        document.add(new Paragraph("Total Ingresos: $" + totalIncome));
        document.add(new Paragraph("Total Egresos: $" + totalExpense));
        document.add(new Paragraph("Balance Neto: $" + totalIncome.subtract(totalExpense)));
        document.add(new Paragraph(" "));

        PdfPTable table = new PdfPTable(4);
        table.setWidthPercentage(100);
        table.setWidths(new float[]{2f, 4f, 2f, 2f});

        addTableHeader(table, "Fecha");
        addTableHeader(table, "Descripción");
        addTableHeader(table, "Tipo");
        addTableHeader(table, "Monto");

        for (FinancialTransaction t : monthlyTransactions) {
            table.addCell(t.transactionDate().toString());
            table.addCell(t.description() != null ? t.description() : t.category().toString());
            table.addCell(t.transactionType().toString());
            table.addCell("$" + t.amount().toString());
        }

        document.add(table);
        document.close();

        return out.toByteArray();
    }

    public byte[] generateResidentCensusExcel() throws IOException {
        List<Resident> residents = residentRepository.findAll();
        
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Residentes");

            Row headerRow = sheet.createRow(0);
            String[] headers = {"ID", "Código", "Nombre", "Apellido", "Género", "Estado"};
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                CellStyle style = workbook.createCellStyle();
                org.apache.poi.ss.usermodel.Font font = workbook.createFont();
                font.setBold(true);
                style.setFont(font);
                cell.setCellStyle(style);
            }

            int rowIdx = 1;
            for (Resident r : residents) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(r.id().toString());
                row.createCell(1).setCellValue(r.residentCode());
                row.createCell(2).setCellValue(r.firstName());
                row.createCell(3).setCellValue(r.lastName());
                row.createCell(4).setCellValue(r.gender().toString());
                row.createCell(5).setCellValue(r.status().toString());
            }

            workbook.write(out);
            return out.toByteArray();
        }
    }

    private void addTableHeader(PdfPTable table, String headerTitle) {
        PdfPCell header = new PdfPCell();
        header.setBackgroundColor(java.awt.Color.LIGHT_GRAY);
        header.setBorderWidth(1);
        header.setPhrase(new Phrase(headerTitle));
        header.setPadding(5);
        table.addCell(header);
    }
}
