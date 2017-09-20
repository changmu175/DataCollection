/*  1:   */ package org.apache.poi.xwpf.usermodel;
/*  2:   */ 
/*  3:   */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSdtCell;
/*  4:   */ 
/*  5:   */ public class XWPFSDTCell
/*  6:   */   extends AbstractXWPFSDT
/*  7:   */   implements ICell
/*  8:   */ {
/*  9:   */   private final XWPFSDTContentCell cellContent;
/* 10:   */   
/* 11:   */   public XWPFSDTCell(CTSdtCell sdtCell, XWPFTableRow xwpfTableRow, IBody part)
/* 12:   */   {
/* 13:34 */     super(sdtCell.getSdtPr(), part);
/* 14:35 */     this.cellContent = new XWPFSDTContentCell(sdtCell.getSdtContent(), xwpfTableRow, part);
/* 15:   */   }
/* 16:   */   
/* 17:   */   public ISDTContent getContent()
/* 18:   */   {
/* 19:40 */     return this.cellContent;
/* 20:   */   }
/* 21:   */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xwpf.usermodel.XWPFSDTCell
 * JD-Core Version:    0.7.0.1
 */