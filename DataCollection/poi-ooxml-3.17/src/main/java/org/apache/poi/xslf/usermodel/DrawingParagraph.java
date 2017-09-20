/*  1:   */ package org.apache.poi.xslf.usermodel;
/*  2:   */ 
/*  3:   */ import org.apache.poi.util.Removal;
/*  4:   */ import org.apache.xmlbeans.XmlCursor;
/*  5:   */ import org.apache.xmlbeans.XmlObject;
/*  6:   */ import org.openxmlformats.schemas.drawingml.x2006.main.CTRegularTextRun;
/*  7:   */ import org.openxmlformats.schemas.drawingml.x2006.main.CTTextLineBreak;
/*  8:   */ import org.openxmlformats.schemas.drawingml.x2006.main.CTTextParagraph;
/*  9:   */ 
/* 10:   */ @Removal(version="3.18")
/* 11:   */ public class DrawingParagraph
/* 12:   */ {
/* 13:   */   private final CTTextParagraph p;
/* 14:   */   
/* 15:   */   public DrawingParagraph(CTTextParagraph p)
/* 16:   */   {
/* 17:35 */     this.p = p;
/* 18:   */   }
/* 19:   */   
/* 20:   */   public CharSequence getText()
/* 21:   */   {
/* 22:39 */     StringBuilder text = new StringBuilder();
/* 23:   */     
/* 24:41 */     XmlCursor c = this.p.newCursor();
/* 25:42 */     c.selectPath("./*");
/* 26:43 */     while (c.toNextSelection())
/* 27:   */     {
/* 28:44 */       XmlObject o = c.getObject();
/* 29:45 */       if ((o instanceof CTRegularTextRun))
/* 30:   */       {
/* 31:46 */         CTRegularTextRun txrun = (CTRegularTextRun)o;
/* 32:47 */         text.append(txrun.getT());
/* 33:   */       }
/* 34:48 */       else if ((o instanceof CTTextLineBreak))
/* 35:   */       {
/* 36:49 */         text.append('\n');
/* 37:   */       }
/* 38:   */     }
/* 39:53 */     c.dispose();
/* 40:   */     
/* 41:55 */     return text;
/* 42:   */   }
/* 43:   */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xslf.usermodel.DrawingParagraph
 * JD-Core Version:    0.7.0.1
 */