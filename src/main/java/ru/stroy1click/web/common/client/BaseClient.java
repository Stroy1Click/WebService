package ru.stroy1click.web.common.client;

public interface BaseClient<T, ID>{

    T get(ID id);

    void create(T dto, String jwt);

    void update(ID id, T dto, String jwt);

    void delete(ID id, String jwt);
}
