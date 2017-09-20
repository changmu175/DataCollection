package org.apache.poi.util;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface Internal
{
  String value() default "";
  
  String since() default "";
}


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.util.Internal
 * JD-Core Version:    0.7.0.1
 */