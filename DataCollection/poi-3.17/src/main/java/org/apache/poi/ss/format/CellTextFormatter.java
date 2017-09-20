/*  1:   */ package org.apache.poi.ss.format;
/*  2:   */ 
/*  3:   */ import java.util.Locale;
/*  4:   */ import java.util.regex.Matcher;
/*  5:   */ 
/*  6:   */ public class CellTextFormatter
/*  7:   */   extends CellFormatter
/*  8:   */ {
/*  9:   */   private final int[] textPos;
/* 10:   */   private final String desc;
/* 11:33 */   static final CellFormatter SIMPLE_TEXT = new CellTextFormatter("@");
/* 12:   */   
/* 13:   */   public CellTextFormatter(String format)
/* 14:   */   {
/* 15:36 */     super(format);
/* 16:   */     
/* 17:38 */     final int[] numPlaces = new int[1];
/* 18:   */     
/* 19:40 */     this.desc = CellFormatPart.parseFormat(format, CellFormatType.TEXT, new CellFormatPart.PartHandler()
/* 20:   */     {
/* 21:   */       public String handlePart(Matcher m, String part, CellFormatType type, StringBuffer desc)
/* 22:   */       {
/* 23:44 */         if (part.equals("@"))
/* 24:   */         {
/* 25:45 */           numPlaces[0] += 1;
/* 26:46 */           return "";
/* 27:   */         }
/* 28:48 */         return null;
/* 29:   */       }
/* 30:52 */     }).toString();
/* 31:53 */     this.textPos = new int[numPlaces[0]];
/* 32:54 */     int pos = this.desc.length() - 1;
/* 33:55 */     for (int i = 0; i < this.textPos.length; i++)
/* 34:   */     {
/* 35:56 */       this.textPos[i] = this.desc.lastIndexOf("", pos);
/* 36:57 */       pos = this.textPos[i] - 1;
/* 37:   */     }
/* 38:   */   }
/* 39:   */   
/* 40:   */   public void formatValue(StringBuffer toAppendTo, Object obj)
/* 41:   */   {
/* 42:63 */     int start = toAppendTo.length();
/* 43:64 */     String text = obj.toString();
/* 44:65 */     if ((obj instanceof Boolean)) {
/* 45:66 */       text = text.toUpperCase(Locale.ROOT);
/* 46:   */     }
/* 47:68 */     toAppendTo.append(this.desc);
/* 48:69 */     for (int i = 0; i < this.textPos.length; i++)
/* 49:   */     {
/* 50:70 */       int pos = start + this.textPos[i];
/* 51:71 */       toAppendTo.replace(pos, pos + 1, text);
/* 52:   */     }
/* 53:   */   }
/* 54:   */   
/* 55:   */   public void simpleValue(StringBuffer toAppendTo, Object value)
/* 56:   */   {
/* 57:81 */     SIMPLE_TEXT.formatValue(toAppendTo, value);
/* 58:   */   }
/* 59:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.format.CellTextFormatter
 * JD-Core Version:    0.7.0.1
 */