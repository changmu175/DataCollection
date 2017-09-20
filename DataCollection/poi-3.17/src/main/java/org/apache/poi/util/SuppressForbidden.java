package org.apache.poi.util;

import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.CLASS)
@Target({java.lang.annotation.ElementType.METHOD, java.lang.annotation.ElementType.TYPE})
public @interface SuppressForbidden
{
  String value() default "";
}


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.util.SuppressForbidden
 * JD-Core Version:    0.7.0.1
 */