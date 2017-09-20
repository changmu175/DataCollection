/*   1:    */ package org.apache.poi.hpsf;
/*   2:    */ 
/*   3:    */ import java.io.IOException;
/*   4:    */ import java.io.OutputStream;
/*   5:    */ import java.io.UnsupportedEncodingException;
/*   6:    */ import org.apache.poi.util.CodePageUtil;
/*   7:    */ import org.apache.poi.util.Internal;
/*   8:    */ import org.apache.poi.util.LittleEndian;
/*   9:    */ import org.apache.poi.util.LittleEndianByteArrayInputStream;
/*  10:    */ import org.apache.poi.util.POILogFactory;
/*  11:    */ import org.apache.poi.util.POILogger;
/*  12:    */ 
/*  13:    */ @Internal
/*  14:    */ class CodePageString
/*  15:    */ {
/*  16: 33 */   private static final POILogger LOG = POILogFactory.getLogger(CodePageString.class);
/*  17:    */   private byte[] _value;
/*  18:    */   
/*  19:    */   void read(LittleEndianByteArrayInputStream lei)
/*  20:    */   {
/*  21: 41 */     int offset = lei.getReadIndex();
/*  22: 42 */     int size = lei.readInt();
/*  23: 43 */     this._value = new byte[size];
/*  24: 44 */     if (size == 0) {
/*  25: 45 */       return;
/*  26:    */     }
/*  27: 57 */     lei.readFully(this._value);
/*  28: 58 */     if (this._value[(size - 1)] != 0)
/*  29:    */     {
/*  30: 62 */       String msg = "CodePageString started at offset #" + offset + " is not NULL-terminated";
/*  31: 63 */       LOG.log(5, new Object[] { msg });
/*  32:    */     }
/*  33: 66 */     TypedPropertyValue.skipPadding(lei);
/*  34:    */   }
/*  35:    */   
/*  36:    */   String getJavaValue(int codepage)
/*  37:    */     throws UnsupportedEncodingException
/*  38:    */   {
/*  39: 70 */     int cp = codepage == -1 ? 1252 : codepage;
/*  40: 71 */     String result = CodePageUtil.getStringFromCodePage(this._value, cp);
/*  41:    */     
/*  42:    */ 
/*  43: 74 */     int terminator = result.indexOf(0);
/*  44: 75 */     if (terminator == -1)
/*  45:    */     {
/*  46: 76 */       String msg = "String terminator (\\0) for CodePageString property value not found.Continue without trimming and hope for the best.";
/*  47:    */       
/*  48:    */ 
/*  49: 79 */       LOG.log(5, new Object[] { msg });
/*  50: 80 */       return result;
/*  51:    */     }
/*  52: 82 */     if (terminator != result.length() - 1)
/*  53:    */     {
/*  54: 83 */       String msg = "String terminator (\\0) for CodePageString property value occured before the end of string. Trimming and hope for the best.";
/*  55:    */       
/*  56:    */ 
/*  57: 86 */       LOG.log(5, new Object[] { msg });
/*  58:    */     }
/*  59: 88 */     return result.substring(0, terminator);
/*  60:    */   }
/*  61:    */   
/*  62:    */   int getSize()
/*  63:    */   {
/*  64: 92 */     return 4 + this._value.length;
/*  65:    */   }
/*  66:    */   
/*  67:    */   void setJavaValue(String string, int codepage)
/*  68:    */     throws UnsupportedEncodingException
/*  69:    */   {
/*  70: 96 */     int cp = codepage == -1 ? 1252 : codepage;
/*  71: 97 */     this._value = CodePageUtil.getBytesInCodePage(string + "", cp);
/*  72:    */   }
/*  73:    */   
/*  74:    */   int write(OutputStream out)
/*  75:    */     throws IOException
/*  76:    */   {
/*  77:101 */     LittleEndian.putUInt(this._value.length, out);
/*  78:102 */     out.write(this._value);
/*  79:103 */     return 4 + this._value.length;
/*  80:    */   }
/*  81:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hpsf.CodePageString
 * JD-Core Version:    0.7.0.1
 */