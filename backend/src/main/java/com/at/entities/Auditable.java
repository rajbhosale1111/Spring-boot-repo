package com.at.entities;

import static jakarta.persistence.TemporalType.TIMESTAMP;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Temporal;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@Setter
@ToString
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class Auditable<U> {

  @CreatedBy
  @Column(updatable = false, name = "created_by")
  protected U createdBy;

  @CreatedDate
  @Temporal(TIMESTAMP)
  @Column(updatable = false, name = "created_at")
  protected Date createdAt;

  @Column(name = "updated_by")
  @LastModifiedBy
  protected U updatedBy;

  @LastModifiedDate
  @Temporal(TIMESTAMP)
  @Column(name = "updated_at")
  protected Date updatedAt;

  @Temporal(TIMESTAMP)
  @Column(name = "deleted_at")
  protected Date deletedAt;
}
