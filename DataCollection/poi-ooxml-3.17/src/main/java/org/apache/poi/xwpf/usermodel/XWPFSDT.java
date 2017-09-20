/*  1:   */ package org.apache.poi.xwpf.usermodel;
/*  2:   */ 
/*  3:   */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSdtBlock;
/*  4:   */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSdtRun;
/*  5:   */ 
/*  6:   */ public class XWPFSDT
/*  7:   */   extends AbstractXWPFSDT
/*  8:   */   implements IBodyElement, IRunBody, ISDTContents, IRunElement
/*  9:   */ {
/* 10:   */   private final ISDTContent content;
/* 11:   */   
/* 12:   */   public XWPFSDT(CTSdtRun sdtRun, IBody part)
/* 13:   */   {
/* 14:33 */     super(sdtRun.getSdtPr(), part);
/* 15:34 */     this.content = new XWPFSDTContent(sdtRun.getSdtContent(), part, this);
/* 16:   */   }
/* 17:   */   
/* 18:   */   public XWPFSDT(CTSdtBlock block, IBody part)
/* 19:   */   {
/* 20:38 */     super(block.getSdtPr(), part);
/* 21:39 */     this.content = new XWPFSDTContent(block.getSdtContent(), part, this);
/* 22:   */   }
/* 23:   */   
/* 24:   */   public ISDTContent getContent()
/* 25:   */   {
/* 26:43 */     return this.content;
/* 27:   */   }
/* 28:   */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xwpf.usermodel.XWPFSDT
 * JD-Core Version:    0.7.0.1
 */