/*   1:    */ package org.apache.poi.hssf.record;
/*   2:    */ 
/*   3:    */ import java.util.Iterator;
/*   4:    */ import org.apache.poi.hssf.record.common.UnicodeString;
/*   5:    */ import org.apache.poi.hssf.record.cont.ContinuableRecord;
/*   6:    */ import org.apache.poi.hssf.record.cont.ContinuableRecordOutput;
/*   7:    */ import org.apache.poi.util.IntMapper;
/*   8:    */ 
/*   9:    */ public final class SSTRecord
/*  10:    */   extends ContinuableRecord
/*  11:    */ {
/*  12:    */   public static final short sid = 252;
/*  13: 41 */   private static final UnicodeString EMPTY_STRING = new UnicodeString("");
/*  14:    */   static final int STD_RECORD_OVERHEAD = 4;
/*  15:    */   static final int SST_RECORD_OVERHEAD = 12;
/*  16:    */   static final int MAX_DATA_SPACE = 8216;
/*  17:    */   private int field_1_num_strings;
/*  18:    */   private int field_2_num_unique_strings;
/*  19:    */   private IntMapper<UnicodeString> field_3_strings;
/*  20:    */   private SSTDeserializer deserializer;
/*  21:    */   int[] bucketAbsoluteOffsets;
/*  22:    */   int[] bucketRelativeOffsets;
/*  23:    */   
/*  24:    */   public SSTRecord()
/*  25:    */   {
/*  26: 69 */     this.field_1_num_strings = 0;
/*  27: 70 */     this.field_2_num_unique_strings = 0;
/*  28: 71 */     this.field_3_strings = new IntMapper();
/*  29: 72 */     this.deserializer = new SSTDeserializer(this.field_3_strings);
/*  30:    */   }
/*  31:    */   
/*  32:    */   public int addString(UnicodeString string)
/*  33:    */   {
/*  34: 84 */     this.field_1_num_strings += 1;
/*  35: 85 */     UnicodeString ucs = string == null ? EMPTY_STRING : string;
/*  36:    */     
/*  37:    */ 
/*  38: 88 */     int index = this.field_3_strings.getIndex(ucs);
/*  39:    */     int rval;
/*  40:    */     int rval;
/*  41: 90 */     if (index != -1)
/*  42:    */     {
/*  43: 91 */       rval = index;
/*  44:    */     }
/*  45:    */     else
/*  46:    */     {
/*  47: 95 */       rval = this.field_3_strings.size();
/*  48: 96 */       this.field_2_num_unique_strings += 1;
/*  49: 97 */       SSTDeserializer.addToStringTable(this.field_3_strings, ucs);
/*  50:    */     }
/*  51: 99 */     return rval;
/*  52:    */   }
/*  53:    */   
/*  54:    */   public int getNumStrings()
/*  55:    */   {
/*  56:107 */     return this.field_1_num_strings;
/*  57:    */   }
/*  58:    */   
/*  59:    */   public int getNumUniqueStrings()
/*  60:    */   {
/*  61:115 */     return this.field_2_num_unique_strings;
/*  62:    */   }
/*  63:    */   
/*  64:    */   public UnicodeString getString(int id)
/*  65:    */   {
/*  66:128 */     return (UnicodeString)this.field_3_strings.get(id);
/*  67:    */   }
/*  68:    */   
/*  69:    */   public String toString()
/*  70:    */   {
/*  71:138 */     StringBuffer buffer = new StringBuffer();
/*  72:    */     
/*  73:140 */     buffer.append("[SST]\n");
/*  74:141 */     buffer.append("    .numstrings     = ").append(Integer.toHexString(getNumStrings())).append("\n");
/*  75:    */     
/*  76:143 */     buffer.append("    .uniquestrings  = ").append(Integer.toHexString(getNumUniqueStrings())).append("\n");
/*  77:145 */     for (int k = 0; k < this.field_3_strings.size(); k++)
/*  78:    */     {
/*  79:147 */       UnicodeString s = (UnicodeString)this.field_3_strings.get(k);
/*  80:148 */       buffer.append("    .string_" + k + "      = ").append(s.getDebugInfo()).append("\n");
/*  81:    */     }
/*  82:151 */     buffer.append("[/SST]\n");
/*  83:152 */     return buffer.toString();
/*  84:    */   }
/*  85:    */   
/*  86:    */   public short getSid()
/*  87:    */   {
/*  88:156 */     return 252;
/*  89:    */   }
/*  90:    */   
/*  91:    */   public SSTRecord(RecordInputStream in)
/*  92:    */   {
/*  93:241 */     this.field_1_num_strings = in.readInt();
/*  94:242 */     this.field_2_num_unique_strings = in.readInt();
/*  95:243 */     this.field_3_strings = new IntMapper();
/*  96:    */     
/*  97:245 */     this.deserializer = new SSTDeserializer(this.field_3_strings);
/*  98:248 */     if (this.field_1_num_strings == 0)
/*  99:    */     {
/* 100:249 */       this.field_2_num_unique_strings = 0;
/* 101:250 */       return;
/* 102:    */     }
/* 103:252 */     this.deserializer.manufactureStrings(this.field_2_num_unique_strings, in);
/* 104:    */   }
/* 105:    */   
/* 106:    */   Iterator<UnicodeString> getStrings()
/* 107:    */   {
/* 108:262 */     return this.field_3_strings.iterator();
/* 109:    */   }
/* 110:    */   
/* 111:    */   int countStrings()
/* 112:    */   {
/* 113:269 */     return this.field_3_strings.size();
/* 114:    */   }
/* 115:    */   
/* 116:    */   protected void serialize(ContinuableRecordOutput out)
/* 117:    */   {
/* 118:273 */     SSTSerializer serializer = new SSTSerializer(this.field_3_strings, getNumStrings(), getNumUniqueStrings());
/* 119:274 */     serializer.serialize(out);
/* 120:275 */     this.bucketAbsoluteOffsets = serializer.getBucketAbsoluteOffsets();
/* 121:276 */     this.bucketRelativeOffsets = serializer.getBucketRelativeOffsets();
/* 122:    */   }
/* 123:    */   
/* 124:    */   SSTDeserializer getDeserializer()
/* 125:    */   {
/* 126:280 */     return this.deserializer;
/* 127:    */   }
/* 128:    */   
/* 129:    */   public ExtSSTRecord createExtSSTRecord(int sstOffset)
/* 130:    */   {
/* 131:297 */     if ((this.bucketAbsoluteOffsets == null) || (this.bucketRelativeOffsets == null)) {
/* 132:298 */       throw new IllegalStateException("SST record has not yet been serialized.");
/* 133:    */     }
/* 134:301 */     ExtSSTRecord extSST = new ExtSSTRecord();
/* 135:302 */     extSST.setNumStringsPerBucket((short)8);
/* 136:303 */     int[] absoluteOffsets = (int[])this.bucketAbsoluteOffsets.clone();
/* 137:304 */     int[] relativeOffsets = (int[])this.bucketRelativeOffsets.clone();
/* 138:305 */     for (int i = 0; i < absoluteOffsets.length; i++) {
/* 139:306 */       absoluteOffsets[i] += sstOffset;
/* 140:    */     }
/* 141:308 */     extSST.setBucketOffsets(absoluteOffsets, relativeOffsets);
/* 142:309 */     return extSST;
/* 143:    */   }
/* 144:    */   
/* 145:    */   public int calcExtSSTRecordSize()
/* 146:    */   {
/* 147:319 */     return ExtSSTRecord.getRecordSizeForStrings(this.field_3_strings.size());
/* 148:    */   }
/* 149:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.record.SSTRecord
 * JD-Core Version:    0.7.0.1
 */