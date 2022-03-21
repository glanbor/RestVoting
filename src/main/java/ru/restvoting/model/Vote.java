package ru.restvoting.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Setter
@Getter
@NoArgsConstructor
@ToString(callSuper = true)
@Entity
@Table(name="vote", uniqueConstraints = {@UniqueConstraint(
        columnNames = {"vote_date", "user_id"}, name = "unique_date_for_user_idx")})
public class Vote extends AbstractBaseEntity {

    @Column(name = "vote_date", nullable = false)
    @NotNull
    private LocalDate voteDate;

    @Column(name = "user_id", nullable = false, unique = true)
    private int userId;

    @Column(name = "restaurant_id", nullable = false)
    private int restId;

    public Vote(Integer id, LocalDate voteDate, int userId, int restId) {
        super(id);
        this.voteDate = voteDate;
        this.userId = userId;
        this.restId=restId;
    }
    public Vote(Vote v) {
        this(v.id(), v.voteDate, v.userId, v.restId);
    }

}
