package com.ngphthinh.flower.serivce;

import com.ngphthinh.flower.entity.Role;
import com.ngphthinh.flower.repo.RoleRepository;
import org.springframework.stereotype.Service;

@Service
public class RoleService {

    private final RoleRepository roleRepository;


    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public Role getRoleEntityById(String id) {
        return roleRepository.findById(id).orElse(null);
    }



}
