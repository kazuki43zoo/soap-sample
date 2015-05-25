package soap.jaxws;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.SmartValidator;
import org.springframework.validation.Validator;

import javax.inject.Inject;
import javax.inject.Named;

@Component
public class WsValidationExecutor {

    @Inject
    @Named("validator")
    SmartValidator validator;

    @Inject
    MessageSource messageSource;

    public void execute(Object bean) throws BindException {
        execute(bean, (Class<?>[]) null, (Validator[]) null);
    }

    public void execute(Object bean, Validator... customValidators) throws BindException {
        execute(bean, (Class<?>[]) null, customValidators);
    }

    public void execute(Object bean, Class<?>... groups) throws BindException {
        execute(bean, (Class<?>[]) groups, (Validator[]) null);
    }

    public void execute(Object bean, Class<?>[] groups, Validator... customValidators) throws BindException {
        if (bean == null) {
            return;
        }
        BindException bindException = new BindException(bean, StringUtils.uncapitalize(bean.getClass().getSimpleName()));
        validator.validate(bean, bindException, (Object[]) groups);
        if (customValidators != null) {
            for (Validator customValidator : customValidators) {
                if (!customValidator.supports(bean.getClass())) {
                    continue;
                }
                if (customValidator instanceof SmartValidator) {
                    SmartValidator.class.cast(customValidator).validate(bean, bindException, (Object[]) groups);
                } else {
                    customValidator.validate(bean, bindException);
                }
            }
        }
        if (bindException.hasErrors()) {
            throw bindException;
        }

    }


}
