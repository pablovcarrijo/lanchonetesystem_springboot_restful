package com.example.lanchonetesystem.Controller;

import com.example.lanchonetesystem.Services.OrderService;
import com.example.lanchonetesystem.domain.FullOrder;
import com.example.lanchonetesystem.domain.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    public OrderService orderService;

    @GetMapping("/{id}")
    public ResponseEntity<FullOrder> findById(@PathVariable Long id){
        return ResponseEntity.ok(orderService.findById(id));
    }

    @GetMapping("/all")
    public ResponseEntity<PagedModel<EntityModel<FullOrder>>> findAll(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "5") Integer size
    ){

        Pageable pageable = PageRequest.of(page, size);

        return ResponseEntity.ok(orderService.findAll(pageable));
    }

    @GetMapping
    public ResponseEntity<PagedModel<EntityModel<FullOrder>>> findByStatus(
            @RequestParam(value = "statusConsult", defaultValue = "RECEBIDO") Status statusConsult,
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "5") Integer size
    ){
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(orderService.findByStatus(statusConsult, pageable));
    }

    @PostMapping
    public ResponseEntity<FullOrder> createOneOrder(@RequestBody FullOrder order){
        return new ResponseEntity<>(orderService.createOneOrder(order), HttpStatus.CREATED);
    }

    @PatchMapping("/{orderId}")
    public ResponseEntity<FullOrder> updateStatus(
            @PathVariable Long orderId,
            @RequestParam(value = "newStatus", defaultValue = "RECEBIDO") Status newStatus
    ){
        return ResponseEntity.ok(orderService.updateStatus(orderId, newStatus));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id){
        orderService.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }
}
