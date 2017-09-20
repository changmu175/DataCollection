package org.apache.poi.openxml4j.opc.internal;

import java.io.IOException;
import java.io.InputStream;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.PackagePart;
import org.apache.poi.openxml4j.opc.internal.unmarshallers.UnmarshallContext;

public abstract interface PartUnmarshaller
{
  public abstract PackagePart unmarshall(UnmarshallContext paramUnmarshallContext, InputStream paramInputStream)
    throws InvalidFormatException, IOException;
}


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.openxml4j.opc.internal.PartUnmarshaller
 * JD-Core Version:    0.7.0.1
 */