package com.kyc.tracking.service;

import com.kyc.core.enums.MessageType;
import com.kyc.core.exception.KycRestException;
import com.kyc.core.model.web.MessageData;
import com.kyc.core.model.web.RequestData;
import com.kyc.core.model.web.ResponseData;
import com.kyc.core.properties.KycMessages;
import com.kyc.core.util.DateUtil;
import com.kyc.tracking.documents.CustomerActionDocument;
import com.kyc.tracking.documents.ExecutiveTrackDocument;
import com.kyc.tracking.mappers.ExecutiveActionMapper;
import com.kyc.tracking.mappers.ExecutiveTrackMapper;
import com.kyc.tracking.model.ExecutiveAction;
import com.kyc.tracking.model.ExecutiveTrackInfo;
import com.kyc.tracking.repository.ExecutiveTrackRepository;
import com.kyc.tracking.repository.MongoBsonOperationRepository;
import org.apache.commons.lang3.BooleanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.kyc.core.util.GeneralUtil.convertOrNull;
import static com.kyc.core.util.ReactiveUtil.getReactiveError;
import static com.kyc.core.util.ReactiveUtil.sendReactiveError;
import static com.kyc.tracking.constants.AppConstants.MESSAGE_002;
import static com.kyc.tracking.constants.AppConstants.MESSAGE_003;

@Service
public class ExecutiveTrackingService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExecutiveTrackingService.class);

    @Autowired
    private ExecutiveTrackRepository executiveTrackRepository;

    @Autowired
    private MongoBsonOperationRepository mongoBsonOperationRepository;

    @Autowired
    private ExecutiveTrackMapper executiveTrackMapper;

    @Autowired
    private ExecutiveActionMapper executiveActionMapper;

    @Autowired
    private KycMessages kycMessages;

    public Mono<ResponseData<List<ExecutiveTrackInfo>>> getExecutiveTrack(RequestData<Void> req){

        Map<String,String> queryParams = req.getQueryParams();
        Integer idExecutive = Integer.parseInt(queryParams.get("idExecutive"));
        Integer idOffice = convertOrNull(queryParams.get("idOffice"),Integer.class);
        Date startDate =DateUtil.stringToDate(queryParams.get("startDate"));
        Date endDate = DateUtil.stringToDate(queryParams.get("endDate"));

        boolean cond1 = Objects.nonNull(startDate);
        boolean cond2 = Objects.nonNull(endDate);

        if( !(BooleanUtils.and(new boolean[]{cond1,cond2}) && endDate.after(startDate)) ){
            startDate=null;
            endDate=null;
        }

        return mongoBsonOperationRepository.getExecutiveInfo(idExecutive,idOffice,startDate,endDate)
                .map(executiveTrackMapper::toModel)
                .onErrorMap(exc -> getReactiveError(exc, HttpStatus.SERVICE_UNAVAILABLE,kycMessages.getMessage(MESSAGE_003),req))
                .collectList()
                .doOnNext(list -> LOGGER.info("Retrieved {}",list.size()))
                .flatMap(list -> Mono.just(ResponseData.of(list)));
    }

    public Mono<ResponseData<String>> executiveTrackInfo(RequestData<ExecutiveTrackInfo> request){

        ExecutiveTrackInfo req = request.getBody();
        ExecutiveTrackDocument doc = executiveTrackMapper.toDocument(req);

        return executiveTrackRepository.save(doc)
                .doOnNext(d -> LOGGER.info("{}",d.getId()))
                .map(d -> ResponseData.of(d.getId().toString()))
                .onErrorMap(exc -> getReactiveError(exc, HttpStatus.SERVICE_UNAVAILABLE,kycMessages.getMessage(MESSAGE_003),req));
    }

    public Mono<ResponseData<Boolean>> executiveTrackAction(RequestData<ExecutiveAction> request){

        Map<String,Object> pathParams = request.getPathParams();
        ExecutiveAction body = request.getBody();

        String id = pathParams.get("id").toString();

        return executiveTrackRepository.findById(id)
                .onErrorMap(exc -> getReactiveError(exc, HttpStatus.SERVICE_UNAVAILABLE,kycMessages.getMessage(MESSAGE_003),request))
                .flatMap(element -> {
                    //element.getActions().add(executiveActionMapper.toNestedDocument(body));
                    return executiveTrackRepository.addAction(id,executiveActionMapper.toNestedDocument(body))
                            .doOnNext(d -> LOGGER.info("count {}",d))
                            .map(d -> ResponseData.of(true))
                            .onErrorMap(exc -> getReactiveError(exc, HttpStatus.SERVICE_UNAVAILABLE,kycMessages.getMessage(MESSAGE_003),request));
                })
                .switchIfEmpty(Mono.defer(()->sendReactiveError(null, HttpStatus.UNPROCESSABLE_ENTITY,kycMessages.getMessage(MESSAGE_002),request)));
    }

}
