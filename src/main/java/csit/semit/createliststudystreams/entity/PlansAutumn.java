package csit.semit.createliststudystreams.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "plans_autumn")
@Data
public class PlansAutumn {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name_course")
    private String nameCourse;
    @Column(name = "groups_names")
    private String groupsNames;
    @Column(name = "course")
    private int course;
    @Column(name = "semestr")
    private int semestr;
    @Column(name = "ECTS")
    private int ects;
    @Column(name = "hours_zagal_obs")
    private int hoursZagalObs;
    @Column(name = "hours_all")
    private int hoursAll;
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
}
