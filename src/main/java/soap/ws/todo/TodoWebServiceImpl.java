package soap.ws.todo;

import org.springframework.stereotype.Component;
import soap.domain.model.Todo;
import soap.domain.service.todo.TodoService;

import javax.inject.Inject;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.validation.ConstraintViolationException;

@Component
@WebService(serviceName = "TodoService", portName = "TodoServicePort")
public class TodoWebServiceImpl implements TodoWebService {

    @Inject
    TodoService todoService;

    @WebMethod
    public Todo getTodo(String todoId) {
        return todoService.getTodo(todoId);
    }

    @WebMethod
    public Todo create(Todo todo) throws ValidationException {
        try {
            return todoService.create(todo);
        } catch (ConstraintViolationException e) {
            throw new ValidationException(e.getMessage());
        }
    }

}
