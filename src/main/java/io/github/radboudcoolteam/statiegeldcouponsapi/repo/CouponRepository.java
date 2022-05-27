package io.github.radboudcoolteam.statiegeldcouponsapi.repo;

import io.github.radboudcoolteam.statiegeldcouponsapi.domain.Coupon;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface CouponRepository extends MongoRepository<Coupon, String> {
    Coupon findCouponById(long id);
    List<Coupon> findCouponsByUserId(long userId);
}
