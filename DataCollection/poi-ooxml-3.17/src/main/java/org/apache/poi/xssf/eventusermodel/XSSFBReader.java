/*   1:    */ package org.apache.poi.xssf.eventusermodel;
/*   2:    */ 
/*   3:    */ import java.io.IOException;
/*   4:    */ import java.io.InputStream;
/*   5:    */ import java.util.ArrayList;
/*   6:    */ import java.util.Arrays;
/*   7:    */ import java.util.BitSet;
/*   8:    */ import java.util.Collections;
/*   9:    */ import java.util.HashSet;
/*  10:    */ import java.util.Iterator;
/*  11:    */ import java.util.LinkedList;
/*  12:    */ import java.util.List;
/*  13:    */ import java.util.Set;
/*  14:    */ import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
/*  15:    */ import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
/*  16:    */ import org.apache.poi.openxml4j.opc.OPCPackage;
/*  17:    */ import org.apache.poi.openxml4j.opc.PackagePart;
/*  18:    */ import org.apache.poi.openxml4j.opc.PackagePartName;
/*  19:    */ import org.apache.poi.openxml4j.opc.PackageRelationship;
/*  20:    */ import org.apache.poi.openxml4j.opc.PackageRelationshipCollection;
/*  21:    */ import org.apache.poi.openxml4j.opc.PackagingURIHelper;
/*  22:    */ import org.apache.poi.util.IOUtils;
/*  23:    */ import org.apache.poi.util.LittleEndian;
/*  24:    */ import org.apache.poi.util.POILogFactory;
/*  25:    */ import org.apache.poi.util.POILogger;
/*  26:    */ import org.apache.poi.xssf.binary.XSSFBCommentsTable;
/*  27:    */ import org.apache.poi.xssf.binary.XSSFBParseException;
/*  28:    */ import org.apache.poi.xssf.binary.XSSFBParser;
/*  29:    */ import org.apache.poi.xssf.binary.XSSFBRecordType;
/*  30:    */ import org.apache.poi.xssf.binary.XSSFBRelation;
/*  31:    */ import org.apache.poi.xssf.binary.XSSFBStylesTable;
/*  32:    */ import org.apache.poi.xssf.binary.XSSFBUtils;
/*  33:    */ import org.apache.poi.xssf.model.CommentsTable;
/*  34:    */ import org.apache.poi.xssf.usermodel.XSSFRelation;
/*  35:    */ 
/*  36:    */ public class XSSFBReader
/*  37:    */   extends XSSFReader
/*  38:    */ {
/*  39: 60 */   private static final POILogger log = POILogFactory.getLogger(XSSFBReader.class);
/*  40: 61 */   private static final Set<String> WORKSHEET_RELS = Collections.unmodifiableSet(new HashSet(Arrays.asList(new String[] { XSSFRelation.WORKSHEET.getRelation(), XSSFRelation.CHARTSHEET.getRelation(), XSSFRelation.MACRO_SHEET_BIN.getRelation(), XSSFRelation.INTL_MACRO_SHEET_BIN.getRelation(), XSSFRelation.DIALOG_SHEET_BIN.getRelation() })));
/*  41:    */   
/*  42:    */   public XSSFBReader(OPCPackage pkg)
/*  43:    */     throws IOException, OpenXML4JException
/*  44:    */   {
/*  45: 78 */     super(pkg);
/*  46:    */   }
/*  47:    */   
/*  48:    */   public String getAbsPathMetadata()
/*  49:    */     throws IOException
/*  50:    */   {
/*  51: 90 */     InputStream is = null;
/*  52:    */     try
/*  53:    */     {
/*  54: 92 */       is = this.workbookPart.getInputStream();
/*  55: 93 */       PathExtractor p = new PathExtractor(this.workbookPart.getInputStream());
/*  56: 94 */       p.parse();
/*  57: 95 */       return p.getPath();
/*  58:    */     }
/*  59:    */     finally
/*  60:    */     {
/*  61: 97 */       IOUtils.closeQuietly(is);
/*  62:    */     }
/*  63:    */   }
/*  64:    */   
/*  65:    */   public Iterator<InputStream> getSheetsData()
/*  66:    */     throws IOException, InvalidFormatException
/*  67:    */   {
/*  68:110 */     return new SheetIterator(this.workbookPart, null);
/*  69:    */   }
/*  70:    */   
/*  71:    */   public XSSFBStylesTable getXSSFBStylesTable()
/*  72:    */     throws IOException
/*  73:    */   {
/*  74:114 */     ArrayList<PackagePart> parts = this.pkg.getPartsByContentType(XSSFBRelation.STYLES_BINARY.getContentType());
/*  75:115 */     if (parts.size() == 0) {
/*  76:115 */       return null;
/*  77:    */     }
/*  78:118 */     return new XSSFBStylesTable(((PackagePart)parts.get(0)).getInputStream());
/*  79:    */   }
/*  80:    */   
/*  81:    */   public static class SheetIterator
/*  82:    */     extends XSSFReader.SheetIterator
/*  83:    */   {
/*  84:    */     private SheetIterator(PackagePart wb)
/*  85:    */       throws IOException
/*  86:    */     {
/*  87:130 */       super();
/*  88:    */     }
/*  89:    */     
/*  90:    */     Set<String> getSheetRelationships()
/*  91:    */     {
/*  92:135 */       return XSSFBReader.WORKSHEET_RELS;
/*  93:    */     }
/*  94:    */     
/*  95:    */     Iterator<XSSFSheetRef> createSheetIteratorFromWB(PackagePart wb)
/*  96:    */       throws IOException
/*  97:    */     {
/*  98:139 */       SheetRefLoader sheetRefLoader = new SheetRefLoader(wb.getInputStream(), null);
/*  99:140 */       sheetRefLoader.parse();
/* 100:141 */       return sheetRefLoader.getSheets().iterator();
/* 101:    */     }
/* 102:    */     
/* 103:    */     public CommentsTable getSheetComments()
/* 104:    */     {
/* 105:151 */       throw new IllegalArgumentException("Please use getXSSFBSheetComments");
/* 106:    */     }
/* 107:    */     
/* 108:    */     public XSSFBCommentsTable getXSSFBSheetComments()
/* 109:    */     {
/* 110:155 */       PackagePart sheetPkg = getSheetPart();
/* 111:    */       try
/* 112:    */       {
/* 113:159 */         PackageRelationshipCollection commentsList = sheetPkg.getRelationshipsByType(XSSFRelation.SHEET_COMMENTS.getRelation());
/* 114:161 */         if (commentsList.size() > 0)
/* 115:    */         {
/* 116:162 */           PackageRelationship comments = commentsList.getRelationship(0);
/* 117:163 */           if ((comments == null) || (comments.getTargetURI() == null)) {
/* 118:164 */             return null;
/* 119:    */           }
/* 120:166 */           PackagePartName commentsName = PackagingURIHelper.createPartName(comments.getTargetURI());
/* 121:167 */           PackagePart commentsPart = sheetPkg.getPackage().getPart(commentsName);
/* 122:168 */           return new XSSFBCommentsTable(commentsPart.getInputStream());
/* 123:    */         }
/* 124:    */       }
/* 125:    */       catch (InvalidFormatException e)
/* 126:    */       {
/* 127:171 */         return null;
/* 128:    */       }
/* 129:    */       catch (IOException e)
/* 130:    */       {
/* 131:173 */         return null;
/* 132:    */       }
/* 133:175 */       return null;
/* 134:    */     }
/* 135:    */   }
/* 136:    */   
/* 137:    */   private static class PathExtractor
/* 138:    */     extends XSSFBParser
/* 139:    */   {
/* 140:182 */     private static BitSet RECORDS = new BitSet();
/* 141:    */     
/* 142:    */     static
/* 143:    */     {
/* 144:184 */       RECORDS.set(XSSFBRecordType.BrtAbsPath15.getId());
/* 145:    */     }
/* 146:    */     
/* 147:186 */     private String path = null;
/* 148:    */     
/* 149:    */     public PathExtractor(InputStream is)
/* 150:    */     {
/* 151:188 */       super(RECORDS);
/* 152:    */     }
/* 153:    */     
/* 154:    */     public void handleRecord(int recordType, byte[] data)
/* 155:    */       throws XSSFBParseException
/* 156:    */     {
/* 157:193 */       if (recordType != XSSFBRecordType.BrtAbsPath15.getId()) {
/* 158:194 */         return;
/* 159:    */       }
/* 160:196 */       StringBuilder sb = new StringBuilder();
/* 161:197 */       XSSFBUtils.readXLWideString(data, 0, sb);
/* 162:198 */       this.path = sb.toString();
/* 163:    */     }
/* 164:    */     
/* 165:    */     String getPath()
/* 166:    */     {
/* 167:206 */       return this.path;
/* 168:    */     }
/* 169:    */   }
/* 170:    */   
/* 171:    */   private static class SheetRefLoader
/* 172:    */     extends XSSFBParser
/* 173:    */   {
/* 174:211 */     List<XSSFSheetRef> sheets = new LinkedList();
/* 175:    */     
/* 176:    */     private SheetRefLoader(InputStream is)
/* 177:    */     {
/* 178:214 */       super();
/* 179:    */     }
/* 180:    */     
/* 181:    */     public void handleRecord(int recordType, byte[] data)
/* 182:    */       throws XSSFBParseException
/* 183:    */     {
/* 184:219 */       if (recordType == XSSFBRecordType.BrtBundleSh.getId()) {
/* 185:220 */         addWorksheet(data);
/* 186:    */       }
/* 187:    */     }
/* 188:    */     
/* 189:    */     private void addWorksheet(byte[] data)
/* 190:    */     {
/* 191:    */       try
/* 192:    */       {
/* 193:230 */         tryToAddWorksheet(data);
/* 194:    */       }
/* 195:    */       catch (XSSFBParseException e)
/* 196:    */       {
/* 197:232 */         if (tryOldFormat(data)) {
/* 198:233 */           XSSFBReader.log.log(5, new Object[] { "This file was written with a beta version of Excel. POI will try to parse the file as a regular xlsb." });
/* 199:    */         } else {
/* 200:236 */           throw e;
/* 201:    */         }
/* 202:    */       }
/* 203:    */     }
/* 204:    */     
/* 205:    */     private void tryToAddWorksheet(byte[] data)
/* 206:    */       throws XSSFBParseException
/* 207:    */     {
/* 208:242 */       int offset = 0;
/* 209:    */       
/* 210:244 */       long hsShtat = LittleEndian.getUInt(data, offset);offset += 4;
/* 211:    */       
/* 212:246 */       long iTabID = LittleEndian.getUInt(data, offset);offset += 4;
/* 213:248 */       if ((iTabID < 1L) || (iTabID > 65535L)) {
/* 214:249 */         throw new XSSFBParseException("table id out of range: " + iTabID);
/* 215:    */       }
/* 216:251 */       StringBuilder sb = new StringBuilder();
/* 217:252 */       offset += XSSFBUtils.readXLWideString(data, offset, sb);
/* 218:253 */       String relId = sb.toString();sb.setLength(0);
/* 219:254 */       offset += XSSFBUtils.readXLWideString(data, offset, sb);
/* 220:255 */       String name = sb.toString();
/* 221:256 */       if ((relId != null) && (relId.trim().length() > 0)) {
/* 222:257 */         this.sheets.add(new XSSFSheetRef(relId, name));
/* 223:    */       }
/* 224:    */     }
/* 225:    */     
/* 226:    */     private boolean tryOldFormat(byte[] data)
/* 227:    */       throws XSSFBParseException
/* 228:    */     {
/* 229:264 */       int offset = 8;
/* 230:265 */       long iTabID = LittleEndian.getUInt(data, offset);offset += 4;
/* 231:266 */       if ((iTabID < 1L) || (iTabID > 65535L)) {
/* 232:267 */         throw new XSSFBParseException("table id out of range: " + iTabID);
/* 233:    */       }
/* 234:269 */       StringBuilder sb = new StringBuilder();
/* 235:270 */       offset += XSSFBUtils.readXLWideString(data, offset, sb);
/* 236:271 */       String relId = sb.toString();
/* 237:272 */       sb.setLength(0);
/* 238:273 */       offset += XSSFBUtils.readXLWideString(data, offset, sb);
/* 239:274 */       String name = sb.toString();
/* 240:275 */       if ((relId != null) && (relId.trim().length() > 0)) {
/* 241:276 */         this.sheets.add(new XSSFSheetRef(relId, name));
/* 242:    */       }
/* 243:278 */       if (offset == data.length) {
/* 244:279 */         return true;
/* 245:    */       }
/* 246:281 */       return false;
/* 247:    */     }
/* 248:    */     
/* 249:    */     List<XSSFSheetRef> getSheets()
/* 250:    */     {
/* 251:285 */       return this.sheets;
/* 252:    */     }
/* 253:    */   }
/* 254:    */ }



/* Location:           F:\poi-ooxml-3.17.jar

 * Qualified Name:     org.apache.poi.xssf.eventusermodel.XSSFBReader

 * JD-Core Version:    0.7.0.1

 */