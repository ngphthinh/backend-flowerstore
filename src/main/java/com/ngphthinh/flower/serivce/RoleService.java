package com.ngphthinh.flower.serivce;

import com.ngphthinh.flower.entity.Role;
import com.ngphthinh.flower.mapper.RoleMapper;
import com.ngphthinh.flower.repo.RoleRepository;
import org.springframework.stereotype.Service;

@Service
public class RoleService {

    private final RoleRepository roleRepository;;

    private final RoleMapper roleMapper;


    public RoleService(RoleRepository roleRepository, RoleMapper roleMapper) {
        this.roleRepository = roleRepository;
        this.roleMapper = roleMapper;
    }


    public Role getRoleEntityById(String id) {
        return roleRepository.findById(id).orElse(null);
    }



}
