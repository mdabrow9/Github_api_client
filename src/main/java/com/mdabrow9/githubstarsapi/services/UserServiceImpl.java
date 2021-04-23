package com.mdabrow9.githubstarsapi.services;

import com.mdabrow9.githubapi.domain.Repo;

import com.mdabrow9.githubstarsapi.Exceptions.NotFoundException;
import com.mdabrow9.githubstarsapi.conventers.RepoListToUserStarsDTO;
import com.mdabrow9.githubstarsapi.conventers.RepoToRepoDTO;
import com.mdabrow9.githubstarsapi.errorHandlerer.RestTemplateResponseErrorHandler;
import com.mdabrow9.githubstarsapi.model.RepoDTO;
import com.mdabrow9.githubstarsapi.model.UserStarsDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService
{
    private RestTemplate restTemplate;
    private RepoToRepoDTO repoToRepoDTO;
    private RepoListToUserStarsDTO repoListToUserStarsDTO;

    private final String apiUrl;

    @Autowired
    public UserServiceImpl(RestTemplateBuilder restTemplateBuilder, RepoToRepoDTO repoToRepoDTO, RepoListToUserStarsDTO repoListToUserStarsDTO, @Value("${api.url}") String apiUrl)
    {
        this.restTemplate = restTemplateBuilder
                .errorHandler(new RestTemplateResponseErrorHandler())
                .build();;
        this.repoToRepoDTO = repoToRepoDTO;
        this.repoListToUserStarsDTO = repoListToUserStarsDTO;
        this.apiUrl = apiUrl;
    }
    //on testing purposes
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

        return list.stream().map( item -> repoToRepoDTO.convert(item)).collect(Collectors.toList());

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
        return Arrays.asList(expect.getBody()).stream().map( item -> repoToRepoDTO.convert(item)).collect(Collectors.toList());
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
