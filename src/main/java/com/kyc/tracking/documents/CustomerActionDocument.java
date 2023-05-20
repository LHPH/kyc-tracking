package com.kyc.tracking.documents;

import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

@Setter
@Getter
@Document
public class CustomerActionDocument implements Serializable {

    @MongoId
    private ObjectId id;
    private Long customerNumber;
    private Date date;
    private Integer channel;
    private String trackId;
    private Map<String,Object> params;

}
