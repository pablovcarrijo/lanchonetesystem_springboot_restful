package com.example.lanchonetesystem.Repository;

import com.example.lanchonetesystem.domain.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Modifying(clearAutomatically = true)
    @Query("UPDATE Product p SET p.price = :newPrice WHERE p.id = :id")
    int updatePrice(@Param("newPrice") Double newPrice, @Param("id") Long id);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE Product p SET p.available = :value WHERE p.id = :id")
    int updateAvailable(@Param("id") Long id, @Param("value") boolean value);

    @Query("SELECT p FROM Product p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%',:name,'%'))")
    Page<Product> findByName(@Param("name") String name, Pageable pageable);

}
