package com.involvest.service;

import com.involvest.exception.BadRequestException;
import com.involvest.model.MovementKind;
import com.involvest.model.MovementType;
import com.involvest.repository.MovementTypeRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class MovementTypeService {
  private final MovementTypeRepository movementTypeRepository;

  public MovementTypeService(MovementTypeRepository movementTypeRepository) {
    this.movementTypeRepository = movementTypeRepository;
  }

  public MovementType create(long familyId, String name, MovementKind kind) {
    if (name == null || name.isBlank()) {
      throw new BadRequestException("Name is required");
    }
    return movementTypeRepository.save(familyId, name.trim(), kind);
  }

  public List<MovementType> list(long familyId) {
    return movementTypeRepository.findAllByFamily(familyId);
  }

  public MovementType getById(long familyId, long typeId) {
    return movementTypeRepository.findByIdAndFamily(typeId, familyId)
        .orElseThrow(() -> new com.involvest.exception.NotFoundException("Type not found"));
  }
}
