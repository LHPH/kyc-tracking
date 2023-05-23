package com.kyc.tracking.controller;

import com.kyc.core.model.web.RequestData;
import com.kyc.core.model.web.ResponseData;
import com.kyc.tracking.controllers.CustomerActionController;
import com.kyc.tracking.controllers.delegate.CustomerActionDelegate;
import com.kyc.tracking.model.CustomerAction;
import org.junit.jupiter.api.Test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.ExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebFluxTest(controllers = CustomerActionController.class)
public class CustomerActionControllerTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerActionControllerTest.class);

    @Autowired
    private WebTestClient webClient;

    @MockBean
    private CustomerActionDelegate delegate;

    @Test
    public void saveAction_savingAction_returnMonoResult(){

        when(delegate.saveAction(any(RequestData.class)))
                .thenReturn(Mono.just(ResponseData.of(true)));

        CustomerAction customerAction = new CustomerAction();
        customerAction.setCustomerNumber(1L);
        customerAction.setChannel(1);
        customerAction.setTrackId("1");

        webClient.post()
                .uri("/customers/v1/action")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(customerAction)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .consumeWith(result -> LOGGER.info("{}",result))
                .jsonPath("$.data").isEqualTo(true);
    }

    @Test
    public void getActions_gettingActions_returnMonoResult(){

        CustomerAction customerAction = new CustomerAction();
        customerAction.setCustomerNumber(1L);
        customerAction.setChannel(1);
        customerAction.setTrackId("1");

        when(delegate.getActions(any(RequestData.class)))
                .thenReturn(Mono.just(ResponseData.of(Collections.singletonList(customerAction))));

        webClient.get()
                .uri("/customers/v1/actions")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .consumeWith(result -> LOGGER.info("{}",result))
                .jsonPath("$.data[0].customerNumber").isEqualTo(customerAction.getCustomerNumber());
    }

}
