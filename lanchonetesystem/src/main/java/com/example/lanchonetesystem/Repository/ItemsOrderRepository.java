package com.example.lanchonetesystem.Repository;

import com.example.lanchonetesystem.domain.ItemsOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemsOrderRepository extends JpaRepository<ItemsOrder, Long> {

    List<ItemsOrder> findByFullOrderId(Long fullOrderId);
    Page<ItemsOrder> findByFullOrderId(Long fullOrderId, Pageable pageable);

}
