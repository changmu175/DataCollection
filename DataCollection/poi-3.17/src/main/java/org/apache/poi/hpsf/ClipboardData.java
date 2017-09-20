/*  1:   */ package org.apache.poi.hpsf;
/*  2:   */ 
/*  3:   */ import org.apache.poi.util.IOUtils;
/*  4:   */ import org.apache.poi.util.Internal;
/*  5:   */ import org.apache.poi.util.LittleEndianByteArrayInputStream;
/*  6:   */ import org.apache.poi.util.LittleEndianByteArrayOutputStream;
/*  7:   */ import org.apache.poi.util.POILogFactory;
/*  8:   */ import org.apache.poi.util.POILogger;
/*  9:   */ 
/* 10:   */ @Internal
/* 11:   */ class ClipboardData
/* 12:   */ {
/* 13:29 */   private static final POILogger LOG = POILogFactory.getLogger(ClipboardData.class);
/* 14:31 */   private int _format = 0;
/* 15:   */   private byte[] _value;
/* 16:   */   
/* 17:   */   void read(LittleEndianByteArrayInputStream lei)
/* 18:   */   {
/* 19:37 */     int offset = lei.getReadIndex();
/* 20:38 */     int size = lei.readInt();
/* 21:40 */     if (size < 4)
/* 22:   */     {
/* 23:41 */       String msg = "ClipboardData at offset " + offset + " size less than 4 bytes " + "(doesn't even have format field!). Setting to format == 0 and hope for the best";
/* 24:   */       
/* 25:   */ 
/* 26:44 */       LOG.log(5, new Object[] { msg });
/* 27:45 */       this._format = 0;
/* 28:46 */       this._value = new byte[0];
/* 29:47 */       return;
/* 30:   */     }
/* 31:50 */     this._format = lei.readInt();
/* 32:51 */     this._value = new byte[size - 4];
/* 33:52 */     lei.readFully(this._value);
/* 34:   */   }
/* 35:   */   
/* 36:   */   byte[] getValue()
/* 37:   */   {
/* 38:56 */     return this._value;
/* 39:   */   }
/* 40:   */   
/* 41:   */   byte[] toByteArray()
/* 42:   */   {
/* 43:60 */     byte[] result = new byte[8 + this._value.length];
/* 44:61 */     LittleEndianByteArrayOutputStream bos = new LittleEndianByteArrayOutputStream(result, 0);
/* 45:   */     try
/* 46:   */     {
/* 47:63 */       bos.writeInt(4 + this._value.length);
/* 48:64 */       bos.writeInt(this._format);
/* 49:65 */       bos.write(this._value);
/* 50:66 */       return result;
/* 51:   */     }
/* 52:   */     finally
/* 53:   */     {
/* 54:68 */       IOUtils.closeQuietly(bos);
/* 55:   */     }
/* 56:   */   }
/* 57:   */   
/* 58:   */   void setValue(byte[] value)
/* 59:   */   {
/* 60:73 */     this._value = ((byte[])value.clone());
/* 61:   */   }
/* 62:   */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hpsf.ClipboardData
 * JD-Core Version:    0.7.0.1
 */