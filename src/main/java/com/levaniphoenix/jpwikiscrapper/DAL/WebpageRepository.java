package com.levaniphoenix.jpwikiscrapper.DAL;

import com.levaniphoenix.jpwikiscrapper.models.Webpage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WebpageRepository extends JpaRepository<Webpage,Integer> {
}
