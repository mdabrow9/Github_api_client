package com.mdabrow9.githubstarsapi.controllers;

import com.mdabrow9.githubapi.domain.Repo;
import com.mdabrow9.githubstarsapi.Exceptions.NotFoundException;
import com.mdabrow9.githubstarsapi.model.RepoDTO;
import com.mdabrow9.githubstarsapi.model.UserStarsDTO;
import com.mdabrow9.githubstarsapi.services.UserService;
import org.junit.Before;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@RunWith(MockitoJUnitRunner.class)
class UserControllerTest
{
    @Spy
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private static List<RepoDTO> testRepos = new ArrayList<>();

    @BeforeAll
    public static void setTestRepos()
    {
        for(int i = 0; i < 10; i++)
        {
            RepoDTO tmp = new RepoDTO();
            tmp.setName("testRepo"+i);
            tmp.setStargazerCount(i);
            testRepos.add(tmp);
        }
    }

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testStatus() throws Exception {
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(userController).build();

        mockMvc.perform(get("/users/mdabrow9/repos"))
                .andExpect(status().isOk());
        mockMvc.perform(get("/users/mdabrow9/stars"))
                .andExpect(status().isOk());
        mockMvc.perform(get("/users//repos"))
                .andExpect(status().isNotFound());
        mockMvc.perform(get("/users//stars"))
                .andExpect(status().isNotFound());
    }


    @Test
    void getUserRepos()
    {
        when(userService.getUserRepos(Mockito.anyString())).thenReturn(this.testRepos);

        ResponseEntity<List<RepoDTO>> responseEntity = userController.getUserRepos("user1",null,null);

        assertEquals(this.testRepos.size(), responseEntity.getBody().size());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        verify(userService , times(1)).getUserRepos(eq("user1"));

    }
    /*@Test
    void getUserRepos2()
    {
        when(userService.(Mockito.anyString())).thenThrow(new NotFoundException("Not Found"));

        ResponseEntity<List<RepoDTO>> responseEntity = userController.getUserRepos("",null,null);

       // assertEquals(this.testRepos.size(), responseEntity.getBody().size());
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        verify(userService , times(1)).getUserRepos(eq("user1"));

    }
*/
    @Test
    void getUserStars()
    {
        UserStarsDTO userStarsDTO = new UserStarsDTO();
        userStarsDTO.setUserStargazerCount(10);
        when(userService.getUserStargazers(Mockito.anyString())).thenReturn(userStarsDTO);

        ResponseEntity<UserStarsDTO> responseEntity = userController.getUserStars("user1");

        assertEquals(10, responseEntity.getBody().getUserStargazerCount());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        verify(userService , times(1)).getUserStargazers(eq("user1"));
    }
}