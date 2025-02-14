package csit.semit.createliststudystreams;

import csit.semit.createliststudystreams.entity.AcademicGroup;
import csit.semit.createliststudystreams.excelutils.ExcelDataProcessing;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static csit.semit.createliststudystreams.excelutils.ExcelDataProcessing.processCell;

public class ExcelDataProcessingTest {

    @Test
    void testCreateExcel() throws IOException, InvalidFormatException {
        String fileWithVUDName = "Test_VUD.xls";
        // Читається вихідний файл для тесту з каталога ресурсів тесту src/test/resources
        InputStream resourceStream = getClass().getClassLoader().getResourceAsStream(fileWithVUDName);
        if (resourceStream == null) {
            throw new FileNotFoundException("Файл не найден: " + fileWithVUDName);
        }
        //Є такий файл, можна зчитувати
        //Если вам действительно нужен FileInputStream, то сначала скопируйте ресурс во временный файл:
        File tempFile = null;
        try {
            tempFile = File.createTempFile("temp_vud", ".xls");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        tempFile.deleteOnExit();
//        Почему нельзя сразу сделать FileInputStream?
//                getResourceAsStream() возвращает поток данных из JAR-файла, который может быть внутри ZIP-архива или вовсе не быть обычным файлом в файловой системе. Поэтому FileInputStream на него создать невозможно.
//
//        Только после копирования данных во временный файл можно использовать FileInputStream.
        try (FileOutputStream fos = new FileOutputStream(tempFile)) {
            byte[] buffer = new byte[1024];
            int length;
            while ((length = resourceStream.read(buffer)) != -1) {
                fos.write(buffer, 0, length);
            }
        }
        //Початок створення Excel
        FileInputStream fis = new FileInputStream(tempFile);
        Workbook wb = new HSSFWorkbook(fis);
        var sheetIterator = wb.sheetIterator();
        int list = 1;
        while (sheetIterator.hasNext()) {
            Sheet sheet = sheetIterator.next();
            if (list == 2) {
                System.out.println("Autumn");
                var data = new HashMap<Integer, List<Object>>();
                var iterator = sheet.rowIterator();
                int i = 0;
                for (var rowIndex = 0; iterator.hasNext(); rowIndex++) {
                    var row = iterator.next();
                    if (i > 6) {
                        data.put(rowIndex, new ArrayList<>());
                        if (row.getCell(0).toString().isEmpty()) break;
                        for (var cell : row) {
                            processCell(cell, data.get(rowIndex));
                        }
                    }
                    i++;
                }
                list++;
                for (Map.Entry<Integer, List<Object>> entry : data.entrySet()) {
                    List<Object> value = entry.getValue();
                    if (value.isEmpty()) break;
                    System.out.println(value);
                }
            } else {
                System.out.println("Spring");
                var data = new HashMap<Integer, List<Object>>();
                var iterator = sheet.rowIterator();
                int i = 0;
                for (var rowIndex = 0; iterator.hasNext(); rowIndex++) {
                    var row = iterator.next();
                    if (i > 6) {
                        data.put(rowIndex, new ArrayList<>());
                        if (row.getCell(0).toString().isEmpty()) break;
                        for (var cell : row) {
                            processCell(cell, data.get(rowIndex));
                        }
                    }
                    i++;
                }
                list++;
                for (Map.Entry<Integer, List<Object>> entry : data.entrySet()) {
                    List<Object> value = entry.getValue();
                    if (value.isEmpty()) break;
                    System.out.println(value);
                }
            }
        }
        fis.close();

        String templateRozpodilFileName = "studyload_rozpodil_template.xlsx";
        // Читається вихідний файл для тесту з каталога ресурсів тесту src/test/resources
        resourceStream = getClass().getClassLoader().getResourceAsStream(templateRozpodilFileName);
        if (resourceStream == null) {
            throw new FileNotFoundException("Файл не найден: " + fileWithVUDName);
        }
        //Є такий файл, можна зчитувати
        //Если вам действительно нужен FileInputStream, то сначала скопируйте ресурс во временный файл:
        tempFile = null;
        try {
            tempFile = File.createTempFile("temp_vud", ".xls");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        tempFile.deleteOnExit();

        try (FileOutputStream fos = new FileOutputStream(tempFile)) {
            byte[] buffer = new byte[1024];
            int length;
            while ((length = resourceStream.read(buffer)) != -1) {
                fos.write(buffer, 0, length);
            }
        }
        Workbook workbook = new XSSFWorkbook(tempFile);
        Sheet sheet = workbook.getSheetAt(0);
        CellStyle style = workbook.createCellStyle();
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        Row row = sheet.createRow(10);
        Cell cell = row.createCell(0);
        cell.setCellValue("Test1");
        cell.setCellStyle(style);
        cell = row.createCell(1);
        cell.setCellValue("Test2");
        cell.setCellStyle(style);
        row = sheet.createRow(11);
        cell = row.createCell(0);
        cell.setCellValue("Test3");
        cell.setCellStyle(style);
        cell = row.createCell(1);
        cell.setCellValue("Test4");
        cell.setCellStyle(style);
        LocalDateTime nowMoment = LocalDateTime.now();
        String nowMomentStr = nowMoment.format(DateTimeFormatter.ofPattern("ddMM_hhmm"));
        try {
            String fileForSaveName = "createExcel" + nowMomentStr + ".xlsx";
            FileOutputStream outputStream = new FileOutputStream(fileForSaveName);
            workbook.write(outputStream);
            System.out.println("Дані успішно записані у " + fileForSaveName);
        } finally {
            workbook.close();
        }

    }

    @Test
    void testEndGroupNum() {
        List<AcademicGroup> testList = new ArrayList<>();
        AcademicGroup ag1 = new AcademicGroup(1L, 3, "КН-424а", 15);
        AcademicGroup ag2 = new AcademicGroup(2L, 3, "КН-424б", 20);
        AcademicGroup ag3 = new AcademicGroup(3L, 3, "КН-424в", 12);
        testList.add(ag1);
        testList.add(ag2);
        testList.add(ag3);
        assertEquals(ExcelDataProcessing.findAcademicGroupInList("CS222",testList),null);
        assertEquals(ExcelDataProcessing.findAcademicGroupInList("КН-424а",testList),ag1);

    }
}
