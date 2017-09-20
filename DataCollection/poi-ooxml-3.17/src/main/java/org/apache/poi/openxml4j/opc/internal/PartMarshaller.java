package org.apache.poi.openxml4j.opc.internal;

import java.io.OutputStream;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.openxml4j.opc.PackagePart;

public abstract interface PartMarshaller
{
  public abstract boolean marshall(PackagePart paramPackagePart, OutputStream paramOutputStream)
    throws OpenXML4JException;
}


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.openxml4j.opc.internal.PartMarshaller
 * JD-Core Version:    0.7.0.1
 */