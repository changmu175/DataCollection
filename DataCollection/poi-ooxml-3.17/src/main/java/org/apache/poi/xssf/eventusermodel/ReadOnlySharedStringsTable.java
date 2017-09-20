/*   1:    */ package org.apache.poi.xssf.eventusermodel;
/*   2:    */ 
/*   3:    */ import java.io.IOException;
/*   4:    */ import java.io.InputStream;
/*   5:    */ import java.io.PushbackInputStream;
/*   6:    */ import java.util.ArrayList;
/*   7:    */ import java.util.HashMap;
/*   8:    */ import java.util.List;
/*   9:    */ import java.util.Map;
/*  10:    */ import javax.xml.parsers.ParserConfigurationException;
/*  11:    */ import org.apache.poi.openxml4j.opc.OPCPackage;
/*  12:    */ import org.apache.poi.openxml4j.opc.PackagePart;
/*  13:    */ import org.apache.poi.util.SAXHelper;
/*  14:    */ import org.apache.poi.xssf.usermodel.XSSFRelation;
/*  15:    */ import org.xml.sax.Attributes;
/*  16:    */ import org.xml.sax.InputSource;
/*  17:    */ import org.xml.sax.SAXException;
/*  18:    */ import org.xml.sax.XMLReader;
/*  19:    */ import org.xml.sax.helpers.DefaultHandler;
/*  20:    */ 
/*  21:    */ public class ReadOnlySharedStringsTable
/*  22:    */   extends DefaultHandler
/*  23:    */ {
/*  24:    */   private final boolean includePhoneticRuns;
/*  25:    */   private int count;
/*  26:    */   private int uniqueCount;
/*  27:    */   private List<String> strings;
/*  28:    */   private Map<Integer, String> phoneticStrings;
/*  29:    */   private StringBuffer characters;
/*  30:    */   private boolean tIsOpen;
/*  31:    */   private boolean inRPh;
/*  32:    */   
/*  33:    */   public ReadOnlySharedStringsTable(OPCPackage pkg)
/*  34:    */     throws IOException, SAXException
/*  35:    */   {
/*  36:117 */     this(pkg, true);
/*  37:    */   }
/*  38:    */   
/*  39:    */   public ReadOnlySharedStringsTable(OPCPackage pkg, boolean includePhoneticRuns)
/*  40:    */     throws IOException, SAXException
/*  41:    */   {
/*  42:130 */     this.includePhoneticRuns = includePhoneticRuns;
/*  43:131 */     ArrayList<PackagePart> parts = pkg.getPartsByContentType(XSSFRelation.SHARED_STRINGS.getContentType());
/*  44:135 */     if (parts.size() > 0)
/*  45:    */     {
/*  46:136 */       PackagePart sstPart = (PackagePart)parts.get(0);
/*  47:137 */       readFrom(sstPart.getInputStream());
/*  48:    */     }
/*  49:    */   }
/*  50:    */   
/*  51:    */   public ReadOnlySharedStringsTable(PackagePart part)
/*  52:    */     throws IOException, SAXException
/*  53:    */   {
/*  54:150 */     this(part, true);
/*  55:    */   }
/*  56:    */   
/*  57:    */   public ReadOnlySharedStringsTable(PackagePart part, boolean includePhoneticRuns)
/*  58:    */     throws IOException, SAXException
/*  59:    */   {
/*  60:160 */     this.includePhoneticRuns = includePhoneticRuns;
/*  61:161 */     readFrom(part.getInputStream());
/*  62:    */   }
/*  63:    */   
/*  64:    */   public void readFrom(InputStream is)
/*  65:    */     throws IOException, SAXException
/*  66:    */   {
/*  67:173 */     PushbackInputStream pis = new PushbackInputStream(is, 1);
/*  68:174 */     int emptyTest = pis.read();
/*  69:175 */     if (emptyTest > -1)
/*  70:    */     {
/*  71:176 */       pis.unread(emptyTest);
/*  72:177 */       InputSource sheetSource = new InputSource(pis);
/*  73:    */       try
/*  74:    */       {
/*  75:179 */         XMLReader sheetParser = SAXHelper.newXMLReader();
/*  76:180 */         sheetParser.setContentHandler(this);
/*  77:181 */         sheetParser.parse(sheetSource);
/*  78:    */       }
/*  79:    */       catch (ParserConfigurationException e)
/*  80:    */       {
/*  81:183 */         throw new RuntimeException("SAX parser appears to be broken - " + e.getMessage());
/*  82:    */       }
/*  83:    */     }
/*  84:    */   }
/*  85:    */   
/*  86:    */   public int getCount()
/*  87:    */   {
/*  88:195 */     return this.count;
/*  89:    */   }
/*  90:    */   
/*  91:    */   public int getUniqueCount()
/*  92:    */   {
/*  93:206 */     return this.uniqueCount;
/*  94:    */   }
/*  95:    */   
/*  96:    */   public String getEntryAt(int idx)
/*  97:    */   {
/*  98:217 */     return (String)this.strings.get(idx);
/*  99:    */   }
/* 100:    */   
/* 101:    */   public List<String> getItems()
/* 102:    */   {
/* 103:221 */     return this.strings;
/* 104:    */   }
/* 105:    */   
/* 106:    */   public void startElement(String uri, String localName, String name, Attributes attributes)
/* 107:    */     throws SAXException
/* 108:    */   {
/* 109:232 */     if ((uri != null) && (!uri.equals("http://schemas.openxmlformats.org/spreadsheetml/2006/main"))) {
/* 110:233 */       return;
/* 111:    */     }
/* 112:236 */     if ("sst".equals(localName))
/* 113:    */     {
/* 114:237 */       String count = attributes.getValue("count");
/* 115:238 */       if (count != null) {
/* 116:238 */         this.count = Integer.parseInt(count);
/* 117:    */       }
/* 118:239 */       String uniqueCount = attributes.getValue("uniqueCount");
/* 119:240 */       if (uniqueCount != null) {
/* 120:240 */         this.uniqueCount = Integer.parseInt(uniqueCount);
/* 121:    */       }
/* 122:242 */       this.strings = new ArrayList(this.uniqueCount);
/* 123:243 */       this.phoneticStrings = new HashMap();
/* 124:244 */       this.characters = new StringBuffer();
/* 125:    */     }
/* 126:245 */     else if ("si".equals(localName))
/* 127:    */     {
/* 128:246 */       this.characters.setLength(0);
/* 129:    */     }
/* 130:247 */     else if ("t".equals(localName))
/* 131:    */     {
/* 132:248 */       this.tIsOpen = true;
/* 133:    */     }
/* 134:249 */     else if ("rPh".equals(localName))
/* 135:    */     {
/* 136:250 */       this.inRPh = true;
/* 137:252 */       if ((this.includePhoneticRuns) && (this.characters.length() > 0)) {
/* 138:253 */         this.characters.append(" ");
/* 139:    */       }
/* 140:    */     }
/* 141:    */   }
/* 142:    */   
/* 143:    */   public void endElement(String uri, String localName, String name)
/* 144:    */     throws SAXException
/* 145:    */   {
/* 146:260 */     if ((uri != null) && (!uri.equals("http://schemas.openxmlformats.org/spreadsheetml/2006/main"))) {
/* 147:261 */       return;
/* 148:    */     }
/* 149:264 */     if ("si".equals(localName)) {
/* 150:265 */       this.strings.add(this.characters.toString());
/* 151:266 */     } else if ("t".equals(localName)) {
/* 152:267 */       this.tIsOpen = false;
/* 153:268 */     } else if ("rPh".equals(localName)) {
/* 154:269 */       this.inRPh = false;
/* 155:    */     }
/* 156:    */   }
/* 157:    */   
/* 158:    */   public void characters(char[] ch, int start, int length)
/* 159:    */     throws SAXException
/* 160:    */   {
/* 161:278 */     if (this.tIsOpen) {
/* 162:279 */       if ((this.inRPh) && (this.includePhoneticRuns)) {
/* 163:280 */         this.characters.append(ch, start, length);
/* 164:281 */       } else if (!this.inRPh) {
/* 165:282 */         this.characters.append(ch, start, length);
/* 166:    */       }
/* 167:    */     }
/* 168:    */   }
/* 169:    */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xssf.eventusermodel.ReadOnlySharedStringsTable
 * JD-Core Version:    0.7.0.1
 */