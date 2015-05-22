package soap.domain.service.todo;

import soap.domain.model.Todo;

import javax.jws.WebService;

@WebService(targetNamespace = "http://todo.ws.soap/")
public interface TodoService {

    Todo getTodo(String todoId);

}
