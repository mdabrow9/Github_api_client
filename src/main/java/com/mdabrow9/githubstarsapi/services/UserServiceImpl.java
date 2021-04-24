package com.mdabrow9.githubstarsapi.services;

import com.mdabrow9.githubapi.domain.Repo;

import com.mdabrow9.githubstarsapi.conventers.RepoListToUserStarsDTO;
import com.mdabrow9.githubstarsapi.conventers.RepoToRepoDTO;
import com.mdabrow9.githubstarsapi.model.RepoDTO;
import com.mdabrow9.githubstarsapi.model.UserStarsDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class UserServiceImpl implements UserService
{
    private final RestTemplate restTemplate;
    private final RepoToRepoDTO repoToRepoDTO;
    private final RepoListToUserStarsDTO repoListToUserStarsDTO;

    //@Value("${api.url}")
    private final String apiUrl;

    public UserServiceImpl(RestTemplate restTemplate, RepoToRepoDTO repoToRepoDTO, RepoListToUserStarsDTO repoListToUserStarsDTO, @Value("${api.url}") String apiUrl)
    {
        this.restTemplate = restTemplate;
        this.repoToRepoDTO = repoToRepoDTO;
        this.repoListToUserStarsDTO = repoListToUserStarsDTO;

        this.apiUrl = apiUrl;
    }

    @Override
    public List<RepoDTO> getUserRepos(String username)
    {
        List<Repo> list = fetchAllUserRepos(username);

        return list.stream().map(repoToRepoDTO::convert).collect(Collectors.toList());

    }

    @Override
    public List<RepoDTO> getUserReposPaginated(String username, int per_page, int page)
    {

        UriComponentsBuilder uriBuilder = UriComponentsBuilder
                .fromUriString(apiUrl)
                .path("/users/")
                .path(username)
                .path("/repos")
                .queryParam("per_page",per_page)
                .queryParam("page",page);
        ResponseEntity<Repo[]> expect =
                restTemplate.getForEntity( uriBuilder.toUriString(), Repo[].class);
        return Arrays.asList(expect.getBody()).stream().map(repoToRepoDTO::convert).collect(Collectors.toList());
    }

    @Override
    public UserStarsDTO getUserStargazers(String username)
    {

        return repoListToUserStarsDTO.convert(fetchAllUserRepos(username));

    }




    private List<Repo> fetchAllUserRepos(String username)
    {
        List<Repo> repos = new ArrayList();
        int reposCount=0;
        int i=1;
        do
        {
            reposCount = repos.size();
            UriComponentsBuilder uriBuilder = UriComponentsBuilder
                    .fromUriString(apiUrl)
                    .path("/users/")
                    .path(username)
                    .path("/repos")
                    .queryParam("per_page",50)
                    .queryParam("page", i++);
            ResponseEntity<Repo[]> expect =
                    restTemplate.getForEntity( uriBuilder.toUriString(), Repo[].class);
            repos.addAll(Arrays.asList(expect.getBody()));
        }
        while(repos.size() != reposCount );

        return repos;
    }
}
