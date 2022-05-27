package io.github.radboudcoolteam.statiegeldcouponsapi.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "coupon")
public class Coupon {

    @Setter
    public long id;

    private String date;

    private String money;

    private String barcode;

    private SupermarketChain supermarketChain;

    @Setter
    private long userId;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Setter
    private long databaseId;

    public void update(Coupon coupon) {
        this.id = coupon.id;
        this.date = coupon.date;
        this.money = coupon.money;
        this.barcode = coupon.barcode;
        this.supermarketChain = coupon.supermarketChain;
        this.userId = coupon.userId;
        this.databaseId = coupon.databaseId;
    }
}
