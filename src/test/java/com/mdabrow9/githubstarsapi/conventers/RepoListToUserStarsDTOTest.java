package com.mdabrow9.githubstarsapi.conventers;

import com.mdabrow9.githubapi.domain.Repo;
import com.mdabrow9.githubstarsapi.model.RepoDTO;
import com.mdabrow9.githubstarsapi.model.UserStarsDTO;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.*;

class RepoListToUserStarsDTOTest
{
    private static RepoListToUserStarsDTO conveter;

    @BeforeAll
    public static void setUp()
    {
        conveter = new RepoListToUserStarsDTO();
    }
    @Test
    public void testNullObject() throws Exception {
        assertNull(conveter.convert(null));
    }
    @Test
    public void convert()
    {
        List<Repo> testRepos = new ArrayList<Repo>();
        for(int i = 0; i < 10; i++)
        {
            Repo tmp = new Repo();
            tmp.setName("testRepo"+i);
            tmp.setStargazers_count(i);
            testRepos.add(tmp);
        }
        int sum = testRepos.stream().mapToInt(o -> o.getStargazers_count()).sum();
        UserStarsDTO userStarsDTO = conveter.convert(testRepos);
        assertNotNull(userStarsDTO);
        assertEquals(sum, userStarsDTO.getUserStargazerCount());

    }

}