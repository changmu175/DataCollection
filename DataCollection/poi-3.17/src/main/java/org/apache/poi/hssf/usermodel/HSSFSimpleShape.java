/*   1:    */ package org.apache.poi.hssf.usermodel;
/*   2:    */ 
/*   3:    */ import java.util.List;
/*   4:    */ import org.apache.poi.ddf.DefaultEscherRecordFactory;
/*   5:    */ import org.apache.poi.ddf.EscherBoolProperty;
/*   6:    */ import org.apache.poi.ddf.EscherClientDataRecord;
/*   7:    */ import org.apache.poi.ddf.EscherContainerRecord;
/*   8:    */ import org.apache.poi.ddf.EscherOptRecord;
/*   9:    */ import org.apache.poi.ddf.EscherRGBProperty;
/*  10:    */ import org.apache.poi.ddf.EscherShapePathProperty;
/*  11:    */ import org.apache.poi.ddf.EscherSimpleProperty;
/*  12:    */ import org.apache.poi.ddf.EscherSpRecord;
/*  13:    */ import org.apache.poi.ddf.EscherTextboxRecord;
/*  14:    */ import org.apache.poi.hssf.record.CommonObjectDataSubRecord;
/*  15:    */ import org.apache.poi.hssf.record.EndSubRecord;
/*  16:    */ import org.apache.poi.hssf.record.EscherAggregate;
/*  17:    */ import org.apache.poi.hssf.record.ObjRecord;
/*  18:    */ import org.apache.poi.hssf.record.TextObjectRecord;
/*  19:    */ import org.apache.poi.ss.usermodel.RichTextString;
/*  20:    */ import org.apache.poi.ss.usermodel.SimpleShape;
/*  21:    */ 
/*  22:    */ public class HSSFSimpleShape
/*  23:    */   extends HSSFShape
/*  24:    */   implements SimpleShape
/*  25:    */ {
/*  26:    */   public static final short OBJECT_TYPE_LINE = 20;
/*  27:    */   public static final short OBJECT_TYPE_RECTANGLE = 1;
/*  28:    */   public static final short OBJECT_TYPE_OVAL = 3;
/*  29:    */   public static final short OBJECT_TYPE_ARC = 19;
/*  30:    */   public static final short OBJECT_TYPE_PICTURE = 75;
/*  31:    */   public static final short OBJECT_TYPE_COMBO_BOX = 201;
/*  32:    */   public static final short OBJECT_TYPE_COMMENT = 202;
/*  33:    */   public static final short OBJECT_TYPE_MICROSOFT_OFFICE_DRAWING = 30;
/*  34:    */   public static final int WRAP_SQUARE = 0;
/*  35:    */   public static final int WRAP_BY_POINTS = 1;
/*  36:    */   public static final int WRAP_NONE = 2;
/*  37:    */   private TextObjectRecord _textObjectRecord;
/*  38:    */   
/*  39:    */   public HSSFSimpleShape(EscherContainerRecord spContainer, ObjRecord objRecord, TextObjectRecord textObjectRecord)
/*  40:    */   {
/*  41: 77 */     super(spContainer, objRecord);
/*  42: 78 */     this._textObjectRecord = textObjectRecord;
/*  43:    */   }
/*  44:    */   
/*  45:    */   public HSSFSimpleShape(EscherContainerRecord spContainer, ObjRecord objRecord)
/*  46:    */   {
/*  47: 82 */     super(spContainer, objRecord);
/*  48:    */   }
/*  49:    */   
/*  50:    */   public HSSFSimpleShape(HSSFShape parent, HSSFAnchor anchor)
/*  51:    */   {
/*  52: 87 */     super(parent, anchor);
/*  53: 88 */     this._textObjectRecord = createTextObjRecord();
/*  54:    */   }
/*  55:    */   
/*  56:    */   protected TextObjectRecord getTextObjectRecord()
/*  57:    */   {
/*  58: 92 */     return this._textObjectRecord;
/*  59:    */   }
/*  60:    */   
/*  61:    */   protected TextObjectRecord createTextObjRecord()
/*  62:    */   {
/*  63: 96 */     TextObjectRecord obj = new TextObjectRecord();
/*  64: 97 */     obj.setHorizontalTextAlignment(2);
/*  65: 98 */     obj.setVerticalTextAlignment(2);
/*  66: 99 */     obj.setTextLocked(true);
/*  67:100 */     obj.setTextOrientation(0);
/*  68:101 */     obj.setStr(new HSSFRichTextString(""));
/*  69:102 */     return obj;
/*  70:    */   }
/*  71:    */   
/*  72:    */   protected EscherContainerRecord createSpContainer()
/*  73:    */   {
/*  74:107 */     EscherContainerRecord spContainer = new EscherContainerRecord();
/*  75:108 */     spContainer.setRecordId((short)-4092);
/*  76:109 */     spContainer.setOptions((short)15);
/*  77:    */     
/*  78:111 */     EscherSpRecord sp = new EscherSpRecord();
/*  79:112 */     sp.setRecordId((short)-4086);
/*  80:113 */     sp.setFlags(2560);
/*  81:114 */     sp.setVersion((short)2);
/*  82:    */     
/*  83:116 */     EscherClientDataRecord clientData = new EscherClientDataRecord();
/*  84:117 */     clientData.setRecordId((short)-4079);
/*  85:118 */     clientData.setOptions((short)0);
/*  86:    */     
/*  87:120 */     EscherOptRecord optRecord = new EscherOptRecord();
/*  88:121 */     optRecord.setEscherProperty(new EscherSimpleProperty((short)462, 0));
/*  89:122 */     optRecord.setEscherProperty(new EscherBoolProperty((short)511, 524296));
/*  90:    */     
/*  91:124 */     optRecord.setEscherProperty(new EscherRGBProperty((short)385, 134217737));
/*  92:125 */     optRecord.setEscherProperty(new EscherRGBProperty((short)448, 134217792));
/*  93:126 */     optRecord.setEscherProperty(new EscherBoolProperty((short)447, 65536));
/*  94:127 */     optRecord.setEscherProperty(new EscherBoolProperty((short)511, 524296));
/*  95:    */     
/*  96:129 */     optRecord.setEscherProperty(new EscherShapePathProperty((short)324, 4));
/*  97:130 */     optRecord.setEscherProperty(new EscherBoolProperty((short)959, 524288));
/*  98:131 */     optRecord.setRecordId((short)-4085);
/*  99:    */     
/* 100:133 */     EscherTextboxRecord escherTextbox = new EscherTextboxRecord();
/* 101:134 */     escherTextbox.setRecordId((short)-4083);
/* 102:135 */     escherTextbox.setOptions((short)0);
/* 103:    */     
/* 104:137 */     spContainer.addChildRecord(sp);
/* 105:138 */     spContainer.addChildRecord(optRecord);
/* 106:139 */     spContainer.addChildRecord(getAnchor().getEscherAnchor());
/* 107:140 */     spContainer.addChildRecord(clientData);
/* 108:141 */     spContainer.addChildRecord(escherTextbox);
/* 109:142 */     return spContainer;
/* 110:    */   }
/* 111:    */   
/* 112:    */   protected ObjRecord createObjRecord()
/* 113:    */   {
/* 114:147 */     ObjRecord obj = new ObjRecord();
/* 115:148 */     CommonObjectDataSubRecord c = new CommonObjectDataSubRecord();
/* 116:149 */     c.setLocked(true);
/* 117:150 */     c.setPrintable(true);
/* 118:151 */     c.setAutofill(true);
/* 119:152 */     c.setAutoline(true);
/* 120:153 */     EndSubRecord e = new EndSubRecord();
/* 121:    */     
/* 122:155 */     obj.addSubRecord(c);
/* 123:156 */     obj.addSubRecord(e);
/* 124:157 */     return obj;
/* 125:    */   }
/* 126:    */   
/* 127:    */   protected void afterRemove(HSSFPatriarch patriarch)
/* 128:    */   {
/* 129:162 */     patriarch.getBoundAggregate().removeShapeToObjRecord(getEscherContainer().getChildById((short)-4079));
/* 130:163 */     if (null != getEscherContainer().getChildById((short)-4083)) {
/* 131:164 */       patriarch.getBoundAggregate().removeShapeToObjRecord(getEscherContainer().getChildById((short)-4083));
/* 132:    */     }
/* 133:    */   }
/* 134:    */   
/* 135:    */   public HSSFRichTextString getString()
/* 136:    */   {
/* 137:172 */     return this._textObjectRecord.getStr();
/* 138:    */   }
/* 139:    */   
/* 140:    */   public void setString(RichTextString string)
/* 141:    */   {
/* 142:180 */     if ((getShapeType() == 0) || (getShapeType() == 20)) {
/* 143:181 */       throw new IllegalStateException("Cannot set text for shape type: " + getShapeType());
/* 144:    */     }
/* 145:183 */     HSSFRichTextString rtr = (HSSFRichTextString)string;
/* 146:185 */     if (rtr.numFormattingRuns() == 0) {
/* 147:185 */       rtr.applyFont((short)0);
/* 148:    */     }
/* 149:186 */     TextObjectRecord txo = getOrCreateTextObjRecord();
/* 150:187 */     txo.setStr(rtr);
/* 151:188 */     if (string.getString() != null) {
/* 152:189 */       setPropertyValue(new EscherSimpleProperty((short)128, string.getString().hashCode()));
/* 153:    */     }
/* 154:    */   }
/* 155:    */   
/* 156:    */   void afterInsert(HSSFPatriarch patriarch)
/* 157:    */   {
/* 158:195 */     EscherAggregate agg = patriarch.getBoundAggregate();
/* 159:196 */     agg.associateShapeToObjRecord(getEscherContainer().getChildById((short)-4079), getObjRecord());
/* 160:198 */     if (null != getTextObjectRecord()) {
/* 161:199 */       agg.associateShapeToObjRecord(getEscherContainer().getChildById((short)-4083), getTextObjectRecord());
/* 162:    */     }
/* 163:    */   }
/* 164:    */   
/* 165:    */   protected HSSFShape cloneShape()
/* 166:    */   {
/* 167:205 */     TextObjectRecord txo = null;
/* 168:206 */     EscherContainerRecord spContainer = new EscherContainerRecord();
/* 169:207 */     byte[] inSp = getEscherContainer().serialize();
/* 170:208 */     spContainer.fillFields(inSp, 0, new DefaultEscherRecordFactory());
/* 171:209 */     ObjRecord obj = (ObjRecord)getObjRecord().cloneViaReserialise();
/* 172:210 */     if ((getTextObjectRecord() != null) && (getString() != null) && (null != getString().getString())) {
/* 173:211 */       txo = (TextObjectRecord)getTextObjectRecord().cloneViaReserialise();
/* 174:    */     }
/* 175:213 */     return new HSSFSimpleShape(spContainer, obj, txo);
/* 176:    */   }
/* 177:    */   
/* 178:    */   public int getShapeType()
/* 179:    */   {
/* 180:228 */     EscherSpRecord spRecord = (EscherSpRecord)getEscherContainer().getChildById((short)-4086);
/* 181:229 */     return spRecord.getShapeType();
/* 182:    */   }
/* 183:    */   
/* 184:    */   public int getWrapText()
/* 185:    */   {
/* 186:233 */     EscherSimpleProperty property = (EscherSimpleProperty)getOptRecord().lookup(133);
/* 187:234 */     return null == property ? 0 : property.getPropertyValue();
/* 188:    */   }
/* 189:    */   
/* 190:    */   public void setWrapText(int value)
/* 191:    */   {
/* 192:238 */     setPropertyValue(new EscherSimpleProperty((short)133, false, false, value));
/* 193:    */   }
/* 194:    */   
/* 195:    */   public void setShapeType(int value)
/* 196:    */   {
/* 197:246 */     CommonObjectDataSubRecord cod = (CommonObjectDataSubRecord)getObjRecord().getSubRecords().get(0);
/* 198:247 */     cod.setObjectType((short)30);
/* 199:248 */     EscherSpRecord spRecord = (EscherSpRecord)getEscherContainer().getChildById((short)-4086);
/* 200:249 */     spRecord.setShapeType((short)value);
/* 201:    */   }
/* 202:    */   
/* 203:    */   private TextObjectRecord getOrCreateTextObjRecord()
/* 204:    */   {
/* 205:253 */     if (getTextObjectRecord() == null) {
/* 206:254 */       this._textObjectRecord = createTextObjRecord();
/* 207:    */     }
/* 208:256 */     EscherTextboxRecord escherTextbox = (EscherTextboxRecord)getEscherContainer().getChildById((short)-4083);
/* 209:257 */     if (null == escherTextbox)
/* 210:    */     {
/* 211:258 */       escherTextbox = new EscherTextboxRecord();
/* 212:259 */       escherTextbox.setRecordId((short)-4083);
/* 213:260 */       escherTextbox.setOptions((short)0);
/* 214:261 */       getEscherContainer().addChildRecord(escherTextbox);
/* 215:262 */       getPatriarch().getBoundAggregate().associateShapeToObjRecord(escherTextbox, this._textObjectRecord);
/* 216:    */     }
/* 217:264 */     return this._textObjectRecord;
/* 218:    */   }
/* 219:    */   
/* 220:    */   public int getShapeId()
/* 221:    */   {
/* 222:269 */     return super.getShapeId();
/* 223:    */   }
/* 224:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.usermodel.HSSFSimpleShape
 * JD-Core Version:    0.7.0.1
 */