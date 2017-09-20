/*   1:    */ package org.apache.poi.xssf.binary;
/*   2:    */ 
/*   3:    */ import java.io.IOException;
/*   4:    */ import java.io.InputStream;
/*   5:    */ import java.util.ArrayList;
/*   6:    */ import java.util.List;
/*   7:    */ import java.util.SortedMap;
/*   8:    */ import java.util.TreeMap;
/*   9:    */ import org.apache.poi.POIXMLException;
/*  10:    */ import org.apache.poi.ss.usermodel.BuiltinFormats;
/*  11:    */ import org.apache.poi.util.Internal;
/*  12:    */ 
/*  13:    */ @Internal
/*  14:    */ public class XSSFBStylesTable
/*  15:    */   extends XSSFBParser
/*  16:    */ {
/*  17: 40 */   private final SortedMap<Short, String> numberFormats = new TreeMap();
/*  18: 41 */   private final List<Short> styleIds = new ArrayList();
/*  19: 43 */   private boolean inCellXFS = false;
/*  20: 44 */   private boolean inFmts = false;
/*  21:    */   
/*  22:    */   public XSSFBStylesTable(InputStream is)
/*  23:    */     throws IOException
/*  24:    */   {
/*  25: 46 */     super(is);
/*  26: 47 */     parse();
/*  27:    */   }
/*  28:    */   
/*  29:    */   String getNumberFormatString(int idx)
/*  30:    */   {
/*  31: 51 */     short numberFormatIdx = getNumberFormatIndex(idx);
/*  32: 52 */     if (this.numberFormats.containsKey(Short.valueOf(numberFormatIdx))) {
/*  33: 53 */       return (String)this.numberFormats.get(Short.valueOf(numberFormatIdx));
/*  34:    */     }
/*  35: 56 */     return BuiltinFormats.getBuiltinFormat(numberFormatIdx);
/*  36:    */   }
/*  37:    */   
/*  38:    */   short getNumberFormatIndex(int idx)
/*  39:    */   {
/*  40: 60 */     return ((Short)this.styleIds.get(idx)).shortValue();
/*  41:    */   }
/*  42:    */   
/*  43:    */   public void handleRecord(int recordType, byte[] data)
/*  44:    */     throws XSSFBParseException
/*  45:    */   {
/*  46: 65 */     XSSFBRecordType type = XSSFBRecordType.lookup(recordType);
/*  47: 66 */     switch (1.$SwitchMap$org$apache$poi$xssf$binary$XSSFBRecordType[type.ordinal()])
/*  48:    */     {
/*  49:    */     case 1: 
/*  50: 68 */       this.inCellXFS = true;
/*  51: 69 */       break;
/*  52:    */     case 2: 
/*  53: 71 */       this.inCellXFS = false;
/*  54: 72 */       break;
/*  55:    */     case 3: 
/*  56: 74 */       if (this.inCellXFS) {
/*  57: 75 */         handleBrtXFInCellXF(data);
/*  58:    */       }
/*  59:    */       break;
/*  60:    */     case 4: 
/*  61: 79 */       this.inFmts = true;
/*  62: 80 */       break;
/*  63:    */     case 5: 
/*  64: 82 */       this.inFmts = false;
/*  65: 83 */       break;
/*  66:    */     case 6: 
/*  67: 85 */       if (this.inFmts) {
/*  68: 86 */         handleFormat(data);
/*  69:    */       }
/*  70:    */       break;
/*  71:    */     }
/*  72:    */   }
/*  73:    */   
/*  74:    */   private void handleFormat(byte[] data)
/*  75:    */   {
/*  76: 94 */     int ifmt = data[0] & 0xFF;
/*  77: 95 */     if (ifmt > 32767) {
/*  78: 96 */       throw new POIXMLException("Format id must be a short");
/*  79:    */     }
/*  80: 98 */     StringBuilder sb = new StringBuilder();
/*  81: 99 */     XSSFBUtils.readXLWideString(data, 2, sb);
/*  82:100 */     String fmt = sb.toString();
/*  83:101 */     this.numberFormats.put(Short.valueOf((short)ifmt), fmt);
/*  84:    */   }
/*  85:    */   
/*  86:    */   private void handleBrtXFInCellXF(byte[] data)
/*  87:    */   {
/*  88:105 */     int ifmtOffset = 2;
/*  89:    */     
/*  90:    */ 
/*  91:    */ 
/*  92:109 */     int ifmt = data[ifmtOffset] & 0xFF;
/*  93:110 */     this.styleIds.add(Short.valueOf((short)ifmt));
/*  94:    */   }
/*  95:    */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xssf.binary.XSSFBStylesTable
 * JD-Core Version:    0.7.0.1
 */