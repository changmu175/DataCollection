/*   1:    */ package org.apache.poi;
/*   2:    */ 
/*   3:    */ import java.lang.reflect.InvocationTargetException;
/*   4:    */ import java.net.URI;
/*   5:    */ import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
/*   6:    */ import org.apache.poi.openxml4j.opc.PackagePart;
/*   7:    */ import org.apache.poi.openxml4j.opc.PackagePartName;
/*   8:    */ import org.apache.poi.openxml4j.opc.PackageRelationship;
/*   9:    */ import org.apache.poi.util.POILogFactory;
/*  10:    */ import org.apache.poi.util.POILogger;
/*  11:    */ 
/*  12:    */ public abstract class POIXMLFactory
/*  13:    */ {
/*  14: 31 */   private static final POILogger LOGGER = POILogFactory.getLogger(POIXMLFactory.class);
/*  15: 33 */   private static final Class<?>[] PARENT_PART = { POIXMLDocumentPart.class, PackagePart.class };
/*  16: 34 */   private static final Class<?>[] ORPHAN_PART = { PackagePart.class };
/*  17:    */   
/*  18:    */   public POIXMLDocumentPart createDocumentPart(POIXMLDocumentPart parent, PackagePart part)
/*  19:    */   {
/*  20: 47 */     PackageRelationship rel = getPackageRelationship(parent, part);
/*  21: 48 */     POIXMLRelation descriptor = getDescriptor(rel.getRelationshipType());
/*  22: 50 */     if ((descriptor == null) || (descriptor.getRelationClass() == null))
/*  23:    */     {
/*  24: 51 */       LOGGER.log(1, new Object[] { "using default POIXMLDocumentPart for " + rel.getRelationshipType() });
/*  25: 52 */       return new POIXMLDocumentPart(parent, part);
/*  26:    */     }
/*  27: 55 */     Class<? extends POIXMLDocumentPart> cls = descriptor.getRelationClass();
/*  28:    */     try
/*  29:    */     {
/*  30: 58 */       return createDocumentPart(cls, PARENT_PART, new Object[] { parent, part });
/*  31:    */     }
/*  32:    */     catch (NoSuchMethodException e)
/*  33:    */     {
/*  34: 60 */       return createDocumentPart(cls, ORPHAN_PART, new Object[] { part });
/*  35:    */     }
/*  36:    */     catch (Exception e)
/*  37:    */     {
/*  38: 63 */       throw new POIXMLException(e);
/*  39:    */     }
/*  40:    */   }
/*  41:    */   
/*  42:    */   protected abstract POIXMLDocumentPart createDocumentPart(Class<? extends POIXMLDocumentPart> paramClass, Class<?>[] paramArrayOfClass, Object[] paramArrayOfObject)
/*  43:    */     throws SecurityException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException;
/*  44:    */   
/*  45:    */   protected abstract POIXMLRelation getDescriptor(String paramString);
/*  46:    */   
/*  47:    */   public POIXMLDocumentPart newDocumentPart(POIXMLRelation descriptor)
/*  48:    */   {
/*  49:104 */     Class<? extends POIXMLDocumentPart> cls = descriptor.getRelationClass();
/*  50:    */     try
/*  51:    */     {
/*  52:106 */       return createDocumentPart(cls, null, null);
/*  53:    */     }
/*  54:    */     catch (Exception e)
/*  55:    */     {
/*  56:108 */       throw new POIXMLException(e);
/*  57:    */     }
/*  58:    */   }
/*  59:    */   
/*  60:    */   protected PackageRelationship getPackageRelationship(POIXMLDocumentPart parent, PackagePart part)
/*  61:    */   {
/*  62:    */     try
/*  63:    */     {
/*  64:126 */       partName = part.getPartName().getName();
/*  65:127 */       for (PackageRelationship pr : parent.getPackagePart().getRelationships())
/*  66:    */       {
/*  67:128 */         String packName = pr.getTargetURI().toASCIIString();
/*  68:129 */         if (packName.equalsIgnoreCase(partName)) {
/*  69:130 */           return pr;
/*  70:    */         }
/*  71:    */       }
/*  72:    */     }
/*  73:    */     catch (InvalidFormatException e)
/*  74:    */     {
/*  75:    */       String partName;
/*  76:134 */       throw new POIXMLException("error while determining package relations", e);
/*  77:    */     }
/*  78:137 */     throw new POIXMLException("package part isn't a child of the parent document.");
/*  79:    */   }
/*  80:    */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.POIXMLFactory
 * JD-Core Version:    0.7.0.1
 */