package ru.restvoting.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;


@Entity
@Table(name = "menu", uniqueConstraints = {@UniqueConstraint(
        columnNames = {"restaurant_id", "menu_date"}, name = "unique_restaurant_for_date_idx")})
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor()
@ToString(callSuper = true, exclude = {"dishList", "restaurant"})
public class Menu extends AbstractBaseEntity{

    @Column(name = "menu_date", nullable = false)
    @NotNull
    private LocalDate menuDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    @NotNull
    @JsonBackReference
    private Restaurant restaurant;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "menu_with_dishes",
            joinColumns = {@JoinColumn(name ="menu_id", referencedColumnName ="id")},
            inverseJoinColumns={@JoinColumn(name ="dish_id", referencedColumnName ="id")})
    private List<Dish> dishList;

    public Menu(Integer id, LocalDate menuDate, Restaurant restaurant, List<Dish> dishList) {
        super(id);
        this.menuDate = menuDate;
        this.restaurant = restaurant;
        this.dishList = dishList;
    }

}
