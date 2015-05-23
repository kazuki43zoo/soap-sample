package soap.ws.todo;

import soap.domain.model.Todo;

import javax.jws.WebMethod;
import javax.jws.WebService;

@WebService
public interface TodoWebService {

    Todo getTodo(String todoId);

    Todo create(Todo todo) throws ValidationException;

}
