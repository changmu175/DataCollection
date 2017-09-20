/*   1:    */ package org.apache.poi.xssf.usermodel.extensions;
/*   2:    */ 
/*   3:    */ import org.apache.poi.util.Internal;
/*   4:    */ import org.apache.poi.xssf.usermodel.helpers.HeaderFooterHelper;
/*   5:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTHeaderFooter;
/*   6:    */ 
/*   7:    */ public abstract class XSSFHeaderFooter
/*   8:    */   implements org.apache.poi.ss.usermodel.HeaderFooter
/*   9:    */ {
/*  10:    */   private HeaderFooterHelper helper;
/*  11:    */   private CTHeaderFooter headerFooter;
/*  12:127 */   private boolean stripFields = false;
/*  13:    */   
/*  14:    */   public XSSFHeaderFooter(CTHeaderFooter headerFooter)
/*  15:    */   {
/*  16:135 */     this.headerFooter = headerFooter;
/*  17:136 */     this.helper = new HeaderFooterHelper();
/*  18:    */   }
/*  19:    */   
/*  20:    */   @Internal
/*  21:    */   public CTHeaderFooter getHeaderFooter()
/*  22:    */   {
/*  23:146 */     return this.headerFooter;
/*  24:    */   }
/*  25:    */   
/*  26:    */   public String getValue()
/*  27:    */   {
/*  28:150 */     String value = getText();
/*  29:151 */     if (value == null) {
/*  30:152 */       return "";
/*  31:    */     }
/*  32:153 */     return value;
/*  33:    */   }
/*  34:    */   
/*  35:    */   public boolean areFieldsStripped()
/*  36:    */   {
/*  37:161 */     return this.stripFields;
/*  38:    */   }
/*  39:    */   
/*  40:    */   public void setAreFieldsStripped(boolean stripFields)
/*  41:    */   {
/*  42:171 */     this.stripFields = stripFields;
/*  43:    */   }
/*  44:    */   
/*  45:    */   public static String stripFields(String text)
/*  46:    */   {
/*  47:180 */     return org.apache.poi.hssf.usermodel.HeaderFooter.stripFields(text);
/*  48:    */   }
/*  49:    */   
/*  50:    */   public abstract String getText();
/*  51:    */   
/*  52:    */   protected abstract void setText(String paramString);
/*  53:    */   
/*  54:    */   public String getCenter()
/*  55:    */   {
/*  56:191 */     String text = this.helper.getCenterSection(getText());
/*  57:192 */     if (this.stripFields) {
/*  58:193 */       return stripFields(text);
/*  59:    */     }
/*  60:194 */     return text;
/*  61:    */   }
/*  62:    */   
/*  63:    */   public String getLeft()
/*  64:    */   {
/*  65:201 */     String text = this.helper.getLeftSection(getText());
/*  66:202 */     if (this.stripFields) {
/*  67:203 */       return stripFields(text);
/*  68:    */     }
/*  69:204 */     return text;
/*  70:    */   }
/*  71:    */   
/*  72:    */   public String getRight()
/*  73:    */   {
/*  74:211 */     String text = this.helper.getRightSection(getText());
/*  75:212 */     if (this.stripFields) {
/*  76:213 */       return stripFields(text);
/*  77:    */     }
/*  78:214 */     return text;
/*  79:    */   }
/*  80:    */   
/*  81:    */   public void setCenter(String newCenter)
/*  82:    */   {
/*  83:221 */     setText(this.helper.setCenterSection(getText(), newCenter));
/*  84:    */   }
/*  85:    */   
/*  86:    */   public void setLeft(String newLeft)
/*  87:    */   {
/*  88:228 */     setText(this.helper.setLeftSection(getText(), newLeft));
/*  89:    */   }
/*  90:    */   
/*  91:    */   public void setRight(String newRight)
/*  92:    */   {
/*  93:235 */     setText(this.helper.setRightSection(getText(), newRight));
/*  94:    */   }
/*  95:    */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xssf.usermodel.extensions.XSSFHeaderFooter
 * JD-Core Version:    0.7.0.1
 */