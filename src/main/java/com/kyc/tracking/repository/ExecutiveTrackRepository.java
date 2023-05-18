package com.kyc.tracking.repository;

import com.kyc.tracking.documents.ExecutiveActionDocument;
import com.kyc.tracking.documents.ExecutiveTrackDocument;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.mongodb.repository.Update;
import reactor.core.publisher.Mono;

public interface ExecutiveTrackRepository extends ReactiveMongoRepository<ExecutiveTrackDocument, String> {

    @Query("{ 'id' : ?0 }")
    @Update("{ '$push' : { 'actions' : ?1 } }")
    Mono<Long> addAction(String id, ExecutiveActionDocument action);
}
