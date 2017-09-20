/*   1:    */ package org.apache.poi.xssf.usermodel;
/*   2:    */ 
/*   3:    */ import java.util.ArrayList;
/*   4:    */ import java.util.Collections;
/*   5:    */ import org.apache.poi.ss.usermodel.ConditionalFormatting;
/*   6:    */ import org.apache.poi.ss.usermodel.ConditionalFormattingRule;
/*   7:    */ import org.apache.poi.ss.util.CellRangeAddress;
/*   8:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCfRule;
/*   9:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTConditionalFormatting;
/*  10:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTConditionalFormatting.Factory;
/*  11:    */ 
/*  12:    */ public class XSSFConditionalFormatting
/*  13:    */   implements ConditionalFormatting
/*  14:    */ {
/*  15:    */   private final CTConditionalFormatting _cf;
/*  16:    */   private final XSSFSheet _sh;
/*  17:    */   
/*  18:    */   XSSFConditionalFormatting(XSSFSheet sh)
/*  19:    */   {
/*  20: 38 */     this._cf = CTConditionalFormatting.Factory.newInstance();
/*  21: 39 */     this._sh = sh;
/*  22:    */   }
/*  23:    */   
/*  24:    */   XSSFConditionalFormatting(XSSFSheet sh, CTConditionalFormatting cf)
/*  25:    */   {
/*  26: 44 */     this._cf = cf;
/*  27: 45 */     this._sh = sh;
/*  28:    */   }
/*  29:    */   
/*  30:    */   CTConditionalFormatting getCTConditionalFormatting()
/*  31:    */   {
/*  32: 49 */     return this._cf;
/*  33:    */   }
/*  34:    */   
/*  35:    */   public CellRangeAddress[] getFormattingRanges()
/*  36:    */   {
/*  37: 57 */     ArrayList<CellRangeAddress> lst = new ArrayList();
/*  38: 58 */     for (Object stRef : this._cf.getSqref())
/*  39:    */     {
/*  40: 59 */       String[] regions = stRef.toString().split(" ");
/*  41: 60 */       for (String region : regions) {
/*  42: 61 */         lst.add(CellRangeAddress.valueOf(region));
/*  43:    */       }
/*  44:    */     }
/*  45: 64 */     return (CellRangeAddress[])lst.toArray(new CellRangeAddress[lst.size()]);
/*  46:    */   }
/*  47:    */   
/*  48:    */   public void setFormattingRanges(CellRangeAddress[] ranges)
/*  49:    */   {
/*  50: 69 */     if (ranges == null) {
/*  51: 70 */       throw new IllegalArgumentException("cellRanges must not be null");
/*  52:    */     }
/*  53: 72 */     StringBuilder sb = new StringBuilder();
/*  54: 73 */     boolean first = true;
/*  55: 74 */     for (CellRangeAddress range : ranges)
/*  56:    */     {
/*  57: 75 */       if (!first) {
/*  58: 76 */         sb.append(" ");
/*  59:    */       } else {
/*  60: 78 */         first = false;
/*  61:    */       }
/*  62: 80 */       sb.append(range.formatAsString());
/*  63:    */     }
/*  64: 82 */     this._cf.setSqref(Collections.singletonList(sb.toString()));
/*  65:    */   }
/*  66:    */   
/*  67:    */   public void setRule(int idx, ConditionalFormattingRule cfRule)
/*  68:    */   {
/*  69: 95 */     XSSFConditionalFormattingRule xRule = (XSSFConditionalFormattingRule)cfRule;
/*  70: 96 */     this._cf.getCfRuleArray(idx).set(xRule.getCTCfRule());
/*  71:    */   }
/*  72:    */   
/*  73:    */   public void addRule(ConditionalFormattingRule cfRule)
/*  74:    */   {
/*  75:107 */     XSSFConditionalFormattingRule xRule = (XSSFConditionalFormattingRule)cfRule;
/*  76:108 */     this._cf.addNewCfRule().set(xRule.getCTCfRule());
/*  77:    */   }
/*  78:    */   
/*  79:    */   public XSSFConditionalFormattingRule getRule(int idx)
/*  80:    */   {
/*  81:116 */     return new XSSFConditionalFormattingRule(this._sh, this._cf.getCfRuleArray(idx));
/*  82:    */   }
/*  83:    */   
/*  84:    */   public int getNumberOfRules()
/*  85:    */   {
/*  86:124 */     return this._cf.sizeOfCfRuleArray();
/*  87:    */   }
/*  88:    */   
/*  89:    */   public String toString()
/*  90:    */   {
/*  91:129 */     return this._cf.toString();
/*  92:    */   }
/*  93:    */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xssf.usermodel.XSSFConditionalFormatting
 * JD-Core Version:    0.7.0.1
 */