/*
 * Activity.java
 *
 * Version 1.0.0
 *
 * This class is the entity mapping for todos table
 *
 * Created by Muhammad Isro Prihandoyo
 */

package activity.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "todos")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Activity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", length = 3, nullable = false)
    private Long id;

    @Column(name = "ACTIVITY_GROUP_ID", length = 3, nullable = false)
    @JsonProperty("activity_group_id")
    private Long activityGroupId;

    @Column(name = "TITLE", length = 200, nullable = false)
    private String title;

    @Column(name = "IS_ACTIVE", length = 1)
    @JsonProperty("is_active")
    private Boolean isActive;

    @Column(name = "PRIORITY", length = 20)
    private String priority;

    @Column(name = "CREATED_DATE", nullable = false)
    @JsonProperty("created_at")
    private LocalDateTime createdDate;

    @Column(name = "UPDATED_DATE")
    @JsonProperty("updated_at")
    private LocalDateTime updatedDate;
}
