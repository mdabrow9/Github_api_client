package com.mdabrow9.githubstarsapi.conventers;

import com.mdabrow9.githubapi.domain.Repo;
import com.mdabrow9.githubstarsapi.model.RepoDTO;
import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Component
public class RepoToRepoDTO implements Converter<Repo,RepoDTO>
{
    @Synchronized
    @Nullable
    @Override
    public RepoDTO convert(Repo source)
    {
        if (source == null) {
            return null;
        }

        final RepoDTO repoDTO = new RepoDTO();

        repoDTO.setName(source.getName());
        repoDTO.setStargazerCount(source.getStargazers_count());

        return repoDTO;
    }
}
