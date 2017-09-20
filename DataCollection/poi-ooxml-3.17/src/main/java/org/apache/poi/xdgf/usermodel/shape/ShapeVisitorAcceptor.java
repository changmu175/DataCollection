package org.apache.poi.xdgf.usermodel.shape;

import org.apache.poi.xdgf.usermodel.XDGFShape;

public abstract interface ShapeVisitorAcceptor
{
  public abstract boolean accept(XDGFShape paramXDGFShape);
}


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xdgf.usermodel.shape.ShapeVisitorAcceptor
 * JD-Core Version:    0.7.0.1
 */