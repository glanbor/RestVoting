package ru.restvoting.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Setter
@Getter
@NoArgsConstructor
@ToString(callSuper = true)
@Entity
@Table(name="vote", uniqueConstraints = {@UniqueConstraint(
        columnNames = {"vote_date", "user_id"}, name = "unique_vote_date_for_user_idx")})
public class Vote extends BaseEntity {

    @Column(name = "vote_date", nullable = false)
    @NotNull
    private LocalDate voteDate;

    @Column(name = "user_id", nullable = false)
    private int userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonBackReference
    private Restaurant restaurant;

    public Vote(Integer id, LocalDate voteDate, int userId, Restaurant restaurant) {
        super(id);
        this.voteDate = voteDate;
        this.userId = userId;
        this.restaurant=restaurant;
    }
    public Vote(Vote v) {
        this(v.id(), v.voteDate, v.userId, v.restaurant);
    }

}
