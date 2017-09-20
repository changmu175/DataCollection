/*   1:    */ package org.apache.poi.hpsf;
/*   2:    */ 
/*   3:    */ import java.io.IOException;
/*   4:    */ import java.io.OutputStream;
/*   5:    */ import org.apache.poi.util.LittleEndian;
/*   6:    */ import org.apache.poi.util.Removal;
/*   7:    */ 
/*   8:    */ @Deprecated
/*   9:    */ @Removal(version="3.18")
/*  10:    */ public class TypeWriter
/*  11:    */ {
/*  12:    */   public static int writeToStream(OutputStream out, short n)
/*  13:    */     throws IOException
/*  14:    */   {
/*  15: 47 */     LittleEndian.putShort(out, n);
/*  16: 48 */     return 2;
/*  17:    */   }
/*  18:    */   
/*  19:    */   public static int writeToStream(OutputStream out, int n)
/*  20:    */     throws IOException
/*  21:    */   {
/*  22: 62 */     LittleEndian.putInt(n, out);
/*  23: 63 */     return 4;
/*  24:    */   }
/*  25:    */   
/*  26:    */   public static int writeToStream(OutputStream out, long n)
/*  27:    */     throws IOException
/*  28:    */   {
/*  29: 77 */     LittleEndian.putLong(n, out);
/*  30: 78 */     return 8;
/*  31:    */   }
/*  32:    */   
/*  33:    */   public static void writeUShortToStream(OutputStream out, int n)
/*  34:    */     throws IOException
/*  35:    */   {
/*  36: 91 */     int high = n & 0xFFFF0000;
/*  37: 92 */     if (high != 0) {
/*  38: 93 */       throw new IllegalPropertySetDataException("Value " + n + " cannot be represented by 2 bytes.");
/*  39:    */     }
/*  40: 96 */     LittleEndian.putUShort(n, out);
/*  41:    */   }
/*  42:    */   
/*  43:    */   public static int writeUIntToStream(OutputStream out, long n)
/*  44:    */     throws IOException
/*  45:    */   {
/*  46:110 */     long high = n & 0x0;
/*  47:111 */     if ((high != 0L) && (high != -4294967296L)) {
/*  48:112 */       throw new IllegalPropertySetDataException("Value " + n + " cannot be represented by 4 bytes.");
/*  49:    */     }
/*  50:115 */     LittleEndian.putUInt(n, out);
/*  51:116 */     return 4;
/*  52:    */   }
/*  53:    */   
/*  54:    */   public static int writeToStream(OutputStream out, ClassID n)
/*  55:    */     throws IOException
/*  56:    */   {
/*  57:130 */     byte[] b = new byte[16];
/*  58:131 */     n.write(b, 0);
/*  59:132 */     out.write(b, 0, b.length);
/*  60:133 */     return b.length;
/*  61:    */   }
/*  62:    */   
/*  63:    */   public static void writeToStream(OutputStream out, Property[] properties, int codepage)
/*  64:    */     throws IOException, UnsupportedVariantTypeException
/*  65:    */   {
/*  66:155 */     if (properties == null) {
/*  67:156 */       return;
/*  68:    */     }
/*  69:161 */     for (int i = 0; i < properties.length; i++)
/*  70:    */     {
/*  71:163 */       Property p = properties[i];
/*  72:164 */       writeUIntToStream(out, p.getID());
/*  73:165 */       writeUIntToStream(out, p.getSize(codepage));
/*  74:    */     }
/*  75:169 */     for (int i = 0; i < properties.length; i++)
/*  76:    */     {
/*  77:171 */       Property p = properties[i];
/*  78:172 */       long type = p.getType();
/*  79:173 */       writeUIntToStream(out, type);
/*  80:174 */       VariantSupport.write(out, (int)type, p.getValue(), codepage);
/*  81:    */     }
/*  82:    */   }
/*  83:    */   
/*  84:    */   public static int writeToStream(OutputStream out, double n)
/*  85:    */     throws IOException
/*  86:    */   {
/*  87:191 */     LittleEndian.putDouble(n, out);
/*  88:192 */     return 8;
/*  89:    */   }
/*  90:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hpsf.TypeWriter
 * JD-Core Version:    0.7.0.1
 */