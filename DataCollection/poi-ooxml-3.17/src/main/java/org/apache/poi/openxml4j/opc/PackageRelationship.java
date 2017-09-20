/*   1:    */ package org.apache.poi.openxml4j.opc;
/*   2:    */ 
/*   3:    */ import java.net.URI;
/*   4:    */ import java.net.URISyntaxException;
/*   5:    */ 
/*   6:    */ public final class PackageRelationship
/*   7:    */ {
/*   8:    */   private static URI containerRelationshipPart;
/*   9:    */   public static final String ID_ATTRIBUTE_NAME = "Id";
/*  10:    */   public static final String RELATIONSHIPS_TAG_NAME = "Relationships";
/*  11:    */   public static final String RELATIONSHIP_TAG_NAME = "Relationship";
/*  12:    */   public static final String TARGET_ATTRIBUTE_NAME = "Target";
/*  13:    */   public static final String TARGET_MODE_ATTRIBUTE_NAME = "TargetMode";
/*  14:    */   public static final String TYPE_ATTRIBUTE_NAME = "Type";
/*  15:    */   private String id;
/*  16:    */   private OPCPackage container;
/*  17:    */   private String relationshipType;
/*  18:    */   private PackagePart source;
/*  19:    */   private TargetMode targetMode;
/*  20:    */   private URI targetUri;
/*  21:    */   
/*  22:    */   static
/*  23:    */   {
/*  24:    */     try
/*  25:    */     {
/*  26: 35 */       containerRelationshipPart = new URI("/_rels/.rels");
/*  27:    */     }
/*  28:    */     catch (URISyntaxException e) {}
/*  29:    */   }
/*  30:    */   
/*  31:    */   public PackageRelationship(OPCPackage pkg, PackagePart sourcePart, URI targetUri, TargetMode targetMode, String relationshipType, String id)
/*  32:    */   {
/*  33:100 */     if (pkg == null) {
/*  34:101 */       throw new IllegalArgumentException("pkg");
/*  35:    */     }
/*  36:102 */     if (targetUri == null) {
/*  37:103 */       throw new IllegalArgumentException("targetUri");
/*  38:    */     }
/*  39:104 */     if (relationshipType == null) {
/*  40:105 */       throw new IllegalArgumentException("relationshipType");
/*  41:    */     }
/*  42:106 */     if (id == null) {
/*  43:107 */       throw new IllegalArgumentException("id");
/*  44:    */     }
/*  45:109 */     this.container = pkg;
/*  46:110 */     this.source = sourcePart;
/*  47:111 */     this.targetUri = targetUri;
/*  48:112 */     this.targetMode = targetMode;
/*  49:113 */     this.relationshipType = relationshipType;
/*  50:114 */     this.id = id;
/*  51:    */   }
/*  52:    */   
/*  53:    */   public boolean equals(Object obj)
/*  54:    */   {
/*  55:119 */     if (!(obj instanceof PackageRelationship)) {
/*  56:120 */       return false;
/*  57:    */     }
/*  58:122 */     PackageRelationship rel = (PackageRelationship)obj;
/*  59:123 */     return (this.id.equals(rel.id)) && (this.relationshipType.equals(rel.relationshipType)) && ((rel.source == null) || (rel.source.equals(this.source))) && (this.targetMode == rel.targetMode) && (this.targetUri.equals(rel.targetUri));
/*  60:    */   }
/*  61:    */   
/*  62:    */   public int hashCode()
/*  63:    */   {
/*  64:132 */     return this.id.hashCode() + this.relationshipType.hashCode() + (this.source == null ? 0 : this.source.hashCode()) + this.targetMode.hashCode() + this.targetUri.hashCode();
/*  65:    */   }
/*  66:    */   
/*  67:    */   public static URI getContainerPartRelationship()
/*  68:    */   {
/*  69:142 */     return containerRelationshipPart;
/*  70:    */   }
/*  71:    */   
/*  72:    */   public OPCPackage getPackage()
/*  73:    */   {
/*  74:149 */     return this.container;
/*  75:    */   }
/*  76:    */   
/*  77:    */   public String getId()
/*  78:    */   {
/*  79:156 */     return this.id;
/*  80:    */   }
/*  81:    */   
/*  82:    */   public String getRelationshipType()
/*  83:    */   {
/*  84:163 */     return this.relationshipType;
/*  85:    */   }
/*  86:    */   
/*  87:    */   public PackagePart getSource()
/*  88:    */   {
/*  89:170 */     return this.source;
/*  90:    */   }
/*  91:    */   
/*  92:    */   public URI getSourceURI()
/*  93:    */   {
/*  94:178 */     if (this.source == null) {
/*  95:179 */       return PackagingURIHelper.PACKAGE_ROOT_URI;
/*  96:    */     }
/*  97:181 */     return this.source._partName.getURI();
/*  98:    */   }
/*  99:    */   
/* 100:    */   public TargetMode getTargetMode()
/* 101:    */   {
/* 102:188 */     return this.targetMode;
/* 103:    */   }
/* 104:    */   
/* 105:    */   public URI getTargetURI()
/* 106:    */   {
/* 107:197 */     if (this.targetMode == TargetMode.EXTERNAL) {
/* 108:198 */       return this.targetUri;
/* 109:    */     }
/* 110:204 */     if (!this.targetUri.toASCIIString().startsWith("/")) {
/* 111:206 */       return PackagingURIHelper.resolvePartUri(getSourceURI(), this.targetUri);
/* 112:    */     }
/* 113:208 */     return this.targetUri;
/* 114:    */   }
/* 115:    */   
/* 116:    */   public String toString()
/* 117:    */   {
/* 118:213 */     StringBuilder sb = new StringBuilder();
/* 119:214 */     sb.append("id=" + this.id);
/* 120:215 */     sb.append(" - container=" + this.container);
/* 121:    */     
/* 122:217 */     sb.append(" - relationshipType=" + this.relationshipType);
/* 123:    */     
/* 124:219 */     sb.append(" - source=" + getSourceURI().toASCIIString());
/* 125:    */     
/* 126:221 */     sb.append(" - target=" + getTargetURI().toASCIIString());
/* 127:    */     
/* 128:223 */     sb.append(",targetMode=" + this.targetMode);
/* 129:    */     
/* 130:225 */     return sb.toString();
/* 131:    */   }
/* 132:    */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.openxml4j.opc.PackageRelationship
 * JD-Core Version:    0.7.0.1
 */