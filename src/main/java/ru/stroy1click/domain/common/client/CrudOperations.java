package ru.stroy1click.domain.common.client;

import java.util.List;

public interface CrudOperations<T, ID>{

    T get(ID id);

    List<T> getAll();

    T create(T dto, String jwt);

    void update(ID id, T dto, String jwt);

    void delete(ID id, String jwt);
}
