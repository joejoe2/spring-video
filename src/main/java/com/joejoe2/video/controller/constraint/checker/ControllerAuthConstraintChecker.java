package com.joejoe2.video.controller.constraint.checker;

import com.joejoe2.video.controller.constraint.auth.AuthenticatedApi;
import com.joejoe2.video.exception.ControllerConstraintViolation;
import com.joejoe2.video.utils.AuthUtil;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import org.springframework.stereotype.Component;

@Component
public class ControllerAuthConstraintChecker {
  public void checkWithMethod(Method method) throws ControllerConstraintViolation {
    checkAuthenticatedApiConstraint(method);
  }

  private static void checkAuthenticatedApiConstraint(Method method)
      throws ControllerConstraintViolation {
    AuthenticatedApi constraint = method.getAnnotation(AuthenticatedApi.class);
    if (constraint != null) {
      if (!AuthUtil.isAuthenticated())
        throw new ControllerConstraintViolation(
            constraint.rejectStatus(), constraint.rejectMessage());
    }

    for (Annotation annotation : method.getAnnotations()) {
      constraint = annotation.annotationType().getAnnotation(AuthenticatedApi.class);
      if (constraint != null) {
        if (!AuthUtil.isAuthenticated())
          throw new ControllerConstraintViolation(
              constraint.rejectStatus(), constraint.rejectMessage());
        else break;
      }
    }
  }
}
