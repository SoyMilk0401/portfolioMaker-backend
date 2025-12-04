package com.portfolioMaker.backend.repository;

import com.portfolioMaker.backend.entity.RelatedLink;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RelatedLinkRepository extends JpaRepository<RelatedLink, Long> {
}