package com.project.shopping.customer.customerservice.service;

import com.google.gson.Gson;
import com.project.shopping.customer.customerservice.dto.CustomerDTO;
import com.project.shopping.customer.customerservice.model.Customer;
import com.project.shopping.customer.customerservice.repository.CustomerRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Transactional
@Service
public class CustomerService {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public List<CustomerDTO> getAllCustomers(){
        return customerRepository.findAll().stream().map(
                c-> modelMapper.map(c, CustomerDTO.class)
        ).collect(Collectors.toList());
    }

    public CustomerDTO saveCustomer(CustomerDTO customerDTO){
        Customer customer = customerRepository.save(modelMapper.map(customerDTO, Customer.class));
        CustomerDTO result = modelMapper.map(customer, CustomerDTO.class);
        kafkaTemplate.send("CustomerCreated", "CustomerCreated", new Gson().toJson(result));
        return result;
    }

    public CustomerDTO getCustomerById(long customerId){
        Optional<Customer> customerResult = customerRepository.findById(customerId);
        if(!customerResult.isPresent()){
            return null;
        }
        return modelMapper.map(customerResult.get(),CustomerDTO.class);
    }

    public void deleteCustomer(long customerId){
        customerRepository.deleteById(customerId);
    }

}
