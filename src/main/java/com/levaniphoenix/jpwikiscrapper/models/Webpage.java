package com.levaniphoenix.jpwikiscrapper.models;


import jakarta.persistence.*;

@Entity
@Table(name = "webpages")
public class Webpage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "url", length = 1024)
    private String URL;

    public Webpage(){}

    public Webpage(String url){
        URL = url;
    }

    @Override
    public int hashCode() {
        return URL.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        return o.hashCode() == this.hashCode();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }
}
