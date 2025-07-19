package com.ngphthinh.flower.config;

import com.ngphthinh.flower.entity.Store;
import com.ngphthinh.flower.repo.StoreRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class AppInitConfig {

    private static final Logger log = LogManager.getLogger(AppInitConfig.class);

    @Bean
    ApplicationRunner applicationRunner(StoreRepository storeRepository) {
        return args -> {
            if (storeRepository.count() == 0) {
                List<Store> stores = List.of(
                        Store.builder().name("Shop Mĩ Tho").address("208 Đoàn Thị Nghiệp").build(),
                        Store.builder().name("Shop Gò công").address("Bình hoà đông, Bình nhì, Gò công tây, Tiền giang").build()
                );
                storeRepository.saveAll(stores);
                log.info("2 default stores added to database for testing");
            }
        };
    }
}
