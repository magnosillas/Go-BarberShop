package br.edu.ufape.gobarber.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "barbershop")
public class Barbershop {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_barbershop")
    private Integer idBarbershop;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "slug", nullable = false, unique = true)
    private String slug;

    @Column(name = "description")
    private String description;

    @Column(name = "cnpj", unique = true)
    private String cnpj;

    @Column(name = "phone")
    private String phone;

    @Column(name = "email")
    private String email;

    @Column(name = "logo_url")
    private String logoUrl;

    @Column(name = "banner_url")
    private String bannerUrl;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_address", referencedColumnName = "id_adress")
    private Address address;

    @Column(name = "opening_hours")
    private String openingHours;

    @Column(name = "active", nullable = false)
    private Boolean active;

    @ManyToMany
    @JoinTable(
            name = "client_barbershop",
            joinColumns = @JoinColumn(name = "id_barbershop"),
            inverseJoinColumns = @JoinColumn(name = "id_client")
    )
    private Set<Client> clients = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "barber_barbershop",
            joinColumns = @JoinColumn(name = "id_barbershop"),
            inverseJoinColumns = @JoinColumn(name = "id_barber")
    )
    private Set<Barber> barbers = new HashSet<>();

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (active == null) active = true;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
