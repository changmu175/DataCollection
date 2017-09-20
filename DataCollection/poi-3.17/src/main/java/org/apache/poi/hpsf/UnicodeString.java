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
/*  12:    */ import org.apache.poi.util.StringUtil;
/*  13:    */ 
/*  14:    */ @Internal
/*  15:    */ class UnicodeString
/*  16:    */ {
/*  17: 34 */   private static final POILogger LOG = POILogFactory.getLogger(UnicodeString.class);
/*  18:    */   private byte[] _value;
/*  19:    */   
/*  20:    */   void read(LittleEndianByteArrayInputStream lei)
/*  21:    */   {
/*  22: 41 */     int length = lei.readInt();
/*  23: 42 */     int unicodeBytes = length * 2;
/*  24: 43 */     this._value = new byte[unicodeBytes];
/*  25: 50 */     if (length == 0) {
/*  26: 51 */       return;
/*  27:    */     }
/*  28: 54 */     int offset = lei.getReadIndex();
/*  29:    */     
/*  30: 56 */     lei.readFully(this._value);
/*  31: 58 */     if ((this._value[(unicodeBytes - 2)] != 0) || (this._value[(unicodeBytes - 1)] != 0))
/*  32:    */     {
/*  33: 59 */       String msg = "UnicodeString started at offset #" + offset + " is not NULL-terminated";
/*  34: 60 */       throw new IllegalPropertySetDataException(msg);
/*  35:    */     }
/*  36: 63 */     TypedPropertyValue.skipPadding(lei);
/*  37:    */   }
/*  38:    */   
/*  39:    */   byte[] getValue()
/*  40:    */   {
/*  41: 67 */     return this._value;
/*  42:    */   }
/*  43:    */   
/*  44:    */   String toJavaString()
/*  45:    */   {
/*  46: 71 */     if (this._value.length == 0) {
/*  47: 72 */       return null;
/*  48:    */     }
/*  49: 75 */     String result = StringUtil.getFromUnicodeLE(this._value, 0, this._value.length >> 1);
/*  50:    */     
/*  51: 77 */     int terminator = result.indexOf(0);
/*  52: 78 */     if (terminator == -1)
/*  53:    */     {
/*  54: 79 */       String msg = "String terminator (\\0) for UnicodeString property value not found.Continue without trimming and hope for the best.";
/*  55:    */       
/*  56:    */ 
/*  57: 82 */       LOG.log(5, new Object[] { msg });
/*  58: 83 */       return result;
/*  59:    */     }
/*  60: 86 */     if (terminator != result.length() - 1)
/*  61:    */     {
/*  62: 87 */       String msg = "String terminator (\\0) for UnicodeString property value occured before the end of string. Trimming and hope for the best.";
/*  63:    */       
/*  64:    */ 
/*  65: 90 */       LOG.log(5, new Object[] { msg });
/*  66:    */     }
/*  67: 92 */     return result.substring(0, terminator);
/*  68:    */   }
/*  69:    */   
/*  70:    */   void setJavaValue(String string)
/*  71:    */     throws UnsupportedEncodingException
/*  72:    */   {
/*  73: 96 */     this._value = CodePageUtil.getBytesInCodePage(string + "", 1200);
/*  74:    */   }
/*  75:    */   
/*  76:    */   int write(OutputStream out)
/*  77:    */     throws IOException
/*  78:    */   {
/*  79:100 */     LittleEndian.putUInt(this._value.length / 2, out);
/*  80:101 */     out.write(this._value);
/*  81:102 */     return 4 + this._value.length;
/*  82:    */   }
/*  83:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hpsf.UnicodeString
 * JD-Core Version:    0.7.0.1
 */