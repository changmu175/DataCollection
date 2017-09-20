/*   1:    */ package org.apache.poi.hssf.record;
/*   2:    */ 
/*   3:    */ import org.apache.poi.util.BitField;
/*   4:    */ import org.apache.poi.util.BitFieldFactory;
/*   5:    */ import org.apache.poi.util.LittleEndianOutput;
/*   6:    */ 
/*   7:    */ public final class WindowOneRecord
/*   8:    */   extends StandardRecord
/*   9:    */ {
/*  10:    */   public static final short sid = 61;
/*  11:    */   private short field_1_h_hold;
/*  12:    */   private short field_2_v_hold;
/*  13:    */   private short field_3_width;
/*  14:    */   private short field_4_height;
/*  15:    */   private short field_5_options;
/*  16: 42 */   private static final BitField hidden = BitFieldFactory.getInstance(1);
/*  17: 44 */   private static final BitField iconic = BitFieldFactory.getInstance(2);
/*  18: 47 */   private static final BitField reserved = BitFieldFactory.getInstance(4);
/*  19: 48 */   private static final BitField hscroll = BitFieldFactory.getInstance(8);
/*  20: 50 */   private static final BitField vscroll = BitFieldFactory.getInstance(16);
/*  21: 52 */   private static final BitField tabs = BitFieldFactory.getInstance(32);
/*  22:    */   private int field_6_active_sheet;
/*  23:    */   private int field_7_first_visible_tab;
/*  24:    */   private short field_8_num_selected_tabs;
/*  25:    */   private short field_9_tab_width_ratio;
/*  26:    */   
/*  27:    */   public WindowOneRecord() {}
/*  28:    */   
/*  29:    */   public WindowOneRecord(RecordInputStream in)
/*  30:    */   {
/*  31: 67 */     this.field_1_h_hold = in.readShort();
/*  32: 68 */     this.field_2_v_hold = in.readShort();
/*  33: 69 */     this.field_3_width = in.readShort();
/*  34: 70 */     this.field_4_height = in.readShort();
/*  35: 71 */     this.field_5_options = in.readShort();
/*  36: 72 */     this.field_6_active_sheet = in.readShort();
/*  37: 73 */     this.field_7_first_visible_tab = in.readShort();
/*  38: 74 */     this.field_8_num_selected_tabs = in.readShort();
/*  39: 75 */     this.field_9_tab_width_ratio = in.readShort();
/*  40:    */   }
/*  41:    */   
/*  42:    */   public void setHorizontalHold(short h)
/*  43:    */   {
/*  44: 85 */     this.field_1_h_hold = h;
/*  45:    */   }
/*  46:    */   
/*  47:    */   public void setVerticalHold(short v)
/*  48:    */   {
/*  49: 95 */     this.field_2_v_hold = v;
/*  50:    */   }
/*  51:    */   
/*  52:    */   public void setWidth(short w)
/*  53:    */   {
/*  54:105 */     this.field_3_width = w;
/*  55:    */   }
/*  56:    */   
/*  57:    */   public void setHeight(short h)
/*  58:    */   {
/*  59:115 */     this.field_4_height = h;
/*  60:    */   }
/*  61:    */   
/*  62:    */   public void setOptions(short o)
/*  63:    */   {
/*  64:126 */     this.field_5_options = o;
/*  65:    */   }
/*  66:    */   
/*  67:    */   public void setHidden(boolean ishidden)
/*  68:    */   {
/*  69:138 */     this.field_5_options = hidden.setShortBoolean(this.field_5_options, ishidden);
/*  70:    */   }
/*  71:    */   
/*  72:    */   public void setIconic(boolean isiconic)
/*  73:    */   {
/*  74:148 */     this.field_5_options = iconic.setShortBoolean(this.field_5_options, isiconic);
/*  75:    */   }
/*  76:    */   
/*  77:    */   public void setDisplayHorizonalScrollbar(boolean scroll)
/*  78:    */   {
/*  79:158 */     this.field_5_options = hscroll.setShortBoolean(this.field_5_options, scroll);
/*  80:    */   }
/*  81:    */   
/*  82:    */   public void setDisplayVerticalScrollbar(boolean scroll)
/*  83:    */   {
/*  84:168 */     this.field_5_options = vscroll.setShortBoolean(this.field_5_options, scroll);
/*  85:    */   }
/*  86:    */   
/*  87:    */   public void setDisplayTabs(boolean disptabs)
/*  88:    */   {
/*  89:178 */     this.field_5_options = tabs.setShortBoolean(this.field_5_options, disptabs);
/*  90:    */   }
/*  91:    */   
/*  92:    */   public void setActiveSheetIndex(int index)
/*  93:    */   {
/*  94:184 */     this.field_6_active_sheet = index;
/*  95:    */   }
/*  96:    */   
/*  97:    */   public void setFirstVisibleTab(int t)
/*  98:    */   {
/*  99:193 */     this.field_7_first_visible_tab = t;
/* 100:    */   }
/* 101:    */   
/* 102:    */   public void setNumSelectedTabs(short n)
/* 103:    */   {
/* 104:203 */     this.field_8_num_selected_tabs = n;
/* 105:    */   }
/* 106:    */   
/* 107:    */   public void setTabWidthRatio(short r)
/* 108:    */   {
/* 109:213 */     this.field_9_tab_width_ratio = r;
/* 110:    */   }
/* 111:    */   
/* 112:    */   public short getHorizontalHold()
/* 113:    */   {
/* 114:223 */     return this.field_1_h_hold;
/* 115:    */   }
/* 116:    */   
/* 117:    */   public short getVerticalHold()
/* 118:    */   {
/* 119:233 */     return this.field_2_v_hold;
/* 120:    */   }
/* 121:    */   
/* 122:    */   public short getWidth()
/* 123:    */   {
/* 124:243 */     return this.field_3_width;
/* 125:    */   }
/* 126:    */   
/* 127:    */   public short getHeight()
/* 128:    */   {
/* 129:253 */     return this.field_4_height;
/* 130:    */   }
/* 131:    */   
/* 132:    */   public short getOptions()
/* 133:    */   {
/* 134:264 */     return this.field_5_options;
/* 135:    */   }
/* 136:    */   
/* 137:    */   public boolean getHidden()
/* 138:    */   {
/* 139:276 */     return hidden.isSet(this.field_5_options);
/* 140:    */   }
/* 141:    */   
/* 142:    */   public boolean getIconic()
/* 143:    */   {
/* 144:286 */     return iconic.isSet(this.field_5_options);
/* 145:    */   }
/* 146:    */   
/* 147:    */   public boolean getDisplayHorizontalScrollbar()
/* 148:    */   {
/* 149:296 */     return hscroll.isSet(this.field_5_options);
/* 150:    */   }
/* 151:    */   
/* 152:    */   public boolean getDisplayVerticalScrollbar()
/* 153:    */   {
/* 154:306 */     return vscroll.isSet(this.field_5_options);
/* 155:    */   }
/* 156:    */   
/* 157:    */   public boolean getDisplayTabs()
/* 158:    */   {
/* 159:316 */     return tabs.isSet(this.field_5_options);
/* 160:    */   }
/* 161:    */   
/* 162:    */   public int getActiveSheetIndex()
/* 163:    */   {
/* 164:326 */     return this.field_6_active_sheet;
/* 165:    */   }
/* 166:    */   
/* 167:    */   public int getFirstVisibleTab()
/* 168:    */   {
/* 169:334 */     return this.field_7_first_visible_tab;
/* 170:    */   }
/* 171:    */   
/* 172:    */   public short getNumSelectedTabs()
/* 173:    */   {
/* 174:344 */     return this.field_8_num_selected_tabs;
/* 175:    */   }
/* 176:    */   
/* 177:    */   public short getTabWidthRatio()
/* 178:    */   {
/* 179:354 */     return this.field_9_tab_width_ratio;
/* 180:    */   }
/* 181:    */   
/* 182:    */   public String toString()
/* 183:    */   {
/* 184:358 */     return "[WINDOW1]\n    .h_hold          = " + Integer.toHexString(getHorizontalHold()) + "\n" + "    .v_hold          = " + Integer.toHexString(getVerticalHold()) + "\n" + "    .width           = " + Integer.toHexString(getWidth()) + "\n" + "    .height          = " + Integer.toHexString(getHeight()) + "\n" + "    .options         = " + Integer.toHexString(getOptions()) + "\n" + "        .hidden      = " + getHidden() + "\n" + "        .iconic      = " + getIconic() + "\n" + "        .hscroll     = " + getDisplayHorizontalScrollbar() + "\n" + "        .vscroll     = " + getDisplayVerticalScrollbar() + "\n" + "        .tabs        = " + getDisplayTabs() + "\n" + "    .activeSheet     = " + Integer.toHexString(getActiveSheetIndex()) + "\n" + "    .firstVisibleTab    = " + Integer.toHexString(getFirstVisibleTab()) + "\n" + "    .numselectedtabs = " + Integer.toHexString(getNumSelectedTabs()) + "\n" + "    .tabwidthratio   = " + Integer.toHexString(getTabWidthRatio()) + "\n" + "[/WINDOW1]\n";
/* 185:    */   }
/* 186:    */   
/* 187:    */   public void serialize(LittleEndianOutput out)
/* 188:    */   {
/* 189:391 */     out.writeShort(getHorizontalHold());
/* 190:392 */     out.writeShort(getVerticalHold());
/* 191:393 */     out.writeShort(getWidth());
/* 192:394 */     out.writeShort(getHeight());
/* 193:395 */     out.writeShort(getOptions());
/* 194:396 */     out.writeShort(getActiveSheetIndex());
/* 195:397 */     out.writeShort(getFirstVisibleTab());
/* 196:398 */     out.writeShort(getNumSelectedTabs());
/* 197:399 */     out.writeShort(getTabWidthRatio());
/* 198:    */   }
/* 199:    */   
/* 200:    */   protected int getDataSize()
/* 201:    */   {
/* 202:403 */     return 18;
/* 203:    */   }
/* 204:    */   
/* 205:    */   public short getSid()
/* 206:    */   {
/* 207:408 */     return 61;
/* 208:    */   }
/* 209:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.record.WindowOneRecord
 * JD-Core Version:    0.7.0.1
 */