package org.tasktracker.service;

import jakarta.ejb.Stateless;
import jakarta.persistence.TypedQuery;

import org.tasktracker.entity.UserEntity;

import java.util.List;

@Stateless
public class UserServiceImpl extends CrudServiceImpl<UserEntity> implements UserService {

    public UserServiceImpl() {
        super(UserEntity.class);
    }

    @Override
    public UserEntity findByEmail(String email) {
        TypedQuery<UserEntity> query = em.createQuery(
                "SELECT u FROM UserEntity u WHERE u.email = :email",
                UserEntity.class
        );
        query.setParameter("email", email);

        List<UserEntity> result = query.getResultList();
        return result.isEmpty() ? null : result.get(0);
    }
}
