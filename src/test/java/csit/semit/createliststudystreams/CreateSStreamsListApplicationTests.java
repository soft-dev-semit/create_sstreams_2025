package csit.semit.createliststudystreams;

import csit.semit.createliststudystreams.entity.AcademicGroup;
import csit.semit.createliststudystreams.repository.GroupRepository;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static csit.semit.createliststudystreams.excelutils.ExcelDataProcessing.processCell;

@SpringBootTest
class CreateSStreamsListApplicationTests {

    @Test
    void contextLoads() {
    }

    @Autowired
    GroupRepository grrep;

    @Test
    void testGroupsFromDB() {
        List<AcademicGroup> list = (List<AcademicGroup>) grrep.findAll();
        list.stream().forEach(System.out::println);
    }

}
