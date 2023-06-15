package com.kyc.tracking.controllers;

import com.kyc.core.model.web.RequestData;
import com.kyc.core.model.web.ResponseData;
import com.kyc.tracking.controllers.delegate.ExecutiveTrackingDelegate;
import com.kyc.tracking.model.ExecutiveAction;
import com.kyc.tracking.model.ExecutiveTrackInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/executives/v1")
public class ExecutiveTrackingController {

    @Autowired
    private ExecutiveTrackingDelegate delegate;

    @GetMapping("/track")
    public Mono<ResponseData<List<ExecutiveTrackInfo>>> getExecutiveTrack(@RequestParam("id_executive") Integer idExecutive,
                                                                          @RequestParam(value = "id_office",required = false) Integer idOffice,
                                                                          @RequestParam(value = "start_date",required = false) String startDate,
                                                                          @RequestParam(value = "end_date",required = false) String endDate){

        Map<String,String> queryParams = new HashMap<>();
        queryParams.put("idExecutive", Objects.toString(idExecutive));
        queryParams.put("idOffice",Objects.toString(idOffice,null));
        queryParams.put("startDate",startDate);
        queryParams.put("endDate",endDate);

        RequestData<Void> req = RequestData.<Void>builder()
                .queryParams(queryParams)
                .build();

        return delegate.getExecutiveTrackInfo(req);
    }

    @PostMapping("/track")
    public Mono<ResponseData<String>> executiveTrackInfo(@Valid @RequestBody ExecutiveTrackInfo body, ServerHttpRequest serverHttpRequest){

        RequestData<ExecutiveTrackInfo> req = RequestData.<ExecutiveTrackInfo>builder()
                .body(body)
                .build();

        return delegate.executiveTrackInfo(req);
    }

    @PutMapping("/track/action/{id}")
    public Mono<ResponseData<Boolean>> executiveTrackAction(@Valid @RequestBody ExecutiveAction body,
                                                            @PathVariable("id") String id,
                                                            ServerHttpRequest serverHttpRequest){

        RequestData<ExecutiveAction> req = RequestData.<ExecutiveAction>builder()
                .pathParams(Collections.singletonMap("id",id))
                .body(body)
                .build();

        return delegate.executiveTrackAction(req);
    }

}
