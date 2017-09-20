/*   1:    */ package org.apache.poi.hssf.record;
/*   2:    */ 
/*   3:    */ import org.apache.poi.hssf.record.common.FeatFormulaErr2;
/*   4:    */ import org.apache.poi.hssf.record.common.FeatProtection;
/*   5:    */ import org.apache.poi.hssf.record.common.FeatSmartTag;
/*   6:    */ import org.apache.poi.hssf.record.common.FtrHeader;
/*   7:    */ import org.apache.poi.hssf.record.common.SharedFeature;
/*   8:    */ import org.apache.poi.ss.util.CellRangeAddress;
/*   9:    */ import org.apache.poi.util.LittleEndianOutput;
/*  10:    */ import org.apache.poi.util.POILogFactory;
/*  11:    */ import org.apache.poi.util.POILogger;
/*  12:    */ 
/*  13:    */ public final class FeatRecord
/*  14:    */   extends StandardRecord
/*  15:    */   implements Cloneable
/*  16:    */ {
/*  17: 37 */   private static POILogger logger = POILogFactory.getLogger(FeatRecord.class);
/*  18:    */   public static final short sid = 2152;
/*  19:    */   public static final short v11_sid = 2162;
/*  20:    */   public static final short v12_sid = 2168;
/*  21:    */   private FtrHeader futureHeader;
/*  22:    */   private int isf_sharedFeatureType;
/*  23:    */   private byte reserved1;
/*  24:    */   private long reserved2;
/*  25:    */   private long cbFeatData;
/*  26:    */   private int reserved3;
/*  27:    */   private CellRangeAddress[] cellRefs;
/*  28:    */   private SharedFeature sharedFeature;
/*  29:    */   
/*  30:    */   public FeatRecord()
/*  31:    */   {
/*  32: 65 */     this.futureHeader = new FtrHeader();
/*  33: 66 */     this.futureHeader.setRecordType((short)2152);
/*  34:    */   }
/*  35:    */   
/*  36:    */   public short getSid()
/*  37:    */   {
/*  38: 70 */     return 2152;
/*  39:    */   }
/*  40:    */   
/*  41:    */   public FeatRecord(RecordInputStream in)
/*  42:    */   {
/*  43: 74 */     this.futureHeader = new FtrHeader(in);
/*  44:    */     
/*  45: 76 */     this.isf_sharedFeatureType = in.readShort();
/*  46: 77 */     this.reserved1 = in.readByte();
/*  47: 78 */     this.reserved2 = in.readInt();
/*  48: 79 */     int cref = in.readUShort();
/*  49: 80 */     this.cbFeatData = in.readInt();
/*  50: 81 */     this.reserved3 = in.readShort();
/*  51:    */     
/*  52: 83 */     this.cellRefs = new CellRangeAddress[cref];
/*  53: 84 */     for (int i = 0; i < this.cellRefs.length; i++) {
/*  54: 85 */       this.cellRefs[i] = new CellRangeAddress(in);
/*  55:    */     }
/*  56: 88 */     switch (this.isf_sharedFeatureType)
/*  57:    */     {
/*  58:    */     case 2: 
/*  59: 90 */       this.sharedFeature = new FeatProtection(in);
/*  60: 91 */       break;
/*  61:    */     case 3: 
/*  62: 93 */       this.sharedFeature = new FeatFormulaErr2(in);
/*  63: 94 */       break;
/*  64:    */     case 4: 
/*  65: 96 */       this.sharedFeature = new FeatSmartTag(in);
/*  66: 97 */       break;
/*  67:    */     default: 
/*  68: 99 */       logger.log(7, new Object[] { "Unknown Shared Feature " + this.isf_sharedFeatureType + " found!" });
/*  69:    */     }
/*  70:    */   }
/*  71:    */   
/*  72:    */   public String toString()
/*  73:    */   {
/*  74:104 */     StringBuffer buffer = new StringBuffer();
/*  75:105 */     buffer.append("[SHARED FEATURE]\n");
/*  76:    */     
/*  77:    */ 
/*  78:    */ 
/*  79:109 */     buffer.append("[/SHARED FEATURE]\n");
/*  80:110 */     return buffer.toString();
/*  81:    */   }
/*  82:    */   
/*  83:    */   public void serialize(LittleEndianOutput out)
/*  84:    */   {
/*  85:114 */     this.futureHeader.serialize(out);
/*  86:    */     
/*  87:116 */     out.writeShort(this.isf_sharedFeatureType);
/*  88:117 */     out.writeByte(this.reserved1);
/*  89:118 */     out.writeInt((int)this.reserved2);
/*  90:119 */     out.writeShort(this.cellRefs.length);
/*  91:120 */     out.writeInt((int)this.cbFeatData);
/*  92:121 */     out.writeShort(this.reserved3);
/*  93:123 */     for (int i = 0; i < this.cellRefs.length; i++) {
/*  94:124 */       this.cellRefs[i].serialize(out);
/*  95:    */     }
/*  96:127 */     this.sharedFeature.serialize(out);
/*  97:    */   }
/*  98:    */   
/*  99:    */   protected int getDataSize()
/* 100:    */   {
/* 101:131 */     return 27 + this.cellRefs.length * 8 + this.sharedFeature.getDataSize();
/* 102:    */   }
/* 103:    */   
/* 104:    */   public int getIsf_sharedFeatureType()
/* 105:    */   {
/* 106:137 */     return this.isf_sharedFeatureType;
/* 107:    */   }
/* 108:    */   
/* 109:    */   public long getCbFeatData()
/* 110:    */   {
/* 111:141 */     return this.cbFeatData;
/* 112:    */   }
/* 113:    */   
/* 114:    */   public void setCbFeatData(long cbFeatData)
/* 115:    */   {
/* 116:144 */     this.cbFeatData = cbFeatData;
/* 117:    */   }
/* 118:    */   
/* 119:    */   public CellRangeAddress[] getCellRefs()
/* 120:    */   {
/* 121:148 */     return this.cellRefs;
/* 122:    */   }
/* 123:    */   
/* 124:    */   public void setCellRefs(CellRangeAddress[] cellRefs)
/* 125:    */   {
/* 126:151 */     this.cellRefs = cellRefs;
/* 127:    */   }
/* 128:    */   
/* 129:    */   public SharedFeature getSharedFeature()
/* 130:    */   {
/* 131:155 */     return this.sharedFeature;
/* 132:    */   }
/* 133:    */   
/* 134:    */   public void setSharedFeature(SharedFeature feature)
/* 135:    */   {
/* 136:158 */     this.sharedFeature = feature;
/* 137:160 */     if ((feature instanceof FeatProtection)) {
/* 138:161 */       this.isf_sharedFeatureType = 2;
/* 139:    */     }
/* 140:163 */     if ((feature instanceof FeatFormulaErr2)) {
/* 141:164 */       this.isf_sharedFeatureType = 3;
/* 142:    */     }
/* 143:166 */     if ((feature instanceof FeatSmartTag)) {
/* 144:167 */       this.isf_sharedFeatureType = 4;
/* 145:    */     }
/* 146:170 */     if (this.isf_sharedFeatureType == 3) {
/* 147:171 */       this.cbFeatData = this.sharedFeature.getDataSize();
/* 148:    */     } else {
/* 149:173 */       this.cbFeatData = 0L;
/* 150:    */     }
/* 151:    */   }
/* 152:    */   
/* 153:    */   public FeatRecord clone()
/* 154:    */   {
/* 155:181 */     return (FeatRecord)cloneViaReserialise();
/* 156:    */   }
/* 157:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.record.FeatRecord
 * JD-Core Version:    0.7.0.1
 */