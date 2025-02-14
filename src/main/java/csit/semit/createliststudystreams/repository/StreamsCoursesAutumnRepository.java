package csit.semit.createliststudystreams.repository;

import csit.semit.createliststudystreams.entity.StreamsCoursesAutumn;
import org.springframework.data.repository.CrudRepository;

public interface StreamsCoursesAutumnRepository extends CrudRepository<StreamsCoursesAutumn, Long> {
    StreamsCoursesAutumn findByCourseName(String course_name);
}
