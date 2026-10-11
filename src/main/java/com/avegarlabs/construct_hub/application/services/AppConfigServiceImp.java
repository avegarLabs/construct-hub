package com.avegarlabs.construct_hub.application.services;

import com.avegarlabs.construct_hub.application.dto.AppConfigDTO;
import com.avegarlabs.construct_hub.application.dto.AppConfigListItem;
import com.avegarlabs.construct_hub.domain.model.AppConfig;
import com.avegarlabs.construct_hub.domain.repositories.AppConfigRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AppConfigServiceImp implements IAppConfigService {

    private final AppConfigRepository appConfigRepository;

    // La configuracion de la app es un registro unico: se crea al arrancar y luego solo se actualiza
    @Override
    @Transactional
    public void initDefaultConfigIfMissing() {
        if (appConfigRepository.count() > 0) {
            return;
        }
        AppConfig appConfig = new AppConfig();
        appConfig.setDataFromDto(AppConfigDTO.builder()
                .factEmiter("Empresa A")
                .factLabal1("SOLICITADO")
                .factLabal2("APROBADO")
                .factLabal3("RECIBO")
                .build());
        appConfigRepository.save(appConfig);
    }

    @Override
    @Transactional(readOnly = true)
    public AppConfigListItem getConfig() {
        return mapperAppToListItem(findSingleConfig());
    }

    @Override
    @Transactional
    public AppConfigListItem update(AppConfigDTO dto) {
        AppConfig appConfig = findSingleConfig();
        appConfig.setDataFromDto(dto);
        return mapperAppToListItem(appConfigRepository.save(appConfig));
    }

    private AppConfig findSingleConfig() {
        return appConfigRepository.findFirstByOrderByIdAsc()
                .orElseThrow(() -> new IllegalStateException("La configuracion de la app no ha sido inicializada"));
    }

    public AppConfigListItem mapperAppToListItem(AppConfig appConfig) {
        return AppConfigListItem.builder()
                .id(appConfig.getId())
                .factEmiter(appConfig.getFactEmiter())
                .factLabal1(appConfig.getFactLabal1())
                .factLabal2(appConfig.getFactLabal2())
                .factLabal3(appConfig.getFactLabal3())
                .build();
    }

}
