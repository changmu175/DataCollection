/*   1:    */ package org.apache.poi.ss.usermodel;
/*   2:    */ 
/*   3:    */ public final class BuiltinFormats
/*   4:    */ {
/*   5:    */   public static final int FIRST_USER_DEFINED_FORMAT_INDEX = 164;
/*   6: 70 */   private static final String[] _formats = { "General", "0", "0.00", "#,##0", "#,##0.00", "\"$\"#,##0_);(\"$\"#,##0)", "\"$\"#,##0_);[Red](\"$\"#,##0)", "\"$\"#,##0.00_);(\"$\"#,##0.00)", "\"$\"#,##0.00_);[Red](\"$\"#,##0.00)", "0%", "0.00%", "0.00E+00", "# ?/?", "# ??/??", "m/d/yy", "d-mmm-yy", "d-mmm", "mmm-yy", "h:mm AM/PM", "h:mm:ss AM/PM", "h:mm", "h:mm:ss", "m/d/yy h:mm", "reserved-0x17", "reserved-0x18", "reserved-0x19", "reserved-0x1A", "reserved-0x1B", "reserved-0x1C", "reserved-0x1D", "reserved-0x1E", "reserved-0x1F", "reserved-0x20", "reserved-0x21", "reserved-0x22", "reserved-0x23", "reserved-0x24", "#,##0_);(#,##0)", "#,##0_);[Red](#,##0)", "#,##0.00_);(#,##0.00)", "#,##0.00_);[Red](#,##0.00)", "_(* #,##0_);_(* (#,##0);_(* \"-\"_);_(@_)", "_(\"$\"* #,##0_);_(\"$\"* (#,##0);_(\"$\"* \"-\"_);_(@_)", "_(* #,##0.00_);_(* (#,##0.00);_(* \"-\"??_);_(@_)", "_(\"$\"* #,##0.00_);_(\"$\"* (#,##0.00);_(\"$\"* \"-\"??_);_(@_)", "mm:ss", "[h]:mm:ss", "mm:ss.0", "##0.0E+0", "@" };
/*   7:    */   
/*   8:    */   public static String[] getAll()
/*   9:    */   {
/*  10:131 */     return (String[])_formats.clone();
/*  11:    */   }
/*  12:    */   
/*  13:    */   public static String getBuiltinFormat(int index)
/*  14:    */   {
/*  15:141 */     if ((index < 0) || (index >= _formats.length)) {
/*  16:142 */       return null;
/*  17:    */     }
/*  18:144 */     return _formats[index];
/*  19:    */   }
/*  20:    */   
/*  21:    */   public static int getBuiltinFormat(String pFmt)
/*  22:    */   {
/*  23:155 */     String fmt = "TEXT".equalsIgnoreCase(pFmt) ? "@" : pFmt;
/*  24:    */     
/*  25:157 */     int i = -1;
/*  26:158 */     for (String f : _formats)
/*  27:    */     {
/*  28:159 */       i++;
/*  29:160 */       if (f.equals(fmt)) {
/*  30:161 */         return i;
/*  31:    */       }
/*  32:    */     }
/*  33:165 */     return -1;
/*  34:    */   }
/*  35:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.usermodel.BuiltinFormats
 * JD-Core Version:    0.7.0.1
 */