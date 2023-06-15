package com.kyc.tracking.repository;

import com.kyc.tracking.documents.CustomerActionDocument;
import com.kyc.tracking.documents.ExecutiveTrackDocument;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Date;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MongoBsonOperationRepositoryTest {

    @Mock
    private ReactiveMongoTemplate reactiveMongoTemplate;

    @InjectMocks
    private MongoBsonOperationRepository repository;

    @Test
    public void getExecutiveInfo_executeQueryWithAllParams_returnFluxResult(){


        when(reactiveMongoTemplate.find(any(Query.class),any(),eq("executives_track")))
                .thenReturn(Flux.just(new ExecutiveTrackDocument()));

        Flux<ExecutiveTrackDocument> result =  repository.getExecutiveInfo(1,1,new Date(), new Date());
        Assertions.assertNotNull(result);
    }

    @Test
    public void getExecutiveInfo_executeQueryWithNullParams_returnFluxResult(){

        when(reactiveMongoTemplate.find(any(Query.class),any(),eq("executives_track")))
                .thenReturn(Flux.just(new ExecutiveTrackDocument()));

        Flux<ExecutiveTrackDocument> result =  repository.getExecutiveInfo(null,null,null, null);
        Assertions.assertNotNull(result);
    }

    @Test
    public void saveCustomerAction_saveCustomerAction_returnMonoResult(){

        when(reactiveMongoTemplate.save(any(CustomerActionDocument.class),eq("customers_track")))
                .thenReturn(Mono.just(new CustomerActionDocument()));

        Mono<CustomerActionDocument> result = repository.saveCustomerAction(new CustomerActionDocument());
        Assertions.assertNotNull(result);
    }

    @Test
    public void getCustomerActions_getCustomerActionWithAllParams_returnFluxResult(){

        when(reactiveMongoTemplate.find(any(Query.class),any(),eq("customers_track")))
                .thenReturn(Flux.just(new CustomerActionDocument()));

        Flux<CustomerActionDocument> result = repository.getCustomerActions(1L,1,"1","param",0);
        Assertions.assertNotNull(result);
    }

    @Test
    public void getCustomerActions_getCustomerActionWithNullParams_returnFluxResult(){

        when(reactiveMongoTemplate.find(any(Query.class),any(),eq("customers_track")))
                .thenReturn(Flux.just(new CustomerActionDocument()));

        Flux<CustomerActionDocument> result = repository.getCustomerActions(null,null,null,null,0);
        Assertions.assertNotNull(result);
    }
}
