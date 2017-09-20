package org.apache.poi.poifs.property;

import java.io.IOException;
import java.util.Iterator;

public abstract interface Parent
  extends Child, Iterable<Property>
{
  public abstract Iterator<Property> getChildren();
  
  public abstract void addChild(Property paramProperty)
    throws IOException;
  
  public abstract void setPreviousChild(Child paramChild);
  
  public abstract void setNextChild(Child paramChild);
}


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.poifs.property.Parent
 * JD-Core Version:    0.7.0.1
 */