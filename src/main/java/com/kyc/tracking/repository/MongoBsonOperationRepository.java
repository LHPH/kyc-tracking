package com.kyc.tracking.repository;

import com.kyc.tracking.documents.ExecutiveTrackDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.util.Date;

@Repository
public class MongoBsonOperationRepository {

    @Autowired
    private ReactiveMongoTemplate reactiveMongoTemplate;


    public Flux<ExecutiveTrackDocument> getExecutiveInfo(Integer idExecutive, Integer idOffice, Date startDate, Date endDate){

        Query query = new Query();
        query.addCriteria(Criteria.where("idExecutive").is(idExecutive));
        if(idOffice!=null){
            query.addCriteria(new Criteria("idBranch").is(idOffice));
        }
        if(startDate!=null && endDate!=null){
            query.addCriteria(new Criteria("date").gte(startDate));
            query.addCriteria(new Criteria("date").lte(endDate));
        }
        return reactiveMongoTemplate.find(query, ExecutiveTrackDocument.class,"executives_track");
    }
}
