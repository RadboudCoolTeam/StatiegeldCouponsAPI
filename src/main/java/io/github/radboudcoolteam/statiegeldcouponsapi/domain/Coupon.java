package io.github.radboudcoolteam.statiegeldcouponsapi.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "coupon")
public class Coupon {

    @Setter
    public UUID localId;

    private String date;

    private String money;

    private String barcode;

    private SupermarketChain supermarketChain;

    @Setter
    private long userId;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Setter
    private long id;

    public void update(Coupon coupon) {
        this.localId = coupon.localId;
        this.date = coupon.date;
        this.money = coupon.money;
        this.barcode = coupon.barcode;
        this.supermarketChain = coupon.supermarketChain;
        this.userId = coupon.userId;
        this.id = coupon.id;
    }
}
