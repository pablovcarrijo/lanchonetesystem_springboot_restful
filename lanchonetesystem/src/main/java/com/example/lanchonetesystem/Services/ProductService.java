package com.example.lanchonetesystem.Services;

import com.example.lanchonetesystem.Controller.ProductController;
import com.example.lanchonetesystem.Repository.ProductRepository;
import com.example.lanchonetesystem.domain.Product;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Service;

import javax.swing.text.StyledEditorKit;
import java.util.List;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private PagedResourcesAssembler assembler;

    public Product findById(Long id){
        return productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
    }

    public PagedModel<EntityModel<Product>> findAll(Pageable pageable){

        Page<Product> products = productRepository.findAll(pageable);

        for(Product product : products){
            addLinkHateoas(product);
        }

        Link selfLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ProductController.class)
                .findAll(pageable.getPageNumber(), pageable.getPageSize(), String.valueOf(pageable.getSort()))).withSelfRel();

        return assembler.toModel(products, selfLink);
    }

    public PagedModel<EntityModel<Product>> findByName(String name, Pageable pageable){

        Page<Product> products = productRepository.findByName(name, pageable);

        for(Product prod : products){
            addLinkHateoas(prod);
        }

        Link selfLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ProductController.class)
                .findByName(name, pageable.getPageNumber(), pageable.getPageSize(), String.valueOf(pageable.getSort()))).withSelfRel();

        return assembler.toModel(products, selfLink);

    }

    public Product saveProduct (Product product){
        Product prod = productRepository.save(product);
        addLinkHateoas(prod);
        return prod;
    }

    public List<Product> saveListProduct (List<Product> products){

        List<Product> prodList = productRepository.saveAll(products);

        for(Product product : prodList){
            addLinkHateoas(product);
        }

        return prodList;
    }

    public Product updateProduct (Product product){
        Product prod = productRepository.findById(product.getId()).orElseThrow(() -> new RuntimeException("Product not found"));

        prod.setAvailable(product.getAvailable());
        prod.setDescription(product.getDescription());
        prod.setName(product.getName());
        prod.setPrice(product.getPrice());

        Product savedProduct = productRepository.save(prod);
        addLinkHateoas(savedProduct);

        return prod;
    }

    public void deleteProduct(Long id){
        productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
        productRepository.deleteById(id);
    }

    @Transactional
    public Product updatePrice(Double newPrice, Long id){

        productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
        int n = productRepository.updatePrice(newPrice, id);
        if(n == 0){
            throw new RuntimeException("Couldn't update price");
        }

        Product product = productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
        addLinkHateoas(product);
        return product;

    }

    @Transactional
    public Product updateAvailable(Long id, boolean value){
        productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
        int n = productRepository.updateAvailable(id, value);
        if(n == 0){
            throw new RuntimeException("Couldn't update available");
        }

        Product product = productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
        addLinkHateoas(product);
        return product;
    }

    private void addLinkHateoas(Product product){
        Long id = product.getId();
        product.add(linkTo(methodOn(ProductController.class).findById(id)).withRel("GET").withSelfRel());
        product.add(linkTo(methodOn(ProductController.class).saveProduct(product)).withRel("POST").withType("POST"));
        product.add(linkTo(methodOn(ProductController.class).updateProduct(product)).withRel("PUT").withType("PUT"));
        product.add(linkTo(methodOn(ProductController.class).deleteProduct(id)).withRel("DELETE").withType("DELETE"));
    }

}
