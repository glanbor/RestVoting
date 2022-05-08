package ru.restvoting.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@ToString(callSuper = true, exclude = {"dishList", "menuList", "voteList"})
@Entity
@Table(name = "restaurant", uniqueConstraints =
        {@UniqueConstraint(columnNames = "name", name = "unique_name_for_restaurant_idx")})
public class Restaurant extends NamedEntity {

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "restaurant")
    @JsonManagedReference
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<Dish> dishList;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "restaurant")
    @JsonManagedReference
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<Menu> menuList;

    @JsonManagedReference
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "restaurant")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @OrderBy("voteDate DESC")
    private List<Vote> voteList;

    public Restaurant(Integer id, String name) {
        super(id, name);
    }

    public Restaurant(Restaurant r) {
        this(r.id, r.name, r.dishList, r.menuList, r.voteList);
    }

    public Restaurant(Integer id, String name, List<Dish> dishList, List<Menu> menuList, List<Vote> voteList) {
        super(id, name);
        this.dishList = dishList;
        this.menuList = menuList;
        this.voteList = voteList;
    }

    public Restaurant(Integer id, String name, List<Dish> dishList, List<Menu> menuList) {
        super(id, name);
        this.dishList = dishList;
        this.menuList = menuList;
    }
}
