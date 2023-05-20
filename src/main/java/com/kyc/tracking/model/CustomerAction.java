package com.kyc.tracking.model;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.Map;

@Setter
@Getter
public class CustomerAction {

    @NotNull
    private Long customerNumber;
    @NotNull
    private Integer channel;
    @NotNull
    private String trackId;
    private Date date;
    private Map<String,Object> params;
}
