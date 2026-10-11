package com.avegarlabs.construct_hub.domain.model;

import com.avegarlabs.construct_hub.application.dto.AppConfigDTO;
import com.avegarlabs.construct_hub.application.dto.EmpresaDTO;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;


@Entity
@Table(name = "app_config")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class AppConfig {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String factEmiter;
    private String  factLabal1;
    private String  factLabal2;
    private String  factLabal3;



    public void setDataFromDto(AppConfigDTO dto){
        factEmiter = dto.getFactEmiter();
        factLabal1 = dto.getFactLabal1();
        factLabal2 = dto.getFactLabal2();
        factLabal3 = dto.getFactLabal3();
    }
}