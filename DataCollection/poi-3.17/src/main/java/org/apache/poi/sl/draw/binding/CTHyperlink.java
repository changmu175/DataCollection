/*   1:    */ package org.apache.poi.sl.draw.binding;
/*   2:    */ 
/*   3:    */ import javax.xml.bind.annotation.XmlAccessType;
/*   4:    */ import javax.xml.bind.annotation.XmlAccessorType;
/*   5:    */ import javax.xml.bind.annotation.XmlAttribute;
/*   6:    */ import javax.xml.bind.annotation.XmlElement;
/*   7:    */ import javax.xml.bind.annotation.XmlType;
/*   8:    */ 
/*   9:    */ @XmlAccessorType(XmlAccessType.FIELD)
/*  10:    */ @XmlType(name="CT_Hyperlink", namespace="http://schemas.openxmlformats.org/drawingml/2006/main", propOrder={"snd", "extLst"})
/*  11:    */ public class CTHyperlink
/*  12:    */ {
/*  13:    */   @XmlElement(namespace="http://schemas.openxmlformats.org/drawingml/2006/main")
/*  14:    */   protected CTEmbeddedWAVAudioFile snd;
/*  15:    */   @XmlElement(namespace="http://schemas.openxmlformats.org/drawingml/2006/main")
/*  16:    */   protected CTOfficeArtExtensionList extLst;
/*  17:    */   @XmlAttribute(namespace="http://schemas.openxmlformats.org/officeDocument/2006/relationships")
/*  18:    */   protected String id;
/*  19:    */   @XmlAttribute
/*  20:    */   protected String invalidUrl;
/*  21:    */   @XmlAttribute
/*  22:    */   protected String action;
/*  23:    */   @XmlAttribute
/*  24:    */   protected String tgtFrame;
/*  25:    */   @XmlAttribute
/*  26:    */   protected String tooltip;
/*  27:    */   @XmlAttribute
/*  28:    */   protected Boolean history;
/*  29:    */   @XmlAttribute
/*  30:    */   protected Boolean highlightClick;
/*  31:    */   @XmlAttribute
/*  32:    */   protected Boolean endSnd;
/*  33:    */   
/*  34:    */   public CTEmbeddedWAVAudioFile getSnd()
/*  35:    */   {
/*  36: 92 */     return this.snd;
/*  37:    */   }
/*  38:    */   
/*  39:    */   public void setSnd(CTEmbeddedWAVAudioFile value)
/*  40:    */   {
/*  41:104 */     this.snd = value;
/*  42:    */   }
/*  43:    */   
/*  44:    */   public boolean isSetSnd()
/*  45:    */   {
/*  46:108 */     return this.snd != null;
/*  47:    */   }
/*  48:    */   
/*  49:    */   public CTOfficeArtExtensionList getExtLst()
/*  50:    */   {
/*  51:120 */     return this.extLst;
/*  52:    */   }
/*  53:    */   
/*  54:    */   public void setExtLst(CTOfficeArtExtensionList value)
/*  55:    */   {
/*  56:132 */     this.extLst = value;
/*  57:    */   }
/*  58:    */   
/*  59:    */   public boolean isSetExtLst()
/*  60:    */   {
/*  61:136 */     return this.extLst != null;
/*  62:    */   }
/*  63:    */   
/*  64:    */   public String getId()
/*  65:    */   {
/*  66:148 */     return this.id;
/*  67:    */   }
/*  68:    */   
/*  69:    */   public void setId(String value)
/*  70:    */   {
/*  71:160 */     this.id = value;
/*  72:    */   }
/*  73:    */   
/*  74:    */   public boolean isSetId()
/*  75:    */   {
/*  76:164 */     return this.id != null;
/*  77:    */   }
/*  78:    */   
/*  79:    */   public String getInvalidUrl()
/*  80:    */   {
/*  81:176 */     if (this.invalidUrl == null) {
/*  82:177 */       return "";
/*  83:    */     }
/*  84:179 */     return this.invalidUrl;
/*  85:    */   }
/*  86:    */   
/*  87:    */   public void setInvalidUrl(String value)
/*  88:    */   {
/*  89:192 */     this.invalidUrl = value;
/*  90:    */   }
/*  91:    */   
/*  92:    */   public boolean isSetInvalidUrl()
/*  93:    */   {
/*  94:196 */     return this.invalidUrl != null;
/*  95:    */   }
/*  96:    */   
/*  97:    */   public String getAction()
/*  98:    */   {
/*  99:208 */     if (this.action == null) {
/* 100:209 */       return "";
/* 101:    */     }
/* 102:211 */     return this.action;
/* 103:    */   }
/* 104:    */   
/* 105:    */   public void setAction(String value)
/* 106:    */   {
/* 107:224 */     this.action = value;
/* 108:    */   }
/* 109:    */   
/* 110:    */   public boolean isSetAction()
/* 111:    */   {
/* 112:228 */     return this.action != null;
/* 113:    */   }
/* 114:    */   
/* 115:    */   public String getTgtFrame()
/* 116:    */   {
/* 117:240 */     if (this.tgtFrame == null) {
/* 118:241 */       return "";
/* 119:    */     }
/* 120:243 */     return this.tgtFrame;
/* 121:    */   }
/* 122:    */   
/* 123:    */   public void setTgtFrame(String value)
/* 124:    */   {
/* 125:256 */     this.tgtFrame = value;
/* 126:    */   }
/* 127:    */   
/* 128:    */   public boolean isSetTgtFrame()
/* 129:    */   {
/* 130:260 */     return this.tgtFrame != null;
/* 131:    */   }
/* 132:    */   
/* 133:    */   public String getTooltip()
/* 134:    */   {
/* 135:272 */     if (this.tooltip == null) {
/* 136:273 */       return "";
/* 137:    */     }
/* 138:275 */     return this.tooltip;
/* 139:    */   }
/* 140:    */   
/* 141:    */   public void setTooltip(String value)
/* 142:    */   {
/* 143:288 */     this.tooltip = value;
/* 144:    */   }
/* 145:    */   
/* 146:    */   public boolean isSetTooltip()
/* 147:    */   {
/* 148:292 */     return this.tooltip != null;
/* 149:    */   }
/* 150:    */   
/* 151:    */   public boolean isHistory()
/* 152:    */   {
/* 153:304 */     if (this.history == null) {
/* 154:305 */       return true;
/* 155:    */     }
/* 156:307 */     return this.history.booleanValue();
/* 157:    */   }
/* 158:    */   
/* 159:    */   public void setHistory(boolean value)
/* 160:    */   {
/* 161:320 */     this.history = Boolean.valueOf(value);
/* 162:    */   }
/* 163:    */   
/* 164:    */   public boolean isSetHistory()
/* 165:    */   {
/* 166:324 */     return this.history != null;
/* 167:    */   }
/* 168:    */   
/* 169:    */   public void unsetHistory()
/* 170:    */   {
/* 171:328 */     this.history = null;
/* 172:    */   }
/* 173:    */   
/* 174:    */   public boolean isHighlightClick()
/* 175:    */   {
/* 176:340 */     if (this.highlightClick == null) {
/* 177:341 */       return false;
/* 178:    */     }
/* 179:343 */     return this.highlightClick.booleanValue();
/* 180:    */   }
/* 181:    */   
/* 182:    */   public void setHighlightClick(boolean value)
/* 183:    */   {
/* 184:356 */     this.highlightClick = Boolean.valueOf(value);
/* 185:    */   }
/* 186:    */   
/* 187:    */   public boolean isSetHighlightClick()
/* 188:    */   {
/* 189:360 */     return this.highlightClick != null;
/* 190:    */   }
/* 191:    */   
/* 192:    */   public void unsetHighlightClick()
/* 193:    */   {
/* 194:364 */     this.highlightClick = null;
/* 195:    */   }
/* 196:    */   
/* 197:    */   public boolean isEndSnd()
/* 198:    */   {
/* 199:376 */     if (this.endSnd == null) {
/* 200:377 */       return false;
/* 201:    */     }
/* 202:379 */     return this.endSnd.booleanValue();
/* 203:    */   }
/* 204:    */   
/* 205:    */   public void setEndSnd(boolean value)
/* 206:    */   {
/* 207:392 */     this.endSnd = Boolean.valueOf(value);
/* 208:    */   }
/* 209:    */   
/* 210:    */   public boolean isSetEndSnd()
/* 211:    */   {
/* 212:396 */     return this.endSnd != null;
/* 213:    */   }
/* 214:    */   
/* 215:    */   public void unsetEndSnd()
/* 216:    */   {
/* 217:400 */     this.endSnd = null;
/* 218:    */   }
/* 219:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.sl.draw.binding.CTHyperlink
 * JD-Core Version:    0.7.0.1
 */