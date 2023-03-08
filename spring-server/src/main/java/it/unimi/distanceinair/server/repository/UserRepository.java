package it.unimi.distanceinair.server.repository;

import it.unimi.distanceinair.server.model.UserEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.UUID;

@Repository
public interface UserRepository
        extends CrudRepository<UserEntity, UUID> {
        @Transactional
        UserEntity findUserEntityByUsername(String username);
}