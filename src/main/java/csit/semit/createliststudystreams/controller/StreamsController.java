package csit.semit.createliststudystreams.controller;

import csit.semit.createliststudystreams.entity.*;
import csit.semit.createliststudystreams.excelutils.ExcelDataProcessing;
import csit.semit.createliststudystreams.repository.*;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static csit.semit.createliststudystreams.excelutils.ExcelDataProcessing.processCell;

@Controller
public class StreamsController {

    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    private PlansAutumnRepository plansAutumnRepository;
    @Autowired
    private PlansSpringRepository plansSpringRepository;
    @Autowired
    private StreamsAutumnRepository streamsAutumnRepository;
    @Autowired
    private StreamsCoursesAutumnRepository streamsCoursesAutumnRepository;
    @Autowired
    private StreamsSpringRepository streamsSpringRepository;
    @Autowired
    private StreamsCoursesSpringRepository streamsCoursesSpringRepository;

    @GetMapping("/")
    public String home(Model model) {
//        Iterable<AcademicGroup> groups = groupRepository.findAll();
//        Iterable<PlansSpring> plansSpring = plansSpringRepository.findAll();
//        Iterable<PlansAutumn> plansAutumn = plansAutumnRepository.findAll();
//        Iterable<StreamsAutumn> streamsAutumn = streamsAutumnRepository.findAll();
//        Iterable<StreamsSpring> streamsSpring = streamsSpringRepository.findAll();
        model.addAttribute("title", "StudyPlan");
//        model.addAttribute("groups", groups);
        model.addAttribute("groups", groupRepository.findAll());
//        model.addAttribute("plansSpring", plansSpring);
        model.addAttribute("plansSpring", plansSpringRepository.findAll());
//        model.addAttribute("plansAutumn", plansAutumn);
        model.addAttribute("plansAutumn", plansAutumnRepository.findAll());
//        model.addAttribute("streamsAutumn", streamsAutumn);
        model.addAttribute("streamsAutumn", streamsAutumnRepository.findAll());
//        model.addAttribute("streamsSpring", streamsSpring);
        model.addAttribute("streamsSpring", streamsSpringRepository.findAll());
        return "StudyStreamsPage";
    }


    @PostMapping("/createStreamsAutumn")
    public String createStreamsAutumn(Model model) throws IOException {
        streamsAutumnRepository.deleteAll();
        streamsCoursesAutumnRepository.deleteAll();
        Iterable<PlansAutumn> plansAutumns = plansAutumnRepository.findAll();
        Iterator<PlansAutumn> plans1 = plansAutumns.iterator();
        while (plans1.hasNext()) {
            PlansAutumn plans2 = plans1.next();
            System.out.println(plans2.toString());
            String courseName = "Lec";
            String[] groups = plans2.getGroupsNames().split(", ");
            String groupsToAdd = "";
            for (String group : groups) {
                Pattern pattern = Pattern.compile("-");
                Matcher matcher = pattern.matcher(group);
                String firstElm = "";
                if (matcher.find()) {
                    firstElm = group.substring(matcher.start());
                }
                char[] charGroup = firstElm.substring(1).toCharArray();
                if (charGroup[0] == '4') {
                    if (!courseName.contains("_122")) {
                        courseName += "_122";
                    }
                } else if (charGroup[0] == '2') {
                    if (!courseName.contains("_121")) {
                        courseName += "_121";
                    }
                } else if (charGroup[0] == '7') {
                    if (!courseName.contains("_126")) {
                        courseName += "_126";
                    }
                }
                String addGroup = group.substring(matcher.start());
                courseName += "_" + addGroup.substring(1);
                if (groupsToAdd == "") {
                    groupsToAdd += group;
                } else {
                    groupsToAdd += " " + group;
                }
            }
            StreamsAutumn streamsAutumn = new StreamsAutumn();
            StreamsAutumn streamsAutumn2 = streamsAutumnRepository.findByNameStream(courseName);
            if (streamsAutumn2 == null) {
                streamsAutumn.setNameStream(courseName);
                streamsAutumn.setNameGroups(groupsToAdd);
                Set<StreamsCoursesAutumn> streamsCoursesAutumns = new HashSet<>();
                StreamsCoursesAutumn streamsCoursesAutumn2 = new StreamsCoursesAutumn();
                streamsCoursesAutumn2.setCourseName(plans2.getNameCourse());
                streamsCoursesAutumn2.setCourse(plans2.getCourse());
                streamsCoursesAutumn2.setSemestr(plans2.getSemestr());
                streamsCoursesAutumn2.setEcts(plans2.getEcts());
                streamsCoursesAutumn2.setHoursLection(plans2.getHoursLection());
                streamsCoursesAutumn2.setIndZadanie(plans2.getIndZadanie());
                streamsCoursesAutumn2.setZalik(plans2.getZalik());
                streamsCoursesAutumn2.setExam(plans2.getExam());
                streamsCoursesAutumns.add(streamsCoursesAutumn2);
                streamsAutumn.setStreamsCoursesAutumns(streamsCoursesAutumns);
                streamsAutumnRepository.save(streamsAutumn);
            } else {
                Set<StreamsCoursesAutumn> streamsCoursesAutumns = streamsAutumn2.getStreamsCoursesAutumns();
                StreamsCoursesAutumn streamsCoursesAutumn2 = new StreamsCoursesAutumn();
                streamsCoursesAutumn2.setCourseName(plans2.getNameCourse());
                streamsCoursesAutumn2.setCourse(plans2.getCourse());
                streamsCoursesAutumn2.setSemestr(plans2.getSemestr());
                streamsCoursesAutumn2.setEcts(plans2.getEcts());
                streamsCoursesAutumn2.setHoursLection(plans2.getHoursLection());
                streamsCoursesAutumn2.setIndZadanie(plans2.getIndZadanie());
                streamsCoursesAutumn2.setZalik(plans2.getZalik());
                streamsCoursesAutumn2.setExam(plans2.getExam());
                streamsCoursesAutumns.add(streamsCoursesAutumn2);
                streamsAutumn2.setStreamsCoursesAutumns(streamsCoursesAutumns);
                streamsAutumnRepository.save(streamsAutumn2);
            }

            System.out.println(courseName);
            String[] groups2 = plans2.getGroupsNames().split(", ");
            for (String group : groups2) {
                if (plans2.getHoursLab() != 0) {
                    String courseName2 = "Lab";
                    Pattern pattern = Pattern.compile("-");
                    Matcher matcher = pattern.matcher(group);
                    String firstElm = "";
                    if (matcher.find()) {
                        firstElm = group.substring(matcher.start());
                    }
                    char[] charGroup = firstElm.substring(1).toCharArray();
                    if (charGroup[0] == '4') {
                        if (!courseName2.contains("_122")) {
                            courseName2 += "_122";
                        }
                    } else if (charGroup[0] == '2') {
                        if (!courseName2.contains("_121")) {
                            courseName2 += "_121";
                        }
                    } else if (charGroup[0] == '7') {
                        if (!courseName2.contains("_126")) {
                            courseName2 += "_126";
                        }
                    }
                    String addGroup = group;
                    courseName2 += "_" + group.substring(matcher.start()).substring(1);
                    StreamsAutumn streamsAutumnLab = new StreamsAutumn();
                    StreamsAutumn streamsAutumn2Lab = streamsAutumnRepository.findByNameStream(courseName2);
                    if (streamsAutumn2Lab == null) {
                        streamsAutumnLab.setNameStream(courseName2);
                        streamsAutumnLab.setNameGroups(addGroup);
                        Set<StreamsCoursesAutumn> streamsCoursesAutumns = new HashSet<>();
                        StreamsCoursesAutumn streamsCoursesAutumn2 = new StreamsCoursesAutumn();
                        streamsCoursesAutumn2.setCourseName(plans2.getNameCourse());
                        streamsCoursesAutumn2.setCourse(plans2.getCourse());
                        streamsCoursesAutumn2.setSemestr(plans2.getSemestr());
                        streamsCoursesAutumn2.setEcts(plans2.getEcts());
                        streamsCoursesAutumn2.setHoursLab(plans2.getHoursLab());
                        streamsCoursesAutumn2.setIndZadanie(plans2.getIndZadanie());
                        streamsCoursesAutumn2.setZalik(plans2.getZalik());
                        streamsCoursesAutumn2.setExam(plans2.getExam());
                        streamsCoursesAutumns.add(streamsCoursesAutumn2);
                        streamsAutumnLab.setStreamsCoursesAutumns(streamsCoursesAutumns);
                        streamsAutumnRepository.save(streamsAutumnLab);
                    } else {
                        Set<StreamsCoursesAutumn> streamsCoursesAutumns = streamsAutumn2Lab.getStreamsCoursesAutumns();
                        StreamsCoursesAutumn streamsCoursesAutumn2 = new StreamsCoursesAutumn();
                        streamsCoursesAutumn2.setCourseName(plans2.getNameCourse());
                        streamsCoursesAutumn2.setCourse(plans2.getCourse());
                        streamsCoursesAutumn2.setSemestr(plans2.getSemestr());
                        streamsCoursesAutumn2.setEcts(plans2.getEcts());
                        streamsCoursesAutumn2.setHoursLab(plans2.getHoursLab());
                        streamsCoursesAutumn2.setIndZadanie(plans2.getIndZadanie());
                        streamsCoursesAutumn2.setZalik(plans2.getZalik());
                        streamsCoursesAutumn2.setExam(plans2.getExam());
                        streamsCoursesAutumns.add(streamsCoursesAutumn2);
                        streamsAutumn2Lab.setStreamsCoursesAutumns(streamsCoursesAutumns);
                        streamsAutumnRepository.save(streamsAutumn2Lab);
                    }
                    System.out.println(courseName2);
                } else if (plans2.getHoursPrak() != 0) {
                    String courseName2 = "Prak";
                    Pattern pattern = Pattern.compile("-");
                    Matcher matcher = pattern.matcher(group);
                    String firstElm = "";
                    if (matcher.find()) {
                        firstElm = group.substring(matcher.start());
                    }
                    char[] charGroup = firstElm.substring(1).toCharArray();
                    if (charGroup[0] == '4') {
                        if (!courseName2.contains("_122")) {
                            courseName2 += "_122";
                        }
                    } else if (charGroup[0] == '2') {
                        if (!courseName2.contains("_121")) {
                            courseName2 += "_121";
                        }
                    } else if (charGroup[0] == '7') {
                        if (!courseName2.contains("_126")) {
                            courseName2 += "_126";
                        }
                    }
                    String addGroup = group;
                    courseName2 += "_" + group.substring(matcher.start()).substring(1);
                    StreamsAutumn streamsAutumnPrak = new StreamsAutumn();
                    StreamsAutumn streamsAutumn2Prak = streamsAutumnRepository.findByNameStream(courseName2);
                    if (streamsAutumn2Prak == null) {
                        streamsAutumnPrak.setNameStream(courseName2);
                        streamsAutumnPrak.setNameGroups(addGroup);
                        Set<StreamsCoursesAutumn> streamsCoursesAutumns = new HashSet<>();
                        StreamsCoursesAutumn streamsCoursesAutumn2 = new StreamsCoursesAutumn();
                        streamsCoursesAutumn2.setCourseName(plans2.getNameCourse());
                        streamsCoursesAutumn2.setCourse(plans2.getCourse());
                        streamsCoursesAutumn2.setSemestr(plans2.getSemestr());
                        streamsCoursesAutumn2.setEcts(plans2.getEcts());
                        streamsCoursesAutumn2.setHoursPrak(plans2.getHoursPrak());
                        streamsCoursesAutumn2.setIndZadanie(plans2.getIndZadanie());
                        streamsCoursesAutumn2.setZalik(plans2.getZalik());
                        streamsCoursesAutumn2.setExam(plans2.getExam());
                        streamsCoursesAutumns.add(streamsCoursesAutumn2);
                        streamsAutumnPrak.setStreamsCoursesAutumns(streamsCoursesAutumns);
                        streamsAutumnRepository.save(streamsAutumnPrak);
                    } else {
                        Set<StreamsCoursesAutumn> streamsCoursesAutumns = streamsAutumn2Prak.getStreamsCoursesAutumns();
                        StreamsCoursesAutumn streamsCoursesAutumn2 = new StreamsCoursesAutumn();
                        streamsCoursesAutumn2.setCourseName(plans2.getNameCourse());
                        streamsCoursesAutumn2.setCourse(plans2.getCourse());
                        streamsCoursesAutumn2.setSemestr(plans2.getSemestr());
                        streamsCoursesAutumn2.setEcts(plans2.getEcts());
                        streamsCoursesAutumn2.setHoursPrak(plans2.getHoursPrak());
                        streamsCoursesAutumn2.setIndZadanie(plans2.getIndZadanie());
                        streamsCoursesAutumn2.setZalik(plans2.getZalik());
                        streamsCoursesAutumn2.setExam(plans2.getExam());
                        streamsCoursesAutumns.add(streamsCoursesAutumn2);
                        streamsAutumn2Prak.setStreamsCoursesAutumns(streamsCoursesAutumns);
                        streamsAutumnRepository.save(streamsAutumn2Prak);
                    }
                    System.out.println(courseName2);
                }
            }
        }
        Iterable<PlansAutumn> plansAutumn = plansAutumnRepository.findAll();
        Iterable<StreamsAutumn> streamsAutumn = streamsAutumnRepository.findAll();
        model.addAttribute("title", "StudyPlan");
        model.addAttribute("plansAutumn", plansAutumn);
        model.addAttribute("streamsAutumn", streamsAutumn);
        return "redirect:/";
    }

    @PostMapping("/combineStreams")
    public String combineStreams(@RequestParam(defaultValue = "") String streamCheck,
                                 @RequestParam(defaultValue = "") String combineStreams,
                                 @RequestParam(defaultValue = "") String splitStreams,
                                 Model model) throws IOException {
        System.out.println(streamCheck);
        if (!streamCheck.isEmpty()) {
            if (!combineStreams.isEmpty()) {
                System.out.println("Start 1");
                String[] streamToCombine = streamCheck.split(",");
                StreamsAutumn streamsAutumn1 = streamsAutumnRepository.findByNameStream(streamToCombine[0]);
                String courseName = streamsAutumn1.getNameStream();
                String addGroup = streamsAutumn1.getNameGroups();
                for (int i = 1; i < streamToCombine.length; i++) {
                    StreamsAutumn streamsAutumn2 = streamsAutumnRepository.findByNameStream(streamToCombine[i]);
                    addGroup += " " + streamsAutumn2.getNameGroups();
                    courseName += "_" + streamsAutumn2.getNameGroups();
                    streamsAutumnRepository.delete(streamsAutumn2);
                }
                streamsAutumn1.setNameStream(courseName);
                streamsAutumn1.setNameGroups(addGroup);
                streamsAutumnRepository.save(streamsAutumn1);
            } else if (!splitStreams.isEmpty()) {
                System.out.println("Start 2");
                StreamsAutumn streamsAutumn = streamsAutumnRepository.findByNameStream(streamCheck);
                String[] groups = streamsAutumn.getNameGroups().split(" ");
                for (int i = 0; i < groups.length; i++) {
                    Pattern pattern = Pattern.compile("[a-zA-Z]+_\\d{3}");
                    Matcher matcher = pattern.matcher(streamsAutumn.getNameStream());
                    StreamsAutumn streamsAutumn2 = new StreamsAutumn();
                    String courseName = "";
                    String group = groups[i];
                    if (matcher.find()) {
                        courseName = streamsAutumn.getNameStream().substring(matcher.start(), matcher.end()) + "_" + group;
                    }
                    System.out.println(courseName);
                    System.out.println(group);
                    Set<StreamsCoursesAutumn> streamsCoursesAutumn = streamsAutumn.getStreamsCoursesAutumns();
                    Set<StreamsCoursesAutumn> streamsCoursesAutumnNew = new HashSet<>();
                    for (StreamsCoursesAutumn elem : streamsCoursesAutumn) {
                        StreamsCoursesAutumn elem2 = new StreamsCoursesAutumn();
                        elem2.setCourseName(elem.getCourseName());
                        streamsCoursesAutumnNew.add(elem2);
                    }
                    System.out.println(streamsCoursesAutumnNew);
                    streamsAutumn2.setNameStream(courseName);
                    streamsAutumn2.setNameGroups(group);
                    streamsAutumn2.setStreamsCoursesAutumns(streamsCoursesAutumnNew);
                    streamsAutumnRepository.save(streamsAutumn2);
                }
                streamsAutumnRepository.delete(streamsAutumn);
            }
        }
        model.addAttribute("title", "StudyPlan");
        return "redirect:/";
    }

    @PostMapping("/createStreamsSpring")
    public String createStreamsSpring(Model model) throws IOException {
        streamsSpringRepository.deleteAll();
        streamsCoursesSpringRepository.deleteAll();
        Iterable<PlansSpring> plansSprings = plansSpringRepository.findAll();
        Iterator<PlansSpring> plans1 = plansSprings.iterator();
        while (plans1.hasNext()) {
            PlansSpring plans2 = plans1.next();
            System.out.println(plans2.toString());
            String courseName = "Lec";
            String[] groups = plans2.getGroupsNames().split(", ");
            String groupsToAdd = "";
            for (String group : groups) {
                Pattern pattern = Pattern.compile("-");
                Matcher matcher = pattern.matcher(group);
                String firstElm = "";
                if (matcher.find()) {
                    firstElm = group.substring(matcher.start());
                }
                char[] charGroup = firstElm.substring(1).toCharArray();
                if (charGroup[0] == '4') {
                    if (!courseName.contains("_122")) {
                        courseName += "_122";
                    }
                } else if (charGroup[0] == '2') {
                    if (!courseName.contains("_121")) {
                        courseName += "_121";
                    }
                } else if (charGroup[0] == '7') {
                    if (!courseName.contains("_126")) {
                        courseName += "_126";
                    }
                }
                String addGroup = group.substring(matcher.start());
                courseName += "_" + addGroup.substring(1);
                if (groupsToAdd == "") {
                    groupsToAdd += group;
                } else {
                    groupsToAdd += " " + group;
                }
            }
            StreamsSpring streamsSpring = new StreamsSpring();
            StreamsSpring streamsSpring2 = streamsSpringRepository.findByNameStream(courseName);
            if (streamsSpring2 == null) {
                streamsSpring.setNameStream(courseName);
                streamsSpring.setNameGroups(groupsToAdd);
                Set<StreamsCoursesSpring> streamsCoursesSprings = new HashSet<>();
                StreamsCoursesSpring streamsCoursesSpring2 = new StreamsCoursesSpring();
                streamsCoursesSpring2.setCourseName(plans2.getNameCourse());
                streamsCoursesSpring2.setCourse(plans2.getCourse());
                streamsCoursesSpring2.setSemestr(plans2.getSemestr());
                streamsCoursesSpring2.setEcts(plans2.getEcts());
                streamsCoursesSpring2.setHoursLection(plans2.getHoursLection());
                streamsCoursesSpring2.setIndZadanie(plans2.getIndZadanie());
                streamsCoursesSpring2.setZalik(plans2.getZalik());
                streamsCoursesSpring2.setExam(plans2.getExam());
                streamsCoursesSprings.add(streamsCoursesSpring2);
                streamsSpring.setStreamsCoursesSprings(streamsCoursesSprings);
                streamsSpringRepository.save(streamsSpring);
            } else {
                Set<StreamsCoursesSpring> streamsCoursesSprings = streamsSpring2.getStreamsCoursesSprings();
                StreamsCoursesSpring streamsCoursesSpring2 = new StreamsCoursesSpring();
                streamsCoursesSpring2.setCourseName(plans2.getNameCourse());
                streamsCoursesSpring2.setCourse(plans2.getCourse());
                streamsCoursesSpring2.setSemestr(plans2.getSemestr());
                streamsCoursesSpring2.setEcts(plans2.getEcts());
                streamsCoursesSpring2.setHoursLection(plans2.getHoursLection());
                streamsCoursesSpring2.setIndZadanie(plans2.getIndZadanie());
                streamsCoursesSpring2.setZalik(plans2.getZalik());
                streamsCoursesSpring2.setExam(plans2.getExam());
                streamsCoursesSprings.add(streamsCoursesSpring2);
                streamsSpring2.setStreamsCoursesSprings(streamsCoursesSprings);
                streamsSpringRepository.save(streamsSpring2);
            }

            System.out.println(courseName);
            String[] groups2 = plans2.getGroupsNames().split(", ");
            for (String group : groups2) {
                if (plans2.getHoursLab() != 0) {
                    String courseName2 = "Lab";
                    Pattern pattern = Pattern.compile("-");
                    Matcher matcher = pattern.matcher(group);
                    String firstElm = "";
                    if (matcher.find()) {
                        firstElm = group.substring(matcher.start());
                    }
                    char[] charGroup = firstElm.substring(1).toCharArray();
                    if (charGroup[0] == '4') {
                        if (!courseName2.contains("_122")) {
                            courseName2 += "_122";
                        }
                    } else if (charGroup[0] == '2') {
                        if (!courseName2.contains("_121")) {
                            courseName2 += "_121";
                        }
                    } else if (charGroup[0] == '7') {
                        if (!courseName2.contains("_126")) {
                            courseName2 += "_126";
                        }
                    }
                    String addGroup = group;
                    courseName2 += "_" + group.substring(matcher.start()).substring(1);
                    StreamsSpring streamsSpringLab = new StreamsSpring();
                    StreamsSpring streamsSpring2Lab = streamsSpringRepository.findByNameStream(courseName2);
                    if (streamsSpring2Lab == null) {
                        streamsSpringLab.setNameStream(courseName2);
                        streamsSpringLab.setNameGroups(addGroup);
                        Set<StreamsCoursesSpring> streamsCoursesSprings = new HashSet<>();
                        StreamsCoursesSpring streamsCoursesSpring2 = new StreamsCoursesSpring();
                        streamsCoursesSpring2.setCourseName(plans2.getNameCourse());
                        streamsCoursesSpring2.setCourse(plans2.getCourse());
                        streamsCoursesSpring2.setSemestr(plans2.getSemestr());
                        streamsCoursesSpring2.setEcts(plans2.getEcts());
                        streamsCoursesSpring2.setHoursLab(plans2.getHoursLab());
                        streamsCoursesSpring2.setIndZadanie(plans2.getIndZadanie());
                        streamsCoursesSpring2.setZalik(plans2.getZalik());
                        streamsCoursesSpring2.setExam(plans2.getExam());
                        streamsCoursesSprings.add(streamsCoursesSpring2);
                        streamsSpringLab.setStreamsCoursesSprings(streamsCoursesSprings);
                        streamsSpringRepository.save(streamsSpringLab);
                    } else {
                        Set<StreamsCoursesSpring> streamsCoursesSprings = streamsSpring2Lab.getStreamsCoursesSprings();
                        StreamsCoursesSpring streamsCoursesSpring2 = new StreamsCoursesSpring();
                        streamsCoursesSpring2.setCourseName(plans2.getNameCourse());
                        streamsCoursesSpring2.setCourse(plans2.getCourse());
                        streamsCoursesSpring2.setSemestr(plans2.getSemestr());
                        streamsCoursesSpring2.setEcts(plans2.getEcts());
                        streamsCoursesSpring2.setHoursLab(plans2.getHoursLab());
                        streamsCoursesSpring2.setIndZadanie(plans2.getIndZadanie());
                        streamsCoursesSpring2.setZalik(plans2.getZalik());
                        streamsCoursesSpring2.setExam(plans2.getExam());
                        streamsCoursesSprings.add(streamsCoursesSpring2);
                        streamsSpring2Lab.setStreamsCoursesSprings(streamsCoursesSprings);
                        streamsSpringRepository.save(streamsSpring2Lab);
                    }
                    System.out.println(courseName2);
                } else if (plans2.getHoursPrak() != 0) {
                    String courseName2 = "Prak";
                    Pattern pattern = Pattern.compile("-");
                    Matcher matcher = pattern.matcher(group);
                    String firstElm = "";
                    if (matcher.find()) {
                        firstElm = group.substring(matcher.start());
                    }
                    char[] charGroup = firstElm.substring(1).toCharArray();
                    if (charGroup[0] == '4') {
                        if (!courseName2.contains("_122")) {
                            courseName2 += "_122";
                        }
                    } else if (charGroup[0] == '2') {
                        if (!courseName2.contains("_121")) {
                            courseName2 += "_121";
                        }
                    } else if (charGroup[0] == '7') {
                        if (!courseName2.contains("_126")) {
                            courseName2 += "_126";
                        }
                    }
                    String addGroup = group;
                    courseName2 += "_" + group.substring(matcher.start()).substring(1);
                    StreamsSpring streamsSpringPrak = new StreamsSpring();
                    StreamsSpring streamsSpring2Prak = streamsSpringRepository.findByNameStream(courseName2);
                    if (streamsSpring2Prak == null) {
                        streamsSpringPrak.setNameStream(courseName2);
                        streamsSpringPrak.setNameGroups(addGroup);
                        Set<StreamsCoursesSpring> streamsCoursesSprings = new HashSet<>();
                        StreamsCoursesSpring streamsCoursesSpring2 = new StreamsCoursesSpring();
                        streamsCoursesSpring2.setCourseName(plans2.getNameCourse());
                        streamsCoursesSpring2.setCourse(plans2.getCourse());
                        streamsCoursesSpring2.setSemestr(plans2.getSemestr());
                        streamsCoursesSpring2.setEcts(plans2.getEcts());
                        streamsCoursesSpring2.setHoursPrak(plans2.getHoursPrak());
                        streamsCoursesSpring2.setIndZadanie(plans2.getIndZadanie());
                        streamsCoursesSpring2.setZalik(plans2.getZalik());
                        streamsCoursesSpring2.setExam(plans2.getExam());
                        streamsCoursesSprings.add(streamsCoursesSpring2);
                        streamsSpringPrak.setStreamsCoursesSprings(streamsCoursesSprings);
                        streamsSpringRepository.save(streamsSpringPrak);
                    } else {
                        Set<StreamsCoursesSpring> streamsCoursesSprings = streamsSpring2Prak.getStreamsCoursesSprings();
                        StreamsCoursesSpring streamsCoursesSpring2 = new StreamsCoursesSpring();
                        streamsCoursesSpring2.setCourseName(plans2.getNameCourse());
                        streamsCoursesSpring2.setCourse(plans2.getCourse());
                        streamsCoursesSpring2.setSemestr(plans2.getSemestr());
                        streamsCoursesSpring2.setEcts(plans2.getEcts());
                        streamsCoursesSpring2.setHoursPrak(plans2.getHoursPrak());
                        streamsCoursesSpring2.setIndZadanie(plans2.getIndZadanie());
                        streamsCoursesSpring2.setZalik(plans2.getZalik());
                        streamsCoursesSpring2.setExam(plans2.getExam());
                        streamsCoursesSprings.add(streamsCoursesSpring2);
                        streamsSpring2Prak.setStreamsCoursesSprings(streamsCoursesSprings);
                        streamsSpringRepository.save(streamsSpring2Prak);
                    }
                    System.out.println(courseName2);
                }
            }
        }
        Iterable<PlansSpring> plansSpring = plansSpringRepository.findAll();
        Iterable<StreamsSpring> streamsSpring = streamsSpringRepository.findAll();
        model.addAttribute("title", "StudyPlan");
        model.addAttribute("plansAutumn", plansSpring);
        model.addAttribute("streamsAutumn", streamsSpring);
        return "redirect:/";
    }

    @PostMapping("/combineStreamsSpring")
    public String combineStreamsSpring(@RequestParam(defaultValue = "") String streamCheck,
                                       @RequestParam(defaultValue = "") String combineStreams,
                                       @RequestParam(defaultValue = "") String splitStreams,
                                       Model model) throws IOException {
        System.out.println(streamCheck);
        if (!streamCheck.isEmpty()) {
            if (!combineStreams.isEmpty()) {
                System.out.println("Start 1");
                String[] streamToCombine = streamCheck.split(",");
                StreamsSpring streamsSpring1 = streamsSpringRepository.findByNameStream(streamToCombine[0]);
                String courseName = streamsSpring1.getNameStream();
                String addGroup = streamsSpring1.getNameGroups();
                for (int i = 1; i < streamToCombine.length; i++) {
                    StreamsSpring streamsSpring2 = streamsSpringRepository.findByNameStream(streamToCombine[i]);
                    addGroup += " " + streamsSpring2.getNameGroups();
                    courseName += "_" + streamsSpring2.getNameGroups();
                    streamsSpringRepository.delete(streamsSpring2);
                }
                streamsSpring1.setNameStream(courseName);
                streamsSpring1.setNameGroups(addGroup);
                streamsSpringRepository.save(streamsSpring1);
            } else if (!splitStreams.isEmpty()) {
                System.out.println("Start 2");
                StreamsSpring streamsSpring = streamsSpringRepository.findByNameStream(streamCheck);
                String[] groups = streamsSpring.getNameGroups().split(" ");
                for (int i = 0; i < groups.length; i++) {
                    Pattern pattern = Pattern.compile("-");
                    Matcher matcher = pattern.matcher(streamsSpring.getNameStream());
                    StreamsSpring streamsSpring2 = new StreamsSpring();
                    String courseName = "";
                    String group = groups[i];
                    if (matcher.find()) {
                        courseName = streamsSpring.getNameStream().substring(matcher.start(), matcher.end()) + "_" + group;
                    }
                    System.out.println(courseName);
                    System.out.println(group);
                    Set<StreamsCoursesSpring> streamsCoursesSpring = streamsSpring.getStreamsCoursesSprings();
                    Set<StreamsCoursesSpring> streamsCoursesSpringNew = new HashSet<>();
                    for (StreamsCoursesSpring elem : streamsCoursesSpring) {
                        StreamsCoursesSpring elem2 = new StreamsCoursesSpring();
                        elem2.setCourseName(elem.getCourseName());
                        streamsCoursesSpringNew.add(elem2);
                    }
                    System.out.println(streamsCoursesSpringNew);
                    streamsSpring2.setNameStream(courseName);
                    streamsSpring2.setNameGroups(group);
                    streamsSpring2.setStreamsCoursesSprings(streamsCoursesSpringNew);
                    streamsSpringRepository.save(streamsSpring2);
                }
                streamsSpringRepository.delete(streamsSpring);
            }
        }
        model.addAttribute("title", "StudyPlan");
        return "redirect:/";
    }

    @PostMapping("/addGroups")
    public String addGroups(@RequestParam MultipartFile groupDocument, Model model) throws IOException {
        FileOutputStream fiswrite = new FileOutputStream(groupDocument.getOriginalFilename());
        fiswrite.write(groupDocument.getBytes());
        fiswrite.close();
        FileInputStream fis = new FileInputStream(groupDocument.getOriginalFilename());
        Workbook wb = new HSSFWorkbook(fis);
        var sheetIterator = wb.sheetIterator();
        groupRepository.deleteAll();
        while (sheetIterator.hasNext()) {
            Sheet sheet = sheetIterator.next();
            var data = new HashMap<Integer, List<Object>>();
            var iterator = sheet.rowIterator();
            int i = 0;
            for (var rowIndex = 0; iterator.hasNext(); rowIndex++) {
                var row = iterator.next();
                if (i > 2) {
                    data.put(rowIndex, new ArrayList<>());
                    for (var cell : row) {
                        processCell(cell, data.get(rowIndex));
                    }
                }
                i++;
            }
            for (Map.Entry<Integer, List<Object>> entry : data.entrySet()) {
                List<Object> value = entry.getValue();
                for (int a = 4; a < value.size(); a += 2) {
                    int course = (a / 2) - 1;
                    String group = value.get(a).toString();
                    String amount = value.get(a + 1).toString();
                    if (group.trim() != "") {
                        AcademicGroup groups = new AcademicGroup();
                        groups.setCourse(course);
                        groups.setGroupName(group);
                        groups.setAmountStudents(Integer.parseInt(amount));
                        groupRepository.save(groups);
                    }
                }
            }
        }
        fis.close();
        model.addAttribute("title", "StudyPlan");
        return "redirect:/";
    }

    @PostMapping("/addPlans")
    public String addPlans(@RequestParam MultipartFile plansDocument,
                           Model model) throws IOException {
        FileOutputStream fiswrite = new FileOutputStream(plansDocument.getOriginalFilename());
        fiswrite.write(plansDocument.getBytes());
        fiswrite.close();
        FileInputStream fis = new FileInputStream(plansDocument.getOriginalFilename());
        Workbook wb = new HSSFWorkbook(fis);
        var sheetIterator = wb.sheetIterator();
        int list = 1;
        plansAutumnRepository.deleteAll();
        plansSpringRepository.deleteAll();
        while (sheetIterator.hasNext()) {
            Sheet sheet = sheetIterator.next();
            if (list == 2) {
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
                for (Map.Entry<Integer, List<Object>> entry : data.entrySet()) {
                    List<Object> value = entry.getValue();
                    if (value.isEmpty()) break;
                    PlansSpring plansSpring = new PlansSpring();
                    plansSpring.setNameCourse(value.get(1).toString());
                    plansSpring.setGroupsNames(value.get(3).toString());
                    plansSpring.setCourse(Integer.parseInt(value.get(4).toString()));
                    plansSpring.setSemestr(Integer.parseInt(value.get(5).toString()));
                    if (value.get(8).toString().trim() != "") {
                        plansSpring.setEcts(Integer.parseInt(value.get(8).toString()));
                    }
                    if (value.get(9).toString().trim() != "") {
                        plansSpring.setHoursZagalObs(Integer.parseInt(value.get(9).toString()));
                    }
                    if (value.get(10).toString().trim() != "") {
                        plansSpring.setHoursAll(Integer.parseInt(value.get(10).toString()));
                    }
                    if (value.get(10).toString().trim() != "") {
                        plansSpring.setHoursLection(Integer.parseInt(value.get(11).toString()));
                    }
                    if (value.get(12).toString().trim() != "") {
                        plansSpring.setHoursLab(Integer.parseInt(value.get(12).toString()));
                    }
                    if (value.get(13).toString().trim() != "") {
                        plansSpring.setHoursPrak(Integer.parseInt(value.get(13).toString()));
                    }
                    if (value.get(14).toString().trim() != "") {
                        plansSpring.setIndZadanie(value.get(14).toString());
                    }
                    if (value.get(15).toString().trim() != "") {
                        plansSpring.setZalik(value.get(15).toString());
                    }
                    if (value.get(16).toString().trim() != "") {
                        plansSpring.setExam(value.get(16).toString());
                    }
                    plansSpringRepository.save(plansSpring);
                }
            } else {
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
                for (Map.Entry<Integer, List<Object>> entry : data.entrySet()) {
                    List<Object> value = entry.getValue();
                    if (value.isEmpty()) break;
                    PlansAutumn plansAutumn = new PlansAutumn();
                    plansAutumn.setNameCourse(value.get(1).toString());
                    plansAutumn.setGroupsNames(value.get(3).toString());
                    plansAutumn.setCourse(Integer.parseInt(value.get(4).toString()));
                    plansAutumn.setSemestr(Integer.parseInt(value.get(5).toString()));
                    if (value.get(8).toString().trim() != "") {
                        plansAutumn.setEcts(Integer.parseInt(value.get(8).toString()));
                    }
                    if (value.get(9).toString().trim() != "") {
                        plansAutumn.setHoursZagalObs(Integer.parseInt(value.get(9).toString()));
                    }
                    if (value.get(10).toString().trim() != "") {
                        plansAutumn.setHoursAll(Integer.parseInt(value.get(10).toString()));
                    }
                    if (value.get(10).toString().trim() != "") {
                        plansAutumn.setHoursLection(Integer.parseInt(value.get(11).toString()));
                    }
                    if (value.get(12).toString().trim() != "") {
                        plansAutumn.setHoursLab(Integer.parseInt(value.get(12).toString()));
                    }
                    if (value.get(13).toString().trim() != "") {
                        plansAutumn.setHoursPrak(Integer.parseInt(value.get(13).toString()));
                    }
                    if (value.get(14).toString().trim() != "") {
                        plansAutumn.setIndZadanie(value.get(14).toString());
                    }
                    if (value.get(15).toString().trim() != "") {
                        plansAutumn.setZalik(value.get(15).toString());
                    }
                    if (value.get(16).toString().trim() != "") {
                        plansAutumn.setExam(value.get(16).toString());
                    }
                    plansAutumnRepository.save(plansAutumn);
                }
            }
            list++;
        }
        fis.close();
        model.addAttribute("title", "StudyPlan");
        return "redirect:/";
    }

    @PostMapping("/createExcel")
    public String createExcel() throws IOException {
        List<StreamsAutumn> streamsAutumn = (List<StreamsAutumn>) streamsAutumnRepository.findAll();
        List<AcademicGroup> academicGroups = (List<AcademicGroup>) groupRepository.findAll();
        List<StreamsSpring> streamsSpring = (List<StreamsSpring>) streamsSpringRepository.findAll();
        String resCreate = ExcelDataProcessing.createExcel(streamsAutumn, academicGroups, streamsSpring);
        System.out.println("File created: "+resCreate);
        return "redirect:/";
    }


}