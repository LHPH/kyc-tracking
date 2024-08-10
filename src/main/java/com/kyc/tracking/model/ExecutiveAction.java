package com.kyc.tracking.model;

import com.kyc.core.model.BaseModel;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
public class ExecutiveAction extends BaseModel {

    @NotNull
    private String action;
    @NotNull
    private String info;
    private String page;
    private String key;
    private Date date;
}
