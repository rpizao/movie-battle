package br.com.rpizao.services;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.rpizao.dtos.AuthUser;
import br.com.rpizao.dtos.UserCredentials;
import br.com.rpizao.entities.User;
import br.com.rpizao.exceptions.BusinessException;
import br.com.rpizao.repositories.UserRepository;
import br.com.rpizao.services.interfaces.IUserService;
import br.com.rpizao.utils.CryptoUtils;

@Service
public class UserService implements IUserService{
	
	@Autowired
	private UserRepository userRepository;
	
	@PostConstruct
    private void init() {
		long estimateSize = userRepository.findAll().spliterator().estimateSize();
		if(estimateSize != 0) return;
		
		String salt = CryptoUtils.generateSalt();
        User john = new User();
        john.setLogin("rpizao@gmail.com");
        john.setSalt(salt);
        john.setPassword(CryptoUtils.hash("12345", salt));
        john.setCode(CryptoUtils.randomString(5));
        john.setName("Rafael");
        
        User mary = new User();
        mary.setLogin("thanelfm@gmail.com");
        mary.setSalt(salt);
        mary.setPassword(CryptoUtils.hash("54321", salt));
        mary.setCode(CryptoUtils.randomString(5));
        mary.setName("Thaís");
        
        userRepository.save(john);
        userRepository.save(mary);
    }

	@Override
	public AuthUser login(String login, String passw) throws BusinessException {
		final User user = userRepository.findByLogin(login);
		final String input = CryptoUtils.hash(passw, user.getSalt());
		
		if(user.getPassword().equals(input)) {
			return AuthUser.builder()
					.token(CryptoUtils.generateSalt())
					.user(UserCredentials.builder()
							.username(user.getName())
							.code(user.getCode())
							.build())
					.build();
		}
		return null;
	}
	
	@Override
	public User findByCode(String code) {
		return userRepository.findByCode(code);
	}

}