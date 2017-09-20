package org.apache.poi.ss.usermodel;

import java.util.Iterator;

public abstract interface CellRange<C extends Cell>
  extends Iterable<C>
{
  public abstract int getWidth();
  
  public abstract int getHeight();
  
  public abstract int size();
  
  public abstract String getReferenceText();
  
  public abstract C getTopLeftCell();
  
  public abstract C getCell(int paramInt1, int paramInt2);
  
  public abstract C[] getFlattenedCells();
  
  public abstract C[][] getCells();
  
  public abstract Iterator<C> iterator();
}


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.usermodel.CellRange
 * JD-Core Version:    0.7.0.1
 */