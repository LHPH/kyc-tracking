package com.kyc.tracking.service;

import com.kyc.core.exception.KycRestException;
import com.kyc.core.model.web.MessageData;
import com.kyc.core.model.web.RequestData;
import com.kyc.core.model.web.ResponseData;
import com.kyc.core.properties.KycMessages;
import com.kyc.tracking.documents.ExecutiveActionDocument;
import com.kyc.tracking.documents.ExecutiveTrackDocument;
import com.kyc.tracking.mappers.ExecutiveActionMapper;
import com.kyc.tracking.mappers.ExecutiveTrackMapper;
import com.kyc.tracking.model.ExecutiveAction;
import com.kyc.tracking.model.ExecutiveTrackInfo;
import com.kyc.tracking.repository.ExecutiveTrackRepository;
import com.kyc.tracking.repository.MongoBsonOperationRepository;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.mongodb.InvalidMongoDbApiUsageException;
import org.springframework.http.HttpStatus;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.kyc.tracking.constants.AppConstants.MESSAGE_002;
import static com.kyc.tracking.constants.AppConstants.MESSAGE_003;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ExecutiveTrackingServiceTest {

    @Mock
    private ExecutiveTrackRepository executiveTrackRepository;

    @Mock
    private MongoBsonOperationRepository mongoBsonOperationRepository;

    @Mock
    private ExecutiveTrackMapper executiveTrackMapper;

    @Mock
    private ExecutiveActionMapper executiveActionMapper;

    @Mock
    private KycMessages kycMessages;

    @InjectMocks
    private ExecutiveTrackingService service;

    @Test
    public void getExecutiveTrack_getTracking_returningTrackingInfo(){

        Map<String,String> params = new HashMap<>();
        params.put("idExecutive","1");
        params.put("idOffice","2");
        params.put("startDate","2023-10-10");
        params.put("endDate","2023-10-11");
        RequestData<Void> req = RequestData.<Void>builder()
                .queryParams(params)
                .build();

        when(mongoBsonOperationRepository.getExecutiveInfo(anyInt(),anyInt(),any(Date.class),any(Date.class)))
                .thenReturn(Flux.just(new ExecutiveTrackDocument()));
        when(executiveTrackMapper.toModel(any(ExecutiveTrackDocument.class)))
                .thenReturn(new ExecutiveTrackInfo());

        Mono<ResponseData<List<ExecutiveTrackInfo>>> monoResult = service.getExecutiveTrack(req);

        StepVerifier.create(monoResult)
                .consumeNextWith(result -> Assertions.assertNotNull(result.getData()))
                .verifyComplete();
    }

    @Test
    public void getExecutiveTrack_getTrackingWithAllParams_returningTrackingInfo(){

        Map<String,String> params = new HashMap<>();
        params.put("idExecutive","1");
        params.put("idOffice","2");
        RequestData<Void> req = RequestData.<Void>builder()
                .queryParams(params)
                .build();

        when(mongoBsonOperationRepository.getExecutiveInfo(anyInt(),anyInt(),eq(null),eq(null)))
                .thenReturn(Flux.just(new ExecutiveTrackDocument()));
        when(executiveTrackMapper.toModel(any(ExecutiveTrackDocument.class)))
                .thenReturn(new ExecutiveTrackInfo());

        Mono<ResponseData<List<ExecutiveTrackInfo>>> monoResult = service.getExecutiveTrack(req);

        StepVerifier.create(monoResult)
                .consumeNextWith(result -> Assertions.assertNotNull(result.getData()))
                .verifyComplete();
    }

    @Test
    public void getExecutiveTrack_getTrackingButDatabaseError_returnKycRestException(){

        Map<String,String> params = new HashMap<>();
        params.put("idExecutive","1");
        params.put("idOffice","2");
        params.put("startDate","2023-11-10");
        params.put("endDate","2023-10-11");
        RequestData<Void> req = RequestData.<Void>builder()
                .queryParams(params)
                .build();

        when(mongoBsonOperationRepository.getExecutiveInfo(anyInt(),anyInt(),eq(null),eq(null)))
                .thenReturn(Flux.error(new InvalidMongoDbApiUsageException("error mongo")));
        when(kycMessages.getMessage(MESSAGE_003)).thenReturn(new MessageData());

        Mono<ResponseData<List<ExecutiveTrackInfo>>> monoResult = service.getExecutiveTrack(req);

        StepVerifier.create(monoResult)
                .expectErrorMatches(exc -> exc instanceof KycRestException)
                .verify();
    }

    @Test
    public void executiveTrackInfo_saveTrackInfo_infoWasSaved(){

        RequestData<ExecutiveTrackInfo> req = RequestData.<ExecutiveTrackInfo>builder()
                .body(new ExecutiveTrackInfo())
                .build();

        ExecutiveTrackDocument doc = new ExecutiveTrackDocument();
        doc.setId(new ObjectId());

        when(executiveTrackMapper.toDocument(any(ExecutiveTrackInfo.class)))
                .thenReturn(new ExecutiveTrackDocument());
        when(executiveTrackRepository.save(any(ExecutiveTrackDocument.class)))
                .thenReturn(Mono.just(doc));

        Mono<ResponseData<String>> monoResult = service.executiveTrackInfo(req);

        StepVerifier.create(monoResult)
                .consumeNextWith(result -> Assertions.assertEquals(doc.getId().toHexString(),result.getData()))
                .verifyComplete();
    }

    @Test
    public void executiveTrackInfo_saveTrackInfoButMongoError_returnKycRestException(){

        RequestData<ExecutiveTrackInfo> req = RequestData.<ExecutiveTrackInfo>builder()
                .body(new ExecutiveTrackInfo())
                .build();

        ExecutiveTrackDocument doc = new ExecutiveTrackDocument();
        doc.setId(new ObjectId());

        when(executiveTrackMapper.toDocument(any(ExecutiveTrackInfo.class)))
                .thenReturn(new ExecutiveTrackDocument());
        when(executiveTrackRepository.save(any(ExecutiveTrackDocument.class)))
                .thenReturn(Mono.error(new InvalidMongoDbApiUsageException("error mongo")));
        when(kycMessages.getMessage(MESSAGE_003)).thenReturn(new MessageData());

        Mono<ResponseData<String>> monoResult = service.executiveTrackInfo(req);

        StepVerifier.create(monoResult)
                .expectErrorMatches(exc -> exc instanceof KycRestException)
                .verify();
    }

    @Test
    public void executiveTrackAction_saveTrackAction_actionWasSaved(){

        RequestData<ExecutiveAction> req = RequestData.<ExecutiveAction>builder()
                .pathParams(Collections.singletonMap("id","1"))
                .body(new ExecutiveAction())
                .build();

        ExecutiveTrackDocument doc = new ExecutiveTrackDocument();
        doc.setActions(new ArrayList<>());
        when(executiveTrackRepository.findById(anyString()))
                .thenReturn(Mono.just(doc));
        when(executiveActionMapper.toNestedDocument(any(ExecutiveAction.class)))
                .thenReturn(new ExecutiveActionDocument());
        when(executiveTrackRepository.addAction(anyString(),any(ExecutiveActionDocument.class)))
                .thenReturn(Mono.just(1L));

        Mono<ResponseData<Boolean>> monoResult = service.executiveTrackAction(req);

        StepVerifier.create(monoResult)
                .consumeNextWith(result -> Assertions.assertTrue(result.getData()))
                .verifyComplete();
    }

    @Test
    public void executiveTrackAction_saveTrackActionButUnknownId_returnKycRestException(){

        RequestData<ExecutiveAction> req = RequestData.<ExecutiveAction>builder()
                .pathParams(Collections.singletonMap("id","1"))
                .body(new ExecutiveAction())
                .build();

        ExecutiveTrackDocument doc = new ExecutiveTrackDocument();
        doc.setActions(new ArrayList<>());
        when(executiveTrackRepository.findById(anyString()))
                .thenReturn(Mono.empty());
        when(kycMessages.getMessage(MESSAGE_002)).thenReturn(new MessageData());

        Mono<ResponseData<Boolean>> monoResult = service.executiveTrackAction(req);

        StepVerifier.create(monoResult)
                .expectErrorMatches(exc -> {
                    if(exc instanceof KycRestException){
                        KycRestException kycExc = (KycRestException)exc;
                        return HttpStatus.UNPROCESSABLE_ENTITY.equals(kycExc.getStatus());
                    }
                    return false;
                } )
                .verify();
    }

    @Test
    public void executiveTrackAction_saveTrackActionButMongoError_returnKycRestException(){

        RequestData<ExecutiveAction> req = RequestData.<ExecutiveAction>builder()
                .pathParams(Collections.singletonMap("id","1"))
                .body(new ExecutiveAction())
                .build();

        ExecutiveTrackDocument doc = new ExecutiveTrackDocument();
        doc.setActions(new ArrayList<>());
        when(executiveTrackRepository.findById(anyString()))
                .thenReturn(Mono.error(new InvalidMongoDbApiUsageException("test db error")));
        when(kycMessages.getMessage(MESSAGE_003)).thenReturn(new MessageData());

        Mono<ResponseData<Boolean>> monoResult = service.executiveTrackAction(req);

        StepVerifier.create(monoResult)
                .expectErrorMatches(exc -> {
                    if(exc instanceof KycRestException){
                        KycRestException kycExc = (KycRestException)exc;
                        return HttpStatus.SERVICE_UNAVAILABLE.equals(kycExc.getStatus());
                    }
                    return false;
                } )
                .verify();
    }
}
