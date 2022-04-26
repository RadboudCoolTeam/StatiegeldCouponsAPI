package io.github.radboudcoolteam.statiegeldcouponsapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class StatiegeldCouponsApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(StatiegeldCouponsApiApplication.class, args);
    }

}
