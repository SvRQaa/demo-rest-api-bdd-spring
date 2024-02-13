package tse.api.demo.helpers;

import org.springframework.stereotype.Component;
import tse.api.demo.model.User;
import tse.api.demo.utils.constants.ExCon;

@Component
public class UserHelper {

    public UserHelper() {}

    public User formUser(String username) {
        return User.builder()
                .username(username)
                .password(ExCon.PWD)
                .build();
    }
}
