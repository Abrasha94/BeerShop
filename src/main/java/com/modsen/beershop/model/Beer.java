package com.modsen.beershop.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "beers")
public class Beer {

    @Id
    @GeneratedValue
    private Integer id;
    private String name;
    private String container;
    private String type;
    private Double abv;
    private Integer ibu;
    @Column(name = "beer_description")
    private String beerDescription;
    private Timestamp created;
    private Timestamp update;
    private Integer quantity;
    @Column(name = "container_volume")
    private Double containerVolume;

    @OneToMany(mappedBy = "beer", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<UserTransaction> userTransactions;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Beer beer = (Beer) o;
        return id != null && Objects.equals(id, beer.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
