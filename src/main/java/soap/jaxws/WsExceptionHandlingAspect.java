package soap.jaxws;

import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.terasoluna.gfw.common.exception.BusinessException;
import org.terasoluna.gfw.common.exception.ExceptionCodeResolver;
import org.terasoluna.gfw.common.exception.ExceptionLogger;
import org.terasoluna.gfw.common.exception.ResourceNotFoundException;
import org.terasoluna.gfw.common.message.ResultMessage;
import org.terasoluna.gfw.common.message.ResultMessages;

import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Path;
import java.util.Iterator;
import java.util.Locale;

@Component
@Aspect
public class WsExceptionHandlingAspect {

    @Inject
    MessageSource messageSource;

    @Inject
    ExceptionCodeResolver exceptionCodeResolver;

    @Inject
    ExceptionLogger exceptionLogger;

    @AfterThrowing(value = "@within(javax.jws.WebService)", throwing = "e")
    public void loggingException(Exception e) {
        exceptionLogger.log(e);
    }

    @AfterThrowing(value = "@within(javax.jws.WebService)", throwing = "e")
    public void translateToWsValidationException(ConstraintViolationException e) throws WsValidationException {
        WsValidationException validationException = new WsValidationException();
        for (ConstraintViolation<?> v : e.getConstraintViolations()) {
            Iterator<Path.Node> pathIt = v.getPropertyPath().iterator();
            pathIt.next();
            pathIt.next();
            validationException.addError(
                    v.getConstraintDescriptor().getAnnotation().annotationType().getSimpleName(),
                    v.getMessage(),
                    pathIt.next().toString());
        }
        throw validationException;
    }

    @AfterThrowing(value = "@within(javax.jws.WebService)", throwing = "e")
    public void translateToWsBusinessException(BusinessException e) throws WsBusinessException {
        WsBusinessException businessException = new WsBusinessException();
        addErrors(businessException, e.getResultMessages());
        throw businessException;
    }

    @AfterThrowing(value = "@within(javax.jws.WebService)", throwing = "e")
    public void translateToWsResourceNotFoundException(ResourceNotFoundException e) throws WsResourceNotFoundException {
        WsResourceNotFoundException resourceNotFoundException = new WsResourceNotFoundException();
        addErrors(resourceNotFoundException, e.getResultMessages());
        throw resourceNotFoundException;
    }

    private void addErrors(WsException e, ResultMessages resultMessages) {
        Locale locale = Locale.getDefault();
        for (ResultMessage message : resultMessages) {
            e.addError(message.getCode(),
                    messageSource.getMessage(
                            message.getCode(),
                            message.getArgs(),
                            message.getText(),
                            locale));
        }

    }

}
