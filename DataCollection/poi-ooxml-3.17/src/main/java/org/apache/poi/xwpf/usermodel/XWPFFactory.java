/*  1:   */ package org.apache.poi.xwpf.usermodel;
/*  2:   */ 
/*  3:   */ import java.lang.reflect.Constructor;
/*  4:   */ import java.lang.reflect.InvocationTargetException;
/*  5:   */ import org.apache.poi.POIXMLDocumentPart;
/*  6:   */ import org.apache.poi.POIXMLFactory;
/*  7:   */ import org.apache.poi.POIXMLRelation;
/*  8:   */ 
/*  9:   */ public final class XWPFFactory
/* 10:   */   extends POIXMLFactory
/* 11:   */ {
/* 12:35 */   private static final XWPFFactory inst = new XWPFFactory();
/* 13:   */   
/* 14:   */   public static XWPFFactory getInstance()
/* 15:   */   {
/* 16:38 */     return inst;
/* 17:   */   }
/* 18:   */   
/* 19:   */   protected POIXMLRelation getDescriptor(String relationshipType)
/* 20:   */   {
/* 21:46 */     return XWPFRelation.getInstance(relationshipType);
/* 22:   */   }
/* 23:   */   
/* 24:   */   protected POIXMLDocumentPart createDocumentPart(Class<? extends POIXMLDocumentPart> cls, Class<?>[] classes, Object[] values)
/* 25:   */     throws SecurityException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException
/* 26:   */   {
/* 27:56 */     Constructor<? extends POIXMLDocumentPart> constructor = cls.getDeclaredConstructor(classes);
/* 28:57 */     return (POIXMLDocumentPart)constructor.newInstance(values);
/* 29:   */   }
/* 30:   */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xwpf.usermodel.XWPFFactory
 * JD-Core Version:    0.7.0.1
 */