package ru.restvoting.to;

import lombok.*;
import org.hibernate.validator.constraints.Range;
import ru.restvoting.model.Vote;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.beans.ConstructorProperties;
import java.util.List;

@Value
@EqualsAndHashCode
@ToString
public class RestaurantTo {

    Integer Id;

    @NotBlank
    @Size(min = 2, max = 50)
    String name;

    List<Vote> voteList;

    @NotNull
    @Range(min = 0)
    int votesAmount;

    @ConstructorProperties({"id", "name", "voteList", "votesAmount"})
    public RestaurantTo(Integer id, String name, List<Vote> voteList, int votesAmount) {
        Id = id;
        this.name = name;
        this.voteList = voteList;
        this.votesAmount = votesAmount;
    }
}
