package org.apache.poi.openxml4j.opc;

import java.util.Date;
import org.apache.poi.openxml4j.util.Nullable;

public abstract interface PackageProperties
{
  public static final String NAMESPACE_DCTERMS = "http://purl.org/dc/terms/";
  public static final String NAMESPACE_DC = "http://purl.org/dc/elements/1.1/";
  
  public abstract Nullable<String> getCategoryProperty();
  
  public abstract void setCategoryProperty(String paramString);
  
  public abstract Nullable<String> getContentStatusProperty();
  
  public abstract void setContentStatusProperty(String paramString);
  
  public abstract Nullable<String> getContentTypeProperty();
  
  public abstract void setContentTypeProperty(String paramString);
  
  public abstract Nullable<Date> getCreatedProperty();
  
  public abstract void setCreatedProperty(String paramString);
  
  public abstract void setCreatedProperty(Nullable<Date> paramNullable);
  
  public abstract Nullable<String> getCreatorProperty();
  
  public abstract void setCreatorProperty(String paramString);
  
  public abstract Nullable<String> getDescriptionProperty();
  
  public abstract void setDescriptionProperty(String paramString);
  
  public abstract Nullable<String> getIdentifierProperty();
  
  public abstract void setIdentifierProperty(String paramString);
  
  public abstract Nullable<String> getKeywordsProperty();
  
  public abstract void setKeywordsProperty(String paramString);
  
  public abstract Nullable<String> getLanguageProperty();
  
  public abstract void setLanguageProperty(String paramString);
  
  public abstract Nullable<String> getLastModifiedByProperty();
  
  public abstract void setLastModifiedByProperty(String paramString);
  
  public abstract Nullable<Date> getLastPrintedProperty();
  
  public abstract void setLastPrintedProperty(String paramString);
  
  public abstract void setLastPrintedProperty(Nullable<Date> paramNullable);
  
  public abstract Nullable<Date> getModifiedProperty();
  
  public abstract void setModifiedProperty(String paramString);
  
  public abstract void setModifiedProperty(Nullable<Date> paramNullable);
  
  public abstract Nullable<String> getRevisionProperty();
  
  public abstract void setRevisionProperty(String paramString);
  
  public abstract Nullable<String> getSubjectProperty();
  
  public abstract void setSubjectProperty(String paramString);
  
  public abstract Nullable<String> getTitleProperty();
  
  public abstract void setTitleProperty(String paramString);
  
  public abstract Nullable<String> getVersionProperty();
  
  public abstract void setVersionProperty(String paramString);
}


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.openxml4j.opc.PackageProperties
 * JD-Core Version:    0.7.0.1
 */