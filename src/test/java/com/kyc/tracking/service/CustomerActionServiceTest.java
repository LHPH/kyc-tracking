package com.kyc.tracking.service;

import com.kyc.core.exception.KycRestException;
import com.kyc.core.model.web.MessageData;
import com.kyc.core.model.web.RequestData;
import com.kyc.core.model.web.ResponseData;
import com.kyc.core.properties.KycMessages;
import com.kyc.tracking.documents.CustomerActionDocument;
import com.kyc.tracking.mappers.CustomerActionMapper;
import com.kyc.tracking.model.CustomerAction;
import com.kyc.tracking.repository.MongoBsonOperationRepository;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.InvalidMongoDbApiUsageException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.kyc.tracking.constants.AppConstants.MESSAGE_003;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CustomerActionServiceTest {

    @Mock
    private MongoBsonOperationRepository mongoBsonOperationRepository;

    @Mock
    private CustomerActionMapper customerActionMapper;

    @Mock
    private KycMessages kycMessages;

    @InjectMocks
    private CustomerActionService service;


    @Test
    public void saveAction_savingAction_actionWasSaved(){

        RequestData<CustomerAction> req = RequestData.<CustomerAction>builder()
                .body(new CustomerAction())
                .build();

        CustomerActionDocument doc = new CustomerActionDocument();
        doc.setId(new ObjectId());

        when(customerActionMapper.toDocument(any(CustomerAction.class)))
                .thenReturn(new CustomerActionDocument());
        when(mongoBsonOperationRepository.saveCustomerAction(any(CustomerActionDocument.class)))
                .thenReturn(Mono.just(doc));

        Mono<ResponseData<Boolean>> monoResult = service.saveAction(req);
        StepVerifier.create(monoResult)
                .consumeNextWith(result -> Assertions.assertTrue(result.getData()))
                .verifyComplete();
    }

    @Test
    public void saveAction_saveActionButError_returnKycRestException(){

        RequestData<CustomerAction> req = RequestData.<CustomerAction>builder()
                .body(new CustomerAction())
                .build();

        CustomerActionDocument doc = new CustomerActionDocument();
        doc.setId(new ObjectId());

        when(customerActionMapper.toDocument(any(CustomerAction.class)))
                .thenReturn(new CustomerActionDocument());
        when(mongoBsonOperationRepository.saveCustomerAction(any(CustomerActionDocument.class)))
                .thenReturn(Mono.error(new InvalidMongoDbApiUsageException("error mongo")));
        when(kycMessages.getMessage(MESSAGE_003)).thenReturn(new MessageData());

        Mono<ResponseData<Boolean>> monoResult = service.saveAction(req);
        StepVerifier.create(monoResult)
                .expectErrorMatches(exc -> exc instanceof KycRestException)
                .verify();
    }

    @Test
    public void getActions_gettingActions_returnSavedActions(){

        Map<String,String> params = new HashMap<>();
        params.put("customerNumber","1");
        params.put("channel","1");
        params.put("trackId","23");
        params.put("param","param");

        RequestData<Void> request = RequestData.<Void>builder()
                .queryParams(params)
                .build();

        CustomerActionDocument doc = new CustomerActionDocument();
        doc.setId(new ObjectId());

        when(mongoBsonOperationRepository.getCustomerActions(anyLong(),anyInt(),anyString(),anyString(),anyInt()))
                .thenReturn(Flux.just(doc));
        when(customerActionMapper.toModel(any(CustomerActionDocument.class)))
                .thenReturn(new CustomerAction());

        Mono<ResponseData<List<CustomerAction>>> monoResult = service.getActions(request);
        StepVerifier.create(monoResult)
                .consumeNextWith(result -> Assertions.assertFalse(result.getData().isEmpty()))
                .verifyComplete();
    }

    @Test
    public void getActions_gettingActionsButError_returnKycRestException(){

        Map<String,String> params = new HashMap<>();
        params.put("customerNumber","1");
        params.put("page","3");

        RequestData<Void> request = RequestData.<Void>builder()
                .queryParams(params)
                .build();

        CustomerActionDocument doc = new CustomerActionDocument();
        doc.setId(new ObjectId());

        when(mongoBsonOperationRepository.getCustomerActions(anyLong(),eq(null),eq(null),eq(null),anyInt()))
                .thenReturn(Flux.error(new InvalidMongoDbApiUsageException("mongo error")));
        when(kycMessages.getMessage(MESSAGE_003))
                .thenReturn(new MessageData());


        Mono<ResponseData<List<CustomerAction>>> monoResult = service.getActions(request);
        StepVerifier.create(monoResult)
                .expectErrorMatches(exc -> exc instanceof KycRestException)
                .verify();
    }

}
