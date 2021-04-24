package com.mdabrow9.githubstarsapi.controllers;

import com.mdabrow9.githubstarsapi.model.RepoDTO;
import com.mdabrow9.githubstarsapi.model.UserStarsDTO;
import com.mdabrow9.githubstarsapi.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController
{
    UserService userService;

    public UserController(UserService userService)
    {
        this.userService = userService;
    }


    @GetMapping({"/{username}/repos"})
    public List<RepoDTO> getUserRepos(@PathVariable String username,
                                                      @RequestParam(required = false ) String per_page,
                                                      @RequestParam(required = false ) String page)
    {
        if(per_page==null && page==null)
        {
            return userService.getUserRepos(username);
        }
        int per_pageInt=30,pageInt=1;
        try {
            if(per_page !=null) per_pageInt =  Integer.parseInt(per_page);
        } catch (NumberFormatException e)
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Given parameter \"per_page\" is not integer");
        }
        try {
            if(page !=null) pageInt =  Integer.parseInt(page);
        } catch (NumberFormatException e)
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Given parameter \"page\" is not integer");
        }
        if(per_pageInt>100)
        {
            per_pageInt=100;
        }
        else if(per_pageInt<1)
        {
            per_pageInt=1;
        }
        if(pageInt<1)
        {
            pageInt=1;
        }

        return userService.getUserReposPaginated(username,per_pageInt,pageInt);
    }
    @GetMapping({"/{username}/stars"})
    public UserStarsDTO getUserStars(@PathVariable String username)
    {
        return userService.getUserStargazers(username);
    }




}
