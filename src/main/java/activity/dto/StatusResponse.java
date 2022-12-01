/*
 * StatusResponse.java
 *
 * Version 1.0.0
 *
 * This class contains mapping for status and message needed by the API
 * It is a superclass that's inherited by ActivityGroupResponse.java
 *
 * Created by Muhammad Isro Prihandoyo
 */

package activity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class StatusResponse implements Serializable {

    private static final long serialVersionUID = -5484212222598254215L;
    private String status;
    private String message;
}
