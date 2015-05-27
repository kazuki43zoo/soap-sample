package soap.client.todo;

import soap.client.WsBusinessException;
import soap.client.WsResourceNotFoundException;
import soap.client.WsValidationException;
import soap.domain.model.Todo;

import javax.jws.WebService;
import java.util.List;

@WebService(targetNamespace = "http://todo.jaxws.soap/")
public interface TodoService {

    List<Todo> getTodos();

    Todo getTodo(String todoId) throws WsValidationException, WsResourceNotFoundException;

    Todo createTodo(Todo todo) throws WsValidationException, WsBusinessException;

    Todo updateTodo(Todo todo) throws WsValidationException, WsResourceNotFoundException;

    void deleteTodo(String todoId) throws WsValidationException;

    void deleteTodos();

}
