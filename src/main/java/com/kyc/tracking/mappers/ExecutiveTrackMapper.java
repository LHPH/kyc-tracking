package com.kyc.tracking.mappers;

import com.kyc.tracking.documents.ExecutiveTrackDocument;
import com.kyc.tracking.model.ExecutiveTrackInfo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring", uses = {ExecutiveActionMapper.class})
public interface ExecutiveTrackMapper {

    @Mappings({
            @Mapping(target = "id",ignore = true),
            @Mapping(target = "idExecutive",source = "source.id"),
            @Mapping(target = "idBranch",source = "source.idBranch"),
            @Mapping(target = "ip",source = "source.ip"),
            @Mapping(target = "date",expression = "java(new java.util.Date())"),
           // @Mapping(target = "actions",qualifiedByName = "")
    })
    ExecutiveTrackDocument toDocument(ExecutiveTrackInfo source);

    @Mappings({
            @Mapping(target = "id",source = "source.idExecutive"),
            @Mapping(target = "idBranch",source = "source.idBranch"),
            @Mapping(target = "ip",source = "source.ip"),
            @Mapping(target = "date",source = "source.date"),
            //@Mapping(target = "actions",source= "source.actions")
            // @Mapping(target = "actions",qualifiedByName = "")
    })
    ExecutiveTrackInfo toModel(ExecutiveTrackDocument source);
}
