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
import tse.api.demo.model.Order;
import tse.api.demo.model.Security;
import tse.api.demo.model.Trade;
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
    private final String TRADES = "/trades";
    private final String ORDERS = "/orders";
    private final String JOB = "/job";

    public RestHelper() {
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
    }

    private String formUserUrl() {
        return String.format("%s%s", getServiceURL(), USERS);
    }

    private String formUserGetUrl(Long usersId) {
        return String.format("%s/%s", formUserUrl(), usersId.toString());
    }

    private String formUserGetByUsernameUrl(String username) {
        return String.format("%s/search/%s", formUserUrl(), username);
    }

    private String formSecuritiesGetBySecNameUrl(String secName) {
        return String.format("%s/search/%s", formSecuritiesUrl(), secName);
    }

    private String formSecuritiesUrl() {
        return String.format("%s%s", getServiceURL(), SECURITIES);
    }
    private String formOrdersUrl() {
        return String.format("%s%s", getServiceURL(), ORDERS);
    }
    private String formTradesUrl() {
        return String.format("%s%s", getServiceURL(), TRADES);
    }
    private String formTradesJobUrl() {
        return String.format("%s%s%s", getServiceURL(), TRADES, JOB);
    }

    private String formSecuritiesGetUrl(Long securityId) {
        return String.format("%s/%s", formSecuritiesUrl(), securityId);
    }

    public ResponseEntity<User> executeGetUserById(Long usersId) {
        return restTemplate.getForEntity(formUserGetUrl(usersId), User.class, new HttpEntity<>(headers));
    }

    public ResponseEntity<User> executeGetUserByUsername(String username) {
        return restTemplate.getForEntity(formUserGetByUsernameUrl(username), User.class, new HttpEntity<>(headers));
    }

    public ResponseEntity<User> executePostUsers(User user) {
        String requestUrl = formUserUrl();
        HttpEntity<User> httpEntity = new HttpEntity<>(user, headers);
        logDetails(requestUrl, httpEntity);
        ResponseEntity<User> responseEntity = restTemplate.postForEntity(requestUrl, new HttpEntity<>(user, headers), User.class);
        logDetails(requestUrl, responseEntity);
        return responseEntity;
    }

    public ResponseEntity<Security> executeGetSecurity(Long securityId) {
        String requestUrl = formSecuritiesGetUrl(securityId);
        HttpEntity<Security> httpEntity = new HttpEntity<>(headers);
        logDetails(requestUrl, httpEntity);
        ResponseEntity<Security> responseEntity = restTemplate.getForEntity(requestUrl, Security.class, new HttpEntity<>(headers));
        logDetails(requestUrl, responseEntity);
        return responseEntity;
    }

    public ResponseEntity<Security> executeGetSecurityBySecurityName(String secName) {
        return restTemplate.getForEntity(formSecuritiesGetBySecNameUrl(secName), Security.class, new HttpEntity<>(headers));
    }

    public ResponseEntity<Security> executePostSecurities(Security security) {
        String requestUrl = formSecuritiesUrl();
        HttpEntity<Security> httpEntity = new HttpEntity<>(security, headers);
        logDetails(requestUrl, httpEntity);
        ResponseEntity<Security> responseEntity = restTemplate.postForEntity(requestUrl, httpEntity, Security.class);
        logDetails(requestUrl, responseEntity);
        return responseEntity;
    }

    public ResponseEntity<Order> executePostOrder(Order order) {
        String requestUrl = formOrdersUrl();
        HttpEntity<Order> httpEntity = new HttpEntity<>(order, headers);
        logDetails(requestUrl, httpEntity);
        ResponseEntity<Order> responseEntity = restTemplate.postForEntity(requestUrl, httpEntity, Order.class);
        logDetails(requestUrl, responseEntity);
        return responseEntity;
    }

    public ResponseEntity<Trade> executePostTrade(Trade trade) {
        String requestUrl = formTradesUrl();
        HttpEntity<Trade> httpEntity = new HttpEntity<>(trade, headers);
        logDetails(requestUrl, httpEntity);
        ResponseEntity<Trade> responseEntity = restTemplate.postForEntity(requestUrl, httpEntity, Trade.class);
        logDetails(requestUrl, responseEntity);
        return responseEntity;
    }

    public ResponseEntity<Trade> executePostTradeJob() {
        String requestUrl = formTradesJobUrl();
        log.info(String.format("Job call, url: %s", requestUrl));
        ResponseEntity<Trade> responseEntity = restTemplate.postForEntity(requestUrl, null, Trade.class);
        log.info(String.format("Job call result, url: %s", requestUrl));
        return responseEntity;
    }

    private void logDetails(String requestUrl, HttpEntity httpEntity) {
        log.info(String.format("Request URL: %s", requestUrl));
        if (httpEntity.getBody() != null) {
            log.info(httpEntity.getBody() + "\n");
        }
    }


}
