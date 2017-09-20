/*   1:    */ package org.apache.poi.sl.image;
/*   2:    */ 
/*   3:    */ import java.awt.Dimension;
/*   4:    */ import java.awt.Rectangle;
/*   5:    */ import java.io.IOException;
/*   6:    */ import java.io.OutputStream;
/*   7:    */ import org.apache.poi.util.Internal;
/*   8:    */ import org.apache.poi.util.LittleEndian;
/*   9:    */ import org.apache.poi.util.POILogFactory;
/*  10:    */ import org.apache.poi.util.POILogger;
/*  11:    */ 
/*  12:    */ @Internal
/*  13:    */ public class ImageHeaderWMF
/*  14:    */ {
/*  15:    */   public static final int APMHEADER_KEY = -1698247209;
/*  16: 49 */   private static final POILogger LOG = POILogFactory.getLogger(ImageHeaderWMF.class);
/*  17:    */   private final int handle;
/*  18:    */   private final int left;
/*  19:    */   private final int top;
/*  20:    */   private final int right;
/*  21:    */   private final int bottom;
/*  22:    */   private final int inch;
/*  23:    */   private final int reserved;
/*  24:    */   private int checksum;
/*  25:    */   
/*  26:    */   public ImageHeaderWMF(Rectangle dim)
/*  27:    */   {
/*  28: 69 */     this.handle = 0;
/*  29: 70 */     this.left = dim.x;
/*  30: 71 */     this.top = dim.y;
/*  31: 72 */     this.right = (dim.x + dim.width);
/*  32: 73 */     this.bottom = (dim.y + dim.height);
/*  33: 74 */     this.inch = 72;
/*  34: 75 */     this.reserved = 0;
/*  35:    */   }
/*  36:    */   
/*  37:    */   public ImageHeaderWMF(byte[] data, int off)
/*  38:    */   {
/*  39: 79 */     int offset = off;
/*  40: 80 */     int key = LittleEndian.getInt(data, offset);offset += 4;
/*  41: 81 */     if (key != -1698247209)
/*  42:    */     {
/*  43: 82 */       LOG.log(5, new Object[] { "WMF file doesn't contain a placeable header - ignore parsing" });
/*  44: 83 */       this.handle = 0;
/*  45: 84 */       this.left = 0;
/*  46: 85 */       this.top = 0;
/*  47: 86 */       this.right = 200;
/*  48: 87 */       this.bottom = 200;
/*  49: 88 */       this.inch = 72;
/*  50: 89 */       this.reserved = 0;
/*  51: 90 */       return;
/*  52:    */     }
/*  53: 93 */     this.handle = LittleEndian.getUShort(data, offset);offset += 2;
/*  54: 94 */     this.left = LittleEndian.getShort(data, offset);offset += 2;
/*  55: 95 */     this.top = LittleEndian.getShort(data, offset);offset += 2;
/*  56: 96 */     this.right = LittleEndian.getShort(data, offset);offset += 2;
/*  57: 97 */     this.bottom = LittleEndian.getShort(data, offset);offset += 2;
/*  58:    */     
/*  59: 99 */     this.inch = LittleEndian.getUShort(data, offset);offset += 2;
/*  60:100 */     this.reserved = LittleEndian.getInt(data, offset);offset += 4;
/*  61:    */     
/*  62:102 */     this.checksum = LittleEndian.getShort(data, offset);offset += 2;
/*  63:103 */     if (this.checksum != getChecksum()) {
/*  64:104 */       LOG.log(5, new Object[] { "WMF checksum does not match the header data" });
/*  65:    */     }
/*  66:    */   }
/*  67:    */   
/*  68:    */   public int getChecksum()
/*  69:    */   {
/*  70:113 */     int cs = 0;
/*  71:114 */     cs ^= 0xCDD7;
/*  72:115 */     cs ^= 0xFFFF9AC6;
/*  73:116 */     cs ^= this.left;
/*  74:117 */     cs ^= this.top;
/*  75:118 */     cs ^= this.right;
/*  76:119 */     cs ^= this.bottom;
/*  77:120 */     cs ^= this.inch;
/*  78:121 */     return cs;
/*  79:    */   }
/*  80:    */   
/*  81:    */   public void write(OutputStream out)
/*  82:    */     throws IOException
/*  83:    */   {
/*  84:125 */     byte[] header = new byte[22];
/*  85:126 */     int pos = 0;
/*  86:127 */     LittleEndian.putInt(header, pos, -1698247209);pos += 4;
/*  87:128 */     LittleEndian.putUShort(header, pos, 0);pos += 2;
/*  88:129 */     LittleEndian.putUShort(header, pos, this.left);pos += 2;
/*  89:130 */     LittleEndian.putUShort(header, pos, this.top);pos += 2;
/*  90:131 */     LittleEndian.putUShort(header, pos, this.right);pos += 2;
/*  91:132 */     LittleEndian.putUShort(header, pos, this.bottom);pos += 2;
/*  92:133 */     LittleEndian.putUShort(header, pos, this.inch);pos += 2;
/*  93:134 */     LittleEndian.putInt(header, pos, 0);pos += 4;
/*  94:    */     
/*  95:136 */     this.checksum = getChecksum();
/*  96:137 */     LittleEndian.putUShort(header, pos, this.checksum);
/*  97:    */     
/*  98:139 */     out.write(header);
/*  99:    */   }
/* 100:    */   
/* 101:    */   public Dimension getSize()
/* 102:    */   {
/* 103:144 */     double coeff = 72.0D / this.inch;
/* 104:145 */     return new Dimension((int)Math.round((this.right - this.left) * coeff), (int)Math.round((this.bottom - this.top) * coeff));
/* 105:    */   }
/* 106:    */   
/* 107:    */   public Rectangle getBounds()
/* 108:    */   {
/* 109:149 */     return new Rectangle(this.left, this.top, this.right - this.left, this.bottom - this.top);
/* 110:    */   }
/* 111:    */   
/* 112:    */   public int getLength()
/* 113:    */   {
/* 114:153 */     return 22;
/* 115:    */   }
/* 116:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.sl.image.ImageHeaderWMF
 * JD-Core Version:    0.7.0.1
 */