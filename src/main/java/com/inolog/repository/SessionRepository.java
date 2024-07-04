package com.inolog.repository;

import com.inolog.domain.Session;
import com.inolog.domain.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface SessionRepository extends CrudRepository<Session, Long> {

}
