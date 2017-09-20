/*   1:    */ package org.apache.poi.util;
/*   2:    */ 
/*   3:    */ import java.io.File;
/*   4:    */ import java.io.IOException;
/*   5:    */ import java.io.InputStream;
/*   6:    */ import java.io.OutputStream;
/*   7:    */ import java.net.URI;
/*   8:    */ import org.apache.poi.POIXMLException;
/*   9:    */ import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
/*  10:    */ import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
/*  11:    */ import org.apache.poi.openxml4j.opc.OPCPackage;
/*  12:    */ import org.apache.poi.openxml4j.opc.PackagePart;
/*  13:    */ import org.apache.poi.openxml4j.opc.PackagePartName;
/*  14:    */ import org.apache.poi.openxml4j.opc.PackageProperties;
/*  15:    */ import org.apache.poi.openxml4j.opc.PackageRelationship;
/*  16:    */ import org.apache.poi.openxml4j.opc.PackageRelationshipCollection;
/*  17:    */ import org.apache.poi.openxml4j.opc.PackagingURIHelper;
/*  18:    */ import org.apache.poi.openxml4j.opc.TargetMode;
/*  19:    */ import org.apache.poi.openxml4j.util.Nullable;
/*  20:    */ 
/*  21:    */ public final class PackageHelper
/*  22:    */ {
/*  23:    */   public static OPCPackage open(InputStream is)
/*  24:    */     throws IOException
/*  25:    */   {
/*  26:    */     try
/*  27:    */     {
/*  28: 37 */       return OPCPackage.open(is);
/*  29:    */     }
/*  30:    */     catch (InvalidFormatException e)
/*  31:    */     {
/*  32: 39 */       throw new POIXMLException(e);
/*  33:    */     }
/*  34:    */   }
/*  35:    */   
/*  36:    */   public static OPCPackage clone(OPCPackage pkg, File file)
/*  37:    */     throws OpenXML4JException, IOException
/*  38:    */   {
/*  39: 52 */     String path = file.getAbsolutePath();
/*  40:    */     
/*  41: 54 */     OPCPackage dest = OPCPackage.create(path);
/*  42: 55 */     PackageRelationshipCollection rels = pkg.getRelationships();
/*  43: 56 */     for (PackageRelationship rel : rels)
/*  44:    */     {
/*  45: 57 */       PackagePart part = pkg.getPart(rel);
/*  46: 59 */       if (rel.getRelationshipType().equals("http://schemas.openxmlformats.org/package/2006/relationships/metadata/core-properties"))
/*  47:    */       {
/*  48: 60 */         copyProperties(pkg.getPackageProperties(), dest.getPackageProperties());
/*  49:    */       }
/*  50:    */       else
/*  51:    */       {
/*  52: 63 */         dest.addRelationship(part.getPartName(), rel.getTargetMode(), rel.getRelationshipType());
/*  53: 64 */         PackagePart part_tgt = dest.createPart(part.getPartName(), part.getContentType());
/*  54:    */         
/*  55: 66 */         OutputStream out = part_tgt.getOutputStream();
/*  56: 67 */         IOUtils.copy(part.getInputStream(), out);
/*  57: 68 */         out.close();
/*  58: 70 */         if (part.hasRelationships()) {
/*  59: 71 */           copy(pkg, part, dest, part_tgt);
/*  60:    */         }
/*  61:    */       }
/*  62:    */     }
/*  63: 74 */     dest.close();
/*  64:    */     
/*  65:    */ 
/*  66: 77 */     new File(path).deleteOnExit();
/*  67: 78 */     return OPCPackage.open(path);
/*  68:    */   }
/*  69:    */   
/*  70:    */   private static void copy(OPCPackage pkg, PackagePart part, OPCPackage tgt, PackagePart part_tgt)
/*  71:    */     throws OpenXML4JException, IOException
/*  72:    */   {
/*  73: 85 */     PackageRelationshipCollection rels = part.getRelationships();
/*  74: 86 */     if (rels != null) {
/*  75: 86 */       for (PackageRelationship rel : rels) {
/*  76: 88 */         if (rel.getTargetMode() == TargetMode.EXTERNAL)
/*  77:    */         {
/*  78: 89 */           part_tgt.addExternalRelationship(rel.getTargetURI().toString(), rel.getRelationshipType(), rel.getId());
/*  79:    */         }
/*  80:    */         else
/*  81:    */         {
/*  82: 93 */           URI uri = rel.getTargetURI();
/*  83: 95 */           if (uri.getRawFragment() != null)
/*  84:    */           {
/*  85: 96 */             part_tgt.addRelationship(uri, rel.getTargetMode(), rel.getRelationshipType(), rel.getId());
/*  86:    */           }
/*  87:    */           else
/*  88:    */           {
/*  89: 99 */             PackagePartName relName = PackagingURIHelper.createPartName(rel.getTargetURI());
/*  90:100 */             PackagePart p = pkg.getPart(relName);
/*  91:101 */             part_tgt.addRelationship(p.getPartName(), rel.getTargetMode(), rel.getRelationshipType(), rel.getId());
/*  92:107 */             if (!tgt.containPart(p.getPartName()))
/*  93:    */             {
/*  94:108 */               PackagePart dest = tgt.createPart(p.getPartName(), p.getContentType());
/*  95:109 */               OutputStream out = dest.getOutputStream();
/*  96:110 */               IOUtils.copy(p.getInputStream(), out);
/*  97:111 */               out.close();
/*  98:112 */               copy(pkg, p, tgt, dest);
/*  99:    */             }
/* 100:    */           }
/* 101:    */         }
/* 102:    */       }
/* 103:    */     }
/* 104:    */   }
/* 105:    */   
/* 106:    */   private static void copyProperties(PackageProperties src, PackageProperties tgt)
/* 107:    */   {
/* 108:124 */     tgt.setCategoryProperty((String)src.getCategoryProperty().getValue());
/* 109:125 */     tgt.setContentStatusProperty((String)src.getContentStatusProperty().getValue());
/* 110:126 */     tgt.setContentTypeProperty((String)src.getContentTypeProperty().getValue());
/* 111:127 */     tgt.setCreatorProperty((String)src.getCreatorProperty().getValue());
/* 112:128 */     tgt.setDescriptionProperty((String)src.getDescriptionProperty().getValue());
/* 113:129 */     tgt.setIdentifierProperty((String)src.getIdentifierProperty().getValue());
/* 114:130 */     tgt.setKeywordsProperty((String)src.getKeywordsProperty().getValue());
/* 115:131 */     tgt.setLanguageProperty((String)src.getLanguageProperty().getValue());
/* 116:132 */     tgt.setRevisionProperty((String)src.getRevisionProperty().getValue());
/* 117:133 */     tgt.setSubjectProperty((String)src.getSubjectProperty().getValue());
/* 118:134 */     tgt.setTitleProperty((String)src.getTitleProperty().getValue());
/* 119:135 */     tgt.setVersionProperty((String)src.getVersionProperty().getValue());
/* 120:    */   }
/* 121:    */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.util.PackageHelper
 * JD-Core Version:    0.7.0.1
 */