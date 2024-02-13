package tse.api.demo.service.interfaces;

import tse.api.demo.model.User;

public interface UserRestService {
    User createUser(User user);
    User getUserById(Long id);
    User getUserByUsername(String username);
}
