package com.mdabrow9.githubstarsapi.conventers;

import com.mdabrow9.githubapi.domain.Repo;
import com.mdabrow9.githubstarsapi.model.RepoDTO;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;


import static org.junit.jupiter.api.Assertions.*;

class RepoToRepoDTOTest
{

    private static RepoToRepoDTO conveter;

    @BeforeAll
    public static void setUp()
    {
        conveter = new RepoToRepoDTO();
    }
    @Test
    public void testNullObject() throws Exception {
        assertNull(conveter.convert(null));
    }
    @Test
    public void convert()
    {
        Repo repo = new Repo();
        repo.setName("testRepo");
        repo.setStargazers_count(3);

        RepoDTO repoDTO = conveter.convert(repo);
        assertNotNull(repoDTO);
        assertEquals(repo.getName(),repoDTO.getName());
        assertEquals(repo.getStargazers_count(),repoDTO.getStargazerCount());
    }
}