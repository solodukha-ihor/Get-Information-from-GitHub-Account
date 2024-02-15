package org.example.Service;

import org.example.Model.Branch;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.example.Model.Repository;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GitHubService {

    private final RestTemplate restTemplate = new RestTemplate();

    public List<Repository> listUserRepositories(String username) {
        String reposUrl = "https://api.github.com/users/" + username + "/repos";
        ResponseEntity<Repository[]> response = restTemplate.getForEntity(reposUrl, Repository[].class);
        List<Repository> repositories = Arrays.asList(response.getBody());

        repositories.forEach(repo -> {
            String branchesUrl = String.format("https://api.github.com/repos/%s/%s/branches", username, repo.getName());
            ResponseEntity<Branch[]> branchesResponse = restTemplate.getForEntity(branchesUrl, Branch[].class);
            repo.setBranches(Arrays.asList(branchesResponse.getBody()));
        });

        return repositories.stream().filter(repo -> !repo.isFork()).collect(Collectors.toList());
    }
}

