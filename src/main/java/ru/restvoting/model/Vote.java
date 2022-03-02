package ru.restvoting.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
@Table(name="vote", uniqueConstraints = {@UniqueConstraint(
        columnNames = {"vote_date", "user_id"}, name = "vote_unique_date_user_id_idx")})
public class Vote extends AbstractBaseEntity {

    @Column(name = "vote_date", nullable = false)
    @NotNull
    private LocalDate voteDate;

    @Column(name = "user_id", nullable = false, unique = true)
    private Integer userId;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;

    public Vote() {

    }
    public Vote(Integer id, LocalDate voteDate, Integer userId, Restaurant restaurant) {
        super(id);
        this.voteDate = voteDate;
        this.userId = userId;
        this.restaurant = restaurant;
    }
    public Vote(Vote v) {
        this(v.id(), v.voteDate, v.userId, v.restaurant);
    }

    public LocalDate getVoteDate() {
        return voteDate;
    }

    public void setVoteDate(LocalDate voteDate) {
        this.voteDate = voteDate;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }
}
