package com.kyc.tracking.controllers.delegate;

import com.kyc.core.model.web.RequestData;
import com.kyc.core.model.web.ResponseData;
import com.kyc.tracking.model.CustomerAction;
import com.kyc.tracking.service.CustomerActionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class CustomerActionDelegate {

    @Autowired
    private CustomerActionService customerActionService;

    public Mono<ResponseData<Boolean>> saveAction(RequestData<CustomerAction> request){

        return customerActionService.saveAction(request);
    }

    public Mono<ResponseData<List<CustomerAction>>> getActions(RequestData<Void> request){

        return customerActionService.getActions(request);
    }
}
