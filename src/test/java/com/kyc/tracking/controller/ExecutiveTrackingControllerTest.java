package com.kyc.tracking.controller;

import com.kyc.core.model.web.RequestData;
import com.kyc.core.model.web.ResponseData;
import com.kyc.tracking.controllers.ExecutiveTrackingController;
import com.kyc.tracking.controllers.delegate.ExecutiveTrackingDelegate;
import com.kyc.tracking.model.ExecutiveAction;
import com.kyc.tracking.model.ExecutiveTrackInfo;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.util.DefaultUriBuilderFactory;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebFluxTest(controllers = ExecutiveTrackingController.class)
public class ExecutiveTrackingControllerTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExecutiveTrackingControllerTest.class);

    @Autowired
    private WebTestClient webClient;

    @MockBean
    private ExecutiveTrackingDelegate delegate;

    @Test
    public void getExecutiveTrack_getExecutiveTrack_returnMonoResponse(){

        ExecutiveTrackInfo model = new ExecutiveTrackInfo();
        model.setId(1);

        when(delegate.getExecutiveTrackInfo(any(RequestData.class)))
                .thenReturn(Mono.just(ResponseData.of(Collections.singletonList(model))));

        URI uri = new DefaultUriBuilderFactory("/executives/v1/track")
                .builder()
                .queryParam("id_executive", "1")
                .queryParam("id_office","1000")
                .build();

        webClient.get()
                .uri(uri)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .consumeWith(result -> LOGGER.info("{}",result))
                .jsonPath("$.data[0].id").isEqualTo(model.getId());
    }

    @Test
    public void executiveTrackInfo_savingExecutiveTrackInfo_returnMonoResponse(){

        ExecutiveTrackInfo model = new ExecutiveTrackInfo();
        model.setId(1);
        model.setIdBranch(1);
        model.setIp("0.0.0.0");
        model.setActions(new ArrayList<>());

        ExecutiveAction action = new ExecutiveAction();
        action.setAction("ACTION");
        action.setInfo("INFO");
        action.setDate(new Date());
        action.setPage("PAGE");

        model.getActions().add(action);

        when(delegate.executiveTrackInfo(any(RequestData.class)))
                .thenReturn(Mono.just(ResponseData.of("1")));

        webClient.post()
                .uri("/executives/v1/track")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(model)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .consumeWith(result -> LOGGER.info("{}",result))
                .jsonPath("$.data").isEqualTo("1");
    }

    @Test
    public void executiveTrackAction_addNewExecutiveAction_returnMonoResponse(){

        ExecutiveAction action = new ExecutiveAction();
        action.setAction("ACTION");
        action.setInfo("INFO");
        action.setDate(new Date());
        action.setPage("PAGE");

        when(delegate.executiveTrackAction(any(RequestData.class)))
                .thenReturn(Mono.just(ResponseData.of(true)));

        webClient.put()
                .uri("/executives/v1/track/action/{id}",1)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(action)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .consumeWith(result -> LOGGER.info("{}",result))
                .jsonPath("$.data").isEqualTo(true);
    }
}
