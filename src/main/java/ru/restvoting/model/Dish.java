package ru.restvoting.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.validator.constraints.Range;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "dish", uniqueConstraints = {@UniqueConstraint(
        columnNames = {"name", "restaurant_id"}, name = "unique_restaurant_for_dish_idx")})
@Setter
@Getter
@NoArgsConstructor
@ToString(callSuper = true, exclude = "restaurant")
public class Dish extends AbstractNamedEntity {
    
    @Column(name = "price", nullable = false)
    @NotNull
    @Range(min = 0, max = 10000)
    private double price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    @NotNull
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Restaurant restaurant;

    public Dish(Dish d) {
        this(d.id, d.name, d.price, d.restaurant);
    }

    public Dish(Integer id, String name, double price, Restaurant restaurant) {
        super(id, name);
        this.price = price;
        this.restaurant = restaurant;
    }

}
