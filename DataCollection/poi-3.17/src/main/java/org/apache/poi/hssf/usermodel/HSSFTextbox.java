/*   1:    */ package org.apache.poi.hssf.usermodel;
/*   2:    */ 
/*   3:    */ import org.apache.poi.ddf.DefaultEscherRecordFactory;
/*   4:    */ import org.apache.poi.ddf.EscherBoolProperty;
/*   5:    */ import org.apache.poi.ddf.EscherClientDataRecord;
/*   6:    */ import org.apache.poi.ddf.EscherContainerRecord;
/*   7:    */ import org.apache.poi.ddf.EscherOptRecord;
/*   8:    */ import org.apache.poi.ddf.EscherRGBProperty;
/*   9:    */ import org.apache.poi.ddf.EscherRecord;
/*  10:    */ import org.apache.poi.ddf.EscherSimpleProperty;
/*  11:    */ import org.apache.poi.ddf.EscherSpRecord;
/*  12:    */ import org.apache.poi.ddf.EscherTextboxRecord;
/*  13:    */ import org.apache.poi.hssf.record.CommonObjectDataSubRecord;
/*  14:    */ import org.apache.poi.hssf.record.EndSubRecord;
/*  15:    */ import org.apache.poi.hssf.record.EscherAggregate;
/*  16:    */ import org.apache.poi.hssf.record.ObjRecord;
/*  17:    */ import org.apache.poi.hssf.record.TextObjectRecord;
/*  18:    */ 
/*  19:    */ public class HSSFTextbox
/*  20:    */   extends HSSFSimpleShape
/*  21:    */ {
/*  22:    */   public static final short OBJECT_TYPE_TEXT = 6;
/*  23:    */   public static final short HORIZONTAL_ALIGNMENT_LEFT = 1;
/*  24:    */   public static final short HORIZONTAL_ALIGNMENT_CENTERED = 2;
/*  25:    */   public static final short HORIZONTAL_ALIGNMENT_RIGHT = 3;
/*  26:    */   public static final short HORIZONTAL_ALIGNMENT_JUSTIFIED = 4;
/*  27:    */   public static final short HORIZONTAL_ALIGNMENT_DISTRIBUTED = 7;
/*  28:    */   public static final short VERTICAL_ALIGNMENT_TOP = 1;
/*  29:    */   public static final short VERTICAL_ALIGNMENT_CENTER = 2;
/*  30:    */   public static final short VERTICAL_ALIGNMENT_BOTTOM = 3;
/*  31:    */   public static final short VERTICAL_ALIGNMENT_JUSTIFY = 4;
/*  32:    */   public static final short VERTICAL_ALIGNMENT_DISTRIBUTED = 7;
/*  33:    */   
/*  34:    */   public HSSFTextbox(EscherContainerRecord spContainer, ObjRecord objRecord, TextObjectRecord textObjectRecord)
/*  35:    */   {
/*  36: 48 */     super(spContainer, objRecord, textObjectRecord);
/*  37:    */   }
/*  38:    */   
/*  39:    */   public HSSFTextbox(HSSFShape parent, HSSFAnchor anchor)
/*  40:    */   {
/*  41: 61 */     super(parent, anchor);
/*  42: 62 */     setHorizontalAlignment((short)1);
/*  43: 63 */     setVerticalAlignment((short)1);
/*  44: 64 */     setString(new HSSFRichTextString(""));
/*  45:    */   }
/*  46:    */   
/*  47:    */   protected ObjRecord createObjRecord()
/*  48:    */   {
/*  49: 69 */     ObjRecord obj = new ObjRecord();
/*  50: 70 */     CommonObjectDataSubRecord c = new CommonObjectDataSubRecord();
/*  51: 71 */     c.setObjectType((short)6);
/*  52: 72 */     c.setLocked(true);
/*  53: 73 */     c.setPrintable(true);
/*  54: 74 */     c.setAutofill(true);
/*  55: 75 */     c.setAutoline(true);
/*  56: 76 */     EndSubRecord e = new EndSubRecord();
/*  57: 77 */     obj.addSubRecord(c);
/*  58: 78 */     obj.addSubRecord(e);
/*  59: 79 */     return obj;
/*  60:    */   }
/*  61:    */   
/*  62:    */   protected EscherContainerRecord createSpContainer()
/*  63:    */   {
/*  64: 84 */     EscherContainerRecord spContainer = new EscherContainerRecord();
/*  65: 85 */     EscherSpRecord sp = new EscherSpRecord();
/*  66: 86 */     EscherOptRecord opt = new EscherOptRecord();
/*  67: 87 */     EscherClientDataRecord clientData = new EscherClientDataRecord();
/*  68: 88 */     EscherTextboxRecord escherTextbox = new EscherTextboxRecord();
/*  69:    */     
/*  70: 90 */     spContainer.setRecordId((short)-4092);
/*  71: 91 */     spContainer.setOptions((short)15);
/*  72: 92 */     sp.setRecordId((short)-4086);
/*  73: 93 */     sp.setOptions((short)3234);
/*  74:    */     
/*  75: 95 */     sp.setFlags(2560);
/*  76: 96 */     opt.setRecordId((short)-4085);
/*  77: 97 */     opt.addEscherProperty(new EscherSimpleProperty((short)128, 0));
/*  78: 98 */     opt.addEscherProperty(new EscherSimpleProperty((short)133, 0));
/*  79: 99 */     opt.addEscherProperty(new EscherSimpleProperty((short)135, 0));
/*  80:100 */     opt.addEscherProperty(new EscherSimpleProperty((short)959, 524288));
/*  81:    */     
/*  82:102 */     opt.addEscherProperty(new EscherSimpleProperty((short)129, 0));
/*  83:103 */     opt.addEscherProperty(new EscherSimpleProperty((short)131, 0));
/*  84:104 */     opt.addEscherProperty(new EscherSimpleProperty((short)130, 0));
/*  85:105 */     opt.addEscherProperty(new EscherSimpleProperty((short)132, 0));
/*  86:    */     
/*  87:107 */     opt.setEscherProperty(new EscherSimpleProperty((short)462, 0));
/*  88:108 */     opt.setEscherProperty(new EscherBoolProperty((short)511, 524296));
/*  89:109 */     opt.setEscherProperty(new EscherSimpleProperty((short)459, 9525));
/*  90:110 */     opt.setEscherProperty(new EscherRGBProperty((short)385, 134217737));
/*  91:111 */     opt.setEscherProperty(new EscherRGBProperty((short)448, 134217792));
/*  92:112 */     opt.setEscherProperty(new EscherBoolProperty((short)447, 65536));
/*  93:113 */     opt.setEscherProperty(new EscherBoolProperty((short)959, 524288));
/*  94:    */     
/*  95:115 */     EscherRecord anchor = getAnchor().getEscherAnchor();
/*  96:116 */     clientData.setRecordId((short)-4079);
/*  97:117 */     clientData.setOptions((short)0);
/*  98:118 */     escherTextbox.setRecordId((short)-4083);
/*  99:119 */     escherTextbox.setOptions((short)0);
/* 100:    */     
/* 101:121 */     spContainer.addChildRecord(sp);
/* 102:122 */     spContainer.addChildRecord(opt);
/* 103:123 */     spContainer.addChildRecord(anchor);
/* 104:124 */     spContainer.addChildRecord(clientData);
/* 105:125 */     spContainer.addChildRecord(escherTextbox);
/* 106:    */     
/* 107:127 */     return spContainer;
/* 108:    */   }
/* 109:    */   
/* 110:    */   void afterInsert(HSSFPatriarch patriarch)
/* 111:    */   {
/* 112:132 */     EscherAggregate agg = patriarch.getBoundAggregate();
/* 113:133 */     agg.associateShapeToObjRecord(getEscherContainer().getChildById((short)-4079), getObjRecord());
/* 114:134 */     if (getTextObjectRecord() != null) {
/* 115:135 */       agg.associateShapeToObjRecord(getEscherContainer().getChildById((short)-4083), getTextObjectRecord());
/* 116:    */     }
/* 117:    */   }
/* 118:    */   
/* 119:    */   public int getMarginLeft()
/* 120:    */   {
/* 121:143 */     EscherSimpleProperty property = (EscherSimpleProperty)getOptRecord().lookup(129);
/* 122:144 */     return property == null ? 0 : property.getPropertyValue();
/* 123:    */   }
/* 124:    */   
/* 125:    */   public void setMarginLeft(int marginLeft)
/* 126:    */   {
/* 127:151 */     setPropertyValue(new EscherSimpleProperty((short)129, marginLeft));
/* 128:    */   }
/* 129:    */   
/* 130:    */   public int getMarginRight()
/* 131:    */   {
/* 132:158 */     EscherSimpleProperty property = (EscherSimpleProperty)getOptRecord().lookup(131);
/* 133:159 */     return property == null ? 0 : property.getPropertyValue();
/* 134:    */   }
/* 135:    */   
/* 136:    */   public void setMarginRight(int marginRight)
/* 137:    */   {
/* 138:166 */     setPropertyValue(new EscherSimpleProperty((short)131, marginRight));
/* 139:    */   }
/* 140:    */   
/* 141:    */   public int getMarginTop()
/* 142:    */   {
/* 143:173 */     EscherSimpleProperty property = (EscherSimpleProperty)getOptRecord().lookup(130);
/* 144:174 */     return property == null ? 0 : property.getPropertyValue();
/* 145:    */   }
/* 146:    */   
/* 147:    */   public void setMarginTop(int marginTop)
/* 148:    */   {
/* 149:181 */     setPropertyValue(new EscherSimpleProperty((short)130, marginTop));
/* 150:    */   }
/* 151:    */   
/* 152:    */   public int getMarginBottom()
/* 153:    */   {
/* 154:188 */     EscherSimpleProperty property = (EscherSimpleProperty)getOptRecord().lookup(132);
/* 155:189 */     return property == null ? 0 : property.getPropertyValue();
/* 156:    */   }
/* 157:    */   
/* 158:    */   public void setMarginBottom(int marginBottom)
/* 159:    */   {
/* 160:196 */     setPropertyValue(new EscherSimpleProperty((short)132, marginBottom));
/* 161:    */   }
/* 162:    */   
/* 163:    */   public short getHorizontalAlignment()
/* 164:    */   {
/* 165:203 */     return (short)getTextObjectRecord().getHorizontalTextAlignment();
/* 166:    */   }
/* 167:    */   
/* 168:    */   public void setHorizontalAlignment(short align)
/* 169:    */   {
/* 170:210 */     getTextObjectRecord().setHorizontalTextAlignment(align);
/* 171:    */   }
/* 172:    */   
/* 173:    */   public short getVerticalAlignment()
/* 174:    */   {
/* 175:217 */     return (short)getTextObjectRecord().getVerticalTextAlignment();
/* 176:    */   }
/* 177:    */   
/* 178:    */   public void setVerticalAlignment(short align)
/* 179:    */   {
/* 180:224 */     getTextObjectRecord().setVerticalTextAlignment(align);
/* 181:    */   }
/* 182:    */   
/* 183:    */   public void setShapeType(int shapeType)
/* 184:    */   {
/* 185:229 */     throw new IllegalStateException("Shape type can not be changed in " + getClass().getSimpleName());
/* 186:    */   }
/* 187:    */   
/* 188:    */   protected HSSFShape cloneShape()
/* 189:    */   {
/* 190:234 */     TextObjectRecord txo = getTextObjectRecord() == null ? null : (TextObjectRecord)getTextObjectRecord().cloneViaReserialise();
/* 191:235 */     EscherContainerRecord spContainer = new EscherContainerRecord();
/* 192:236 */     byte[] inSp = getEscherContainer().serialize();
/* 193:237 */     spContainer.fillFields(inSp, 0, new DefaultEscherRecordFactory());
/* 194:238 */     ObjRecord obj = (ObjRecord)getObjRecord().cloneViaReserialise();
/* 195:239 */     return new HSSFTextbox(spContainer, obj, txo);
/* 196:    */   }
/* 197:    */   
/* 198:    */   protected void afterRemove(HSSFPatriarch patriarch)
/* 199:    */   {
/* 200:244 */     patriarch.getBoundAggregate().removeShapeToObjRecord(getEscherContainer().getChildById((short)-4079));
/* 201:245 */     patriarch.getBoundAggregate().removeShapeToObjRecord(getEscherContainer().getChildById((short)-4083));
/* 202:    */   }
/* 203:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.usermodel.HSSFTextbox
 * JD-Core Version:    0.7.0.1
 */