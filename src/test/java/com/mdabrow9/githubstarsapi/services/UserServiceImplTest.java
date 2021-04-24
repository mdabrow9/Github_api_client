package com.mdabrow9.githubstarsapi.services;

import com.mdabrow9.githubapi.domain.Repo;
import com.mdabrow9.githubstarsapi.Exceptions.NotFoundException;
import com.mdabrow9.githubstarsapi.conventers.RepoListToUserStarsDTO;
import com.mdabrow9.githubstarsapi.conventers.RepoToRepoDTO;
import com.mdabrow9.githubstarsapi.model.RepoDTO;
import com.mdabrow9.githubstarsapi.model.UserStarsDTO;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(MockitoJUnitRunner.class)
class UserServiceImplTest
{

    @Mock
    private RestTemplate restTemplate;
    @Spy
    private RepoToRepoDTO repoToRepoDTO;
    @Spy
    private RepoListToUserStarsDTO repoListToUserStarsDTO;

    private UserServiceImpl userService;

    private static Repo[] testRepos = new Repo[10];

    @BeforeEach
    public void setUp()
    {
        MockitoAnnotations.initMocks(this);

        this.userService = new UserServiceImpl(restTemplate,repoToRepoDTO,repoListToUserStarsDTO,"https://api.github.com");

    }

    @BeforeAll
    public static void setTestRepos()
    {
        for(int i = 0; i < 10; i++)
        {
            Repo tmp = new Repo();
            tmp.setName("testRepo"+i);
            tmp.setStargazers_count(i);
            testRepos[i] = tmp;
        }
    }

    @Test
    void getUserRepos()
    {
        Mockito.when(restTemplate.getForEntity("https://api.github.com/users/user1/repos?per_page=50&page=1",Repo[].class))
                .thenReturn(new ResponseEntity(testRepos, HttpStatus.OK));
        Mockito.when(restTemplate.getForEntity("https://api.github.com/users/user1/repos?per_page=50&page=2",Repo[].class))
                .thenReturn(new ResponseEntity(new Repo[0], HttpStatus.OK));

        List<RepoDTO> list = userService.getUserRepos("user1");
        assertEquals( 10, list.size());
        assertEquals("testRepo0", list.get(0).getName());
    }

    @Test
    void getUserReposPaginated()
    {
        Repo[] testOneRepo = {testRepos[3]};
        Mockito.when(restTemplate.getForEntity("https://api.github.com/users/user1/repos?per_page=1&page=3",Repo[].class))
                .thenReturn(new ResponseEntity(testOneRepo, HttpStatus.OK));
        List<RepoDTO> list = userService.getUserReposPaginated("user1",1,3);
        assertEquals( 1, list.size());
        assertEquals("testRepo3", list.get(0).getName());
    }

    @Test
    void getUserStargazers()
    {
        Mockito.when(restTemplate.getForEntity("https://api.github.com/users/user1/repos?per_page=50&page=1",Repo[].class))
                .thenReturn(new ResponseEntity(testRepos, HttpStatus.OK));
        Mockito.when(restTemplate.getForEntity("https://api.github.com/users/user1/repos?per_page=50&page=2",Repo[].class))
                .thenReturn(new ResponseEntity(new Repo[0], HttpStatus.OK));
        UserStarsDTO userStarsDTO = userService.getUserStargazers("user1");
        assertEquals( 45,userStarsDTO.getUserStargazerCount());
    }
    @Test
    void getUsersExceptionTest()
    {
        Mockito.when(restTemplate.getForEntity("https://api.github.com/users/user1/repos?per_page=50&page=1",Repo[].class))
                .thenThrow(NotFoundException.class);
        assertThrows(NotFoundException.class, () -> userService.getUserRepos("user1"));
    }
}