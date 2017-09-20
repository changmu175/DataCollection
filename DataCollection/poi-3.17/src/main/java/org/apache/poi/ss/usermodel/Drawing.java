package org.apache.poi.ss.usermodel;

public abstract interface Drawing<T extends Shape>
  extends ShapeContainer<T>
{
  public abstract Picture createPicture(ClientAnchor paramClientAnchor, int paramInt);
  
  public abstract Comment createCellComment(ClientAnchor paramClientAnchor);
  
  public abstract Chart createChart(ClientAnchor paramClientAnchor);
  
  public abstract ClientAnchor createAnchor(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8);
  
  public abstract ObjectData createObjectData(ClientAnchor paramClientAnchor, int paramInt1, int paramInt2);
}


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.usermodel.Drawing
 * JD-Core Version:    0.7.0.1
 */