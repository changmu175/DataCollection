/*   1:    */ package org.apache.poi.openxml4j.opc.internal;
/*   2:    */ 
/*   3:    */ import java.io.ByteArrayInputStream;
/*   4:    */ import java.io.ByteArrayOutputStream;
/*   5:    */ import java.io.IOException;
/*   6:    */ import java.io.InputStream;
/*   7:    */ import java.io.OutputStream;
/*   8:    */ import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
/*   9:    */ import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
/*  10:    */ import org.apache.poi.openxml4j.opc.OPCPackage;
/*  11:    */ import org.apache.poi.openxml4j.opc.PackagePart;
/*  12:    */ import org.apache.poi.openxml4j.opc.PackagePartName;
/*  13:    */ import org.apache.poi.openxml4j.opc.internal.marshallers.ZipPartMarshaller;
/*  14:    */ import org.apache.poi.util.IOUtils;
/*  15:    */ 
/*  16:    */ public final class MemoryPackagePart
/*  17:    */   extends PackagePart
/*  18:    */ {
/*  19:    */   protected byte[] data;
/*  20:    */   
/*  21:    */   public MemoryPackagePart(OPCPackage pack, PackagePartName partName, String contentType)
/*  22:    */     throws InvalidFormatException
/*  23:    */   {
/*  24: 61 */     super(pack, partName, contentType);
/*  25:    */   }
/*  26:    */   
/*  27:    */   public MemoryPackagePart(OPCPackage pack, PackagePartName partName, String contentType, boolean loadRelationships)
/*  28:    */     throws InvalidFormatException
/*  29:    */   {
/*  30: 81 */     super(pack, partName, new ContentType(contentType), loadRelationships);
/*  31:    */   }
/*  32:    */   
/*  33:    */   protected InputStream getInputStreamImpl()
/*  34:    */   {
/*  35: 89 */     if (this.data == null) {
/*  36: 90 */       this.data = new byte[0];
/*  37:    */     }
/*  38: 92 */     return new ByteArrayInputStream(this.data);
/*  39:    */   }
/*  40:    */   
/*  41:    */   protected OutputStream getOutputStreamImpl()
/*  42:    */   {
/*  43: 97 */     return new MemoryPackagePartOutputStream(this);
/*  44:    */   }
/*  45:    */   
/*  46:    */   public long getSize()
/*  47:    */   {
/*  48:102 */     return this.data == null ? 0L : this.data.length;
/*  49:    */   }
/*  50:    */   
/*  51:    */   public void clear()
/*  52:    */   {
/*  53:107 */     this.data = null;
/*  54:    */   }
/*  55:    */   
/*  56:    */   public boolean save(OutputStream os)
/*  57:    */     throws OpenXML4JException
/*  58:    */   {
/*  59:112 */     return new ZipPartMarshaller().marshall(this, os);
/*  60:    */   }
/*  61:    */   
/*  62:    */   public boolean load(InputStream ios)
/*  63:    */     throws InvalidFormatException
/*  64:    */   {
/*  65:118 */     ByteArrayOutputStream baos = new ByteArrayOutputStream();
/*  66:    */     try
/*  67:    */     {
/*  68:120 */       IOUtils.copy(ios, baos);
/*  69:    */     }
/*  70:    */     catch (IOException e)
/*  71:    */     {
/*  72:122 */       throw new InvalidFormatException(e.getMessage());
/*  73:    */     }
/*  74:126 */     this.data = baos.toByteArray();
/*  75:    */     
/*  76:    */ 
/*  77:129 */     return true;
/*  78:    */   }
/*  79:    */   
/*  80:    */   public void close() {}
/*  81:    */   
/*  82:    */   public void flush() {}
/*  83:    */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.openxml4j.opc.internal.MemoryPackagePart
 * JD-Core Version:    0.7.0.1
 */