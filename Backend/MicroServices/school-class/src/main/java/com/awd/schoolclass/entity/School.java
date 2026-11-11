package com.awd.schoolclass.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "schools")
public class School {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String city;
    private String postalCode;
    private String country;
    private String principalEmail;
    private LocalDate registeredAt = LocalDate.now();

    @OneToMany(mappedBy = "school", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SchoolClass> classes = new ArrayList<>();

    public School() {}
    public School(String name, String city, String postalCode, String principalEmail) {
        this.name = name;
        this.city = city;
        this.postalCode = postalCode;
        this.principalEmail = principalEmail;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }
    public String getPostalCode() { return postalCode; }
    public void setPostalCode(String postalCode) { this.postalCode = postalCode; }
    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }
    public String getPrincipalEmail() { return principalEmail; }
    public void setPrincipalEmail(String principalEmail) { this.principalEmail = principalEmail; }
    public LocalDate getRegisteredAt() { return registeredAt; }
    public List<SchoolClass> getClasses() { return classes; }
}
