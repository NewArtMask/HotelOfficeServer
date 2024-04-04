package com.example.demohotelofficeproject.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "hotels")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@EqualsAndHashCode
public class Hotel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String hotelName;

    @Column(nullable = false)
    private Integer roomCount;

    @Column(nullable = false)
    private Integer bedCount;

    @Column(nullable = false)
    private Integer occupiedRoomCount;

    @Column(nullable = false)
    private String street;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private Long zipCode;

    @Column
    private String description;

    @ManyToOne
    @JoinColumn(name = "user_uuid", referencedColumnName = "uuid")
    private User user;
}
