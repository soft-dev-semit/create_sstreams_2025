package csit.semit.createliststudystreams.repository;

import csit.semit.createliststudystreams.entity.AcademicGroup;
import org.springframework.data.repository.CrudRepository;

public interface GroupRepository extends CrudRepository<AcademicGroup, Long> {
    AcademicGroup findByGroupNameIsEndingWith(String name_group);
}
