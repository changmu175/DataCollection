/*   1:    */ package org.apache.poi.hssf.usermodel;
/*   2:    */ 
/*   3:    */ import java.io.ByteArrayOutputStream;
/*   4:    */ import java.io.IOException;
/*   5:    */ import java.util.List;
/*   6:    */ import org.apache.poi.ddf.EscherBoolProperty;
/*   7:    */ import org.apache.poi.ddf.EscherChildAnchorRecord;
/*   8:    */ import org.apache.poi.ddf.EscherClientAnchorRecord;
/*   9:    */ import org.apache.poi.ddf.EscherComplexProperty;
/*  10:    */ import org.apache.poi.ddf.EscherContainerRecord;
/*  11:    */ import org.apache.poi.ddf.EscherOptRecord;
/*  12:    */ import org.apache.poi.ddf.EscherProperty;
/*  13:    */ import org.apache.poi.ddf.EscherRGBProperty;
/*  14:    */ import org.apache.poi.ddf.EscherRecord;
/*  15:    */ import org.apache.poi.ddf.EscherSimpleProperty;
/*  16:    */ import org.apache.poi.ddf.EscherSpRecord;
/*  17:    */ import org.apache.poi.hssf.record.CommonObjectDataSubRecord;
/*  18:    */ import org.apache.poi.hssf.record.ObjRecord;
/*  19:    */ import org.apache.poi.ss.usermodel.Shape;
/*  20:    */ import org.apache.poi.util.LittleEndian;
/*  21:    */ import org.apache.poi.util.POILogFactory;
/*  22:    */ import org.apache.poi.util.POILogger;
/*  23:    */ import org.apache.poi.util.StringUtil;
/*  24:    */ 
/*  25:    */ public abstract class HSSFShape
/*  26:    */   implements Shape
/*  27:    */ {
/*  28: 51 */   private static final POILogger LOG = POILogFactory.getLogger(HSSFShape.class);
/*  29:    */   public static final int LINEWIDTH_ONE_PT = 12700;
/*  30:    */   public static final int LINEWIDTH_DEFAULT = 9525;
/*  31:    */   public static final int LINESTYLE__COLOR_DEFAULT = 134217792;
/*  32:    */   public static final int FILL__FILLCOLOR_DEFAULT = 134217737;
/*  33:    */   public static final boolean NO_FILL_DEFAULT = true;
/*  34:    */   public static final int LINESTYLE_SOLID = 0;
/*  35:    */   public static final int LINESTYLE_DASHSYS = 1;
/*  36:    */   public static final int LINESTYLE_DOTSYS = 2;
/*  37:    */   public static final int LINESTYLE_DASHDOTSYS = 3;
/*  38:    */   public static final int LINESTYLE_DASHDOTDOTSYS = 4;
/*  39:    */   public static final int LINESTYLE_DOTGEL = 5;
/*  40:    */   public static final int LINESTYLE_DASHGEL = 6;
/*  41:    */   public static final int LINESTYLE_LONGDASHGEL = 7;
/*  42:    */   public static final int LINESTYLE_DASHDOTGEL = 8;
/*  43:    */   public static final int LINESTYLE_LONGDASHDOTGEL = 9;
/*  44:    */   public static final int LINESTYLE_LONGDASHDOTDOTGEL = 10;
/*  45:    */   public static final int LINESTYLE_NONE = -1;
/*  46:    */   public static final int LINESTYLE_DEFAULT = -1;
/*  47:    */   private HSSFShape parent;
/*  48:    */   HSSFAnchor anchor;
/*  49:    */   private HSSFPatriarch _patriarch;
/*  50:    */   private final EscherContainerRecord _escherContainer;
/*  51:    */   private final ObjRecord _objRecord;
/*  52:    */   private final EscherOptRecord _optRecord;
/*  53:    */   public static final int NO_FILLHITTEST_TRUE = 1114112;
/*  54:    */   public static final int NO_FILLHITTEST_FALSE = 65536;
/*  55:    */   
/*  56:    */   public HSSFShape(EscherContainerRecord spContainer, ObjRecord objRecord)
/*  57:    */   {
/*  58: 92 */     this._escherContainer = spContainer;
/*  59: 93 */     this._objRecord = objRecord;
/*  60: 94 */     this._optRecord = ((EscherOptRecord)spContainer.getChildById((short)-4085));
/*  61: 95 */     this.anchor = HSSFAnchor.createAnchorFromEscher(spContainer);
/*  62:    */   }
/*  63:    */   
/*  64:    */   public HSSFShape(HSSFShape parent, HSSFAnchor anchor)
/*  65:    */   {
/*  66:102 */     this.parent = parent;
/*  67:103 */     this.anchor = anchor;
/*  68:104 */     this._escherContainer = createSpContainer();
/*  69:105 */     this._optRecord = ((EscherOptRecord)this._escherContainer.getChildById((short)-4085));
/*  70:106 */     this._objRecord = createObjRecord();
/*  71:    */   }
/*  72:    */   
/*  73:    */   protected abstract EscherContainerRecord createSpContainer();
/*  74:    */   
/*  75:    */   protected abstract ObjRecord createObjRecord();
/*  76:    */   
/*  77:    */   protected abstract void afterRemove(HSSFPatriarch paramHSSFPatriarch);
/*  78:    */   
/*  79:    */   void setShapeId(int shapeId)
/*  80:    */   {
/*  81:126 */     EscherSpRecord spRecord = (EscherSpRecord)this._escherContainer.getChildById((short)-4086);
/*  82:127 */     spRecord.setShapeId(shapeId);
/*  83:128 */     CommonObjectDataSubRecord cod = (CommonObjectDataSubRecord)this._objRecord.getSubRecords().get(0);
/*  84:129 */     cod.setObjectId((short)(shapeId % 1024));
/*  85:    */   }
/*  86:    */   
/*  87:    */   int getShapeId()
/*  88:    */   {
/*  89:136 */     return ((EscherSpRecord)this._escherContainer.getChildById((short)-4086)).getShapeId();
/*  90:    */   }
/*  91:    */   
/*  92:    */   abstract void afterInsert(HSSFPatriarch paramHSSFPatriarch);
/*  93:    */   
/*  94:    */   protected EscherContainerRecord getEscherContainer()
/*  95:    */   {
/*  96:142 */     return this._escherContainer;
/*  97:    */   }
/*  98:    */   
/*  99:    */   protected ObjRecord getObjRecord()
/* 100:    */   {
/* 101:146 */     return this._objRecord;
/* 102:    */   }
/* 103:    */   
/* 104:    */   public EscherOptRecord getOptRecord()
/* 105:    */   {
/* 106:155 */     return this._optRecord;
/* 107:    */   }
/* 108:    */   
/* 109:    */   public HSSFShape getParent()
/* 110:    */   {
/* 111:160 */     return this.parent;
/* 112:    */   }
/* 113:    */   
/* 114:    */   public HSSFAnchor getAnchor()
/* 115:    */   {
/* 116:168 */     return this.anchor;
/* 117:    */   }
/* 118:    */   
/* 119:    */   public void setAnchor(HSSFAnchor anchor)
/* 120:    */   {
/* 121:182 */     int i = 0;
/* 122:183 */     int recordId = -1;
/* 123:184 */     if (this.parent == null)
/* 124:    */     {
/* 125:185 */       if ((anchor instanceof HSSFChildAnchor)) {
/* 126:186 */         throw new IllegalArgumentException("Must use client anchors for shapes directly attached to sheet.");
/* 127:    */       }
/* 128:187 */       EscherClientAnchorRecord anch = (EscherClientAnchorRecord)this._escherContainer.getChildById((short)-4080);
/* 129:188 */       if (null != anch)
/* 130:    */       {
/* 131:189 */         for (i = 0; i < this._escherContainer.getChildRecords().size(); i++) {
/* 132:190 */           if ((this._escherContainer.getChild(i).getRecordId() == -4080) && 
/* 133:191 */             (i != this._escherContainer.getChildRecords().size() - 1)) {
/* 134:192 */             recordId = this._escherContainer.getChild(i + 1).getRecordId();
/* 135:    */           }
/* 136:    */         }
/* 137:196 */         this._escherContainer.removeChildRecord(anch);
/* 138:    */       }
/* 139:    */     }
/* 140:    */     else
/* 141:    */     {
/* 142:199 */       if ((anchor instanceof HSSFClientAnchor)) {
/* 143:200 */         throw new IllegalArgumentException("Must use child anchors for shapes attached to groups.");
/* 144:    */       }
/* 145:201 */       EscherChildAnchorRecord anch = (EscherChildAnchorRecord)this._escherContainer.getChildById((short)-4081);
/* 146:202 */       if (null != anch)
/* 147:    */       {
/* 148:203 */         for (i = 0; i < this._escherContainer.getChildRecords().size(); i++) {
/* 149:204 */           if ((this._escherContainer.getChild(i).getRecordId() == -4081) && 
/* 150:205 */             (i != this._escherContainer.getChildRecords().size() - 1)) {
/* 151:206 */             recordId = this._escherContainer.getChild(i + 1).getRecordId();
/* 152:    */           }
/* 153:    */         }
/* 154:210 */         this._escherContainer.removeChildRecord(anch);
/* 155:    */       }
/* 156:    */     }
/* 157:213 */     if (-1 == recordId) {
/* 158:214 */       this._escherContainer.addChildRecord(anchor.getEscherAnchor());
/* 159:    */     } else {
/* 160:216 */       this._escherContainer.addChildBefore(anchor.getEscherAnchor(), recordId);
/* 161:    */     }
/* 162:218 */     this.anchor = anchor;
/* 163:    */   }
/* 164:    */   
/* 165:    */   public int getLineStyleColor()
/* 166:    */   {
/* 167:225 */     EscherRGBProperty rgbProperty = (EscherRGBProperty)this._optRecord.lookup(448);
/* 168:226 */     return rgbProperty == null ? 134217792 : rgbProperty.getRgbColor();
/* 169:    */   }
/* 170:    */   
/* 171:    */   public void setLineStyleColor(int lineStyleColor)
/* 172:    */   {
/* 173:233 */     setPropertyValue(new EscherRGBProperty((short)448, lineStyleColor));
/* 174:    */   }
/* 175:    */   
/* 176:    */   public void setLineStyleColor(int red, int green, int blue)
/* 177:    */   {
/* 178:238 */     int lineStyleColor = blue << 16 | green << 8 | red;
/* 179:239 */     setPropertyValue(new EscherRGBProperty((short)448, lineStyleColor));
/* 180:    */   }
/* 181:    */   
/* 182:    */   public int getFillColor()
/* 183:    */   {
/* 184:246 */     EscherRGBProperty rgbProperty = (EscherRGBProperty)this._optRecord.lookup(385);
/* 185:247 */     return rgbProperty == null ? 134217737 : rgbProperty.getRgbColor();
/* 186:    */   }
/* 187:    */   
/* 188:    */   public void setFillColor(int fillColor)
/* 189:    */   {
/* 190:254 */     setPropertyValue(new EscherRGBProperty((short)385, fillColor));
/* 191:    */   }
/* 192:    */   
/* 193:    */   public void setFillColor(int red, int green, int blue)
/* 194:    */   {
/* 195:259 */     int fillColor = blue << 16 | green << 8 | red;
/* 196:260 */     setPropertyValue(new EscherRGBProperty((short)385, fillColor));
/* 197:    */   }
/* 198:    */   
/* 199:    */   public int getLineWidth()
/* 200:    */   {
/* 201:267 */     EscherSimpleProperty property = (EscherSimpleProperty)this._optRecord.lookup(459);
/* 202:268 */     return property == null ? 9525 : property.getPropertyValue();
/* 203:    */   }
/* 204:    */   
/* 205:    */   public void setLineWidth(int lineWidth)
/* 206:    */   {
/* 207:278 */     setPropertyValue(new EscherSimpleProperty((short)459, lineWidth));
/* 208:    */   }
/* 209:    */   
/* 210:    */   public int getLineStyle()
/* 211:    */   {
/* 212:285 */     EscherSimpleProperty property = (EscherSimpleProperty)this._optRecord.lookup(462);
/* 213:286 */     if (null == property) {
/* 214:287 */       return -1;
/* 215:    */     }
/* 216:289 */     return property.getPropertyValue();
/* 217:    */   }
/* 218:    */   
/* 219:    */   public void setLineStyle(int lineStyle)
/* 220:    */   {
/* 221:298 */     setPropertyValue(new EscherSimpleProperty((short)462, lineStyle));
/* 222:299 */     if (getLineStyle() != 0)
/* 223:    */     {
/* 224:300 */       setPropertyValue(new EscherSimpleProperty((short)471, 0));
/* 225:301 */       if (getLineStyle() == -1) {
/* 226:302 */         setPropertyValue(new EscherBoolProperty((short)511, 524288));
/* 227:    */       } else {
/* 228:304 */         setPropertyValue(new EscherBoolProperty((short)511, 524296));
/* 229:    */       }
/* 230:    */     }
/* 231:    */   }
/* 232:    */   
/* 233:    */   public boolean isNoFill()
/* 234:    */   {
/* 235:311 */     EscherBoolProperty property = (EscherBoolProperty)this._optRecord.lookup(447);
/* 236:312 */     return property == null;
/* 237:    */   }
/* 238:    */   
/* 239:    */   public void setNoFill(boolean noFill)
/* 240:    */   {
/* 241:317 */     setPropertyValue(new EscherBoolProperty((short)447, noFill ? 1114112 : 65536));
/* 242:    */   }
/* 243:    */   
/* 244:    */   protected void setPropertyValue(EscherProperty property)
/* 245:    */   {
/* 246:321 */     this._optRecord.setEscherProperty(property);
/* 247:    */   }
/* 248:    */   
/* 249:    */   public void setFlipVertical(boolean value)
/* 250:    */   {
/* 251:328 */     EscherSpRecord sp = (EscherSpRecord)getEscherContainer().getChildById((short)-4086);
/* 252:329 */     if (value) {
/* 253:330 */       sp.setFlags(sp.getFlags() | 0x80);
/* 254:    */     } else {
/* 255:332 */       sp.setFlags(sp.getFlags() & 0x7FFFFF7F);
/* 256:    */     }
/* 257:    */   }
/* 258:    */   
/* 259:    */   public void setFlipHorizontal(boolean value)
/* 260:    */   {
/* 261:340 */     EscherSpRecord sp = (EscherSpRecord)getEscherContainer().getChildById((short)-4086);
/* 262:341 */     if (value) {
/* 263:342 */       sp.setFlags(sp.getFlags() | 0x40);
/* 264:    */     } else {
/* 265:344 */       sp.setFlags(sp.getFlags() & 0x7FFFFFBF);
/* 266:    */     }
/* 267:    */   }
/* 268:    */   
/* 269:    */   public boolean isFlipVertical()
/* 270:    */   {
/* 271:352 */     EscherSpRecord sp = (EscherSpRecord)getEscherContainer().getChildById((short)-4086);
/* 272:353 */     return (sp.getFlags() & 0x80) != 0;
/* 273:    */   }
/* 274:    */   
/* 275:    */   public boolean isFlipHorizontal()
/* 276:    */   {
/* 277:360 */     EscherSpRecord sp = (EscherSpRecord)getEscherContainer().getChildById((short)-4086);
/* 278:361 */     return (sp.getFlags() & 0x40) != 0;
/* 279:    */   }
/* 280:    */   
/* 281:    */   public int getRotationDegree()
/* 282:    */   {
/* 283:368 */     ByteArrayOutputStream bos = new ByteArrayOutputStream();
/* 284:369 */     EscherSimpleProperty property = (EscherSimpleProperty)getOptRecord().lookup(4);
/* 285:370 */     if (null == property) {
/* 286:371 */       return 0;
/* 287:    */     }
/* 288:    */     try
/* 289:    */     {
/* 290:374 */       LittleEndian.putInt(property.getPropertyValue(), bos);
/* 291:375 */       return LittleEndian.getShort(bos.toByteArray(), 2);
/* 292:    */     }
/* 293:    */     catch (IOException e)
/* 294:    */     {
/* 295:377 */       LOG.log(7, new Object[] { "can't determine rotation degree", e });
/* 296:    */     }
/* 297:378 */     return 0;
/* 298:    */   }
/* 299:    */   
/* 300:    */   public void setRotationDegree(short value)
/* 301:    */   {
/* 302:391 */     setPropertyValue(new EscherSimpleProperty((short)4, value << 16));
/* 303:    */   }
/* 304:    */   
/* 305:    */   public int countOfAllChildren()
/* 306:    */   {
/* 307:398 */     return 1;
/* 308:    */   }
/* 309:    */   
/* 310:    */   protected abstract HSSFShape cloneShape();
/* 311:    */   
/* 312:    */   protected void setPatriarch(HSSFPatriarch _patriarch)
/* 313:    */   {
/* 314:404 */     this._patriarch = _patriarch;
/* 315:    */   }
/* 316:    */   
/* 317:    */   public HSSFPatriarch getPatriarch()
/* 318:    */   {
/* 319:408 */     return this._patriarch;
/* 320:    */   }
/* 321:    */   
/* 322:    */   protected void setParent(HSSFShape parent)
/* 323:    */   {
/* 324:412 */     this.parent = parent;
/* 325:    */   }
/* 326:    */   
/* 327:    */   public String getShapeName()
/* 328:    */   {
/* 329:419 */     EscherOptRecord eor = getOptRecord();
/* 330:420 */     if (eor == null) {
/* 331:421 */       return null;
/* 332:    */     }
/* 333:423 */     EscherProperty ep = eor.lookup(896);
/* 334:424 */     if ((ep instanceof EscherComplexProperty)) {
/* 335:425 */       return StringUtil.getFromUnicodeLE(((EscherComplexProperty)ep).getComplexData());
/* 336:    */     }
/* 337:427 */     return null;
/* 338:    */   }
/* 339:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.usermodel.HSSFShape
 * JD-Core Version:    0.7.0.1
 */