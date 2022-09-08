package vn.trinhtung.service;

import vn.trinhtung.dto.RoleDto;

public interface RoleService {
	RoleDto getByName(String name);
	
	RoleDto save(RoleDto roleDto);
}
