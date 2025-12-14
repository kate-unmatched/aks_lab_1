package org.tasktracker.service;

import org.tasktracker.entity.UserEntity;

public interface UserService extends CrudService<UserEntity> {

    UserEntity findByEmail(String email);

}
