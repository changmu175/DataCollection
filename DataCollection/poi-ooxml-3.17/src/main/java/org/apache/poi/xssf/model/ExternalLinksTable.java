/*   1:    */ package org.apache.poi.xssf.model;
/*   2:    */ 
/*   3:    */ import java.io.IOException;
/*   4:    */ import java.io.InputStream;
/*   5:    */ import java.io.OutputStream;
/*   6:    */ import java.net.URI;
/*   7:    */ import java.util.ArrayList;
/*   8:    */ import java.util.List;
/*   9:    */ import org.apache.poi.POIXMLDocumentPart;
/*  10:    */ import org.apache.poi.POIXMLTypeLoader;
/*  11:    */ import org.apache.poi.openxml4j.opc.PackagePart;
/*  12:    */ import org.apache.poi.openxml4j.opc.PackageRelationship;
/*  13:    */ import org.apache.poi.openxml4j.opc.TargetMode;
/*  14:    */ import org.apache.poi.ss.usermodel.Name;
/*  15:    */ import org.apache.xmlbeans.XmlException;
/*  16:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExternalBook;
/*  17:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExternalDefinedName;
/*  18:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExternalDefinedNames;
/*  19:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExternalLink;
/*  20:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExternalLink.Factory;
/*  21:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExternalSheetName;
/*  22:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExternalSheetNames;
/*  23:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.ExternalLinkDocument;
/*  24:    */ import org.openxmlformats.schemas.spreadsheetml.x2006.main.ExternalLinkDocument.Factory;
/*  25:    */ 
/*  26:    */ public class ExternalLinksTable
/*  27:    */   extends POIXMLDocumentPart
/*  28:    */ {
/*  29:    */   private CTExternalLink link;
/*  30:    */   
/*  31:    */   public ExternalLinksTable()
/*  32:    */   {
/*  33: 48 */     this.link = CTExternalLink.Factory.newInstance();
/*  34: 49 */     this.link.addNewExternalBook();
/*  35:    */   }
/*  36:    */   
/*  37:    */   public ExternalLinksTable(PackagePart part)
/*  38:    */     throws IOException
/*  39:    */   {
/*  40: 56 */     super(part);
/*  41: 57 */     readFrom(part.getInputStream());
/*  42:    */   }
/*  43:    */   
/*  44:    */   public void readFrom(InputStream is)
/*  45:    */     throws IOException
/*  46:    */   {
/*  47:    */     try
/*  48:    */     {
/*  49: 62 */       ExternalLinkDocument doc = ExternalLinkDocument.Factory.parse(is, POIXMLTypeLoader.DEFAULT_XML_OPTIONS);
/*  50: 63 */       this.link = doc.getExternalLink();
/*  51:    */     }
/*  52:    */     catch (XmlException e)
/*  53:    */     {
/*  54: 65 */       throw new IOException(e.getLocalizedMessage());
/*  55:    */     }
/*  56:    */   }
/*  57:    */   
/*  58:    */   public void writeTo(OutputStream out)
/*  59:    */     throws IOException
/*  60:    */   {
/*  61: 69 */     ExternalLinkDocument doc = ExternalLinkDocument.Factory.newInstance();
/*  62: 70 */     doc.setExternalLink(this.link);
/*  63: 71 */     doc.save(out, POIXMLTypeLoader.DEFAULT_XML_OPTIONS);
/*  64:    */   }
/*  65:    */   
/*  66:    */   protected void commit()
/*  67:    */     throws IOException
/*  68:    */   {
/*  69: 76 */     PackagePart part = getPackagePart();
/*  70: 77 */     OutputStream out = part.getOutputStream();
/*  71: 78 */     writeTo(out);
/*  72: 79 */     out.close();
/*  73:    */   }
/*  74:    */   
/*  75:    */   public CTExternalLink getCTExternalLink()
/*  76:    */   {
/*  77: 87 */     return this.link;
/*  78:    */   }
/*  79:    */   
/*  80:    */   public String getLinkedFileName()
/*  81:    */   {
/*  82: 95 */     String rId = this.link.getExternalBook().getId();
/*  83: 96 */     PackageRelationship rel = getPackagePart().getRelationship(rId);
/*  84: 97 */     if ((rel != null) && (rel.getTargetMode() == TargetMode.EXTERNAL)) {
/*  85: 98 */       return rel.getTargetURI().toString();
/*  86:    */     }
/*  87:100 */     return null;
/*  88:    */   }
/*  89:    */   
/*  90:    */   public void setLinkedFileName(String target)
/*  91:    */   {
/*  92:107 */     String rId = this.link.getExternalBook().getId();
/*  93:109 */     if ((rId != null) && (!rId.isEmpty())) {
/*  94:113 */       getPackagePart().removeRelationship(rId);
/*  95:    */     }
/*  96:117 */     PackageRelationship newRel = getPackagePart().addExternalRelationship(target, "http://schemas.openxmlformats.org/officeDocument/2006/relationships/externalLinkPath");
/*  97:    */     
/*  98:119 */     this.link.getExternalBook().setId(newRel.getId());
/*  99:    */   }
/* 100:    */   
/* 101:    */   public List<String> getSheetNames()
/* 102:    */   {
/* 103:123 */     CTExternalSheetName[] sheetNames = this.link.getExternalBook().getSheetNames().getSheetNameArray();
/* 104:    */     
/* 105:125 */     List<String> names = new ArrayList(sheetNames.length);
/* 106:126 */     for (CTExternalSheetName name : sheetNames) {
/* 107:127 */       names.add(name.getVal());
/* 108:    */     }
/* 109:129 */     return names;
/* 110:    */   }
/* 111:    */   
/* 112:    */   public List<Name> getDefinedNames()
/* 113:    */   {
/* 114:133 */     CTExternalDefinedName[] extNames = this.link.getExternalBook().getDefinedNames().getDefinedNameArray();
/* 115:    */     
/* 116:135 */     List<Name> names = new ArrayList(extNames.length);
/* 117:136 */     for (CTExternalDefinedName extName : extNames) {
/* 118:137 */       names.add(new ExternalName(extName));
/* 119:    */     }
/* 120:139 */     return names;
/* 121:    */   }
/* 122:    */   
/* 123:    */   protected class ExternalName
/* 124:    */     implements Name
/* 125:    */   {
/* 126:    */     private CTExternalDefinedName name;
/* 127:    */     
/* 128:    */     protected ExternalName(CTExternalDefinedName name)
/* 129:    */     {
/* 130:149 */       this.name = name;
/* 131:    */     }
/* 132:    */     
/* 133:    */     public String getNameName()
/* 134:    */     {
/* 135:153 */       return this.name.getName();
/* 136:    */     }
/* 137:    */     
/* 138:    */     public void setNameName(String name)
/* 139:    */     {
/* 140:156 */       this.name.setName(name);
/* 141:    */     }
/* 142:    */     
/* 143:    */     public String getSheetName()
/* 144:    */     {
/* 145:160 */       int sheetId = getSheetIndex();
/* 146:161 */       if (sheetId >= 0) {
/* 147:162 */         return (String)ExternalLinksTable.this.getSheetNames().get(sheetId);
/* 148:    */       }
/* 149:164 */       return null;
/* 150:    */     }
/* 151:    */     
/* 152:    */     public int getSheetIndex()
/* 153:    */     {
/* 154:168 */       if (this.name.isSetSheetId()) {
/* 155:169 */         return (int)this.name.getSheetId();
/* 156:    */       }
/* 157:171 */       return -1;
/* 158:    */     }
/* 159:    */     
/* 160:    */     public void setSheetIndex(int sheetId)
/* 161:    */     {
/* 162:174 */       this.name.setSheetId(sheetId);
/* 163:    */     }
/* 164:    */     
/* 165:    */     public String getRefersToFormula()
/* 166:    */     {
/* 167:179 */       return this.name.getRefersTo().substring(1);
/* 168:    */     }
/* 169:    */     
/* 170:    */     public void setRefersToFormula(String formulaText)
/* 171:    */     {
/* 172:183 */       this.name.setRefersTo('=' + formulaText);
/* 173:    */     }
/* 174:    */     
/* 175:    */     public boolean isFunctionName()
/* 176:    */     {
/* 177:187 */       return false;
/* 178:    */     }
/* 179:    */     
/* 180:    */     public boolean isDeleted()
/* 181:    */     {
/* 182:190 */       return false;
/* 183:    */     }
/* 184:    */     
/* 185:    */     public String getComment()
/* 186:    */     {
/* 187:194 */       return null;
/* 188:    */     }
/* 189:    */     
/* 190:    */     public void setComment(String comment)
/* 191:    */     {
/* 192:197 */       throw new IllegalStateException("Not Supported");
/* 193:    */     }
/* 194:    */     
/* 195:    */     public void setFunction(boolean value)
/* 196:    */     {
/* 197:200 */       throw new IllegalStateException("Not Supported");
/* 198:    */     }
/* 199:    */   }
/* 200:    */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xssf.model.ExternalLinksTable
 * JD-Core Version:    0.7.0.1
 */