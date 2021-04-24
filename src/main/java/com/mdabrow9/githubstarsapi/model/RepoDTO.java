package com.mdabrow9.githubstarsapi.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RepoDTO
{
    private String name;
    private Integer stargazerCount;

}

