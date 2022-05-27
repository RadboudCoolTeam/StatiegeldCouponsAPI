package io.github.radboudcoolteam.statiegeldcouponsapi.controller;

import io.github.radboudcoolteam.statiegeldcouponsapi.domain.Gift;
import io.github.radboudcoolteam.statiegeldcouponsapi.repo.GiftsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/gifts")
public class GiftsController {

    @Autowired
    private GiftsRepository giftsRepository;

    @GetMapping("/{id}")
    public ResponseEntity<Gift> getGift(@PathVariable String id) {
        if (giftsRepository.existsById(id)) {
            return ResponseEntity.accepted()
                    .body(giftsRepository.findGiftById(id));
        } else {
            return ResponseEntity.notFound()
                    .build();
        }
    }

    @DeleteMapping("/{id}/close")
    public ResponseEntity<String> closeGift(@PathVariable String id) {
        if (giftsRepository.existsById(id)) {
            giftsRepository.deleteById(id);
            return ResponseEntity.accepted()
                    .body("OK");
        } else {
            return ResponseEntity.notFound()
                    .build();
        }
    }


}
