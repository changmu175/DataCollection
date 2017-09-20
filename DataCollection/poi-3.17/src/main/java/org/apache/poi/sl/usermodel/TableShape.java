package org.apache.poi.sl.usermodel;

public abstract interface TableShape<S extends Shape<S, P>, P extends TextParagraph<S, P, ?>>
  extends Shape<S, P>, PlaceableShape<S, P>
{
  public abstract int getNumberOfColumns();
  
  public abstract int getNumberOfRows();
  
  public abstract TableCell<S, P> getCell(int paramInt1, int paramInt2);
  
  public abstract double getColumnWidth(int paramInt);
  
  public abstract void setColumnWidth(int paramInt, double paramDouble);
  
  public abstract double getRowHeight(int paramInt);
  
  public abstract void setRowHeight(int paramInt, double paramDouble);
}


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.sl.usermodel.TableShape
 * JD-Core Version:    0.7.0.1
 */