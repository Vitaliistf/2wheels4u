package org.vitaliistf.twowheels4u.service;

import java.util.List;
import org.vitaliistf.twowheels4u.model.User;

public interface UserService {
    User save(User user);

    User update(User user);

    User findById(Long id);

    User findByEmail(String email);

    List<User> findByRoles(User.Role roleName);

}
