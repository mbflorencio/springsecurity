package br.mf.demospringsecurity.repository;

import br.mf.demospringsecurity.model.RequestProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RequestItemRepository extends JpaRepository<RequestProduct, Long> {
}
