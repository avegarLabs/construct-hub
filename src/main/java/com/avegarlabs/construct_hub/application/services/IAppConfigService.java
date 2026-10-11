package com.avegarlabs.construct_hub.application.services;

import com.avegarlabs.construct_hub.application.dto.AppConfigDTO;
import com.avegarlabs.construct_hub.application.dto.AppConfigListItem;

public interface IAppConfigService {

    void initDefaultConfigIfMissing();

    AppConfigListItem getConfig();

    AppConfigListItem update(AppConfigDTO dto);

}
