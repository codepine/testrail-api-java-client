/**
 *
 */
package com.cymbocha.apis.testrail.utils;

import com.fasterxml.jackson.databind.PropertyNamingStrategy.LowerCaseWithUnderscoresStrategy;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;

import java.lang.reflect.Type;

/**
 * @author kms
 */
public class JsonPropertyNamingStrategy extends
        LowerCaseWithUnderscoresStrategy {

    @Override
    public String nameForField(MapperConfig<?> config, AnnotatedField field,
                               String defaultName) {
        System.out.println(field + ", " + defaultName + ", "
                + super.nameForField(config, field, defaultName));
        return super.nameForField(config, field, defaultName);
    }

    @Override
    public String nameForGetterMethod(MapperConfig<?> config,
                                      AnnotatedMethod method, String defaultName) {
        System.out.println(method + ", " + defaultName + ", "
                + super.nameForGetterMethod(config, method, defaultName));
        return super.nameForGetterMethod(config, method, defaultName);
    }

    @Override
    public String nameForSetterMethod(MapperConfig<?> config,
                                      AnnotatedMethod method, String defaultName) {
        Type paramType = method.getParameter(0).getParameterType();
        if (method.getParameterCount() > 0
                && method.getParameter(0).getParameterType() == Boolean.TYPE) {
            System.out.println("match for " + defaultName);
        }
        System.out.println(method + ", " + defaultName + ", "
                + super.nameForGetterMethod(config, method, defaultName));
        return super.nameForSetterMethod(config, method, defaultName);
    }
}
