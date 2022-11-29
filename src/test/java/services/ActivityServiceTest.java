package services;

import activity.dto.GeneralRequest;
import activity.entity.Activity;
import activity.repository.ActivityRepository;
import activity.services.ActivityService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
@ContextConfiguration(classes = ActivityService.class)
public class ActivityServiceTest {
    @Autowired
    private ActivityService activityService;

    @MockBean
    private ActivityRepository activityRepository;

    @BeforeEach
    void init () {
        when(activityRepository.findTodoItemsByActivityGroupId(1L)).thenReturn(List.of(Activity.builder().build()));
        when(activityRepository.findItemById(1L)).thenReturn(Activity.builder().build());
        when(activityRepository.save(any())).thenReturn(Activity.builder().build());
        when(activityRepository.existsById(1234L)).thenReturn(Boolean.FALSE);
        when(activityRepository.existsById(1L)).thenReturn(Boolean.TRUE);
    }

    @Test
    void givenIdNotNull_whenGetAllTodoItems_thenfindTodoItemsByActivityGroupIdSuccess() {
        assertNotNull(activityService.getAllTodoItems(1L).getBody().getData());
    }

    @Test
    void givenIdNull_whenGetAllTodoItems_thenFindAllSuccess() {
        assertNotNull(activityService.getAllTodoItems(null).getBody().getData());
    }

    @Test
    public void givenIdNull_whenGetAllTodoItems_thenNotFound() {
        assertEquals(NOT_FOUND_STATUS, activityService.getTodoItems(null).getBody().getStatus());
    }

    @Test
    public void givenIdNotNull_whenGetAllTodoItems_thenNotFound() {
        assertEquals(SUCCESS, activityService.getTodoItems(1L).getBody().getStatus());
    }

    @Test
    public void givenTitleAndActivityGroupIdNotNull_whenCreateTodoItems_thenSuccess() {
        assertNotNull(activityService.createTodoItems(GeneralRequest.builder().title("test").activityGroupId(1L).build()).getBody().getData());
    }

    @Test
    public void givenTitleNull_whenCreateTodoItems_thenBadRequest() {
        assertEquals(BAD_REQUEST_STATUS, activityService.createTodoItems(GeneralRequest.builder().activityGroupId(1L).build()).getBody().getStatus());
    }

    @Test
    public void givenActivityGroupIdNull_whenCreateTodoItems_thenBadRequest() {
        assertEquals(BAD_REQUEST_STATUS, activityService.createTodoItems(GeneralRequest.builder().title("test").build()).getBody().getStatus());
    }

    @Test
    public void givenIdNotFound_whenUpdateTodoItems_thenNotFound() {
        assertEquals(NOT_FOUND_STATUS, activityService.updateTodoItems(GeneralRequest.builder().title("test").build(), 1234L).getBody().getStatus());
    }

    @Test
    public void givenTitleNotNull_whenUpdateTodoItems_thenSuccess() {
        assertNotNull(activityService.updateTodoItems(GeneralRequest.builder().title("test").build(), 1L).getBody().getData());
    }

    @Test
    public void givenisActive_whenUpdateTodoItems_thenSuccess() {
        assertNotNull(activityService.updateTodoItems(GeneralRequest.builder().isActive(Boolean.TRUE).build(), 1L).getBody().getData());
    }

    @Test
    public void givenTitleAndIsActiveNull_whenUpdateTodoItems_thenBadRequest() {
        assertEquals(BAD_REQUEST_STATUS, activityService.updateTodoItems(GeneralRequest.builder().build(), 1L).getBody().getStatus());
    }

    @Test
    public void givenIdFound_whenDeleteTodoItems_thenSuccess() {
        assertEquals(SUCCESS, activityService.deleteTodoItems( 1L).getBody().getStatus());
    }

    @Test
    public void givenIdNotFound_whenDeleteTodoItems_thenSuccess() {
        assertEquals(NOT_FOUND_STATUS, activityService.deleteTodoItems( 1234L).getBody().getStatus());
    }
}
