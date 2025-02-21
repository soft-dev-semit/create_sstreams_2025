package csit.semit.createliststudystreams.repository;

import csit.semit.createliststudystreams.entity.StreamsAutumn;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface StreamsAutumnRepository extends CrudRepository<StreamsAutumn, Long> {

    //Для того, щоб завантажувати дані, коли викликається перелік StreamsAutumn з інших класів
    @EntityGraph(attributePaths = {"streamsCoursesAutumns"})
    Optional<StreamsAutumn> findById(Long id);

    @EntityGraph(attributePaths = {"streamsCoursesAutumns"})
    List<StreamsAutumn> findAll(); // Добавили @EntityGraph
    StreamsAutumn findByNameStream(String name_stream);
}
