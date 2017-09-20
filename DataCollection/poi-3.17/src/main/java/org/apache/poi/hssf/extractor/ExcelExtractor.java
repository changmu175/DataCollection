/*   1:    */ package org.apache.poi.hssf.extractor;
/*   2:    */ 
/*   3:    */ import java.io.File;
/*   4:    */ import java.io.FileInputStream;
/*   5:    */ import java.io.IOException;
/*   6:    */ import java.io.InputStream;
/*   7:    */ import java.io.PrintStream;
/*   8:    */ import java.util.Locale;
/*   9:    */ import org.apache.poi.POIOLE2TextExtractor;
/*  10:    */ import org.apache.poi.hssf.usermodel.HSSFCell;
/*  11:    */ import org.apache.poi.hssf.usermodel.HSSFCellStyle;
/*  12:    */ import org.apache.poi.hssf.usermodel.HSSFComment;
/*  13:    */ import org.apache.poi.hssf.usermodel.HSSFDataFormatter;
/*  14:    */ import org.apache.poi.hssf.usermodel.HSSFRichTextString;
/*  15:    */ import org.apache.poi.hssf.usermodel.HSSFRow;
/*  16:    */ import org.apache.poi.hssf.usermodel.HSSFSheet;
/*  17:    */ import org.apache.poi.hssf.usermodel.HSSFWorkbook;
/*  18:    */ import org.apache.poi.poifs.filesystem.DirectoryNode;
/*  19:    */ import org.apache.poi.poifs.filesystem.POIFSFileSystem;
/*  20:    */ import org.apache.poi.ss.formula.eval.ErrorEval;
/*  21:    */ import org.apache.poi.ss.usermodel.HeaderFooter;
/*  22:    */ import org.apache.poi.ss.usermodel.Row.MissingCellPolicy;
/*  23:    */ 
/*  24:    */ public class ExcelExtractor
/*  25:    */   extends POIOLE2TextExtractor
/*  26:    */   implements org.apache.poi.ss.extractor.ExcelExtractor
/*  27:    */ {
/*  28:    */   private final HSSFWorkbook _wb;
/*  29:    */   private final HSSFDataFormatter _formatter;
/*  30: 59 */   private boolean _includeSheetNames = true;
/*  31: 60 */   private boolean _shouldEvaluateFormulas = true;
/*  32: 61 */   private boolean _includeCellComments = false;
/*  33: 62 */   private boolean _includeBlankCells = false;
/*  34: 63 */   private boolean _includeHeadersFooters = true;
/*  35:    */   
/*  36:    */   public ExcelExtractor(HSSFWorkbook wb)
/*  37:    */   {
/*  38: 66 */     super(wb);
/*  39: 67 */     this._wb = wb;
/*  40: 68 */     this._formatter = new HSSFDataFormatter();
/*  41:    */   }
/*  42:    */   
/*  43:    */   public ExcelExtractor(POIFSFileSystem fs)
/*  44:    */     throws IOException
/*  45:    */   {
/*  46: 71 */     this(fs.getRoot());
/*  47:    */   }
/*  48:    */   
/*  49:    */   public ExcelExtractor(DirectoryNode dir)
/*  50:    */     throws IOException
/*  51:    */   {
/*  52: 74 */     this(new HSSFWorkbook(dir, true));
/*  53:    */   }
/*  54:    */   
/*  55:    */   private static final class CommandParseException
/*  56:    */     extends Exception
/*  57:    */   {
/*  58:    */     public CommandParseException(String msg)
/*  59:    */     {
/*  60: 79 */       super();
/*  61:    */     }
/*  62:    */   }
/*  63:    */   
/*  64:    */   private static final class CommandArgs
/*  65:    */   {
/*  66:    */     private final boolean _requestHelp;
/*  67:    */     private final File _inputFile;
/*  68:    */     private final boolean _showSheetNames;
/*  69:    */     private final boolean _evaluateFormulas;
/*  70:    */     private final boolean _showCellComments;
/*  71:    */     private final boolean _showBlankCells;
/*  72:    */     private final boolean _headersFooters;
/*  73:    */     
/*  74:    */     public CommandArgs(String[] args)
/*  75:    */       throws CommandParseException
/*  76:    */     {
/*  77: 91 */       int nArgs = args.length;
/*  78: 92 */       File inputFile = null;
/*  79: 93 */       boolean requestHelp = false;
/*  80: 94 */       boolean showSheetNames = true;
/*  81: 95 */       boolean evaluateFormulas = true;
/*  82: 96 */       boolean showCellComments = false;
/*  83: 97 */       boolean showBlankCells = false;
/*  84: 98 */       boolean headersFooters = true;
/*  85: 99 */       for (int i = 0; i < nArgs; i++)
/*  86:    */       {
/*  87:100 */         String arg = args[i];
/*  88:101 */         if ("-help".equalsIgnoreCase(arg))
/*  89:    */         {
/*  90:102 */           requestHelp = true;
/*  91:103 */           break;
/*  92:    */         }
/*  93:105 */         if ("-i".equals(arg))
/*  94:    */         {
/*  95:107 */           i++;
/*  96:107 */           if (i >= nArgs) {
/*  97:108 */             throw new CommandParseException("Expected filename after '-i'");
/*  98:    */           }
/*  99:110 */           arg = args[i];
/* 100:111 */           if (inputFile != null) {
/* 101:112 */             throw new CommandParseException("Only one input file can be supplied");
/* 102:    */           }
/* 103:114 */           inputFile = new File(arg);
/* 104:115 */           if (!inputFile.exists()) {
/* 105:116 */             throw new CommandParseException("Specified input file '" + arg + "' does not exist");
/* 106:    */           }
/* 107:118 */           if (inputFile.isDirectory()) {
/* 108:119 */             throw new CommandParseException("Specified input file '" + arg + "' is a directory");
/* 109:    */           }
/* 110:    */         }
/* 111:123 */         else if ("--show-sheet-names".equals(arg))
/* 112:    */         {
/* 113:124 */           showSheetNames = parseBoolArg(args, ++i);
/* 114:    */         }
/* 115:127 */         else if ("--evaluate-formulas".equals(arg))
/* 116:    */         {
/* 117:128 */           evaluateFormulas = parseBoolArg(args, ++i);
/* 118:    */         }
/* 119:131 */         else if ("--show-comments".equals(arg))
/* 120:    */         {
/* 121:132 */           showCellComments = parseBoolArg(args, ++i);
/* 122:    */         }
/* 123:135 */         else if ("--show-blanks".equals(arg))
/* 124:    */         {
/* 125:136 */           showBlankCells = parseBoolArg(args, ++i);
/* 126:    */         }
/* 127:139 */         else if ("--headers-footers".equals(arg))
/* 128:    */         {
/* 129:140 */           headersFooters = parseBoolArg(args, ++i);
/* 130:    */         }
/* 131:    */         else
/* 132:    */         {
/* 133:143 */           throw new CommandParseException("Invalid argument '" + arg + "'");
/* 134:    */         }
/* 135:    */       }
/* 136:145 */       this._requestHelp = requestHelp;
/* 137:146 */       this._inputFile = inputFile;
/* 138:147 */       this._showSheetNames = showSheetNames;
/* 139:148 */       this._evaluateFormulas = evaluateFormulas;
/* 140:149 */       this._showCellComments = showCellComments;
/* 141:150 */       this._showBlankCells = showBlankCells;
/* 142:151 */       this._headersFooters = headersFooters;
/* 143:    */     }
/* 144:    */     
/* 145:    */     private static boolean parseBoolArg(String[] args, int i)
/* 146:    */       throws CommandParseException
/* 147:    */     {
/* 148:154 */       if (i >= args.length) {
/* 149:155 */         throw new CommandParseException("Expected value after '" + args[(i - 1)] + "'");
/* 150:    */       }
/* 151:157 */       String value = args[i].toUpperCase(Locale.ROOT);
/* 152:158 */       if (("Y".equals(value)) || ("YES".equals(value)) || ("ON".equals(value)) || ("TRUE".equals(value))) {
/* 153:159 */         return true;
/* 154:    */       }
/* 155:161 */       if (("N".equals(value)) || ("NO".equals(value)) || ("OFF".equals(value)) || ("FALSE".equals(value))) {
/* 156:162 */         return false;
/* 157:    */       }
/* 158:164 */       throw new CommandParseException("Invalid value '" + args[i] + "' for '" + args[(i - 1)] + "'. Expected 'Y' or 'N'");
/* 159:    */     }
/* 160:    */     
/* 161:    */     public boolean isRequestHelp()
/* 162:    */     {
/* 163:167 */       return this._requestHelp;
/* 164:    */     }
/* 165:    */     
/* 166:    */     public File getInputFile()
/* 167:    */     {
/* 168:170 */       return this._inputFile;
/* 169:    */     }
/* 170:    */     
/* 171:    */     public boolean shouldShowSheetNames()
/* 172:    */     {
/* 173:173 */       return this._showSheetNames;
/* 174:    */     }
/* 175:    */     
/* 176:    */     public boolean shouldEvaluateFormulas()
/* 177:    */     {
/* 178:176 */       return this._evaluateFormulas;
/* 179:    */     }
/* 180:    */     
/* 181:    */     public boolean shouldShowCellComments()
/* 182:    */     {
/* 183:179 */       return this._showCellComments;
/* 184:    */     }
/* 185:    */     
/* 186:    */     public boolean shouldShowBlankCells()
/* 187:    */     {
/* 188:182 */       return this._showBlankCells;
/* 189:    */     }
/* 190:    */     
/* 191:    */     public boolean shouldIncludeHeadersFooters()
/* 192:    */     {
/* 193:185 */       return this._headersFooters;
/* 194:    */     }
/* 195:    */   }
/* 196:    */   
/* 197:    */   private static void printUsageMessage(PrintStream ps)
/* 198:    */   {
/* 199:190 */     ps.println("Use:");
/* 200:191 */     ps.println("    " + ExcelExtractor.class.getName() + " [<flag> <value> [<flag> <value> [...]]] [-i <filename.xls>]");
/* 201:192 */     ps.println("       -i <filename.xls> specifies input file (default is to use stdin)");
/* 202:193 */     ps.println("       Flags can be set on or off by using the values 'Y' or 'N'.");
/* 203:194 */     ps.println("       Following are available flags and their default values:");
/* 204:195 */     ps.println("       --show-sheet-names  Y");
/* 205:196 */     ps.println("       --evaluate-formulas Y");
/* 206:197 */     ps.println("       --show-comments     N");
/* 207:198 */     ps.println("       --show-blanks       Y");
/* 208:199 */     ps.println("       --headers-footers   Y");
/* 209:    */   }
/* 210:    */   
/* 211:    */   public static void main(String[] args)
/* 212:    */     throws IOException
/* 213:    */   {
/* 214:    */     CommandArgs cmdArgs;
/* 215:    */     try
/* 216:    */     {
/* 217:213 */       cmdArgs = new CommandArgs(args);
/* 218:    */     }
/* 219:    */     catch (CommandParseException e)
/* 220:    */     {
/* 221:215 */       System.err.println(e.getMessage());
/* 222:216 */       printUsageMessage(System.err);
/* 223:217 */       System.exit(1);
/* 224:218 */       return;
/* 225:    */     }
/* 226:221 */     if (cmdArgs.isRequestHelp())
/* 227:    */     {
/* 228:222 */       printUsageMessage(System.out); return;
/* 229:    */     }
/* 230:    */     InputStream is;
/* 231:    */     InputStream is;
/* 232:227 */     if (cmdArgs.getInputFile() == null) {
/* 233:228 */       is = System.in;
/* 234:    */     } else {
/* 235:230 */       is = new FileInputStream(cmdArgs.getInputFile());
/* 236:    */     }
/* 237:232 */     HSSFWorkbook wb = new HSSFWorkbook(is);
/* 238:233 */     is.close();
/* 239:    */     
/* 240:235 */     ExcelExtractor extractor = new ExcelExtractor(wb);
/* 241:236 */     extractor.setIncludeSheetNames(cmdArgs.shouldShowSheetNames());
/* 242:237 */     extractor.setFormulasNotResults(!cmdArgs.shouldEvaluateFormulas());
/* 243:238 */     extractor.setIncludeCellComments(cmdArgs.shouldShowCellComments());
/* 244:239 */     extractor.setIncludeBlankCells(cmdArgs.shouldShowBlankCells());
/* 245:240 */     extractor.setIncludeHeadersFooters(cmdArgs.shouldIncludeHeadersFooters());
/* 246:241 */     System.out.println(extractor.getText());
/* 247:242 */     extractor.close();
/* 248:243 */     wb.close();
/* 249:    */   }
/* 250:    */   
/* 251:    */   public void setIncludeSheetNames(boolean includeSheetNames)
/* 252:    */   {
/* 253:248 */     this._includeSheetNames = includeSheetNames;
/* 254:    */   }
/* 255:    */   
/* 256:    */   public void setFormulasNotResults(boolean formulasNotResults)
/* 257:    */   {
/* 258:253 */     this._shouldEvaluateFormulas = (!formulasNotResults);
/* 259:    */   }
/* 260:    */   
/* 261:    */   public void setIncludeCellComments(boolean includeCellComments)
/* 262:    */   {
/* 263:258 */     this._includeCellComments = includeCellComments;
/* 264:    */   }
/* 265:    */   
/* 266:    */   public void setIncludeBlankCells(boolean includeBlankCells)
/* 267:    */   {
/* 268:269 */     this._includeBlankCells = includeBlankCells;
/* 269:    */   }
/* 270:    */   
/* 271:    */   public void setIncludeHeadersFooters(boolean includeHeadersFooters)
/* 272:    */   {
/* 273:274 */     this._includeHeadersFooters = includeHeadersFooters;
/* 274:    */   }
/* 275:    */   
/* 276:    */   public String getText()
/* 277:    */   {
/* 278:279 */     StringBuffer text = new StringBuffer();
/* 279:    */     
/* 280:    */ 
/* 281:    */ 
/* 282:283 */     this._wb.setMissingCellPolicy(Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
/* 283:286 */     for (int i = 0; i < this._wb.getNumberOfSheets(); i++)
/* 284:    */     {
/* 285:287 */       HSSFSheet sheet = this._wb.getSheetAt(i);
/* 286:288 */       if (sheet != null)
/* 287:    */       {
/* 288:290 */         if (this._includeSheetNames)
/* 289:    */         {
/* 290:291 */           String name = this._wb.getSheetName(i);
/* 291:292 */           if (name != null)
/* 292:    */           {
/* 293:293 */             text.append(name);
/* 294:294 */             text.append("\n");
/* 295:    */           }
/* 296:    */         }
/* 297:299 */         if (this._includeHeadersFooters) {
/* 298:300 */           text.append(_extractHeaderFooter(sheet.getHeader()));
/* 299:    */         }
/* 300:303 */         int firstRow = sheet.getFirstRowNum();
/* 301:304 */         int lastRow = sheet.getLastRowNum();
/* 302:305 */         for (int j = firstRow; j <= lastRow; j++)
/* 303:    */         {
/* 304:306 */           HSSFRow row = sheet.getRow(j);
/* 305:307 */           if (row != null)
/* 306:    */           {
/* 307:310 */             int firstCell = row.getFirstCellNum();
/* 308:311 */             int lastCell = row.getLastCellNum();
/* 309:312 */             if (this._includeBlankCells) {
/* 310:313 */               firstCell = 0;
/* 311:    */             }
/* 312:316 */             for (int k = firstCell; k < lastCell; k++)
/* 313:    */             {
/* 314:317 */               HSSFCell cell = row.getCell(k);
/* 315:318 */               boolean outputContents = true;
/* 316:320 */               if (cell == null)
/* 317:    */               {
/* 318:322 */                 outputContents = this._includeBlankCells;
/* 319:    */               }
/* 320:    */               else
/* 321:    */               {
/* 322:324 */                 switch (1.$SwitchMap$org$apache$poi$ss$usermodel$CellType[cell.getCellTypeEnum().ordinal()])
/* 323:    */                 {
/* 324:    */                 case 1: 
/* 325:326 */                   text.append(cell.getRichStringCellValue().getString());
/* 326:327 */                   break;
/* 327:    */                 case 2: 
/* 328:329 */                   text.append(this._formatter.formatCellValue(cell));
/* 329:330 */                   break;
/* 330:    */                 case 3: 
/* 331:332 */                   text.append(cell.getBooleanCellValue());
/* 332:333 */                   break;
/* 333:    */                 case 4: 
/* 334:335 */                   text.append(ErrorEval.getText(cell.getErrorCellValue()));
/* 335:336 */                   break;
/* 336:    */                 case 5: 
/* 337:338 */                   if (!this._shouldEvaluateFormulas) {
/* 338:339 */                     text.append(cell.getCellFormula());
/* 339:    */                   } else {
/* 340:341 */                     switch (1.$SwitchMap$org$apache$poi$ss$usermodel$CellType[cell.getCachedFormulaResultTypeEnum().ordinal()])
/* 341:    */                     {
/* 342:    */                     case 1: 
/* 343:343 */                       HSSFRichTextString str = cell.getRichStringCellValue();
/* 344:344 */                       if ((str != null) && (str.length() > 0)) {
/* 345:345 */                         text.append(str);
/* 346:    */                       }
/* 347:    */                       break;
/* 348:    */                     case 2: 
/* 349:349 */                       HSSFCellStyle style = cell.getCellStyle();
/* 350:350 */                       double nVal = cell.getNumericCellValue();
/* 351:351 */                       short df = style.getDataFormat();
/* 352:352 */                       String dfs = style.getDataFormatString();
/* 353:353 */                       text.append(this._formatter.formatRawCellContents(nVal, df, dfs));
/* 354:354 */                       break;
/* 355:    */                     case 3: 
/* 356:356 */                       text.append(cell.getBooleanCellValue());
/* 357:357 */                       break;
/* 358:    */                     case 4: 
/* 359:359 */                       text.append(ErrorEval.getText(cell.getErrorCellValue()));
/* 360:360 */                       break;
/* 361:    */                     default: 
/* 362:362 */                       throw new IllegalStateException("Unexpected cell cached formula result type: " + cell.getCachedFormulaResultTypeEnum());
/* 363:    */                     }
/* 364:    */                   }
/* 365:366 */                   break;
/* 366:    */                 default: 
/* 367:368 */                   throw new RuntimeException("Unexpected cell type (" + cell.getCellTypeEnum() + ")");
/* 368:    */                 }
/* 369:372 */                 HSSFComment comment = cell.getCellComment();
/* 370:373 */                 if ((this._includeCellComments) && (comment != null))
/* 371:    */                 {
/* 372:376 */                   String commentText = comment.getString().getString().replace('\n', ' ');
/* 373:377 */                   text.append(" Comment by " + comment.getAuthor() + ": " + commentText);
/* 374:    */                 }
/* 375:    */               }
/* 376:382 */               if ((outputContents) && (k < lastCell - 1)) {
/* 377:383 */                 text.append("\t");
/* 378:    */               }
/* 379:    */             }
/* 380:388 */             text.append("\n");
/* 381:    */           }
/* 382:    */         }
/* 383:392 */         if (this._includeHeadersFooters) {
/* 384:393 */           text.append(_extractHeaderFooter(sheet.getFooter()));
/* 385:    */         }
/* 386:    */       }
/* 387:    */     }
/* 388:397 */     return text.toString();
/* 389:    */   }
/* 390:    */   
/* 391:    */   public static String _extractHeaderFooter(HeaderFooter hf)
/* 392:    */   {
/* 393:401 */     StringBuffer text = new StringBuffer();
/* 394:403 */     if (hf.getLeft() != null) {
/* 395:404 */       text.append(hf.getLeft());
/* 396:    */     }
/* 397:406 */     if (hf.getCenter() != null)
/* 398:    */     {
/* 399:407 */       if (text.length() > 0) {
/* 400:408 */         text.append("\t");
/* 401:    */       }
/* 402:409 */       text.append(hf.getCenter());
/* 403:    */     }
/* 404:411 */     if (hf.getRight() != null)
/* 405:    */     {
/* 406:412 */       if (text.length() > 0) {
/* 407:413 */         text.append("\t");
/* 408:    */       }
/* 409:414 */       text.append(hf.getRight());
/* 410:    */     }
/* 411:416 */     if (text.length() > 0) {
/* 412:417 */       text.append("\n");
/* 413:    */     }
/* 414:419 */     return text.toString();
/* 415:    */   }
/* 416:    */ }



/* Location:           F:\poi-3.17.jar

 * Qualified Name:     org.apache.poi.hssf.extractor.ExcelExtractor

 * JD-Core Version:    0.7.0.1

 */