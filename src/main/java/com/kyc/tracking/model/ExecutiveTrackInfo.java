package com.kyc.tracking.model;

import com.kyc.core.model.BaseModel;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Setter
@Getter
public class ExecutiveTrackInfo extends BaseModel {

    @NotNull
    private Integer id;
    @NotNull
    private Integer idBranch;
    @NotNull
    private String ip;

    private Date date;
    @Valid
    @Size(min = 1)
    @NotNull
    private List<ExecutiveAction> actions;
}
