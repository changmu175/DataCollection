/*   1:    */ package org.apache.poi.hssf.record.aggregates;
/*   2:    */ 
/*   3:    */ import java.util.ArrayList;
/*   4:    */ import java.util.List;
/*   5:    */ import org.apache.poi.hssf.model.RecordStream;
/*   6:    */ import org.apache.poi.hssf.record.MergeCellsRecord;
/*   7:    */ import org.apache.poi.ss.util.CellRangeAddress;
/*   8:    */ import org.apache.poi.ss.util.CellRangeAddressList;
/*   9:    */ 
/*  10:    */ public final class MergedCellsTable
/*  11:    */   extends RecordAggregate
/*  12:    */ {
/*  13: 33 */   private static int MAX_MERGED_REGIONS = 1027;
/*  14:    */   private final List<CellRangeAddress> _mergedRegions;
/*  15:    */   
/*  16:    */   public MergedCellsTable()
/*  17:    */   {
/*  18: 41 */     this._mergedRegions = new ArrayList();
/*  19:    */   }
/*  20:    */   
/*  21:    */   public void read(RecordStream rs)
/*  22:    */   {
/*  23: 49 */     List<CellRangeAddress> temp = this._mergedRegions;
/*  24: 50 */     while (rs.peekNextClass() == MergeCellsRecord.class)
/*  25:    */     {
/*  26: 51 */       MergeCellsRecord mcr = (MergeCellsRecord)rs.getNext();
/*  27: 52 */       int nRegions = mcr.getNumAreas();
/*  28: 53 */       for (int i = 0; i < nRegions; i++)
/*  29:    */       {
/*  30: 54 */         CellRangeAddress cra = mcr.getAreaAt(i);
/*  31: 55 */         temp.add(cra);
/*  32:    */       }
/*  33:    */     }
/*  34:    */   }
/*  35:    */   
/*  36:    */   public int getRecordSize()
/*  37:    */   {
/*  38: 62 */     int nRegions = this._mergedRegions.size();
/*  39: 63 */     if (nRegions < 1) {
/*  40: 65 */       return 0;
/*  41:    */     }
/*  42: 67 */     int nMergedCellsRecords = nRegions / MAX_MERGED_REGIONS;
/*  43: 68 */     int nLeftoverMergedRegions = nRegions % MAX_MERGED_REGIONS;
/*  44:    */     
/*  45: 70 */     int result = nMergedCellsRecords * (4 + CellRangeAddressList.getEncodedSize(MAX_MERGED_REGIONS)) + 4 + CellRangeAddressList.getEncodedSize(nLeftoverMergedRegions);
/*  46:    */     
/*  47:    */ 
/*  48: 73 */     return result;
/*  49:    */   }
/*  50:    */   
/*  51:    */   public void visitContainedRecords(RecordVisitor rv)
/*  52:    */   {
/*  53: 77 */     int nRegions = this._mergedRegions.size();
/*  54: 78 */     if (nRegions < 1) {
/*  55: 80 */       return;
/*  56:    */     }
/*  57: 83 */     int nFullMergedCellsRecords = nRegions / MAX_MERGED_REGIONS;
/*  58: 84 */     int nLeftoverMergedRegions = nRegions % MAX_MERGED_REGIONS;
/*  59: 85 */     CellRangeAddress[] cras = new CellRangeAddress[nRegions];
/*  60: 86 */     this._mergedRegions.toArray(cras);
/*  61: 88 */     for (int i = 0; i < nFullMergedCellsRecords; i++)
/*  62:    */     {
/*  63: 89 */       int startIx = i * MAX_MERGED_REGIONS;
/*  64: 90 */       rv.visitRecord(new MergeCellsRecord(cras, startIx, MAX_MERGED_REGIONS));
/*  65:    */     }
/*  66: 92 */     if (nLeftoverMergedRegions > 0)
/*  67:    */     {
/*  68: 93 */       int startIx = nFullMergedCellsRecords * MAX_MERGED_REGIONS;
/*  69: 94 */       rv.visitRecord(new MergeCellsRecord(cras, startIx, nLeftoverMergedRegions));
/*  70:    */     }
/*  71:    */   }
/*  72:    */   
/*  73:    */   public void addRecords(MergeCellsRecord[] mcrs)
/*  74:    */   {
/*  75: 98 */     for (int i = 0; i < mcrs.length; i++) {
/*  76: 99 */       addMergeCellsRecord(mcrs[i]);
/*  77:    */     }
/*  78:    */   }
/*  79:    */   
/*  80:    */   private void addMergeCellsRecord(MergeCellsRecord mcr)
/*  81:    */   {
/*  82:104 */     int nRegions = mcr.getNumAreas();
/*  83:105 */     for (int i = 0; i < nRegions; i++)
/*  84:    */     {
/*  85:106 */       CellRangeAddress cra = mcr.getAreaAt(i);
/*  86:107 */       this._mergedRegions.add(cra);
/*  87:    */     }
/*  88:    */   }
/*  89:    */   
/*  90:    */   public CellRangeAddress get(int index)
/*  91:    */   {
/*  92:112 */     checkIndex(index);
/*  93:113 */     return (CellRangeAddress)this._mergedRegions.get(index);
/*  94:    */   }
/*  95:    */   
/*  96:    */   public void remove(int index)
/*  97:    */   {
/*  98:117 */     checkIndex(index);
/*  99:118 */     this._mergedRegions.remove(index);
/* 100:    */   }
/* 101:    */   
/* 102:    */   private void checkIndex(int index)
/* 103:    */   {
/* 104:122 */     if ((index < 0) || (index >= this._mergedRegions.size())) {
/* 105:123 */       throw new IllegalArgumentException("Specified CF index " + index + " is outside the allowable range (0.." + (this._mergedRegions.size() - 1) + ")");
/* 106:    */     }
/* 107:    */   }
/* 108:    */   
/* 109:    */   public void addArea(int rowFrom, int colFrom, int rowTo, int colTo)
/* 110:    */   {
/* 111:129 */     this._mergedRegions.add(new CellRangeAddress(rowFrom, rowTo, colFrom, colTo));
/* 112:    */   }
/* 113:    */   
/* 114:    */   public int getNumberOfMergedRegions()
/* 115:    */   {
/* 116:133 */     return this._mergedRegions.size();
/* 117:    */   }
/* 118:    */ }



/* Location:           F:\poi-3.17.jar

 * Qualified Name:     org.apache.poi.hssf.record.aggregates.MergedCellsTable

 * JD-Core Version:    0.7.0.1

 */