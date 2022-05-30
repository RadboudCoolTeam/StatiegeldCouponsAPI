package io.github.radboudcoolteam.statiegeldcouponsapi.controller;

import io.github.radboudcoolteam.statiegeldcouponsapi.domain.Coupon;
import io.github.radboudcoolteam.statiegeldcouponsapi.domain.Image;
import io.github.radboudcoolteam.statiegeldcouponsapi.domain.Pair;
import io.github.radboudcoolteam.statiegeldcouponsapi.domain.User;
import io.github.radboudcoolteam.statiegeldcouponsapi.repo.CouponRepository;
import io.github.radboudcoolteam.statiegeldcouponsapi.repo.ImagesRepo;
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

    private long newImageId;

    private final int NEW_COUPON = 0;

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private ImagesRepo imageRepository;

    @Autowired
    public UsersController(UserRepository userRepository, CouponRepository couponRepository, ImagesRepo imageRepository) {
        this.userRepository = userRepository;
        this.couponRepository = couponRepository;
        this.imageRepository = imageRepository;
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
                .map(Coupon::getId)
                .sorted()
                .limit(1)
                .findAny()
                .orElse(0L) + 1;

        newImageId = imageRepository
                .findAll()
                .stream()
                .map(Image::getId)
                .sorted()
                .limit(1)
                .findAny()
                .orElse(0L) + 1;
    }

    private boolean authoriseAPIUser(User user) {
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

    @PostMapping("/{id}/coupons")
    public ResponseEntity<List<Coupon>> getUserCoupons(@PathVariable Long id, @RequestBody User user) {

        if (!authoriseAPIUser(user) || user.getId() != id) {
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

    @PostMapping("/{id}/avatar")
    public ResponseEntity<Image> getUserAvatar(@PathVariable Long id, @RequestBody User user) {

        if (!authoriseAPIUser(user) || user.getId() != id) {
            return ResponseEntity.badRequest()
                    .build();
        }

        if (userRepository.existsById(id)) {
            return ResponseEntity.accepted()
                    .body(imageRepository.getImageById(user.getImageId()));
        } else {
            return ResponseEntity.notFound()
                    .build();
        }
    }

    @PostMapping("{id}/updateCoupon")
    public ResponseEntity<Coupon> updateCoupon(@PathVariable Long id, @RequestBody Pair<User, Coupon> pair) {

        User user = pair.first;
        Coupon coupon = pair.second;

        if (!authoriseAPIUser(user) || user.getId() != id) {
            return ResponseEntity.badRequest()
                    .build();
        }

        if (coupon.getUserId() == NEW_COUPON && coupon.getId() == NEW_COUPON) {
            coupon.setId(newCouponId++);
            coupon.setUserId(user.getId());

            Coupon couponInDb = couponRepository.save(coupon);

            return ResponseEntity.accepted().body(couponInDb);
        } else {
            if (coupon.getUserId() == user.getId()) {
                Coupon couponInDb = couponRepository.findCouponById(coupon.getId());
                if (couponInDb != null) {
                    couponInDb.update(coupon);
                    couponRepository.save(couponInDb);

                    return ResponseEntity.accepted().body(couponInDb);
                }
            }
        }

        return ResponseEntity.badRequest().body(null);
    }

    @PostMapping("{id}/delete")
    public ResponseEntity<String> deleteCoupon(@PathVariable long id, @RequestBody Pair<User, Coupon> pair) {
        User user = pair.first;
        Coupon coupon = pair.second;

        if (!authoriseAPIUser(user) || user.getId() != id) {
            return ResponseEntity.badRequest()
                    .build();
        }

        if (coupon.getUserId() == user.getId()) {
            Coupon couponInDb = couponRepository.findCouponById(coupon.getId());
            if (couponInDb != null) {
                couponRepository.delete(couponInDb);

                return ResponseEntity.accepted().body("OK");
            }
        }

        return ResponseEntity.badRequest().body("Unable to update!");
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
                EmailValidator.getInstance().isValid(user.getEmail());
    }

    @PostMapping("/new")
    public ResponseEntity<String> createUser(@RequestBody Pair<User, Image> pair) {

        User user = pair.first;

        Image image = pair.second;

        if (validateUser(user) && !userExists(user)) {
            try {

                image.setId(newImageId++);
                imageRepository.save(image);

                MessageDigest digest = MessageDigest.getInstance("SHA-256");

                String hash = DatatypeConverter.printHexBinary(digest.digest(user.getPassword().getBytes(StandardCharsets.UTF_8)));

                user.setPassword("");
                user.setPasswordHash(hash);

                user.setId(newUserId++);
                user.setImageId(image.getId());
                User userInDb = userRepository.save(user);

                return ResponseEntity.accepted()
                        .body(String.valueOf(userInDb.getId()));
            } catch (NoSuchAlgorithmException e) {
                return ResponseEntity.badRequest().body("Failed to create user!");
            }
        } else {
            return ResponseEntity.badRequest().body("Failed to create user!");
        }
    }

    @PostMapping("/auth")
    public ResponseEntity<User> authorizeUser(@RequestBody User user) {
        if (validateUser(user) && userExists(user) && authoriseAPIUser(user)) {

            User userInDb = userRepository.getUserByEmail(user.getEmail()).get(0);

            long id = userInDb.getId();

            String name = userInDb.getName();

            user.setId(id);
            user.setName(name);

            return ResponseEntity.accepted().body(user);
        }

        return ResponseEntity.badRequest().build();
    }
}
