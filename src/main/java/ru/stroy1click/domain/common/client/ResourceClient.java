package ru.stroy1click.domain.common.client;

public interface ResourceClient<T,ID> extends CrudOperations<T, ID>, ImageAssignmentService<ID> {

}
