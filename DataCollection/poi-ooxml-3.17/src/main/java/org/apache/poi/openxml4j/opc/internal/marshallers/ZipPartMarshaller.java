/*   1:    */ package org.apache.poi.openxml4j.opc.internal.marshallers;
/*   2:    */ 
/*   3:    */ import java.io.IOException;
/*   4:    */ import java.io.InputStream;
/*   5:    */ import java.io.OutputStream;
/*   6:    */ import java.net.URI;
/*   7:    */ import java.util.zip.ZipEntry;
/*   8:    */ import java.util.zip.ZipOutputStream;
/*   9:    */ import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
/*  10:    */ import org.apache.poi.openxml4j.opc.PackagePart;
/*  11:    */ import org.apache.poi.openxml4j.opc.PackagePartName;
/*  12:    */ import org.apache.poi.openxml4j.opc.PackageRelationship;
/*  13:    */ import org.apache.poi.openxml4j.opc.PackageRelationshipCollection;
/*  14:    */ import org.apache.poi.openxml4j.opc.PackagingURIHelper;
/*  15:    */ import org.apache.poi.openxml4j.opc.StreamHelper;
/*  16:    */ import org.apache.poi.openxml4j.opc.TargetMode;
/*  17:    */ import org.apache.poi.openxml4j.opc.internal.PartMarshaller;
/*  18:    */ import org.apache.poi.openxml4j.opc.internal.ZipHelper;
/*  19:    */ import org.apache.poi.util.DocumentHelper;
/*  20:    */ import org.apache.poi.util.POILogFactory;
/*  21:    */ import org.apache.poi.util.POILogger;
/*  22:    */ import org.apache.poi.xssf.usermodel.XSSFRelation;
/*  23:    */ import org.w3c.dom.Document;
/*  24:    */ import org.w3c.dom.Element;
/*  25:    */ 
/*  26:    */ public final class ZipPartMarshaller
/*  27:    */   implements PartMarshaller
/*  28:    */ {
/*  29: 49 */   private static final POILogger logger = POILogFactory.getLogger(ZipPartMarshaller.class);
/*  30:    */   
/*  31:    */   public boolean marshall(PackagePart part, OutputStream os)
/*  32:    */     throws OpenXML4JException
/*  33:    */   {
/*  34: 60 */     if (!(os instanceof ZipOutputStream))
/*  35:    */     {
/*  36: 61 */       logger.log(7, new Object[] { "Unexpected class " + os.getClass().getName() });
/*  37: 62 */       throw new OpenXML4JException("ZipOutputStream expected !");
/*  38:    */     }
/*  39: 69 */     if ((part.getSize() == 0L) && (part.getPartName().getName().equals(XSSFRelation.SHARED_STRINGS.getDefaultFileName()))) {
/*  40: 70 */       return true;
/*  41:    */     }
/*  42: 73 */     ZipOutputStream zos = (ZipOutputStream)os;
/*  43: 74 */     ZipEntry partEntry = new ZipEntry(ZipHelper.getZipItemNameFromOPCName(part.getPartName().getURI().getPath()));
/*  44:    */     try
/*  45:    */     {
/*  46: 79 */       zos.putNextEntry(partEntry);
/*  47:    */       
/*  48:    */ 
/*  49: 82 */       InputStream ins = part.getInputStream();
/*  50: 83 */       byte[] buff = new byte[8192];
/*  51: 84 */       while (ins.available() > 0)
/*  52:    */       {
/*  53: 85 */         int resultRead = ins.read(buff);
/*  54: 86 */         if (resultRead == -1) {
/*  55:    */           break;
/*  56:    */         }
/*  57: 90 */         zos.write(buff, 0, resultRead);
/*  58:    */       }
/*  59: 92 */       zos.closeEntry();
/*  60:    */     }
/*  61:    */     catch (IOException ioe)
/*  62:    */     {
/*  63: 94 */       logger.log(7, new Object[] { "Cannot write: " + part.getPartName() + ": in ZIP", ioe });
/*  64:    */       
/*  65: 96 */       return false;
/*  66:    */     }
/*  67:100 */     if (part.hasRelationships())
/*  68:    */     {
/*  69:101 */       PackagePartName relationshipPartName = PackagingURIHelper.getRelationshipPartName(part.getPartName());
/*  70:    */       
/*  71:    */ 
/*  72:104 */       marshallRelationshipPart(part.getRelationships(), relationshipPartName, zos);
/*  73:    */     }
/*  74:108 */     return true;
/*  75:    */   }
/*  76:    */   
/*  77:    */   public static boolean marshallRelationshipPart(PackageRelationshipCollection rels, PackagePartName relPartName, ZipOutputStream zos)
/*  78:    */   {
/*  79:126 */     Document xmlOutDoc = DocumentHelper.createDocument();
/*  80:    */     
/*  81:    */ 
/*  82:129 */     Element root = xmlOutDoc.createElementNS("http://schemas.openxmlformats.org/package/2006/relationships", "Relationships");
/*  83:130 */     xmlOutDoc.appendChild(root);
/*  84:    */     
/*  85:    */ 
/*  86:    */ 
/*  87:    */ 
/*  88:    */ 
/*  89:    */ 
/*  90:    */ 
/*  91:138 */     URI sourcePartURI = PackagingURIHelper.getSourcePartUriFromRelationshipPartUri(relPartName.getURI());
/*  92:141 */     for (PackageRelationship rel : rels)
/*  93:    */     {
/*  94:143 */       Element relElem = xmlOutDoc.createElementNS("http://schemas.openxmlformats.org/package/2006/relationships", "Relationship");
/*  95:144 */       root.appendChild(relElem);
/*  96:    */       
/*  97:    */ 
/*  98:147 */       relElem.setAttribute("Id", rel.getId());
/*  99:    */       
/* 100:    */ 
/* 101:150 */       relElem.setAttribute("Type", rel.getRelationshipType());
/* 102:    */       
/* 103:    */ 
/* 104:    */ 
/* 105:154 */       URI uri = rel.getTargetURI();
/* 106:    */       String targetValue;
/* 107:155 */       if (rel.getTargetMode() == TargetMode.EXTERNAL)
/* 108:    */       {
/* 109:158 */         String targetValue = uri.toString();
/* 110:    */         
/* 111:    */ 
/* 112:161 */         relElem.setAttribute("TargetMode", "External");
/* 113:    */       }
/* 114:    */       else
/* 115:    */       {
/* 116:163 */         URI targetURI = rel.getTargetURI();
/* 117:164 */         targetValue = PackagingURIHelper.relativizeURI(sourcePartURI, targetURI, true).toString();
/* 118:    */       }
/* 119:167 */       relElem.setAttribute("Target", targetValue);
/* 120:    */     }
/* 121:170 */     xmlOutDoc.normalize();
/* 122:    */     
/* 123:    */ 
/* 124:    */ 
/* 125:    */ 
/* 126:    */ 
/* 127:176 */     ZipEntry ctEntry = new ZipEntry(ZipHelper.getZipURIFromOPCName(relPartName.getURI().toASCIIString()).getPath());
/* 128:    */     try
/* 129:    */     {
/* 130:179 */       zos.putNextEntry(ctEntry);
/* 131:180 */       if (!StreamHelper.saveXmlInStream(xmlOutDoc, zos)) {
/* 132:181 */         return false;
/* 133:    */       }
/* 134:183 */       zos.closeEntry();
/* 135:    */     }
/* 136:    */     catch (IOException e)
/* 137:    */     {
/* 138:185 */       logger.log(7, new Object[] { "Cannot create zip entry " + relPartName, e });
/* 139:186 */       return false;
/* 140:    */     }
/* 141:188 */     return true;
/* 142:    */   }
/* 143:    */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.openxml4j.opc.internal.marshallers.ZipPartMarshaller
 * JD-Core Version:    0.7.0.1
 */