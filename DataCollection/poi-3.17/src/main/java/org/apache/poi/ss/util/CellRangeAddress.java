/*   1:    */ package org.apache.poi.ss.util;
/*   2:    */ 
/*   3:    */ import org.apache.poi.hssf.record.RecordInputStream;
/*   4:    */ import org.apache.poi.ss.formula.SheetNameFormatter;
/*   5:    */ import org.apache.poi.util.LittleEndianOutput;
/*   6:    */ 
/*   7:    */ public class CellRangeAddress
/*   8:    */   extends CellRangeAddressBase
/*   9:    */ {
/*  10:    */   public static final int ENCODED_SIZE = 8;
/*  11:    */   
/*  12:    */   public CellRangeAddress(int firstRow, int lastRow, int firstCol, int lastCol)
/*  13:    */   {
/*  14: 46 */     super(firstRow, lastRow, firstCol, lastCol);
/*  15: 48 */     if ((lastRow < firstRow) || (lastCol < firstCol)) {
/*  16: 49 */       throw new IllegalArgumentException("Invalid cell range, having lastRow < firstRow || lastCol < firstCol, had rows " + lastRow + " >= " + firstRow + " or cells " + lastCol + " >= " + firstCol);
/*  17:    */     }
/*  18:    */   }
/*  19:    */   
/*  20:    */   public void serialize(LittleEndianOutput out)
/*  21:    */   {
/*  22: 55 */     out.writeShort(getFirstRow());
/*  23: 56 */     out.writeShort(getLastRow());
/*  24: 57 */     out.writeShort(getFirstColumn());
/*  25: 58 */     out.writeShort(getLastColumn());
/*  26:    */   }
/*  27:    */   
/*  28:    */   public CellRangeAddress(RecordInputStream in)
/*  29:    */   {
/*  30: 62 */     super(readUShortAndCheck(in), in.readUShort(), in.readUShort(), in.readUShort());
/*  31:    */   }
/*  32:    */   
/*  33:    */   private static int readUShortAndCheck(RecordInputStream in)
/*  34:    */   {
/*  35: 66 */     if (in.remaining() < 8) {
/*  36: 68 */       throw new RuntimeException("Ran out of data reading CellRangeAddress");
/*  37:    */     }
/*  38: 70 */     return in.readUShort();
/*  39:    */   }
/*  40:    */   
/*  41:    */   public CellRangeAddress copy()
/*  42:    */   {
/*  43: 74 */     return new CellRangeAddress(getFirstRow(), getLastRow(), getFirstColumn(), getLastColumn());
/*  44:    */   }
/*  45:    */   
/*  46:    */   public static int getEncodedSize(int numberOfItems)
/*  47:    */   {
/*  48: 78 */     return numberOfItems * 8;
/*  49:    */   }
/*  50:    */   
/*  51:    */   public String formatAsString()
/*  52:    */   {
/*  53: 86 */     return formatAsString(null, false);
/*  54:    */   }
/*  55:    */   
/*  56:    */   public String formatAsString(String sheetName, boolean useAbsoluteAddress)
/*  57:    */   {
/*  58: 93 */     StringBuilder sb = new StringBuilder();
/*  59: 94 */     if (sheetName != null)
/*  60:    */     {
/*  61: 95 */       sb.append(SheetNameFormatter.format(sheetName));
/*  62: 96 */       sb.append("!");
/*  63:    */     }
/*  64: 98 */     CellReference cellRefFrom = new CellReference(getFirstRow(), getFirstColumn(), useAbsoluteAddress, useAbsoluteAddress);
/*  65:    */     
/*  66:100 */     CellReference cellRefTo = new CellReference(getLastRow(), getLastColumn(), useAbsoluteAddress, useAbsoluteAddress);
/*  67:    */     
/*  68:102 */     sb.append(cellRefFrom.formatAsString());
/*  69:107 */     if ((!cellRefFrom.equals(cellRefTo)) || (isFullColumnRange()) || (isFullRowRange()))
/*  70:    */     {
/*  71:109 */       sb.append(':');
/*  72:110 */       sb.append(cellRefTo.formatAsString());
/*  73:    */     }
/*  74:112 */     return sb.toString();
/*  75:    */   }
/*  76:    */   
/*  77:    */   public static CellRangeAddress valueOf(String ref)
/*  78:    */   {
/*  79:124 */     int sep = ref.indexOf(":");
/*  80:    */     CellReference b;
/*  81:    */     CellReference a;
/*  82:    */     CellReference b;
/*  83:127 */     if (sep == -1)
/*  84:    */     {
/*  85:128 */       CellReference a = new CellReference(ref);
/*  86:129 */       b = a;
/*  87:    */     }
/*  88:    */     else
/*  89:    */     {
/*  90:131 */       a = new CellReference(ref.substring(0, sep));
/*  91:132 */       b = new CellReference(ref.substring(sep + 1));
/*  92:    */     }
/*  93:134 */     return new CellRangeAddress(a.getRow(), b.getRow(), a.getCol(), b.getCol());
/*  94:    */   }
/*  95:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.util.CellRangeAddress
 * JD-Core Version:    0.7.0.1
 */