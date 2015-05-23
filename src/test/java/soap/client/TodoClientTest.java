package soap.client;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.remoting.jaxws.JaxWsPortProxyFactoryBean;
import org.springframework.remoting.jaxws.JaxWsSoapFaultException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import soap.domain.model.Todo;
import soap.domain.service.todo.TodoService;

import javax.inject.Inject;
import java.io.IOException;
import java.net.MalformedURLException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class TodoClientTest {

    @Inject
    TodoService todoService;

    @Test
    public void createAndGetTodo() {
        Todo todo = new Todo();
        todo.setTitle("test title");
        todo.setDescription("test description");
        Todo createdTodo = todoService.create(todo);
        System.out.println(createdTodo.getTodoId());
        System.out.println(createdTodo.getDescription());
        System.out.println(createdTodo.getCreatedAt());
        System.out.println(createdTodo.isFinished());
        System.out.println("-----");
        Todo loadedTodo = todoService.getTodo(createdTodo.getTodoId());
        System.out.println(loadedTodo.getTodoId());
        System.out.println(loadedTodo.getDescription());
        System.out.println(loadedTodo.getCreatedAt());
        System.out.println(loadedTodo.isFinished());
    }

    @Test
    public void validationErrorOnCreateTodo() {
        Todo todo = new Todo();
        todo.setDescription("test description");
        try {
            todoService.create(todo);
        } catch (JaxWsSoapFaultException e) {
            System.out.println(e.getFault().getFaultString());
        }
    }

    @Test
    public void getTodoSystemError() {
        try {
            todoService.getTodo("systemError");
        } catch (JaxWsSoapFaultException e) {
            System.out.println(e.getFault().getFaultString());
        }
    }


    @Configuration
    static class TestConfig {
        @Bean
        public JaxWsPortProxyFactoryBean todoService() throws IOException {
            JaxWsPortProxyFactoryBean factoryBean = new JaxWsPortProxyFactoryBean();
            factoryBean.setServiceInterface(TodoService.class);
            factoryBean.setWsdlDocumentResource(new UrlResource("http://localhost:8081/TodoService?WSDL"));
            factoryBean.setNamespaceUri("http://todo.service.domain.soap/");
            factoryBean.setServiceName("TodoService");
            factoryBean.setPortName("TodoWebServicePort");
            return factoryBean;
        }

    }

}
