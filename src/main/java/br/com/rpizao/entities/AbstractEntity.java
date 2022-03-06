package br.com.rpizao.entities;

import javax.persistence.MappedSuperclass;
import javax.persistence.PostLoad;
import javax.persistence.PrePersist;

import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;


@MappedSuperclass
public abstract class AbstractEntity<TypeId> implements Persistable<TypeId> {

  @Transient
  private boolean isNew = true; 

  @Override
  public boolean isNew() {
    return isNew; 
  }

  @PrePersist 
  @PostLoad
  void markNotNew() {
    this.isNew = false;
  }

  // More code…
}