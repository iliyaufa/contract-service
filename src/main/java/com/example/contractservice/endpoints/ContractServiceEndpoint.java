package com.example.contractservice.endpoints;

import com.example.contractservice.mapper.NewContractMapper;
import com.example.contractservice.model.ContractStatus;
import com.example.contractservice.model.CreateNewContract;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import com.example.contractservice.model.CreateNewContractRequest;
import com.example.contractservice.model.CreateNewContractResponse;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

@Endpoint
@Slf4j
public class ContractServiceEndpoint {
    private static final String NAMESPACE_URI = "http://xmlns.esb.ru/ext/ContractService/";

    private final RabbitTemplate rabbitTemplate;

    private final NewContractMapper newContractMapperImpl;

    @Value("${spring.amqp.core.queue.name}")
    private String qName;

    @Autowired
    public ContractServiceEndpoint(NewContractMapper newContractMapperImpl, RabbitTemplate rabbitTemplate){
        this.newContractMapperImpl = newContractMapperImpl;
        this.rabbitTemplate = rabbitTemplate;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "CreateNewContractRequest")
    @ResponsePayload
    public CreateNewContractResponse createContract(@RequestPayload CreateNewContractRequest request) {
        CreateNewContractResponse response = new CreateNewContractResponse();
        CreateNewContract newContract = newContractMapperImpl.toNewContract(request);
        ObjectMapper objectMapper = mapperSetup();
        String message;
        try {
            message = objectMapper.writeValueAsString(newContract);
        } catch (JsonProcessingException e) {
            throw new RuntimeException();
        }
        rabbitTemplate.convertAndSend(qName,message);
        System.out.println(message);
        response.setStatus(ContractStatus.Status.CREATED.value());
        return response;
    }

    private ObjectMapper mapperSetup(){
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.setDateFormat(dateFormat);
        return objectMapper;
    }
}
