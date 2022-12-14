package com.levaniphoenix.jpwikiscrapper.models;

import jakarta.persistence.*;

@Entity
@Table(name = "kanjis")
public class Kanji {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Character kanji;

    private int occurrence;

    public Kanji(){}

    public Kanji(char ch){
        kanji = ch;
    }

    public Kanji(char ch, int occurrence){
        kanji = ch;
        this.occurrence = occurrence;
    }

    public Kanji(Integer id, Character kanji, int occurrence) {
        this.id = id;
        this.kanji = kanji;
        this.occurrence = occurrence;
    }
    public Kanji getThis(){
        return this;
    }

    @Override
    public int hashCode() {
        return kanji.hashCode();
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

    public Character getKanji() {
        return kanji;
    }

    public void setKanji(Character kanji) {
        this.kanji = kanji;
    }

    public int getOccurrence() {
        return occurrence;
    }

    public void setOccurrence(int occurrence) {
        this.occurrence = occurrence;
    }
}
