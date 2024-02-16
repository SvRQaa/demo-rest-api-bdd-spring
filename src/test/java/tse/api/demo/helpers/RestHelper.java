package tse.api.demo.helpers;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import tse.api.demo.model.Security;
import tse.api.demo.model.User;

import java.util.Collections;

@Component
public class RestHelper {
    @Autowired
    RestTemplate restTemplate;
    @Getter
    @Setter
    HttpHeaders headers = new HttpHeaders();
    @Getter
    String ServiceURL = "http://localhost/8080";
    @Getter
    String USERS = "/users/";
    @Getter
    String SECURITIES = "/users/";

    public RestHelper() {
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
    }

    private String formUserUrl() {
        return String.format("%s%s", getServiceURL(), USERS);
    }

    private String formUserGetUrl(Long usersId) {
        return String.format("%s%s", formUserUrl(), usersId.toString());
    }

    private String formSecuritiesUrl() {
        return String.format("%s%s", getServiceURL(), SECURITIES);
    }

    private String formSecuritiesGetUrl(Long securityId) {
        return String.format("%s%s", formSecuritiesUrl(), securityId);
    }

    public ResponseEntity<User> executeGetUser(Long usersId) {
        return restTemplate.getForEntity(formUserGetUrl(usersId), User.class, new HttpEntity<>(headers));
    }

    public ResponseEntity<User> executePostUsers(User user) {
        return restTemplate.postForEntity(formUserUrl(), new HttpEntity<>(user, headers), User.class);
    }

    public ResponseEntity<Security> executeGetSecurity(Long securityId) {
        return restTemplate.getForEntity(formSecuritiesGetUrl(securityId), Security.class, new HttpEntity<>(headers));
    }

    public ResponseEntity<Security> executePostSecurities(Security security) {
        return restTemplate.postForEntity(formSecuritiesUrl(), new HttpEntity<>(security, headers), Security.class);
    }


}
