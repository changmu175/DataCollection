/*   1:    */ package org.apache.poi.poifs.storage;
/*   2:    */ 
/*   3:    */ import java.io.IOException;
/*   4:    */ import java.io.InputStream;
/*   5:    */ import org.apache.poi.util.IOUtils;
/*   6:    */ import org.apache.poi.util.POILogFactory;
/*   7:    */ import org.apache.poi.util.POILogger;
/*   8:    */ 
/*   9:    */ public class RawDataBlock
/*  10:    */   implements ListManagedBlock
/*  11:    */ {
/*  12:    */   private byte[] _data;
/*  13:    */   private boolean _eof;
/*  14:    */   private boolean _hasData;
/*  15: 41 */   static POILogger log = POILogFactory.getLogger(RawDataBlock.class);
/*  16:    */   
/*  17:    */   public RawDataBlock(InputStream stream)
/*  18:    */     throws IOException
/*  19:    */   {
/*  20: 54 */     this(stream, 512);
/*  21:    */   }
/*  22:    */   
/*  23:    */   public RawDataBlock(InputStream stream, int blockSize)
/*  24:    */     throws IOException
/*  25:    */   {
/*  26: 69 */     this._data = new byte[blockSize];
/*  27: 70 */     int count = IOUtils.readFully(stream, this._data);
/*  28: 71 */     this._hasData = (count > 0);
/*  29: 73 */     if (count == -1)
/*  30:    */     {
/*  31: 74 */       this._eof = true;
/*  32:    */     }
/*  33: 76 */     else if (count != blockSize)
/*  34:    */     {
/*  35: 80 */       this._eof = true;
/*  36: 81 */       String type = " byte" + (count == 1 ? "" : "s");
/*  37:    */       
/*  38:    */ 
/*  39: 84 */       log.log(7, new Object[] { "Unable to read entire block; " + count + type + " read before EOF; expected " + blockSize + " bytes. Your document " + "was either written by software that " + "ignores the spec, or has been truncated!" });
/*  40:    */     }
/*  41:    */     else
/*  42:    */     {
/*  43: 93 */       this._eof = false;
/*  44:    */     }
/*  45:    */   }
/*  46:    */   
/*  47:    */   public boolean eof()
/*  48:    */   {
/*  49:105 */     return this._eof;
/*  50:    */   }
/*  51:    */   
/*  52:    */   public boolean hasData()
/*  53:    */   {
/*  54:113 */     return this._hasData;
/*  55:    */   }
/*  56:    */   
/*  57:    */   public String toString()
/*  58:    */   {
/*  59:117 */     return "RawDataBlock of size " + this._data.length;
/*  60:    */   }
/*  61:    */   
/*  62:    */   public byte[] getData()
/*  63:    */     throws IOException
/*  64:    */   {
/*  65:132 */     if (!hasData()) {
/*  66:134 */       throw new IOException("Cannot return empty data");
/*  67:    */     }
/*  68:136 */     return this._data;
/*  69:    */   }
/*  70:    */   
/*  71:    */   public int getBigBlockSize()
/*  72:    */   {
/*  73:143 */     return this._data.length;
/*  74:    */   }
/*  75:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.poifs.storage.RawDataBlock
 * JD-Core Version:    0.7.0.1
 */