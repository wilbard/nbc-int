package com.nbc.repository;

import com.nbc.model.NbcStatic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NbcStaticRepository extends JpaRepository<NbcStatic, Long> {

    NbcStatic findById(long id);
    NbcStatic findByConfigName(String configName);
}
