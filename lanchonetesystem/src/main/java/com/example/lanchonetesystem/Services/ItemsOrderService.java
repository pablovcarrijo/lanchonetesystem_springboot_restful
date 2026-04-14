package com.example.lanchonetesystem.Services;

import com.example.lanchonetesystem.Repository.ItemsOrderRepository;
import com.example.lanchonetesystem.Repository.OrderRepository;
import com.example.lanchonetesystem.domain.FullOrder;
import com.example.lanchonetesystem.domain.ItemsOrder;
import com.example.lanchonetesystem.domain.Product;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ItemsOrderService {

    @Autowired
    private ItemsOrderRepository itemsOrderRepository;

    @Autowired
    private ProductService productService;

    @Autowired
    private OrderRepository orderRepository;

    public Page<ItemsOrder> findItemsByOrderId(Long id, Pageable pageable){
        return itemsOrderRepository.findByFullOrderId(id, pageable);
    }

    public ItemsOrder createNewItemOrder(Long orderId, ItemsOrder itemsOrder){
        FullOrder fullOrder = orderRepository.findById(orderId).orElseThrow(() -> new RuntimeException("Order not found"));
        Product product = productService.findById(itemsOrder.getProductId());
        itemsOrder.setUnityPrice(product.getPrice());
        itemsOrder.setFullOrderId(orderId);
        fullOrder.setTotal_value(fullOrder.getTotal_value() + (itemsOrder.getQuantity() * itemsOrder.getUnityPrice()));
        return itemsOrderRepository.save(itemsOrder);
    }

    @Transactional
    public void deleteItem(Long orderId, Long itemId){
        FullOrder order = orderRepository.findById(orderId).orElseThrow(() -> new RuntimeException("Order not found"));
        ItemsOrder item = itemsOrderRepository.findById(itemId).orElseThrow(() -> new RuntimeException("Item not found"));

        if(!item.getFullOrderId().equals(order.getId())){
            throw new RuntimeException("Error, item doesn't belong to the order");
        }

        double itemValue = item.getQuantity() * item.getUnityPrice();
        order.setTotal_value(order.getTotal_value() - itemValue);

        orderRepository.save(order);
        itemsOrderRepository.deleteById(itemId);
    }

    @Transactional
    public void deleteItem(Long itemId){
        itemsOrderRepository.deleteById(itemId);

    }

}
