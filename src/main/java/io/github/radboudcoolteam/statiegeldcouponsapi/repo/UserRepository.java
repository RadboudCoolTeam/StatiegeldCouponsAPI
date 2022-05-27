package io.github.radboudcoolteam.statiegeldcouponsapi.repo;

import io.github.radboudcoolteam.statiegeldcouponsapi.domain.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    User getUserById(String id);
}
