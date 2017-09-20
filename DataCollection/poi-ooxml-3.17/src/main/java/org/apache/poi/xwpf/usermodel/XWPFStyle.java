/*   1:    */ package org.apache.poi.xwpf.usermodel;
/*   2:    */ 
/*   3:    */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString;
/*   4:    */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTStyle;
/*   5:    */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.STStyleType.Enum;
/*   6:    */ 
/*   7:    */ public class XWPFStyle
/*   8:    */ {
/*   9:    */   protected XWPFStyles styles;
/*  10:    */   private CTStyle ctStyle;
/*  11:    */   
/*  12:    */   public XWPFStyle(CTStyle style)
/*  13:    */   {
/*  14: 37 */     this(style, null);
/*  15:    */   }
/*  16:    */   
/*  17:    */   public XWPFStyle(CTStyle style, XWPFStyles styles)
/*  18:    */   {
/*  19: 47 */     this.ctStyle = style;
/*  20: 48 */     this.styles = styles;
/*  21:    */   }
/*  22:    */   
/*  23:    */   public String getStyleId()
/*  24:    */   {
/*  25: 57 */     return this.ctStyle.getStyleId();
/*  26:    */   }
/*  27:    */   
/*  28:    */   public void setStyleId(String styleId)
/*  29:    */   {
/*  30: 66 */     this.ctStyle.setStyleId(styleId);
/*  31:    */   }
/*  32:    */   
/*  33:    */   public STStyleType.Enum getType()
/*  34:    */   {
/*  35: 75 */     return this.ctStyle.getType();
/*  36:    */   }
/*  37:    */   
/*  38:    */   public void setType(STStyleType.Enum type)
/*  39:    */   {
/*  40: 84 */     this.ctStyle.setType(type);
/*  41:    */   }
/*  42:    */   
/*  43:    */   public void setStyle(CTStyle style)
/*  44:    */   {
/*  45: 93 */     this.ctStyle = style;
/*  46:    */   }
/*  47:    */   
/*  48:    */   public CTStyle getCTStyle()
/*  49:    */   {
/*  50:102 */     return this.ctStyle;
/*  51:    */   }
/*  52:    */   
/*  53:    */   public XWPFStyles getStyles()
/*  54:    */   {
/*  55:111 */     return this.styles;
/*  56:    */   }
/*  57:    */   
/*  58:    */   public String getBasisStyleID()
/*  59:    */   {
/*  60:115 */     if (this.ctStyle.getBasedOn() != null) {
/*  61:116 */       return this.ctStyle.getBasedOn().getVal();
/*  62:    */     }
/*  63:118 */     return null;
/*  64:    */   }
/*  65:    */   
/*  66:    */   public String getLinkStyleID()
/*  67:    */   {
/*  68:126 */     if (this.ctStyle.getLink() != null) {
/*  69:127 */       return this.ctStyle.getLink().getVal();
/*  70:    */     }
/*  71:129 */     return null;
/*  72:    */   }
/*  73:    */   
/*  74:    */   public String getNextStyleID()
/*  75:    */   {
/*  76:136 */     if (this.ctStyle.getNext() != null) {
/*  77:137 */       return this.ctStyle.getNext().getVal();
/*  78:    */     }
/*  79:139 */     return null;
/*  80:    */   }
/*  81:    */   
/*  82:    */   public String getName()
/*  83:    */   {
/*  84:143 */     if (this.ctStyle.isSetName()) {
/*  85:144 */       return this.ctStyle.getName().getVal();
/*  86:    */     }
/*  87:145 */     return null;
/*  88:    */   }
/*  89:    */   
/*  90:    */   public boolean hasSameName(XWPFStyle compStyle)
/*  91:    */   {
/*  92:154 */     CTStyle ctCompStyle = compStyle.getCTStyle();
/*  93:155 */     String name = ctCompStyle.getName().getVal();
/*  94:156 */     return name.equals(this.ctStyle.getName().getVal());
/*  95:    */   }
/*  96:    */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xwpf.usermodel.XWPFStyle
 * JD-Core Version:    0.7.0.1
 */