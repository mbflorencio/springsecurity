package br.mf.demospringsecurity.repository;

import br.mf.demospringsecurity.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query("""
            select distinct p
            from Product p
            join RequestProduct rp on rp.product = p
            where rp.request.id = :requestId
            """)
    List<Product> findByRequestId(@Param("requestId") Long requestId);
}
