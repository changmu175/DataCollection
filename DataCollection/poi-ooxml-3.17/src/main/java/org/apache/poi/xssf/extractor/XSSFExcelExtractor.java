/*   1:    */ package org.apache.poi.xssf.extractor;
/*   2:    */ 
/*   3:    */ import java.io.IOException;
/*   4:    */ import java.io.PrintStream;
/*   5:    */ import java.util.Iterator;
/*   6:    */ import java.util.Locale;
/*   7:    */ import org.apache.poi.POIXMLTextExtractor;
/*   8:    */ import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
/*   9:    */ import org.apache.poi.openxml4j.opc.OPCPackage;
/*  10:    */ import org.apache.poi.ss.usermodel.Cell;
/*  11:    */ import org.apache.poi.ss.usermodel.CellStyle;
/*  12:    */ import org.apache.poi.ss.usermodel.CellType;
/*  13:    */ import org.apache.poi.ss.usermodel.Comment;
/*  14:    */ import org.apache.poi.ss.usermodel.DataFormatter;
/*  15:    */ import org.apache.poi.ss.usermodel.HeaderFooter;
/*  16:    */ import org.apache.poi.ss.usermodel.RichTextString;
/*  17:    */ import org.apache.poi.ss.usermodel.Row;
/*  18:    */ import org.apache.poi.ss.usermodel.Sheet;
/*  19:    */ import org.apache.poi.xssf.usermodel.XSSFCell;
/*  20:    */ import org.apache.poi.xssf.usermodel.XSSFDrawing;
/*  21:    */ import org.apache.poi.xssf.usermodel.XSSFRelation;
/*  22:    */ import org.apache.poi.xssf.usermodel.XSSFShape;
/*  23:    */ import org.apache.poi.xssf.usermodel.XSSFSheet;
/*  24:    */ import org.apache.poi.xssf.usermodel.XSSFSimpleShape;
/*  25:    */ import org.apache.poi.xssf.usermodel.XSSFWorkbook;
/*  26:    */ import org.apache.xmlbeans.XmlException;
/*  27:    */ 
/*  28:    */ public class XSSFExcelExtractor
/*  29:    */   extends POIXMLTextExtractor
/*  30:    */   implements org.apache.poi.ss.extractor.ExcelExtractor
/*  31:    */ {
/*  32: 49 */   public static final XSSFRelation[] SUPPORTED_TYPES = { XSSFRelation.WORKBOOK, XSSFRelation.MACRO_TEMPLATE_WORKBOOK, XSSFRelation.MACRO_ADDIN_WORKBOOK, XSSFRelation.TEMPLATE_WORKBOOK, XSSFRelation.MACROS_WORKBOOK };
/*  33:    */   private Locale locale;
/*  34:    */   private XSSFWorkbook workbook;
/*  35: 57 */   private boolean includeSheetNames = true;
/*  36:    */   private boolean formulasNotResults;
/*  37:    */   private boolean includeCellComments;
/*  38: 60 */   private boolean includeHeadersFooters = true;
/*  39: 61 */   private boolean includeTextBoxes = true;
/*  40:    */   
/*  41:    */   public XSSFExcelExtractor(OPCPackage container)
/*  42:    */     throws XmlException, OpenXML4JException, IOException
/*  43:    */   {
/*  44: 64 */     this(new XSSFWorkbook(container));
/*  45:    */   }
/*  46:    */   
/*  47:    */   public XSSFExcelExtractor(XSSFWorkbook workbook)
/*  48:    */   {
/*  49: 67 */     super(workbook);
/*  50: 68 */     this.workbook = workbook;
/*  51:    */   }
/*  52:    */   
/*  53:    */   public static void main(String[] args)
/*  54:    */     throws Exception
/*  55:    */   {
/*  56: 72 */     if (args.length < 1)
/*  57:    */     {
/*  58: 73 */       System.err.println("Use:");
/*  59: 74 */       System.err.println("  XSSFExcelExtractor <filename.xlsx>");
/*  60: 75 */       System.exit(1);
/*  61:    */     }
/*  62: 77 */     OPCPackage pkg = OPCPackage.create(args[0]);
/*  63: 78 */     POIXMLTextExtractor extractor = new XSSFExcelExtractor(pkg);
/*  64:    */     try
/*  65:    */     {
/*  66: 80 */       System.out.println(extractor.getText());
/*  67:    */     }
/*  68:    */     finally
/*  69:    */     {
/*  70: 82 */       extractor.close();
/*  71:    */     }
/*  72:    */   }
/*  73:    */   
/*  74:    */   public void setIncludeSheetNames(boolean includeSheetNames)
/*  75:    */   {
/*  76: 90 */     this.includeSheetNames = includeSheetNames;
/*  77:    */   }
/*  78:    */   
/*  79:    */   public void setFormulasNotResults(boolean formulasNotResults)
/*  80:    */   {
/*  81: 97 */     this.formulasNotResults = formulasNotResults;
/*  82:    */   }
/*  83:    */   
/*  84:    */   public void setIncludeCellComments(boolean includeCellComments)
/*  85:    */   {
/*  86:103 */     this.includeCellComments = includeCellComments;
/*  87:    */   }
/*  88:    */   
/*  89:    */   public void setIncludeHeadersFooters(boolean includeHeadersFooters)
/*  90:    */   {
/*  91:109 */     this.includeHeadersFooters = includeHeadersFooters;
/*  92:    */   }
/*  93:    */   
/*  94:    */   public void setIncludeTextBoxes(boolean includeTextBoxes)
/*  95:    */   {
/*  96:116 */     this.includeTextBoxes = includeTextBoxes;
/*  97:    */   }
/*  98:    */   
/*  99:    */   public void setLocale(Locale locale)
/* 100:    */   {
/* 101:123 */     this.locale = locale;
/* 102:    */   }
/* 103:    */   
/* 104:    */   public String getText()
/* 105:    */   {
/* 106:    */     DataFormatter formatter;
/* 107:    */     DataFormatter formatter;
/* 108:132 */     if (this.locale == null) {
/* 109:133 */       formatter = new DataFormatter();
/* 110:    */     } else {
/* 111:135 */       formatter = new DataFormatter(this.locale);
/* 112:    */     }
/* 113:138 */     StringBuffer text = new StringBuffer();
/* 114:139 */     for (Sheet sh : this.workbook)
/* 115:    */     {
/* 116:140 */       XSSFSheet sheet = (XSSFSheet)sh;
/* 117:141 */       if (this.includeSheetNames) {
/* 118:142 */         text.append(sheet.getSheetName()).append("\n");
/* 119:    */       }
/* 120:146 */       if (this.includeHeadersFooters)
/* 121:    */       {
/* 122:147 */         text.append(extractHeaderFooter(sheet.getFirstHeader()));
/* 123:    */         
/* 124:    */ 
/* 125:150 */         text.append(extractHeaderFooter(sheet.getOddHeader()));
/* 126:    */         
/* 127:    */ 
/* 128:153 */         text.append(extractHeaderFooter(sheet.getEvenHeader()));
/* 129:    */       }
/* 130:159 */       for (Object rawR : sheet)
/* 131:    */       {
/* 132:160 */         Row row = (Row)rawR;
/* 133:161 */         for (Iterator<Cell> ri = row.cellIterator(); ri.hasNext();)
/* 134:    */         {
/* 135:162 */           Cell cell = (Cell)ri.next();
/* 136:165 */           if (cell.getCellTypeEnum() == CellType.FORMULA)
/* 137:    */           {
/* 138:166 */             if (this.formulasNotResults)
/* 139:    */             {
/* 140:167 */               String contents = cell.getCellFormula();
/* 141:168 */               checkMaxTextSize(text, contents);
/* 142:169 */               text.append(contents);
/* 143:    */             }
/* 144:171 */             else if (cell.getCachedFormulaResultTypeEnum() == CellType.STRING)
/* 145:    */             {
/* 146:172 */               handleStringCell(text, cell);
/* 147:    */             }
/* 148:    */             else
/* 149:    */             {
/* 150:174 */               handleNonStringCell(text, cell, formatter);
/* 151:    */             }
/* 152:    */           }
/* 153:177 */           else if (cell.getCellTypeEnum() == CellType.STRING) {
/* 154:178 */             handleStringCell(text, cell);
/* 155:    */           } else {
/* 156:180 */             handleNonStringCell(text, cell, formatter);
/* 157:    */           }
/* 158:184 */           Comment comment = cell.getCellComment();
/* 159:185 */           if ((this.includeCellComments) && (comment != null))
/* 160:    */           {
/* 161:188 */             String commentText = comment.getString().getString().replace('\n', ' ');
/* 162:189 */             checkMaxTextSize(text, commentText);
/* 163:190 */             text.append(" Comment by ").append(comment.getAuthor()).append(": ").append(commentText);
/* 164:    */           }
/* 165:193 */           if (ri.hasNext()) {
/* 166:194 */             text.append("\t");
/* 167:    */           }
/* 168:    */         }
/* 169:197 */         text.append("\n");
/* 170:    */       }
/* 171:201 */       if (this.includeTextBoxes)
/* 172:    */       {
/* 173:202 */         XSSFDrawing drawing = sheet.getDrawingPatriarch();
/* 174:203 */         if (drawing != null) {
/* 175:204 */           for (XSSFShape shape : drawing.getShapes()) {
/* 176:205 */             if ((shape instanceof XSSFSimpleShape))
/* 177:    */             {
/* 178:206 */               String boxText = ((XSSFSimpleShape)shape).getText();
/* 179:207 */               if (boxText.length() > 0)
/* 180:    */               {
/* 181:208 */                 text.append(boxText);
/* 182:209 */                 text.append('\n');
/* 183:    */               }
/* 184:    */             }
/* 185:    */           }
/* 186:    */         }
/* 187:    */       }
/* 188:216 */       if (this.includeHeadersFooters)
/* 189:    */       {
/* 190:217 */         text.append(extractHeaderFooter(sheet.getFirstFooter()));
/* 191:    */         
/* 192:    */ 
/* 193:220 */         text.append(extractHeaderFooter(sheet.getOddFooter()));
/* 194:    */         
/* 195:    */ 
/* 196:223 */         text.append(extractHeaderFooter(sheet.getEvenFooter()));
/* 197:    */       }
/* 198:    */     }
/* 199:229 */     return text.toString();
/* 200:    */   }
/* 201:    */   
/* 202:    */   private void handleStringCell(StringBuffer text, Cell cell)
/* 203:    */   {
/* 204:233 */     String contents = cell.getRichStringCellValue().getString();
/* 205:234 */     checkMaxTextSize(text, contents);
/* 206:235 */     text.append(contents);
/* 207:    */   }
/* 208:    */   
/* 209:    */   private void handleNonStringCell(StringBuffer text, Cell cell, DataFormatter formatter)
/* 210:    */   {
/* 211:239 */     CellType type = cell.getCellTypeEnum();
/* 212:240 */     if (type == CellType.FORMULA) {
/* 213:241 */       type = cell.getCachedFormulaResultTypeEnum();
/* 214:    */     }
/* 215:244 */     if (type == CellType.NUMERIC)
/* 216:    */     {
/* 217:245 */       CellStyle cs = cell.getCellStyle();
/* 218:247 */       if ((cs != null) && (cs.getDataFormatString() != null))
/* 219:    */       {
/* 220:248 */         String contents = formatter.formatRawCellContents(cell.getNumericCellValue(), cs.getDataFormat(), cs.getDataFormatString());
/* 221:    */         
/* 222:250 */         checkMaxTextSize(text, contents);
/* 223:251 */         text.append(contents);
/* 224:252 */         return;
/* 225:    */       }
/* 226:    */     }
/* 227:257 */     String contents = ((XSSFCell)cell).getRawValue();
/* 228:258 */     if (contents != null)
/* 229:    */     {
/* 230:259 */       checkMaxTextSize(text, contents);
/* 231:260 */       text.append(contents);
/* 232:    */     }
/* 233:    */   }
/* 234:    */   
/* 235:    */   private String extractHeaderFooter(HeaderFooter hf)
/* 236:    */   {
/* 237:265 */     return org.apache.poi.hssf.extractor.ExcelExtractor._extractHeaderFooter(hf);
/* 238:    */   }
/* 239:    */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xssf.extractor.XSSFExcelExtractor
 * JD-Core Version:    0.7.0.1
 */