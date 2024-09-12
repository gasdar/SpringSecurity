package com.spring.security.app;

import com.spring.security.app.configs.SecurityConfig;
import com.spring.security.app.persistence.entities.PermissionEntity;
import com.spring.security.app.persistence.entities.RoleEntity;
import com.spring.security.app.persistence.entities.UserEntity;
import com.spring.security.app.persistence.enums.RoleEnum;
import com.spring.security.app.persistence.repositories.UserRepository;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SpringSecurityApplication {

	@Autowired
	private SecurityConfig security;

	public static void main(String[] args) {
		SpringApplication.run(SpringSecurityApplication.class, args);
	}

	@Bean
	public CommandLineRunner init(UserRepository userRepository) {
		return args -> {

			/* Create PERMISSIONS */
			PermissionEntity createPermission = PermissionEntity.builder()
				.name("CREATE")
				.build();
		
			PermissionEntity readPermission = PermissionEntity.builder()
				.name("READ")
				.build();

			PermissionEntity updatePermission = PermissionEntity.builder()
				.name("UPDATE")
				.build();

			PermissionEntity deletePermission = PermissionEntity.builder()
				.name("DELETE")
				.build();

			PermissionEntity refactorPermission = PermissionEntity.builder()
				.name("REFACTOR")
				.build();

			/* Create ROLES */
			RoleEntity roleAdmin = RoleEntity.builder()
				.roleEnum(RoleEnum.ADMIN)
				.permissions(Set.of(createPermission, readPermission, updatePermission, deletePermission))
				.build();

			RoleEntity roleUser = RoleEntity.builder()
				.roleEnum(RoleEnum.USER)
				.permissions(Set.of(createPermission, readPermission))
				.build();

			RoleEntity roleInvited = RoleEntity.builder()
				.roleEnum(RoleEnum.INVITED)
				.permissions(Set.of(readPermission))
				.build();

			RoleEntity roleDeveloper = RoleEntity.builder()
				.roleEnum(RoleEnum.DEVELOPER)
				.permissions(Set.of(createPermission, readPermission, updatePermission, deletePermission, refactorPermission))
				.build();

			/* Create USERS */
			UserEntity userPepe = UserEntity
				.builder()
				.username("pepe")
				.password(security.passwordEncoder().encode("12345"))
				.roles(Set.of(roleAdmin, roleUser))
				.enabled(true)
				.accountNonExpired(true)
				.accountNonLooked(true)
				.credentialsNonExpired(true)
				.build();

			UserEntity userMaria = UserEntity
				.builder()
				.username("maria")
				.password(security.passwordEncoder().encode("12345"))
				.roles(Set.of(roleAdmin, roleUser))
				.enabled(true)
				.accountNonExpired(true)
				.accountNonLooked(true)
				.credentialsNonExpired(true)
				.build();

			UserEntity userRolan = UserEntity
				.builder()
				.username("rolan")
				.password(security.passwordEncoder().encode("12345"))
				.roles(Set.of(roleDeveloper))
				.enabled(true)
				.accountNonExpired(true)
				.accountNonLooked(true)
				.credentialsNonExpired(true)
				.build();

			UserEntity userCamila = UserEntity
				.builder()
				.username("camila")
				.password(security.passwordEncoder().encode("12345"))
				.roles(Set.of(roleDeveloper))
				.enabled(true)
				.accountNonExpired(true)
				.accountNonLooked(true)
				.credentialsNonExpired(true)
				.build();

			UserEntity userValentina = UserEntity
				.builder()
				.username("valentina")
				.password(security.passwordEncoder().encode("12345"))
				.roles(Set.of(roleInvited))
				.enabled(true)
				.accountNonExpired(true)
				.accountNonLooked(true)
				.credentialsNonExpired(true)
				.build();

			UserEntity userMarco = UserEntity
				.builder()
				.username("marco")
				.password(security.passwordEncoder().encode("12345"))
				.roles(Set.of(roleUser))
				.enabled(true)
				.accountNonExpired(true)
				.accountNonLooked(true)
				.credentialsNonExpired(true)
				.build();
			
			// INSERT ALL USERS
			userRepository.saveAll(List.of(userPepe, userMaria, userRolan, userCamila, userValentina, userMarco));
		};
	}

}
