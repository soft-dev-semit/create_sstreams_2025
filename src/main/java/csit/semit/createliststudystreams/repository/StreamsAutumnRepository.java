package csit.semit.createliststudystreams.repository;

import csit.semit.createliststudystreams.entity.StreamsAutumn;
import org.springframework.data.repository.CrudRepository;

public interface StreamsAutumnRepository extends CrudRepository<StreamsAutumn, Long> {
    StreamsAutumn findByNameStream(String name_stream);
}
