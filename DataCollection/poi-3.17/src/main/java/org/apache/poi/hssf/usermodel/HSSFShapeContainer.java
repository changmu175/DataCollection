package org.apache.poi.hssf.usermodel;

import java.util.List;
import org.apache.poi.ss.usermodel.ShapeContainer;

public abstract interface HSSFShapeContainer
  extends ShapeContainer<HSSFShape>
{
  public abstract List<HSSFShape> getChildren();
  
  public abstract void addShape(HSSFShape paramHSSFShape);
  
  public abstract void setCoordinates(int paramInt1, int paramInt2, int paramInt3, int paramInt4);
  
  public abstract void clear();
  
  public abstract int getX1();
  
  public abstract int getY1();
  
  public abstract int getX2();
  
  public abstract int getY2();
  
  public abstract boolean removeShape(HSSFShape paramHSSFShape);
}


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.usermodel.HSSFShapeContainer
 * JD-Core Version:    0.7.0.1
 */