/*   1:    */ package org.apache.poi.xwpf.usermodel;
/*   2:    */ 
/*   3:    */ import javax.xml.namespace.QName;
/*   4:    */ import org.apache.xmlbeans.XmlCursor;
/*   5:    */ import org.apache.xmlbeans.XmlCursor.TokenType;
/*   6:    */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSdtContentCell;
/*   7:    */ 
/*   8:    */ public class XWPFSDTContentCell
/*   9:    */   implements ISDTContent
/*  10:    */ {
/*  11: 43 */   private String text = "";
/*  12:    */   
/*  13:    */   public XWPFSDTContentCell(CTSdtContentCell sdtContentCell, XWPFTableRow xwpfTableRow, IBody part)
/*  14:    */   {
/*  15: 49 */     if (sdtContentCell == null) {
/*  16: 50 */       return;
/*  17:    */     }
/*  18: 52 */     StringBuilder sb = new StringBuilder();
/*  19: 53 */     XmlCursor cursor = sdtContentCell.newCursor();
/*  20:    */     
/*  21:    */ 
/*  22:    */ 
/*  23:    */ 
/*  24:    */ 
/*  25:    */ 
/*  26: 60 */     int tcCnt = 0;
/*  27:    */     
/*  28: 62 */     int iBodyCnt = 0;
/*  29: 63 */     int depth = 1;
/*  30: 65 */     while ((cursor.hasNextToken()) && (depth > 0))
/*  31:    */     {
/*  32: 66 */       XmlCursor.TokenType t = cursor.toNextToken();
/*  33: 67 */       if (t.isText())
/*  34:    */       {
/*  35: 68 */         sb.append(cursor.getTextValue());
/*  36:    */       }
/*  37: 69 */       else if (isStartToken(cursor, "tr"))
/*  38:    */       {
/*  39: 70 */         tcCnt = 0;
/*  40: 71 */         iBodyCnt = 0;
/*  41:    */       }
/*  42: 72 */       else if (isStartToken(cursor, "tc"))
/*  43:    */       {
/*  44: 73 */         if (tcCnt++ > 0) {
/*  45: 74 */           sb.append("\t");
/*  46:    */         }
/*  47: 76 */         iBodyCnt = 0;
/*  48:    */       }
/*  49: 77 */       else if ((isStartToken(cursor, "p")) || (isStartToken(cursor, "tbl")) || (isStartToken(cursor, "sdt")))
/*  50:    */       {
/*  51: 80 */         if (iBodyCnt > 0) {
/*  52: 81 */           sb.append("\n");
/*  53:    */         }
/*  54: 83 */         iBodyCnt++;
/*  55:    */       }
/*  56: 85 */       if (cursor.isStart()) {
/*  57: 86 */         depth++;
/*  58: 87 */       } else if (cursor.isEnd()) {
/*  59: 88 */         depth--;
/*  60:    */       }
/*  61:    */     }
/*  62: 91 */     this.text = sb.toString();
/*  63: 92 */     cursor.dispose();
/*  64:    */   }
/*  65:    */   
/*  66:    */   private boolean isStartToken(XmlCursor cursor, String string)
/*  67:    */   {
/*  68: 97 */     if (!cursor.isStart()) {
/*  69: 98 */       return false;
/*  70:    */     }
/*  71:100 */     QName qName = cursor.getName();
/*  72:101 */     if ((qName != null) && (qName.getLocalPart() != null) && (qName.getLocalPart().equals(string))) {
/*  73:103 */       return true;
/*  74:    */     }
/*  75:105 */     return false;
/*  76:    */   }
/*  77:    */   
/*  78:    */   public String getText()
/*  79:    */   {
/*  80:110 */     return this.text;
/*  81:    */   }
/*  82:    */   
/*  83:    */   public String toString()
/*  84:    */   {
/*  85:114 */     return getText();
/*  86:    */   }
/*  87:    */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xwpf.usermodel.XWPFSDTContentCell
 * JD-Core Version:    0.7.0.1
 */