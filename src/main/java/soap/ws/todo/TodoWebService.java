package soap.ws.todo;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import soap.domain.model.Todo;
import soap.domain.service.todo.TodoService;

import javax.inject.Inject;
import javax.jws.WebMethod;
import javax.jws.WebService;
import java.util.Date;

@Component
@WebService(serviceName = "TodoService")
public class TodoWebService implements TodoService {

    @Inject
    TodoService todoService;

    @WebMethod
    public Todo getTodo(String todoId) {
        return todoService.getTodo(todoId);
    }

}
