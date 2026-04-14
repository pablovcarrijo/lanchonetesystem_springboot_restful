package com.example.lanchonetesystem.Controller;

import com.example.lanchonetesystem.Services.ClientService;
import com.example.lanchonetesystem.domain.Client;
import com.sun.net.httpserver.HttpsServer;
import jakarta.websocket.server.PathParam;
import org.apache.coyote.Response;
import org.hibernate.query.SortDirection;
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

import java.util.List;

@RestController
@RequestMapping("/client")
public class ClientController {

    @Autowired
    private ClientService clientService;

    @GetMapping("/all")
    public ResponseEntity<PagedModel<EntityModel<Client>>> findAll(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "6") Integer size,
            @RequestParam(value = "direction", defaultValue = "asc") String direction
    ){
        Direction sortDirection = "desc".equalsIgnoreCase(direction) ? Direction.DESC : Direction.ASC;

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, "name"));

        return ResponseEntity.ok(clientService.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Client> findClientById(@PathVariable Long id){
        return ResponseEntity.ok(clientService.findClientById(id));
    }

    @GetMapping("/findByName/{name}")
    public ResponseEntity<PagedModel<EntityModel<Client>>> findByName(
            @PathVariable String name,
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size,
            @RequestParam(value = "direction", defaultValue = "asc") String direction
    ){

        Direction sortDirection = "desc".equalsIgnoreCase(direction) ? Direction.DESC : Direction.ASC;

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, "name"));

        return ResponseEntity.ok(clientService.findByName(name, pageable));

    }

    @PostMapping
    public ResponseEntity<Client> saveClient(@RequestBody Client client){
        return new ResponseEntity<>(clientService.saveClient(client), HttpStatus.CREATED);
    }

    @PostMapping("/saveList")
    public ResponseEntity<List<Client>> saveListClient(@RequestBody List<Client> clients){
        return new ResponseEntity<>(clientService.saveListClient(clients), HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<Client> updateClient(@RequestBody Client client){
        return new ResponseEntity<>(clientService.updateClient(client), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClient(@PathVariable Long id){
        clientService.deleteClient(id);
        return ResponseEntity.noContent().build();
    }


}
