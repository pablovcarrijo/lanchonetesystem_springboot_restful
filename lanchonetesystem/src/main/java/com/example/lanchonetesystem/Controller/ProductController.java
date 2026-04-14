package com.example.lanchonetesystem.Controller;

import com.example.lanchonetesystem.Services.ProductService;
import com.example.lanchonetesystem.domain.Product;
import org.apache.coyote.Response;
import org.aspectj.util.PartialOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.*;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.sound.sampled.Port;
import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("/{id}")
    public ResponseEntity<Product> findById (@PathVariable Long id){
        return ResponseEntity.ok(productService.findById(id));
    }

    @GetMapping("/all")
    public ResponseEntity<PagedModel<EntityModel<Product>>> findAll(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "5") Integer size,
            @RequestParam(value = "direction", defaultValue = "asc") String direction
    ){

        Direction sortDirection = "desc".equalsIgnoreCase(direction) ? Direction.DESC : Direction.ASC;

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, "name"));

        return ResponseEntity.ok(productService.findAll(pageable));
    }

    @GetMapping("/findByName/{name}")
    public ResponseEntity<PagedModel<EntityModel<Product>>> findByName(
            @PathVariable String name,
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size,
            @RequestParam(value = "direction", defaultValue = "asc") String direction
    ){

        Direction sortDirection = "desc".equalsIgnoreCase(direction) ? Direction.DESC : Direction.ASC;

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, "name"));

        return ResponseEntity.ok(productService.findByName(name, pageable));
    }

    @PostMapping
    public ResponseEntity<Product> saveProduct(@RequestBody Product product){
        return new ResponseEntity<>(productService.saveProduct(product), HttpStatus.CREATED);
    }

    @PostMapping("/productList")
    public ResponseEntity<List<Product>> saveListProduct(@RequestBody List<Product> products){
        return new ResponseEntity<>(productService.saveListProduct(products), HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<Product> updateProduct(@RequestBody Product product){
        return new ResponseEntity<>(productService.updateProduct(product), HttpStatus.CREATED);
    }

    @PatchMapping("/priceUpdate/{id}")
    public ResponseEntity<Product> updatePrice(@PathVariable Long id, @RequestParam(value = "newPrice") Double newPrice){
        return ResponseEntity.ok(productService.updatePrice(newPrice, id));
    }

    @PatchMapping("/availableUpdate/{id}")
    public ResponseEntity<Product> updateAvailable(@PathVariable Long id, @RequestParam(value = "updateBool", defaultValue = "true") boolean updateBool){
        return ResponseEntity.ok(productService.updateAvailable(id, updateBool));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id){
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

}
