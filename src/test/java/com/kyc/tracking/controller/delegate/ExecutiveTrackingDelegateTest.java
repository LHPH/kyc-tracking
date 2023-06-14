package com.kyc.tracking.controller.delegate;

import com.kyc.core.model.web.RequestData;
import com.kyc.core.model.web.ResponseData;
import com.kyc.tracking.controllers.delegate.ExecutiveTrackingDelegate;
import com.kyc.tracking.model.ExecutiveAction;
import com.kyc.tracking.model.ExecutiveTrackInfo;
import com.kyc.tracking.service.ExecutiveTrackingService;
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
public class ExecutiveTrackingDelegateTest {

    @Mock
    private ExecutiveTrackingService service;

    @InjectMocks
    private ExecutiveTrackingDelegate delegate;


    @Test
    public void getExecutiveTrackInfo_passData_returnMono(){

        when(service.getExecutiveTrack(any(RequestData.class)))
                .thenReturn(Mono.just(ResponseData.of(new ArrayList<>())));

        Mono<ResponseData<List<ExecutiveTrackInfo>>> monoResult = delegate.getExecutiveTrackInfo(RequestData.<Void>builder().build());
        StepVerifier.create(monoResult)
                .consumeNextWith(result -> Assertions.assertNotNull(result.getData()))
                .verifyComplete();
    }

    @Test
    public void executiveTrackInfo_passData_returnMono(){

        when(service.executiveTrackInfo(any(RequestData.class)))
                .thenReturn(Mono.just(ResponseData.of("id")));

        Mono<ResponseData<String>> monoResult = delegate.executiveTrackInfo(RequestData.<ExecutiveTrackInfo>builder().build());
        StepVerifier.create(monoResult)
                .consumeNextWith(result -> Assertions.assertNotNull(result.getData()))
                .verifyComplete();
    }

    @Test
    public void executiveTrackAction_passData_returnMono(){

        when(service.executiveTrackAction(any(RequestData.class)))
                .thenReturn(Mono.just(ResponseData.of(true)));

        Mono<ResponseData<Boolean>> monoResult = delegate.executiveTrackAction(RequestData.<ExecutiveAction>builder().build());
        StepVerifier.create(monoResult)
                .consumeNextWith(result -> Assertions.assertTrue(result.getData()))
                .verifyComplete();
    }
}
