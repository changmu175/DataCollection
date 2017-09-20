/*   1:    */ package org.apache.poi.hpsf;
/*   2:    */ 
/*   3:    */ import org.apache.poi.util.LittleEndian;
/*   4:    */ 
/*   5:    */ public final class Thumbnail
/*   6:    */ {
/*   7:    */   public static final int OFFSET_CFTAG = 4;
/*   8:    */   public static final int OFFSET_CF = 8;
/*   9:    */   public static final int OFFSET_WMFDATA = 20;
/*  10:    */   public static final int CFTAG_WINDOWS = -1;
/*  11:    */   public static final int CFTAG_MACINTOSH = -2;
/*  12:    */   public static final int CFTAG_FMTID = -3;
/*  13:    */   public static final int CFTAG_NODATA = 0;
/*  14:    */   public static final int CF_METAFILEPICT = 3;
/*  15:    */   public static final int CF_DIB = 8;
/*  16:    */   public static final int CF_ENHMETAFILE = 14;
/*  17:    */   public static final int CF_BITMAP = 2;
/*  18:126 */   private byte[] _thumbnailData = null;
/*  19:    */   
/*  20:    */   public Thumbnail() {}
/*  21:    */   
/*  22:    */   public Thumbnail(byte[] thumbnailData)
/*  23:    */   {
/*  24:152 */     this._thumbnailData = thumbnailData;
/*  25:    */   }
/*  26:    */   
/*  27:    */   public byte[] getThumbnail()
/*  28:    */   {
/*  29:166 */     return this._thumbnailData;
/*  30:    */   }
/*  31:    */   
/*  32:    */   public void setThumbnail(byte[] thumbnail)
/*  33:    */   {
/*  34:180 */     this._thumbnailData = thumbnail;
/*  35:    */   }
/*  36:    */   
/*  37:    */   public long getClipboardFormatTag()
/*  38:    */   {
/*  39:201 */     long clipboardFormatTag = LittleEndian.getInt(getThumbnail(), 4);
/*  40:    */     
/*  41:203 */     return clipboardFormatTag;
/*  42:    */   }
/*  43:    */   
/*  44:    */   public long getClipboardFormat()
/*  45:    */     throws HPSFException
/*  46:    */   {
/*  47:229 */     if (getClipboardFormatTag() != -1L) {
/*  48:230 */       throw new HPSFException("Clipboard Format Tag of Thumbnail must be CFTAG_WINDOWS.");
/*  49:    */     }
/*  50:233 */     return LittleEndian.getInt(getThumbnail(), 8);
/*  51:    */   }
/*  52:    */   
/*  53:    */   public byte[] getThumbnailAsWMF()
/*  54:    */     throws HPSFException
/*  55:    */   {
/*  56:256 */     if (getClipboardFormatTag() != -1L) {
/*  57:257 */       throw new HPSFException("Clipboard Format Tag of Thumbnail must be CFTAG_WINDOWS.");
/*  58:    */     }
/*  59:259 */     if (getClipboardFormat() != 3L) {
/*  60:260 */       throw new HPSFException("Clipboard Format of Thumbnail must be CF_METAFILEPICT.");
/*  61:    */     }
/*  62:263 */     byte[] thumbnail = getThumbnail();
/*  63:264 */     int wmfImageLength = thumbnail.length - 20;
/*  64:265 */     byte[] wmfImage = new byte[wmfImageLength];
/*  65:266 */     System.arraycopy(thumbnail, 20, wmfImage, 0, wmfImageLength);
/*  66:    */     
/*  67:    */ 
/*  68:    */ 
/*  69:    */ 
/*  70:271 */     return wmfImage;
/*  71:    */   }
/*  72:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hpsf.Thumbnail
 * JD-Core Version:    0.7.0.1
 */