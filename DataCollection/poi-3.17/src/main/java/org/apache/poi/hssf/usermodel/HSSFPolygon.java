/*   1:    */ package org.apache.poi.hssf.usermodel;
/*   2:    */ 
/*   3:    */ import org.apache.poi.ddf.EscherArrayProperty;
/*   4:    */ import org.apache.poi.ddf.EscherBoolProperty;
/*   5:    */ import org.apache.poi.ddf.EscherClientDataRecord;
/*   6:    */ import org.apache.poi.ddf.EscherContainerRecord;
/*   7:    */ import org.apache.poi.ddf.EscherOptRecord;
/*   8:    */ import org.apache.poi.ddf.EscherRGBProperty;
/*   9:    */ import org.apache.poi.ddf.EscherRecord;
/*  10:    */ import org.apache.poi.ddf.EscherShapePathProperty;
/*  11:    */ import org.apache.poi.ddf.EscherSimpleProperty;
/*  12:    */ import org.apache.poi.ddf.EscherSpRecord;
/*  13:    */ import org.apache.poi.hssf.record.CommonObjectDataSubRecord;
/*  14:    */ import org.apache.poi.hssf.record.EndSubRecord;
/*  15:    */ import org.apache.poi.hssf.record.EscherAggregate;
/*  16:    */ import org.apache.poi.hssf.record.ObjRecord;
/*  17:    */ import org.apache.poi.hssf.record.TextObjectRecord;
/*  18:    */ import org.apache.poi.util.LittleEndian;
/*  19:    */ import org.apache.poi.util.POILogFactory;
/*  20:    */ import org.apache.poi.util.POILogger;
/*  21:    */ 
/*  22:    */ public class HSSFPolygon
/*  23:    */   extends HSSFSimpleShape
/*  24:    */ {
/*  25: 30 */   private static POILogger logger = POILogFactory.getLogger(HSSFPolygon.class);
/*  26:    */   public static final short OBJECT_TYPE_MICROSOFT_OFFICE_DRAWING = 30;
/*  27:    */   
/*  28:    */   public HSSFPolygon(EscherContainerRecord spContainer, ObjRecord objRecord, TextObjectRecord _textObjectRecord)
/*  29:    */   {
/*  30: 34 */     super(spContainer, objRecord, _textObjectRecord);
/*  31:    */   }
/*  32:    */   
/*  33:    */   public HSSFPolygon(EscherContainerRecord spContainer, ObjRecord objRecord)
/*  34:    */   {
/*  35: 38 */     super(spContainer, objRecord);
/*  36:    */   }
/*  37:    */   
/*  38:    */   HSSFPolygon(HSSFShape parent, HSSFAnchor anchor)
/*  39:    */   {
/*  40: 42 */     super(parent, anchor);
/*  41:    */   }
/*  42:    */   
/*  43:    */   protected TextObjectRecord createTextObjRecord()
/*  44:    */   {
/*  45: 47 */     return null;
/*  46:    */   }
/*  47:    */   
/*  48:    */   protected EscherContainerRecord createSpContainer()
/*  49:    */   {
/*  50: 54 */     EscherContainerRecord spContainer = new EscherContainerRecord();
/*  51: 55 */     EscherSpRecord sp = new EscherSpRecord();
/*  52: 56 */     EscherOptRecord opt = new EscherOptRecord();
/*  53: 57 */     EscherClientDataRecord clientData = new EscherClientDataRecord();
/*  54:    */     
/*  55: 59 */     spContainer.setRecordId((short)-4092);
/*  56: 60 */     spContainer.setOptions((short)15);
/*  57: 61 */     sp.setRecordId((short)-4086);
/*  58: 62 */     sp.setOptions((short)2);
/*  59: 63 */     if (getParent() == null) {
/*  60: 64 */       sp.setFlags(2560);
/*  61:    */     } else {
/*  62: 66 */       sp.setFlags(2562);
/*  63:    */     }
/*  64: 68 */     opt.setRecordId((short)-4085);
/*  65: 69 */     opt.setEscherProperty(new EscherSimpleProperty((short)4, false, false, 0));
/*  66: 70 */     opt.setEscherProperty(new EscherSimpleProperty((short)322, false, false, 100));
/*  67: 71 */     opt.setEscherProperty(new EscherSimpleProperty((short)323, false, false, 100));
/*  68: 72 */     opt.setEscherProperty(new EscherShapePathProperty((short)324, 4));
/*  69:    */     
/*  70: 74 */     opt.setEscherProperty(new EscherSimpleProperty((short)383, false, false, 65537));
/*  71: 75 */     opt.setEscherProperty(new EscherSimpleProperty((short)464, false, false, 0));
/*  72: 76 */     opt.setEscherProperty(new EscherSimpleProperty((short)465, false, false, 0));
/*  73: 77 */     opt.setEscherProperty(new EscherSimpleProperty((short)471, false, false, 0));
/*  74:    */     
/*  75: 79 */     opt.setEscherProperty(new EscherSimpleProperty((short)462, 0));
/*  76: 80 */     opt.setEscherProperty(new EscherBoolProperty((short)511, 524296));
/*  77: 81 */     opt.setEscherProperty(new EscherSimpleProperty((short)459, 9525));
/*  78: 82 */     opt.setEscherProperty(new EscherRGBProperty((short)385, 134217737));
/*  79: 83 */     opt.setEscherProperty(new EscherRGBProperty((short)448, 134217792));
/*  80: 84 */     opt.setEscherProperty(new EscherBoolProperty((short)447, 1));
/*  81:    */     
/*  82: 86 */     opt.setEscherProperty(new EscherBoolProperty((short)959, 524288));
/*  83:    */     
/*  84: 88 */     EscherRecord anchor = getAnchor().getEscherAnchor();
/*  85: 89 */     clientData.setRecordId((short)-4079);
/*  86: 90 */     clientData.setOptions((short)0);
/*  87:    */     
/*  88: 92 */     spContainer.addChildRecord(sp);
/*  89: 93 */     spContainer.addChildRecord(opt);
/*  90: 94 */     spContainer.addChildRecord(anchor);
/*  91: 95 */     spContainer.addChildRecord(clientData);
/*  92:    */     
/*  93: 97 */     return spContainer;
/*  94:    */   }
/*  95:    */   
/*  96:    */   protected ObjRecord createObjRecord()
/*  97:    */   {
/*  98:104 */     ObjRecord obj = new ObjRecord();
/*  99:105 */     CommonObjectDataSubRecord c = new CommonObjectDataSubRecord();
/* 100:106 */     c.setObjectType((short)30);
/* 101:107 */     c.setLocked(true);
/* 102:108 */     c.setPrintable(true);
/* 103:109 */     c.setAutofill(true);
/* 104:110 */     c.setAutoline(true);
/* 105:111 */     EndSubRecord e = new EndSubRecord();
/* 106:112 */     obj.addSubRecord(c);
/* 107:113 */     obj.addSubRecord(e);
/* 108:114 */     return obj;
/* 109:    */   }
/* 110:    */   
/* 111:    */   protected void afterRemove(HSSFPatriarch patriarch)
/* 112:    */   {
/* 113:119 */     patriarch.getBoundAggregate().removeShapeToObjRecord(getEscherContainer().getChildById((short)-4079));
/* 114:    */   }
/* 115:    */   
/* 116:    */   public int[] getXPoints()
/* 117:    */   {
/* 118:126 */     EscherArrayProperty verticesProp = (EscherArrayProperty)getOptRecord().lookup(325);
/* 119:127 */     if (null == verticesProp) {
/* 120:128 */       return new int[0];
/* 121:    */     }
/* 122:130 */     int[] array = new int[verticesProp.getNumberOfElementsInArray() - 1];
/* 123:131 */     for (int i = 0; i < verticesProp.getNumberOfElementsInArray() - 1; i++)
/* 124:    */     {
/* 125:132 */       byte[] property = verticesProp.getElement(i);
/* 126:133 */       short x = LittleEndian.getShort(property, 0);
/* 127:134 */       array[i] = x;
/* 128:    */     }
/* 129:136 */     return array;
/* 130:    */   }
/* 131:    */   
/* 132:    */   public int[] getYPoints()
/* 133:    */   {
/* 134:143 */     EscherArrayProperty verticesProp = (EscherArrayProperty)getOptRecord().lookup(325);
/* 135:144 */     if (null == verticesProp) {
/* 136:145 */       return new int[0];
/* 137:    */     }
/* 138:147 */     int[] array = new int[verticesProp.getNumberOfElementsInArray() - 1];
/* 139:148 */     for (int i = 0; i < verticesProp.getNumberOfElementsInArray() - 1; i++)
/* 140:    */     {
/* 141:149 */       byte[] property = verticesProp.getElement(i);
/* 142:150 */       short x = LittleEndian.getShort(property, 2);
/* 143:151 */       array[i] = x;
/* 144:    */     }
/* 145:153 */     return array;
/* 146:    */   }
/* 147:    */   
/* 148:    */   public void setPoints(int[] xPoints, int[] yPoints)
/* 149:    */   {
/* 150:161 */     if (xPoints.length != yPoints.length)
/* 151:    */     {
/* 152:162 */       logger.log(7, new Object[] { "xPoint.length must be equal to yPoints.length" });
/* 153:163 */       return;
/* 154:    */     }
/* 155:165 */     if (xPoints.length == 0) {
/* 156:166 */       logger.log(7, new Object[] { "HSSFPolygon must have at least one point" });
/* 157:    */     }
/* 158:168 */     EscherArrayProperty verticesProp = new EscherArrayProperty((short)325, false, new byte[0]);
/* 159:169 */     verticesProp.setNumberOfElementsInArray(xPoints.length + 1);
/* 160:170 */     verticesProp.setNumberOfElementsInMemory(xPoints.length + 1);
/* 161:171 */     verticesProp.setSizeOfElements(65520);
/* 162:172 */     for (int i = 0; i < xPoints.length; i++)
/* 163:    */     {
/* 164:174 */       byte[] data = new byte[4];
/* 165:175 */       LittleEndian.putShort(data, 0, (short)xPoints[i]);
/* 166:176 */       LittleEndian.putShort(data, 2, (short)yPoints[i]);
/* 167:177 */       verticesProp.setElement(i, data);
/* 168:    */     }
/* 169:179 */     int point = xPoints.length;
/* 170:180 */     byte[] data = new byte[4];
/* 171:181 */     LittleEndian.putShort(data, 0, (short)xPoints[0]);
/* 172:182 */     LittleEndian.putShort(data, 2, (short)yPoints[0]);
/* 173:183 */     verticesProp.setElement(point, data);
/* 174:184 */     setPropertyValue(verticesProp);
/* 175:    */     
/* 176:186 */     EscherArrayProperty segmentsProp = new EscherArrayProperty((short)326, false, null);
/* 177:187 */     segmentsProp.setSizeOfElements(2);
/* 178:188 */     segmentsProp.setNumberOfElementsInArray(xPoints.length * 2 + 4);
/* 179:189 */     segmentsProp.setNumberOfElementsInMemory(xPoints.length * 2 + 4);
/* 180:190 */     segmentsProp.setElement(0, new byte[] { 0, 64 });
/* 181:191 */     segmentsProp.setElement(1, new byte[] { 0, -84 });
/* 182:192 */     for (int i = 0; i < xPoints.length; i++)
/* 183:    */     {
/* 184:194 */       segmentsProp.setElement(2 + i * 2, new byte[] { 1, 0 });
/* 185:195 */       segmentsProp.setElement(3 + i * 2, new byte[] { 0, -84 });
/* 186:    */     }
/* 187:197 */     segmentsProp.setElement(segmentsProp.getNumberOfElementsInArray() - 2, new byte[] { 1, 96 });
/* 188:198 */     segmentsProp.setElement(segmentsProp.getNumberOfElementsInArray() - 1, new byte[] { 0, -128 });
/* 189:199 */     setPropertyValue(segmentsProp);
/* 190:    */   }
/* 191:    */   
/* 192:    */   public void setPolygonDrawArea(int width, int height)
/* 193:    */   {
/* 194:208 */     setPropertyValue(new EscherSimpleProperty((short)322, width));
/* 195:209 */     setPropertyValue(new EscherSimpleProperty((short)323, height));
/* 196:    */   }
/* 197:    */   
/* 198:    */   public int getDrawAreaWidth()
/* 199:    */   {
/* 200:216 */     EscherSimpleProperty property = (EscherSimpleProperty)getOptRecord().lookup(322);
/* 201:217 */     return property == null ? 100 : property.getPropertyValue();
/* 202:    */   }
/* 203:    */   
/* 204:    */   public int getDrawAreaHeight()
/* 205:    */   {
/* 206:224 */     EscherSimpleProperty property = (EscherSimpleProperty)getOptRecord().lookup(323);
/* 207:225 */     return property == null ? 100 : property.getPropertyValue();
/* 208:    */   }
/* 209:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.usermodel.HSSFPolygon
 * JD-Core Version:    0.7.0.1
 */