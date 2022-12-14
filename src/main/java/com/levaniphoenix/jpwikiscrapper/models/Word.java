package com.levaniphoenix.jpwikiscrapper.models;

import jakarta.persistence.*;

@Entity
@Table(name = "words")
public class Word {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String word;

    private int occurrence;

    private String partOfSpeechLevel1;

    private String partOfSpeechLevel2;

    public Word(){}

    public Word(String word,String partOfSpeechLevel1,String partOfSpeechLevel2 ){
        this.word = word;
        this.partOfSpeechLevel1 = partOfSpeechLevel1;
        this.partOfSpeechLevel2 = partOfSpeechLevel2;
    }

    public Word getThis(){
        return this;
    }

    @Override
    public int hashCode() {
        return word.hashCode();
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

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public int getOccurrence() {
        return occurrence;
    }

    public void setOccurrence(int occurrence) {
        this.occurrence = occurrence;
    }

    public String getPartOfSpeechLevel1() {
        return partOfSpeechLevel1;
    }

    public void setPartOfSpeechLevel1(String partOfSpeechLevel1) {
        this.partOfSpeechLevel1 = partOfSpeechLevel1;
    }

    public String getPartOfSpeechLevel2() {
        return partOfSpeechLevel2;
    }

    public void setPartOfSpeechLevel2(String partOfSpeechLevel2) {
        this.partOfSpeechLevel2 = partOfSpeechLevel2;
    }
}
