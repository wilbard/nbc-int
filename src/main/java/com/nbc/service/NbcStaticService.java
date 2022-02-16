package com.nbc.service;

import com.nbc.model.NbcStatic;
import com.nbc.repository.NbcStaticRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class NbcStaticService {

    @Autowired
    private NbcStaticRepository nbcStaticRepository;

    @Transactional
    public void update(NbcStatic nbcStatic) {
        this.nbcStaticRepository.save(nbcStatic);
    }
    @Transactional
    public void remove(NbcStatic nbcStatic) {
        this.nbcStaticRepository.delete(nbcStatic);
    }

    public NbcStatic findById(long id) {
        return this.nbcStaticRepository.findById(id);
    }

    public NbcStatic findByConfigName(String configName) {
        return this.nbcStaticRepository.findByConfigName(configName);
    }
}
