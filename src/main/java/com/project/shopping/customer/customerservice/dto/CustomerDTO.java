package com.project.shopping.customer.customerservice.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class CustomerDTO implements Serializable {

    private long id;
    private String email;
    private String firstName;
    private String lastName;
    private String phone;
    private String address;


}
