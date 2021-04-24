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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static net.bytebuddy.matcher.ElementMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@RunWith(MockitoJUnitRunner.class)
class UserControllerTest
{
    @Spy
    private UserService userService;

    @InjectMocks
    private UserController userController;

    MockMvc mockMvc;

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
    public void init()
    {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    public void testStatus() throws Exception {


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
    void getUserReposPaginated() throws Exception
    {
        // MockMvc mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
        when(userService.getUserReposPaginated(anyString(),eq(2),eq(3))).thenReturn(List.of(testRepos.get(4),testRepos.get(5)));


        mockMvc.perform(get("/users/user1/repos?per_page=2&page=3")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name").value("testRepo4"))
                .andExpect(jsonPath("$[0].stargazerCount").value(4))
                .andExpect(jsonPath("$[1].name").value("testRepo5"))
                .andExpect(jsonPath("$[1].stargazerCount").value(5));


        verify(userService , times(1)).getUserReposPaginated(eq("user1"),eq(2),eq(3));

    }
    @Test
    void getUserReposPaginatedBadRequest() throws Exception
    {
        mockMvc.perform(get("/users/user1/repos?per_page=abc")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        verify(userService , times(0)).getUserReposPaginated(anyString(),anyInt(),anyInt());

    }
    @Test
    void getUserReposPaginatedBadRequest2() throws Exception
    {
        mockMvc.perform(get("/users/user1/repos?page=abc")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        verify(userService , times(0)).getUserReposPaginated(anyString(),anyInt(),anyInt());

    }
    @Test
    void getUserReposPaginatedNotFound() throws Exception
    {

        when(userService.getUserReposPaginated(anyString(),anyInt(),anyInt())).thenThrow(new NotFoundException("NOT FOUND"));
        mockMvc.perform(get("/users/user1/repos?per_page=20&page=1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
        verify(userService , times(1)).getUserReposPaginated(anyString(),anyInt(),anyInt());

    }
    @Test
    void getUserReposPaginatedMaxAndMinValues() throws Exception
    {

        when(userService.getUserReposPaginated(anyString(),anyInt(),anyInt())).thenReturn(testRepos);
        mockMvc.perform(get("/users/user1/repos?per_page=200000")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        verify(userService , times(1)).getUserReposPaginated(anyString(),eq(100),anyInt());
        mockMvc.perform(get("/users/user1/repos?per_page=-10&page=-1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        verify(userService , times(1)).getUserReposPaginated(anyString(),eq(1),eq(1));

    }


    @Test
    void getUserRepos() throws Exception
    {
       // MockMvc mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
        when(userService.getUserRepos(Mockito.anyString())).thenReturn(List.of(testRepos.get(0),testRepos.get(1)));


        mockMvc.perform(get("/users/user1/repos")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(2)))
            .andExpect(jsonPath("$[0].name").value("testRepo0"))
            .andExpect(jsonPath("$[0].stargazerCount").value(0))
            .andExpect(jsonPath("$[1].name").value("testRepo1"))
            .andExpect(jsonPath("$[1].stargazerCount").value(1));


        verify(userService , times(1)).getUserRepos(eq("user1"));

    }
    @Test
    void getUserReposNotFound() throws Exception
    {

        when(userService.getUserRepos(Mockito.anyString())).thenThrow(new NotFoundException("NOT FOUND"));


        mockMvc.perform(get("/users/user1/repos")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
        verify(userService , times(1)).getUserRepos(eq("user1"));

    }

    @Test
    void getUserStars() throws Exception
    {

        UserStarsDTO userStarsDTO = new UserStarsDTO();
        userStarsDTO.setUserStargazerCount(10);
        when(userService.getUserStargazers(Mockito.anyString())).thenReturn(userStarsDTO);

        mockMvc.perform(get("/users/user1/stars")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())

                .andExpect(jsonPath("$.userStargazerCount").value(10));


        verify(userService , times(1)).getUserStargazers(eq("user1"));
    }

    @Test
    void getUserStarsNotFound() throws Exception
    {

        UserStarsDTO userStarsDTO = new UserStarsDTO();
        userStarsDTO.setUserStargazerCount(10);
        when(userService.getUserStargazers(Mockito.anyString())).thenThrow(new NotFoundException("NOT FOUND"));

        mockMvc.perform(get("/users/user1/stars")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
        verify(userService , times(1)).getUserStargazers(eq("user1"));
    }
}