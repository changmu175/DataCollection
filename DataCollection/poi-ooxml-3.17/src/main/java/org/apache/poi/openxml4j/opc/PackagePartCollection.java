/*  1:   */ package org.apache.poi.openxml4j.opc;
/*  2:   */ 
/*  3:   */ import java.io.Serializable;
/*  4:   */ import java.net.URI;
/*  5:   */ import java.util.ArrayList;
/*  6:   */ import java.util.Collection;
/*  7:   */ import java.util.Collections;
/*  8:   */ import java.util.HashMap;
/*  9:   */ import java.util.HashSet;
/* 10:   */ import org.apache.poi.openxml4j.exceptions.InvalidOperationException;
/* 11:   */ 
/* 12:   */ public final class PackagePartCollection
/* 13:   */   implements Serializable
/* 14:   */ {
/* 15:   */   private static final long serialVersionUID = 2515031135957635517L;
/* 16:39 */   private HashSet<String> registerPartNameStr = new HashSet();
/* 17:42 */   private final HashMap<PackagePartName, PackagePart> packagePartLookup = new HashMap();
/* 18:   */   
/* 19:   */   public PackagePart put(PackagePartName partName, PackagePart part)
/* 20:   */   {
/* 21:55 */     String[] segments = partName.getURI().toASCIIString().split(PackagingURIHelper.FORWARD_SLASH_STRING);
/* 22:   */     
/* 23:57 */     StringBuilder concatSeg = new StringBuilder();
/* 24:58 */     for (String seg : segments)
/* 25:   */     {
/* 26:59 */       if (!seg.equals("")) {
/* 27:60 */         concatSeg.append(PackagingURIHelper.FORWARD_SLASH_CHAR);
/* 28:   */       }
/* 29:61 */       concatSeg.append(seg);
/* 30:62 */       if (this.registerPartNameStr.contains(concatSeg.toString())) {
/* 31:63 */         throw new InvalidOperationException("You can't add a part with a part name derived from another part ! [M1.11]");
/* 32:   */       }
/* 33:   */     }
/* 34:67 */     this.registerPartNameStr.add(partName.getName());
/* 35:68 */     return (PackagePart)this.packagePartLookup.put(partName, part);
/* 36:   */   }
/* 37:   */   
/* 38:   */   public PackagePart remove(PackagePartName key)
/* 39:   */   {
/* 40:72 */     this.registerPartNameStr.remove(key.getName());
/* 41:73 */     return (PackagePart)this.packagePartLookup.remove(key);
/* 42:   */   }
/* 43:   */   
/* 44:   */   public Collection<PackagePart> sortedValues()
/* 45:   */   {
/* 46:82 */     ArrayList<PackagePart> packageParts = new ArrayList(this.packagePartLookup.values());
/* 47:83 */     Collections.sort(packageParts);
/* 48:84 */     return packageParts;
/* 49:   */   }
/* 50:   */   
/* 51:   */   public boolean containsKey(PackagePartName partName)
/* 52:   */   {
/* 53:89 */     return this.packagePartLookup.containsKey(partName);
/* 54:   */   }
/* 55:   */   
/* 56:   */   public PackagePart get(PackagePartName partName)
/* 57:   */   {
/* 58:93 */     return (PackagePart)this.packagePartLookup.get(partName);
/* 59:   */   }
/* 60:   */   
/* 61:   */   public int size()
/* 62:   */   {
/* 63:97 */     return this.packagePartLookup.size();
/* 64:   */   }
/* 65:   */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.openxml4j.opc.PackagePartCollection
 * JD-Core Version:    0.7.0.1
 */