package com.ric.dev2.repository;

import com.ric.dev2.repository.entity.RegistrationMessageEntity;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;

@Repository
public interface MessageRepository extends CrudRepository<RegistrationMessageEntity, String> {
}
