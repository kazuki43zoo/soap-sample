package soap.ws.todo;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import soap.domain.model.Todo;
import soap.domain.service.todo.TodoService;

import javax.inject.Inject;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.validation.Valid;
import java.util.Date;

@Component
@WebService(serviceName = "TodoService", targetNamespace = "http://todo.service.domain.soap/")
public class TodoWebService implements TodoService {

    @Inject
    TodoService todoService;

    @WebMethod
    public Todo getTodo(String todoId) {
        return todoService.getTodo(todoId);
    }

    @WebMethod
    public Todo create(Todo todo) {
        return todoService.create(todo);
    }

}
