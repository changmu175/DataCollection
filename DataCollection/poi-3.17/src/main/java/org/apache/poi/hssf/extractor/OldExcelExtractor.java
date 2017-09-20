/*   1:    */ package org.apache.poi.hssf.extractor;
/*   2:    */ 
/*   3:    */ import java.io.BufferedInputStream;
/*   4:    */ import java.io.Closeable;
/*   5:    */ import java.io.File;
/*   6:    */ import java.io.FileInputStream;
/*   7:    */ import java.io.FileNotFoundException;
/*   8:    */ import java.io.IOException;
/*   9:    */ import java.io.InputStream;
/*  10:    */ import java.io.PrintStream;
/*  11:    */ import org.apache.poi.EncryptedDocumentException;
/*  12:    */ import org.apache.poi.hssf.OldExcelFormatException;
/*  13:    */ import org.apache.poi.hssf.record.BOFRecord;
/*  14:    */ import org.apache.poi.hssf.record.CodepageRecord;
/*  15:    */ import org.apache.poi.hssf.record.FormulaRecord;
/*  16:    */ import org.apache.poi.hssf.record.NumberRecord;
/*  17:    */ import org.apache.poi.hssf.record.OldFormulaRecord;
/*  18:    */ import org.apache.poi.hssf.record.OldLabelRecord;
/*  19:    */ import org.apache.poi.hssf.record.OldSheetRecord;
/*  20:    */ import org.apache.poi.hssf.record.OldStringRecord;
/*  21:    */ import org.apache.poi.hssf.record.RKRecord;
/*  22:    */ import org.apache.poi.hssf.record.RecordInputStream;
/*  23:    */ import org.apache.poi.poifs.filesystem.DirectoryNode;
/*  24:    */ import org.apache.poi.poifs.filesystem.DocumentNode;
/*  25:    */ import org.apache.poi.poifs.filesystem.NPOIFSFileSystem;
/*  26:    */ import org.apache.poi.poifs.filesystem.NotOLE2FileException;
/*  27:    */ import org.apache.poi.ss.usermodel.CellType;
/*  28:    */ import org.apache.poi.util.IOUtils;
/*  29:    */ 
/*  30:    */ public class OldExcelExtractor
/*  31:    */   implements Closeable
/*  32:    */ {
/*  33:    */   private static final int FILE_PASS_RECORD_SID = 47;
/*  34:    */   private RecordInputStream ris;
/*  35:    */   private Closeable toClose;
/*  36:    */   private int biffVersion;
/*  37:    */   private int fileType;
/*  38:    */   
/*  39:    */   public OldExcelExtractor(InputStream input)
/*  40:    */     throws IOException
/*  41:    */   {
/*  42: 73 */     open(input);
/*  43:    */   }
/*  44:    */   
/*  45:    */   public OldExcelExtractor(File f)
/*  46:    */     throws IOException
/*  47:    */   {
/*  48: 77 */     NPOIFSFileSystem poifs = null;
/*  49:    */     try
/*  50:    */     {
/*  51: 79 */       poifs = new NPOIFSFileSystem(f);
/*  52: 80 */       open(poifs);
/*  53: 81 */       this.toClose = poifs; return;
/*  54:    */     }
/*  55:    */     catch (OldExcelFormatException e) {}catch (NotOLE2FileException e) {}catch (IOException e)
/*  56:    */     {
/*  57: 89 */       throw e;
/*  58:    */     }
/*  59:    */     catch (RuntimeException e)
/*  60:    */     {
/*  61: 92 */       throw e;
/*  62:    */     }
/*  63:    */     finally
/*  64:    */     {
/*  65: 94 */       if (this.toClose == null) {
/*  66: 95 */         IOUtils.closeQuietly(poifs);
/*  67:    */       }
/*  68:    */     }
/*  69:100 */     FileInputStream biffStream = new FileInputStream(f);
/*  70:    */     try
/*  71:    */     {
/*  72:102 */       open(biffStream);
/*  73:    */     }
/*  74:    */     catch (IOException e)
/*  75:    */     {
/*  76:106 */       biffStream.close();
/*  77:107 */       throw e;
/*  78:    */     }
/*  79:    */     catch (RuntimeException e)
/*  80:    */     {
/*  81:111 */       biffStream.close();
/*  82:112 */       throw e;
/*  83:    */     }
/*  84:    */   }
/*  85:    */   
/*  86:    */   public OldExcelExtractor(NPOIFSFileSystem fs)
/*  87:    */     throws IOException
/*  88:    */   {
/*  89:117 */     open(fs);
/*  90:    */   }
/*  91:    */   
/*  92:    */   public OldExcelExtractor(DirectoryNode directory)
/*  93:    */     throws IOException
/*  94:    */   {
/*  95:121 */     open(directory);
/*  96:    */   }
/*  97:    */   
/*  98:    */   private void open(InputStream biffStream)
/*  99:    */     throws IOException
/* 100:    */   {
/* 101:125 */     BufferedInputStream bis = (biffStream instanceof BufferedInputStream) ? (BufferedInputStream)biffStream : new BufferedInputStream(biffStream, 8);
/* 102:129 */     if (NPOIFSFileSystem.hasPOIFSHeader(bis))
/* 103:    */     {
/* 104:130 */       NPOIFSFileSystem poifs = new NPOIFSFileSystem(bis);
/* 105:    */       try
/* 106:    */       {
/* 107:132 */         open(poifs);
/* 108:    */       }
/* 109:    */       finally
/* 110:    */       {
/* 111:134 */         poifs.close();
/* 112:    */       }
/* 113:    */     }
/* 114:    */     else
/* 115:    */     {
/* 116:137 */       this.ris = new RecordInputStream(bis);
/* 117:138 */       this.toClose = bis;
/* 118:139 */       prepare();
/* 119:    */     }
/* 120:    */   }
/* 121:    */   
/* 122:    */   private void open(NPOIFSFileSystem fs)
/* 123:    */     throws IOException
/* 124:    */   {
/* 125:144 */     open(fs.getRoot());
/* 126:    */   }
/* 127:    */   
/* 128:    */   private void open(DirectoryNode directory)
/* 129:    */     throws IOException
/* 130:    */   {
/* 131:    */     DocumentNode book;
/* 132:    */     try
/* 133:    */     {
/* 134:150 */       book = (DocumentNode)directory.getEntry("Book");
/* 135:    */     }
/* 136:    */     catch (FileNotFoundException e)
/* 137:    */     {
/* 138:153 */       book = (DocumentNode)directory.getEntry(org.apache.poi.hssf.model.InternalWorkbook.WORKBOOK_DIR_ENTRY_NAMES[0]);
/* 139:    */     }
/* 140:156 */     if (book == null) {
/* 141:157 */       throw new IOException("No Excel 5/95 Book stream found");
/* 142:    */     }
/* 143:160 */     this.ris = new RecordInputStream(directory.createDocumentInputStream(book));
/* 144:161 */     prepare();
/* 145:    */   }
/* 146:    */   
/* 147:    */   public static void main(String[] args)
/* 148:    */     throws IOException
/* 149:    */   {
/* 150:165 */     if (args.length < 1)
/* 151:    */     {
/* 152:166 */       System.err.println("Use:");
/* 153:167 */       System.err.println("   OldExcelExtractor <filename>");
/* 154:168 */       System.exit(1);
/* 155:    */     }
/* 156:170 */     OldExcelExtractor extractor = new OldExcelExtractor(new File(args[0]));
/* 157:171 */     System.out.println(extractor.getText());
/* 158:172 */     extractor.close();
/* 159:    */   }
/* 160:    */   
/* 161:    */   private void prepare()
/* 162:    */   {
/* 163:176 */     if (!this.ris.hasNextRecord()) {
/* 164:177 */       throw new IllegalArgumentException("File contains no records!");
/* 165:    */     }
/* 166:179 */     this.ris.nextRecord();
/* 167:    */     
/* 168:    */ 
/* 169:182 */     int bofSid = this.ris.getSid();
/* 170:183 */     switch (bofSid)
/* 171:    */     {
/* 172:    */     case 9: 
/* 173:185 */       this.biffVersion = 2;
/* 174:186 */       break;
/* 175:    */     case 521: 
/* 176:188 */       this.biffVersion = 3;
/* 177:189 */       break;
/* 178:    */     case 1033: 
/* 179:191 */       this.biffVersion = 4;
/* 180:192 */       break;
/* 181:    */     case 2057: 
/* 182:194 */       this.biffVersion = 5;
/* 183:195 */       break;
/* 184:    */     default: 
/* 185:197 */       throw new IllegalArgumentException("File does not begin with a BOF, found sid of " + bofSid);
/* 186:    */     }
/* 187:201 */     BOFRecord bof = new BOFRecord(this.ris);
/* 188:202 */     this.fileType = bof.getType();
/* 189:    */   }
/* 190:    */   
/* 191:    */   public int getBiffVersion()
/* 192:    */   {
/* 193:211 */     return this.biffVersion;
/* 194:    */   }
/* 195:    */   
/* 196:    */   public int getFileType()
/* 197:    */   {
/* 198:222 */     return this.fileType;
/* 199:    */   }
/* 200:    */   
/* 201:    */   public String getText()
/* 202:    */   {
/* 203:232 */     StringBuffer text = new StringBuffer();
/* 204:    */     
/* 205:    */ 
/* 206:235 */     CodepageRecord codepage = null;
/* 207:239 */     while (this.ris.hasNextRecord())
/* 208:    */     {
/* 209:240 */       int sid = this.ris.getNextSid();
/* 210:241 */       this.ris.nextRecord();
/* 211:243 */       switch (sid)
/* 212:    */       {
/* 213:    */       case 47: 
/* 214:245 */         throw new EncryptedDocumentException("Encryption not supported for Old Excel files");
/* 215:    */       case 133: 
/* 216:248 */         OldSheetRecord shr = new OldSheetRecord(this.ris);
/* 217:249 */         shr.setCodePage(codepage);
/* 218:250 */         text.append("Sheet: ");
/* 219:251 */         text.append(shr.getSheetname());
/* 220:252 */         text.append('\n');
/* 221:253 */         break;
/* 222:    */       case 4: 
/* 223:    */       case 516: 
/* 224:257 */         OldLabelRecord lr = new OldLabelRecord(this.ris);
/* 225:258 */         lr.setCodePage(codepage);
/* 226:259 */         text.append(lr.getValue());
/* 227:260 */         text.append('\n');
/* 228:261 */         break;
/* 229:    */       case 7: 
/* 230:    */       case 519: 
/* 231:264 */         OldStringRecord sr = new OldStringRecord(this.ris);
/* 232:265 */         sr.setCodePage(codepage);
/* 233:266 */         text.append(sr.getString());
/* 234:267 */         text.append('\n');
/* 235:268 */         break;
/* 236:    */       case 515: 
/* 237:271 */         NumberRecord nr = new NumberRecord(this.ris);
/* 238:272 */         handleNumericCell(text, nr.getValue());
/* 239:273 */         break;
/* 240:    */       case 6: 
/* 241:    */       case 518: 
/* 242:    */       case 1030: 
/* 243:278 */         if (this.biffVersion == 5)
/* 244:    */         {
/* 245:279 */           FormulaRecord fr = new FormulaRecord(this.ris);
/* 246:280 */           if (fr.getCachedResultType() == CellType.NUMERIC.getCode()) {
/* 247:281 */             handleNumericCell(text, fr.getValue());
/* 248:    */           }
/* 249:    */         }
/* 250:    */         else
/* 251:    */         {
/* 252:284 */           OldFormulaRecord fr = new OldFormulaRecord(this.ris);
/* 253:285 */           if (fr.getCachedResultType() == CellType.NUMERIC.getCode()) {
/* 254:286 */             handleNumericCell(text, fr.getValue());
/* 255:    */           }
/* 256:    */         }
/* 257:289 */         break;
/* 258:    */       case 638: 
/* 259:291 */         RKRecord rr = new RKRecord(this.ris);
/* 260:292 */         handleNumericCell(text, rr.getRKNumber());
/* 261:293 */         break;
/* 262:    */       case 66: 
/* 263:296 */         codepage = new CodepageRecord(this.ris);
/* 264:297 */         break;
/* 265:    */       default: 
/* 266:300 */         this.ris.readFully(new byte[this.ris.remaining()]);
/* 267:    */       }
/* 268:    */     }
/* 269:304 */     close();
/* 270:305 */     this.ris = null;
/* 271:    */     
/* 272:307 */     return text.toString();
/* 273:    */   }
/* 274:    */   
/* 275:    */   public void close()
/* 276:    */   {
/* 277:313 */     if (this.toClose != null)
/* 278:    */     {
/* 279:314 */       IOUtils.closeQuietly(this.toClose);
/* 280:315 */       this.toClose = null;
/* 281:    */     }
/* 282:    */   }
/* 283:    */   
/* 284:    */   protected void handleNumericCell(StringBuffer text, double value)
/* 285:    */   {
/* 286:321 */     text.append(value);
/* 287:322 */     text.append('\n');
/* 288:    */   }
/* 289:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.extractor.OldExcelExtractor
 * JD-Core Version:    0.7.0.1
 */