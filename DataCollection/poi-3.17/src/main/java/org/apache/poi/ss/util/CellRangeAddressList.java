/*   1:    */ package org.apache.poi.ss.util;
/*   2:    */ 
/*   3:    */ import java.util.ArrayList;
/*   4:    */ import java.util.List;
/*   5:    */ import org.apache.poi.hssf.record.RecordInputStream;
/*   6:    */ import org.apache.poi.util.LittleEndianByteArrayOutputStream;
/*   7:    */ import org.apache.poi.util.LittleEndianOutput;
/*   8:    */ 
/*   9:    */ public class CellRangeAddressList
/*  10:    */ {
/*  11:    */   protected final List<CellRangeAddress> _list;
/*  12:    */   
/*  13:    */   public CellRangeAddressList()
/*  14:    */   {
/*  15: 48 */     this._list = new ArrayList();
/*  16:    */   }
/*  17:    */   
/*  18:    */   public CellRangeAddressList(int firstRow, int lastRow, int firstCol, int lastCol)
/*  19:    */   {
/*  20: 55 */     this();
/*  21: 56 */     addCellRangeAddress(firstRow, firstCol, lastRow, lastCol);
/*  22:    */   }
/*  23:    */   
/*  24:    */   public CellRangeAddressList(RecordInputStream in)
/*  25:    */   {
/*  26: 62 */     this();
/*  27: 63 */     int nItems = in.readUShort();
/*  28: 65 */     for (int k = 0; k < nItems; k++) {
/*  29: 66 */       this._list.add(new CellRangeAddress(in));
/*  30:    */     }
/*  31:    */   }
/*  32:    */   
/*  33:    */   public int countRanges()
/*  34:    */   {
/*  35: 78 */     return this._list.size();
/*  36:    */   }
/*  37:    */   
/*  38:    */   public void addCellRangeAddress(int firstRow, int firstCol, int lastRow, int lastCol)
/*  39:    */   {
/*  40: 90 */     CellRangeAddress region = new CellRangeAddress(firstRow, lastRow, firstCol, lastCol);
/*  41: 91 */     addCellRangeAddress(region);
/*  42:    */   }
/*  43:    */   
/*  44:    */   public void addCellRangeAddress(CellRangeAddress cra)
/*  45:    */   {
/*  46: 94 */     this._list.add(cra);
/*  47:    */   }
/*  48:    */   
/*  49:    */   public CellRangeAddress remove(int rangeIndex)
/*  50:    */   {
/*  51: 97 */     if (this._list.isEmpty()) {
/*  52: 98 */       throw new RuntimeException("List is empty");
/*  53:    */     }
/*  54:100 */     if ((rangeIndex < 0) || (rangeIndex >= this._list.size())) {
/*  55:101 */       throw new RuntimeException("Range index (" + rangeIndex + ") is outside allowable range (0.." + (this._list.size() - 1) + ")");
/*  56:    */     }
/*  57:104 */     return (CellRangeAddress)this._list.remove(rangeIndex);
/*  58:    */   }
/*  59:    */   
/*  60:    */   public CellRangeAddress getCellRangeAddress(int index)
/*  61:    */   {
/*  62:111 */     return (CellRangeAddress)this._list.get(index);
/*  63:    */   }
/*  64:    */   
/*  65:    */   public int getSize()
/*  66:    */   {
/*  67:115 */     return getEncodedSize(this._list.size());
/*  68:    */   }
/*  69:    */   
/*  70:    */   public static int getEncodedSize(int numberOfRanges)
/*  71:    */   {
/*  72:122 */     return 2 + CellRangeAddress.getEncodedSize(numberOfRanges);
/*  73:    */   }
/*  74:    */   
/*  75:    */   public int serialize(int offset, byte[] data)
/*  76:    */   {
/*  77:126 */     int totalSize = getSize();
/*  78:127 */     serialize(new LittleEndianByteArrayOutputStream(data, offset, totalSize));
/*  79:128 */     return totalSize;
/*  80:    */   }
/*  81:    */   
/*  82:    */   public void serialize(LittleEndianOutput out)
/*  83:    */   {
/*  84:131 */     int nItems = this._list.size();
/*  85:132 */     out.writeShort(nItems);
/*  86:133 */     for (int k = 0; k < nItems; k++)
/*  87:    */     {
/*  88:134 */       CellRangeAddress region = (CellRangeAddress)this._list.get(k);
/*  89:135 */       region.serialize(out);
/*  90:    */     }
/*  91:    */   }
/*  92:    */   
/*  93:    */   public CellRangeAddressList copy()
/*  94:    */   {
/*  95:141 */     CellRangeAddressList result = new CellRangeAddressList();
/*  96:    */     
/*  97:143 */     int nItems = this._list.size();
/*  98:144 */     for (int k = 0; k < nItems; k++)
/*  99:    */     {
/* 100:145 */       CellRangeAddress region = (CellRangeAddress)this._list.get(k);
/* 101:146 */       result.addCellRangeAddress(region.copy());
/* 102:    */     }
/* 103:148 */     return result;
/* 104:    */   }
/* 105:    */   
/* 106:    */   public CellRangeAddress[] getCellRangeAddresses()
/* 107:    */   {
/* 108:151 */     CellRangeAddress[] result = new CellRangeAddress[this._list.size()];
/* 109:152 */     this._list.toArray(result);
/* 110:153 */     return result;
/* 111:    */   }
/* 112:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.util.CellRangeAddressList
 * JD-Core Version:    0.7.0.1
 */