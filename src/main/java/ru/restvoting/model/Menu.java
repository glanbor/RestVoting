package ru.restvoting.model;

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
    private Restaurant restaurant;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "menu")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<Dish> dishList;

    public Menu(Integer id, LocalDate menuDate, Restaurant restaurant, List<Dish> dishList) {
        super(id);
        this.menuDate = menuDate;
        this.restaurant = restaurant;
        this.dishList = dishList;
    }

}
