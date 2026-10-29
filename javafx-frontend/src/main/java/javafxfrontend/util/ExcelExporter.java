package javafxfrontend.util;

import javafxfrontend.dto.AnnouncementResponseDto;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

public class ExcelExporter {

    public static void exportToExcel(List<AnnouncementResponseDto> data, File file) throws Exception {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Заявки");

            String[] headers = {"ID", "Название", "Категория", "Статус", "Дата", "Комментарий"};
            CellStyle headerStyle = createHeaderStyle(workbook);
            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            int rowIdx = 1;
            for (AnnouncementResponseDto item : data) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(item.id() != null ? item.id() : 0);
                row.createCell(1).setCellValue(nullSafe(item.title()));
                row.createCell(2).setCellValue(nullSafe(item.category()));
                row.createCell(3).setCellValue(nullSafe(item.status()));
                row.createCell(4).setCellValue(nullSafe(item.createdAt()));
                row.createCell(5).setCellValue(nullSafe(item.comment()));
            }

            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            try (FileOutputStream out = new FileOutputStream(file)) {
                workbook.write(out);
            }
        }
    }

    private static CellStyle createHeaderStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);
        style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        return style;
    }

    private static String nullSafe(String s) {
        return s == null ? "" : s;
    }
}