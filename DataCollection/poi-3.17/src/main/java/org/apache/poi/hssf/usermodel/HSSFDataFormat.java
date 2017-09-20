/*   1:    */ package org.apache.poi.hssf.usermodel;
/*   2:    */ 
/*   3:    */ import java.util.Arrays;
/*   4:    */ import java.util.Iterator;
/*   5:    */ import java.util.List;
/*   6:    */ import java.util.Vector;
/*   7:    */ import org.apache.poi.hssf.model.InternalWorkbook;
/*   8:    */ import org.apache.poi.hssf.record.FormatRecord;
/*   9:    */ import org.apache.poi.ss.usermodel.BuiltinFormats;
/*  10:    */ import org.apache.poi.ss.usermodel.DataFormat;
/*  11:    */ 
/*  12:    */ public final class HSSFDataFormat
/*  13:    */   implements DataFormat
/*  14:    */ {
/*  15: 43 */   private static final String[] _builtinFormats = ;
/*  16: 45 */   private final Vector<String> _formats = new Vector();
/*  17:    */   private final InternalWorkbook _workbook;
/*  18: 47 */   private boolean _movedBuiltins = false;
/*  19:    */   
/*  20:    */   HSSFDataFormat(InternalWorkbook workbook)
/*  21:    */   {
/*  22: 58 */     this._workbook = workbook;
/*  23:    */     
/*  24: 60 */     Iterator<FormatRecord> i = workbook.getFormats().iterator();
/*  25: 61 */     while (i.hasNext())
/*  26:    */     {
/*  27: 62 */       FormatRecord r = (FormatRecord)i.next();
/*  28: 63 */       ensureFormatsSize(r.getIndexCode());
/*  29: 64 */       this._formats.set(r.getIndexCode(), r.getFormatString());
/*  30:    */     }
/*  31:    */   }
/*  32:    */   
/*  33:    */   public static List<String> getBuiltinFormats()
/*  34:    */   {
/*  35: 69 */     return Arrays.asList(_builtinFormats);
/*  36:    */   }
/*  37:    */   
/*  38:    */   public static short getBuiltinFormat(String format)
/*  39:    */   {
/*  40: 79 */     return (short)BuiltinFormats.getBuiltinFormat(format);
/*  41:    */   }
/*  42:    */   
/*  43:    */   public short getFormat(String pFormat)
/*  44:    */   {
/*  45:    */     String format;
/*  46:    */     String format;
/*  47: 92 */     if (pFormat.equalsIgnoreCase("TEXT")) {
/*  48: 93 */       format = "@";
/*  49:    */     } else {
/*  50: 95 */       format = pFormat;
/*  51:    */     }
/*  52: 99 */     if (!this._movedBuiltins)
/*  53:    */     {
/*  54:100 */       for (int i = 0; i < _builtinFormats.length; i++)
/*  55:    */       {
/*  56:101 */         ensureFormatsSize(i);
/*  57:102 */         if (this._formats.get(i) == null) {
/*  58:103 */           this._formats.set(i, _builtinFormats[i]);
/*  59:    */         }
/*  60:    */       }
/*  61:108 */       this._movedBuiltins = true;
/*  62:    */     }
/*  63:112 */     for (int i = 0; i < this._formats.size(); i++) {
/*  64:113 */       if (format.equals(this._formats.get(i))) {
/*  65:114 */         return (short)i;
/*  66:    */       }
/*  67:    */     }
/*  68:119 */     short index = this._workbook.getFormat(format, true);
/*  69:120 */     ensureFormatsSize(index);
/*  70:121 */     this._formats.set(index, format);
/*  71:122 */     return index;
/*  72:    */   }
/*  73:    */   
/*  74:    */   public String getFormat(short index)
/*  75:    */   {
/*  76:131 */     if (this._movedBuiltins) {
/*  77:132 */       return (String)this._formats.get(index);
/*  78:    */     }
/*  79:135 */     if (index == -1) {
/*  80:138 */       return null;
/*  81:    */     }
/*  82:141 */     String fmt = this._formats.size() > index ? (String)this._formats.get(index) : null;
/*  83:142 */     if ((_builtinFormats.length > index) && (_builtinFormats[index] != null))
/*  84:    */     {
/*  85:144 */       if (fmt != null) {
/*  86:146 */         return fmt;
/*  87:    */       }
/*  88:149 */       return _builtinFormats[index];
/*  89:    */     }
/*  90:152 */     return fmt;
/*  91:    */   }
/*  92:    */   
/*  93:    */   public static String getBuiltinFormat(short index)
/*  94:    */   {
/*  95:161 */     return BuiltinFormats.getBuiltinFormat(index);
/*  96:    */   }
/*  97:    */   
/*  98:    */   public static int getNumberOfBuiltinBuiltinFormats()
/*  99:    */   {
/* 100:169 */     return _builtinFormats.length;
/* 101:    */   }
/* 102:    */   
/* 103:    */   private void ensureFormatsSize(int index)
/* 104:    */   {
/* 105:177 */     if (this._formats.size() <= index) {
/* 106:178 */       this._formats.setSize(index + 1);
/* 107:    */     }
/* 108:    */   }
/* 109:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.usermodel.HSSFDataFormat
 * JD-Core Version:    0.7.0.1
 */