package services;

import activity.dto.GeneralRequest;
import activity.entity.ActivityGroup;
import activity.repository.ActivityGroupRepository;
import activity.services.ActivityGroupService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static activity.services.ActivityGroupService.BAD_REQUEST_STATUS;
import static activity.services.ActivityService.NOT_FOUND_STATUS;
import static activity.services.ActivityService.SUCCESS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = ActivityGroupService.class)
public class ActivityGroupServiceTest {
    @Autowired
    private ActivityGroupService activityGroupService;

    @MockBean
    private ActivityGroupRepository activityGroupRepository;

    @BeforeEach
    void init () {
        when(activityGroupRepository.findAllActivity()).thenReturn(List.of(ActivityGroup.builder().build()));
        when(activityGroupRepository.findByEmail(any())).thenReturn(List.of(ActivityGroup.builder().build()));
        when(activityGroupRepository.findByActivityId(1L)).thenReturn(ActivityGroup.builder().build());
        when(activityGroupRepository.save(any())).thenReturn(ActivityGroup.builder().build());
        when(activityGroupRepository.existsById(1234L)).thenReturn(Boolean.FALSE);
        when(activityGroupRepository.existsById(1L)).thenReturn(Boolean.TRUE);
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "cek@gmail.com"})
    void givenAnyvalue_whenGetAllActivity_thenSuccess(String email) {
        assertNotNull(activityGroupService.getAllActivity(email).getBody().getData());
    }

    @Test
    void givenIdNotNull_whenGetActivity_thenSuccess() {
        assertNotNull(activityGroupService.getActivity(1L).getBody().getData());
    }

    @Test
    public void givenIdNull_whenGetActivity_thenNotFound() {
        assertEquals(NOT_FOUND_STATUS, activityGroupService.getActivity(null).getBody().getStatus());
    }

    @Test
    public void givenTitleNull_whenCreateActivity_thenBadRequest() {
        assertEquals(BAD_REQUEST_STATUS, activityGroupService.createActivity(GeneralRequest.builder().build()).getBody().getStatus());
    }

    @Test
    public void givenTitleNotNull_whenCreateActivity_thenSuccess() {
        assertNotNull(activityGroupService.createActivity(GeneralRequest.builder().title("test").build()).getBody().getData());
    }

    @Test
    public void givenTitleNull_whenUpdateActivity_thenBadRequest() {
        assertEquals(BAD_REQUEST_STATUS, activityGroupService.updateActivity(GeneralRequest.builder().build(), 1L).getBody().getStatus());
    }

    @Test
    public void givenIdNotFound_whenUpdateActivity_thenNotFound() {
        assertEquals(NOT_FOUND_STATUS, activityGroupService.updateActivity(GeneralRequest.builder().title("test").build(), 1234L).getBody().getStatus());
    }

    @Test
    public void givenTitleNotNull_whenUpdateActivity_thenSuccess() {
        assertNotNull(activityGroupService.updateActivity(GeneralRequest.builder().title("test").build(), 1L).getBody().getData());
    }

    @Test
    public void givenIdFound_whenDeleteActivity_thenSuccess() {
        assertEquals(SUCCESS, activityGroupService.deleteActivity( 1L).getBody().getStatus());
    }

    @Test
    public void givenIdNotFound_whenDeleteActivity_thenSuccess() {
        assertEquals(NOT_FOUND_STATUS, activityGroupService.deleteActivity( 1234L).getBody().getStatus());
    }
}
