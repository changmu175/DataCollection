/*  1:   */ package org.apache.poi.xssf.util;
/*  2:   */ 
/*  3:   */ import java.io.InputStream;
/*  4:   */ import org.apache.poi.util.Internal;
/*  5:   */ import org.apache.poi.util.Removal;
/*  6:   */ import org.apache.poi.util.ReplacingInputStream;
/*  7:   */ 
/*  8:   */ @Deprecated
/*  9:   */ @Removal(version="3.18")
/* 10:   */ @Internal
/* 11:   */ public class EvilUnclosedBRFixingInputStream
/* 12:   */   extends ReplacingInputStream
/* 13:   */ {
/* 14:   */   public EvilUnclosedBRFixingInputStream(InputStream source)
/* 15:   */   {
/* 16:44 */     super(source, "<br>", "<br/>");
/* 17:   */   }
/* 18:   */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xssf.util.EvilUnclosedBRFixingInputStream
 * JD-Core Version:    0.7.0.1
 */