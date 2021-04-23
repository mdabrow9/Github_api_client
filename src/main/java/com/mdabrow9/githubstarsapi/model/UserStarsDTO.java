package com.mdabrow9.githubstarsapi.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


public class UserStarsDTO
{
    private int userStargazerCount;

    public UserStarsDTO()
    {
    }

    public int getUserStargazerCount()
    {
        return userStargazerCount;
    }

    public void setUserStargazerCount(int userStargazerCount)
    {
        this.userStargazerCount = userStargazerCount;
    }
}
