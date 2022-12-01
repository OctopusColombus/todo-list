/*
 * ActivityGroupService.java
 *
 * Version 1.0.0
 *
 * This class contains all method needed by the API for Activity CRUD
 *
 * Created by Muhammad Isro Prihandoyo
 */

package activity.services;

import activity.dto.ActivityGroupResponse;
import activity.dto.GeneralRequest;
import activity.entity.ActivityGroup;
import activity.repository.ActivityGroupRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class ActivityGroupService {

    @Autowired
    private ActivityGroupRepository activityGroupRepository;

    public static final String SUCCESS = "Success";
    public static final String NOT_FOUND_STATUS = "Not Found";
    public static final String NOT_FOUND_MESSAGE = "Activity with ID ";
    public static final String BAD_REQUEST_STATUS = "Bad Request";
    public static final String BAD_REQUEST_MESSAGE = "title cannot be null";

    public ResponseEntity<ActivityGroupResponse> getAllActivity(String email) {
        log.info("Get All Activity");
        List<ActivityGroup> activityGroups;
        Integer totalData;

        //If email is not empty or null then find by email, else findAll
        if (StringUtils.isNotEmpty(email)) {
            log.info("Find By Email");
            activityGroups = activityGroupRepository.findByEmail(email);
        } else {
            log.info("Find All");
            activityGroups = activityGroupRepository.findAllActivity();
        }

        //Set todoItems to null since it is not needed
        if (!CollectionUtils.isEmpty(activityGroups)) {
            activityGroups.forEach(x -> x.setTodoItems(null));
        }

        ActivityGroupResponse activityGroupResponse = ActivityGroupResponse.builder()
                .status(SUCCESS)
                .message(SUCCESS)
                .data(activityGroups)
                .build();

        return new ResponseEntity<>(activityGroupResponse, HttpStatus.OK);
    }

    public ResponseEntity<ActivityGroupResponse> getActivity(Long id) {
        log.info("Get Activity");
        String status = NOT_FOUND_STATUS;
        String message = StringUtils.join(NOT_FOUND_MESSAGE, id, StringUtils.SPACE, NOT_FOUND_STATUS);
        HttpStatus httpStatus = HttpStatus.NOT_FOUND;

        ActivityGroup activityGroup = ActivityGroup.builder().build();

        //check If Id exist and response not null
        if (activityGroupRepository.existsById(id)) {
            status = SUCCESS;
            message = SUCCESS;
            httpStatus = HttpStatus.OK;
            activityGroup = activityGroupRepository.findByActivityId(id);
        }

        return new ResponseEntity<>(ActivityGroupResponse.builder()
                .data(activityGroup)
                .status(status)
                .message(message)
                .build(), httpStatus);
    }

    public ResponseEntity<ActivityGroupResponse> createActivity(GeneralRequest request) {
        log.info("Create new Activity");
        String status = BAD_REQUEST_STATUS;
        String message = BAD_REQUEST_MESSAGE;
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

        ActivityGroup activityGroup = ActivityGroup.builder().build();

        if (StringUtils.isNotEmpty(request.getTitle())) {
            LocalDateTime now  = LocalDateTime.now();
            status = SUCCESS;
            message = SUCCESS;
            httpStatus = HttpStatus.CREATED;
            activityGroup = ActivityGroup.builder()
                    .title(request.getTitle())
                    .email(request.getEmail())
                    .createdDate(now)
                    .updatedDate(now)
                    .build();

            activityGroup = activityGroupRepository.save(activityGroup);
        }

        return new ResponseEntity<>(ActivityGroupResponse.builder()
                .data(activityGroup)
                .status(status)
                .message(message)
                .build(), httpStatus);
    }

    public ResponseEntity<ActivityGroupResponse> updateActivity(GeneralRequest request, Long id) {
        log.info("Update Activity");
        String status = NOT_FOUND_STATUS;
        String message = StringUtils.join(NOT_FOUND_MESSAGE, id, StringUtils.SPACE, NOT_FOUND_STATUS);
        HttpStatus httpStatus = HttpStatus.NOT_FOUND;

        ActivityGroup activityGroup = ActivityGroup.builder().build();

        if (StringUtils.isBlank(request.getTitle())) {
            status = BAD_REQUEST_STATUS;
            message = BAD_REQUEST_MESSAGE;
            httpStatus = HttpStatus.BAD_REQUEST;

            return new ResponseEntity<>(ActivityGroupResponse.builder()
                    .data(activityGroup)
                    .status(status)
                    .message(message)
                    .build(), httpStatus);
        }

        if (activityGroupRepository.existsById(id)) {
            LocalDateTime now  = LocalDateTime.now();
            status = SUCCESS;
            message = SUCCESS;
            httpStatus = HttpStatus.OK;

            activityGroup = activityGroupRepository.findByActivityId(id);
            activityGroup.setId(id);
            activityGroup.setTitle(request.getTitle());
            activityGroup.setUpdatedDate(now);

            activityGroupRepository.save(activityGroup);

            activityGroup.setTodoItems(null);
        }

        return new ResponseEntity<>(ActivityGroupResponse.builder()
                .data(activityGroup)
                .status(status)
                .message(message)
                .build(), httpStatus);
    }

    public ResponseEntity<ActivityGroupResponse> deleteActivity(Long id) {
        log.info("Delete Activity");
        String status = NOT_FOUND_STATUS;
        String message = StringUtils.join(NOT_FOUND_MESSAGE, id, StringUtils.SPACE, NOT_FOUND_STATUS);
        HttpStatus httpStatus = HttpStatus.NOT_FOUND;

        if (activityGroupRepository.existsById(id)) {
            status = SUCCESS;
            message = SUCCESS;
            httpStatus = HttpStatus.OK;

            activityGroupRepository.deleteById(id);
        }

        return new ResponseEntity<>(ActivityGroupResponse.builder()
                .status(status)
                .message(message)
                .data(ActivityGroup.builder().build())
                .build(), httpStatus);
    }
}
