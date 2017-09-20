/*  1:   */ package org.apache.poi.xdgf.usermodel;
/*  2:   */ 
/*  3:   */ import java.lang.reflect.Constructor;
/*  4:   */ import java.lang.reflect.InvocationTargetException;
/*  5:   */ import org.apache.poi.POIXMLDocumentPart;
/*  6:   */ import org.apache.poi.POIXMLFactory;
/*  7:   */ import org.apache.poi.POIXMLRelation;
/*  8:   */ 
/*  9:   */ public class XDGFFactory
/* 10:   */   extends POIXMLFactory
/* 11:   */ {
/* 12:   */   private final XDGFDocument document;
/* 13:   */   
/* 14:   */   public XDGFFactory(XDGFDocument document)
/* 15:   */   {
/* 16:35 */     this.document = document;
/* 17:   */   }
/* 18:   */   
/* 19:   */   protected POIXMLRelation getDescriptor(String relationshipType)
/* 20:   */   {
/* 21:42 */     return XDGFRelation.getInstance(relationshipType);
/* 22:   */   }
/* 23:   */   
/* 24:   */   protected POIXMLDocumentPart createDocumentPart(Class<? extends POIXMLDocumentPart> cls, Class<?>[] classes, Object[] values)
/* 25:   */     throws SecurityException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException
/* 26:   */   {
/* 27:   */     Object[] vals;
/* 28:   */     Class<?>[] cl;
/* 29:   */     Object[] vals;
/* 30:54 */     if (classes == null)
/* 31:   */     {
/* 32:55 */       Class<?>[] cl = { XDGFDocument.class };
/* 33:56 */       vals = new Object[] { this.document };
/* 34:   */     }
/* 35:   */     else
/* 36:   */     {
/* 37:58 */       cl = new Class[classes.length + 1];
/* 38:59 */       System.arraycopy(classes, 0, cl, 0, classes.length);
/* 39:60 */       cl[classes.length] = XDGFDocument.class;
/* 40:61 */       vals = new Object[values.length + 1];
/* 41:62 */       System.arraycopy(values, 0, vals, 0, values.length);
/* 42:63 */       vals[values.length] = this.document;
/* 43:   */     }
/* 44:66 */     Constructor<? extends POIXMLDocumentPart> constructor = cls.getDeclaredConstructor(cl);
/* 45:67 */     return (POIXMLDocumentPart)constructor.newInstance(vals);
/* 46:   */   }
/* 47:   */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xdgf.usermodel.XDGFFactory
 * JD-Core Version:    0.7.0.1
 */