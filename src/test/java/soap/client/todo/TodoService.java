package soap.client.todo;

import soap.client.WsBusinessException;
import soap.client.WsResourceNotFoundException;
import soap.client.WsValidationException;
import soap.domain.model.Todo;

import javax.jws.WebService;

@WebService(targetNamespace = "http://todo.jaxws.soap/")
public interface TodoService {

    Todo getTodo(String todoId) throws WsResourceNotFoundException;

    Todo createTodo(Todo todo) throws WsValidationException, WsBusinessException;

    Todo updateTodo(Todo todo) throws WsValidationException,WsResourceNotFoundException;

}
