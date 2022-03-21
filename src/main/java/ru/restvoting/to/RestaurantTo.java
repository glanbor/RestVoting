package ru.restvoting.to;

import lombok.*;
import org.hibernate.validator.constraints.Range;
import ru.restvoting.model.Vote;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Value
public class RestaurantTo {
    Integer Id;

    @NotBlank
    @Size(min = 2, max = 50)
    String name;

    List<Vote> voteList;

    @NotNull
    @Range(min = 0)
    int votesAmount;

}
