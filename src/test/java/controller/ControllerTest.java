package controller;

import activity.controller.ActivityController;
import activity.dto.ActivityGroupResponse;
import activity.dto.GeneralRequest;
import activity.repository.ActivityGroupRepository;
import activity.services.ActivityGroupService;
import activity.services.ActivityService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.stream.Stream;

import static activity.controller.ActivityController.ACTIVITY_GROUP_URL;
import static activity.controller.ActivityController.TODO_ITEMS_URL;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class ControllerTest {

    @Mock
    private WebApplicationContext webApplicationContext;

    @InjectMocks
    private ActivityController controller;

    @Mock
    private ActivityGroupRepository activityGroupRepository;

    @Mock
    private ActivityGroupService activityGroupService;

    @Mock
    private ActivityService activityService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @ParameterizedTest
    @MethodSource("getActivityParam")
    void getAllActivityByEmail(String value) throws Exception {

        when(activityGroupService.getAllActivity("cek@gmail.com")).thenReturn(new ResponseEntity<>(ActivityGroupResponse.builder().build(), HttpStatus.OK));
        when(activityGroupService.getAllActivity(null)).thenReturn(new ResponseEntity<>(ActivityGroupResponse.builder().build(), HttpStatus.OK));

        mockMvc.perform(get(value)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @ParameterizedTest
    @MethodSource("getOneActivityParam")
    void saveUpdateContact(String value, Long id) throws Exception {

        when(activityGroupService.getActivity(1L)).thenReturn(new ResponseEntity<>(ActivityGroupResponse.builder().build(), HttpStatus.OK));
        when(activityGroupService.getActivity(null)).thenReturn(new ResponseEntity<>(ActivityGroupResponse.builder().build(), HttpStatus.OK));
        when(activityGroupService.getActivity(1234L)).thenReturn(new ResponseEntity<>(ActivityGroupResponse.builder().build(), HttpStatus.NOT_FOUND));

        if (id != null && id == 1234L) {
            mockMvc.perform(get(value)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound());

        } else {
            when(activityGroupRepository.findByActivityId(any())).thenReturn(null);
            mockMvc.perform(get(value)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());
        }
    }

    @ParameterizedTest
    @MethodSource("createActivityParam")
    void createActivity(String value, GeneralRequest request) throws Exception {

        if (StringUtils.isBlank(request.getTitle())) {
            when(activityGroupService.createActivity(any())).thenReturn(new ResponseEntity<>(null, HttpStatus.BAD_REQUEST));
            mockMvc.perform(post(value)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());

        } else {
            mockMvc.perform(post(value)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk());
        }
    }

    @ParameterizedTest
    @MethodSource("updateActivityParam")
    void updateActivity(String value, Long id, GeneralRequest request) throws Exception {

        if (StringUtils.isBlank(request.getTitle())) {
            when(activityGroupService.updateActivity(any(), anyLong())).thenReturn(new ResponseEntity<>(null, HttpStatus.BAD_REQUEST));
            mockMvc.perform(patch(value)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());

        } else if (id != null && id == 1234L) {
            when(activityGroupService.updateActivity(any(), eq(1234L))).thenReturn(new ResponseEntity<>(null, HttpStatus.NOT_FOUND));
            mockMvc.perform(patch(value)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isNotFound());

        } else {
            mockMvc.perform(patch(value)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk());
        }
    }

    @ParameterizedTest
    @MethodSource("deleteActivityParam")
    void deleteActivity(String value, Long id) throws Exception {
        if (id == 1L) {
            mockMvc.perform(delete(value))
                    .andExpect(status().isOk());
        } else {
            when(activityGroupService.deleteActivity(1234L)).thenReturn(new ResponseEntity<>(null, HttpStatus.NOT_FOUND));
            mockMvc.perform(delete(value))
                    .andExpect(status().isNotFound());
        }
    }

    private static Stream<Arguments> getActivityParam() {
        return Stream.of(
                Arguments.of(ACTIVITY_GROUP_URL),
                Arguments.of(ACTIVITY_GROUP_URL + "?email=cek@gmail.com")
        );
    }

    private static Stream<Arguments> getOneActivityParam() {
        return Stream.of(
                Arguments.of(ACTIVITY_GROUP_URL, null),
                Arguments.of(ACTIVITY_GROUP_URL + "/1", 1L),
                Arguments.of(ACTIVITY_GROUP_URL + "/1234", 1234L)
        );
    }

    private static Stream<Arguments> createActivityParam() {
        return Stream.of(
                Arguments.of(ACTIVITY_GROUP_URL, GeneralRequest.builder().title("test").build()),
                Arguments.of(ACTIVITY_GROUP_URL, GeneralRequest.builder().build())
        );
    }

    private static Stream<Arguments> updateActivityParam() {
        return Stream.of(
                Arguments.of(ACTIVITY_GROUP_URL + "/1", 1L, GeneralRequest.builder().title("test").build()),
                Arguments.of(ACTIVITY_GROUP_URL + "/1234", 1234L, GeneralRequest.builder().build()),
                Arguments.of(ACTIVITY_GROUP_URL + "/1", 1L, GeneralRequest.builder().title("test").build())
        );
    }

    private static Stream<Arguments> deleteActivityParam() {
        return Stream.of(
                Arguments.of(ACTIVITY_GROUP_URL + "/1", 1L),
                Arguments.of(ACTIVITY_GROUP_URL + "/1234", 1234L)
        );
    }

    @ParameterizedTest
    @MethodSource("getTodoParam")
    void getAllTodoById(String value) throws Exception {

        when(activityService.getAllTodoItems(1L)).thenReturn(new ResponseEntity<>(ActivityGroupResponse.builder().build(), HttpStatus.OK));
        when(activityService.getAllTodoItems(null)).thenReturn(new ResponseEntity<>(ActivityGroupResponse.builder().build(), HttpStatus.OK));

        mockMvc.perform(get(value)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @ParameterizedTest
    @MethodSource("getOneTodoParam")
    void saveUpdateTodo(String value, Long id) throws Exception {

        when(activityService.getTodoItems(1L)).thenReturn(new ResponseEntity<>(ActivityGroupResponse.builder().build(), HttpStatus.OK));
        when(activityService.getTodoItems(null)).thenReturn(new ResponseEntity<>(ActivityGroupResponse.builder().build(), HttpStatus.OK));
        when(activityService.getTodoItems(1234L)).thenReturn(new ResponseEntity<>(ActivityGroupResponse.builder().build(), HttpStatus.NOT_FOUND));

        if (id != null && id == 1234L) {
            mockMvc.perform(get(value)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound());

        } else {
            when(activityService.getTodoItems(any())).thenReturn(null);
            mockMvc.perform(get(value)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());
        }
    }

    @ParameterizedTest
    @MethodSource("createTodoParam")
    void createTodo(String value, GeneralRequest request) throws Exception {

        if (StringUtils.isBlank(request.getTitle())) {
            when(activityService.createTodoItems(any())).thenReturn(new ResponseEntity<>(null, HttpStatus.BAD_REQUEST));
            mockMvc.perform(post(value)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());

        } else {
            mockMvc.perform(post(value)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk());
        }
    }

    @ParameterizedTest
    @MethodSource("updateTodoParam")
    void updateTodo(String value, Long id, GeneralRequest request) throws Exception {

        if (StringUtils.isBlank(request.getTitle())) {
            when(activityService.updateTodoItems(any(), anyLong())).thenReturn(new ResponseEntity<>(null, HttpStatus.BAD_REQUEST));
            mockMvc.perform(patch(value)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());

        } else if (id != null && id == 1234L) {
            when(activityService.updateTodoItems(any(), eq(1234L))).thenReturn(new ResponseEntity<>(null, HttpStatus.NOT_FOUND));
            mockMvc.perform(patch(value)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isNotFound());

        } else {
            mockMvc.perform(patch(value)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk());
        }
    }

    @ParameterizedTest
    @MethodSource("deleteTodoParam")
    void deleteTodo(String value, Long id) throws Exception {
        if (id == 1L) {
            mockMvc.perform(delete(value))
                    .andExpect(status().isOk());
        } else {
            when(activityService.deleteTodoItems(1234L)).thenReturn(new ResponseEntity<>(null, HttpStatus.NOT_FOUND));
            mockMvc.perform(delete(value))
                    .andExpect(status().isNotFound());
        }
    }

    private static Stream<Arguments> getTodoParam() {
        return Stream.of(
                Arguments.of(TODO_ITEMS_URL),
                Arguments.of(TODO_ITEMS_URL + "?activity_group_id=1")
        );
    }

    private static Stream<Arguments> getOneTodoParam() {
        return Stream.of(
                Arguments.of(TODO_ITEMS_URL, null),
                Arguments.of(TODO_ITEMS_URL + "/1", 1L),
                Arguments.of(TODO_ITEMS_URL + "/1234", 1234L)
        );
    }

    private static Stream<Arguments> createTodoParam() {
        return Stream.of(
                Arguments.of(TODO_ITEMS_URL, GeneralRequest.builder().title("test").build()),
                Arguments.of(TODO_ITEMS_URL, GeneralRequest.builder().build())
        );
    }

    private static Stream<Arguments> updateTodoParam() {
        return Stream.of(
                Arguments.of(TODO_ITEMS_URL + "/1", 1L, GeneralRequest.builder().title("test").build()),
                Arguments.of(TODO_ITEMS_URL + "/1234", 1234L, GeneralRequest.builder().build()),
                Arguments.of(TODO_ITEMS_URL + "/1", 1L, GeneralRequest.builder().title("test").build())
        );
    }

    private static Stream<Arguments> deleteTodoParam() {
        return Stream.of(
                Arguments.of(TODO_ITEMS_URL + "/1", 1L),
                Arguments.of(TODO_ITEMS_URL + "/1234", 1234L)
        );
    }

}
