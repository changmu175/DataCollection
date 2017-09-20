package org.apache.poi.util;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface Removal
{
  String version() default "";
}


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.util.Removal
 * JD-Core Version:    0.7.0.1
 */