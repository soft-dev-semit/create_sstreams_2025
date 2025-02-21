package csit.semit.createliststudystreams.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "educational_streams_courses")
@NoArgsConstructor
@Getter
@Setter
public class StreamsCoursesAutumn implements Comparable<StreamsCoursesAutumn> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "course_name")
    private String courseName;
    @Column(name = "course")
    private int course;
    @Column(name = "semestr")
    private int semestr;
    @Column(name = "ECTS")
    private int ects;
    @Column(name = "hours_lection")
    private int hoursLection;
    @Column(name = "hours_lab")
    private int hoursLab;
    @Column(name = "hours_prak")
    private int hoursPrak;
    @Column(name = "ind_zadanie")
    private String indZadanie;
    @Column(name = "zalik")
    private String zalik;
    @Column(name = "exam")
    private String exam;



//    Из того что вижу в "боевом" файле:
//1 уровень - по курсам. При этом группы с индексом "с" - они могут объединяться группами на курс больше. Т.е. например групп 223с - это 2 курс, но она идет в потоке с 3 курсом (222а и т.п.)
//2 уровень - по шифрам дисциплин (т.е. в том порядке, в котором они идут в темплане) Понимаю, что как это делать непонятно
//3 Сначала лекции, потом лабы, пз, потом кр
//4. Группы сначала 121, потом 122, потом 126 , потом другие
//5. Группы укр  и анг сортируются отдельно - укр сначала.
    @Override
    public int compareTo(StreamsCoursesAutumn o) {
        //Сравнение по курсу
        int res = course - o.course;
        if (res == 0) {
            //Сравнение по семестру
            res = semestr - o.semestr;
            if (res==0) {
                //Сравнение по алфавиту
                res = courseName.compareTo(o.courseName);
                return res;
            } else {
                return res;
            }
        } else {
            return res;
        }
    }
}
