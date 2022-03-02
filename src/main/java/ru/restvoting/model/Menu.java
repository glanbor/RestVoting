package ru.restvoting.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "menu", uniqueConstraints = {@UniqueConstraint(
        columnNames = {"for_date", "restaurant_id"}, name = "menu_unique_date_restaurant_id_idx")})
public class Menu extends AbstractBaseEntity{

    @Column(name = "menu_date", nullable = false)
    @NotNull
    private LocalDate menuDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    @NotNull
    private Restaurant restaurant;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "menu")
    private List<Dish> dishList;

    public Menu(LocalDate menuDate, Restaurant restaurant, List<Dish> dishList) {
        this.menuDate = menuDate;
        this.restaurant = restaurant;
        this.dishList = dishList;
    }
    public Menu() {
    }

    public Menu(Integer id, LocalDate menuDate, Restaurant restaurant, List<Dish> dishList) {
        super(id);
        this.menuDate = menuDate;
        this.restaurant = restaurant;
        this.dishList = dishList;
    }

    public LocalDate getMenuDate() {
        return menuDate;
    }

    public void setMenuDate(LocalDate menuDate) {
        this.menuDate = menuDate;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public List<Dish> getDishList() {
        return dishList;
    }

    public void setDishList(List<Dish> dishList) {
        this.dishList = dishList;
    }

}
