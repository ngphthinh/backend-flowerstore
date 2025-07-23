package com.ngphthinh.flower.repo;

import com.ngphthinh.flower.entity.ActiveToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActiveTokenRepository extends JpaRepository<ActiveToken, String> {
}
