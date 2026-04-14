package com.example.lanchonetesystem.Services;

import com.example.lanchonetesystem.Controller.ItemsOrderController;
import com.example.lanchonetesystem.Controller.OrderController;
import com.example.lanchonetesystem.Repository.ItemsOrderRepository;
import com.example.lanchonetesystem.Repository.OrderRepository;
import com.example.lanchonetesystem.domain.FullOrder;
import com.example.lanchonetesystem.domain.ItemsOrder;
import com.example.lanchonetesystem.domain.Status;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ItemsOrderService itemsOrderService;

    @Autowired
    private ItemsOrderRepository itemsOrderRepository;

    @Autowired
    private PagedResourcesAssembler assembler;

    public FullOrder findById(Long id){
        FullOrder order = orderRepository.findById(id).orElseThrow(() -> new RuntimeException("Order not found"));
        addHateoasLink(order);
        return order;
    }

    public PagedModel<EntityModel<FullOrder>> findAll(Pageable pageable){
        Page<FullOrder> orders = orderRepository.findAll(pageable);

        for(FullOrder order : orders){
            addHateoasLink(order, pageable);
        }

        Link selfLink = linkTo(methodOn(OrderController.class)
                .findAll(pageable.getPageNumber(), pageable.getPageSize())).withSelfRel();

        return assembler.toModel(orders, selfLink);
    }

    public PagedModel<EntityModel<FullOrder>> findByStatus(Status statusConsult, Pageable pageable){

        Page<FullOrder> orders = orderRepository.findByStatus(statusConsult, pageable);

        for(FullOrder  order : orders){
            addHateoasLink(order, pageable);
        }

        Link selfLink = linkTo(methodOn(OrderController.class)
                .findByStatus(statusConsult, pageable.getPageNumber(), pageable.getPageSize())).withSelfRel();

        return assembler.toModel(orders, selfLink);
    }

    public FullOrder createOneOrder(FullOrder fullOrder){
        fullOrder.setStatus(Status.RECEBIDO);
        fullOrder.setTotal_value(0.0);
        return orderRepository.save(fullOrder);
    }

    @Transactional
    public FullOrder updateStatus(Long orderId, Status newStatus){
        FullOrder orderVerify = orderRepository.findById(orderId).orElseThrow(() -> new RuntimeException("Order not found"));

        if(orderVerify.getStatus().equals(Status.CANCELADO)){
            throw new RuntimeException("Failed to update order status, one canceled order can't change");
        }

        int n = orderRepository.updateStatus(orderId, newStatus);

        if(n == 0){
            throw new RuntimeException("Failed to update order status");
        }

        FullOrder order = orderRepository.findById(orderId).orElseThrow(() -> new RuntimeException("Order not found"));
        addHateoasLink(order);
        return order;
    }

    @Transactional
    public void deleteOrder(Long id) {
        orderRepository.findById(id).orElseThrow(() -> new RuntimeException("Order not found"));

        List<ItemsOrder> items = itemsOrderRepository.findByFullOrderId(id);

        for (ItemsOrder item : items) {
            itemsOrderService.deleteItem(item.getId());
        }

        orderRepository.deleteById(id);
    }

    private void addHateoasLink(FullOrder order){
        Long id = order.getId();

        order.add(linkTo(methodOn(OrderController.class).findById(id)).withSelfRel());
        order.add(linkTo(methodOn(OrderController.class).updateStatus(id, order.getStatus())).withRel("PATCH").withType("PATCH"));
        order.add(linkTo(methodOn(OrderController.class).deleteOrder(id)).withRel("DELETE").withType("DELETE"));

    }

    private void addHateoasLink(FullOrder order, Pageable pageable){

        addHateoasLink(order);

        order.add(linkTo(methodOn(ItemsOrderController.class)
                .findItemsByOrderId(order.getId(), pageable.getPageNumber(), pageable.getPageSize())).withRel("Items").withType("GET"));

    }

}
