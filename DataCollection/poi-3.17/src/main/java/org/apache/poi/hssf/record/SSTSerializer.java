/*  1:   */ package org.apache.poi.hssf.record;
/*  2:   */ 
/*  3:   */ import org.apache.poi.hssf.record.common.UnicodeString;
/*  4:   */ import org.apache.poi.hssf.record.cont.ContinuableRecordOutput;
/*  5:   */ import org.apache.poi.util.IntMapper;
/*  6:   */ 
/*  7:   */ final class SSTSerializer
/*  8:   */ {
/*  9:   */   private final int _numStrings;
/* 10:   */   private final int _numUniqueStrings;
/* 11:   */   private final IntMapper<UnicodeString> strings;
/* 12:   */   private final int[] bucketAbsoluteOffsets;
/* 13:   */   private final int[] bucketRelativeOffsets;
/* 14:   */   
/* 15:   */   public SSTSerializer(IntMapper<UnicodeString> strings, int numStrings, int numUniqueStrings)
/* 16:   */   {
/* 17:44 */     this.strings = strings;
/* 18:45 */     this._numStrings = numStrings;
/* 19:46 */     this._numUniqueStrings = numUniqueStrings;
/* 20:   */     
/* 21:48 */     int infoRecs = ExtSSTRecord.getNumberOfInfoRecsForStrings(strings.size());
/* 22:49 */     this.bucketAbsoluteOffsets = new int[infoRecs];
/* 23:50 */     this.bucketRelativeOffsets = new int[infoRecs];
/* 24:   */   }
/* 25:   */   
/* 26:   */   public void serialize(ContinuableRecordOutput out)
/* 27:   */   {
/* 28:54 */     out.writeInt(this._numStrings);
/* 29:55 */     out.writeInt(this._numUniqueStrings);
/* 30:57 */     for (int k = 0; k < this.strings.size(); k++)
/* 31:   */     {
/* 32:59 */       if (k % 8 == 0)
/* 33:   */       {
/* 34:61 */         int rOff = out.getTotalSize();
/* 35:62 */         int index = k / 8;
/* 36:63 */         if (index < 128)
/* 37:   */         {
/* 38:65 */           this.bucketAbsoluteOffsets[index] = rOff;
/* 39:66 */           this.bucketRelativeOffsets[index] = rOff;
/* 40:   */         }
/* 41:   */       }
/* 42:69 */       UnicodeString s = getUnicodeString(k);
/* 43:70 */       s.serialize(out);
/* 44:   */     }
/* 45:   */   }
/* 46:   */   
/* 47:   */   private UnicodeString getUnicodeString(int index)
/* 48:   */   {
/* 49:77 */     return getUnicodeString(this.strings, index);
/* 50:   */   }
/* 51:   */   
/* 52:   */   private static UnicodeString getUnicodeString(IntMapper<UnicodeString> strings, int index)
/* 53:   */   {
/* 54:82 */     return (UnicodeString)strings.get(index);
/* 55:   */   }
/* 56:   */   
/* 57:   */   public int[] getBucketAbsoluteOffsets()
/* 58:   */   {
/* 59:87 */     return this.bucketAbsoluteOffsets;
/* 60:   */   }
/* 61:   */   
/* 62:   */   public int[] getBucketRelativeOffsets()
/* 63:   */   {
/* 64:92 */     return this.bucketRelativeOffsets;
/* 65:   */   }
/* 66:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.record.SSTSerializer
 * JD-Core Version:    0.7.0.1
 */