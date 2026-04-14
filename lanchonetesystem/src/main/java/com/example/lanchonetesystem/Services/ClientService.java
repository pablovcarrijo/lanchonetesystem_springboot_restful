package com.example.lanchonetesystem.Services;

import com.example.lanchonetesystem.Controller.ClientController;
import com.example.lanchonetesystem.Repository.ClientRepository;
import com.example.lanchonetesystem.domain.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Service;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;


import java.util.List;

@Service
public class ClientService {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private PagedResourcesAssembler<Client> assembler;

    public PagedModel<EntityModel<Client>> findAll(Pageable pageable){
        Page<Client> clients = clientRepository.findAll(pageable);

        for(Client client : clients){
            addHateoasLink(client);
        }

        Link selfLinkPages = WebMvcLinkBuilder.linkTo(methodOn(ClientController.class)
                .findAll(pageable.getPageNumber(), pageable.getPageSize(), String.valueOf(pageable.getSort()))).withSelfRel();

        return assembler.toModel(clients, selfLinkPages);
    }

    public PagedModel<EntityModel<Client>> findByName(String name, Pageable pageable){
        Page<Client> clients = clientRepository.findByName(name, pageable);

        for(Client client : clients){
            addHateoasLink(client);
        }

        Link selfLinkPages = WebMvcLinkBuilder.linkTo(methodOn(ClientController.class)
                .findByName(name, pageable.getPageNumber(), pageable.getPageSize(), String.valueOf(pageable.getSort()))).withSelfRel();

        return assembler.toModel(clients, selfLinkPages);
    }

    public Client findClientById(Long id){
        Client client = clientRepository.findById(id).orElseThrow(() -> new RuntimeException("Client not found"));
        addHateoasLink(client);
        return client;
    }

    public Client saveClient(Client client){
        Client clientSaved = clientRepository.save(client);
        addHateoasLink(clientSaved);
        return clientSaved;
    }

    public List<Client> saveListClient(List<Client> clients){
        List<Client> clientsSaved = clientRepository.saveAll(clients);
        for (Client client : clientsSaved){
            addHateoasLink(client);
        }
        return clientsSaved;
    }

    public Client updateClient(Client client){
        Client clientFound = clientRepository.findById(client.getId()).orElseThrow(() -> new RuntimeException("Client not found"));

        clientFound.setEmail(client.getEmail());
        clientFound.setName(client.getName());
        clientFound.setPhone(client.getPhone());
        clientRepository.save(clientFound);

        addHateoasLink(clientFound);
        return clientFound;
    }

    public void deleteClient(Long id){
        clientRepository.findById(id).orElseThrow(() -> new RuntimeException("Client not found"));
        clientRepository.deleteById(id);
    }

    private void addHateoasLink(Client client){
        Long id = client.getId();
        client.add(linkTo(methodOn(ClientController.class).findClientById(id)).withSelfRel().withType("GET"));
        client.add(linkTo(methodOn(ClientController.class).saveClient(client)).withRel("POST").withType("POST"));
        client.add(linkTo(methodOn(ClientController.class).updateClient(client)).withRel("PUT").withType("PUT"));
        client.add(linkTo(methodOn(ClientController.class).deleteClient(id)).withRel("DELETE").withType("DELETE"));
    }

}
