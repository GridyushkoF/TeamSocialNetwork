package ru.skillbox.userservice.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.skillbox.userservice.controller.FriendController;
import ru.skillbox.userservice.model.dto.FriendDto;
import ru.skillbox.userservice.model.dto.FriendSearchDto;
import ru.skillbox.commondto.account.StatusCode;

import java.time.LocalDateTime;
//import java.util.concurrent.TestUtil;


import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FriendController.class)
public class FriendControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FriendshipService friendshipService;

//    @Test
//    void addFriend() throws Exception {
//        FriendSearchDto searchDto = new FriendSearchDto()
//                .setIds(List.of(1L))
//                .setFirstName("Test User");
//        doNothing().when(friendshipService).requestFriendship(searchDto.getIds().getFirst(), 2L);
//
//        mockMvc.perform(MockMvcRequestBuilders.post("/friends/request")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(TestUtil.convertObjectToJsonBytes(searchDto)))
//                .andExpect(status().isOk());
//    }

    @Test
    void deleteFriendship() throws Exception {
        doNothing().when(friendshipService).deleteFriendship(1L, 2L);

        mockMvc.perform(MockMvcRequestBuilders.delete("/friends/2")
                        .header("id", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void approveFriend() throws Exception {
        doNothing().when(friendshipService).approveFriendship(1L, 2L);

        mockMvc.perform(MockMvcRequestBuilders.put("/friends/2/approve")
                .header("id", "1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void blockFriend() throws Exception {
        doNothing().when(friendshipService).blockAccount(1L, 2L);

        mockMvc.perform(MockMvcRequestBuilders.put("/friends/block/2")
                        .header("id", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void unblockFriend() throws Exception {
        doNothing().when(friendshipService).deleteFriendship(1L, 2L);

        mockMvc.perform(MockMvcRequestBuilders.put("/friends/unblock/2")
                        .header("id", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void subscribe() throws Exception {
        doNothing().when(friendshipService).subscribeToAccount(1L, 2L);

        mockMvc.perform(MockMvcRequestBuilders.post("/friends/subscribe/2")
                        .header("id", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void getFriends() throws Exception {
        List<FriendDto> friendDtos = List.of(
                new FriendDto(1L, "https://example.com/avatar.jpg", StatusCode.REQUEST_TO, "Test", "User", "City", "Country", LocalDateTime.now(), true)
        );
        when(friendshipService.getFriendsByStatus(StatusCode.REQUEST_TO, 3, 1L)).thenReturn(new org.springframework.data.domain.PageImpl<>(friendDtos));

        mockMvc.perform(MockMvcRequestBuilders.get("/friends")
                        .header("id", "1")
                        .param("statusCode", "REQUEST_TO") // Use REQUEST_TO as it is present in your enum
                        .param("size", "3")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void getByRecommendation() throws Exception {
        when(friendshipService.getFriendRecommendations(any(FriendSearchDto.class), anyLong())).thenReturn(List.of());

        mockMvc.perform(MockMvcRequestBuilders.get("/friends/recommendations")
                        .header("id", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void requestCount() throws Exception {
        when(friendshipService.getFriendRequestCount(1L)).thenReturn(1);

        mockMvc.perform(MockMvcRequestBuilders.get("/friends/count")
                        .header("id", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("1"));
    }
}
