package com.mdabrow9.githubstarsapi.conventers;

import com.mdabrow9.githubapi.domain.Repo;
import com.mdabrow9.githubstarsapi.model.UserStarsDTO;
import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.util.List;
@Component
public class RepoListToUserStarsDTO implements Converter<List<Repo>, UserStarsDTO>
{
    @Synchronized
    @Nullable
    @Override
    public UserStarsDTO convert(List<Repo> source)
    {
        if (source == null) {
            return null;
        }

        final UserStarsDTO userStarsDTO = new UserStarsDTO();

        final int count = source.stream().mapToInt(o -> o.getStargazers_count()).sum();
        userStarsDTO.setUserStargazerCount(count);

        return userStarsDTO;
    }
}
