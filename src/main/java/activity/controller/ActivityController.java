/*
 * ActivityController.java
 *
 * Version 1.0.0
 *
 * This class is the controller (API) for to-do list application
 * This controller includes activoty controller and To-Do controller
 *
 * Created by Muhammad Isro Prihandoyo
 */

package activity.controller;

import activity.dto.ActivityGroupResponse;
import activity.dto.GeneralRequest;
import activity.services.ActivityGroupService;
import activity.services.ActivityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class ActivityController {

    @Autowired
    private ActivityGroupService activityGroupService;

    @Autowired
    private ActivityService activityService;

    public static final String ACTIVITY_GROUP_URL = "/activity-groups";
    public static final String ACTIVITY_GROUP_PARAM_URL = ACTIVITY_GROUP_URL + ("/{id}");
    public static final String TODO_ITEMS_URL = "/todo-items";
    public static final String TODO_ITEMS_PARAM_URL = TODO_ITEMS_URL + ("/{id}");


    /**
     *
     * API for Activity CRUD
     *
     */
    @GetMapping(ACTIVITY_GROUP_URL)
    public ResponseEntity<ActivityGroupResponse> getAllActivityByEmail(@RequestParam(required = false) String email) {
        return activityGroupService.getAllActivity(email);
    }

    @GetMapping(ACTIVITY_GROUP_PARAM_URL)
    public ResponseEntity<ActivityGroupResponse> getActivityById(@PathVariable Long id) {
        return activityGroupService.getActivity(id);
    }

    @PostMapping(ACTIVITY_GROUP_URL)
    public ResponseEntity<ActivityGroupResponse> createActivity(@RequestBody GeneralRequest request) {
        return activityGroupService.createActivity(request);
    }

    @PatchMapping(ACTIVITY_GROUP_PARAM_URL)
    public ResponseEntity<ActivityGroupResponse> updateActivity(@PathVariable Long id, @RequestBody(required = false) GeneralRequest request) {
        return activityGroupService.updateActivity(request, id);
    }

    @DeleteMapping(ACTIVITY_GROUP_PARAM_URL)
    public ResponseEntity<ActivityGroupResponse> deleteActivity(@PathVariable Long id) {
        return activityGroupService.deleteActivity(id);
    }

    /**
     *
     * API for TO-DO CRUD
     *
     */
    @GetMapping(TODO_ITEMS_URL)
    public ResponseEntity<ActivityGroupResponse> getAllTodoItems(@RequestParam(required = false) Long activity_group_id) {
        return activityService.getAllTodoItems(activity_group_id);
    }

    @GetMapping(TODO_ITEMS_PARAM_URL)
    public ResponseEntity<ActivityGroupResponse> getTodoItemsById(@PathVariable Long id) {
        return activityService.getTodoItems(id);
    }

    @PostMapping(TODO_ITEMS_URL)
    public ResponseEntity<ActivityGroupResponse> createTodoItems(@RequestBody GeneralRequest request) {
        return activityService.createTodoItems(request);
    }

    @DeleteMapping(TODO_ITEMS_PARAM_URL)
    public ResponseEntity<ActivityGroupResponse> deleteTodoItems(@PathVariable Long id) {
        return activityService.deleteTodoItems(id);
    }

    @PatchMapping(TODO_ITEMS_PARAM_URL)
    public ResponseEntity<ActivityGroupResponse> updateTodoItem(@PathVariable Long id, @RequestBody GeneralRequest request) {
        return activityService.updateTodoItems(request, id);
    }
}
