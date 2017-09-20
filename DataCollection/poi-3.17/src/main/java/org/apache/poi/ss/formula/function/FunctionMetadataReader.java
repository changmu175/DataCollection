/*   1:    */ package org.apache.poi.ss.formula.function;
/*   2:    */ 
/*   3:    */ import java.util.Arrays;
/*   4:    */ import java.util.HashSet;
/*   5:    */ import java.util.Set;
/*   6:    */ import java.util.regex.Pattern;
/*   7:    */ 
/*   8:    */ final class FunctionMetadataReader
/*   9:    */ {
/*  10:    */   private static final String METADATA_FILE_NAME = "functionMetadata.txt";
/*  11:    */   private static final String ELLIPSIS = "...";
/*  12: 44 */   private static final Pattern TAB_DELIM_PATTERN = Pattern.compile("\t");
/*  13: 45 */   private static final Pattern SPACE_DELIM_PATTERN = Pattern.compile(" ");
/*  14: 46 */   private static final byte[] EMPTY_BYTE_ARRAY = new byte[0];
/*  15: 48 */   private static final String[] DIGIT_ENDING_FUNCTION_NAMES = { "LOG10", "ATAN2", "DAYS360", "SUMXMY2", "SUMX2MY2", "SUMX2PY2" };
/*  16: 53 */   private static final Set<String> DIGIT_ENDING_FUNCTION_NAMES_SET = new HashSet(Arrays.asList(DIGIT_ENDING_FUNCTION_NAMES));
/*  17:    */   
/*  18:    */   /* Error */
/*  19:    */   public static FunctionMetadataRegistry createRegistry()
/*  20:    */   {
/*  21:    */     // Byte code:
/*  22:    */     //   0: ldc_w 2
/*  23:    */     //   3: ldc 3
/*  24:    */     //   5: invokevirtual 4	java/lang/Class:getResourceAsStream	(Ljava/lang/String;)Ljava/io/InputStream;
/*  25:    */     //   8: astore_0
/*  26:    */     //   9: aload_0
/*  27:    */     //   10: ifnonnull +13 -> 23
/*  28:    */     //   13: new 5	java/lang/RuntimeException
/*  29:    */     //   16: dup
/*  30:    */     //   17: ldc 6
/*  31:    */     //   19: invokespecial 7	java/lang/RuntimeException:<init>	(Ljava/lang/String;)V
/*  32:    */     //   22: athrow
/*  33:    */     //   23: new 8	java/io/BufferedReader
/*  34:    */     //   26: dup
/*  35:    */     //   27: new 9	java/io/InputStreamReader
/*  36:    */     //   30: dup
/*  37:    */     //   31: aload_0
/*  38:    */     //   32: ldc 10
/*  39:    */     //   34: invokespecial 11	java/io/InputStreamReader:<init>	(Ljava/io/InputStream;Ljava/lang/String;)V
/*  40:    */     //   37: invokespecial 12	java/io/BufferedReader:<init>	(Ljava/io/Reader;)V
/*  41:    */     //   40: astore_1
/*  42:    */     //   41: goto +13 -> 54
/*  43:    */     //   44: astore_2
/*  44:    */     //   45: new 5	java/lang/RuntimeException
/*  45:    */     //   48: dup
/*  46:    */     //   49: aload_2
/*  47:    */     //   50: invokespecial 14	java/lang/RuntimeException:<init>	(Ljava/lang/Throwable;)V
/*  48:    */     //   53: athrow
/*  49:    */     //   54: new 15	org/apache/poi/ss/formula/function/FunctionDataBuilder
/*  50:    */     //   57: dup
/*  51:    */     //   58: sipush 400
/*  52:    */     //   61: invokespecial 16	org/apache/poi/ss/formula/function/FunctionDataBuilder:<init>	(I)V
/*  53:    */     //   64: astore_2
/*  54:    */     //   65: aload_1
/*  55:    */     //   66: invokevirtual 17	java/io/BufferedReader:readLine	()Ljava/lang/String;
/*  56:    */     //   69: astore_3
/*  57:    */     //   70: aload_3
/*  58:    */     //   71: ifnonnull +6 -> 77
/*  59:    */     //   74: goto +50 -> 124
/*  60:    */     //   77: aload_3
/*  61:    */     //   78: invokevirtual 18	java/lang/String:length	()I
/*  62:    */     //   81: iconst_1
/*  63:    */     //   82: if_icmplt -17 -> 65
/*  64:    */     //   85: aload_3
/*  65:    */     //   86: iconst_0
/*  66:    */     //   87: invokevirtual 19	java/lang/String:charAt	(I)C
/*  67:    */     //   90: bipush 35
/*  68:    */     //   92: if_icmpne +6 -> 98
/*  69:    */     //   95: goto -30 -> 65
/*  70:    */     //   98: aload_3
/*  71:    */     //   99: invokevirtual 20	java/lang/String:trim	()Ljava/lang/String;
/*  72:    */     //   102: astore 4
/*  73:    */     //   104: aload 4
/*  74:    */     //   106: invokevirtual 18	java/lang/String:length	()I
/*  75:    */     //   109: iconst_1
/*  76:    */     //   110: if_icmpge +6 -> 116
/*  77:    */     //   113: goto -48 -> 65
/*  78:    */     //   116: aload_2
/*  79:    */     //   117: aload_3
/*  80:    */     //   118: invokestatic 21	org/apache/poi/ss/formula/function/FunctionMetadataReader:processLine	(Lorg/apache/poi/ss/formula/function/FunctionDataBuilder;Ljava/lang/String;)V
/*  81:    */     //   121: goto -56 -> 65
/*  82:    */     //   124: aload_2
/*  83:    */     //   125: invokevirtual 22	org/apache/poi/ss/formula/function/FunctionDataBuilder:build	()Lorg/apache/poi/ss/formula/function/FunctionMetadataRegistry;
/*  84:    */     //   128: astore_3
/*  85:    */     //   129: aload_1
/*  86:    */     //   130: invokevirtual 23	java/io/BufferedReader:close	()V
/*  87:    */     //   133: aload_0
/*  88:    */     //   134: invokevirtual 24	java/io/InputStream:close	()V
/*  89:    */     //   137: aload_3
/*  90:    */     //   138: areturn
/*  91:    */     //   139: astore 5
/*  92:    */     //   141: aload_1
/*  93:    */     //   142: invokevirtual 23	java/io/BufferedReader:close	()V
/*  94:    */     //   145: aload 5
/*  95:    */     //   147: athrow
/*  96:    */     //   148: astore 6
/*  97:    */     //   150: aload_0
/*  98:    */     //   151: invokevirtual 24	java/io/InputStream:close	()V
/*  99:    */     //   154: aload 6
/* 100:    */     //   156: athrow
/* 101:    */     //   157: astore_0
/* 102:    */     //   158: new 5	java/lang/RuntimeException
/* 103:    */     //   161: dup
/* 104:    */     //   162: aload_0
/* 105:    */     //   163: invokespecial 14	java/lang/RuntimeException:<init>	(Ljava/lang/Throwable;)V
/* 106:    */     //   166: athrow
/* 107:    */     // Line number table:
/* 108:    */     //   Java source line #57	-> byte code offset #0
/* 109:    */     //   Java source line #58	-> byte code offset #9
/* 110:    */     //   Java source line #59	-> byte code offset #13
/* 111:    */     //   Java source line #65	-> byte code offset #23
/* 112:    */     //   Java source line #68	-> byte code offset #41
/* 113:    */     //   Java source line #66	-> byte code offset #44
/* 114:    */     //   Java source line #67	-> byte code offset #45
/* 115:    */     //   Java source line #71	-> byte code offset #54
/* 116:    */     //   Java source line #74	-> byte code offset #65
/* 117:    */     //   Java source line #75	-> byte code offset #70
/* 118:    */     //   Java source line #76	-> byte code offset #74
/* 119:    */     //   Java source line #78	-> byte code offset #77
/* 120:    */     //   Java source line #79	-> byte code offset #95
/* 121:    */     //   Java source line #81	-> byte code offset #98
/* 122:    */     //   Java source line #82	-> byte code offset #104
/* 123:    */     //   Java source line #83	-> byte code offset #113
/* 124:    */     //   Java source line #85	-> byte code offset #116
/* 125:    */     //   Java source line #86	-> byte code offset #121
/* 126:    */     //   Java source line #88	-> byte code offset #124
/* 127:    */     //   Java source line #90	-> byte code offset #129
/* 128:    */     //   Java source line #93	-> byte code offset #133
/* 129:    */     //   Java source line #90	-> byte code offset #139
/* 130:    */     //   Java source line #93	-> byte code offset #148
/* 131:    */     //   Java source line #95	-> byte code offset #157
/* 132:    */     //   Java source line #96	-> byte code offset #158
/* 133:    */     // Local variable table:
/* 134:    */     //   start	length	slot	name	signature
/* 135:    */     //   8	143	0	is	java.io.InputStream
/* 136:    */     //   157	6	0	e	java.io.IOException
/* 137:    */     //   40	102	1	br	java.io.BufferedReader
/* 138:    */     //   44	6	2	e	java.io.UnsupportedEncodingException
/* 139:    */     //   64	61	2	fdb	FunctionDataBuilder
/* 140:    */     //   69	69	3	line	String
/* 141:    */     //   102	3	4	trimLine	String
/* 142:    */     //   139	7	5	localObject1	Object
/* 143:    */     //   148	7	6	localObject2	Object
/* 144:    */     // Exception table:
/* 145:    */     //   from	to	target	type
/* 146:    */     //   23	41	44	java/io/UnsupportedEncodingException
/* 147:    */     //   54	129	139	finally
/* 148:    */     //   139	141	139	finally
/* 149:    */     //   23	133	148	finally
/* 150:    */     //   139	150	148	finally
/* 151:    */     //   0	137	157	java/io/IOException
/* 152:    */     //   139	157	157	java/io/IOException
/* 153:    */   }
/* 154:    */   
/* 155:    */   private static void processLine(FunctionDataBuilder fdb, String line)
/* 156:    */   {
/* 157:102 */     String[] parts = TAB_DELIM_PATTERN.split(line, -2);
/* 158:103 */     if (parts.length != 8) {
/* 159:104 */       throw new RuntimeException("Bad line format '" + line + "' - expected 8 data fields");
/* 160:    */     }
/* 161:106 */     int functionIndex = parseInt(parts[0]);
/* 162:107 */     String functionName = parts[1];
/* 163:108 */     int minParams = parseInt(parts[2]);
/* 164:109 */     int maxParams = parseInt(parts[3]);
/* 165:110 */     byte returnClassCode = parseReturnTypeCode(parts[4]);
/* 166:111 */     byte[] parameterClassCodes = parseOperandTypeCodes(parts[5]);
/* 167:    */     
/* 168:113 */     boolean hasNote = parts[7].length() > 0;
/* 169:    */     
/* 170:115 */     validateFunctionName(functionName);
/* 171:    */     
/* 172:117 */     fdb.add(functionIndex, functionName, minParams, maxParams, returnClassCode, parameterClassCodes, hasNote);
/* 173:    */   }
/* 174:    */   
/* 175:    */   private static byte parseReturnTypeCode(String code)
/* 176:    */   {
/* 177:123 */     if (code.length() == 0) {
/* 178:124 */       return 0;
/* 179:    */     }
/* 180:126 */     return parseOperandTypeCode(code);
/* 181:    */   }
/* 182:    */   
/* 183:    */   private static byte[] parseOperandTypeCodes(String codes)
/* 184:    */   {
/* 185:130 */     if (codes.length() < 1) {
/* 186:131 */       return EMPTY_BYTE_ARRAY;
/* 187:    */     }
/* 188:133 */     if (isDash(codes)) {
/* 189:135 */       return EMPTY_BYTE_ARRAY;
/* 190:    */     }
/* 191:137 */     String[] array = SPACE_DELIM_PATTERN.split(codes);
/* 192:138 */     int nItems = array.length;
/* 193:139 */     if ("...".equals(array[(nItems - 1)])) {
/* 194:142 */       nItems--;
/* 195:    */     }
/* 196:144 */     byte[] result = new byte[nItems];
/* 197:145 */     for (int i = 0; i < nItems; i++) {
/* 198:146 */       result[i] = parseOperandTypeCode(array[i]);
/* 199:    */     }
/* 200:148 */     return result;
/* 201:    */   }
/* 202:    */   
/* 203:    */   private static boolean isDash(String codes)
/* 204:    */   {
/* 205:152 */     if (codes.length() == 1) {
/* 206:153 */       switch (codes.charAt(0))
/* 207:    */       {
/* 208:    */       case '-': 
/* 209:155 */         return true;
/* 210:    */       }
/* 211:    */     }
/* 212:158 */     return false;
/* 213:    */   }
/* 214:    */   
/* 215:    */   private static byte parseOperandTypeCode(String code)
/* 216:    */   {
/* 217:162 */     if (code.length() != 1) {
/* 218:163 */       throw new RuntimeException("Bad operand type code format '" + code + "' expected single char");
/* 219:    */     }
/* 220:165 */     switch (code.charAt(0))
/* 221:    */     {
/* 222:    */     case 'V': 
/* 223:166 */       return 32;
/* 224:    */     case 'R': 
/* 225:167 */       return 0;
/* 226:    */     case 'A': 
/* 227:168 */       return 64;
/* 228:    */     }
/* 229:170 */     throw new IllegalArgumentException("Unexpected operand type code '" + code + "' (" + code.charAt(0) + ")");
/* 230:    */   }
/* 231:    */   
/* 232:    */   private static void validateFunctionName(String functionName)
/* 233:    */   {
/* 234:178 */     int len = functionName.length();
/* 235:179 */     int ix = len - 1;
/* 236:180 */     if (!Character.isDigit(functionName.charAt(ix))) {
/* 237:181 */       return;
/* 238:    */     }
/* 239:183 */     while ((ix >= 0) && 
/* 240:184 */       (Character.isDigit(functionName.charAt(ix)))) {
/* 241:187 */       ix--;
/* 242:    */     }
/* 243:189 */     if (DIGIT_ENDING_FUNCTION_NAMES_SET.contains(functionName)) {
/* 244:190 */       return;
/* 245:    */     }
/* 246:192 */     throw new RuntimeException("Invalid function name '" + functionName + "' (is footnote number incorrectly appended)");
/* 247:    */   }
/* 248:    */   
/* 249:    */   private static int parseInt(String valStr)
/* 250:    */   {
/* 251:    */     try
/* 252:    */     {
/* 253:198 */       return Integer.parseInt(valStr);
/* 254:    */     }
/* 255:    */     catch (NumberFormatException e)
/* 256:    */     {
/* 257:200 */       throw new RuntimeException("Value '" + valStr + "' could not be parsed as an integer");
/* 258:    */     }
/* 259:    */   }
/* 260:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.formula.function.FunctionMetadataReader
 * JD-Core Version:    0.7.0.1
 */