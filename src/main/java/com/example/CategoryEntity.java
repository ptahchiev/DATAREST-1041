package com.example;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OrderColumn;
import java.util.Set;

/**
 * @author Petar Tahchiev
 * @since 1.5
 */
@Entity
public class CategoryEntity {

    public static final String NAME = "category";

    @Id
    private Long id;

    @OrderColumn(name = ProductEntity.NAME + "_order")
    @ManyToMany(fetch = FetchType.LAZY, targetEntity = ProductEntity.class)
    @JoinTable(name = CategoryEntity.NAME + "_" + ProductEntity.NAME, joinColumns = @JoinColumn(name = CategoryEntity.NAME
                    + "_id"), inverseJoinColumns = @JoinColumn(name = ProductEntity.NAME + "_id"), indexes = {
                    @Index(columnList = CategoryEntity.NAME + "_id") })
    private Set<ProductEntity> products;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<ProductEntity> getProducts() {
        return products;
    }

    public void setProducts(Set<ProductEntity> products) {
        this.products = products;
    }
}
