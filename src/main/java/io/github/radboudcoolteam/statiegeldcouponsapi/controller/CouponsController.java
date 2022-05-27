package io.github.radboudcoolteam.statiegeldcouponsapi.controller;

import io.github.radboudcoolteam.statiegeldcouponsapi.repo.CouponRepository;
import io.github.radboudcoolteam.statiegeldcouponsapi.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/coupons")
public class CouponsController {

    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private UserRepository userRepository;

}
