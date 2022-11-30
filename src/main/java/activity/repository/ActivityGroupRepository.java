/*
 * ActivityGroupRepository.java
 *
 * Version 1.0.0
 *
 * Created by Muhammad Isro Prihandoyo
 */

package activity.repository;

import activity.entity.ActivityGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ActivityGroupRepository extends JpaRepository<ActivityGroup, Long> {
    //Query to get only one activity by ID
    @Query(value = "SELECT * FROM activities WHERE id = :id", nativeQuery = true)
    ActivityGroup findByActivityId(@Param("id") Long id);

    //Query to get activities by Email
    @Query(value = "SELECT * FROM activities WHERE email = :email ORDER BY updated_date LIMIT 1000", nativeQuery = true)
    List<ActivityGroup> findByEmail(@Param("email") String email);

    //Query to get all activities
    @Query(value = "SELECT * FROM activities ORDER BY updated_date LIMIT 1000", nativeQuery = true)
    List<ActivityGroup> findAllActivity();
}
