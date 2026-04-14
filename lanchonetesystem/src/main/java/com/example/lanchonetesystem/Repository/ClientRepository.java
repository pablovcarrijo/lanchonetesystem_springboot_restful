package com.example.lanchonetesystem.Repository;

import com.example.lanchonetesystem.domain.Client;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ClientRepository extends JpaRepository<Client, Long> {

    @Query("SELECT c FROM Client c WHERE LOWER(c.name) LIKE LOWER(CONCAT('%',:name,'%'))")
    Page<Client> findByName(@Param("name") String name, Pageable pageable);
}
