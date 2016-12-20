package com.mm.appdirect.techchallenge.domain;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface UserRepository extends PagingAndSortingRepository<User, Long> {

    List<User> findByLastName(String lastName);

	User findByUuid(String uuid);
}
