package soap.domain.service.todo;

import soap.domain.model.Todo;

import javax.jws.WebService;

@WebService
public interface TodoService {

    Todo getTodo(String todoId);

    Todo create(Todo todo);

}
