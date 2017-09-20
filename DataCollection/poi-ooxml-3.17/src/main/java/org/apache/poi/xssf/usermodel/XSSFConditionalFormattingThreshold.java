/*  1:   */ package org.apache.poi.xssf.usermodel;
/*  2:   */ 
/*  3:   */ import org.apache.poi.ss.usermodel.ConditionalFormattingThreshold;
/*  4:   */ import org.apache.poi.ss.usermodel.ConditionalFormattingThreshold.RangeType;
/*  5:   */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCfvo;
/*  6:   */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.STCfvoType;
/*  7:   */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.STCfvoType.Enum;
/*  8:   */ 
/*  9:   */ public class XSSFConditionalFormattingThreshold
/* 10:   */   implements ConditionalFormattingThreshold
/* 11:   */ {
/* 12:   */   private CTCfvo cfvo;
/* 13:   */   
/* 14:   */   protected XSSFConditionalFormattingThreshold(CTCfvo cfvo)
/* 15:   */   {
/* 16:33 */     this.cfvo = cfvo;
/* 17:   */   }
/* 18:   */   
/* 19:   */   protected CTCfvo getCTCfvo()
/* 20:   */   {
/* 21:37 */     return this.cfvo;
/* 22:   */   }
/* 23:   */   
/* 24:   */   public ConditionalFormattingThreshold.RangeType getRangeType()
/* 25:   */   {
/* 26:41 */     return ConditionalFormattingThreshold.RangeType.byName(this.cfvo.getType().toString());
/* 27:   */   }
/* 28:   */   
/* 29:   */   public void setRangeType(ConditionalFormattingThreshold.RangeType type)
/* 30:   */   {
/* 31:44 */     STCfvoType.Enum xtype = STCfvoType.Enum.forString(type.name);
/* 32:45 */     this.cfvo.setType(xtype);
/* 33:   */   }
/* 34:   */   
/* 35:   */   public String getFormula()
/* 36:   */   {
/* 37:49 */     if (this.cfvo.getType() == STCfvoType.FORMULA) {
/* 38:50 */       return this.cfvo.getVal();
/* 39:   */     }
/* 40:52 */     return null;
/* 41:   */   }
/* 42:   */   
/* 43:   */   public void setFormula(String formula)
/* 44:   */   {
/* 45:55 */     this.cfvo.setVal(formula);
/* 46:   */   }
/* 47:   */   
/* 48:   */   public Double getValue()
/* 49:   */   {
/* 50:59 */     if ((this.cfvo.getType() == STCfvoType.FORMULA) || (this.cfvo.getType() == STCfvoType.MIN) || (this.cfvo.getType() == STCfvoType.MAX)) {
/* 51:62 */       return null;
/* 52:   */     }
/* 53:64 */     if (this.cfvo.isSetVal()) {
/* 54:65 */       return Double.valueOf(Double.parseDouble(this.cfvo.getVal()));
/* 55:   */     }
/* 56:67 */     return null;
/* 57:   */   }
/* 58:   */   
/* 59:   */   public void setValue(Double value)
/* 60:   */   {
/* 61:71 */     if (value == null) {
/* 62:72 */       this.cfvo.unsetVal();
/* 63:   */     } else {
/* 64:74 */       this.cfvo.setVal(value.toString());
/* 65:   */     }
/* 66:   */   }
/* 67:   */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xssf.usermodel.XSSFConditionalFormattingThreshold
 * JD-Core Version:    0.7.0.1
 */