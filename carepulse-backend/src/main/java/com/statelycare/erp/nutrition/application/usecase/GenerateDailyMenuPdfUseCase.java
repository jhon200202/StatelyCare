package com.statelycare.erp.nutrition.application.usecase;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.stereotype.Service;

import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.statelycare.erp.nutrition.application.dto.DailyMenuItemResponse;
import com.statelycare.erp.nutrition.domain.model.MealType;
import com.statelycare.erp.nutrition.domain.repository.DailyMenuRepository;

@Service
public class GenerateDailyMenuPdfUseCase {

    private final DailyMenuRepository repository;

    public GenerateDailyMenuPdfUseCase(DailyMenuRepository repository) {
        this.repository = repository;
    }

    public byte[] execute(String dateStr) {
        LocalDate date = LocalDate.parse(dateStr);
        List<DailyMenuItemResponse> dailyMenus = repository.findByMenuDate(date)
            .stream()
            .map(this::mapToResponse)
            .toList();

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Document document = new Document();
        PdfWriter.getInstance(document, out);

        document.open();

        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
        Font subtitleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);
        Font normalFont = FontFactory.getFont(FontFactory.HELVETICA, 10);
        Font smallFont = FontFactory.getFont(FontFactory.HELVETICA, 8);

        Paragraph title = new Paragraph("PLAN DE MENÚ DEL DÍA", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);

        Paragraph datePara = new Paragraph(
            "Fecha: " + date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
            subtitleFont
        );
        datePara.setAlignment(Element.ALIGN_CENTER);
        datePara.setSpacingAfter(20);
        document.add(datePara);

        MealType[] mealTypes = { MealType.BREAKFAST, MealType.LUNCH, MealType.SNACK, MealType.DINNER };
        String[] mealLabels = { "DESAYUNO", "ALMUERZO", "MERIENDA", "CENA" };

        for (int i = 0; i < mealTypes.length; i++) {
            final MealType currentType = mealTypes[i];
            List<DailyMenuItemResponse> items = dailyMenus.stream()
                .filter(m -> m.mealType().equals(currentType.name()))
                .toList();

            Paragraph mealTitle = new Paragraph(mealLabels[i], subtitleFont);
            mealTitle.setSpacingBefore(15);
            mealTitle.setSpacingAfter(5);
            document.add(mealTitle);

            if (items.isEmpty()) {
                document.add(new Paragraph("(Sin planificación)", normalFont));
            } else {
                for (DailyMenuItemResponse item : items) {
                    PdfPTable table = new PdfPTable(2);
                    table.setWidthPercentage(100);
                    table.setWidths(new float[]{1, 3});

                    addCell(table, "Plato:", normalFont);
                    addCell(table, getMenuItemName(item.menuItemId(), date), normalFont);

                    addCell(table, "Porciones:", normalFont);
                    addCell(table, String.valueOf(item.servingsPlanned()), normalFont);

                    if (item.residentId() != null && !item.residentId().isEmpty()) {
                        addCell(table, "Residente ID:", normalFont);
                        addCell(table, item.residentId(), normalFont);
                    }

                    if (item.notes() != null && !item.notes().isEmpty()) {
                        addCell(table, "Notas:", normalFont);
                        addCell(table, item.notes(), normalFont);
                    }

                    document.add(table);
                    document.add(new Paragraph(" "));
                }
            }
        }

        document.add(new Paragraph(" "));
        Paragraph footer = new Paragraph(
            "Generado por CarePulse ERP\nFecha de impresión: " + LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
            smallFont
        );
        footer.setAlignment(Element.ALIGN_CENTER);
        document.add(footer);

        document.close();

        return out.toByteArray();
    }

    private void addCell(PdfPTable table, String text, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setBorder(PdfPCell.NO_BORDER);
        cell.setPadding(3);
        table.addCell(cell);
    }

    private String getMenuItemName(String menuItemId, LocalDate date) {
        return "Plato ID: " + menuItemId;
    }

    private DailyMenuItemResponse mapToResponse(com.statelycare.erp.nutrition.domain.model.DailyMenu m) {
        return new DailyMenuItemResponse(
            m.id().toString(),
            m.menuDate() != null ? m.menuDate().toString() : "",
            m.mealType().toString(),
            m.menuItemId().toString(),
            m.servingsPlanned(),
            m.notes() != null ? m.notes() : "",
            m.residentId() != null ? m.residentId().toString() : null
        );
    }
}