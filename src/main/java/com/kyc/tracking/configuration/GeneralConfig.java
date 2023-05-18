package com.kyc.tracking.configuration;

import com.kyc.core.exception.handlers.KycGenericRestExceptionHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(value = {KycGenericRestExceptionHandler.class})
public class GeneralConfig {
}
