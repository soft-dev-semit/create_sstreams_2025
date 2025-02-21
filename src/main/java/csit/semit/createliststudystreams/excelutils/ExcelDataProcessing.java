package csit.semit.createliststudystreams.excelutils;

import csit.semit.createliststudystreams.entity.*;
import csit.semit.createliststudystreams.repository.PlansAutumnRepository;
import csit.semit.createliststudystreams.repository.StreamsCoursesAutumnRepository;
import csit.semit.createliststudystreams.service.StreamsAutumnService;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.NumberToTextConverter;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class ExcelDataProcessing {

    private final StreamsAutumnService streamsAutumnService;

    @Autowired
    public ExcelDataProcessing(StreamsAutumnService streamsAutumnService) {
        this.streamsAutumnService = streamsAutumnService;
    }
//
//    @Autowired
//    StreamsAutumnService streamsAutumnService;

    public static void processCell(Cell cell, List<Object> dataRow) {
        switch (cell.getCellType()) {
            case STRING:
                dataRow.add(cell.getStringCellValue());
                break;
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    dataRow.add(cell.getLocalDateTimeCellValue());
                } else {
                    dataRow.add(NumberToTextConverter.toText(cell.getNumericCellValue()));
                }
                break;
            case BOOLEAN:
                dataRow.add(cell.getBooleanCellValue());
                break;
            case FORMULA:
                dataRow.add(cell.getCellFormula());
                break;
            default:
                dataRow.add("");
        }
    }

    public static String createExcel(List<StreamsAutumn> streamsAutumn,
                                     List<AcademicGroup> academicGroups,
                                     List<StreamsSpring> streamsSpring
    ) {
        String rez = "File not created";
        String templateRozpodilFileName = "studyload_rozpodil_template.xlsx";
        FileInputStream file = null;
        try {
            file = new FileInputStream(new File(templateRozpodilFileName));

            Workbook workbook = new XSSFWorkbook(file);
            //Обчислювач для формул
//            XSSFFormulaEvaluator formulaEvaluator = (XSSFFormulaEvaluator) workbook.getCreationHelper().createFormulaEvaluator();

            Sheet sheet = workbook.getSheetAt(0);
            //Установка стилей
            CellStyle style = workbook.createCellStyle();
            CellStyle style2 = workbook.createCellStyle();
            CellStyle style3 = workbook.createCellStyle();
            style3.setBorderBottom(BorderStyle.THIN);
            style3.setBorderTop(BorderStyle.THIN);
            style3.setBorderLeft(BorderStyle.THIN);
            style3.setBorderRight(BorderStyle.THIN);
            style3.setWrapText(true);
            style3.setVerticalAlignment(VerticalAlignment.CENTER);
            style2.setBorderBottom(BorderStyle.THIN);
            style2.setBorderTop(BorderStyle.THIN);
            style2.setBorderLeft(BorderStyle.THIN);
            style2.setBorderRight(BorderStyle.THIN);
            style2.setWrapText(true);
            style2.setVerticalAlignment(VerticalAlignment.CENTER);
            style2.setAlignment(HorizontalAlignment.CENTER);
            style.setBorderBottom(BorderStyle.THIN);
            style.setBorderTop(BorderStyle.THIN);
            style.setBorderLeft(BorderStyle.THIN);
            style.setBorderRight(BorderStyle.THIN);
            style.setVerticalAlignment(VerticalAlignment.CENTER);
            style.setAlignment(HorizontalAlignment.CENTER);

            //AA =========== AUTUMN ==============================
//            Iterable<StreamsAutumn> streamsAutumn = streamsAutumnRepository.findAll();
            System.out.println("\n=== Старт формування потоків ===");
            System.out.println("1. Формуються групи осіннього семестру ....");
            int numRow = 10;
            int n = 1;
            for (StreamsAutumn autumn : streamsAutumn) {
//                System.out.println(autumn.getNameStream());
                //Для груп потоку порахувати кількість студентів
                //Stream API?
                int amountStudents = 0;
                int kgroup = 0;
                String[] groups = autumn.getNameGroups().split(" ");
                for (String group : groups) {
//                    System.out.println(group);
//                    AcademicGroup groupCheck = acedemicGroups.findByGroupNameIsEndingWith(group);
                    AcademicGroup groupCheck = findAcademicGroupInList(group, academicGroups);
                    if (groupCheck != null) {
                        amountStudents += groupCheck.getAmountStudents();
                    }
                    kgroup++;
                }
//                Set<StreamsCoursesAutumn> streamsCoursesAutumnsSet = autumn.getStreamsCoursesAutumns();
//                Iterator<StreamsCoursesAutumn> iterator = streamsCoursesAutumnsSet.iterator();
//                while (iterator.hasNext()) {
                SortedSet<StreamsCoursesAutumn> streamsCoursesAutumnsSet = autumn.getStreamsCoursesAutumns();
                for (StreamsCoursesAutumn streamsCoursesAutumn : streamsCoursesAutumnsSet) {
//                    System.out.println(n+": "+streamsCoursesAutumn.getCourseName()+" "+autumn.getNameStream());
                    Row row = sheet.createRow(numRow);
                    Cell cell = row.createCell(0);
                    cell.setCellValue(n);
                    cell.setCellStyle(style3);
                    cell = row.createCell(1);
                    cell.setCellValue(streamsCoursesAutumn.getCourseName());
                    cell.setCellStyle(style3);
                    cell = row.createCell(2);
                    cell.setCellValue("СП");
                    cell.setCellStyle(style);
                    cell = row.createCell(3);
                    cell.setCellValue(streamsCoursesAutumn.getCourse());
                    cell.setCellStyle(style);
                    cell = row.createCell(4);
                    cell.setCellValue(amountStudents);
                    cell.setCellStyle(style);
                    cell = row.createCell(5);
                    cell.setCellValue(autumn.getNameGroups().replaceAll(" ", " + "));
                    cell.setCellStyle(style2);
                    cell = row.createCell(6);
                    cell.setCellValue(1);
                    cell.setCellStyle(style);
                    cell = row.createCell(7);
                    cell.setCellValue(kgroup);
                    cell.setCellStyle(style);
                    cell = row.createCell(8);
                    cell.setCellValue(streamsCoursesAutumn.getEcts());
                    cell.setCellStyle(style);
                    cell = row.createCell(9);
                    cell.setCellValue(streamsCoursesAutumn.getHoursLection());
                    cell.setCellStyle(style);
                    cell = row.createCell(10);
                    cell.setCellValue(streamsCoursesAutumn.getHoursLab());
                    cell.setCellStyle(style);
                    cell = row.createCell(11);
                    cell.setCellValue(streamsCoursesAutumn.getHoursPrak());
                    cell.setCellStyle(style);
                    //Якщо "інші форми" не пуста, то заповнюється.
                    //Якщо це КР, то в наступну колонку пишеться або 2, або 3.
                    //2 - КР на 1-2 курсі, 3 - на інших
                    cell = row.createCell(12);
                    cell.setCellValue(streamsCoursesAutumn.getIndZadanie());
                    cell.setCellStyle(style);
                    cell = row.createCell(13);
                    String anForms = streamsCoursesAutumn.getIndZadanie();
                    int hourKR = 0;
                    if (anForms != null) {
                        if (anForms.equals("КР")) {
                            hourKR = streamsCoursesAutumn.getCourse() < 3 ? 2 : 3;
                        }
                    }
                    cell.setCellValue(hourKR);
                    cell.setCellStyle(style);
                    cell = row.createCell(14);
                    cell.setCellValue("");
                    cell.setCellStyle(style);
                    cell = row.createCell(15);
                    if (streamsCoursesAutumn.getHoursLection() > 0) {
                        cell.setCellValue(streamsCoursesAutumn.getZalik());
                    } else {
                        cell.setCellValue("");
                    }
                    cell.setCellStyle(style);
                    cell = row.createCell(16);
                    if (streamsCoursesAutumn.getHoursLection() > 0) {
                        cell.setCellValue(streamsCoursesAutumn.getExam());
                    } else {
                        cell.setCellValue("");
                    }
                    cell.setCellStyle(style);
                    //Комірки з формулами для розрахунку навантаження
                    //17 - кол-во лекцій: =J11
                    cell = row.createCell(17);
                    cell.setCellFormula("J" + (numRow + 1));
                    cell.setCellStyle(style);
                    //18 - кол-во часов консультаций лекцій:
                    //=ЕСЛИ(ИЛИ(Q11="Е",Q11="ДЕК"),2*H11,0)
                    cell = row.createCell(18);
                    cell.setCellFormula("IF(OR(Q" + (numRow + 1) + "=\"Е\",Q" + (numRow + 1) + "=\"ДЕК\"),2*H" + (numRow + 1) + ",0)");
                    cell.setCellStyle(style);
                    //19 - кол-во ЛЗ: =K11*H11
                    cell = row.createCell(19);
                    cell.setCellFormula("K" + (numRow + 1) + "*" + "H" + (numRow + 1));
                    cell.setCellStyle(style);
                    //20 - кол-во Прак: =L12*H12
                    cell = row.createCell(20);
                    cell.setCellFormula("L" + (numRow + 1) + "*" + "H" + (numRow + 1));
                    cell.setCellStyle(style);
                    //21 Перевірка Р,РЕ  =ОКРУГЛ(ЕСЛИ(ИЛИ(M13="Р",M13="РЕ",M13="РГ" ),0.5*E13,0),0)
                    //=ОКРУГЛ(ЕСЛИ(ИЛИ(M113="Р",M113="РЕ",M113="РГ" ),0.5*E113,0),0)
                    cell = row.createCell(21);
//                    cell.setCellFormula("ROUND(IF(OR(M"+(numRow+1)+"=\"Р\",M"+(numRow+1)+"=\"РЕ\"),0.5*E"+(numRow+1)+",0),0)");
                    cell.setCellFormula("IF(OR(M" + (numRow + 1) + "=\"Р\",M" + (numRow + 1) + "=\"РЕ\",M" + (numRow + 1) + "=\"РГ\"),0.5*E" + (numRow + 1) + ",0)");
                    cell.setCellStyle(style);
                    //22 - кол-во КР: =N74*E74
                    cell = row.createCell(22);
                    cell.setCellFormula("N" + (numRow + 1) + "*" + "E" + (numRow + 1));
                    cell.setCellStyle(style);
                    //23 - Заліки: =ЕСЛИ(P56="Залік",2*H56,0)
                    cell = row.createCell(23);
                    cell.setCellFormula("IF(P" + (numRow + 1) + "=\"З\",2*H" + (numRow + 1) + ",0)");
                    cell.setCellStyle(style);
                    //24 - Екзамени =ОКРУГЛ(ЕСЛИ(ИЛИ(Q181="Ісп",Q181="ДЕК"),0.33*E181,0),0)
                    cell = row.createCell(24);
//                    cell.setCellFormula("ROUND(IF(OR(Q"+(numRow+1)+"=\"Е\",Q"+(numRow+1)+"=\"ДЕК\"),0.33*E"+(numRow+1)+",0),0)");
                    cell.setCellFormula("IF(OR(Q" + (numRow + 1) + "=\"Е\",Q" + (numRow + 1) + "=\"ДЕК\"),0.33*E" + (numRow + 1) + ",0)");
                    cell.setCellStyle(style);
                    //Поки пусті комірки
                    for (int c = 25; c < 34; c++) {
                        cell = row.createCell(c);
                        cell.setCellValue("");
                        cell.setCellStyle(style);
                    }
                    //34 - ВСЬОГО =СУММ(R179:AH179)
                    cell = row.createCell(34);
                    cell.setCellFormula("SUM(R" + (numRow + 1) + ":AH" + (numRow + 1) + ")");
                    cell.setCellStyle(style);
                    for (int c = 35; c < 37; c++) {
                        cell = row.createCell(c);
                        cell.setCellValue("");
                        cell.setCellStyle(style);
                    }
                    System.out.println(""+n+": ряд записан");
                    n++;
                    numRow++;
                }


            }
            numRow = numRow + 2;
            sheet.addMergedRegion(new CellRangeAddress(numRow, numRow, 27, 35));
            sheet.addMergedRegion(new CellRangeAddress(numRow + 1, numRow + 1, 27, 35));
            CellStyle style4 = workbook.createCellStyle();
            style4.setAlignment(HorizontalAlignment.CENTER);
            CellStyle style5 = workbook.createCellStyle();
            style5.setBorderBottom(BorderStyle.NONE);
            style5.setBorderTop(BorderStyle.NONE);
            style5.setBorderLeft(BorderStyle.NONE);
            style5.setBorderRight(BorderStyle.NONE);
            Row row = sheet.createRow(numRow);
            Cell cell;
            for (int a = 0; a < 27; a++) {
                cell = row.createCell(a);
                cell.setCellValue("");
                cell.setCellStyle(style5);
            }
            cell = row.createCell(27);
            cell.setCellValue("Зав. кафедрою __________________ Андрій КОПП");
            cell.setCellStyle(style4);
            row = sheet.createRow(numRow + 1);
            for (int a = 0; a < 27; a++) {
                cell = row.createCell(a);
                cell.setCellValue("");
                cell.setCellStyle(style5);
            }
            cell = row.createCell(27);
            cell.setCellValue("(підпис)");
            cell.setCellStyle(style4);

            //SS =========== SPRING ==============================
            System.out.println("2. Формуються групи весняного семестру ....");
            Sheet sheet2 = workbook.getSheetAt(1);
//            Iterable<StreamsSpring> streamsSpring = streamsSpringRepository.findAll();
            int i2 = 10;
            int n2 = 1;
            for (StreamsSpring spring : streamsSpring) {
//                System.out.println(spring.getNameStream());
                int amountStudents = 0;
                int kgroup = 0;
                String[] groups = spring.getNameGroups().split(" ");
                for (String group : groups) {
//                    System.out.println(group);
//                    AcademicGroup groupCheck = acedemicGroups.findByGroupNameIsEndingWith(group);
                    AcademicGroup groupCheck = findAcademicGroupInList(group, academicGroups);
                    if (groupCheck != null) {
                        amountStudents += groupCheck.getAmountStudents();
                    }
                    kgroup++;
                }
//                Set<StreamsCoursesSpring> streamsCoursesSpringsSet = spring.getStreamsCoursesSprings();
//                Iterator<StreamsCoursesSpring> iterator = streamsCoursesSpringsSet.iterator();
//                while (iterator.hasNext()) {
                Set<StreamsCoursesSpring> streamsCoursesSpringsSet = spring.getStreamsCoursesSprings();
                for (StreamsCoursesSpring streamsCoursesSpring : streamsCoursesSpringsSet) {
//                    StreamsCoursesSpring streamsCoursesSpring = iterator.next();
                    row = sheet2.createRow(i2);
                    cell = row.createCell(0);
                    cell.setCellValue(n2);
                    cell.setCellStyle(style3);
                    cell = row.createCell(1);
                    cell.setCellValue(streamsCoursesSpring.getCourseName());
                    cell.setCellStyle(style3);
                    cell = row.createCell(2);
                    cell.setCellValue("СП");
                    cell.setCellStyle(style);
                    cell = row.createCell(3);
                    cell.setCellValue(streamsCoursesSpring.getCourse());
                    cell.setCellStyle(style);
                    cell = row.createCell(4);
                    cell.setCellValue(amountStudents);
                    cell.setCellStyle(style);
                    cell = row.createCell(5);
                    cell.setCellValue(spring.getNameGroups().replaceAll(" ", " + "));
                    cell.setCellStyle(style2);
                    cell = row.createCell(6);
                    cell.setCellValue(1);
                    cell.setCellStyle(style);
                    cell = row.createCell(7);
                    cell.setCellValue(kgroup);
                    cell.setCellStyle(style);
                    cell = row.createCell(8);
                    cell.setCellValue(streamsCoursesSpring.getEcts());
                    cell.setCellStyle(style);
                    cell = row.createCell(9);
                    cell.setCellValue(streamsCoursesSpring.getHoursLection());
                    cell.setCellStyle(style);
                    cell = row.createCell(10);
                    cell.setCellValue(streamsCoursesSpring.getHoursLab());
                    cell.setCellStyle(style);
                    cell = row.createCell(11);
                    cell.setCellValue(streamsCoursesSpring.getHoursPrak());
                    cell.setCellStyle(style);
                    //Якщо "інші форми" не пуста, то заповнюється.
                    //Якщо це КР, то в наступну колонку пишеться або 2, або 3.
                    //2 - КР на 1-2 курсі, 3 - на інших
                    cell = row.createCell(12);
                    cell.setCellValue(streamsCoursesSpring.getIndZadanie());
                    cell.setCellStyle(style);
                    cell = row.createCell(13);
                    String anForms = streamsCoursesSpring.getIndZadanie();
                    int hourKR = 0;
                    if (anForms != null) {
                        if (anForms.equals("КР")) {
                            hourKR = streamsCoursesSpring.getCourse() < 3 ? 2 : 3;
                        }
                    }
                    cell.setCellValue(hourKR);
                    cell.setCellStyle(style);
                    cell = row.createCell(14);
                    cell.setCellValue("");
                    cell.setCellStyle(style);
                    cell = row.createCell(15);
                    cell.setCellValue(streamsCoursesSpring.getZalik());
                    cell.setCellStyle(style);
                    cell = row.createCell(16);
                    cell.setCellValue(streamsCoursesSpring.getExam());
                    cell.setCellStyle(style);
                    //
                    for (int c = 17; c < 37; c++) {
                        cell = row.createCell(c);
                        cell.setCellValue("");
                        cell.setCellStyle(style);
                    }
                }
                n2++;
                i2++;
            }
            i2 = i2 + 2;
            sheet2.addMergedRegion(new CellRangeAddress(i2, i2, 27, 35));
            sheet2.addMergedRegion(new CellRangeAddress(i2 + 1, i2 + 1, 27, 35));
            row = sheet2.createRow(i2);
            for (int a = 0; a < 27; a++) {
                cell = row.createCell(a);
                cell.setCellValue("");
                cell.setCellStyle(style5);
            }
            cell = row.createCell(27);
            cell.setCellValue("Зав. кафедрою __________________ Андрій КОПП");
            cell.setCellStyle(style4);
            row = sheet2.createRow(i2 + 1);
            for (int a = 0; a < 27; a++) {
                cell = row.createCell(a);
                cell.setCellValue("");
                cell.setCellStyle(style5);
            }
            cell = row.createCell(27);
            cell.setCellValue("(підпис)");
            cell.setCellStyle(style4);
            //========== SAVE TO FILE ===================
            LocalDateTime nowMoment = LocalDateTime.now();
            String nowMomentStr = nowMoment.format(DateTimeFormatter.ofPattern("ddMM_hhmm"));
            String fileForSaveName = "createExcel" + nowMomentStr + ".xlsx";
            FileOutputStream outputStream = new FileOutputStream(fileForSaveName);
            workbook.write(outputStream);
            workbook.close();
            rez = fileForSaveName;
        } catch (FileNotFoundException e) {
//            throw new RuntimeException(e);
            System.err.println("File not found!");
            rez = "File not found!";
        } catch (IOException e) {
//            throw new RuntimeException(e);
            System.err.println("File when file creating!");
            rez = "File when file creating!";
        }
        return rez;
    }

    public static AcademicGroup findAcademicGroupInList(String groupName, List<AcademicGroup> academicGroups) {
        AcademicGroup res = null;
        int length1 = groupName.length();
        for (AcademicGroup acGr : academicGroups) {
            int length2 = acGr.getGroupName().length();
            if (length2 < length1) {
                continue;
            }
            if (length1 == length2) {
                if (acGr.getGroupName().equals(groupName)) {
                    res = acGr;
                }
            } else {
                //Знайти кінець рядка та перевірити, чи співпадає він з шаблоном
                String endAcGr = acGr.getGroupName().trim().substring(acGr.getGroupName().trim().length() - groupName.length());
                if (endAcGr.equals(groupName)) {
                    res = acGr;
                }
            }
        }
        return res;
    }

    //Метод автоматичного визначення коду потоку при початковому створенні
    public static String defStudyStreamName(String prefix, String groups) {
        String studyStreamName = prefix;
        String[] groupsList = groups.split(", ");
        for (String group : groupsList) {
            studyStreamName = addSpecGroup(group, studyStreamName);
        }
        return studyStreamName;
    }

    private static String addSpecGroup(String group, String studyStreamName) {
        Pattern pattern = Pattern.compile("-");
        Matcher matcher = pattern.matcher(group);
        String firstElm = "";
        if (matcher.find()) {
            firstElm = group.substring(matcher.start());
        }
        char[] charGroup = firstElm.substring(1).toCharArray();
        if (charGroup[0] == '4') {
                studyStreamName += "_122";
        } else if (charGroup[0] == '2') {
            if (!studyStreamName.contains("_121")) {
                studyStreamName += "_121";
            }
        } else if (charGroup[0] == '7') {
            if (!studyStreamName.contains("_126")) {
                studyStreamName += "_126";
            }
        }

        studyStreamName += "_" + group.substring(matcher.start()).substring(1);;
        return studyStreamName;
    }

    //Метод створення рядка базового навантаження при початковому створенні
    //String prefix - який тип потоку (лекції, ЛЗ-ПЗ, КР)
    //В залежності від цього по різному записуються рядки плану
    public static StreamsCoursesAutumn createNewStudyloadRow(String prefix, PlansAutumn plan) {

        StreamsCoursesAutumn newStudyloadRow = new StreamsCoursesAutumn();

        //Встановити загальну інформацію
        newStudyloadRow.setCourseName(plan.getNameCourse());
        newStudyloadRow.setCourse(plan.getCourse());
        newStudyloadRow.setSemestr(plan.getSemestr());
        newStudyloadRow.setEcts(plan.getEcts());
        switch (prefix) {
            case "Lec":
                newStudyloadRow.setHoursLection(plan.getHoursLection());
                if (
                        (plan.getIndZadanie() != null) &&
                                (!(plan.getIndZadanie().equals("КР") || plan.getIndZadanie().equals("КП")))
                ) {
                    newStudyloadRow.setIndZadanie(plan.getIndZadanie());
                }
                newStudyloadRow.setZalik(plan.getZalik());
                newStudyloadRow.setExam(plan.getExam());
                break;
            case "Lab":
                newStudyloadRow.setHoursLab(plan.getHoursLab());
                if (plan.getHoursLection() == 0) {
                    newStudyloadRow.setZalik(plan.getZalik());
                }
                break;
            case "Prak":
                newStudyloadRow.setHoursPrak(plan.getHoursPrak());
                if (plan.getHoursLection() == 0) {
                    newStudyloadRow.setZalik(plan.getZalik());
                }
                break;
            case "KR":
                //Кроме общей информации сюда заполнять нечего
                newStudyloadRow.setIndZadanie(plan.getIndZadanie());
                break;
        }
        return newStudyloadRow;
    }


    @Autowired
    StreamsCoursesAutumnRepository streamsCoursesAutumnRepository;
    @Autowired
    PlansAutumnRepository plansAutumnRepository;

    public void createStreamsAutumn() {
        System.out.println("\nНачало створення потоків");
        System.out.println("\n* вилучення записів про потоки з БД...");
        //Очистити перелік навчальних груп
        streamsAutumnService.clearStreamsAutumn();
        //TODO убрать репозиторий
        //Очистити перелік рядків навантаження
        streamsCoursesAutumnRepository.deleteAll();
//        Iterable<PlansAutumn> plansAutumns = plansAutumnRepository.findAll();
//
//        Iterator<PlansAutumn> plans1 = plansAutumns.iterator();
//        while (plans1.hasNext()) {
//        PlansAutumn plan = plans1.next();
        System.out.println("\n* додавання потоків до БД ...");
        //Отримати перелік завантажених позицій ВУД для розбору
        List<PlansAutumn> plansToProcessing = (List<PlansAutumn>) plansAutumnRepository.findAll();
        for (PlansAutumn plan : plansToProcessing) {
//            System.out.println(plan.toString());
            //Є лекції? - може не бути, перш за все у дисциплін, орієнтованих на виконання курсової роботи або НДР
            if (plan.getHoursLection() > 0) {
                //LEC
                //Створити рядок з розподілом годин для запису для лекцій дисципліни (=лекції, консультації, Р-РГ-РЗ, екзамени/заліки
                StreamsCoursesAutumn streamsCoursesAutumnNewLec = createNewStudyloadRow("Lec", plan);
                // Створити імя лекційного потоку
                String lecStudyStreamName = ExcelDataProcessing.defStudyStreamName("Lec", plan.getGroupsNames());
                // Чи є потік із таким ім`ям в БД?
                StreamsAutumn streamsAutumnInDB = streamsAutumnService.getStreamsAutumnByName(lecStudyStreamName);
                if (streamsAutumnInDB == null) {
                    //Створити новий навчальний потік
                    StreamsAutumn streamAutumnNew = new StreamsAutumn();
                    //Привласнити сгенероване імя
                    streamAutumnNew.setNameStream(lecStudyStreamName);
                    //Привласнити перелік груп
                    //DDimaE Отримати перелік груп без пробілів // ?? Навіщо ??
                    // Убирает запятые и лишние пробелы после них
                    String groupsToAdd = plan.getGroupsNames().replaceAll(",\\s*", " ");
                    streamAutumnNew.setNameGroups(groupsToAdd);
                    //Додати дисципліну у перелік дисциплін, що викладаються потоку
                    streamAutumnNew.addStreamsCourses(streamsCoursesAutumnNewLec);
                    //Записати потік до БД
                    streamsAutumnService.createStreamsAutumn(streamAutumnNew);
                    System.out.println("LEC === " + lecStudyStreamName+" створено");
                } else {
                    //Додати дисципліну у перелік дисциплін, що викладаються потоку
                    streamsAutumnInDB.addStreamsCourses(streamsCoursesAutumnNewLec);
                    //Оновити базу
                    streamsAutumnService.updateStreamsAutumn(streamsAutumnInDB);
                    System.out.println("LEC === " + lecStudyStreamName+" додана нова дисципліна");
                }
            }

            //НА ЛАБИ та на ПЗ та на КР - цикл по всіх групах!!!!
            //TODO Якось треба додавання груп для ЛР, ПЗ, КР свести в один метод
            //LABS
            if (plan.getHoursLab() != 0) {
                String[] groupsInStream = plan.getGroupsNames().split(", ");
                //Створити рядок з розподілом годин для лабораторних занять з дисципліни (=ЛЗ и заліки, якщо немає лекцій)
                StreamsCoursesAutumn streamsCoursesAutumnNewLab = createNewStudyloadRow("Lab", plan);
                //Отримати перелік груп, в яких проводяться лаби
                String[] groupsInLabStream = plan.getGroupsNames().split(", ");
                for (String group : groupsInStream) {
                    // Створити імя потоку для проведення лаб в певній групі
                    String labStudyStreamName = "Lab";
                    labStudyStreamName = addSpecGroup(group,labStudyStreamName);
                    //Чи Є навчальний потік для лаб у БД
                    StreamsAutumn streamsAutumnLabInDB = streamsAutumnService.getStreamsAutumnByName(labStudyStreamName);
                    if (streamsAutumnLabInDB == null) {
                        //Створити новий навчальний потік для групи
                        StreamsAutumn streamsAutumnLab = new StreamsAutumn();
                        streamsAutumnLab.setNameStream(labStudyStreamName);
                        streamsAutumnLab.setNameGroups(group);
                        //Додати дисципліну у перелік дисциплін, що викладаються потоку
                        streamsAutumnLab.addStreamsCourses(streamsCoursesAutumnNewLab);
                        //Записати потік до БД
                        streamsAutumnService.createStreamsAutumn(streamsAutumnLab);
                        System.out.println("LAB === " + labStudyStreamName+" створено");
                    } else {
                        //Є навчальний потік у БД
                        //Додати дисципліну у перелік дисциплін, що викладаються потоку
                        streamsAutumnLabInDB.addStreamsCourses(streamsCoursesAutumnNewLab);
                        //Оновити базу
                        streamsAutumnService.updateStreamsAutumn(streamsAutumnLabInDB);
                    }
                    System.out.println("LAB === " + labStudyStreamName+" додана нова дисципліна");
                }
            }

            //PRAK
            if (plan.getHoursPrak() != 0) {
                String[] groupsInStream = plan.getGroupsNames().split(", ");
                //Створити рядок з розподілом годин для практичних занять з дисципліни (=ПЗ и заліки, якщо немає лекцій)
                StreamsCoursesAutumn streamsCoursesAutumnNewPrak = createNewStudyloadRow("Prak", plan);
                //Отримати перелік груп, в яких проводяться практичні заняття
                String[] groupsInPrakStream = plan.getGroupsNames().split(", ");
                for (String group : groupsInStream) {
                    // Створити імя потоку для проведення практичних занять в певній групі
                    String prakStudyStreamName = "Prak";
                    prakStudyStreamName = addSpecGroup(group,prakStudyStreamName);
                    //Чи Є навчальний потік для лаб у БД
                    StreamsAutumn streamsAutumnPrakInDB = streamsAutumnService.getStreamsAutumnByName(prakStudyStreamName);
                    if (streamsAutumnPrakInDB == null) {
                        //Створити новий навчальний потік для групи
                        StreamsAutumn streamsAutumnPrak = new StreamsAutumn();
                        streamsAutumnPrak.setNameStream(prakStudyStreamName);
                        streamsAutumnPrak.setNameGroups(group);
                        //Додати дисципліну у перелік дисциплін, що викладаються потоку
                        streamsAutumnPrak.addStreamsCourses(streamsCoursesAutumnNewPrak);
                        //Записати потік до БД
                        streamsAutumnService.createStreamsAutumn(streamsAutumnPrak);
                        System.out.println("PRAK === " + prakStudyStreamName+" створено");
                    } else {
                        //Є навчальний потік у БД
                        //Додати дисципліну у перелік дисциплін, що викладаються потоку
                        streamsAutumnPrakInDB.addStreamsCourses(streamsCoursesAutumnNewPrak);
                        //Оновити базу
                        streamsAutumnService.updateStreamsAutumn(streamsAutumnPrakInDB);
                    }
                    System.out.println("PRAK === " + prakStudyStreamName+" додана нова дисципліна");
                }
            }

            //KURS DDimaE 20-02-25
            if (plan.getIndZadanie() != null &&
                    (plan.getIndZadanie().equals("КР") || plan.getIndZadanie().equals("КП"))) {
                String[] groupsInStream = plan.getGroupsNames().split(", ");
                //Створити рядок з розподілом годин для КП (КР,НДР) з дисципліни (Просто строка с інд завданням =  КР або КП)
                StreamsCoursesAutumn streamsCoursesAutumnNewKR = createNewStudyloadRow("КР", plan);
                //Отримати перелік груп
                String[] groupsInKRStream = plan.getGroupsNames().split(", ");
                for (String group : groupsInStream) {
                    // Створити імя потоку для проведення КР в певній групі
                    String krStudyStreamName = "KR";
                    krStudyStreamName = addSpecGroup(group,krStudyStreamName);
                    //Чи Є навчальний потік для лаб у БД
                    StreamsAutumn streamsAutumnKRInDB = streamsAutumnService.getStreamsAutumnByName(krStudyStreamName);
                    if (streamsAutumnKRInDB == null) {
                        //Створити новий навчальний потік для групи
                        StreamsAutumn streamsAutumnKR = new StreamsAutumn();
                        streamsAutumnKR.setNameStream(krStudyStreamName);
                        streamsAutumnKR.setNameGroups(group);
                        //Додати дисципліну у перелік дисциплін, що викладаються потоку
                        streamsAutumnKR.addStreamsCourses(streamsCoursesAutumnNewKR);
                        //Записати потік до БД
                        streamsAutumnService.createStreamsAutumn(streamsAutumnKR);
                        System.out.println("KR === " + krStudyStreamName+" створено");
                    } else {
                        //Є навчальний потік у БД
                        //Додати дисципліну у перелік дисциплін, що викладаються потоку
                        streamsAutumnKRInDB.addStreamsCourses(streamsCoursesAutumnNewKR);
                        //Оновити базу
                        streamsAutumnService.updateStreamsAutumn(streamsAutumnKRInDB);
                    }
                    System.out.println("KR === " + krStudyStreamName+" додана нова дисципліна");
                }
            }

        }
        System.out.println("=== StreamsProc: Перелік потоків на осінь було оновлено");
    }


}
