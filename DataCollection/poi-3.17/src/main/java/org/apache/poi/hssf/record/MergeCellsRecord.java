/*   1:    */ package org.apache.poi.hssf.record;
/*   2:    */ 
/*   3:    */ import org.apache.poi.ss.util.CellRangeAddress;
/*   4:    */ import org.apache.poi.ss.util.CellRangeAddressList;
/*   5:    */ import org.apache.poi.util.LittleEndianOutput;
/*   6:    */ 
/*   7:    */ public final class MergeCellsRecord
/*   8:    */   extends StandardRecord
/*   9:    */   implements Cloneable
/*  10:    */ {
/*  11:    */   public static final short sid = 229;
/*  12:    */   private final CellRangeAddress[] _regions;
/*  13:    */   private final int _startIndex;
/*  14:    */   private final int _numberOfRegions;
/*  15:    */   
/*  16:    */   public MergeCellsRecord(CellRangeAddress[] regions, int startIndex, int numberOfRegions)
/*  17:    */   {
/*  18: 37 */     this._regions = regions;
/*  19: 38 */     this._startIndex = startIndex;
/*  20: 39 */     this._numberOfRegions = numberOfRegions;
/*  21:    */   }
/*  22:    */   
/*  23:    */   public MergeCellsRecord(RecordInputStream in)
/*  24:    */   {
/*  25: 46 */     int nRegions = in.readUShort();
/*  26: 47 */     CellRangeAddress[] cras = new CellRangeAddress[nRegions];
/*  27: 48 */     for (int i = 0; i < nRegions; i++) {
/*  28: 49 */       cras[i] = new CellRangeAddress(in);
/*  29:    */     }
/*  30: 51 */     this._numberOfRegions = nRegions;
/*  31: 52 */     this._startIndex = 0;
/*  32: 53 */     this._regions = cras;
/*  33:    */   }
/*  34:    */   
/*  35:    */   public short getNumAreas()
/*  36:    */   {
/*  37: 61 */     return (short)this._numberOfRegions;
/*  38:    */   }
/*  39:    */   
/*  40:    */   public CellRangeAddress getAreaAt(int index)
/*  41:    */   {
/*  42: 70 */     return this._regions[(this._startIndex + index)];
/*  43:    */   }
/*  44:    */   
/*  45:    */   protected int getDataSize()
/*  46:    */   {
/*  47: 75 */     return CellRangeAddressList.getEncodedSize(this._numberOfRegions);
/*  48:    */   }
/*  49:    */   
/*  50:    */   public short getSid()
/*  51:    */   {
/*  52: 80 */     return 229;
/*  53:    */   }
/*  54:    */   
/*  55:    */   public void serialize(LittleEndianOutput out)
/*  56:    */   {
/*  57: 85 */     int nItems = this._numberOfRegions;
/*  58: 86 */     out.writeShort(nItems);
/*  59: 87 */     for (int i = 0; i < this._numberOfRegions; i++) {
/*  60: 88 */       this._regions[(this._startIndex + i)].serialize(out);
/*  61:    */     }
/*  62:    */   }
/*  63:    */   
/*  64:    */   public String toString()
/*  65:    */   {
/*  66: 94 */     StringBuffer retval = new StringBuffer();
/*  67:    */     
/*  68: 96 */     retval.append("[MERGEDCELLS]").append("\n");
/*  69: 97 */     retval.append("     .numregions =").append(getNumAreas()).append("\n");
/*  70: 98 */     for (int k = 0; k < this._numberOfRegions; k++)
/*  71:    */     {
/*  72: 99 */       CellRangeAddress r = this._regions[(this._startIndex + k)];
/*  73:    */       
/*  74:101 */       retval.append("     .rowfrom =").append(r.getFirstRow()).append("\n");
/*  75:102 */       retval.append("     .rowto   =").append(r.getLastRow()).append("\n");
/*  76:103 */       retval.append("     .colfrom =").append(r.getFirstColumn()).append("\n");
/*  77:104 */       retval.append("     .colto   =").append(r.getLastColumn()).append("\n");
/*  78:    */     }
/*  79:106 */     retval.append("[MERGEDCELLS]").append("\n");
/*  80:107 */     return retval.toString();
/*  81:    */   }
/*  82:    */   
/*  83:    */   public MergeCellsRecord clone()
/*  84:    */   {
/*  85:112 */     int nRegions = this._numberOfRegions;
/*  86:113 */     CellRangeAddress[] clonedRegions = new CellRangeAddress[nRegions];
/*  87:114 */     for (int i = 0; i < clonedRegions.length; i++) {
/*  88:115 */       clonedRegions[i] = this._regions[(this._startIndex + i)].copy();
/*  89:    */     }
/*  90:117 */     return new MergeCellsRecord(clonedRegions, 0, nRegions);
/*  91:    */   }
/*  92:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.record.MergeCellsRecord
 * JD-Core Version:    0.7.0.1
 */