package com.ngphthinh.flower.repo;

import com.ngphthinh.flower.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByPhoneNumber(String phoneNumber);

    Optional<User> findByPhoneNumber(String phoneNumber);

    @Query("""
            select u from User u
            join fetch u.role
            join fetch u.store
            where (:roleName is null or u.role.name = upper(:roleName)) and 
                  (:phoneNumber is null or u.phoneNumber like concat('%', :phoneNumber, '%'))
            """)
    Page<User> findAllByPhoneNumberContainingIgnoreCaseOrRoleNameContainingIgnoreCase(
            String phoneNumber, String roleName, Pageable pageable);

}
