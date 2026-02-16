package br.edu.ufape.gobarber.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalTime;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "service")
public class Services {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_service")
    private Integer id;

    @Column(name = "name_service")
    private String name;

    @Column(name = "description_service")
    private String description;

    @Column(name = "price_service")
    private double value;

    @Column(name = "time_service")
    private LocalTime time;

    @ManyToMany(mappedBy = "services")
    @JsonIgnore
    private Set<Barber> barbers;

}
