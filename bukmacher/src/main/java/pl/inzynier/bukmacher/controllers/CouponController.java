package pl.inzynier.bukmacher.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.inzynier.bukmacher.domain.Coupon;
import pl.inzynier.bukmacher.domain.Game;
import pl.inzynier.bukmacher.domain.Person;
import pl.inzynier.bukmacher.repositories.CouponRepository;
import pl.inzynier.bukmacher.repositories.GameRepository;
import pl.inzynier.bukmacher.repositories.PersonRepository;

import java.util.List;

@RestController
@RequestMapping("/coupons")
public class CouponController {

    @Autowired
    CouponRepository couponRepository;

    @Autowired
    GameRepository gameRepository;

    @Autowired
    PersonRepository personRepository;

    @GetMapping
    List<Coupon> getCoupon(){
        return couponRepository.findAll();
    }

    @PostMapping
    Coupon createCoupon(@RequestBody Coupon coupon){
        return couponRepository.save(coupon);
    }

    @PutMapping("/{couponIdd}/games/{gameId}/person/{personId}")
    Coupon addGameToCoupon(
            @PathVariable Long couponIdd,
            @PathVariable Long gameId,
            @PathVariable Long personId
    ){
        Coupon coupon = couponRepository.findById(couponIdd).get();
        Game game = gameRepository.findById(gameId).get();
        Person person = personRepository.findById(personId).get();
        double couponCourse = coupon.getTotal_course();
        double gameCourse = game.getCourse();
        double couponAmount = coupon.getAmount();

        if (couponCourse == 0){
            coupon.setTotal_course(gameCourse);
        } else {
            coupon.setTotal_course(couponCourse * gameCourse);
        }
        double couponCourse1 = coupon.getTotal_course();
        coupon.setWinning_amount(couponAmount * couponCourse1);

        coupon.games.add(game);
        coupon.setPerson(person);
        return couponRepository.save(coupon);
    }

    @PutMapping("/{couponIdd}/status/{couponStatus}")
    Coupon changeCouponStatus(
            @PathVariable Long couponIdd,
            @PathVariable int couponStatus
    ){
        Coupon coupon = couponRepository.findById(couponIdd).get();
        Person person = coupon.getPerson();
        double couponWon = coupon.getWinning_amount();
        coupon.setStatus(couponStatus);
        if (couponStatus == 1){
            person.setPoints(person.getPoints() + couponWon);
        } else {
            System.out.println("brak wygranej");
        }
        personRepository.save(person);
        return couponRepository.save(coupon);
    }
}
