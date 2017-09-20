/*  1:   */ package org.apache.poi.openxml4j.opc.internal.unmarshallers;
/*  2:   */ 
/*  3:   */ import java.util.zip.ZipEntry;
/*  4:   */ import org.apache.poi.openxml4j.opc.OPCPackage;
/*  5:   */ import org.apache.poi.openxml4j.opc.PackagePartName;
/*  6:   */ 
/*  7:   */ public final class UnmarshallContext
/*  8:   */ {
/*  9:   */   private OPCPackage _package;
/* 10:   */   private PackagePartName partName;
/* 11:   */   private ZipEntry zipEntry;
/* 12:   */   
/* 13:   */   public UnmarshallContext(OPCPackage targetPackage, PackagePartName partName)
/* 14:   */   {
/* 15:48 */     this._package = targetPackage;
/* 16:49 */     this.partName = partName;
/* 17:   */   }
/* 18:   */   
/* 19:   */   OPCPackage getPackage()
/* 20:   */   {
/* 21:56 */     return this._package;
/* 22:   */   }
/* 23:   */   
/* 24:   */   public void setPackage(OPCPackage container)
/* 25:   */   {
/* 26:64 */     this._package = container;
/* 27:   */   }
/* 28:   */   
/* 29:   */   PackagePartName getPartName()
/* 30:   */   {
/* 31:71 */     return this.partName;
/* 32:   */   }
/* 33:   */   
/* 34:   */   public void setPartName(PackagePartName partName)
/* 35:   */   {
/* 36:79 */     this.partName = partName;
/* 37:   */   }
/* 38:   */   
/* 39:   */   ZipEntry getZipEntry()
/* 40:   */   {
/* 41:86 */     return this.zipEntry;
/* 42:   */   }
/* 43:   */   
/* 44:   */   public void setZipEntry(ZipEntry zipEntry)
/* 45:   */   {
/* 46:94 */     this.zipEntry = zipEntry;
/* 47:   */   }
/* 48:   */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.openxml4j.opc.internal.unmarshallers.UnmarshallContext
 * JD-Core Version:    0.7.0.1
 */