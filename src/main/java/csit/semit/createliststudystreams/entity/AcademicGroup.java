package csit.semit.createliststudystreams.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// academic_group
@Entity
@Table(name = "group_students")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AcademicGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "course")
    private int course;
    @Column(name = "group_name")
    private String groupName;
    @Column(name = "amount_students")
    private int amountStudents;
}
