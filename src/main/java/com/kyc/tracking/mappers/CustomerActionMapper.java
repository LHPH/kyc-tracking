package com.kyc.tracking.mappers;

import com.kyc.tracking.documents.CustomerActionDocument;
import com.kyc.tracking.model.CustomerAction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface CustomerActionMapper {

    @Mappings({
            @Mapping(target = "id",ignore = true),
            @Mapping(target = "date",expression = "java(new java.util.Date())")
    })
    CustomerActionDocument toDocument(CustomerAction source);


    CustomerAction toModel(CustomerActionDocument source);
}
