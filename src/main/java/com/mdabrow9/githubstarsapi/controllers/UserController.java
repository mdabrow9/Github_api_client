package com.mdabrow9.githubstarsapi.controllers;

import com.mdabrow9.githubstarsapi.model.RepoDTO;
import com.mdabrow9.githubstarsapi.model.UserStarsDTO;
import com.mdabrow9.githubstarsapi.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/users")
public class UserController
{
    UserService userService;

    public UserController(UserService userService)
    {
        this.userService = userService;
    }


    @GetMapping({"/{username}/repos"})
    public ResponseEntity<List<RepoDTO>> getUserRepos(@PathVariable String username,
                                                      @RequestParam(required = false ) String per_page,
                                                      @RequestParam(required = false ) String page)
    {
        if(per_page==null && page==null)
        {
            return new ResponseEntity<>(userService.getUserRepos(username), HttpStatus.OK);
        }
        int per_pageInt=30,pageInt=1;
        try {
            per_pageInt =  Integer.parseInt(per_page);
        } catch (NumberFormatException ignored) {

        }
        try {
            pageInt =  Integer.parseInt(page);
        } catch (Exception ignored) {
        }
        if(per_pageInt>100) per_pageInt=100;
        if(pageInt<1) pageInt=1;

        return new ResponseEntity<>(userService.getUserReposPaginated(username,per_pageInt,pageInt), HttpStatus.OK);

    }
    @GetMapping({"/{username}/stars"})
    public ResponseEntity<UserStarsDTO> getUserStars(@PathVariable String username)
    {
        return new ResponseEntity<UserStarsDTO>(userService.getUserStargazers(username), HttpStatus.OK);
    }




}
