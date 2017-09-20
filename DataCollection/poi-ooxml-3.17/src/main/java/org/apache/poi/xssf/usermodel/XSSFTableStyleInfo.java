/*  1:   */ package org.apache.poi.xssf.usermodel;
/*  2:   */ 
/*  3:   */ import org.apache.poi.ss.usermodel.TableStyle;
/*  4:   */ import org.apache.poi.ss.usermodel.TableStyleInfo;
/*  5:   */ import org.apache.poi.xssf.model.StylesTable;
/*  6:   */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTableStyleInfo;
/*  7:   */ 
/*  8:   */ public class XSSFTableStyleInfo
/*  9:   */   implements TableStyleInfo
/* 10:   */ {
/* 11:   */   private final CTTableStyleInfo styleInfo;
/* 12:   */   private final StylesTable stylesTable;
/* 13:   */   private TableStyle style;
/* 14:   */   private boolean columnStripes;
/* 15:   */   private boolean rowStripes;
/* 16:   */   private boolean firstColumn;
/* 17:   */   private boolean lastColumn;
/* 18:   */   
/* 19:   */   public XSSFTableStyleInfo(StylesTable stylesTable, CTTableStyleInfo tableStyleInfo)
/* 20:   */   {
/* 21:42 */     this.columnStripes = tableStyleInfo.getShowColumnStripes();
/* 22:43 */     this.rowStripes = tableStyleInfo.getShowRowStripes();
/* 23:44 */     this.firstColumn = tableStyleInfo.getShowFirstColumn();
/* 24:45 */     this.lastColumn = tableStyleInfo.getShowLastColumn();
/* 25:46 */     this.style = stylesTable.getTableStyle(tableStyleInfo.getName());
/* 26:47 */     this.stylesTable = stylesTable;
/* 27:48 */     this.styleInfo = tableStyleInfo;
/* 28:   */   }
/* 29:   */   
/* 30:   */   public boolean isShowColumnStripes()
/* 31:   */   {
/* 32:52 */     return this.columnStripes;
/* 33:   */   }
/* 34:   */   
/* 35:   */   public void setShowColumnStripes(boolean show)
/* 36:   */   {
/* 37:55 */     this.columnStripes = show;
/* 38:56 */     this.styleInfo.setShowColumnStripes(show);
/* 39:   */   }
/* 40:   */   
/* 41:   */   public boolean isShowRowStripes()
/* 42:   */   {
/* 43:60 */     return this.rowStripes;
/* 44:   */   }
/* 45:   */   
/* 46:   */   public void setShowRowStripes(boolean show)
/* 47:   */   {
/* 48:63 */     this.rowStripes = show;
/* 49:64 */     this.styleInfo.setShowRowStripes(show);
/* 50:   */   }
/* 51:   */   
/* 52:   */   public boolean isShowFirstColumn()
/* 53:   */   {
/* 54:68 */     return this.firstColumn;
/* 55:   */   }
/* 56:   */   
/* 57:   */   public void setFirstColumn(boolean showFirstColumn)
/* 58:   */   {
/* 59:71 */     this.firstColumn = showFirstColumn;
/* 60:72 */     this.styleInfo.setShowFirstColumn(showFirstColumn);
/* 61:   */   }
/* 62:   */   
/* 63:   */   public boolean isShowLastColumn()
/* 64:   */   {
/* 65:76 */     return this.lastColumn;
/* 66:   */   }
/* 67:   */   
/* 68:   */   public void setLastColumn(boolean showLastColumn)
/* 69:   */   {
/* 70:79 */     this.lastColumn = showLastColumn;
/* 71:80 */     this.styleInfo.setShowLastColumn(showLastColumn);
/* 72:   */   }
/* 73:   */   
/* 74:   */   public String getName()
/* 75:   */   {
/* 76:84 */     return this.style.getName();
/* 77:   */   }
/* 78:   */   
/* 79:   */   public void setName(String name)
/* 80:   */   {
/* 81:87 */     this.styleInfo.setName(name);
/* 82:88 */     this.style = this.stylesTable.getTableStyle(name);
/* 83:   */   }
/* 84:   */   
/* 85:   */   public TableStyle getStyle()
/* 86:   */   {
/* 87:92 */     return this.style;
/* 88:   */   }
/* 89:   */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xssf.usermodel.XSSFTableStyleInfo
 * JD-Core Version:    0.7.0.1
 */