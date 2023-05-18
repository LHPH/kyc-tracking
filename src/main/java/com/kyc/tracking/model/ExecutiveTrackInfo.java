package com.kyc.tracking.model;

import com.kyc.core.model.BaseModel;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
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
