/*   1:    */ package org.apache.poi.xssf.usermodel.extensions;
/*   2:    */ 
/*   3:    */ import org.apache.poi.ss.usermodel.HorizontalAlignment;
/*   4:    */ import org.apache.poi.ss.usermodel.VerticalAlignment;
/*   5:    */ import org.apache.poi.util.Internal;
/*   6:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCellAlignment;
/*   7:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.STHorizontalAlignment;
/*   8:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.STHorizontalAlignment.Enum;
/*   9:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.STVerticalAlignment;
/*  10:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.STVerticalAlignment.Enum;
/*  11:    */ 
/*  12:    */ public class XSSFCellAlignment
/*  13:    */ {
/*  14:    */   private CTCellAlignment cellAlignement;
/*  15:    */   
/*  16:    */   public XSSFCellAlignment(CTCellAlignment cellAlignment)
/*  17:    */   {
/*  18: 39 */     this.cellAlignement = cellAlignment;
/*  19:    */   }
/*  20:    */   
/*  21:    */   public VerticalAlignment getVertical()
/*  22:    */   {
/*  23: 49 */     STVerticalAlignment.Enum align = this.cellAlignement.getVertical();
/*  24: 50 */     if (align == null) {
/*  25: 50 */       align = STVerticalAlignment.BOTTOM;
/*  26:    */     }
/*  27: 52 */     return VerticalAlignment.values()[(align.intValue() - 1)];
/*  28:    */   }
/*  29:    */   
/*  30:    */   public void setVertical(VerticalAlignment align)
/*  31:    */   {
/*  32: 62 */     this.cellAlignement.setVertical(STVerticalAlignment.Enum.forInt(align.ordinal() + 1));
/*  33:    */   }
/*  34:    */   
/*  35:    */   public HorizontalAlignment getHorizontal()
/*  36:    */   {
/*  37: 72 */     STHorizontalAlignment.Enum align = this.cellAlignement.getHorizontal();
/*  38: 73 */     if (align == null) {
/*  39: 73 */       align = STHorizontalAlignment.GENERAL;
/*  40:    */     }
/*  41: 75 */     return HorizontalAlignment.values()[(align.intValue() - 1)];
/*  42:    */   }
/*  43:    */   
/*  44:    */   public void setHorizontal(HorizontalAlignment align)
/*  45:    */   {
/*  46: 85 */     this.cellAlignement.setHorizontal(STHorizontalAlignment.Enum.forInt(align.ordinal() + 1));
/*  47:    */   }
/*  48:    */   
/*  49:    */   public long getIndent()
/*  50:    */   {
/*  51: 94 */     return this.cellAlignement.getIndent();
/*  52:    */   }
/*  53:    */   
/*  54:    */   public void setIndent(long indent)
/*  55:    */   {
/*  56:103 */     this.cellAlignement.setIndent(indent);
/*  57:    */   }
/*  58:    */   
/*  59:    */   public long getTextRotation()
/*  60:    */   {
/*  61:121 */     return this.cellAlignement.getTextRotation();
/*  62:    */   }
/*  63:    */   
/*  64:    */   public void setTextRotation(long rotation)
/*  65:    */   {
/*  66:144 */     if ((rotation < 0L) && (rotation >= -90L)) {
/*  67:145 */       rotation = 90L + -1L * rotation;
/*  68:    */     }
/*  69:147 */     this.cellAlignement.setTextRotation(rotation);
/*  70:    */   }
/*  71:    */   
/*  72:    */   public boolean getWrapText()
/*  73:    */   {
/*  74:156 */     return this.cellAlignement.getWrapText();
/*  75:    */   }
/*  76:    */   
/*  77:    */   public void setWrapText(boolean wrapped)
/*  78:    */   {
/*  79:165 */     this.cellAlignement.setWrapText(wrapped);
/*  80:    */   }
/*  81:    */   
/*  82:    */   public boolean getShrinkToFit()
/*  83:    */   {
/*  84:169 */     return this.cellAlignement.getShrinkToFit();
/*  85:    */   }
/*  86:    */   
/*  87:    */   public void setShrinkToFit(boolean shrink)
/*  88:    */   {
/*  89:173 */     this.cellAlignement.setShrinkToFit(shrink);
/*  90:    */   }
/*  91:    */   
/*  92:    */   @Internal
/*  93:    */   public CTCellAlignment getCTCellAlignment()
/*  94:    */   {
/*  95:181 */     return this.cellAlignement;
/*  96:    */   }
/*  97:    */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xssf.usermodel.extensions.XSSFCellAlignment
 * JD-Core Version:    0.7.0.1
 */