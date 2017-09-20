/*   1:    */ package org.apache.poi.hssf.usermodel;
/*   2:    */ 
/*   3:    */ import java.awt.Dimension;
/*   4:    */ import java.io.ByteArrayInputStream;
/*   5:    */ import java.util.List;
/*   6:    */ import org.apache.poi.ddf.DefaultEscherRecordFactory;
/*   7:    */ import org.apache.poi.ddf.EscherBSERecord;
/*   8:    */ import org.apache.poi.ddf.EscherBlipRecord;
/*   9:    */ import org.apache.poi.ddf.EscherComplexProperty;
/*  10:    */ import org.apache.poi.ddf.EscherContainerRecord;
/*  11:    */ import org.apache.poi.ddf.EscherOptRecord;
/*  12:    */ import org.apache.poi.ddf.EscherSimpleProperty;
/*  13:    */ import org.apache.poi.hssf.model.InternalWorkbook;
/*  14:    */ import org.apache.poi.hssf.record.CommonObjectDataSubRecord;
/*  15:    */ import org.apache.poi.hssf.record.EscherAggregate;
/*  16:    */ import org.apache.poi.hssf.record.ObjRecord;
/*  17:    */ import org.apache.poi.ss.usermodel.ClientAnchor.AnchorType;
/*  18:    */ import org.apache.poi.ss.usermodel.Picture;
/*  19:    */ import org.apache.poi.ss.util.ImageUtils;
/*  20:    */ import org.apache.poi.util.POILogFactory;
/*  21:    */ import org.apache.poi.util.POILogger;
/*  22:    */ import org.apache.poi.util.StringUtil;
/*  23:    */ 
/*  24:    */ public class HSSFPicture
/*  25:    */   extends HSSFSimpleShape
/*  26:    */   implements Picture
/*  27:    */ {
/*  28: 49 */   private static POILogger logger = POILogFactory.getLogger(HSSFPicture.class);
/*  29:    */   public static final int PICTURE_TYPE_EMF = 2;
/*  30:    */   public static final int PICTURE_TYPE_WMF = 3;
/*  31:    */   public static final int PICTURE_TYPE_PICT = 4;
/*  32:    */   public static final int PICTURE_TYPE_JPEG = 5;
/*  33:    */   public static final int PICTURE_TYPE_PNG = 6;
/*  34:    */   public static final int PICTURE_TYPE_DIB = 7;
/*  35:    */   
/*  36:    */   public HSSFPicture(EscherContainerRecord spContainer, ObjRecord objRecord)
/*  37:    */   {
/*  38: 59 */     super(spContainer, objRecord);
/*  39:    */   }
/*  40:    */   
/*  41:    */   public HSSFPicture(HSSFShape parent, HSSFAnchor anchor)
/*  42:    */   {
/*  43: 67 */     super(parent, anchor);
/*  44: 68 */     super.setShapeType(75);
/*  45: 69 */     CommonObjectDataSubRecord cod = (CommonObjectDataSubRecord)getObjRecord().getSubRecords().get(0);
/*  46: 70 */     cod.setObjectType((short)8);
/*  47:    */   }
/*  48:    */   
/*  49:    */   public int getPictureIndex()
/*  50:    */   {
/*  51: 75 */     EscherSimpleProperty property = (EscherSimpleProperty)getOptRecord().lookup(260);
/*  52: 76 */     if (null == property) {
/*  53: 77 */       return -1;
/*  54:    */     }
/*  55: 79 */     return property.getPropertyValue();
/*  56:    */   }
/*  57:    */   
/*  58:    */   public void setPictureIndex(int pictureIndex)
/*  59:    */   {
/*  60: 84 */     setPropertyValue(new EscherSimpleProperty((short)260, false, true, pictureIndex));
/*  61:    */   }
/*  62:    */   
/*  63:    */   protected EscherContainerRecord createSpContainer()
/*  64:    */   {
/*  65: 89 */     EscherContainerRecord spContainer = super.createSpContainer();
/*  66: 90 */     EscherOptRecord opt = (EscherOptRecord)spContainer.getChildById((short)-4085);
/*  67: 91 */     opt.removeEscherProperty(462);
/*  68: 92 */     opt.removeEscherProperty(511);
/*  69: 93 */     spContainer.removeChildRecord(spContainer.getChildById((short)-4083));
/*  70: 94 */     return spContainer;
/*  71:    */   }
/*  72:    */   
/*  73:    */   public void resize()
/*  74:    */   {
/*  75:108 */     resize(1.7976931348623157E+308D);
/*  76:    */   }
/*  77:    */   
/*  78:    */   public void resize(double scale)
/*  79:    */   {
/*  80:118 */     resize(scale, scale);
/*  81:    */   }
/*  82:    */   
/*  83:    */   public void resize(double scaleX, double scaleY)
/*  84:    */   {
/*  85:140 */     HSSFClientAnchor anchor = getClientAnchor();
/*  86:141 */     anchor.setAnchorType(ClientAnchor.AnchorType.MOVE_DONT_RESIZE);
/*  87:    */     
/*  88:143 */     HSSFClientAnchor pref = getPreferredSize(scaleX, scaleY);
/*  89:    */     
/*  90:145 */     int row2 = anchor.getRow1() + (pref.getRow2() - pref.getRow1());
/*  91:146 */     int col2 = anchor.getCol1() + (pref.getCol2() - pref.getCol1());
/*  92:    */     
/*  93:148 */     anchor.setCol2((short)col2);
/*  94:    */     
/*  95:150 */     anchor.setDx2(pref.getDx2());
/*  96:    */     
/*  97:152 */     anchor.setRow2(row2);
/*  98:    */     
/*  99:154 */     anchor.setDy2(pref.getDy2());
/* 100:    */   }
/* 101:    */   
/* 102:    */   public HSSFClientAnchor getPreferredSize()
/* 103:    */   {
/* 104:165 */     return getPreferredSize(1.0D);
/* 105:    */   }
/* 106:    */   
/* 107:    */   public HSSFClientAnchor getPreferredSize(double scale)
/* 108:    */   {
/* 109:176 */     return getPreferredSize(scale, scale);
/* 110:    */   }
/* 111:    */   
/* 112:    */   public HSSFClientAnchor getPreferredSize(double scaleX, double scaleY)
/* 113:    */   {
/* 114:189 */     ImageUtils.setPreferredSize(this, scaleX, scaleY);
/* 115:190 */     return getClientAnchor();
/* 116:    */   }
/* 117:    */   
/* 118:    */   public Dimension getImageDimension()
/* 119:    */   {
/* 120:200 */     InternalWorkbook iwb = getPatriarch().getSheet().getWorkbook().getWorkbook();
/* 121:201 */     EscherBSERecord bse = iwb.getBSERecord(getPictureIndex());
/* 122:202 */     byte[] data = bse.getBlipRecord().getPicturedata();
/* 123:203 */     int type = bse.getBlipTypeWin32();
/* 124:204 */     return ImageUtils.getImageDimension(new ByteArrayInputStream(data), type);
/* 125:    */   }
/* 126:    */   
/* 127:    */   public HSSFPictureData getPictureData()
/* 128:    */   {
/* 129:214 */     int picIdx = getPictureIndex();
/* 130:215 */     if (picIdx == -1) {
/* 131:216 */       return null;
/* 132:    */     }
/* 133:219 */     HSSFPatriarch patriarch = getPatriarch();
/* 134:220 */     HSSFShape parent = getParent();
/* 135:221 */     while ((patriarch == null) && (parent != null))
/* 136:    */     {
/* 137:222 */       patriarch = parent.getPatriarch();
/* 138:223 */       parent = parent.getParent();
/* 139:    */     }
/* 140:225 */     if (patriarch == null) {
/* 141:226 */       throw new IllegalStateException("Could not find a patriarch for a HSSPicture");
/* 142:    */     }
/* 143:229 */     InternalWorkbook iwb = patriarch.getSheet().getWorkbook().getWorkbook();
/* 144:230 */     EscherBSERecord bse = iwb.getBSERecord(picIdx);
/* 145:231 */     EscherBlipRecord blipRecord = bse.getBlipRecord();
/* 146:232 */     return new HSSFPictureData(blipRecord);
/* 147:    */   }
/* 148:    */   
/* 149:    */   void afterInsert(HSSFPatriarch patriarch)
/* 150:    */   {
/* 151:237 */     EscherAggregate agg = patriarch.getBoundAggregate();
/* 152:238 */     agg.associateShapeToObjRecord(getEscherContainer().getChildById((short)-4079), getObjRecord());
/* 153:239 */     if (getPictureIndex() != -1)
/* 154:    */     {
/* 155:240 */       EscherBSERecord bse = patriarch.getSheet().getWorkbook().getWorkbook().getBSERecord(getPictureIndex());
/* 156:    */       
/* 157:242 */       bse.setRef(bse.getRef() + 1);
/* 158:    */     }
/* 159:    */   }
/* 160:    */   
/* 161:    */   public String getFileName()
/* 162:    */   {
/* 163:250 */     EscherComplexProperty propFile = (EscherComplexProperty)getOptRecord().lookup(261);
/* 164:    */     
/* 165:252 */     return null == propFile ? "" : StringUtil.getFromUnicodeLE(propFile.getComplexData()).trim();
/* 166:    */   }
/* 167:    */   
/* 168:    */   public void setFileName(String data)
/* 169:    */   {
/* 170:259 */     byte[] bytes = StringUtil.getToUnicodeLE(data);
/* 171:260 */     EscherComplexProperty prop = new EscherComplexProperty((short)261, true, bytes);
/* 172:261 */     setPropertyValue(prop);
/* 173:    */   }
/* 174:    */   
/* 175:    */   public void setShapeType(int shapeType)
/* 176:    */   {
/* 177:266 */     throw new IllegalStateException("Shape type can not be changed in " + getClass().getSimpleName());
/* 178:    */   }
/* 179:    */   
/* 180:    */   protected HSSFShape cloneShape()
/* 181:    */   {
/* 182:271 */     EscherContainerRecord spContainer = new EscherContainerRecord();
/* 183:272 */     byte[] inSp = getEscherContainer().serialize();
/* 184:273 */     spContainer.fillFields(inSp, 0, new DefaultEscherRecordFactory());
/* 185:274 */     ObjRecord obj = (ObjRecord)getObjRecord().cloneViaReserialise();
/* 186:275 */     return new HSSFPicture(spContainer, obj);
/* 187:    */   }
/* 188:    */   
/* 189:    */   public HSSFClientAnchor getClientAnchor()
/* 190:    */   {
/* 191:283 */     HSSFAnchor a = getAnchor();
/* 192:284 */     return (a instanceof HSSFClientAnchor) ? (HSSFClientAnchor)a : null;
/* 193:    */   }
/* 194:    */   
/* 195:    */   public HSSFSheet getSheet()
/* 196:    */   {
/* 197:293 */     return getPatriarch().getSheet();
/* 198:    */   }
/* 199:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.usermodel.HSSFPicture
 * JD-Core Version:    0.7.0.1
 */