package org.apache.poi.poifs.dev;

import java.util.Iterator;

public abstract interface POIFSViewable
{
  public abstract Object[] getViewableArray();
  
  public abstract Iterator<Object> getViewableIterator();
  
  public abstract boolean preferArray();
  
  public abstract String getShortDescription();
}


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.poifs.dev.POIFSViewable
 * JD-Core Version:    0.7.0.1
 */