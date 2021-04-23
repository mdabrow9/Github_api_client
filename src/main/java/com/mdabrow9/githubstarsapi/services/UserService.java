package com.mdabrow9.githubstarsapi.services;

import com.mdabrow9.githubapi.domain.Repo;
import com.mdabrow9.githubstarsapi.model.RepoDTO;
import com.mdabrow9.githubstarsapi.model.UserStarsDTO;

import java.util.List;

public interface UserService
{
    List<RepoDTO> getUserRepos(String username);
    List<RepoDTO> getUserReposPaginated(String username,int per_page,int page);
    UserStarsDTO getUserStargazers(String username);

}
