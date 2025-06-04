package com.example.demo.Services;

import com.example.demo.Entity.ReporteEntity;
import com.example.demo.Repository.ReporteRepository;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Stream;

@Service
public class ReportesService {
    private final ReporteRepository reporteRepository;

    public ReportesService(ReporteRepository reporteRepository) {
        this.reporteRepository = reporteRepository;
    }

    private void processReservationsForMonthlyIncome(List<ReporteEntity> reservas,
                                                     int reservaTipo,
                                                     Map<YearMonth, double[]> monthlyIncomeData) {
        int typeIndex;
        switch (reservaTipo) {
            case 1:
                typeIndex = 0;
                break;
            case 2:
                typeIndex = 1;
                break;
            case 3:
                typeIndex = 2;
                break;
            default:
                return; // Unknown type
        }

        for (ReporteEntity reserva : reservas) {
            if (reserva.getFechaInicio() != null) {
                YearMonth ym = YearMonth.from(reserva.getFechaInicio().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
                monthlyIncomeData.putIfAbsent(ym, new double[3]); // 3 types: MINUTOS10, MINUTOS15, MINUTOS20
                monthlyIncomeData.get(ym)[typeIndex] += reserva.getTotal();
            }
        }
    }
    public byte[] generarReporteVueltas(Date inicio, Date fin) {
        List<ReporteEntity> reservas10 = reporteRepository.findByTipoReservaAndFechaInicioBetween(1, inicio, fin);
        List<ReporteEntity> reservas20 = reporteRepository.findByTipoReservaAndFechaInicioBetween(2, inicio, fin);
        List<ReporteEntity> reservas30 = reporteRepository.findByTipoReservaAndFechaInicioBetween(3, inicio, fin);

        Map<YearMonth, double[]> monthlyIncome = new TreeMap<>(); // TreeMap to keep months sorted

        processReservationsForMonthlyIncome(reservas10, 1, monthlyIncome);
        processReservationsForMonthlyIncome(reservas20, 2, monthlyIncome);
        processReservationsForMonthlyIncome(reservas30, 3, monthlyIncome);

        try {
            Document document = new Document(PageSize.A4.rotate()); // Use landscape for wider tables
            ByteArrayOutputStream pdfOutputStream = new ByteArrayOutputStream();
            PdfWriter.getInstance(document, pdfOutputStream);

            document.open();

            // Title
            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, BaseColor.BLACK);
            Paragraph title = new Paragraph("Reporte de Ingresos Mensuales por Tipo de Reserva", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            document.add(Chunk.NEWLINE);

            // Table: Mes/Año | Ingresos MINUTOS10 | Ingresos MINUTOS15 | Ingresos MINUTOS20 | Total por Mes
            PdfPTable table = new PdfPTable(5);
            table.setWidthPercentage(100);
            table.setSpacingBefore(10f);
            table.setSpacingAfter(10f);
            table.setWidths(new float[]{2.5f, 2f, 2f, 2f, 2f}); // Relative column widths

            // Table Headers
            Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11, BaseColor.WHITE);
            Stream.of("Mes/Año",
                            "Ingresos reservas 10 minutos o 10 vueltas",
                            "Ingresos reservas 15 minutos o 15 vueltas",
                            "Ingresos reservas 20 minutos o 20 vueltas",
                            "Total por Mes")
                    .forEach(headerTitle -> {
                        PdfPCell headerCell = new PdfPCell(new Phrase(headerTitle, headerFont));
                        headerCell.setBackgroundColor(new BaseColor(79, 129, 189)); // A blueish color
                        headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                        headerCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                        headerCell.setPadding(5);
                        table.addCell(headerCell);
                    });

            // Table Data
            DateTimeFormatter monthFormatter = DateTimeFormatter.ofPattern("MMMM yyyy");
            Font cellFont = FontFactory.getFont(FontFactory.HELVETICA, 10, BaseColor.BLACK);
            Font boldCellFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, BaseColor.BLACK);

            double[] grandTotalsByType = new double[3]; // Totals for MINUTOS10, MINUTOS20, MINUTOS30
            double overallGrandTotal = 0;

            if (monthlyIncome.isEmpty()) {
                PdfPCell noDataCell = new PdfPCell(new Phrase("No hay datos disponibles para el período seleccionado.", cellFont));
                noDataCell.setColspan(5); // Span all 5 columns
                noDataCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                noDataCell.setPadding(10);
                table.addCell(noDataCell);
            } else {
                for (Map.Entry<YearMonth, double[]> entry : monthlyIncome.entrySet()) {
                    // Month/Year Cell
                    table.addCell(createCell(entry.getKey().format(monthFormatter), cellFont, Element.ALIGN_LEFT));

                    double monthlyTotalForThisRow = 0;
                    double[] incomesThisMonth = entry.getValue();

                    // Income Cells for each type
                    for (int i = 0; i < 3; i++) {
                        table.addCell(createCell(String.format("%.2f", incomesThisMonth[i]), cellFont, Element.ALIGN_RIGHT));
                        monthlyTotalForThisRow += incomesThisMonth[i];
                        grandTotalsByType[i] += incomesThisMonth[i];
                    }

                    // Total por Mes Cell
                    table.addCell(createCell(String.format("%.2f", monthlyTotalForThisRow), boldCellFont, Element.ALIGN_RIGHT));
                    overallGrandTotal += monthlyTotalForThisRow;
                }
            }

            // Footer Row for Totals
            PdfPCell totalLabelCell = createCell("Total General por Tipo:", boldCellFont, Element.ALIGN_RIGHT);
            totalLabelCell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            totalLabelCell.setPadding(5);
            table.addCell(totalLabelCell);

            for (int i = 0; i < 3; i++) {
                PdfPCell typeTotalCell = createCell(String.format("%.2f", grandTotalsByType[i]), boldCellFont, Element.ALIGN_RIGHT);
                typeTotalCell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                typeTotalCell.setPadding(5);
                table.addCell(typeTotalCell);
            }

            PdfPCell overallGrandTotalCell = createCell(String.format("%.2f", overallGrandTotal), boldCellFont, Element.ALIGN_RIGHT);
            overallGrandTotalCell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            overallGrandTotalCell.setPadding(5);
            table.addCell(overallGrandTotalCell);


            document.add(table);
            document.close();
            return pdfOutputStream.toByteArray();
        } catch (DocumentException e) {
            // Log error e.g., using a logger
            throw new RuntimeException("Error generando el PDF: " + e.getMessage(), e);
        }
    }
    private int getCategoryIndexForAsistencia(int cantidadPersonas) {
        if (cantidadPersonas >= 1 && cantidadPersonas <= 2) return 0; // Cat 1: 1-2
        if (cantidadPersonas >= 3 && cantidadPersonas <= 5) return 1; // Cat 2: 3-5
        if (cantidadPersonas >= 6 && cantidadPersonas <= 10) return 2; // Cat 3: 6-10
        if (cantidadPersonas >= 11 && cantidadPersonas <= 15) return 3; // Cat 4: 11-15
        return -1; // Not in any specified category
    }

    public byte[] generarReporteIngresosPorAsistencia(Date inicio, Date fin) {
        List<ReporteEntity> todasLasReservas = reporteRepository.findByfechaInicioBetween(inicio, fin);
        final int NUM_CATEGORIES = 4;
        // monthlyIncomeData: Key=YearMonth, Value=double array of size NUM_CATEGORIES
        // Each element in double array stores income for that category in that month
        Map<YearMonth, double[]> monthlyIncomeData = new TreeMap<>();
        double[] grandTotalsByCategory = new double[NUM_CATEGORIES]; // To store total income for each category

        for (ReporteEntity reserva : todasLasReservas) {
            if (reserva.getFechaInicio() != null) {
                int categoryIndex = getCategoryIndexForAsistencia(reserva.getCantidadPersonas());
                if (categoryIndex != -1) { // Process only if it falls into one of our categories
                    YearMonth ym = YearMonth.from(reserva.getFechaInicio().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
                    monthlyIncomeData.putIfAbsent(ym, new double[NUM_CATEGORIES]);
                    monthlyIncomeData.get(ym)[categoryIndex] += reserva.getTotal();
                    grandTotalsByCategory[categoryIndex] += reserva.getTotal();
                }
            }
        }

        try {
            Document document = new Document(PageSize.A4.rotate()); // Landscape for wider table
            ByteArrayOutputStream pdfOutputStream = new ByteArrayOutputStream();
            PdfWriter.getInstance(document, pdfOutputStream);
            document.open();

            // Title
            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16, BaseColor.BLACK);
            Paragraph title = new Paragraph("Reporte de Ingresos por Cantidad de Asistentes", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            document.add(Chunk.NEWLINE);

            // Table
            // Columns: Mes/Año | Cat 1 (1-2p) | Cat 2 (3-5p) | Cat 3 (6-10p) | Cat 4 (11-15p) | Total Mes
            PdfPTable table = new PdfPTable(NUM_CATEGORIES + 2); // Month + Categories + Monthly Total
            table.setWidthPercentage(100);
            table.setSpacingBefore(10f);
            table.setSpacingAfter(10f);

            // Table Headers
            Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, BaseColor.WHITE);
            String[] headers = {"Mes/Año", "1-2 Personas", "3-5 Personas", "6-10 Personas", "11-15 Personas", "Total por Mes"};
            Stream.of(headers).forEach(headerTitle -> {
                PdfPCell headerCell = new PdfPCell(new Phrase(headerTitle, headerFont));
                headerCell.setBackgroundColor(new BaseColor(0, 102, 153)); // Darker blue
                headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                headerCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                headerCell.setPadding(5);
                table.addCell(headerCell);
            });

            // Table Data
            DateTimeFormatter monthFormatter = DateTimeFormatter.ofPattern("MMMM yyyy");
            Font cellFont = FontFactory.getFont(FontFactory.HELVETICA, 9);
            double overallGrandTotal = 0;

            if (monthlyIncomeData.isEmpty()) {
                PdfPCell noDataCell = new PdfPCell(new Phrase("No hay datos de ingresos para el período y categorías seleccionadas.", cellFont));
                noDataCell.setColspan(NUM_CATEGORIES + 2);
                noDataCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                noDataCell.setPadding(10);
                table.addCell(noDataCell);
            } else {
                for (Map.Entry<YearMonth, double[]> entry : monthlyIncomeData.entrySet()) {
                    table.addCell(createCell(entry.getKey().format(monthFormatter), cellFont, Element.ALIGN_LEFT));

                    double monthlyTotal = 0;
                    for (int i = 0; i < NUM_CATEGORIES; i++) {
                        double categoryIncome = entry.getValue()[i];
                        table.addCell(createCell(String.format("%.2f", categoryIncome), cellFont, Element.ALIGN_RIGHT));
                        monthlyTotal += categoryIncome;
                    }
                    table.addCell(createCell(String.format("%.2f", monthlyTotal), cellFont, Element.ALIGN_RIGHT, Font.BOLD));
                    overallGrandTotal += monthlyTotal;
                }
            }

            // Footer Row for Totals
            Font boldCellFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 9);
            PdfPCell totalGeneralHeaderCell = createCell("Total General por Categoría:", boldCellFont, Element.ALIGN_RIGHT);
            totalGeneralHeaderCell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            table.addCell(totalGeneralHeaderCell);

            for (int i = 0; i < NUM_CATEGORIES; i++) {
                PdfPCell categoryTotalCell = createCell(String.format("%.2f", grandTotalsByCategory[i]), boldCellFont, Element.ALIGN_RIGHT);
                categoryTotalCell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                table.addCell(categoryTotalCell);
            }
            PdfPCell grandTotalCell = createCell(String.format("%.2f", overallGrandTotal), boldCellFont, Element.ALIGN_RIGHT);
            grandTotalCell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            table.addCell(grandTotalCell);


            document.add(table);
            document.close();
            return pdfOutputStream.toByteArray();

        } catch (DocumentException e) {
            // Log error appropriately
            throw new RuntimeException("Error generando el PDF de ingresos por asistencia: " + e.getMessage(), e);
        }
    }






    private PdfPCell createCell(String content, Font font, int horizontalAlignment) {
        return createCell(content, font, horizontalAlignment, font.getStyle());
    }
    private PdfPCell createCell(String content, Font font, int horizontalAlignment, int style) {
        Font styledFont = new Font(font.getBaseFont(), font.getSize(), style, font.getColor());
        PdfPCell cell = new PdfPCell(new Phrase(content, styledFont));
        cell.setHorizontalAlignment(horizontalAlignment);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setPadding(4);
        return cell;
    }
}
