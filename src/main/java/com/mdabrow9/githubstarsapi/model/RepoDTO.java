package com.mdabrow9.githubstarsapi.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


public class RepoDTO
{
    private String name;
    private int stargazerCount;

    public RepoDTO()
    {
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public int getStargazerCount()
    {
        return stargazerCount;
    }

    public void setStargazerCount(int stargazerCount)
    {
        this.stargazerCount = stargazerCount;
    }
}

