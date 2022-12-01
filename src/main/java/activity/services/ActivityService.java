/*
 * ActivityService.java
 *
 * Version 1.0.0
 *
 * This class contains all method needed by the API for To-Do Items CRUD
 *
 * Created by Muhammad Isro Prihandoyo
 */

package activity.services;

import activity.dto.ActivityGroupResponse;
import activity.dto.GeneralRequest;
import activity.entity.Activity;
import activity.repository.ActivityRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class ActivityService {

    @Autowired
    private ActivityRepository activityRepository;

    public static final String SUCCESS = "Success";
    public static final String NOT_FOUND_STATUS = "Not Found";
    public static final String NOT_FOUND_MESSAGE = "Todo with ID ";
    public static final String BAD_REQUEST_STATUS = "Bad Request";
    public static final String BAD_REQUEST_TITLE_MESSAGE = "title cannot be null";
    public static final String BAD_REQUEST_MESSAGE = "title and status cannot be null";
    public static final String BAD_REQUEST_ID_MESSAGE = "activity_group_id cannot be null";

    public ResponseEntity<ActivityGroupResponse> getAllTodoItems(Long id) {
        log.info("get All Todo Items with id {}", id);
        List<Activity> activities;

        if (id != null) {
            log.info("Find By Id");
            activities = activityRepository.findTodoItemsByActivityGroupId(id);
        } else {
            log.info("Find All");
            activities = activityRepository.findAll();
        }
        log.info("Activity Response {}", activities);

        ActivityGroupResponse activityGroupResponse = ActivityGroupResponse.builder()
                .status(SUCCESS)
                .message(SUCCESS)
                .data(activities)
                .build();

        return new ResponseEntity<>(activityGroupResponse, HttpStatus.OK);
    }

    public ResponseEntity<ActivityGroupResponse> getTodoItems(Long id) {
        log.info("get Todo Items with id {}", id);
        String status = NOT_FOUND_STATUS;
        String message = StringUtils.join(NOT_FOUND_MESSAGE, id, StringUtils.SPACE, NOT_FOUND_STATUS);
        HttpStatus httpStatus = HttpStatus.NOT_FOUND;

        Activity activity = Activity.builder().build();

        if (activityRepository.existsById(id)) {
            status = SUCCESS;
            message = SUCCESS;
            httpStatus = HttpStatus.OK;
            activity = activityRepository.findItemById(id);
        }

        return new ResponseEntity<>(ActivityGroupResponse.builder()
                .data(activity)
                .status(status)
                .message(message)
                .build(), httpStatus);
    }

    public ResponseEntity<ActivityGroupResponse> createTodoItems(GeneralRequest request) {
        log.info("Create new Todo Item");
        String status = SUCCESS;
        String message = SUCCESS;
        HttpStatus httpStatus = HttpStatus.CREATED;
        LocalDateTime now  = LocalDateTime.now();

        Activity activity = new Activity();

        if (request.getActivityGroupId() == null) {
            status = BAD_REQUEST_STATUS;
            message = BAD_REQUEST_ID_MESSAGE;
            httpStatus = HttpStatus.BAD_REQUEST;

            return new ResponseEntity<>(ActivityGroupResponse.builder()
                    .data(activity)
                    .status(status)
                    .message(message)
                    .build(), httpStatus);
        }

        if (StringUtils.isBlank(request.getTitle())) {
            status = BAD_REQUEST_STATUS;
            message = BAD_REQUEST_TITLE_MESSAGE;
            httpStatus = HttpStatus.BAD_REQUEST;

            return new ResponseEntity<>(ActivityGroupResponse.builder()
                    .data(activity)
                    .status(status)
                    .message(message)
                    .build(), httpStatus);
        }

        activity = Activity.builder()
                .activityGroupId(request.getActivityGroupId())
                .title(request.getTitle())
                .isActive(Boolean.TRUE)
                .priority(request.getPriority() != null ? request.getPriority() : "very-high")
                .createdDate(now)
                .updatedDate(now)
                .build();

        activity = activityRepository.save(activity);

        return new ResponseEntity<>(ActivityGroupResponse.builder()
                .data(activity)
                .status(status)
                .message(message)
                .build(), httpStatus);
    }

    public ResponseEntity<ActivityGroupResponse> updateTodoItems(GeneralRequest request, Long id) {
        log.info("Update Todo Items");
        String status = NOT_FOUND_STATUS;
        String message = StringUtils.join(NOT_FOUND_MESSAGE, id, StringUtils.SPACE, NOT_FOUND_STATUS);
        HttpStatus httpStatus = HttpStatus.NOT_FOUND;
        LocalDateTime now  = LocalDateTime.now();

        Activity activity = Activity.builder().build();

        if (StringUtils.isBlank(request.getTitle()) && request.getIsActive() == null) {
            status = BAD_REQUEST_STATUS;
            message = BAD_REQUEST_MESSAGE;
            httpStatus = HttpStatus.BAD_REQUEST;

            return new ResponseEntity<>(ActivityGroupResponse.builder()
                    .data(activity)
                    .status(status)
                    .message(message)
                    .build(), httpStatus);
        }

        if (activityRepository.existsById(id)) {
            status = SUCCESS;
            message = SUCCESS;
            httpStatus = HttpStatus.OK;

            activity = activityRepository.findItemById(id);
            activity.setId(id);

            if (request.getIsActive() != null) {
                activity.setIsActive(request.getIsActive());
            } else {
                activity.setTitle(request.getTitle());
            }

            activity.setUpdatedDate(now);

            activityRepository.save(activity);
        }

        return new ResponseEntity<>(ActivityGroupResponse.builder()
                .data(activity)
                .status(status)
                .message(message)
                .build(), httpStatus);
    }

    public ResponseEntity<ActivityGroupResponse> deleteTodoItems(Long id) {
        log.info("Delete Todo items");
        String status = NOT_FOUND_STATUS;
        String message = StringUtils.join(NOT_FOUND_MESSAGE, id, StringUtils.SPACE, NOT_FOUND_STATUS);
        HttpStatus httpStatus = HttpStatus.NOT_FOUND;

        if (activityRepository.existsById(id)) {
            status = SUCCESS;
            message = SUCCESS;
            httpStatus = HttpStatus.OK;

            activityRepository.deleteById(id);
        }

        return new ResponseEntity<>(ActivityGroupResponse.builder()
                .status(status)
                .message(message)
                .data(Activity.builder().build())
                .build(), httpStatus);
    }
}
