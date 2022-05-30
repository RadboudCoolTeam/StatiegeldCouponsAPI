package io.github.radboudcoolteam.statiegeldcouponsapi.domain;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "image")
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Setter
    private long id;

    @Column(name = "width")
    private long width;

    @Column(name = "height")
    private long height;

    @Column(name = "data", length = 26214400)
    private byte[] data;
}
