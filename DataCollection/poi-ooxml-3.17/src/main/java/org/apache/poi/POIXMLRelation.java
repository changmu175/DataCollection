/*   1:    */ package org.apache.poi;
/*   2:    */ 
/*   3:    */ import java.io.IOException;
/*   4:    */ import java.io.InputStream;
/*   5:    */ import java.util.Iterator;
/*   6:    */ import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
/*   7:    */ import org.apache.poi.openxml4j.opc.OPCPackage;
/*   8:    */ import org.apache.poi.openxml4j.opc.PackagePart;
/*   9:    */ import org.apache.poi.openxml4j.opc.PackagePartName;
/*  10:    */ import org.apache.poi.openxml4j.opc.PackageRelationship;
/*  11:    */ import org.apache.poi.openxml4j.opc.PackageRelationshipCollection;
/*  12:    */ import org.apache.poi.openxml4j.opc.PackagingURIHelper;
/*  13:    */ import org.apache.poi.util.POILogFactory;
/*  14:    */ import org.apache.poi.util.POILogger;
/*  15:    */ 
/*  16:    */ public abstract class POIXMLRelation
/*  17:    */ {
/*  18: 37 */   private static final POILogger log = POILogFactory.getLogger(POIXMLRelation.class);
/*  19:    */   private String _type;
/*  20:    */   private String _relation;
/*  21:    */   private String _defaultName;
/*  22:    */   private Class<? extends POIXMLDocumentPart> _cls;
/*  23:    */   
/*  24:    */   public POIXMLRelation(String type, String rel, String defaultName, Class<? extends POIXMLDocumentPart> cls)
/*  25:    */   {
/*  26: 68 */     this._type = type;
/*  27: 69 */     this._relation = rel;
/*  28: 70 */     this._defaultName = defaultName;
/*  29: 71 */     this._cls = cls;
/*  30:    */   }
/*  31:    */   
/*  32:    */   public POIXMLRelation(String type, String rel, String defaultName)
/*  33:    */   {
/*  34: 82 */     this(type, rel, defaultName, null);
/*  35:    */   }
/*  36:    */   
/*  37:    */   public String getContentType()
/*  38:    */   {
/*  39: 91 */     return this._type;
/*  40:    */   }
/*  41:    */   
/*  42:    */   public String getRelation()
/*  43:    */   {
/*  44:102 */     return this._relation;
/*  45:    */   }
/*  46:    */   
/*  47:    */   public String getDefaultFileName()
/*  48:    */   {
/*  49:112 */     return this._defaultName;
/*  50:    */   }
/*  51:    */   
/*  52:    */   public String getFileName(int index)
/*  53:    */   {
/*  54:122 */     if (!this._defaultName.contains("#")) {
/*  55:124 */       return getDefaultFileName();
/*  56:    */     }
/*  57:126 */     return this._defaultName.replace("#", Integer.toString(index));
/*  58:    */   }
/*  59:    */   
/*  60:    */   public Integer getFileNameIndex(POIXMLDocumentPart part)
/*  61:    */   {
/*  62:137 */     String regex = this._defaultName.replace("#", "(\\d+)");
/*  63:138 */     return Integer.valueOf(part.getPackagePart().getPartName().getName().replaceAll(regex, "$1"));
/*  64:    */   }
/*  65:    */   
/*  66:    */   public Class<? extends POIXMLDocumentPart> getRelationClass()
/*  67:    */   {
/*  68:147 */     return this._cls;
/*  69:    */   }
/*  70:    */   
/*  71:    */   public InputStream getContents(PackagePart corePart)
/*  72:    */     throws IOException, InvalidFormatException
/*  73:    */   {
/*  74:158 */     PackageRelationshipCollection prc = corePart.getRelationshipsByType(getRelation());
/*  75:    */     
/*  76:160 */     Iterator<PackageRelationship> it = prc.iterator();
/*  77:161 */     if (it.hasNext())
/*  78:    */     {
/*  79:162 */       PackageRelationship rel = (PackageRelationship)it.next();
/*  80:163 */       PackagePartName relName = PackagingURIHelper.createPartName(rel.getTargetURI());
/*  81:164 */       PackagePart part = corePart.getPackage().getPart(relName);
/*  82:165 */       return part.getInputStream();
/*  83:    */     }
/*  84:167 */     log.log(5, new Object[] { "No part " + getDefaultFileName() + " found" });
/*  85:168 */     return null;
/*  86:    */   }
/*  87:    */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.POIXMLRelation
 * JD-Core Version:    0.7.0.1
 */