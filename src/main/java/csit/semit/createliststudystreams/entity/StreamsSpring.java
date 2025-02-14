package csit.semit.createliststudystreams.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Set;

@Entity
@Table(name = "educational_streams_spring")
@Data
public class StreamsSpring {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name_stream")
    private String nameStream;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "streams_courses_spring",
            joinColumns =
                    { @JoinColumn(name = "stream_id", referencedColumnName = "id") },
            inverseJoinColumns =
                    { @JoinColumn(name = "course_id", referencedColumnName = "id") })
    private Set<StreamsCoursesSpring> streamsCoursesSprings;
    @Column(name = "name_groups")
    private String nameGroups;
}
