/*
 * ActivityGroup.java
 *
 * Version 1.0.0
 *
 * This class is the entity mapping for activity_group table
 *
 * Created by Muhammad Isro Prihandoyo
 */

package activity.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.internal.bytebuddy.implementation.bind.annotation.IgnoreForBinding;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "activities")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ActivityGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", length = 3, nullable = false)
    private Long id;

    @Column(name = "TITLE", length = 200, nullable = false)
    private String title;

    @Column(name = "CREATED_DATE", nullable = false)
    @JsonProperty("created_at")
    private LocalDateTime createdDate;

    @Column(name = "UPDATED_DATE")
    @JsonProperty("updated_at")
    private LocalDateTime updatedDate;

    @Column(name = "EMAIL", length = 200)
    private String email;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "ACTIVITY_GROUP_ID")
    @JsonProperty("todo_items")
    private List<Activity> todoItems;
}
