package org.apache.poi.poifs.filesystem;

public abstract interface BATManaged
{
  public abstract int countBlocks();
  
  public abstract void setStartBlock(int paramInt);
}


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.poifs.filesystem.BATManaged
 * JD-Core Version:    0.7.0.1
 */