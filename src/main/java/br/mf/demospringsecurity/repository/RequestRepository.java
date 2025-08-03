package br.mf.demospringsecurity.repository;

import br.mf.demospringsecurity.model.Request;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {
    List<Request> findByUserId(Long userId);
}
