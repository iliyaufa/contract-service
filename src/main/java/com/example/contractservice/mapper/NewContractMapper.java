package com.example.contractservice.mapper;

import com.example.contractservice.model.ContractualParty;
import com.example.contractservice.model.CreateNewContract;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import com.example.contractservice.model.CreateNewContractRequest;

import javax.xml.datatype.XMLGregorianCalendar;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface NewContractMapper {

    default List<ContractualParty> map(CreateNewContractRequest.ContractualParties values) {
        List<ContractualParty> partyList = new ArrayList<>();
        if (!values.getContractualParty().isEmpty()) {
            partyList = values.getContractualParty().stream().map(c -> toContractualParty(c)).collect(Collectors.toList());
        }
        return partyList;
    }

    @Mapping(target = "bankAccountNumber", source = "bankAccount")
    @Mapping(target = "bik", source = "bankBik")
    ContractualParty toContractualParty(CreateNewContractRequest.ContractualParties.ContractualParty source);

    @Mappings({
            @Mapping(target = "dateStart",dateFormat = "yyyy-MM-dd"),
            @Mapping(target = "dateEnd",dateFormat = "yyyy-MM-dd"),
            @Mapping(target = "dateSend", expression = "java(java.time.LocalDateTime.now())",dateFormat = "yyyy-MM-ddTHH:mm:ss"),
            @Mapping(target = "clientApi", constant = "SOAP")
    })
    CreateNewContract toNewContract(CreateNewContractRequest request);
}
