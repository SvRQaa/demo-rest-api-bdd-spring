package tse.api.demo.helpers;

import org.springframework.stereotype.Component;
import tse.api.demo.model.Security;

@Component
public class SecurityHelper {

    public SecurityHelper() {}

    public Security formSecurity(String name) {
        return Security.builder()
                .name(name)
                .build();
    }
}
