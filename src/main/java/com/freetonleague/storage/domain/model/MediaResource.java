package com.freetonleague.storage.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.freetonleague.storage.domain.enums.ResourceFileType;
import com.freetonleague.storage.domain.enums.ResourcePrivacyType;
import com.freetonleague.storage.domain.enums.ResourceStatusType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import com.freetonleague.storage.util.StringUtil;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.apache.commons.lang3.StringUtils.isBlank;

@EqualsAndHashCode
@ToString
@Getter
@Setter
@Entity
@Table(name = "media_resources")
@SequenceGenerator(name = "media_resources_id_seq", sequenceName = "media_resources_id_seq", allocationSize = 1)
public class MediaResource implements Serializable {

    private static final long serialVersionUID = -4729003329213350620L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "media_resources_id_seq")
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    private ResourceFileType resourceType;

    /** Hash unique identifier of resource */
    @Column(name = "hash_key", nullable = false, updatable = false)
    private String hashKey;

    @Column(name = "status", nullable = false, updatable = false)
    @Enumerated(EnumType.STRING)
    private ResourceStatusType status;

    @Column(name = "privacy_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private ResourcePrivacyType privacyType;

    /** Owner reference (League-id for user, CoreId for team)
     * Used only for files that can be accessed by one owner.
     */
    @Column(name = "privacy_owner_guid", nullable = false)
    private UUID privacyOwnerGUID;

    @Column(name = "created_by", nullable = false, updatable = false)
    private UUID creatorGUID;

    @CreationTimestamp
    @JsonIgnore
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @JsonIgnore
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        if(isBlank(hashKey)){
            hashKey = StringUtil.generateRandomHash();
        }
    }
}
