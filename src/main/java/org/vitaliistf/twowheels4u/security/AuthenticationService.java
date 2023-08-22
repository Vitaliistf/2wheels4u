package org.vitaliistf.twowheels4u.security;

import org.vitaliistf.twowheels4u.models.User;

public interface AuthenticationService {

    User register(String email, String password, String firstName, String lastName);

    User login(String email, String password);

}
