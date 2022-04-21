package ru.restvoting.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "menu", uniqueConstraints = {@UniqueConstraint(
        columnNames = {"menu_date", "restaurant_id",}, name = "unique_menu_date_for_restaurant_idx")})
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor()
@ToString(callSuper = true, exclude = {"restaurant"})
public class Menu extends AbstractBaseEntity{

    @Column(name = "menu_date", nullable = false)
    @NotNull
    private LocalDate menuDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    @JsonBackReference
    private Restaurant restaurant;

    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.REFRESH})
    @JoinTable(name = "menu_with_dishes",
            joinColumns = {@JoinColumn(name ="menu_id")},
            inverseJoinColumns={@JoinColumn(name ="dish_id")})
    private List<Dish> dishList;;

    public Menu(Integer id, LocalDate menuDate, Restaurant restaurant, List<Dish> dishList) {
        super(id);
        this.menuDate = menuDate;
        this.restaurant = restaurant;
        this.dishList = dishList;
    }

}
