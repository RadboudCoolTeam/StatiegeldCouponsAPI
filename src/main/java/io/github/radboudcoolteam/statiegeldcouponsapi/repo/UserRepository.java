package io.github.radboudcoolteam.statiegeldcouponsapi.repo;

import io.github.radboudcoolteam.statiegeldcouponsapi.domain.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends MongoRepository<User, Long> {
    User getUserById(long id);

    List<User> getUserByEmail(String email);
}
