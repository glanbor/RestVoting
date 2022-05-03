package ru.restvoting.to;

import lombok.*;
import org.hibernate.validator.constraints.Range;
import ru.restvoting.HasId;
import ru.restvoting.model.Vote;

import javax.validation.constraints.NotNull;
import java.beans.ConstructorProperties;
import java.util.List;

@Value
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class RestaurantTo extends NamedTo implements HasId {

    List<Vote> voteList;

    @NotNull
    @Range(min = 0)
    int votesAmount;

    @ConstructorProperties({"id", "name", "voteList", "votesAmount"})
    public RestaurantTo(Integer id, String name, List<Vote> voteList, int votesAmount) {
        super(id, name);
        this.voteList = voteList;
        this.votesAmount = votesAmount;
    }
}
