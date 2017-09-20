/*  1:   */ package org.apache.poi.openxml4j.opc.internal.marshallers;
/*  2:   */ 
/*  3:   */ import java.io.OutputStream;
/*  4:   */ import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
/*  5:   */ import org.apache.poi.openxml4j.opc.PackagePart;
/*  6:   */ import org.apache.poi.openxml4j.opc.internal.PartMarshaller;
/*  7:   */ 
/*  8:   */ public final class DefaultMarshaller
/*  9:   */   implements PartMarshaller
/* 10:   */ {
/* 11:   */   public boolean marshall(PackagePart part, OutputStream out)
/* 12:   */     throws OpenXML4JException
/* 13:   */   {
/* 14:43 */     return part.save(out);
/* 15:   */   }
/* 16:   */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.openxml4j.opc.internal.marshallers.DefaultMarshaller
 * JD-Core Version:    0.7.0.1
 */