/*
 * ActivityGroupResponse.java
 *
 * Version 1.0.0
 *
 * This class contains response mapping for to-do application
 *
 * Created by Muhammad Isro Prihandoyo
 */

package activity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class ActivityGroupResponse extends StatusResponse implements Serializable {

    private static final long serialVersionUID = -5708989144475961535L;
    private Object data;
}
