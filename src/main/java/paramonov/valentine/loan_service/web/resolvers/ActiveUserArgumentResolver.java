package paramonov.valentine.loan_service.web.resolvers;

import org.springframework.core.MethodParameter;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebArgumentResolver;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import paramonov.valentine.loan_service.db.entities.User;
import paramonov.valentine.loan_service.web.annotations.ActiveUser;

import java.lang.annotation.Annotation;

@Component("activeUserArgumentResolver")
public class ActiveUserArgumentResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        final Class<?> parameterType = methodParameter.getParameterType();

        return User.class.equals(parameterType);
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter,
        ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest,
        WebDataBinderFactory webDataBinderFactory) throws Exception {

        final Annotation[] annotations = methodParameter.getParameterAnnotations();
        for(Annotation annotation : annotations) {
            if(ActiveUser.class.isInstance(annotation)) {
                return getActiveUser(nativeWebRequest);
            }
        }

        return WebArgumentResolver.UNRESOLVED;
    }

    private User getActiveUser(NativeWebRequest request) {
        final Authentication userPrincipal = (Authentication) request.getUserPrincipal();
        if(userPrincipal == null) {
            return null;
        }

        return (User) userPrincipal.getPrincipal();
    }
}
