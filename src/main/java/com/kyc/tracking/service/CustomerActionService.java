package com.kyc.tracking.service;

import com.kyc.core.model.web.RequestData;
import com.kyc.core.model.web.ResponseData;
import com.kyc.core.util.GeneralUtil;
import com.kyc.tracking.documents.CustomerActionDocument;
import com.kyc.tracking.mappers.CustomerActionMapper;
import com.kyc.tracking.model.CustomerAction;
import com.kyc.tracking.repository.MongoBsonOperationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@Service
public class CustomerActionService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerActionService.class);

    @Autowired
    private MongoBsonOperationRepository mongoBsonOperationRepository;

    @Autowired
    private CustomerActionMapper customerActionMapper;

    public Mono<ResponseData<Boolean>> saveAction(RequestData<CustomerAction> request){

        CustomerActionDocument doc = customerActionMapper.toDocument(request.getBody());

        return mongoBsonOperationRepository.saveCustomerAction(doc)
                .doOnNext(element -> LOGGER.info("{}",element.getId().toHexString()))
                .map(element -> ResponseData.of(true));
    }

    public Mono<ResponseData<List<CustomerAction>>> getActions(RequestData<Void> request){

        Map<String,String> queryParams = request.getQueryParams();
        Long customerNumber = Long.parseLong(queryParams.get("customerNumber"));
        Integer channel = GeneralUtil.convertOrNull(queryParams.get("channel"),Integer.class);
        String trackId = queryParams.get("trackId");
        String param = queryParams.get("param");
        Integer page = GeneralUtil.convertOrNull(queryParams.getOrDefault("page","0"),Integer.class);

        if(page<0){
            page = 0;
        }

        return mongoBsonOperationRepository.getCustomerActions(customerNumber,channel,trackId,param,page)
                .map(customerActionMapper::toModel)
                .collectList()
                .doOnNext(list -> LOGGER.info("Retrieved {}",list.size()))
                .flatMap(list -> Mono.just(ResponseData.of(list)));
    }
}
