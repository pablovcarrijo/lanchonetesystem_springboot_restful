package com.example.lanchonetesystem.domain;

import jakarta.persistence.*;
import org.springframework.hateoas.RepresentationModel;

@Entity
@Table(name = "full_order")
public class FullOrder extends RepresentationModel<FullOrder> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long client_id;

    @Enumerated(EnumType.STRING)
    private Status status;
    private Double total_value;

    public FullOrder() {}

    public FullOrder(Long client_id, Status status, Double total_value) {
        this.client_id = client_id;
        this.status = status;
        this.total_value = total_value;
    }

    public FullOrder(Long client_id) {
        this.client_id = client_id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getClient_id() {
        return client_id;
    }

    public void setClient_id(Long client_id) {
        this.client_id = client_id;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Double getTotal_value() {
        return total_value;
    }

    public void setTotal_value(Double total_value) {
        this.total_value = total_value;
    }
}
