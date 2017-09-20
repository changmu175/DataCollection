package org.apache.poi.util;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface NotImplemented
{
  String value() default "";
}


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.util.NotImplemented
 * JD-Core Version:    0.7.0.1
 */