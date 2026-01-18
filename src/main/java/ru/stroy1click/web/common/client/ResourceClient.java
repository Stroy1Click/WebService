package ru.stroy1click.web.common.client;

public interface ResourceClient<T,ID> extends CrudOperations<T, ID>, ImageManager<ID> {

}
