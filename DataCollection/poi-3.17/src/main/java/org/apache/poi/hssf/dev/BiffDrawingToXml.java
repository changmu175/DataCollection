/*   1:    */ package org.apache.poi.hssf.dev;
/*   2:    */ 
/*   3:    */ import java.io.FileInputStream;
/*   4:    */ import java.io.FileOutputStream;
/*   5:    */ import java.io.IOException;
/*   6:    */ import java.io.InputStream;
/*   7:    */ import java.io.OutputStream;
/*   8:    */ import java.io.PrintStream;
/*   9:    */ import java.util.ArrayList;
/*  10:    */ import java.util.List;
/*  11:    */ import org.apache.poi.ddf.EscherRecord;
/*  12:    */ import org.apache.poi.hssf.model.InternalWorkbook;
/*  13:    */ import org.apache.poi.hssf.record.DrawingGroupRecord;
/*  14:    */ import org.apache.poi.hssf.record.EscherAggregate;
/*  15:    */ import org.apache.poi.hssf.usermodel.HSSFPatriarch;
/*  16:    */ import org.apache.poi.hssf.usermodel.HSSFSheet;
/*  17:    */ import org.apache.poi.hssf.usermodel.HSSFWorkbook;
/*  18:    */ import org.apache.poi.util.StringUtil;
/*  19:    */ 
/*  20:    */ public class BiffDrawingToXml
/*  21:    */ {
/*  22:    */   private static final String SHEET_NAME_PARAM = "-sheet-name";
/*  23:    */   private static final String SHEET_INDEXES_PARAM = "-sheet-indexes";
/*  24:    */   private static final String EXCLUDE_WORKBOOK_RECORDS = "-exclude-workbook";
/*  25:    */   
/*  26:    */   private static int getAttributeIndex(String attribute, String[] params)
/*  27:    */   {
/*  28: 46 */     for (int i = 0; i < params.length; i++)
/*  29:    */     {
/*  30: 47 */       String param = params[i];
/*  31: 48 */       if (attribute.equals(param)) {
/*  32: 49 */         return i;
/*  33:    */       }
/*  34:    */     }
/*  35: 52 */     return -1;
/*  36:    */   }
/*  37:    */   
/*  38:    */   private static boolean isExcludeWorkbookRecords(String[] params)
/*  39:    */   {
/*  40: 56 */     return -1 != getAttributeIndex("-exclude-workbook", params);
/*  41:    */   }
/*  42:    */   
/*  43:    */   private static List<Integer> getIndexesByName(String[] params, HSSFWorkbook workbook)
/*  44:    */   {
/*  45: 60 */     List<Integer> list = new ArrayList();
/*  46: 61 */     int pos = getAttributeIndex("-sheet-name", params);
/*  47: 62 */     if (-1 != pos)
/*  48:    */     {
/*  49: 63 */       if (pos >= params.length) {
/*  50: 64 */         throw new IllegalArgumentException("sheet name param value was not specified");
/*  51:    */       }
/*  52: 66 */       String sheetName = params[(pos + 1)];
/*  53: 67 */       int sheetPos = workbook.getSheetIndex(sheetName);
/*  54: 68 */       if (-1 == sheetPos) {
/*  55: 69 */         throw new IllegalArgumentException("specified sheet name has not been found in xls file");
/*  56:    */       }
/*  57: 71 */       list.add(Integer.valueOf(sheetPos));
/*  58:    */     }
/*  59: 73 */     return list;
/*  60:    */   }
/*  61:    */   
/*  62:    */   private static List<Integer> getIndexesByIdArray(String[] params)
/*  63:    */   {
/*  64: 77 */     List<Integer> list = new ArrayList();
/*  65: 78 */     int pos = getAttributeIndex("-sheet-indexes", params);
/*  66: 79 */     if (-1 != pos)
/*  67:    */     {
/*  68: 80 */       if (pos >= params.length) {
/*  69: 81 */         throw new IllegalArgumentException("sheet list value was not specified");
/*  70:    */       }
/*  71: 83 */       String sheetParam = params[(pos + 1)];
/*  72: 84 */       String[] sheets = sheetParam.split(",");
/*  73: 85 */       for (String sheet : sheets) {
/*  74: 86 */         list.add(Integer.valueOf(Integer.parseInt(sheet)));
/*  75:    */       }
/*  76:    */     }
/*  77: 89 */     return list;
/*  78:    */   }
/*  79:    */   
/*  80:    */   private static List<Integer> getSheetsIndexes(String[] params, HSSFWorkbook workbook)
/*  81:    */   {
/*  82: 93 */     List<Integer> list = new ArrayList();
/*  83: 94 */     list.addAll(getIndexesByIdArray(params));
/*  84: 95 */     list.addAll(getIndexesByName(params, workbook));
/*  85: 96 */     if (0 == list.size())
/*  86:    */     {
/*  87: 97 */       int size = workbook.getNumberOfSheets();
/*  88: 98 */       for (int i = 0; i < size; i++) {
/*  89: 99 */         list.add(Integer.valueOf(i));
/*  90:    */       }
/*  91:    */     }
/*  92:102 */     return list;
/*  93:    */   }
/*  94:    */   
/*  95:    */   private static String getInputFileName(String[] params)
/*  96:    */   {
/*  97:106 */     return params[(params.length - 1)];
/*  98:    */   }
/*  99:    */   
/* 100:    */   private static String getOutputFileName(String input)
/* 101:    */   {
/* 102:110 */     if (input.contains("xls")) {
/* 103:111 */       return input.replace(".xls", ".xml");
/* 104:    */     }
/* 105:113 */     return input + ".xml";
/* 106:    */   }
/* 107:    */   
/* 108:    */   public static void main(String[] params)
/* 109:    */     throws IOException
/* 110:    */   {
/* 111:117 */     if (0 == params.length)
/* 112:    */     {
/* 113:118 */       System.out.println("Usage: BiffDrawingToXml [options] inputWorkbook");
/* 114:119 */       System.out.println("Options:");
/* 115:120 */       System.out.println("  -exclude-workbook            exclude workbook-level records");
/* 116:121 */       System.out.println("  -sheet-indexes   <indexes>   output sheets with specified indexes");
/* 117:122 */       System.out.println("  -sheet-namek  <names>        output sheets with specified name");
/* 118:123 */       return;
/* 119:    */     }
/* 120:125 */     String input = getInputFileName(params);
/* 121:126 */     FileInputStream inp = new FileInputStream(input);
/* 122:127 */     String output = getOutputFileName(input);
/* 123:128 */     FileOutputStream outputStream = new FileOutputStream(output);
/* 124:129 */     writeToFile(outputStream, inp, isExcludeWorkbookRecords(params), params);
/* 125:130 */     inp.close();
/* 126:131 */     outputStream.close();
/* 127:    */   }
/* 128:    */   
/* 129:    */   public static void writeToFile(OutputStream fos, InputStream xlsWorkbook, boolean excludeWorkbookRecords, String[] params)
/* 130:    */     throws IOException
/* 131:    */   {
/* 132:135 */     HSSFWorkbook workbook = new HSSFWorkbook(xlsWorkbook);
/* 133:136 */     InternalWorkbook internalWorkbook = workbook.getInternalWorkbook();
/* 134:137 */     DrawingGroupRecord r = (DrawingGroupRecord)internalWorkbook.findFirstRecordBySid((short)235);
/* 135:    */     
/* 136:139 */     StringBuilder builder = new StringBuilder();
/* 137:140 */     builder.append("<workbook>\n");
/* 138:141 */     String tab = "\t";
/* 139:142 */     if ((!excludeWorkbookRecords) && (r != null))
/* 140:    */     {
/* 141:143 */       r.decode();
/* 142:144 */       List<EscherRecord> escherRecords = r.getEscherRecords();
/* 143:145 */       for (EscherRecord record : escherRecords) {
/* 144:146 */         builder.append(record.toXml(tab));
/* 145:    */       }
/* 146:    */     }
/* 147:149 */     List<Integer> sheets = getSheetsIndexes(params, workbook);
/* 148:150 */     for (Integer i : sheets)
/* 149:    */     {
/* 150:151 */       HSSFPatriarch p = workbook.getSheetAt(i.intValue()).getDrawingPatriarch();
/* 151:152 */       if (p != null)
/* 152:    */       {
/* 153:153 */         builder.append(tab).append("<sheet").append(i).append(">\n");
/* 154:154 */         builder.append(p.getBoundAggregate().toXml(tab + "\t"));
/* 155:155 */         builder.append(tab).append("</sheet").append(i).append(">\n");
/* 156:    */       }
/* 157:    */     }
/* 158:158 */     builder.append("</workbook>\n");
/* 159:159 */     fos.write(builder.toString().getBytes(StringUtil.UTF8));
/* 160:160 */     fos.close();
/* 161:161 */     workbook.close();
/* 162:    */   }
/* 163:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.dev.BiffDrawingToXml
 * JD-Core Version:    0.7.0.1
 */