package util;

import model.Announcement;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

public final class ExcelExporter {

    private static final DateTimeFormatter DATE_FMT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private ExcelExporter() { }

    public static void writeAnnouncements(List<Announcement> announcements, String filePath)
            throws IOException {

        File file = new File(filePath);

        // Создаём папку, если её нет
        File parent = file.getParentFile();
        if (parent != null && !parent.exists()) {
            boolean created = parent.mkdirs();
            if (!created) {
                throw new IOException("Не удалось создать папку: " + parent.getAbsolutePath());
            }
        }

        // Создаём новый файл (если есть — не трогаем, FileOutputStream перезапишет)
        if (!file.exists()) {
            boolean created = file.createNewFile();
            if (!created) {
                throw new IOException("Не удалось создать файл: " + file.getAbsolutePath());
            }
        }

        // Дальше — как раньше: пишем xlsx
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Заявки");

            String[] headers = {
                    "ID", "Статус", "Категория", "Заголовок", "Описание",
                    "Автор (ID)", "Ответственный (ID)",
                    "Создана", "Обновлена", "Комментарий"
            };

            CellStyle headerStyle = createHeaderStyle(workbook);
            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            int rowIdx = 1;
            for (Announcement a : announcements) {
                Row row = sheet.createRow(rowIdx++);

                row.createCell(0).setCellValue(a.id());
                row.createCell(1).setCellValue(String.valueOf(a.status()));
                row.createCell(2).setCellValue(a.category());
                row.createCell(3).setCellValue(a.title());
                row.createCell(4).setCellValue(nullSafe(a.description()));

                row.createCell(5).setCellValue(a.userId());
                row.createCell(6).setCellValue(a.employeeId() != null ? a.employeeId() : 0);

                row.createCell(7).setCellValue(
                        a.createdAt() != null ? a.createdAt().format(DATE_FMT) : "");
                row.createCell(8).setCellValue(
                        a.updatedAt() != null ? a.updatedAt().format(DATE_FMT) : "");
                row.createCell(9).setCellValue(nullSafe(a.comment()));
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