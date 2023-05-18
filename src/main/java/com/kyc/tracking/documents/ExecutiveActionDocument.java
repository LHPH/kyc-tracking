package com.kyc.tracking.documents;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
public class ExecutiveActionDocument {

    private String action;
    private String info;
    private String page;
    private String key;
    private Date date;
}
