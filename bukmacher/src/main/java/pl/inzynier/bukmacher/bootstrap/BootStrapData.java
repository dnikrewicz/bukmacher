
package pl.inzynier.bukmacher.bootstrap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import pl.inzynier.bukmacher.domain.Coupon;
import pl.inzynier.bukmacher.domain.Game;
import pl.inzynier.bukmacher.domain.Person;
import pl.inzynier.bukmacher.repositories.CouponRepository;
import pl.inzynier.bukmacher.repositories.GameRepository;
import pl.inzynier.bukmacher.repositories.PersonRepository;

import java.util.Random;

@Component
public class BootStrapData implements CommandLineRunner {

    private final CouponRepository couponRepository;
    private final GameRepository gameRepository;
    private final PersonRepository personRepository;


    public BootStrapData(CouponRepository couponRepository, GameRepository gameRepository, PersonRepository personRepository) {
        this.couponRepository = couponRepository;
        this.gameRepository = gameRepository;
        this.personRepository = personRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        int i;
        String[] name = {"Senegal - Katar","Francja - Anglia", "Polska - Niemcy", "Brazylia - Ghana", "Portugalia - Urugwaj", "Ekwador - Senegal", "Iran - USA", "Walia - Anglia", "Australia - Dania", "Tunezja - Francja"};
        for(int a = 0;a<10;a++){
            Game game = new Game(name[a],"1",3.5,0);
            Game game1 = new Game(name[a],"X",1.5,0);
            Game game2 = new Game( name[a],"2",2.2,0);
            gameRepository.save(game);
            gameRepository.save(game1);
            gameRepository.save(game2);

        }
        for(i=0; i<100; i++){
            Person person1 = new Person("asd" + i, "asdd",100);
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            String encodedPassword = encoder.encode(person1.getPassword());
            person1.setPassword(encodedPassword);
            personRepository.save(person1);
            //Coupon coupon = new Coupon(20,0,0,0);
            //couponRepository.save(coupon);

            //Coupon coupon = new Coupon(20,0,0,0);
            //couponRepository.save(coupon);
            //Coupon coupon = couponRepository.findById(rand_int).get();
            //Game game = gameRepository.findById(rand_int).get();
            //Person person = personRepository.findById(rand_int).get();
            /*
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
            coupon.setPerson(person1);
            couponRepository.save(coupon);
            */
        }


        /*
        int a;
        for(a = 0; a<=100;a++){
            Coupon coupon = new Coupon(20,0,0,0);
            Random rand = new Random();
            int count = 100;
            long rand_int = rand.nextInt(count);
            //Coupon coupon = couponRepository.findById(rand_int).get();
            Game game = gameRepository.findById(rand_int).get();
            Person person = personRepository.findById(rand_int).get();
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
            couponRepository.save(coupon);
        }
        */
       // System.out.println("User" + personRepository.count());

        //Game game1 = new Game("BarcelonaReal", 1, 1.5,0);
        //Game game2 = new Game("BorussiaBayern", 1, 1.8,0);
        //Coupon coupon1 = new Coupon(20,3,60,0);
        //Coupon coupon2 = new Coupon(30,3,90,0);
       // game1.getCoupons().add(coupon1);
       // coupon1.getGames().add(game1);
        //gameRepository.save(game1);
        //coupon1.addGame(game1);
        //coupon1.addGame(game2);
        //couponRepository.save(coupon1);
        //coupon2.getGames().add(game2);
        //coupon2.addGame(game2);
        //couponRepository.save(coupon2);
        //coupon1.setPerson(person1);
        //person1.getCoupons().add(coupon1);
        //personRepository.save(person1);

        //gameRepository.save(game1);
        //System.out.println("czek" + couponRepository.count());
       // Long test = person1.getId();
        //System.out.println("czekkk  " + person1.getCoupons().size() );//+ "  asdsadd " + test);


    }
}
