package ru.restvoting.to;

import ru.restvoting.model.AbstractNamedEntity;
import ru.restvoting.model.Dish;
import ru.restvoting.model.Menu;
import ru.restvoting.model.Vote;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

public class RestaurantTo {
    private final Integer Id;
    private final String name;
    private final List<Dish> dishList;
    private final List<Menu> menuList;
    private final List<Vote> voteList;
    private final Integer votesAmount;

    public RestaurantTo(Integer id, String name, List<Dish> dishList, List<Menu> menuList, List<Vote> voteList, Integer votesAmount) {
        Id = id;
        this.name = name;
        this.dishList = dishList;
        this.menuList = menuList;
        this.voteList = voteList;
        this.votesAmount = votesAmount;
    }

    public Integer getId() {
        return Id;
    }

    public String getName() {
        return name;
    }

    public List<Dish> getDishList() {
        return dishList;
    }

    public List<Menu> getMenuList() {
        return menuList;
    }

    public List<Vote> getVoteList() {
        return voteList;
    }

    public Integer getVotesAmount() {
        return votesAmount;
    }

    @Override
    public String toString() {
        return "RestaurantTo{" +
                "Id=" + Id +
                ", name=" + name +
                ", dishList=" + dishList +
                ", menuList=" + menuList +
                ", voteList=" + voteList +
                ", votesAmount=" + votesAmount +
                '}';
    }
}
