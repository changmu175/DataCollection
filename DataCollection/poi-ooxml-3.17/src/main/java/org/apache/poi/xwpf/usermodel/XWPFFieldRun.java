/*  1:   */ package org.apache.poi.xwpf.usermodel;
/*  2:   */ 
/*  3:   */ import org.apache.poi.util.Internal;
/*  4:   */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTR;
/*  5:   */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSimpleField;
/*  6:   */ 
/*  7:   */ public class XWPFFieldRun
/*  8:   */   extends XWPFRun
/*  9:   */ {
/* 10:   */   private CTSimpleField field;
/* 11:   */   
/* 12:   */   public XWPFFieldRun(CTSimpleField field, CTR run, IRunBody p)
/* 13:   */   {
/* 14:32 */     super(run, p);
/* 15:33 */     this.field = field;
/* 16:   */   }
/* 17:   */   
/* 18:   */   @Internal
/* 19:   */   public CTSimpleField getCTField()
/* 20:   */   {
/* 21:38 */     return this.field;
/* 22:   */   }
/* 23:   */   
/* 24:   */   public String getFieldInstruction()
/* 25:   */   {
/* 26:42 */     return this.field.getInstr();
/* 27:   */   }
/* 28:   */   
/* 29:   */   public void setFieldInstruction(String instruction)
/* 30:   */   {
/* 31:46 */     this.field.setInstr(instruction);
/* 32:   */   }
/* 33:   */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xwpf.usermodel.XWPFFieldRun
 * JD-Core Version:    0.7.0.1
 */