/*   1:    */ package org.apache.poi.hssf.dev;
/*   2:    */ 
/*   3:    */ import java.io.File;
/*   4:    */ import java.io.IOException;
/*   5:    */ import java.io.InputStream;
/*   6:    */ import java.io.PrintStream;
/*   7:    */ import java.util.List;
/*   8:    */ import org.apache.poi.hssf.model.HSSFFormulaParser;
/*   9:    */ import org.apache.poi.hssf.record.FormulaRecord;
/*  10:    */ import org.apache.poi.hssf.record.Record;
/*  11:    */ import org.apache.poi.hssf.record.RecordFactory;
/*  12:    */ import org.apache.poi.hssf.usermodel.HSSFWorkbook;
/*  13:    */ import org.apache.poi.poifs.filesystem.NPOIFSFileSystem;
/*  14:    */ import org.apache.poi.ss.formula.ptg.ExpPtg;
/*  15:    */ import org.apache.poi.ss.formula.ptg.FuncPtg;
/*  16:    */ import org.apache.poi.ss.formula.ptg.Ptg;
/*  17:    */ 
/*  18:    */ public class FormulaViewer
/*  19:    */ {
/*  20:    */   private String file;
/*  21: 45 */   private boolean list = false;
/*  22:    */   
/*  23:    */   public void run()
/*  24:    */     throws IOException
/*  25:    */   {
/*  26: 59 */     NPOIFSFileSystem fs = new NPOIFSFileSystem(new File(this.file), true);
/*  27:    */     try
/*  28:    */     {
/*  29: 61 */       InputStream is = BiffViewer.getPOIFSInputStream(fs);
/*  30:    */       try
/*  31:    */       {
/*  32: 63 */         List<Record> records = RecordFactory.createRecords(is);
/*  33: 65 */         for (Record record : records) {
/*  34: 66 */           if (record.getSid() == 6) {
/*  35: 67 */             if (this.list) {
/*  36: 68 */               listFormula((FormulaRecord)record);
/*  37:    */             } else {
/*  38: 70 */               parseFormulaRecord((FormulaRecord)record);
/*  39:    */             }
/*  40:    */           }
/*  41:    */         }
/*  42:    */       }
/*  43:    */       finally {}
/*  44:    */     }
/*  45:    */     finally
/*  46:    */     {
/*  47: 78 */       fs.close();
/*  48:    */     }
/*  49:    */   }
/*  50:    */   
/*  51:    */   private void listFormula(FormulaRecord record)
/*  52:    */   {
/*  53: 83 */     String sep = "~";
/*  54: 84 */     Ptg[] tokens = record.getParsedExpression();
/*  55:    */     
/*  56: 86 */     int numptgs = tokens.length;
/*  57:    */     
/*  58: 88 */     Ptg token = tokens[(numptgs - 1)];
/*  59:    */     String numArg;
/*  60:    */     String numArg;
/*  61: 89 */     if ((token instanceof FuncPtg)) {
/*  62: 90 */       numArg = String.valueOf(numptgs - 1);
/*  63:    */     } else {
/*  64: 92 */       numArg = String.valueOf(-1);
/*  65:    */     }
/*  66: 95 */     StringBuilder buf = new StringBuilder();
/*  67: 97 */     if ((token instanceof ExpPtg)) {
/*  68: 97 */       return;
/*  69:    */     }
/*  70: 98 */     buf.append(token.toFormulaString());
/*  71: 99 */     buf.append(sep);
/*  72:100 */     switch (token.getPtgClass())
/*  73:    */     {
/*  74:    */     case 0: 
/*  75:102 */       buf.append("REF");
/*  76:103 */       break;
/*  77:    */     case 32: 
/*  78:105 */       buf.append("VALUE");
/*  79:106 */       break;
/*  80:    */     case 64: 
/*  81:108 */       buf.append("ARRAY");
/*  82:109 */       break;
/*  83:    */     default: 
/*  84:111 */       throwInvalidRVAToken(token);
/*  85:    */     }
/*  86:114 */     buf.append(sep);
/*  87:115 */     if (numptgs > 1)
/*  88:    */     {
/*  89:116 */       token = tokens[(numptgs - 2)];
/*  90:117 */       switch (token.getPtgClass())
/*  91:    */       {
/*  92:    */       case 0: 
/*  93:119 */         buf.append("REF");
/*  94:120 */         break;
/*  95:    */       case 32: 
/*  96:122 */         buf.append("VALUE");
/*  97:123 */         break;
/*  98:    */       case 64: 
/*  99:125 */         buf.append("ARRAY");
/* 100:126 */         break;
/* 101:    */       default: 
/* 102:128 */         throwInvalidRVAToken(token); break;
/* 103:    */       }
/* 104:    */     }
/* 105:    */     else
/* 106:    */     {
/* 107:131 */       buf.append("VALUE");
/* 108:    */     }
/* 109:133 */     buf.append(sep);
/* 110:134 */     buf.append(numArg);
/* 111:135 */     System.out.println(buf);
/* 112:    */   }
/* 113:    */   
/* 114:    */   public void parseFormulaRecord(FormulaRecord record)
/* 115:    */   {
/* 116:145 */     System.out.println("==============================");
/* 117:146 */     System.out.print("row = " + record.getRow());
/* 118:147 */     System.out.println(", col = " + record.getColumn());
/* 119:148 */     System.out.println("value = " + record.getValue());
/* 120:149 */     System.out.print("xf = " + record.getXFIndex());
/* 121:150 */     System.out.print(", number of ptgs = " + record.getParsedExpression().length);
/* 122:    */     
/* 123:152 */     System.out.println(", options = " + record.getOptions());
/* 124:153 */     System.out.println("RPN List = " + formulaString(record));
/* 125:154 */     System.out.println("Formula text = " + composeFormula(record));
/* 126:    */   }
/* 127:    */   
/* 128:    */   private String formulaString(FormulaRecord record)
/* 129:    */   {
/* 130:159 */     StringBuilder buf = new StringBuilder();
/* 131:160 */     Ptg[] tokens = record.getParsedExpression();
/* 132:161 */     for (Ptg token : tokens)
/* 133:    */     {
/* 134:162 */       buf.append(token.toFormulaString());
/* 135:163 */       switch (token.getPtgClass())
/* 136:    */       {
/* 137:    */       case 0: 
/* 138:165 */         buf.append("(R)");
/* 139:166 */         break;
/* 140:    */       case 32: 
/* 141:168 */         buf.append("(V)");
/* 142:169 */         break;
/* 143:    */       case 64: 
/* 144:171 */         buf.append("(A)");
/* 145:172 */         break;
/* 146:    */       default: 
/* 147:174 */         throwInvalidRVAToken(token);
/* 148:    */       }
/* 149:176 */       buf.append(' ');
/* 150:    */     }
/* 151:178 */     return buf.toString();
/* 152:    */   }
/* 153:    */   
/* 154:    */   private static void throwInvalidRVAToken(Ptg token)
/* 155:    */   {
/* 156:182 */     throw new IllegalStateException("Invalid RVA type (" + token.getPtgClass() + "). This should never happen.");
/* 157:    */   }
/* 158:    */   
/* 159:    */   private static String composeFormula(FormulaRecord record)
/* 160:    */   {
/* 161:188 */     return HSSFFormulaParser.toFormulaString((HSSFWorkbook)null, record.getParsedExpression());
/* 162:    */   }
/* 163:    */   
/* 164:    */   public void setFile(String file)
/* 165:    */   {
/* 166:199 */     this.file = file;
/* 167:    */   }
/* 168:    */   
/* 169:    */   public void setList(boolean list)
/* 170:    */   {
/* 171:203 */     this.list = list;
/* 172:    */   }
/* 173:    */   
/* 174:    */   public static void main(String[] args)
/* 175:    */     throws IOException
/* 176:    */   {
/* 177:216 */     if ((args == null) || (args.length > 2) || (args[0].equals("--help")))
/* 178:    */     {
/* 179:219 */       System.out.println("FormulaViewer .8 proof that the devil lies in the details (or just in BIFF8 files in general)");
/* 180:    */       
/* 181:221 */       System.out.println("usage: Give me a big fat file name");
/* 182:    */     }
/* 183:222 */     else if (args[0].equals("--listFunctions"))
/* 184:    */     {
/* 185:223 */       FormulaViewer viewer = new FormulaViewer();
/* 186:224 */       viewer.setFile(args[1]);
/* 187:225 */       viewer.setList(true);
/* 188:226 */       viewer.run();
/* 189:    */     }
/* 190:    */     else
/* 191:    */     {
/* 192:230 */       FormulaViewer viewer = new FormulaViewer();
/* 193:    */       
/* 194:232 */       viewer.setFile(args[0]);
/* 195:233 */       viewer.run();
/* 196:    */     }
/* 197:    */   }
/* 198:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.dev.FormulaViewer
 * JD-Core Version:    0.7.0.1
 */