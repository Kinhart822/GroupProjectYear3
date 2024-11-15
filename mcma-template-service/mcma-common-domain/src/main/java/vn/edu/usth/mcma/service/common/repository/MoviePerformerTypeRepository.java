package vn.edu.usth.mcma.service.common.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.edu.usth.mcma.service.common.dao.MoviePerformerType;

@Repository
public interface MoviePerformerTypeRepository extends JpaRepository<MoviePerformerType, Long> {
}
