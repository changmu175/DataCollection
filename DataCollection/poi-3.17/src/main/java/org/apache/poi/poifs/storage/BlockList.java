package org.apache.poi.poifs.storage;

import java.io.IOException;

public abstract interface BlockList
{
  public abstract void zap(int paramInt);
  
  public abstract ListManagedBlock remove(int paramInt)
    throws IOException;
  
  public abstract ListManagedBlock[] fetchBlocks(int paramInt1, int paramInt2)
    throws IOException;
  
  public abstract void setBAT(BlockAllocationTableReader paramBlockAllocationTableReader)
    throws IOException;
  
  public abstract int blockCount();
}


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.poifs.storage.BlockList
 * JD-Core Version:    0.7.0.1
 */