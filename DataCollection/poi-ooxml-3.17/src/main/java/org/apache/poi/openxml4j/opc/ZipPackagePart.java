/*   1:    */ package org.apache.poi.openxml4j.opc;
/*   2:    */ 
/*   3:    */ import java.io.IOException;
/*   4:    */ import java.io.InputStream;
/*   5:    */ import java.io.OutputStream;
/*   6:    */ import java.util.zip.ZipEntry;
/*   7:    */ import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
/*   8:    */ import org.apache.poi.openxml4j.exceptions.InvalidOperationException;
/*   9:    */ import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
/*  10:    */ import org.apache.poi.openxml4j.opc.internal.marshallers.ZipPartMarshaller;
/*  11:    */ import org.apache.poi.openxml4j.util.ZipEntrySource;
/*  12:    */ import org.apache.poi.util.NotImplemented;
/*  13:    */ 
/*  14:    */ public class ZipPackagePart
/*  15:    */   extends PackagePart
/*  16:    */ {
/*  17:    */   private ZipEntry zipEntry;
/*  18:    */   
/*  19:    */   public ZipPackagePart(OPCPackage container, PackagePartName partName, String contentType)
/*  20:    */     throws InvalidFormatException
/*  21:    */   {
/*  22: 59 */     super(container, partName, contentType);
/*  23:    */   }
/*  24:    */   
/*  25:    */   public ZipPackagePart(OPCPackage container, ZipEntry zipEntry, PackagePartName partName, String contentType)
/*  26:    */     throws InvalidFormatException
/*  27:    */   {
/*  28: 79 */     super(container, partName, contentType);
/*  29: 80 */     this.zipEntry = zipEntry;
/*  30:    */   }
/*  31:    */   
/*  32:    */   public ZipEntry getZipArchive()
/*  33:    */   {
/*  34: 89 */     return this.zipEntry;
/*  35:    */   }
/*  36:    */   
/*  37:    */   protected InputStream getInputStreamImpl()
/*  38:    */     throws IOException
/*  39:    */   {
/*  40:102 */     return ((ZipPackage)this._container).getZipArchive().getInputStream(this.zipEntry);
/*  41:    */   }
/*  42:    */   
/*  43:    */   protected OutputStream getOutputStreamImpl()
/*  44:    */   {
/*  45:114 */     return null;
/*  46:    */   }
/*  47:    */   
/*  48:    */   public long getSize()
/*  49:    */   {
/*  50:119 */     return this.zipEntry.getSize();
/*  51:    */   }
/*  52:    */   
/*  53:    */   public boolean save(OutputStream os)
/*  54:    */     throws OpenXML4JException
/*  55:    */   {
/*  56:124 */     return new ZipPartMarshaller().marshall(this, os);
/*  57:    */   }
/*  58:    */   
/*  59:    */   @NotImplemented
/*  60:    */   public boolean load(InputStream ios)
/*  61:    */   {
/*  62:130 */     throw new InvalidOperationException("Method not implemented !");
/*  63:    */   }
/*  64:    */   
/*  65:    */   @NotImplemented
/*  66:    */   public void close()
/*  67:    */   {
/*  68:136 */     throw new InvalidOperationException("Method not implemented !");
/*  69:    */   }
/*  70:    */   
/*  71:    */   @NotImplemented
/*  72:    */   public void flush()
/*  73:    */   {
/*  74:142 */     throw new InvalidOperationException("Method not implemented !");
/*  75:    */   }
/*  76:    */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.openxml4j.opc.ZipPackagePart
 * JD-Core Version:    0.7.0.1
 */