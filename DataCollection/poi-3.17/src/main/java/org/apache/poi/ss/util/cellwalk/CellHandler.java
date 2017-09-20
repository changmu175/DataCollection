package org.apache.poi.ss.util.cellwalk;

import org.apache.poi.ss.usermodel.Cell;

public abstract interface CellHandler
{
  public abstract void onCell(Cell paramCell, CellWalkContext paramCellWalkContext);
}


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.util.cellwalk.CellHandler
 * JD-Core Version:    0.7.0.1
 */