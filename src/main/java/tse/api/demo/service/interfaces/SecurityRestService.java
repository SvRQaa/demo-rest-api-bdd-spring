package tse.api.demo.service.interfaces;

import tse.api.demo.model.Security;

public interface SecurityRestService {
    Security createSecurity(Security security);
    Security findSecurityById(Long id);
    Security findSecurityByName(String name);
}
