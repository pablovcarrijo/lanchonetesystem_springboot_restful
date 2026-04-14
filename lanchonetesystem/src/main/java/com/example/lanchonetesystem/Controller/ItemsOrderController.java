package com.example.lanchonetesystem.Controller;

import com.example.lanchonetesystem.Services.ItemsOrderService;
import com.example.lanchonetesystem.domain.ItemsOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/itemsOrder")
public class ItemsOrderController {

    @Autowired
    private ItemsOrderService itemsOrderService;

    @GetMapping("/{id}/items")
    public ResponseEntity<Page<ItemsOrder>> findItemsByOrderId (
            @PathVariable Long id,
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "5") Integer size
    ){
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(itemsOrderService.findItemsByOrderId(id, pageable));
    }

    @PostMapping("/{orderId}")
    public ResponseEntity<ItemsOrder> createNewItemOrder(
            @PathVariable Long orderId,
            @RequestBody ItemsOrder itemsOrder
    ){
        return new ResponseEntity<>(itemsOrderService.createNewItemOrder(orderId, itemsOrder), HttpStatus.CREATED);
    }

    @DeleteMapping("/{orderId}/{itemId}")
    public ResponseEntity<Void> deleteItem(@PathVariable Long orderId, @PathVariable Long itemId){
        itemsOrderService.deleteItem(orderId, itemId);
        return ResponseEntity.noContent().build();
    }

}
