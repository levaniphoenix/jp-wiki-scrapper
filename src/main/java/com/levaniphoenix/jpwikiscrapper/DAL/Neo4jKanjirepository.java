package com.levaniphoenix.jpwikiscrapper.DAL;

import com.levaniphoenix.jpwikiscrapper.neo4jModels.Kanji;
import org.springframework.data.repository.CrudRepository;

public interface Neo4jKanjirepository extends CrudRepository<Kanji, Character> {
}
