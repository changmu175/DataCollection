package org.apache.poi.xwpf.usermodel;

import org.apache.poi.POIXMLDocumentPart;

public abstract interface IBodyElement
{
  public abstract IBody getBody();
  
  public abstract POIXMLDocumentPart getPart();
  
  public abstract BodyType getPartType();
  
  public abstract BodyElementType getElementType();
}


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xwpf.usermodel.IBodyElement
 * JD-Core Version:    0.7.0.1
 */