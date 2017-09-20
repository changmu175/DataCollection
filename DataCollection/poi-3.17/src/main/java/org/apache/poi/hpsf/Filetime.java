/*   1:    */ package org.apache.poi.hpsf;
/*   2:    */ 
/*   3:    */ import java.io.IOException;
/*   4:    */ import java.io.OutputStream;
/*   5:    */ import java.util.Date;
/*   6:    */ import org.apache.poi.util.LittleEndian;
/*   7:    */ import org.apache.poi.util.LittleEndianByteArrayInputStream;
/*   8:    */ 
/*   9:    */ public class Filetime
/*  10:    */ {
/*  11:    */   private static final long EPOCH_DIFF = -11644473600000L;
/*  12:    */   private static final int SIZE = 8;
/*  13:    */   private static final long UINT_MASK = 4294967295L;
/*  14:    */   private static final long NANO_100 = 10000L;
/*  15:    */   private int _dwHighDateTime;
/*  16:    */   private int _dwLowDateTime;
/*  17:    */   
/*  18:    */   Filetime() {}
/*  19:    */   
/*  20:    */   Filetime(int low, int high)
/*  21:    */   {
/*  22: 45 */     this._dwLowDateTime = low;
/*  23: 46 */     this._dwHighDateTime = high;
/*  24:    */   }
/*  25:    */   
/*  26:    */   Filetime(Date date)
/*  27:    */   {
/*  28: 50 */     long filetime = dateToFileTime(date);
/*  29: 51 */     this._dwHighDateTime = ((int)(filetime >>> 32 & 0xFFFFFFFF));
/*  30: 52 */     this._dwLowDateTime = ((int)(filetime & 0xFFFFFFFF));
/*  31:    */   }
/*  32:    */   
/*  33:    */   void read(LittleEndianByteArrayInputStream lei)
/*  34:    */   {
/*  35: 57 */     this._dwLowDateTime = lei.readInt();
/*  36: 58 */     this._dwHighDateTime = lei.readInt();
/*  37:    */   }
/*  38:    */   
/*  39:    */   long getHigh()
/*  40:    */   {
/*  41: 62 */     return this._dwHighDateTime;
/*  42:    */   }
/*  43:    */   
/*  44:    */   long getLow()
/*  45:    */   {
/*  46: 66 */     return this._dwLowDateTime;
/*  47:    */   }
/*  48:    */   
/*  49:    */   byte[] toByteArray()
/*  50:    */   {
/*  51: 70 */     byte[] result = new byte[8];
/*  52: 71 */     LittleEndian.putInt(result, 0, this._dwLowDateTime);
/*  53: 72 */     LittleEndian.putInt(result, 4, this._dwHighDateTime);
/*  54: 73 */     return result;
/*  55:    */   }
/*  56:    */   
/*  57:    */   int write(OutputStream out)
/*  58:    */     throws IOException
/*  59:    */   {
/*  60: 77 */     LittleEndian.putInt(this._dwLowDateTime, out);
/*  61: 78 */     LittleEndian.putInt(this._dwHighDateTime, out);
/*  62: 79 */     return 8;
/*  63:    */   }
/*  64:    */   
/*  65:    */   Date getJavaValue()
/*  66:    */   {
/*  67: 83 */     long l = this._dwHighDateTime << 32 | this._dwLowDateTime & 0xFFFFFFFF;
/*  68: 84 */     return filetimeToDate(l);
/*  69:    */   }
/*  70:    */   
/*  71:    */   public static Date filetimeToDate(long filetime)
/*  72:    */   {
/*  73: 98 */     long ms_since_16010101 = filetime / 10000L;
/*  74: 99 */     long ms_since_19700101 = ms_since_16010101 + -11644473600000L;
/*  75:100 */     return new Date(ms_since_19700101);
/*  76:    */   }
/*  77:    */   
/*  78:    */   public static long dateToFileTime(Date date)
/*  79:    */   {
/*  80:112 */     long ms_since_19700101 = date.getTime();
/*  81:113 */     long ms_since_16010101 = ms_since_19700101 - -11644473600000L;
/*  82:114 */     return ms_since_16010101 * 10000L;
/*  83:    */   }
/*  84:    */   
/*  85:    */   public static boolean isUndefined(Date date)
/*  86:    */   {
/*  87:124 */     return (date == null) || (dateToFileTime(date) == 0L);
/*  88:    */   }
/*  89:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hpsf.Filetime
 * JD-Core Version:    0.7.0.1
 */