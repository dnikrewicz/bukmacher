package pl.inzynier.bukmacher.domain;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Coupon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private double amount;
    private double total_course;
    private double winning_amount;
    private int status;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "person_id", referencedColumnName = "id")
    private Person person;

    @ManyToMany
    @JoinTable(name = "game_coupon",
            joinColumns = @JoinColumn(name = "coupon_id"),
    inverseJoinColumns = @JoinColumn(name = "game_id"))
    public Set<Game> games = new HashSet<>();

    public void addGame(Game game) {
        this.games.add(game);
        game.getCoupons().add(this);
    }

    public void removeGame(Game game) {
        this.games.remove(game);
        game.getCoupons().remove(this);
    }

    public Coupon() {
    }

    public Coupon(double amount, double total_course,
                  double winning_amount, int status,
                  Person person) {
        this.amount = amount;
        this.total_course = total_course;
        this.winning_amount = winning_amount;
        this.status = status;
        this.person = person;
    }

    public Coupon(double amount, double total_course, double winning_amount, int status) {
        this.amount = amount;
        this.total_course = total_course;
        this.winning_amount = winning_amount;
        this.status = status;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getTotal_course() {
        return total_course;
    }

    public void setTotal_course(double total_course) {
        this.total_course = total_course;
    }

    public double getWinning_amount() {
        return winning_amount;
    }

    public void setWinning_amount(double winning_amount) {
        this.winning_amount = winning_amount;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Set<Game> getGames() {
        return games;
    }

    public void setGames(Set<Game> games) {
        this.games = games;
    }

    @Override
    public String toString() {
        return "Coupon{" +
                "id=" + id +
                ", amount=" + amount +
                ", total_course=" + total_course +
                ", winning_amount=" + winning_amount +
                ", status=" + status +
                ", games=" + games +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Coupon coupon = (Coupon) o;

        return id != null ? id.equals(coupon.id) :
                coupon.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
