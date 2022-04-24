package ru.restvoting.to;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.beans.ConstructorProperties;
import java.time.LocalDate;

@Value
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class VoteTo extends BaseTo {

    @NotNull
    LocalDate voteDate;

    @NotBlank
    @Size(min = 2, max = 50)
    String restaurantName;

    @ConstructorProperties({"id", "voteDate", "restaurantName"})
    public VoteTo(Integer id, LocalDate voteDate, String restaurantName) {
        super(id);
        this.voteDate = voteDate;
        this.restaurantName = restaurantName;
    }
}
