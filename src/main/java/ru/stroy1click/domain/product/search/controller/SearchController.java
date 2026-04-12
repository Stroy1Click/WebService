package ru.stroy1click.domain.product.search.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.stroy1click.domain.product.search.client.SearchClient;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/search")
public class SearchController {

    private final SearchClient searchClient;

    @GetMapping
    public List<Integer> getProductIds(@RequestParam("q") String query){
        return this.searchClient.getProductIds(query);
    }
}
