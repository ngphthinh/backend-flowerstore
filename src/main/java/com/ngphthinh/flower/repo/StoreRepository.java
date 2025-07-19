package com.ngphthinh.flower.repo;

import com.ngphthinh.flower.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface StoreRepository extends JpaRepository<Store, Long> {
}
