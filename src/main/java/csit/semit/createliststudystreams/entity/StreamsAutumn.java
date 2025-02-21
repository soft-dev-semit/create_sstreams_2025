package csit.semit.createliststudystreams.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SortNatural;

import java.util.SortedSet;
import java.util.TreeSet;


@Entity
@Table(name = "educational_streams")
@NoArgsConstructor
@Getter
@Setter
public class StreamsAutumn {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name_stream")
    private String nameStream;
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "streams_courses",
            joinColumns =
                    { @JoinColumn(name = "stream_id", referencedColumnName = "id") },
            inverseJoinColumns =
                    { @JoinColumn(name = "course_id", referencedColumnName = "id") })
    @SortNatural // Hibernate будет сортировать по `Comparable`
    private SortedSet<StreamsCoursesAutumn> streamsCoursesAutumns;

    @Column(name = "name_groups")
    private String nameGroups;

    public void addStreamsCourses(StreamsCoursesAutumn newStreamsCoursesAutumn) {
        if (streamsCoursesAutumns == null) {
            streamsCoursesAutumns =new TreeSet ();
        }
        streamsCoursesAutumns.add(newStreamsCoursesAutumn);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("StreamA (id="+id+"): ");
        sb.append(nameStream).append(System.lineSeparator()).append("Disciplines:").append(System.lineSeparator());
        if (streamsCoursesAutumns != null) {
            streamsCoursesAutumns.size();
            for (StreamsCoursesAutumn scA: streamsCoursesAutumns) {
                sb.append(scA.getId()).append(": ").append(scA.getCourseName()).append(System.lineSeparator());
            }
        } else {
            sb.append("не знайдені");
        }
        sb.append(System.lineSeparator());
        return sb.toString();
    }
}