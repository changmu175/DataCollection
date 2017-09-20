/*  1:   */ package org.apache.poi.xslf.usermodel;
/*  2:   */ 
/*  3:   */ import org.apache.poi.util.Removal;
/*  4:   */ import org.openxmlformats.schemas.drawingml.x2006.main.CTTextBody;
/*  5:   */ import org.openxmlformats.schemas.presentationml.x2006.main.CTPlaceholder;
/*  6:   */ import org.openxmlformats.schemas.presentationml.x2006.main.STPlaceholderType.Enum;
/*  7:   */ 
/*  8:   */ @Removal(version="3.18")
/*  9:   */ public class DrawingTextPlaceholder
/* 10:   */   extends DrawingTextBody
/* 11:   */ {
/* 12:   */   private final CTPlaceholder placeholder;
/* 13:   */   
/* 14:   */   public DrawingTextPlaceholder(CTTextBody textBody, CTPlaceholder placeholder)
/* 15:   */   {
/* 16:38 */     super(textBody);
/* 17:39 */     this.placeholder = placeholder;
/* 18:   */   }
/* 19:   */   
/* 20:   */   public String getPlaceholderType()
/* 21:   */   {
/* 22:46 */     return this.placeholder.getType().toString();
/* 23:   */   }
/* 24:   */   
/* 25:   */   public STPlaceholderType.Enum getPlaceholderTypeEnum()
/* 26:   */   {
/* 27:53 */     return this.placeholder.getType();
/* 28:   */   }
/* 29:   */   
/* 30:   */   public boolean isPlaceholderCustom()
/* 31:   */   {
/* 32:60 */     return this.placeholder.getHasCustomPrompt();
/* 33:   */   }
/* 34:   */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xslf.usermodel.DrawingTextPlaceholder
 * JD-Core Version:    0.7.0.1
 */