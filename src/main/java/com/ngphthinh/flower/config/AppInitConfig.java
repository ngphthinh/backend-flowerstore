package com.ngphthinh.flower.config;

import com.ngphthinh.flower.entity.Permission;
import com.ngphthinh.flower.entity.Role;
import com.ngphthinh.flower.entity.Store;
import com.ngphthinh.flower.entity.User;
import com.ngphthinh.flower.repo.PermissionRepository;
import com.ngphthinh.flower.repo.RoleRepository;
import com.ngphthinh.flower.repo.StoreRepository;
import com.ngphthinh.flower.repo.UserRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Set;

@Configuration
public class AppInitConfig {

    private static final Logger log = LogManager.getLogger(AppInitConfig.class);

//    private final PasswordEncoder passwordEncoder;

//    public AppInitConfig(PasswordEncoder passwordEncoder) {
//        this.passwordEncoder = passwordEncoder;
//    }

    @Bean
    ApplicationRunner applicationRunner(StoreRepository storeRepository, PermissionRepository permissionRepository, RoleRepository roleRepository, UserRepository userRepository) {
        return args -> {
            if (storeRepository.count() == 0) {
                List<Store> stores = List.of(
                        Store.builder().name("Shop Mĩ Tho").address("208 Đoàn Thị Nghiệp").build(),
                        Store.builder().name("Shop Gò công").address("Bình hoà đông, Bình nhì, Gò công tây, Tiền giang").build()
                );
                storeRepository.saveAll(stores);
                log.info("2 default stores added to database for testing");
            }


            if (permissionRepository.count() == 0) {
                log.info("No permissions found in the database, initializing default permissions.");
                permissionRepository.saveAll(List.of(
                        Permission.builder().name("VIEW_INVOICE").description("Permission to view invoices").build(),
                        Permission.builder().name("CREATE_INVOICE").description("Permission to create invoices").build(),
                        Permission.builder().name("UPDATE_INVOICE").description("Permission to update invoices").build(),
                        Permission.builder().name("DELETE_INVOICE").description("Permission to delete invoices").build(),
                        Permission.builder().name("VIEW_PRODUCT").description("Permission to view products").build(),
                        Permission.builder().name("CREATE_PRODUCT").description("Permission to create products").build(),
                        Permission.builder().name("UPDATE_PRODUCT").description("Permission to update products").build(),
                        Permission.builder().name("DELETE_PRODUCT").description("Permission to delete products").build()
                ));
                log.info("Default permissions added to database for testing");
            }

            if (roleRepository.count() == 0) {
                log.info("No roles found in the database, initializing default roles.");
                Role adminRole = Role.builder()
                        .name("ADMIN")
                        .description("Administrator role with all permissions")
                        .permissions(Set.copyOf(permissionRepository.findAll()))
                        .build();
                Role userRole = Role.builder()
                        .name("USER")
                        .description("User role with limited permissions")
                        .permissions(Set.of(
                                permissionRepository.findById("VIEW_INVOICE").orElseThrow(),
                                permissionRepository.findById("CREATE_INVOICE").orElseThrow()
                        )).build();
                roleRepository.saveAll(List.of(adminRole, userRole));
                log.info("Default roles added to database for testing");
            }
//            if (!userRepository.existsByPhoneNumber("0000000000")) {
//                User adminUser = User.builder()
//                        .phoneNumber("0000000000")
//                        .password(passwordEncoder.encode("Admin@123"))
//                        .store(storeRepository.findById(1L).orElse(null))
//                        .role(roleRepository.findById("ADMIN").orElse(null))
//                        .build();
//                userRepository.save(adminUser);
//                log.info("Default user added to database for testing");
//            }
        };
    }
}
