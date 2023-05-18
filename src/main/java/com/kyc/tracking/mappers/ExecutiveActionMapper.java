package com.kyc.tracking.mappers;

import com.kyc.tracking.documents.ExecutiveActionDocument;
import com.kyc.tracking.model.ExecutiveAction;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ExecutiveActionMapper {

    ExecutiveActionDocument toNestedDocument(ExecutiveAction source);

    List<ExecutiveActionDocument> toList(List<ExecutiveAction> source);

    ExecutiveAction toModel(ExecutiveActionDocument source);

    List<ExecutiveAction> toModelList(List<ExecutiveActionDocument> source);
}
