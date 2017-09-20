package org.apache.poi.xslf.usermodel;

import org.apache.poi.sl.usermodel.PictureData;
import org.apache.poi.sl.usermodel.ShapeContainer;

public abstract interface XSLFShapeContainer
  extends ShapeContainer<XSLFShape, XSLFTextParagraph>
{
  public abstract XSLFAutoShape createAutoShape();
  
  public abstract XSLFFreeformShape createFreeform();
  
  public abstract XSLFTextBox createTextBox();
  
  public abstract XSLFConnectorShape createConnector();
  
  public abstract XSLFGroupShape createGroup();
  
  public abstract XSLFPictureShape createPicture(PictureData paramPictureData);
  
  public abstract void clear();
}


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xslf.usermodel.XSLFShapeContainer
 * JD-Core Version:    0.7.0.1
 */