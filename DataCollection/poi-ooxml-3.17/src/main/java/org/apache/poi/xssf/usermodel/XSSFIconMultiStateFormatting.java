/*  1:   */ package org.apache.poi.xssf.usermodel;
/*  2:   */ 
/*  3:   */ import org.apache.poi.ss.usermodel.ConditionalFormattingThreshold;
/*  4:   */ import org.apache.poi.ss.usermodel.IconMultiStateFormatting;
/*  5:   */ import org.apache.poi.ss.usermodel.IconMultiStateFormatting.IconSet;
/*  6:   */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCfvo;
/*  7:   */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTIconSet;
/*  8:   */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.STIconSetType.Enum;
/*  9:   */ 
/* 10:   */ public class XSSFIconMultiStateFormatting
/* 11:   */   implements IconMultiStateFormatting
/* 12:   */ {
/* 13:   */   CTIconSet _iconset;
/* 14:   */   
/* 15:   */   XSSFIconMultiStateFormatting(CTIconSet iconset)
/* 16:   */   {
/* 17:35 */     this._iconset = iconset;
/* 18:   */   }
/* 19:   */   
/* 20:   */   public IconMultiStateFormatting.IconSet getIconSet()
/* 21:   */   {
/* 22:39 */     String set = this._iconset.getIconSet().toString();
/* 23:40 */     return IconMultiStateFormatting.IconSet.byName(set);
/* 24:   */   }
/* 25:   */   
/* 26:   */   public void setIconSet(IconMultiStateFormatting.IconSet set)
/* 27:   */   {
/* 28:43 */     STIconSetType.Enum xIconSet = STIconSetType.Enum.forString(set.name);
/* 29:44 */     this._iconset.setIconSet(xIconSet);
/* 30:   */   }
/* 31:   */   
/* 32:   */   public boolean isIconOnly()
/* 33:   */   {
/* 34:48 */     if (this._iconset.isSetShowValue()) {
/* 35:49 */       return !this._iconset.getShowValue();
/* 36:   */     }
/* 37:50 */     return false;
/* 38:   */   }
/* 39:   */   
/* 40:   */   public void setIconOnly(boolean only)
/* 41:   */   {
/* 42:53 */     this._iconset.setShowValue(!only);
/* 43:   */   }
/* 44:   */   
/* 45:   */   public boolean isReversed()
/* 46:   */   {
/* 47:57 */     if (this._iconset.isSetReverse()) {
/* 48:58 */       return this._iconset.getReverse();
/* 49:   */     }
/* 50:59 */     return false;
/* 51:   */   }
/* 52:   */   
/* 53:   */   public void setReversed(boolean reversed)
/* 54:   */   {
/* 55:62 */     this._iconset.setReverse(reversed);
/* 56:   */   }
/* 57:   */   
/* 58:   */   public XSSFConditionalFormattingThreshold[] getThresholds()
/* 59:   */   {
/* 60:66 */     CTCfvo[] cfvos = this._iconset.getCfvoArray();
/* 61:67 */     XSSFConditionalFormattingThreshold[] t = new XSSFConditionalFormattingThreshold[cfvos.length];
/* 62:69 */     for (int i = 0; i < cfvos.length; i++) {
/* 63:70 */       t[i] = new XSSFConditionalFormattingThreshold(cfvos[i]);
/* 64:   */     }
/* 65:72 */     return t;
/* 66:   */   }
/* 67:   */   
/* 68:   */   public void setThresholds(ConditionalFormattingThreshold[] thresholds)
/* 69:   */   {
/* 70:75 */     CTCfvo[] cfvos = new CTCfvo[thresholds.length];
/* 71:76 */     for (int i = 0; i < thresholds.length; i++) {
/* 72:77 */       cfvos[i] = ((XSSFConditionalFormattingThreshold)thresholds[i]).getCTCfvo();
/* 73:   */     }
/* 74:79 */     this._iconset.setCfvoArray(cfvos);
/* 75:   */   }
/* 76:   */   
/* 77:   */   public XSSFConditionalFormattingThreshold createThreshold()
/* 78:   */   {
/* 79:82 */     return new XSSFConditionalFormattingThreshold(this._iconset.addNewCfvo());
/* 80:   */   }
/* 81:   */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xssf.usermodel.XSSFIconMultiStateFormatting
 * JD-Core Version:    0.7.0.1
 */