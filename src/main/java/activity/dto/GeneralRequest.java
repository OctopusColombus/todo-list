/*
 * GeneralRequest.java
 *
 * Version 1.0.0
 *
 * This class contains mapping for all request needed by the API
 *
 * Created by Muhammad Isro Prihandoyo
 */

package activity.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GeneralRequest implements Serializable {

    private static final long serialVersionUID = -771869993480431594L;

    @JsonProperty("activity_group_id")
    private Long activityGroupId;

    @JsonProperty("is_active")
    private Boolean isActive;

    private String priority;

    private String email;

    private String title;
}
