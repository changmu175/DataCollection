/*   1:    */ package org.apache.poi.hpsf.extractor;
/*   2:    */ 
/*   3:    */ import java.io.File;
/*   4:    */ import java.io.IOException;
/*   5:    */ import java.io.PrintStream;
/*   6:    */ import org.apache.poi.POIDocument;
/*   7:    */ import org.apache.poi.POIOLE2TextExtractor;
/*   8:    */ import org.apache.poi.POITextExtractor;
/*   9:    */ import org.apache.poi.hpsf.CustomProperties;
/*  10:    */ import org.apache.poi.hpsf.DocumentSummaryInformation;
/*  11:    */ import org.apache.poi.hpsf.HPSFPropertiesOnlyDocument;
/*  12:    */ import org.apache.poi.hpsf.Property;
/*  13:    */ import org.apache.poi.hpsf.PropertySet;
/*  14:    */ import org.apache.poi.hpsf.SummaryInformation;
/*  15:    */ import org.apache.poi.hpsf.wellknown.PropertyIDMap;
/*  16:    */ import org.apache.poi.poifs.filesystem.NPOIFSFileSystem;
/*  17:    */ import org.apache.poi.poifs.filesystem.POIFSFileSystem;
/*  18:    */ 
/*  19:    */ public class HPSFPropertiesExtractor
/*  20:    */   extends POIOLE2TextExtractor
/*  21:    */ {
/*  22:    */   public HPSFPropertiesExtractor(POIOLE2TextExtractor mainExtractor)
/*  23:    */   {
/*  24: 44 */     super(mainExtractor);
/*  25:    */   }
/*  26:    */   
/*  27:    */   public HPSFPropertiesExtractor(POIDocument doc)
/*  28:    */   {
/*  29: 47 */     super(doc);
/*  30:    */   }
/*  31:    */   
/*  32:    */   public HPSFPropertiesExtractor(POIFSFileSystem fs)
/*  33:    */   {
/*  34: 50 */     super(new HPSFPropertiesOnlyDocument(fs));
/*  35:    */   }
/*  36:    */   
/*  37:    */   public HPSFPropertiesExtractor(NPOIFSFileSystem fs)
/*  38:    */   {
/*  39: 53 */     super(new HPSFPropertiesOnlyDocument(fs));
/*  40:    */   }
/*  41:    */   
/*  42:    */   public String getDocumentSummaryInformationText()
/*  43:    */   {
/*  44: 57 */     if (this.document == null) {
/*  45: 58 */       return "";
/*  46:    */     }
/*  47: 61 */     DocumentSummaryInformation dsi = this.document.getDocumentSummaryInformation();
/*  48: 62 */     StringBuilder text = new StringBuilder();
/*  49:    */     
/*  50:    */ 
/*  51: 65 */     text.append(getPropertiesText(dsi));
/*  52:    */     
/*  53:    */ 
/*  54: 68 */     CustomProperties cps = dsi == null ? null : dsi.getCustomProperties();
/*  55: 69 */     if (cps != null) {
/*  56: 70 */       for (String key : cps.nameSet())
/*  57:    */       {
/*  58: 71 */         String val = getPropertyValueText(cps.get(key));
/*  59: 72 */         text.append(key).append(" = ").append(val).append("\n");
/*  60:    */       }
/*  61:    */     }
/*  62: 77 */     return text.toString();
/*  63:    */   }
/*  64:    */   
/*  65:    */   public String getSummaryInformationText()
/*  66:    */   {
/*  67: 80 */     if (this.document == null) {
/*  68: 81 */       return "";
/*  69:    */     }
/*  70: 84 */     SummaryInformation si = this.document.getSummaryInformation();
/*  71:    */     
/*  72:    */ 
/*  73: 87 */     return getPropertiesText(si);
/*  74:    */   }
/*  75:    */   
/*  76:    */   private static String getPropertiesText(PropertySet ps)
/*  77:    */   {
/*  78: 91 */     if (ps == null) {
/*  79: 93 */       return "";
/*  80:    */     }
/*  81: 96 */     StringBuilder text = new StringBuilder();
/*  82:    */     
/*  83: 98 */     PropertyIDMap idMap = ps.getPropertySetIDMap();
/*  84: 99 */     Property[] props = ps.getProperties();
/*  85:100 */     for (Property prop : props)
/*  86:    */     {
/*  87:101 */       String type = Long.toString(prop.getID());
/*  88:102 */       Object typeObj = idMap == null ? null : idMap.get(Long.valueOf(prop.getID()));
/*  89:103 */       if (typeObj != null) {
/*  90:104 */         type = typeObj.toString();
/*  91:    */       }
/*  92:107 */       String val = getPropertyValueText(prop.getValue());
/*  93:108 */       text.append(type).append(" = ").append(val).append("\n");
/*  94:    */     }
/*  95:111 */     return text.toString();
/*  96:    */   }
/*  97:    */   
/*  98:    */   public String getText()
/*  99:    */   {
/* 100:119 */     return getSummaryInformationText() + getDocumentSummaryInformationText();
/* 101:    */   }
/* 102:    */   
/* 103:    */   public POITextExtractor getMetadataTextExtractor()
/* 104:    */   {
/* 105:126 */     throw new IllegalStateException("You already have the Metadata Text Extractor, not recursing!");
/* 106:    */   }
/* 107:    */   
/* 108:    */   private static String getPropertyValueText(Object val)
/* 109:    */   {
/* 110:130 */     return val == null ? "(not set)" : PropertySet.getPropertyStringValue(val);
/* 111:    */   }
/* 112:    */   
/* 113:    */   public boolean equals(Object o)
/* 114:    */   {
/* 115:137 */     return super.equals(o);
/* 116:    */   }
/* 117:    */   
/* 118:    */   public int hashCode()
/* 119:    */   {
/* 120:142 */     return super.hashCode();
/* 121:    */   }
/* 122:    */   
/* 123:    */   public static void main(String[] args)
/* 124:    */     throws IOException
/* 125:    */   {
/* 126:146 */     for (String file : args)
/* 127:    */     {
/* 128:147 */       HPSFPropertiesExtractor ext = new HPSFPropertiesExtractor(new NPOIFSFileSystem(new File(file)));
/* 129:    */       try
/* 130:    */       {
/* 131:150 */         System.out.println(ext.getText());
/* 132:    */       }
/* 133:    */       finally
/* 134:    */       {
/* 135:152 */         ext.close();
/* 136:    */       }
/* 137:    */     }
/* 138:    */   }
/* 139:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hpsf.extractor.HPSFPropertiesExtractor
 * JD-Core Version:    0.7.0.1
 */