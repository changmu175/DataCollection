/*  1:   */ package org.apache.poi.xwpf.model;
/*  2:   */ 
/*  3:   */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.STOnOff;
/*  4:   */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.STOnOff.Enum;
/*  5:   */ 
/*  6:   */ public final class WMLHelper
/*  7:   */ {
/*  8:   */   public static boolean convertSTOnOffToBoolean(STOnOff.Enum value)
/*  9:   */   {
/* 10:24 */     if ((value == STOnOff.TRUE) || (value == STOnOff.ON) || (value == STOnOff.X_1)) {
/* 11:25 */       return true;
/* 12:   */     }
/* 13:27 */     return false;
/* 14:   */   }
/* 15:   */   
/* 16:   */   public static STOnOff.Enum convertBooleanToSTOnOff(boolean value)
/* 17:   */   {
/* 18:31 */     return value ? STOnOff.TRUE : STOnOff.FALSE;
/* 19:   */   }
/* 20:   */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xwpf.model.WMLHelper
 * JD-Core Version:    0.7.0.1
 */