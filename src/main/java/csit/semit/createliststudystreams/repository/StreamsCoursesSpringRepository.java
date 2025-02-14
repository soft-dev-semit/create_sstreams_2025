package csit.semit.createliststudystreams.repository;

import csit.semit.createliststudystreams.entity.StreamsCoursesSpring;
import org.springframework.data.repository.CrudRepository;

public interface StreamsCoursesSpringRepository extends CrudRepository<StreamsCoursesSpring, Long> {
    StreamsCoursesSpring findByCourseName(String course_name);
}
