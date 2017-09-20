/*  1:   */ package org.apache.poi.xssf.usermodel;
/*  2:   */ 
/*  3:   */ import java.lang.reflect.Constructor;
/*  4:   */ import java.lang.reflect.InvocationTargetException;
/*  5:   */ import org.apache.poi.POIXMLDocumentPart;
/*  6:   */ import org.apache.poi.POIXMLFactory;
/*  7:   */ import org.apache.poi.POIXMLRelation;
/*  8:   */ 
/*  9:   */ public final class XSSFFactory
/* 10:   */   extends POIXMLFactory
/* 11:   */ {
/* 12:34 */   private static final XSSFFactory inst = new XSSFFactory();
/* 13:   */   
/* 14:   */   public static XSSFFactory getInstance()
/* 15:   */   {
/* 16:37 */     return inst;
/* 17:   */   }
/* 18:   */   
/* 19:   */   protected POIXMLRelation getDescriptor(String relationshipType)
/* 20:   */   {
/* 21:45 */     return XSSFRelation.getInstance(relationshipType);
/* 22:   */   }
/* 23:   */   
/* 24:   */   protected POIXMLDocumentPart createDocumentPart(Class<? extends POIXMLDocumentPart> cls, Class<?>[] classes, Object[] values)
/* 25:   */     throws SecurityException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException
/* 26:   */   {
/* 27:55 */     Constructor<? extends POIXMLDocumentPart> constructor = cls.getDeclaredConstructor(classes);
/* 28:56 */     return (POIXMLDocumentPart)constructor.newInstance(values);
/* 29:   */   }
/* 30:   */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xssf.usermodel.XSSFFactory
 * JD-Core Version:    0.7.0.1
 */