package com.example.lanchonetesystem.Repository;

import com.example.lanchonetesystem.domain.FullOrder;
import com.example.lanchonetesystem.domain.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderRepository extends JpaRepository<FullOrder, Long> {

    Page<FullOrder> findByStatus(Status status, Pageable pageable);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE FullOrder f SET f.status = :newStatus WHERE f.id = :orderId")
    int updateStatus(@Param("orderId") Long orderId, @Param("newStatus") Status newStatus);

}
