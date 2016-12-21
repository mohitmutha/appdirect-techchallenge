package com.mm.appdirect.techchallenge.domain;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface UserRepository extends PagingAndSortingRepository<User, Long> {
	User findByUuid(String uuid);
	List<User> findByAccount(Account account);
}
