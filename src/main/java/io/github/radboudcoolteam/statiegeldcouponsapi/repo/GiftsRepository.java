package io.github.radboudcoolteam.statiegeldcouponsapi.repo;

import io.github.radboudcoolteam.statiegeldcouponsapi.domain.Gift;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GiftsRepository extends MongoRepository<Gift, String> {
    Gift findGiftById(String id);
}
