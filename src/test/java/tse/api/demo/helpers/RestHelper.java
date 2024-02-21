package tse.api.demo.helpers;

import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import tse.api.demo.model.Security;
import tse.api.demo.model.User;
import tse.api.demo.service.v2.ExchangeService;

import java.util.Collections;

@Component
public class RestHelper {
    @Autowired
    private RestTemplate restTemplate;
    private static final Logger log = LoggerFactory.getLogger(ExchangeService.class);

    @Getter
    @Setter
    private HttpHeaders headers = new HttpHeaders();
    @Getter
    private String ServiceURL = "http://localhost:8080";
    @Getter
    private String USERS = "/users";
    @Getter
    private String SECURITIES = "/securities";

    public RestHelper() {
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
    }

    private String formUserUrl() {
        return String.format("%s%s", getServiceURL(), USERS);
    }

    private String formUserGetUrl(Long usersId) {
        return String.format("%s/%s", formUserUrl(), usersId.toString());
    }

    private String formSecuritiesUrl() {
        return String.format("%s%s", getServiceURL(), SECURITIES);
    }

    private String formSecuritiesGetUrl(Long securityId) {
        return String.format("%s/%s", formSecuritiesUrl(), securityId);
    }

    public ResponseEntity<User> executeGetUser(Long usersId) {
        return restTemplate.getForEntity(formUserGetUrl(usersId), User.class, new HttpEntity<>(headers));
    }

    public ResponseEntity<User> executePostUsers(User user) {
        return restTemplate.postForEntity(formUserUrl(), new HttpEntity<>(user, headers), User.class);
    }

    public ResponseEntity<Security> executeGetSecurity(Long securityId) {
        String requestUrl = formSecuritiesGetUrl(securityId);
        HttpEntity<Security> httpEntity = new HttpEntity<>(headers);
        logDetails(requestUrl, httpEntity);
        ResponseEntity<Security> responseEntity = restTemplate.getForEntity(requestUrl, Security.class, new HttpEntity<>(headers));
        logDetails(requestUrl, responseEntity);
        return responseEntity;
    }

    public ResponseEntity<Security> executePostSecurities(Security security) {
        String requestUrl = formSecuritiesUrl();
        HttpEntity<Security> httpEntity = new HttpEntity<>(security, headers);
        logDetails(requestUrl, httpEntity);
        ResponseEntity<Security> responseEntity = restTemplate.postForEntity(requestUrl, httpEntity, Security.class);
        logDetails(requestUrl, responseEntity);
        return responseEntity;
    }

    private void logDetails(String requestUrl, HttpEntity httpEntity) {
        log.info(String.format("Request URL: %s", requestUrl));
        if (httpEntity.getBody() != null) {
            log.info(httpEntity.getBody() + "\n");
        }
    }


}
