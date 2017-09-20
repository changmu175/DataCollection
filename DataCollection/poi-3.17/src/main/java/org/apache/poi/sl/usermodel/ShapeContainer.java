package org.apache.poi.sl.usermodel;

import java.util.List;

public abstract interface ShapeContainer<S extends Shape<S, P>, P extends TextParagraph<S, P, ?>>
  extends Iterable<S>
{
  public abstract List<S> getShapes();
  
  public abstract void addShape(S paramS);
  
  public abstract boolean removeShape(S paramS);
  
  public abstract AutoShape<S, P> createAutoShape();
  
  public abstract FreeformShape<S, P> createFreeform();
  
  public abstract TextBox<S, P> createTextBox();
  
  public abstract ConnectorShape<S, P> createConnector();
  
  public abstract GroupShape<S, P> createGroup();
  
  public abstract PictureShape<S, P> createPicture(PictureData paramPictureData);
  
  public abstract TableShape<S, P> createTable(int paramInt1, int paramInt2);
}


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.sl.usermodel.ShapeContainer
 * JD-Core Version:    0.7.0.1
 */