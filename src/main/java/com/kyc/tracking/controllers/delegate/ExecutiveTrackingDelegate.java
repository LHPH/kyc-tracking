package com.kyc.tracking.controllers.delegate;

import com.kyc.core.model.web.RequestData;
import com.kyc.core.model.web.ResponseData;
import com.kyc.tracking.model.ExecutiveAction;
import com.kyc.tracking.model.ExecutiveTrackInfo;
import com.kyc.tracking.service.ExecutiveTrackingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class ExecutiveTrackingDelegate {

    @Autowired
    private ExecutiveTrackingService service;

    public Mono<ResponseData<List<ExecutiveTrackInfo>>> getExecutiveTrackInfo(RequestData<Void> req){

        return service.getExecutiveTrack(req);
    }

    public Mono<ResponseData<String>> executiveTrackInfo(RequestData<ExecutiveTrackInfo> req){

        return service.executiveTrackInfo(req);
    }

    public Mono<ResponseData<Boolean>> executiveTrackAction(RequestData<ExecutiveAction> req){

        return service.executiveTrackAction(req);
    }
}
