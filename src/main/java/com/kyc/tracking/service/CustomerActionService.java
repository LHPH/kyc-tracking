package com.kyc.tracking.service;

import com.kyc.core.model.web.RequestData;
import com.kyc.core.model.web.ResponseData;
import com.kyc.core.properties.KycMessages;
import com.kyc.tracking.documents.CustomerActionDocument;
import com.kyc.tracking.mappers.CustomerActionMapper;
import com.kyc.tracking.model.CustomerAction;
import com.kyc.tracking.repository.MongoBsonOperationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

import static com.kyc.core.util.GeneralUtil.convertOrNull;
import static com.kyc.core.util.ReactiveUtil.getReactiveError;
import static com.kyc.core.util.ReactiveUtil.sendReactiveError;
import static com.kyc.tracking.constants.AppConstants.MESSAGE_003;

@Service
public class CustomerActionService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerActionService.class);

    @Autowired
    private MongoBsonOperationRepository mongoBsonOperationRepository;

    @Autowired
    private CustomerActionMapper customerActionMapper;

    @Autowired
    private KycMessages kycMessages;

    public Mono<ResponseData<Boolean>> saveAction(RequestData<CustomerAction> request){

        CustomerActionDocument doc = customerActionMapper.toDocument(request.getBody());

        return mongoBsonOperationRepository.saveCustomerAction(doc)
                .doOnNext(element -> LOGGER.info("{}",element.getId().toHexString()))
                .map(element -> ResponseData.of(true))
                .onErrorMap(exc -> getReactiveError(exc, HttpStatus.SERVICE_UNAVAILABLE,kycMessages.getMessage(MESSAGE_003),request));
    }

    public Mono<ResponseData<List<CustomerAction>>> getActions(RequestData<Void> request){

        Map<String,String> queryParams = request.getQueryParams();
        Long customerNumber = convertOrNull(queryParams.get("customerNumber"),Long.class);
        Integer channel = convertOrNull(queryParams.get("channel"),Integer.class);
        String trackId = queryParams.get("trackId");
        String param = queryParams.get("param");
        Integer page = convertOrNull(queryParams.getOrDefault("page","0"),Integer.class);

        if(page<0){
            page = 0;
        }
        LOGGER.info("Page {}",page);
        return mongoBsonOperationRepository.getCustomerActions(customerNumber,channel,trackId,param,page)
                .map(customerActionMapper::toModel)
                .onErrorMap(exc -> getReactiveError(exc, HttpStatus.SERVICE_UNAVAILABLE,kycMessages.getMessage(MESSAGE_003),request))
                .collectList()
                .doOnNext(list -> LOGGER.info("Retrieved {}",list.size()))
                .flatMap(list -> Mono.just(ResponseData.of(list)));
    }


}
