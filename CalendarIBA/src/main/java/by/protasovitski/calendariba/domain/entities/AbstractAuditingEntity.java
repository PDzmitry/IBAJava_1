package by.protasovitski.calendariba.domain.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(value = {"created_date", "last_modified_date","created_by","last_modified_by"},
        allowGetters = true)
@Getter
@Setter
public class AbstractAuditingEntity implements Serializable {

    @CreatedDate
    @Column(name = "created_date", nullable = false, updatable = false)
    private Instant createdDate;

    @CreatedBy
    @Basic(optional = false)
    @Column(name = "created_by", nullable = false, length = 50, updatable = false)
    @JsonIgnore
    private String createdBy;

    @LastModifiedDate
    @Basic(optional = false)
    @Column(name = "last_modified_date",nullable = false)
    private Instant lastModifiedDate;

    @LastModifiedBy
    @Basic(optional = false)
    @Column(name = "last_modified_by", nullable = false,length = 50)
    @JsonIgnore
    private String lastModifiedBy;
}
