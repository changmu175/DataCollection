/*   1:    */ package org.apache.poi.hssf.record.cf;
/*   2:    */ 
/*   3:    */ import org.apache.poi.ss.usermodel.IconMultiStateFormatting.IconSet;
/*   4:    */ import org.apache.poi.util.BitField;
/*   5:    */ import org.apache.poi.util.BitFieldFactory;
/*   6:    */ import org.apache.poi.util.LittleEndianInput;
/*   7:    */ import org.apache.poi.util.LittleEndianOutput;
/*   8:    */ import org.apache.poi.util.POILogFactory;
/*   9:    */ import org.apache.poi.util.POILogger;
/*  10:    */ 
/*  11:    */ public final class IconMultiStateFormatting
/*  12:    */   implements Cloneable
/*  13:    */ {
/*  14: 32 */   private static POILogger log = POILogFactory.getLogger(IconMultiStateFormatting.class);
/*  15:    */   private IconMultiStateFormatting.IconSet iconSet;
/*  16:    */   private byte options;
/*  17:    */   private Threshold[] thresholds;
/*  18: 38 */   private static BitField iconOnly = BitFieldFactory.getInstance(1);
/*  19: 39 */   private static BitField reversed = BitFieldFactory.getInstance(4);
/*  20:    */   
/*  21:    */   public IconMultiStateFormatting()
/*  22:    */   {
/*  23: 42 */     this.iconSet = IconMultiStateFormatting.IconSet.GYR_3_TRAFFIC_LIGHTS;
/*  24: 43 */     this.options = 0;
/*  25: 44 */     this.thresholds = new Threshold[this.iconSet.num];
/*  26:    */   }
/*  27:    */   
/*  28:    */   public IconMultiStateFormatting(LittleEndianInput in)
/*  29:    */   {
/*  30: 47 */     in.readShort();
/*  31: 48 */     in.readByte();
/*  32: 49 */     int num = in.readByte();
/*  33: 50 */     int set = in.readByte();
/*  34: 51 */     this.iconSet = IconMultiStateFormatting.IconSet.byId(set);
/*  35: 52 */     if (this.iconSet.num != num) {
/*  36: 53 */       log.log(5, new Object[] { "Inconsistent Icon Set defintion, found " + this.iconSet + " but defined as " + num + " entries" });
/*  37:    */     }
/*  38: 55 */     this.options = in.readByte();
/*  39:    */     
/*  40: 57 */     this.thresholds = new Threshold[this.iconSet.num];
/*  41: 58 */     for (int i = 0; i < this.thresholds.length; i++) {
/*  42: 59 */       this.thresholds[i] = new IconMultiStateThreshold(in);
/*  43:    */     }
/*  44:    */   }
/*  45:    */   
/*  46:    */   public IconMultiStateFormatting.IconSet getIconSet()
/*  47:    */   {
/*  48: 64 */     return this.iconSet;
/*  49:    */   }
/*  50:    */   
/*  51:    */   public void setIconSet(IconMultiStateFormatting.IconSet set)
/*  52:    */   {
/*  53: 67 */     this.iconSet = set;
/*  54:    */   }
/*  55:    */   
/*  56:    */   public Threshold[] getThresholds()
/*  57:    */   {
/*  58: 71 */     return this.thresholds;
/*  59:    */   }
/*  60:    */   
/*  61:    */   public void setThresholds(Threshold[] thresholds)
/*  62:    */   {
/*  63: 74 */     this.thresholds = (thresholds == null ? null : (Threshold[])thresholds.clone());
/*  64:    */   }
/*  65:    */   
/*  66:    */   public boolean isIconOnly()
/*  67:    */   {
/*  68: 78 */     return getOptionFlag(iconOnly);
/*  69:    */   }
/*  70:    */   
/*  71:    */   public void setIconOnly(boolean only)
/*  72:    */   {
/*  73: 81 */     setOptionFlag(only, iconOnly);
/*  74:    */   }
/*  75:    */   
/*  76:    */   public boolean isReversed()
/*  77:    */   {
/*  78: 85 */     return getOptionFlag(reversed);
/*  79:    */   }
/*  80:    */   
/*  81:    */   public void setReversed(boolean rev)
/*  82:    */   {
/*  83: 88 */     setOptionFlag(rev, reversed);
/*  84:    */   }
/*  85:    */   
/*  86:    */   private boolean getOptionFlag(BitField field)
/*  87:    */   {
/*  88: 92 */     int value = field.getValue(this.options);
/*  89: 93 */     return value != 0;
/*  90:    */   }
/*  91:    */   
/*  92:    */   private void setOptionFlag(boolean option, BitField field)
/*  93:    */   {
/*  94: 96 */     this.options = field.setByteBoolean(this.options, option);
/*  95:    */   }
/*  96:    */   
/*  97:    */   public String toString()
/*  98:    */   {
/*  99:100 */     StringBuffer buffer = new StringBuffer();
/* 100:101 */     buffer.append("    [Icon Formatting]\n");
/* 101:102 */     buffer.append("          .icon_set = ").append(this.iconSet).append("\n");
/* 102:103 */     buffer.append("          .icon_only= ").append(isIconOnly()).append("\n");
/* 103:104 */     buffer.append("          .reversed = ").append(isReversed()).append("\n");
/* 104:105 */     for (Threshold t : this.thresholds) {
/* 105:106 */       buffer.append(t);
/* 106:    */     }
/* 107:108 */     buffer.append("    [/Icon Formatting]\n");
/* 108:109 */     return buffer.toString();
/* 109:    */   }
/* 110:    */   
/* 111:    */   public Object clone()
/* 112:    */   {
/* 113:113 */     IconMultiStateFormatting rec = new IconMultiStateFormatting();
/* 114:114 */     rec.iconSet = this.iconSet;
/* 115:115 */     rec.options = this.options;
/* 116:116 */     rec.thresholds = new Threshold[this.thresholds.length];
/* 117:117 */     System.arraycopy(this.thresholds, 0, rec.thresholds, 0, this.thresholds.length);
/* 118:118 */     return rec;
/* 119:    */   }
/* 120:    */   
/* 121:    */   public int getDataLength()
/* 122:    */   {
/* 123:122 */     int len = 6;
/* 124:123 */     for (Threshold t : this.thresholds) {
/* 125:124 */       len += t.getDataLength();
/* 126:    */     }
/* 127:126 */     return len;
/* 128:    */   }
/* 129:    */   
/* 130:    */   public void serialize(LittleEndianOutput out)
/* 131:    */   {
/* 132:130 */     out.writeShort(0);
/* 133:131 */     out.writeByte(0);
/* 134:132 */     out.writeByte(this.iconSet.num);
/* 135:133 */     out.writeByte(this.iconSet.id);
/* 136:134 */     out.writeByte(this.options);
/* 137:135 */     for (Threshold t : this.thresholds) {
/* 138:136 */       t.serialize(out);
/* 139:    */     }
/* 140:    */   }
/* 141:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.record.cf.IconMultiStateFormatting
 * JD-Core Version:    0.7.0.1
 */