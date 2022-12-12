package pl.inzynier.bukmacher.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import pl.inzynier.bukmacher.domain.Coupon;
import pl.inzynier.bukmacher.domain.Game;
import pl.inzynier.bukmacher.domain.Person;
import pl.inzynier.bukmacher.repositories.CouponRepository;
import pl.inzynier.bukmacher.repositories.GameRepository;
import pl.inzynier.bukmacher.repositories.PersonRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
//@RequestMapping("/persons")
public class PersonController {
    @Autowired
    PersonRepository personRepository;

    @Autowired
    GameRepository gameRepository;

    @Autowired
    CouponRepository couponRepository;

    List<Game> listCouponGames = new ArrayList<>();
    double count = 0;
    double personPoints = 0;

    @GetMapping("")
    public ModelAndView viewHomePage() {
        ModelAndView mav = new ModelAndView("index");
        return mav;
    }

    @GetMapping("/test")
    public ModelAndView testHomePage() {
        ModelAndView mav = new ModelAndView("loggedTest");
        List<Game> list = gameRepository.findAll();
        mav.addObject("games", list);
        return mav;
    }

    @GetMapping("/register")
    public ModelAndView showSignUpForm(){
        Person person = new Person();
        ModelAndView mav = new ModelAndView("signup_form");
        mav.addObject("person", person);
        return mav;
    }


    @PostMapping(value = "/process_register")
    public ModelAndView saveSignUpForm(@ModelAttribute Person person){
        person.setPoints(100);
        String loginn = person.getLogin();
        Person person1 = personRepository.findByLogin(loginn);
        if (person1 == null){
            System.out.println("gitara");
        }else{
            System.out.println("jest juz taki");
            ModelAndView mav2 = new ModelAndView("bad_login_signup_form");
            return mav2;
        }
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodedPassword = encoder.encode(person.getPassword());
        person.setPassword(encodedPassword);
        personRepository.save(person);
        ModelAndView mav1 = new ModelAndView("index");
        return mav1;
    }

    @GetMapping("/admin")
    public ModelAndView showAdminForm(){
        ModelAndView mav = new ModelAndView("admin");
        return mav;
    }

    //zmiana przez admina statusu gry i odswiezenie statusu kuponow
    @PostMapping(value = "/admin/game")
    public ModelAndView changeGameStatus(long id, int result){

        ModelAndView mav1 = new ModelAndView("admin");
        Game game1 = gameRepository.findGameById(id);
        if (game1 == null){
            System.out.println("nie ma takiej gry");
        }else{
            game1.setResult(result);
            gameRepository.save(game1);
        }
        List<Coupon> list = couponRepository.findAll();


        for(int i=0;i<list.size();i++){ //lista kuponow
            int correctType = 0;
            Coupon coupon = list.get(i);
            Set<Game> listpom = coupon.getGames();
            List<Game> listpom1 = new ArrayList<>();
            listpom1.addAll(listpom);
            for (int a=0; a<listpom1.size();a++){ //lista gier
                Game game = listpom1.get(a);
                if (game.getResult()==2 && coupon.getStatus()
                        != 1 && coupon.getStatus() != 2){
                    coupon.setStatus(2);
                    couponRepository.save(coupon);
                    System.out.println("USTAWIONY STATUS 2");

                }else if(game.getResult()==1 && coupon.getStatus()
                        != 2 && coupon.getStatus() != 1) {
                    correctType++;
                    System.out.println("CORRECT TYPE DODANY");
                }else{
                    System.out.println("mecz bez STATUSU");
                }
                if(correctType == listpom1.size()){
                    correctType=0;
                    Person person = coupon.getPerson();
                    double couponWon = coupon.getWinning_amount();
                    coupon.setStatus(1);
                    person.setPoints(person.getPoints() + couponWon);
                    couponRepository.save(coupon);
                    personRepository.save(person);
                    System.out.println("DOBRY BET!!!" + coupon.getId());
                }else{
                    System.out.println("za malo dobrych vetow");
                }


            }

        }

        return mav1;
    }

    public void refreshCouponsStatus(){
        List<Coupon> list = couponRepository.findAll();
        for(int i=0;i<list.size();i++){
            Coupon coupon = list.get(i);
            Set<Game> listpom = coupon.getGames();
            List<Game> listpom1 = new ArrayList<>();
            listpom1.addAll(listpom);
            for (int a=0; a<listpom1.size();a++){
                Game game = listpom1.get(a);
                if (game.getResult()==2){
                    coupon.setStatus(2);

                }else if(game.getResult()==1){
                    Person person = coupon.getPerson();
                    double couponWon = coupon.getWinning_amount();
                    coupon.setStatus(1);
                    person.setPoints(person.getPoints() + couponWon);
                }
            }
            //System.out.println(listpom.size());
        }
    }

    @GetMapping({"/bukmacherDominik"})
    public ModelAndView loggedPerson() {
        ModelAndView mav = new ModelAndView("logged");
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();
        String login = authentication.getName();
        Person person1 = personRepository.findByLogin(login);
        Long id = person1.getId();
        System.out.println(id);
        personPoints = person1.getPoints();
        List<Coupon> list1 = couponRepository.findByPersonID(person1);
        mav.addObject("coupons",list1);
        List<Game> list = gameRepository.findAll();
        mav.addObject("games", list);
        List<Person> list2 = personRepository.findAll();
        mav.addObject("persons", list2);
        mav.addObject("couponGames", listCouponGames);
        mav.addObject("count_amount", count);
        mav.addObject("personPoints", personPoints);
        return mav;
    }
    //String c ="";

    @GetMapping("/personCoupons")
    public ModelAndView personCoupons() {
        ModelAndView mav = new ModelAndView("personCoupons");
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();
        String login = authentication.getName();
        Person person1 = personRepository.findByLogin(login);
        List<Coupon> list = couponRepository.findByPersonID(person1);
        List<Coupon> wonCoupons = new ArrayList<Coupon>();
        List<Coupon> lostCoupons = new ArrayList<Coupon>();
        List<Coupon> openCoupons = new ArrayList<Coupon>();

        int pom = list.size();
        for (int i = 0; i<pom;i++){
            Coupon coupon = list.get(i);
            int status = coupon.getStatus();
            if (status == 0){
                openCoupons.add(coupon);
            }else if (status == 1){
                wonCoupons.add(coupon);
            }else{
                lostCoupons.add(coupon);
            }
        }
        personPoints = person1.getPoints();
        mav.addObject("personPoints", personPoints);
        mav.addObject("coupons",list);
        mav.addObject("wonCoupons",wonCoupons);
        mav.addObject("lostCoupons",lostCoupons);
        mav.addObject("openCoupons",openCoupons);
        return mav;
    }

    List<Integer> couponGames = new ArrayList<Integer>();
    @GetMapping(value = "/bukmacherDominik/{id}")
    public ModelAndView addGameToCoupon(@PathVariable int id){
        long idd = id;
        int a = id/3;
        int pom = id - (a*3);
        if(pom == 1){
            if(couponGames.contains(id + 1) == true ||
                    couponGames.contains(id+2) == true ||
                    couponGames.contains(id) == true){
                System.out.println("nie mozna takiego");
                return loggedPerson();
            }else{
                couponGames.add(id);
                System.out.println(couponGames);
                couponRefreshAdd();
            }
        }
        if(pom == 2){
            if(couponGames.contains(id - 1) == true ||
                    couponGames.contains(id + 1) == true){
                System.out.println("nie mozna takiego");
                return loggedPerson();
            }else{
                couponGames.add(id);
                System.out.println(couponGames);
                couponRefreshAdd();
            }
        }
        if(pom == 0){
            if(couponGames.contains(id - 1) == true ||
                    couponGames.contains(id - 2) == true){
                System.out.println("nie mozna takiego");
                return loggedPerson();
            }else{
                couponGames.add(id);
                System.out.println(couponGames);
                couponRefreshAdd();
            }
        }
        return loggedPerson();
    }

    @GetMapping(value = "/bukmacherDominik/delete/{id}")
    public ModelAndView deleteGameFromCoupon(@PathVariable int id){
        if(couponGames.contains(id) == false){
            System.out.println("nie ma takiego");
        }else{
            couponGames.remove(couponGames.indexOf(id));
            long idd = id;
            Game game = gameRepository.findGameById(idd);
            listCouponGames.remove(game);
        }
        return loggedPerson();
    }

    @GetMapping(value = "/bukmacherDominik/couponRefresh")
    public ModelAndView couponRefreshAdd(){
        int size = couponGames.size();
        int element = couponGames.get(couponGames.size()-1);
        long id = element;
        Game game = gameRepository.findGameById(id);
        listCouponGames.add(game);
        return loggedPerson();
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

    @PostMapping(value = "/counting_amount")
    public ModelAndView countingAmount(double amount){
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();
        String login = authentication.getName();
        Person person1 = personRepository.findByLogin(login);
        double checkMoney = person1.getPoints();
        if(checkMoney < amount){
            return loggedPerson();
        }
        long personId = person1.getId();
        int size = couponGames.size();
        Coupon coupon = new Coupon(amount,0,0,0);
        couponRepository.save(coupon);
        long couponId = coupon.getId();
        for(int i = 0; i<size;i++){
            long gameId = couponGames.get(i);
            addGameToCoupon(couponId,gameId,personId);
        }
        person1.setPoints(checkMoney - amount);
        personRepository.save(person1);
        couponGames.clear();
        listCouponGames.clear();
        System.out.println(couponGames);

        return loggedPerson();
    }

    @GetMapping(value = "/count/{amount}")
    public ModelAndView countingAmountTest(@PathVariable double amount){

        return loggedPerson();
    }

    @GetMapping({"/pointsPerson"})
    public ModelAndView showPointsPersons() {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();
        String login = authentication.getName();
        Person person1 = personRepository.findByLogin(login);
        personPoints = person1.getPoints();
        return loggedPerson();
    }

    @GetMapping({"/showPersons",  "/list"})
    public ModelAndView showPersons() {
        ModelAndView mav = new ModelAndView("list-persons");
        List<Person> list = personRepository.findAll();
        mav.addObject("persons", list);
        return mav;
    }

    @GetMapping({"/persons"})
    List<Person> getPersons(){
        return personRepository.findAll();
    }

    @PostMapping({"/persons"})
    Person createPerson(@RequestBody Person person){
        return personRepository.save(person);
    }
}
