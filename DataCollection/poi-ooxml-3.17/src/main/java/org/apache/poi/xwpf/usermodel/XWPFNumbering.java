/*   1:    */ package org.apache.poi.xwpf.usermodel;
/*   2:    */ 
/*   3:    */ import java.io.IOException;
/*   4:    */ import java.io.InputStream;
/*   5:    */ import java.io.OutputStream;
/*   6:    */ import java.math.BigInteger;
/*   7:    */ import java.util.ArrayList;
/*   8:    */ import java.util.List;
/*   9:    */ import javax.xml.namespace.QName;
/*  10:    */ import org.apache.poi.POIXMLDocumentPart;
/*  11:    */ import org.apache.poi.POIXMLException;
/*  12:    */ import org.apache.poi.POIXMLTypeLoader;
/*  13:    */ import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
/*  14:    */ import org.apache.poi.openxml4j.opc.PackagePart;
/*  15:    */ import org.apache.xmlbeans.SchemaType;
/*  16:    */ import org.apache.xmlbeans.XmlException;
/*  17:    */ import org.apache.xmlbeans.XmlOptions;
/*  18:    */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTAbstractNum;
/*  19:    */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber;
/*  20:    */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTNum;
/*  21:    */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTNumbering;
/*  22:    */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.NumberingDocument;
/*  23:    */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.NumberingDocument.Factory;
/*  24:    */ 
/*  25:    */ public class XWPFNumbering
/*  26:    */   extends POIXMLDocumentPart
/*  27:    */ {
/*  28: 45 */   protected List<XWPFAbstractNum> abstractNums = new ArrayList();
/*  29: 46 */   protected List<XWPFNum> nums = new ArrayList();
/*  30:    */   boolean isNew;
/*  31:    */   private CTNumbering ctNumbering;
/*  32:    */   
/*  33:    */   public XWPFNumbering(PackagePart part)
/*  34:    */     throws IOException, OpenXML4JException
/*  35:    */   {
/*  36: 56 */     super(part);
/*  37: 57 */     this.isNew = true;
/*  38:    */   }
/*  39:    */   
/*  40:    */   public XWPFNumbering()
/*  41:    */   {
/*  42: 64 */     this.abstractNums = new ArrayList();
/*  43: 65 */     this.nums = new ArrayList();
/*  44: 66 */     this.isNew = true;
/*  45:    */   }
/*  46:    */   
/*  47:    */   protected void onDocumentRead()
/*  48:    */     throws IOException
/*  49:    */   {
/*  50: 74 */     NumberingDocument numberingDoc = null;
/*  51:    */     
/*  52: 76 */     InputStream is = getPackagePart().getInputStream();
/*  53:    */     try
/*  54:    */     {
/*  55: 78 */       numberingDoc = NumberingDocument.Factory.parse(is, POIXMLTypeLoader.DEFAULT_XML_OPTIONS);
/*  56: 79 */       this.ctNumbering = numberingDoc.getNumbering();
/*  57: 81 */       for (CTNum ctNum : this.ctNumbering.getNumArray()) {
/*  58: 82 */         this.nums.add(new XWPFNum(ctNum, this));
/*  59:    */       }
/*  60: 84 */       for (CTAbstractNum ctAbstractNum : this.ctNumbering.getAbstractNumArray()) {
/*  61: 85 */         this.abstractNums.add(new XWPFAbstractNum(ctAbstractNum, this));
/*  62:    */       }
/*  63: 87 */       this.isNew = false;
/*  64:    */     }
/*  65:    */     catch (XmlException e)
/*  66:    */     {
/*  67: 89 */       throw new POIXMLException();
/*  68:    */     }
/*  69:    */     finally
/*  70:    */     {
/*  71: 91 */       is.close();
/*  72:    */     }
/*  73:    */   }
/*  74:    */   
/*  75:    */   protected void commit()
/*  76:    */     throws IOException
/*  77:    */   {
/*  78:100 */     XmlOptions xmlOptions = new XmlOptions(POIXMLTypeLoader.DEFAULT_XML_OPTIONS);
/*  79:101 */     xmlOptions.setSaveSyntheticDocumentElement(new QName(CTNumbering.type.getName().getNamespaceURI(), "numbering"));
/*  80:102 */     PackagePart part = getPackagePart();
/*  81:103 */     OutputStream out = part.getOutputStream();
/*  82:104 */     this.ctNumbering.save(out, xmlOptions);
/*  83:105 */     out.close();
/*  84:    */   }
/*  85:    */   
/*  86:    */   public void setNumbering(CTNumbering numbering)
/*  87:    */   {
/*  88:115 */     this.ctNumbering = numbering;
/*  89:    */   }
/*  90:    */   
/*  91:    */   public boolean numExist(BigInteger numID)
/*  92:    */   {
/*  93:126 */     for (XWPFNum num : this.nums) {
/*  94:127 */       if (num.getCTNum().getNumId().equals(numID)) {
/*  95:128 */         return true;
/*  96:    */       }
/*  97:    */     }
/*  98:130 */     return false;
/*  99:    */   }
/* 100:    */   
/* 101:    */   public BigInteger addNum(XWPFNum num)
/* 102:    */   {
/* 103:139 */     this.ctNumbering.addNewNum();
/* 104:140 */     int pos = this.ctNumbering.sizeOfNumArray() - 1;
/* 105:141 */     this.ctNumbering.setNumArray(pos, num.getCTNum());
/* 106:142 */     this.nums.add(num);
/* 107:143 */     return num.getCTNum().getNumId();
/* 108:    */   }
/* 109:    */   
/* 110:    */   public BigInteger addNum(BigInteger abstractNumID)
/* 111:    */   {
/* 112:152 */     CTNum ctNum = this.ctNumbering.addNewNum();
/* 113:153 */     ctNum.addNewAbstractNumId();
/* 114:154 */     ctNum.getAbstractNumId().setVal(abstractNumID);
/* 115:155 */     ctNum.setNumId(BigInteger.valueOf(this.nums.size() + 1));
/* 116:156 */     XWPFNum num = new XWPFNum(ctNum, this);
/* 117:157 */     this.nums.add(num);
/* 118:158 */     return ctNum.getNumId();
/* 119:    */   }
/* 120:    */   
/* 121:    */   public void addNum(BigInteger abstractNumID, BigInteger numID)
/* 122:    */   {
/* 123:168 */     CTNum ctNum = this.ctNumbering.addNewNum();
/* 124:169 */     ctNum.addNewAbstractNumId();
/* 125:170 */     ctNum.getAbstractNumId().setVal(abstractNumID);
/* 126:171 */     ctNum.setNumId(numID);
/* 127:172 */     XWPFNum num = new XWPFNum(ctNum, this);
/* 128:173 */     this.nums.add(num);
/* 129:    */   }
/* 130:    */   
/* 131:    */   public XWPFNum getNum(BigInteger numID)
/* 132:    */   {
/* 133:184 */     for (XWPFNum num : this.nums) {
/* 134:185 */       if (num.getCTNum().getNumId().equals(numID)) {
/* 135:186 */         return num;
/* 136:    */       }
/* 137:    */     }
/* 138:188 */     return null;
/* 139:    */   }
/* 140:    */   
/* 141:    */   public XWPFAbstractNum getAbstractNum(BigInteger abstractNumID)
/* 142:    */   {
/* 143:199 */     for (XWPFAbstractNum abstractNum : this.abstractNums) {
/* 144:200 */       if (abstractNum.getAbstractNum().getAbstractNumId().equals(abstractNumID)) {
/* 145:201 */         return abstractNum;
/* 146:    */       }
/* 147:    */     }
/* 148:204 */     return null;
/* 149:    */   }
/* 150:    */   
/* 151:    */   public BigInteger getIdOfAbstractNum(XWPFAbstractNum abstractNum)
/* 152:    */   {
/* 153:217 */     CTAbstractNum copy = (CTAbstractNum)abstractNum.getCTAbstractNum().copy();
/* 154:218 */     XWPFAbstractNum newAbstractNum = new XWPFAbstractNum(copy, this);
/* 155:220 */     for (int i = 0; i < this.abstractNums.size(); i++)
/* 156:    */     {
/* 157:221 */       newAbstractNum.getCTAbstractNum().setAbstractNumId(BigInteger.valueOf(i));
/* 158:222 */       newAbstractNum.setNumbering(this);
/* 159:223 */       if (newAbstractNum.getCTAbstractNum().valueEquals(((XWPFAbstractNum)this.abstractNums.get(i)).getCTAbstractNum())) {
/* 160:224 */         return newAbstractNum.getCTAbstractNum().getAbstractNumId();
/* 161:    */       }
/* 162:    */     }
/* 163:227 */     return null;
/* 164:    */   }
/* 165:    */   
/* 166:    */   public BigInteger addAbstractNum(XWPFAbstractNum abstractNum)
/* 167:    */   {
/* 168:237 */     int pos = this.abstractNums.size();
/* 169:238 */     if (abstractNum.getAbstractNum() != null)
/* 170:    */     {
/* 171:239 */       this.ctNumbering.addNewAbstractNum().set(abstractNum.getAbstractNum());
/* 172:    */     }
/* 173:    */     else
/* 174:    */     {
/* 175:241 */       this.ctNumbering.addNewAbstractNum();
/* 176:242 */       abstractNum.getAbstractNum().setAbstractNumId(BigInteger.valueOf(pos));
/* 177:243 */       this.ctNumbering.setAbstractNumArray(pos, abstractNum.getAbstractNum());
/* 178:    */     }
/* 179:245 */     this.abstractNums.add(abstractNum);
/* 180:246 */     return abstractNum.getCTAbstractNum().getAbstractNumId();
/* 181:    */   }
/* 182:    */   
/* 183:    */   public boolean removeAbstractNum(BigInteger abstractNumID)
/* 184:    */   {
/* 185:257 */     if (abstractNumID.byteValue() < this.abstractNums.size())
/* 186:    */     {
/* 187:258 */       this.ctNumbering.removeAbstractNum(abstractNumID.byteValue());
/* 188:259 */       this.abstractNums.remove(abstractNumID.byteValue());
/* 189:260 */       return true;
/* 190:    */     }
/* 191:262 */     return false;
/* 192:    */   }
/* 193:    */   
/* 194:    */   public BigInteger getAbstractNumID(BigInteger numID)
/* 195:    */   {
/* 196:274 */     XWPFNum num = getNum(numID);
/* 197:275 */     if (num == null) {
/* 198:276 */       return null;
/* 199:    */     }
/* 200:277 */     if (num.getCTNum() == null) {
/* 201:278 */       return null;
/* 202:    */     }
/* 203:279 */     if (num.getCTNum().getAbstractNumId() == null) {
/* 204:280 */       return null;
/* 205:    */     }
/* 206:281 */     return num.getCTNum().getAbstractNumId().getVal();
/* 207:    */   }
/* 208:    */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xwpf.usermodel.XWPFNumbering
 * JD-Core Version:    0.7.0.1
 */