/*  1:   */ package org.apache.poi.hpsf;
/*  2:   */ 
/*  3:   */ import org.apache.poi.util.Internal;
/*  4:   */ import org.apache.poi.util.LittleEndianByteArrayInputStream;
/*  5:   */ import org.apache.poi.util.POILogFactory;
/*  6:   */ import org.apache.poi.util.POILogger;
/*  7:   */ 
/*  8:   */ @Internal
/*  9:   */ class VariantBool
/* 10:   */ {
/* 11:26 */   private static final POILogger LOG = POILogFactory.getLogger(VariantBool.class);
/* 12:   */   static final int SIZE = 2;
/* 13:   */   private boolean _value;
/* 14:   */   
/* 15:   */   void read(LittleEndianByteArrayInputStream lei)
/* 16:   */   {
/* 17:35 */     short value = lei.readShort();
/* 18:36 */     switch (value)
/* 19:   */     {
/* 20:   */     case 0: 
/* 21:38 */       this._value = false;
/* 22:39 */       break;
/* 23:   */     case -1: 
/* 24:41 */       this._value = true;
/* 25:42 */       break;
/* 26:   */     default: 
/* 27:44 */       LOG.log(5, new Object[] { "VARIANT_BOOL value '" + value + "' is incorrect" });
/* 28:45 */       this._value = true;
/* 29:   */     }
/* 30:   */   }
/* 31:   */   
/* 32:   */   boolean getValue()
/* 33:   */   {
/* 34:51 */     return this._value;
/* 35:   */   }
/* 36:   */   
/* 37:   */   void setValue(boolean value)
/* 38:   */   {
/* 39:55 */     this._value = value;
/* 40:   */   }
/* 41:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hpsf.VariantBool
 * JD-Core Version:    0.7.0.1
 */