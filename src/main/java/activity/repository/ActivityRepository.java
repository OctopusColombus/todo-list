/*
 * ActivityRepository.java
 *
 * Version 1.0.0
 *
 * Created by Muhammad Isro Prihandoyo
 */

package activity.repository;

import activity.entity.Activity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ActivityRepository extends JpaRepository<Activity, Long> {
    //Query to get To-Do Item by activity Group id
    List<Activity> findTodoItemsByActivityGroupId(Long id);

    //Query to get only one To-Do Item by ID
    Activity findItemById(Long id);
}
