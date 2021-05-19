package com.freetonleague.storage.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.freetonleague.storage.domain.dto.MediaResourceMetaDataDto;
import com.freetonleague.storage.domain.enums.ResourceFileExtensionType;
import com.freetonleague.storage.domain.enums.ResourcePrivacyType;
import com.freetonleague.storage.domain.enums.ResourceStatusType;
import com.freetonleague.storage.util.StringUtil;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.InputStream;
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
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
@SequenceGenerator(name = "media_resources_id_seq", sequenceName = "media_resources_id_seq", allocationSize = 1)
public class MediaResource implements Serializable {

    private static final long serialVersionUID = -4729003329213350620L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "media_resources_id_seq")
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @Size(max = 200)
    @Column(name = "name")
    private String name;

    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    private ResourceFileExtensionType resourceType;

    @Transient
    private InputStream rawResourceData;

    @Column(name = "size", nullable = false)
    private Integer sizeInBytes;

    @Type(type = "jsonb")
    @Column(name = "meta_data", columnDefinition = "jsonb")
    private MediaResourceMetaDataDto resourceMetaData;

    /**
     * Hash unique identifier of resource
     */
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
        if (isBlank(hashKey)) {
            this.generateHash();
        }
    }

    public void generateHash() {
        hashKey = StringUtil.generateRandomHash();
    }
}
