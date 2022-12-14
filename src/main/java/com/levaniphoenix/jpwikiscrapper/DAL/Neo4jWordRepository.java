package com.levaniphoenix.jpwikiscrapper.DAL;

import com.levaniphoenix.jpwikiscrapper.neo4jModels.Word;
import org.springframework.data.repository.CrudRepository;

public interface Neo4jWordRepository extends CrudRepository<Word, String>{
}
