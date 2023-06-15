package com.kyc.tracking.documents;

import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Setter
@Getter
@Document(collection = "executives_track")
public class ExecutiveTrackDocument implements Serializable {

    @MongoId
    private ObjectId id;
    private Integer idExecutive;
    private Integer idBranch;
    private String ip;
    private Date date;
    private List<ExecutiveActionDocument> actions;
}
