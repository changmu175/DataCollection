/*   1:    */ package org.apache.poi;
/*   2:    */ 
/*   3:    */ import java.io.Closeable;
/*   4:    */ import java.io.IOException;
/*   5:    */ import java.io.OutputStream;
/*   6:    */ import java.util.HashMap;
/*   7:    */ import java.util.HashSet;
/*   8:    */ import java.util.List;
/*   9:    */ import java.util.Map;
/*  10:    */ import java.util.Set;
/*  11:    */ import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
/*  12:    */ import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
/*  13:    */ import org.apache.poi.openxml4j.opc.OPCPackage;
/*  14:    */ import org.apache.poi.openxml4j.opc.PackageAccess;
/*  15:    */ import org.apache.poi.openxml4j.opc.PackagePart;
/*  16:    */ import org.apache.poi.openxml4j.opc.PackageRelationship;
/*  17:    */ import org.apache.poi.openxml4j.opc.PackageRelationshipCollection;
/*  18:    */ import org.apache.xmlbeans.impl.common.SystemCache;
/*  19:    */ 
/*  20:    */ public abstract class POIXMLDocument
/*  21:    */   extends POIXMLDocumentPart
/*  22:    */   implements Closeable
/*  23:    */ {
/*  24:    */   public static final String DOCUMENT_CREATOR = "Apache POI";
/*  25:    */   public static final String OLE_OBJECT_REL_TYPE = "http://schemas.openxmlformats.org/officeDocument/2006/relationships/oleObject";
/*  26:    */   public static final String PACK_OBJECT_REL_TYPE = "http://schemas.openxmlformats.org/officeDocument/2006/relationships/package";
/*  27:    */   private OPCPackage pkg;
/*  28:    */   private POIXMLProperties properties;
/*  29:    */   
/*  30:    */   protected POIXMLDocument(OPCPackage pkg)
/*  31:    */   {
/*  32: 61 */     super(pkg);
/*  33: 62 */     init(pkg);
/*  34:    */   }
/*  35:    */   
/*  36:    */   protected POIXMLDocument(OPCPackage pkg, String coreDocumentRel)
/*  37:    */   {
/*  38: 66 */     super(pkg, coreDocumentRel);
/*  39: 67 */     init(pkg);
/*  40:    */   }
/*  41:    */   
/*  42:    */   private void init(OPCPackage p)
/*  43:    */   {
/*  44: 71 */     this.pkg = p;
/*  45:    */     
/*  46:    */ 
/*  47:    */ 
/*  48:    */ 
/*  49: 76 */     SystemCache.get().setSaxLoader(null);
/*  50:    */   }
/*  51:    */   
/*  52:    */   public static OPCPackage openPackage(String path)
/*  53:    */     throws IOException
/*  54:    */   {
/*  55:    */     try
/*  56:    */     {
/*  57: 89 */       return OPCPackage.open(path);
/*  58:    */     }
/*  59:    */     catch (InvalidFormatException e)
/*  60:    */     {
/*  61: 91 */       throw new IOException(e.toString(), e);
/*  62:    */     }
/*  63:    */   }
/*  64:    */   
/*  65:    */   public OPCPackage getPackage()
/*  66:    */   {
/*  67:101 */     return this.pkg;
/*  68:    */   }
/*  69:    */   
/*  70:    */   protected PackagePart getCorePart()
/*  71:    */   {
/*  72:105 */     return getPackagePart();
/*  73:    */   }
/*  74:    */   
/*  75:    */   protected PackagePart[] getRelatedByType(String contentType)
/*  76:    */     throws InvalidFormatException
/*  77:    */   {
/*  78:124 */     PackageRelationshipCollection partsC = getPackagePart().getRelationshipsByType(contentType);
/*  79:    */     
/*  80:    */ 
/*  81:127 */     PackagePart[] parts = new PackagePart[partsC.size()];
/*  82:128 */     int count = 0;
/*  83:129 */     for (PackageRelationship rel : partsC)
/*  84:    */     {
/*  85:130 */       parts[count] = getPackagePart().getRelatedPart(rel);
/*  86:131 */       count++;
/*  87:    */     }
/*  88:133 */     return parts;
/*  89:    */   }
/*  90:    */   
/*  91:    */   public POIXMLProperties getProperties()
/*  92:    */   {
/*  93:143 */     if (this.properties == null) {
/*  94:    */       try
/*  95:    */       {
/*  96:145 */         this.properties = new POIXMLProperties(this.pkg);
/*  97:    */       }
/*  98:    */       catch (Exception e)
/*  99:    */       {
/* 100:147 */         throw new POIXMLException(e);
/* 101:    */       }
/* 102:    */     }
/* 103:150 */     return this.properties;
/* 104:    */   }
/* 105:    */   
/* 106:    */   public abstract List<PackagePart> getAllEmbedds()
/* 107:    */     throws OpenXML4JException;
/* 108:    */   
/* 109:    */   protected final void load(POIXMLFactory factory)
/* 110:    */     throws IOException
/* 111:    */   {
/* 112:163 */     Map<PackagePart, POIXMLDocumentPart> context = new HashMap();
/* 113:    */     try
/* 114:    */     {
/* 115:165 */       read(factory, context);
/* 116:    */     }
/* 117:    */     catch (OpenXML4JException e)
/* 118:    */     {
/* 119:167 */       throw new POIXMLException(e);
/* 120:    */     }
/* 121:169 */     onDocumentRead();
/* 122:170 */     context.clear();
/* 123:    */   }
/* 124:    */   
/* 125:    */   public void close()
/* 126:    */     throws IOException
/* 127:    */   {
/* 128:185 */     if (this.pkg != null)
/* 129:    */     {
/* 130:186 */       if (this.pkg.getPackageAccess() == PackageAccess.READ) {
/* 131:187 */         this.pkg.revert();
/* 132:    */       } else {
/* 133:189 */         this.pkg.close();
/* 134:    */       }
/* 135:191 */       this.pkg = null;
/* 136:    */     }
/* 137:    */   }
/* 138:    */   
/* 139:    */   public final void write(OutputStream stream)
/* 140:    */     throws IOException
/* 141:    */   {
/* 142:213 */     OPCPackage p = getPackage();
/* 143:214 */     if (p == null) {
/* 144:215 */       throw new IOException("Cannot write data, document seems to have been closed already");
/* 145:    */     }
/* 146:220 */     Set<PackagePart> context = new HashSet();
/* 147:221 */     onSave(context);
/* 148:222 */     context.clear();
/* 149:    */     
/* 150:    */ 
/* 151:225 */     getProperties().commit();
/* 152:    */     
/* 153:227 */     p.save(stream);
/* 154:    */   }
/* 155:    */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.POIXMLDocument
 * JD-Core Version:    0.7.0.1
 */