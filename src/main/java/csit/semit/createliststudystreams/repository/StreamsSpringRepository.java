package csit.semit.createliststudystreams.repository;

import csit.semit.createliststudystreams.entity.StreamsSpring;
import org.springframework.data.repository.CrudRepository;

public interface StreamsSpringRepository extends CrudRepository<StreamsSpring, Long> {
    StreamsSpring findByNameStream(String name_stream);
}
