/*   1:    */ package org.apache.poi.hssf.record;
/*   2:    */ 
/*   3:    */ import java.util.ArrayList;
/*   4:    */ import java.util.HashMap;
/*   5:    */ import java.util.Iterator;
/*   6:    */ import java.util.List;
/*   7:    */ import java.util.Map;
/*   8:    */ import org.apache.poi.util.LittleEndianOutput;
/*   9:    */ 
/*  10:    */ public abstract class PageBreakRecord
/*  11:    */   extends StandardRecord
/*  12:    */ {
/*  13: 40 */   private static final int[] EMPTY_INT_ARRAY = new int[0];
/*  14:    */   private List<Break> _breaks;
/*  15:    */   private Map<Integer, Break> _breakMap;
/*  16:    */   
/*  17:    */   public static final class Break
/*  18:    */   {
/*  19:    */     public static final int ENCODED_SIZE = 6;
/*  20:    */     public int main;
/*  21:    */     public int subFrom;
/*  22:    */     public int subTo;
/*  23:    */     
/*  24:    */     public Break(int main, int subFrom, int subTo)
/*  25:    */     {
/*  26: 61 */       this.main = main;
/*  27: 62 */       this.subFrom = subFrom;
/*  28: 63 */       this.subTo = subTo;
/*  29:    */     }
/*  30:    */     
/*  31:    */     public Break(RecordInputStream in)
/*  32:    */     {
/*  33: 67 */       this.main = (in.readUShort() - 1);
/*  34: 68 */       this.subFrom = in.readUShort();
/*  35: 69 */       this.subTo = in.readUShort();
/*  36:    */     }
/*  37:    */     
/*  38:    */     public void serialize(LittleEndianOutput out)
/*  39:    */     {
/*  40: 73 */       out.writeShort(this.main + 1);
/*  41: 74 */       out.writeShort(this.subFrom);
/*  42: 75 */       out.writeShort(this.subTo);
/*  43:    */     }
/*  44:    */   }
/*  45:    */   
/*  46:    */   protected PageBreakRecord()
/*  47:    */   {
/*  48: 80 */     this._breaks = new ArrayList();
/*  49: 81 */     this._breakMap = new HashMap();
/*  50:    */   }
/*  51:    */   
/*  52:    */   public PageBreakRecord(RecordInputStream in)
/*  53:    */   {
/*  54: 86 */     int nBreaks = in.readShort();
/*  55: 87 */     this._breaks = new ArrayList(nBreaks + 2);
/*  56: 88 */     this._breakMap = new HashMap();
/*  57: 90 */     for (int k = 0; k < nBreaks; k++)
/*  58:    */     {
/*  59: 91 */       Break br = new Break(in);
/*  60: 92 */       this._breaks.add(br);
/*  61: 93 */       this._breakMap.put(Integer.valueOf(br.main), br);
/*  62:    */     }
/*  63:    */   }
/*  64:    */   
/*  65:    */   public boolean isEmpty()
/*  66:    */   {
/*  67: 99 */     return this._breaks.isEmpty();
/*  68:    */   }
/*  69:    */   
/*  70:    */   protected int getDataSize()
/*  71:    */   {
/*  72:102 */     return 2 + this._breaks.size() * 6;
/*  73:    */   }
/*  74:    */   
/*  75:    */   public final void serialize(LittleEndianOutput out)
/*  76:    */   {
/*  77:106 */     int nBreaks = this._breaks.size();
/*  78:107 */     out.writeShort(nBreaks);
/*  79:108 */     for (int i = 0; i < nBreaks; i++) {
/*  80:109 */       ((Break)this._breaks.get(i)).serialize(out);
/*  81:    */     }
/*  82:    */   }
/*  83:    */   
/*  84:    */   public int getNumBreaks()
/*  85:    */   {
/*  86:114 */     return this._breaks.size();
/*  87:    */   }
/*  88:    */   
/*  89:    */   public final Iterator<Break> getBreaksIterator()
/*  90:    */   {
/*  91:118 */     return this._breaks.iterator();
/*  92:    */   }
/*  93:    */   
/*  94:    */   public String toString()
/*  95:    */   {
/*  96:122 */     StringBuffer retval = new StringBuffer();
/*  97:    */     String subLabel;
/*  98:    */     String label;
/*  99:    */     String mainLabel;
/* 100:    */     String subLabel;
/* 101:128 */     if (getSid() == 27)
/* 102:    */     {
/* 103:129 */       String label = "HORIZONTALPAGEBREAK";
/* 104:130 */       String mainLabel = "row";
/* 105:131 */       subLabel = "col";
/* 106:    */     }
/* 107:    */     else
/* 108:    */     {
/* 109:133 */       label = "VERTICALPAGEBREAK";
/* 110:134 */       mainLabel = "column";
/* 111:135 */       subLabel = "row";
/* 112:    */     }
/* 113:138 */     retval.append("[" + label + "]").append("\n");
/* 114:139 */     retval.append("     .sid        =").append(getSid()).append("\n");
/* 115:140 */     retval.append("     .numbreaks =").append(getNumBreaks()).append("\n");
/* 116:141 */     Iterator<Break> iterator = getBreaksIterator();
/* 117:142 */     for (int k = 0; k < getNumBreaks(); k++)
/* 118:    */     {
/* 119:144 */       Break region = (Break)iterator.next();
/* 120:    */       
/* 121:146 */       retval.append("     .").append(mainLabel).append(" (zero-based) =").append(region.main).append("\n");
/* 122:147 */       retval.append("     .").append(subLabel).append("From    =").append(region.subFrom).append("\n");
/* 123:148 */       retval.append("     .").append(subLabel).append("To      =").append(region.subTo).append("\n");
/* 124:    */     }
/* 125:151 */     retval.append("[" + label + "]").append("\n");
/* 126:152 */     return retval.toString();
/* 127:    */   }
/* 128:    */   
/* 129:    */   public void addBreak(int main, int subFrom, int subTo)
/* 130:    */   {
/* 131:163 */     Integer key = Integer.valueOf(main);
/* 132:164 */     Break region = (Break)this._breakMap.get(key);
/* 133:165 */     if (region == null)
/* 134:    */     {
/* 135:166 */       region = new Break(main, subFrom, subTo);
/* 136:167 */       this._breakMap.put(key, region);
/* 137:168 */       this._breaks.add(region);
/* 138:    */     }
/* 139:    */     else
/* 140:    */     {
/* 141:170 */       region.main = main;
/* 142:171 */       region.subFrom = subFrom;
/* 143:172 */       region.subTo = subTo;
/* 144:    */     }
/* 145:    */   }
/* 146:    */   
/* 147:    */   public final void removeBreak(int main)
/* 148:    */   {
/* 149:181 */     Integer rowKey = Integer.valueOf(main);
/* 150:182 */     Break region = (Break)this._breakMap.get(rowKey);
/* 151:183 */     this._breaks.remove(region);
/* 152:184 */     this._breakMap.remove(rowKey);
/* 153:    */   }
/* 154:    */   
/* 155:    */   public final Break getBreak(int main)
/* 156:    */   {
/* 157:193 */     Integer rowKey = Integer.valueOf(main);
/* 158:194 */     return (Break)this._breakMap.get(rowKey);
/* 159:    */   }
/* 160:    */   
/* 161:    */   public final int[] getBreaks()
/* 162:    */   {
/* 163:198 */     int count = getNumBreaks();
/* 164:199 */     if (count < 1) {
/* 165:200 */       return EMPTY_INT_ARRAY;
/* 166:    */     }
/* 167:202 */     int[] result = new int[count];
/* 168:203 */     for (int i = 0; i < count; i++)
/* 169:    */     {
/* 170:204 */       Break breakItem = (Break)this._breaks.get(i);
/* 171:205 */       result[i] = breakItem.main;
/* 172:    */     }
/* 173:207 */     return result;
/* 174:    */   }
/* 175:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.record.PageBreakRecord
 * JD-Core Version:    0.7.0.1
 */