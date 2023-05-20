package com.kyc.tracking.controllers;

import com.kyc.core.model.web.RequestData;
import com.kyc.core.model.web.ResponseData;
import com.kyc.core.util.GeneralUtil;
import com.kyc.tracking.controllers.delegate.CustomerActionDelegate;
import com.kyc.tracking.model.CustomerAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/customers/v1")
public class CustomerActionController {

    @Autowired
    private CustomerActionDelegate delegate;

    @PostMapping("/action")
    public Mono<ResponseData<Boolean>> saveAction(@Valid @RequestBody CustomerAction customerAction){

        RequestData<CustomerAction> request = RequestData.<CustomerAction>builder()
                .body(customerAction)
                .build();

        return delegate.saveAction(request);
    }

    @GetMapping("/actions")
    public Mono<ResponseData<List<CustomerAction>>> getActions(@RequestParam(value="customer_number",required = false) Long customerNumber,
                                                               @RequestParam(value="channel",required = false) Integer channel,
                                                               @RequestParam(value="track_id",required = false) String trackId,
                                                               @RequestParam(value="param",required = false) String param,
                                                               @RequestParam(value = "page",defaultValue = "1") Integer page){

        Map<String,Object> queryParams = new HashMap<>();
        queryParams.put("customerNumber",customerNumber);
        queryParams.put("channel",channel);
        queryParams.put("trackId",trackId);
        queryParams.put("param",param);
        queryParams.put("page",page);

        RequestData<Void> request = RequestData.<Void>builder()
                .queryParams(GeneralUtil.mapStringValue(queryParams))
                .build();

        return delegate.getActions(request);
    }

}
