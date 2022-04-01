package com.griddynamics.mainservice.Repository;

import com.griddynamics.mainservice.domain.User;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserInfoRepository extends ReactiveMongoRepository<User, String> {
}
