package io.github.radboudcoolteam.statiegeldcouponsapi.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "coupon")
public class Coupon {

    @Id
    public String id;

    private String date;

    private String money;

    private String barcode;

    private SupermarketChain supermarketChain;

    private String userId;
}
