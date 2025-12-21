package br.edu.ufape.gobarber.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_product")
    private Integer id;

    @Column(name = "name_product")
    private String name;

    @Column(name = "brand_product")
    private String brand;

    @Column(name = "description")
    private String description;

    @Column(name = "price_product")
    private double price;

    @Column(name = "size")
    private String size;

    @JsonIgnore
    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
    private Set<ProductStock> productStocks;

    public Product(Integer id, String name, String brand, String description, double price, String size) {
        this.id = id;
        this.name = name;
        this.brand = brand;
        this.description = description;
        this.price = price;
        this.size = size;
    }

}