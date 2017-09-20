package org.apache.poi.ss.usermodel;

public abstract interface Shape
{
  public abstract String getShapeName();
  
  public abstract Shape getParent();
  
  public abstract ChildAnchor getAnchor();
  
  public abstract boolean isNoFill();
  
  public abstract void setNoFill(boolean paramBoolean);
  
  public abstract void setFillColor(int paramInt1, int paramInt2, int paramInt3);
  
  public abstract void setLineStyleColor(int paramInt1, int paramInt2, int paramInt3);
}


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.usermodel.Shape
 * JD-Core Version:    0.7.0.1
 */