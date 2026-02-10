package com.involvest.service;

import com.involvest.exception.NotFoundException;
import com.involvest.model.Family;
import com.involvest.repository.FamilyRepository;
import org.springframework.stereotype.Service;

@Service
public class FamilyService {
  private final FamilyRepository familyRepository;

  public FamilyService(FamilyRepository familyRepository) {
    this.familyRepository = familyRepository;
  }

  public Family create(String name) {
    return familyRepository.save(name);
  }

  public Family getById(long id) {
    return familyRepository.findById(id)
        .orElseThrow(() -> new NotFoundException("Family not found"));
  }
}
