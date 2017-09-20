/*   1:    */ package org.apache.poi.xssf.usermodel;
/*   2:    */ 
/*   3:    */ import java.net.URI;
/*   4:    */ import java.net.URISyntaxException;
/*   5:    */ import org.apache.poi.common.usermodel.HyperlinkType;
/*   6:    */ import org.apache.poi.openxml4j.opc.PackagePart;
/*   7:    */ import org.apache.poi.openxml4j.opc.PackageRelationship;
/*   8:    */ import org.apache.poi.ss.usermodel.Hyperlink;
/*   9:    */ import org.apache.poi.ss.util.CellReference;
/*  10:    */ import org.apache.poi.util.Internal;
/*  11:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTHyperlink;
/*  12:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTHyperlink.Factory;
/*  13:    */ 
/*  14:    */ public class XSSFHyperlink
/*  15:    */   implements Hyperlink
/*  16:    */ {
/*  17:    */   private final HyperlinkType _type;
/*  18:    */   private final PackageRelationship _externalRel;
/*  19:    */   private final CTHyperlink _ctHyperlink;
/*  20:    */   private String _location;
/*  21:    */   
/*  22:    */   protected XSSFHyperlink(HyperlinkType type)
/*  23:    */   {
/*  24: 48 */     this._type = type;
/*  25: 49 */     this._ctHyperlink = CTHyperlink.Factory.newInstance();
/*  26: 50 */     this._externalRel = null;
/*  27:    */   }
/*  28:    */   
/*  29:    */   protected XSSFHyperlink(CTHyperlink ctHyperlink, PackageRelationship hyperlinkRel)
/*  30:    */   {
/*  31: 60 */     this._ctHyperlink = ctHyperlink;
/*  32: 61 */     this._externalRel = hyperlinkRel;
/*  33: 65 */     if (this._externalRel == null)
/*  34:    */     {
/*  35: 67 */       if (ctHyperlink.getLocation() != null)
/*  36:    */       {
/*  37: 68 */         this._type = HyperlinkType.DOCUMENT;
/*  38: 69 */         this._location = ctHyperlink.getLocation();
/*  39:    */       }
/*  40:    */       else
/*  41:    */       {
/*  42: 70 */         if (ctHyperlink.getId() != null) {
/*  43: 71 */           throw new IllegalStateException("The hyperlink for cell " + ctHyperlink.getRef() + " references relation " + ctHyperlink.getId() + ", but that didn't exist!");
/*  44:    */         }
/*  45: 76 */         this._type = HyperlinkType.DOCUMENT;
/*  46:    */       }
/*  47:    */     }
/*  48:    */     else
/*  49:    */     {
/*  50: 79 */       URI target = this._externalRel.getTargetURI();
/*  51: 80 */       this._location = target.toString();
/*  52: 81 */       if (ctHyperlink.getLocation() != null) {
/*  53: 83 */         this._location = (this._location + "#" + ctHyperlink.getLocation());
/*  54:    */       }
/*  55: 87 */       if ((this._location.startsWith("http://")) || (this._location.startsWith("https://")) || (this._location.startsWith("ftp://"))) {
/*  56: 89 */         this._type = HyperlinkType.URL;
/*  57: 90 */       } else if (this._location.startsWith("mailto:")) {
/*  58: 91 */         this._type = HyperlinkType.EMAIL;
/*  59:    */       } else {
/*  60: 93 */         this._type = HyperlinkType.FILE;
/*  61:    */       }
/*  62:    */     }
/*  63:    */   }
/*  64:    */   
/*  65:    */   @Internal
/*  66:    */   public XSSFHyperlink(Hyperlink other)
/*  67:    */   {
/*  68:109 */     if ((other instanceof XSSFHyperlink))
/*  69:    */     {
/*  70:110 */       XSSFHyperlink xlink = (XSSFHyperlink)other;
/*  71:111 */       this._type = xlink.getTypeEnum();
/*  72:112 */       this._location = xlink._location;
/*  73:113 */       this._externalRel = xlink._externalRel;
/*  74:114 */       this._ctHyperlink = ((CTHyperlink)xlink._ctHyperlink.copy());
/*  75:    */     }
/*  76:    */     else
/*  77:    */     {
/*  78:117 */       this._type = other.getTypeEnum();
/*  79:118 */       this._location = other.getAddress();
/*  80:119 */       this._externalRel = null;
/*  81:120 */       this._ctHyperlink = CTHyperlink.Factory.newInstance();
/*  82:121 */       setCellReference(new CellReference(other.getFirstRow(), other.getFirstColumn()));
/*  83:    */     }
/*  84:    */   }
/*  85:    */   
/*  86:    */   @Internal
/*  87:    */   public CTHyperlink getCTHyperlink()
/*  88:    */   {
/*  89:129 */     return this._ctHyperlink;
/*  90:    */   }
/*  91:    */   
/*  92:    */   public boolean needsRelationToo()
/*  93:    */   {
/*  94:137 */     return this._type != HyperlinkType.DOCUMENT;
/*  95:    */   }
/*  96:    */   
/*  97:    */   protected void generateRelationIfNeeded(PackagePart sheetPart)
/*  98:    */   {
/*  99:144 */     if ((this._externalRel == null) && (needsRelationToo()))
/* 100:    */     {
/* 101:146 */       PackageRelationship rel = sheetPart.addExternalRelationship(this._location, XSSFRelation.SHEET_HYPERLINKS.getRelation());
/* 102:    */       
/* 103:    */ 
/* 104:    */ 
/* 105:150 */       this._ctHyperlink.setId(rel.getId());
/* 106:    */     }
/* 107:    */   }
/* 108:    */   
/* 109:    */   /**
/* 110:    */    * @deprecated
/* 111:    */    */
/* 112:    */   public int getType()
/* 113:    */   {
/* 114:164 */     return this._type.getCode();
/* 115:    */   }
/* 116:    */   
/* 117:    */   public HyperlinkType getTypeEnum()
/* 118:    */   {
/* 119:174 */     return this._type;
/* 120:    */   }
/* 121:    */   
/* 122:    */   public String getCellRef()
/* 123:    */   {
/* 124:182 */     return this._ctHyperlink.getRef();
/* 125:    */   }
/* 126:    */   
/* 127:    */   public String getAddress()
/* 128:    */   {
/* 129:193 */     return this._location;
/* 130:    */   }
/* 131:    */   
/* 132:    */   public String getLabel()
/* 133:    */   {
/* 134:203 */     return this._ctHyperlink.getDisplay();
/* 135:    */   }
/* 136:    */   
/* 137:    */   public String getLocation()
/* 138:    */   {
/* 139:213 */     return this._ctHyperlink.getLocation();
/* 140:    */   }
/* 141:    */   
/* 142:    */   public void setLabel(String label)
/* 143:    */   {
/* 144:223 */     this._ctHyperlink.setDisplay(label);
/* 145:    */   }
/* 146:    */   
/* 147:    */   public void setLocation(String location)
/* 148:    */   {
/* 149:233 */     this._ctHyperlink.setLocation(location);
/* 150:    */   }
/* 151:    */   
/* 152:    */   public void setAddress(String address)
/* 153:    */   {
/* 154:244 */     validate(address);
/* 155:    */     
/* 156:246 */     this._location = address;
/* 157:248 */     if (this._type == HyperlinkType.DOCUMENT) {
/* 158:249 */       setLocation(address);
/* 159:    */     }
/* 160:    */   }
/* 161:    */   
/* 162:    */   private void validate(String address)
/* 163:    */   {
/* 164:255 */     switch (1.$SwitchMap$org$apache$poi$common$usermodel$HyperlinkType[this._type.ordinal()])
/* 165:    */     {
/* 166:    */     case 1: 
/* 167:    */     case 2: 
/* 168:    */     case 3: 
/* 169:    */       try
/* 170:    */       {
/* 171:261 */         new URI(address);
/* 172:    */       }
/* 173:    */       catch (URISyntaxException e)
/* 174:    */       {
/* 175:263 */         throw new IllegalArgumentException("Address of hyperlink must be a valid URI", e);
/* 176:    */       }
/* 177:    */     case 4: 
/* 178:    */       break;
/* 179:    */     default: 
/* 180:270 */       throw new IllegalStateException("Invalid Hyperlink type: " + this._type);
/* 181:    */     }
/* 182:    */   }
/* 183:    */   
/* 184:    */   @Internal
/* 185:    */   public void setCellReference(String ref)
/* 186:    */   {
/* 187:279 */     this._ctHyperlink.setRef(ref);
/* 188:    */   }
/* 189:    */   
/* 190:    */   @Internal
/* 191:    */   public void setCellReference(CellReference ref)
/* 192:    */   {
/* 193:283 */     setCellReference(ref.formatAsString());
/* 194:    */   }
/* 195:    */   
/* 196:    */   private CellReference buildCellReference()
/* 197:    */   {
/* 198:287 */     String ref = this._ctHyperlink.getRef();
/* 199:288 */     if (ref == null) {
/* 200:289 */       ref = "A1";
/* 201:    */     }
/* 202:291 */     return new CellReference(ref);
/* 203:    */   }
/* 204:    */   
/* 205:    */   public int getFirstColumn()
/* 206:    */   {
/* 207:302 */     return buildCellReference().getCol();
/* 208:    */   }
/* 209:    */   
/* 210:    */   public int getLastColumn()
/* 211:    */   {
/* 212:313 */     return buildCellReference().getCol();
/* 213:    */   }
/* 214:    */   
/* 215:    */   public int getFirstRow()
/* 216:    */   {
/* 217:323 */     return buildCellReference().getRow();
/* 218:    */   }
/* 219:    */   
/* 220:    */   public int getLastRow()
/* 221:    */   {
/* 222:334 */     return buildCellReference().getRow();
/* 223:    */   }
/* 224:    */   
/* 225:    */   public void setFirstColumn(int col)
/* 226:    */   {
/* 227:344 */     setCellReference(new CellReference(getFirstRow(), col));
/* 228:    */   }
/* 229:    */   
/* 230:    */   public void setLastColumn(int col)
/* 231:    */   {
/* 232:355 */     setFirstColumn(col);
/* 233:    */   }
/* 234:    */   
/* 235:    */   public void setFirstRow(int row)
/* 236:    */   {
/* 237:365 */     setCellReference(new CellReference(row, getFirstColumn()));
/* 238:    */   }
/* 239:    */   
/* 240:    */   public void setLastRow(int row)
/* 241:    */   {
/* 242:376 */     setFirstRow(row);
/* 243:    */   }
/* 244:    */   
/* 245:    */   public String getTooltip()
/* 246:    */   {
/* 247:383 */     return this._ctHyperlink.getTooltip();
/* 248:    */   }
/* 249:    */   
/* 250:    */   public void setTooltip(String text)
/* 251:    */   {
/* 252:390 */     this._ctHyperlink.setTooltip(text);
/* 253:    */   }
/* 254:    */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xssf.usermodel.XSSFHyperlink
 * JD-Core Version:    0.7.0.1
 */