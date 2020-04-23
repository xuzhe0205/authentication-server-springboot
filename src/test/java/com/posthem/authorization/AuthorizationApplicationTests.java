package com.posthem.authorization;

import com.posthem.authorization.common.exception.ResourceNotFoundException;
import com.posthem.authorization.dao.UserRepository;
import com.posthem.authorization.entity.AuthProvider;
import com.posthem.authorization.entity.User;
import com.posthem.authorization.security.UserPrincipal;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;


@RunWith(SpringRunner.class)
@SpringBootTest
class AuthorizationApplicationTests {


	@Autowired
	private UserRepository userRepository;

	@Test
	public void testAddUser(){
		// Creating user's account
		User user = new User();
		user.setName("Oliver Xu");
		user.setEmail("oliverxu@124444443.com");
		user.setPassword("xuzhe950205");
		user.setProvider(AuthProvider.local);

		user.setPassword("xuzhe1234");

		userRepository.save(user);
		User myuser = userRepository.findUserByEmail("oliverxu@123.com");
		System.out.println(myuser.getId());
	}

	@Test
	public void testFindAll(){
		List<User> userList = userRepository.findAll();
		System.out.println("number of users: " + userList.size());
	}

	@Test
	public void testFindByEmail(){
		User myuser = userRepository.findUserByEmail("oliverxu@123.com");
		System.out.println("fetched user's id: " + myuser.getId());
	}

	@Test
	public void testExistsByEmail(){
		System.out.println("existed user? " + userRepository.existsUserByEmail("oliverxu@123.com"));
	}

	@Test
	public void loadUserById() {
		User user = userRepository.findUserById("5e9f56559de9ad767821a6c4fff");
		if (user == null) {
			throw new ResourceNotFoundException("User", "id", "5e9f56559de9ad767821a6c4");
		}

	}

}
