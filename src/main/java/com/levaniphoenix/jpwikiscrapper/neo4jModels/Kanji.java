package com.levaniphoenix.jpwikiscrapper.neo4jModels;

import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.List;

@Node
public class Kanji {

    //private Integer id;

    @Id
    private Character kanji;


    private int occurrence;

    @Relationship(type = "Contains", direction = Relationship.Direction.INCOMING)
    private List<Word> inWords;


    public Kanji(){}

    public Kanji(char ch){
        kanji = ch;
    }

    public Kanji(char ch, int occurrence){
        kanji = ch;
        this.occurrence = occurrence;
    }

    public Kanji(Integer id, Character kanji, int occurrence) {
        //this.id = id;
        this.kanji = kanji;
        this.occurrence = occurrence;
    }

    public Kanji (com.levaniphoenix.jpwikiscrapper.models.Kanji kanji){
        //this.id = kanji.getId();
        this.occurrence = kanji.getOccurrence();
        this.kanji = kanji.getKanji();
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

//    public Integer getId() {
//        return id;
//    }
//
//    public void setId(Integer id) {
//        this.id = id;
//    }

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

    public List<Word> getInWords() {
        return inWords;
    }

    public void setInWords(List<Word> inWords) {
        this.inWords = inWords;
    }
}
