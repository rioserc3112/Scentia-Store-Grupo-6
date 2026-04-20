package com.store.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "ruta")
public class Ruta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String patron;

    @Column(length = 50)
    private String rolName;

    public Ruta() {
    }

    public Ruta(String patron, String rolName) {
        this.patron = patron;
        this.rolName = rolName;
    }

    public Long getId() {
        return id;
    }

    public String getPatron() {
        return patron;
    }

    public void setPatron(String patron) {
        this.patron = patron;
    }

    public String getRolName() {
        return rolName;
    }

    public void setRolName(String rolName) {
        this.rolName = rolName;
    }
}
