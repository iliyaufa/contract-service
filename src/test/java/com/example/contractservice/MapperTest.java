package com.example.contractservice;

import com.example.contractservice.mapper.NewContractMapper;
import com.example.contractservice.model.CreateNewContract;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.esb.xmlns.ext.contractservice.CreateNewContractRequest;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;

@SpringBootTest
public class MapperTest {

    private final NewContractMapper newContractMapperImpl;

    @Autowired
    public MapperTest(NewContractMapper newContractMapperImpl){

        this.newContractMapperImpl = newContractMapperImpl;
    }

    @Test
    void CreateNewContractRequestToCreateNewContract(){
        CreateNewContractRequest source = new CreateNewContractRequest();
        source.setId("48f00da6-8f94-46e4-af27-6a6a19324b1d");
        try {
            source.setDateStart(DatatypeFactory.newInstance().newXMLGregorianCalendar("2022-03-07"));
            source.setDateEnd(DatatypeFactory.newInstance().newXMLGregorianCalendar("2022-03-15"));
        } catch (DatatypeConfigurationException e) {
            throw new RuntimeException();
        }
        source.setContractNumber("X2");
        source.setContractName("Контракт сопровождения");
        CreateNewContractRequest.ContractualParties.ContractualParty companyA = new CreateNewContractRequest.ContractualParties.ContractualParty();
        companyA.setName("Компания A");
        companyA.setBankAccount("40817810099910004312");
        companyA.setBankBik("044525974");
        CreateNewContractRequest.ContractualParties.ContractualParty companyB = new CreateNewContractRequest.ContractualParties.ContractualParty();
        companyA.setName("Компания B");
        companyA.setBankAccount("40817810099910004213");
        companyA.setBankBik("044525974");
        CreateNewContractRequest.ContractualParties contractualParties = new CreateNewContractRequest.ContractualParties();
        contractualParties.getContractualParty().add(companyA);
        contractualParties.getContractualParty().add(companyB);

        CreateNewContract target = newContractMapperImpl.toNewContract(source);

        Assertions.assertNotNull(target);
        Assertions.assertEquals(source.getId(), target.getId());
        Assertions.assertEquals("2022-03-07",target.getDateStart());
        Assertions.assertEquals("2022-03-07", target.getDateEnd());
        Assertions.assertEquals(source.getContractNumber(), target.getContractNumber());
        Assertions.assertEquals(source.getContractName(),target.getContractName());
        Assertions.assertEquals(source.getContractualParties().getContractualParty().get(0), target.getContractualParties().get(0));
    }
}
