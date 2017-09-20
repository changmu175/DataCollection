/*  1:   */ package org.apache.poi.xdgf.usermodel.section;
/*  2:   */ 
/*  3:   */ import com.microsoft.schemas.office.visio.x2012.main.CellType;
/*  4:   */ import com.microsoft.schemas.office.visio.x2012.main.RowType;
/*  5:   */ import com.microsoft.schemas.office.visio.x2012.main.SectionType;
/*  6:   */ import java.awt.Color;
/*  7:   */ import java.util.HashMap;
/*  8:   */ import java.util.Map;
/*  9:   */ import org.apache.poi.xdgf.usermodel.XDGFCell;
/* 10:   */ import org.apache.poi.xdgf.usermodel.XDGFSheet;
/* 11:   */ 
/* 12:   */ public class CharacterSection
/* 13:   */   extends XDGFSection
/* 14:   */ {
/* 15:33 */   Double _fontSize = null;
/* 16:34 */   Color _fontColor = null;
/* 17:36 */   Map<String, XDGFCell> _characterCells = new HashMap();
/* 18:   */   
/* 19:   */   public CharacterSection(SectionType section, XDGFSheet containingSheet)
/* 20:   */   {
/* 21:39 */     super(section, containingSheet);
/* 22:   */     
/* 23:   */ 
/* 24:42 */     RowType row = section.getRowArray(0);
/* 25:44 */     for (CellType cell : row.getCellArray()) {
/* 26:45 */       this._characterCells.put(cell.getN(), new XDGFCell(cell));
/* 27:   */     }
/* 28:48 */     this._fontSize = XDGFCell.maybeGetDouble(this._characterCells, "Size");
/* 29:   */     
/* 30:50 */     String tmpColor = XDGFCell.maybeGetString(this._characterCells, "Color");
/* 31:51 */     if (tmpColor != null) {
/* 32:52 */       this._fontColor = Color.decode(tmpColor);
/* 33:   */     }
/* 34:   */   }
/* 35:   */   
/* 36:   */   public Double getFontSize()
/* 37:   */   {
/* 38:56 */     return this._fontSize;
/* 39:   */   }
/* 40:   */   
/* 41:   */   public Color getFontColor()
/* 42:   */   {
/* 43:60 */     return this._fontColor;
/* 44:   */   }
/* 45:   */   
/* 46:   */   public void setupMaster(XDGFSection section) {}
/* 47:   */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xdgf.usermodel.section.CharacterSection
 * JD-Core Version:    0.7.0.1
 */