package com.kyc.tracking.config;

import com.kyc.core.config.BuildDetailConfig;
import com.kyc.core.exception.handlers.KycGenericRestExceptionHandler;
import com.kyc.core.exception.handlers.KycUnhandledExceptionHandler;
import com.kyc.core.exception.handlers.KycValidationRestExceptionHandler;
import com.kyc.core.properties.KycMessages;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import static com.kyc.tracking.constants.AppConstants.MESSAGE_001;
import static com.kyc.tracking.constants.AppConstants.MESSAGE_002;

@Configuration
@Import(value = {BuildDetailConfig.class,KycGenericRestExceptionHandler.class, KycMessages.class})
public class GeneralConfig {

    @Bean
    public KycUnhandledExceptionHandler kycUnhandledExceptionHandler(KycMessages kycMessages){
        return new KycUnhandledExceptionHandler(kycMessages.getMessage(MESSAGE_001));
    }

   //@Bean
    public KycValidationRestExceptionHandler kycValidationRestExceptionHandler(KycMessages kycMessages){
        return new KycValidationRestExceptionHandler(kycMessages.getMessage(MESSAGE_002));
    }
}
