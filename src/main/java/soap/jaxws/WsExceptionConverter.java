package soap.jaxws;

import org.springframework.context.MessageSource;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.terasoluna.gfw.common.exception.BusinessException;
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
public class WsExceptionConverter {

    @Inject
    MessageSource messageSource;

    public WsValidationException toWsValidationException(BindException e) {
        Locale locale = Locale.getDefault();
        WsValidationException validationException = new WsValidationException();
        for (ObjectError error : e.getGlobalErrors()) {
            validationException.addError(error.getCode(),
                    messageSource.getMessage(
                            new DefaultMessageSourceResolvable(
                                    error.getCodes(),
                                    error.getArguments(),
                                    error.getDefaultMessage()),
                            locale), null);

        }
        for (FieldError error : e.getFieldErrors()) {
            validationException.addError(error.getCode(),
                    messageSource.getMessage(
                            new DefaultMessageSourceResolvable(
                                    error.getCodes(),
                                    error.getArguments(),
                                    error.getDefaultMessage()),
                            locale), error.getField());

        }
        return validationException;
    }

    public WsValidationException toWsValidationException(ConstraintViolationException e) {
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
        return validationException;
    }


    public WsBusinessException toWsBusinessException(BusinessException e) {
        WsBusinessException businessException = new WsBusinessException();
        addErrors(businessException, e.getResultMessages());
        return businessException;
    }

    public WsResourceNotFoundException toWsResourceNotFoundException(ResourceNotFoundException e) {
        WsResourceNotFoundException resourceNotFoundException = new WsResourceNotFoundException();
        addErrors(resourceNotFoundException, e.getResultMessages());
        return resourceNotFoundException;
    }

    private void addErrors(WsException e, ResultMessages resultMessages) {
        for (ResultMessage message : resultMessages) {
            e.addError(message.getCode(),
                    messageSource.getMessage(
                            message.getCode(),
                            message.getArgs(),
                            message.getText(),
                            Locale.getDefault()), null);
        }

    }

}
