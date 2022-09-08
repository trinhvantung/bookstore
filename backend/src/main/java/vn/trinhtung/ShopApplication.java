package vn.trinhtung;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;

import vn.trinhtung.dto.RoleDto;
import vn.trinhtung.entity.Role;
import vn.trinhtung.entity.User;
import vn.trinhtung.repository.UserRepository;
import vn.trinhtung.service.RoleService;

@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableJpaAuditing
@EnableCaching
@SpringBootApplication
public class ShopApplication implements CommandLineRunner {
	@Autowired
	private RoleService roleService;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder encoder;

	@Override
	public void run(String... args) throws Exception {
		initRole();

		initUser();

	}

	private void initUser() {
		if (userRepository.findByEmail("tungvlhy@gmail.com").isEmpty()) {
			User user = new User();
			user.setStatus(true);
			user.setEmail("tungvlhy@gmail.com");
			user.setPassword(encoder.encode("12345"));
			user.setFullname("Admin");

			RoleDto dto = roleService.getByName("ADMIN");
			Role role = new Role();
			role.setId(dto.getId());

			user.setRoles(Arrays.asList(role));

			userRepository.save(user);
		}
	}

	private void initRole() {
		if (roleService.getByName("ADMIN") == null) {
			roleService.save(new RoleDto(null, "ADMIN"));
		}

		if (roleService.getByName("MANAGER") == null) {
			roleService.save(new RoleDto(null, "MANAGER"));
		}

		if (roleService.getByName("USER") == null) {
			roleService.save(new RoleDto(null, "USER"));
		}
	}

	public static void main(String[] args) {
		SpringApplication.run(ShopApplication.class, args);
	}

}
