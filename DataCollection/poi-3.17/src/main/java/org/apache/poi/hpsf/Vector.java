/*  1:   */ package org.apache.poi.hpsf;
/*  2:   */ 
/*  3:   */ import java.util.ArrayList;
/*  4:   */ import java.util.List;
/*  5:   */ import org.apache.poi.util.Internal;
/*  6:   */ import org.apache.poi.util.LittleEndianByteArrayInputStream;
/*  7:   */ 
/*  8:   */ @Internal
/*  9:   */ class Vector
/* 10:   */ {
/* 11:   */   private final short _type;
/* 12:   */   private TypedPropertyValue[] _values;
/* 13:   */   
/* 14:   */   Vector(short type)
/* 15:   */   {
/* 16:35 */     this._type = type;
/* 17:   */   }
/* 18:   */   
/* 19:   */   void read(LittleEndianByteArrayInputStream lei)
/* 20:   */   {
/* 21:39 */     long longLength = lei.readUInt();
/* 22:41 */     if (longLength > 2147483647L) {
/* 23:42 */       throw new UnsupportedOperationException("Vector is too long -- " + longLength);
/* 24:   */     }
/* 25:44 */     int length = (int)longLength;
/* 26:   */     
/* 27:   */ 
/* 28:   */ 
/* 29:   */ 
/* 30:   */ 
/* 31:50 */     List<TypedPropertyValue> values = new ArrayList();
/* 32:51 */     int paddedType = this._type == 12 ? 0 : this._type;
/* 33:52 */     for (int i = 0; i < length; i++)
/* 34:   */     {
/* 35:53 */       TypedPropertyValue value = new TypedPropertyValue(paddedType, null);
/* 36:54 */       if (paddedType == 0) {
/* 37:55 */         value.read(lei);
/* 38:   */       } else {
/* 39:57 */         value.readValue(lei);
/* 40:   */       }
/* 41:59 */       values.add(value);
/* 42:   */     }
/* 43:61 */     this._values = ((TypedPropertyValue[])values.toArray(new TypedPropertyValue[values.size()]));
/* 44:   */   }
/* 45:   */   
/* 46:   */   TypedPropertyValue[] getValues()
/* 47:   */   {
/* 48:65 */     return this._values;
/* 49:   */   }
/* 50:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hpsf.Vector
 * JD-Core Version:    0.7.0.1
 */