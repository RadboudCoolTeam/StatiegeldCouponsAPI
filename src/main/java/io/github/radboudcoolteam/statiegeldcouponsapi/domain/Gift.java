package io.github.radboudcoolteam.statiegeldcouponsapi.domain;

import javax.persistence.Id;

public class Gift {

    @Id
    private String id;

    private Coupon coupon;
}
