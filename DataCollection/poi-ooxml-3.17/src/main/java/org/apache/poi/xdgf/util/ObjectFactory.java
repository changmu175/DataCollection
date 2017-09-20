/*  1:   */ package org.apache.poi.xdgf.util;
/*  2:   */ 
/*  3:   */ import java.lang.reflect.Constructor;
/*  4:   */ import java.lang.reflect.InvocationTargetException;
/*  5:   */ import java.util.HashMap;
/*  6:   */ import java.util.Map;
/*  7:   */ import javax.xml.namespace.QName;
/*  8:   */ import org.apache.poi.POIXMLException;
/*  9:   */ import org.apache.xmlbeans.SchemaType;
/* 10:   */ import org.apache.xmlbeans.XmlObject;
/* 11:   */ 
/* 12:   */ public class ObjectFactory<T, X extends XmlObject>
/* 13:   */ {
/* 14:31 */   Map<String, Constructor<? extends T>> _types = new HashMap();
/* 15:   */   
/* 16:   */   public void put(String typeName, Class<? extends T> cls, Class<?>... varargs)
/* 17:   */     throws NoSuchMethodException, SecurityException
/* 18:   */   {
/* 19:34 */     this._types.put(typeName, cls.getDeclaredConstructor(varargs));
/* 20:   */   }
/* 21:   */   
/* 22:   */   public T load(String name, Object... varargs)
/* 23:   */   {
/* 24:38 */     Constructor<? extends T> constructor = (Constructor)this._types.get(name);
/* 25:39 */     if (constructor == null)
/* 26:   */     {
/* 27:42 */       X xmlObject = (XmlObject)varargs[0];
/* 28:   */       
/* 29:44 */       String typeName = xmlObject.schemaType().getName().getLocalPart();
/* 30:45 */       throw new POIXMLException("Invalid '" + typeName + "' name '" + name + "'");
/* 31:   */     }
/* 32:   */     try
/* 33:   */     {
/* 34:49 */       return constructor.newInstance(varargs);
/* 35:   */     }
/* 36:   */     catch (InvocationTargetException e)
/* 37:   */     {
/* 38:51 */       throw new POIXMLException(e.getCause());
/* 39:   */     }
/* 40:   */     catch (Exception e)
/* 41:   */     {
/* 42:53 */       throw new POIXMLException(e);
/* 43:   */     }
/* 44:   */   }
/* 45:   */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xdgf.util.ObjectFactory
 * JD-Core Version:    0.7.0.1
 */