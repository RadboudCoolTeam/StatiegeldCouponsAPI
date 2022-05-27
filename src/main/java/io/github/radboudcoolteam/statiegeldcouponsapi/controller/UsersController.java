package io.github.radboudcoolteam.statiegeldcouponsapi.controller;

import io.github.radboudcoolteam.statiegeldcouponsapi.domain.Coupon;
import io.github.radboudcoolteam.statiegeldcouponsapi.domain.Pair;
import io.github.radboudcoolteam.statiegeldcouponsapi.domain.User;
import io.github.radboudcoolteam.statiegeldcouponsapi.repo.CouponRepository;
import io.github.radboudcoolteam.statiegeldcouponsapi.repo.UserRepository;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.xml.bind.DatatypeConverter;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/users")
public class UsersController {

    private long newUserId;

    private long newCouponId;

    private final int NEW_COUPON = 0;

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    public UsersController(UserRepository userRepository, CouponRepository couponRepository) {
        this.userRepository = userRepository;
        this.couponRepository = couponRepository;
        newUserId = userRepository
                .findAll()
                .stream()
                .map(User::getId)
                .sorted()
                .limit(1)
                .findAny()
                .orElse(0L) + 1;
        newCouponId = couponRepository
                .findAll()
                .stream()
                .map(Coupon::getDatabaseId)
                .sorted()
                .limit(1)
                .findAny()
                .orElse(0L) + 1;
    }

    public boolean authoriseUser(User user) {
        if (validateUser(user)) {
            try {
                if (userExists(user)) {

                    MessageDigest digest = MessageDigest.getInstance("SHA-256");

                    String hash = DatatypeConverter.printHexBinary(digest.digest(user.getPassword().getBytes(StandardCharsets.UTF_8)));

                    User userInDb = userRepository.getUserByEmail(user.getEmail()).get(0);

                    return hash.equals(userInDb.getPasswordHash());
                }
            } catch (NoSuchAlgorithmException e) {
                return false;
            }
        }
        return false;
    }


    @GetMapping("/")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.accepted()
                .body(userRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        if (userRepository.existsById(id)) {
            return ResponseEntity.accepted()
                    .body(userRepository.getUserById(id));
        } else {
            return ResponseEntity.notFound()
                    .build();
        }
    }

    @GetMapping("/{id}/coupons")
    public ResponseEntity<List<Coupon>> getUserCoupons(@PathVariable Long id, @RequestBody User user) {

        if (!authoriseUser(user) || user.getId() != id) {
            return ResponseEntity.badRequest()
                    .build();
        }

        if (userRepository.existsById(id)) {
            return ResponseEntity.accepted()
                    .body(couponRepository.findAll()
                            .stream()
                            .filter(e -> e.getUserId() == id)
                            .collect(Collectors.toList()));
        } else {
            return ResponseEntity.notFound()
                    .build();
        }
    }

    @PostMapping("{id}/updateCoupon")
    public ResponseEntity<String> updateCoupon(@PathVariable Long id, @RequestBody Pair<User, Coupon> pair) {

        User user = pair.first;
        Coupon coupon = pair.second;

        if (!authoriseUser(user) || user.getId() != id) {
            return ResponseEntity.badRequest()
                    .build();
        }

        if (coupon.getUserId() == NEW_COUPON && coupon.getDatabaseId() == NEW_COUPON) {
            coupon.setDatabaseId(newCouponId++);
            coupon.setUserId(user.getId());

            couponRepository.save(coupon);

            return ResponseEntity.accepted().body("OK");
        } else {
            if (coupon.getUserId() == user.getId()) {
                Coupon couponInDb = couponRepository.findCouponById(coupon.getDatabaseId());
                if (couponInDb != null) {
                    couponInDb.update(coupon);
                    couponRepository.save(coupon);

                    return ResponseEntity.accepted().body("OK");
                }
            }
        }

        return ResponseEntity.badRequest().body("Unable to update!");
    }

    @PostMapping("{id}/updateCoupons")
    public ResponseEntity<String> updateCoupons(@PathVariable Long id, @RequestBody User user, @RequestBody List<Coupon> coupons) {
        if (!authoriseUser(user) || user.getId() != id) {
            return ResponseEntity.badRequest()
                    .build();
        }

        int updated = 0;

        for (Coupon coupon : coupons) {

            if (coupon.getUserId() == NEW_COUPON && coupon.getDatabaseId() == NEW_COUPON) {
                coupon.setDatabaseId(newCouponId++);
                coupon.setUserId(user.getId());

                couponRepository.save(coupon);

                updated++;
            } else {
                if (coupon.getUserId() == user.getId()) {
                    Coupon couponInDb = couponRepository.findCouponById(coupon.getDatabaseId());
                    if (couponInDb != null) {
                        couponInDb.update(coupon);
                        couponRepository.save(coupon);

                        updated++;
                    }
                }
            }
        }

        return updated == coupons.size() ?
                ResponseEntity.accepted().body("OK") :
                ResponseEntity.badRequest().body("Unable to update!");
    }

    public boolean userExists(User user) {
        return userRepository.getUserByEmail(user.getEmail()).size() > 0;
    }

    public boolean validateUser(User user) {
        return user.getPassword() != null &&
                user.getName() != null &&
                user.getEmail() != null &&
                !user.getPassword().equals("") &&
                !user.getName().equals("") &&
                EmailValidator.getInstance().isValid(user.getEmail()) &&
                !userExists(user);
    }

    @PostMapping("/new")
    public ResponseEntity<String> createUser(@RequestBody User user) {
        if (validateUser(user)) {
            try {

                MessageDigest digest = MessageDigest.getInstance("SHA-256");

                String hash = DatatypeConverter.printHexBinary(digest.digest(user.getPassword().getBytes(StandardCharsets.UTF_8)));

                user.setPassword("");
                user.setPasswordHash(hash);

                user.setId(newUserId++);

                userRepository.save(user);

                return ResponseEntity.accepted()
                        .body("OK");
            } catch (NoSuchAlgorithmException e) {
                return ResponseEntity.badRequest().body("Failed to create user!");
            }
        } else {
            return ResponseEntity.badRequest().body("Failed to create user!");
        }
    }
}
