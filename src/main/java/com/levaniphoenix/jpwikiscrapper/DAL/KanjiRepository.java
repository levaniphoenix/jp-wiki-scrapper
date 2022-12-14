package com.levaniphoenix.jpwikiscrapper.DAL;

import com.levaniphoenix.jpwikiscrapper.models.Kanji;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface KanjiRepository extends JpaRepository<Kanji, Integer> {

    Optional<Kanji> findByKanji(Character ch);
}