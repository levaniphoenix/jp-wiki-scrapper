package com.levaniphoenix.jpwikiscrapper.DAL;


import com.levaniphoenix.jpwikiscrapper.models.Word;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WordRepository extends JpaRepository<Word, Integer> {

    Optional<Word> findByWord(String word);

}
