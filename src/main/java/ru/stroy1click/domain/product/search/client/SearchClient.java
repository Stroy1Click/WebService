package ru.stroy1click.domain.product.search.client;

import java.util.List;

public interface SearchClient {

    List<Integer> getProductIds(String query);
}
