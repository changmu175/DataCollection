/*  1:   */ package org.apache.poi.xslf.model;
/*  2:   */ 
/*  3:   */ import org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties;
/*  4:   */ import org.openxmlformats.schemas.drawingml.x2006.main.CTTextParagraphProperties;
/*  5:   */ 
/*  6:   */ public abstract class CharacterPropertyFetcher<T>
/*  7:   */   extends ParagraphPropertyFetcher<T>
/*  8:   */ {
/*  9:   */   public CharacterPropertyFetcher(int level)
/* 10:   */   {
/* 11:27 */     super(level);
/* 12:   */   }
/* 13:   */   
/* 14:   */   public boolean fetch(CTTextParagraphProperties props)
/* 15:   */   {
/* 16:31 */     if ((props != null) && (props.isSetDefRPr())) {
/* 17:32 */       return fetch(props.getDefRPr());
/* 18:   */     }
/* 19:35 */     return false;
/* 20:   */   }
/* 21:   */   
/* 22:   */   public abstract boolean fetch(CTTextCharacterProperties paramCTTextCharacterProperties);
/* 23:   */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xslf.model.CharacterPropertyFetcher
 * JD-Core Version:    0.7.0.1
 */