package vn.trinhtung.service.impl;

import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import vn.trinhtung.dto.RoleDto;
import vn.trinhtung.entity.Role;
import vn.trinhtung.repository.RoleRepository;
import vn.trinhtung.service.RoleService;

@Service
public class RoleServiceImpl implements RoleService {
	@Autowired
	private RoleRepository roleRepository;

	@Cacheable(value = "role", key = "#name")
	@Override
	public RoleDto getByName(String name) {
		Optional<Role> optional = roleRepository.findByName(name);
		if (optional.isPresent()) {
			RoleDto dto = new RoleDto();
			BeanUtils.copyProperties(optional.get(), dto);
			return dto;
		}

		return null;
	}

	@CachePut(value = "role", key = "#roleDto.name")
	@Override
	public RoleDto save(RoleDto roleDto) {
		Role role = new Role();
		BeanUtils.copyProperties(roleDto, role);

		Role saved = roleRepository.save(role);

		RoleDto result = new RoleDto();
		BeanUtils.copyProperties(saved, result);

		return result;
	}

}
