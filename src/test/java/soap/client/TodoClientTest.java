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
    public void getTodo() {
        Todo todo = todoService.getTodo("test");
        System.out.println(todo.getTodoId());
        System.out.println(todo.getDescription());
        System.out.println(todo.getCreatedAt());
        System.out.println(todo.isFinished());
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
