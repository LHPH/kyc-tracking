package com.kyc.tracking.repository;

import com.kyc.tracking.documents.CustomerActionDocument;
import com.kyc.tracking.documents.ExecutiveTrackDocument;
import org.springframework.beans.CachedIntrospectionResults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Date;

@Repository
public class MongoBsonOperationRepository {

    @Autowired
    private ReactiveMongoTemplate reactiveMongoTemplate;


    public Flux<ExecutiveTrackDocument> getExecutiveInfo(Integer idExecutive, Integer idOffice, Date startDate, Date endDate){

        Query query = new Query();

        if(idExecutive!=null){
            query.addCriteria(Criteria.where("idExecutive").is(idExecutive));
        }
        if(idOffice!=null){
            query.addCriteria(new Criteria("idBranch").is(idOffice));
        }
        if(startDate!=null && endDate!=null){
            query.addCriteria(new Criteria("date").gte(startDate).lte(endDate));
        }
        return reactiveMongoTemplate.find(query, ExecutiveTrackDocument.class,"executives_track");
    }

    public Mono<CustomerActionDocument> saveCustomerAction(CustomerActionDocument customerAction){

        return reactiveMongoTemplate.save(customerAction,"customers_track");
    }

    public Flux<CustomerActionDocument> getCustomerActions(Long customerNumber,
                                                           Integer channel,
                                                           String trackId,
                                                           String param,
                                                           Integer page){

        Query query = new Query();
        Pageable pageable = Pageable.ofSize(10);
        pageable.withPage(page);

        query.with(Pageable.ofSize(10));

        if(customerNumber!=null){
            query.addCriteria(new Criteria("customerNumber").is(customerNumber));
        }

        if(channel!=null){
            query.addCriteria(new Criteria("channel").is(channel));
        }

        if(trackId!=null){
            query.addCriteria(new Criteria("trackId").is(trackId));
        }

        if(param!=null){
            query.addCriteria(new Criteria("param").is(param));
        }

        return reactiveMongoTemplate.find(query,CustomerActionDocument.class,"customers_track");
    }
}
