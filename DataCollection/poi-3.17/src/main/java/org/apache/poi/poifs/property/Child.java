package org.apache.poi.poifs.property;

public abstract interface Child
{
  public abstract Child getNextChild();
  
  public abstract Child getPreviousChild();
  
  public abstract void setNextChild(Child paramChild);
  
  public abstract void setPreviousChild(Child paramChild);
}


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.poifs.property.Child
 * JD-Core Version:    0.7.0.1
 */