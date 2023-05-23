package com.kyc.tracking.controller.delegate;

import com.kyc.core.model.web.RequestData;
import com.kyc.core.model.web.ResponseData;
import com.kyc.tracking.controllers.delegate.CustomerActionDelegate;
import com.kyc.tracking.model.CustomerAction;
import com.kyc.tracking.service.CustomerActionService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CustomerActionDelegateTest {

    @Mock
    private CustomerActionService customerActionService;

    @InjectMocks
    private CustomerActionDelegate delegate;

    @Test
    public void saveAction_passData_returnMono(){

        when(customerActionService.saveAction(any(RequestData.class)))
                .thenReturn(Mono.just(ResponseData.of(true)));

        Mono<ResponseData<Boolean>> monoResult =  delegate.saveAction(RequestData.<CustomerAction>builder().build());
        StepVerifier.create(monoResult)
                .consumeNextWith(result -> Assertions.assertTrue(result.getData()))
                .verifyComplete();
    }

    @Test
    public void getActions_passData_returnMono(){

        when(customerActionService.getActions(any(RequestData.class)))
                .thenReturn(Mono.just(ResponseData.of(new ArrayList<>())));

        Mono<ResponseData<List<CustomerAction>>> monoResult =  delegate.getActions(RequestData.<Void>builder().build());
        StepVerifier.create(monoResult)
                .consumeNextWith(result -> Assertions.assertNotNull(result.getData()))
                .verifyComplete();
    }
}
