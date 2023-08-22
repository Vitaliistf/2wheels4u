package org.vitaliistf.twowheels4u.models;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "motorcycles")
public class Motorcycle {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Basic
    @Column(name = "model")
    private String model;

    @Basic
    @Column(name = "manufacturer")
    private String manufacturer;

    @Basic
    @Column(name = "inventory")
    private Integer inventory;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "type")
    private Type type;

    @Basic
    @Column(name = "fee")
    private BigDecimal fee;

    @Basic
    @Column(name = "is_deleted")
    private boolean deleted;

    public enum Type {
        ENDURO,
        STREET,
        CRUISER,
        SPORT
    }
}
