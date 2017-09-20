/*   1:    */ package org.apache.poi.xslf.usermodel;
/*   2:    */ 
/*   3:    */ import java.net.URI;
/*   4:    */ import org.apache.poi.common.usermodel.HyperlinkType;
/*   5:    */ import org.apache.poi.openxml4j.opc.PackagePart;
/*   6:    */ import org.apache.poi.openxml4j.opc.PackagePartName;
/*   7:    */ import org.apache.poi.openxml4j.opc.PackageRelationship;
/*   8:    */ import org.apache.poi.openxml4j.opc.TargetMode;
/*   9:    */ import org.apache.poi.sl.usermodel.Hyperlink;
/*  10:    */ import org.apache.poi.sl.usermodel.Slide;
/*  11:    */ import org.apache.poi.util.Internal;
/*  12:    */ import org.openxmlformats.schemas.drawingml.x2006.main.CTHyperlink;
/*  13:    */ 
/*  14:    */ public class XSLFHyperlink
/*  15:    */   implements Hyperlink<XSLFShape, XSLFTextParagraph>
/*  16:    */ {
/*  17:    */   final XSLFSheet _sheet;
/*  18:    */   final CTHyperlink _link;
/*  19:    */   
/*  20:    */   XSLFHyperlink(CTHyperlink link, XSLFSheet sheet)
/*  21:    */   {
/*  22: 36 */     this._sheet = sheet;
/*  23: 37 */     this._link = link;
/*  24:    */   }
/*  25:    */   
/*  26:    */   @Internal
/*  27:    */   public CTHyperlink getXmlObject()
/*  28:    */   {
/*  29: 42 */     return this._link;
/*  30:    */   }
/*  31:    */   
/*  32:    */   public void setAddress(String address)
/*  33:    */   {
/*  34: 47 */     linkToUrl(address);
/*  35:    */   }
/*  36:    */   
/*  37:    */   public String getAddress()
/*  38:    */   {
/*  39: 52 */     String id = this._link.getId();
/*  40: 53 */     if ((id == null) || ("".equals(id))) {
/*  41: 54 */       return this._link.getAction();
/*  42:    */     }
/*  43: 57 */     URI targetURI = this._sheet.getPackagePart().getRelationship(id).getTargetURI();
/*  44:    */     
/*  45: 59 */     return targetURI.toASCIIString();
/*  46:    */   }
/*  47:    */   
/*  48:    */   public String getLabel()
/*  49:    */   {
/*  50: 64 */     return this._link.getTooltip();
/*  51:    */   }
/*  52:    */   
/*  53:    */   public void setLabel(String label)
/*  54:    */   {
/*  55: 69 */     this._link.setTooltip(label);
/*  56:    */   }
/*  57:    */   
/*  58:    */   public int getType()
/*  59:    */   {
/*  60: 78 */     return getTypeEnum().getCode();
/*  61:    */   }
/*  62:    */   
/*  63:    */   public HyperlinkType getTypeEnum()
/*  64:    */   {
/*  65: 83 */     String action = this._link.getAction();
/*  66: 84 */     if (action == null) {
/*  67: 85 */       action = "";
/*  68:    */     }
/*  69: 87 */     if ((action.equals("ppaction://hlinksldjump")) || (action.startsWith("ppaction://hlinkshowjump"))) {
/*  70: 88 */       return HyperlinkType.DOCUMENT;
/*  71:    */     }
/*  72: 91 */     String address = getAddress();
/*  73: 92 */     if (address == null) {
/*  74: 93 */       address = "";
/*  75:    */     }
/*  76: 95 */     if (address.startsWith("mailto:")) {
/*  77: 96 */       return HyperlinkType.EMAIL;
/*  78:    */     }
/*  79: 98 */     return HyperlinkType.URL;
/*  80:    */   }
/*  81:    */   
/*  82:    */   public void linkToEmail(String emailAddress)
/*  83:    */   {
/*  84:104 */     linkToExternal("mailto:" + emailAddress);
/*  85:105 */     setLabel(emailAddress);
/*  86:    */   }
/*  87:    */   
/*  88:    */   public void linkToUrl(String url)
/*  89:    */   {
/*  90:110 */     linkToExternal(url);
/*  91:111 */     setLabel(url);
/*  92:    */   }
/*  93:    */   
/*  94:    */   private void linkToExternal(String url)
/*  95:    */   {
/*  96:115 */     PackagePart thisPP = this._sheet.getPackagePart();
/*  97:116 */     if ((this._link.isSetId()) && (!this._link.getId().isEmpty())) {
/*  98:117 */       thisPP.removeRelationship(this._link.getId());
/*  99:    */     }
/* 100:119 */     PackageRelationship rel = thisPP.addExternalRelationship(url, XSLFRelation.HYPERLINK.getRelation());
/* 101:120 */     this._link.setId(rel.getId());
/* 102:121 */     if (this._link.isSetAction()) {
/* 103:122 */       this._link.unsetAction();
/* 104:    */     }
/* 105:    */   }
/* 106:    */   
/* 107:    */   public void linkToSlide(Slide<XSLFShape, XSLFTextParagraph> slide)
/* 108:    */   {
/* 109:128 */     PackagePart thisPP = this._sheet.getPackagePart();
/* 110:129 */     PackagePartName otherPPN = ((XSLFSheet)slide).getPackagePart().getPartName();
/* 111:130 */     if ((this._link.isSetId()) && (!this._link.getId().isEmpty())) {
/* 112:131 */       thisPP.removeRelationship(this._link.getId());
/* 113:    */     }
/* 114:133 */     PackageRelationship rel = thisPP.addRelationship(otherPPN, TargetMode.INTERNAL, XSLFRelation.SLIDE.getRelation());
/* 115:    */     
/* 116:135 */     this._link.setId(rel.getId());
/* 117:136 */     this._link.setAction("ppaction://hlinksldjump");
/* 118:    */   }
/* 119:    */   
/* 120:    */   public void linkToNextSlide()
/* 121:    */   {
/* 122:141 */     linkToRelativeSlide("nextslide");
/* 123:    */   }
/* 124:    */   
/* 125:    */   public void linkToPreviousSlide()
/* 126:    */   {
/* 127:146 */     linkToRelativeSlide("previousslide");
/* 128:    */   }
/* 129:    */   
/* 130:    */   public void linkToFirstSlide()
/* 131:    */   {
/* 132:151 */     linkToRelativeSlide("firstslide");
/* 133:    */   }
/* 134:    */   
/* 135:    */   public void linkToLastSlide()
/* 136:    */   {
/* 137:156 */     linkToRelativeSlide("lastslide");
/* 138:    */   }
/* 139:    */   
/* 140:    */   private void linkToRelativeSlide(String jump)
/* 141:    */   {
/* 142:160 */     PackagePart thisPP = this._sheet.getPackagePart();
/* 143:161 */     if ((this._link.isSetId()) && (!this._link.getId().isEmpty())) {
/* 144:162 */       thisPP.removeRelationship(this._link.getId());
/* 145:    */     }
/* 146:164 */     this._link.setId("");
/* 147:165 */     this._link.setAction("ppaction://hlinkshowjump?jump=" + jump);
/* 148:    */   }
/* 149:    */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xslf.usermodel.XSLFHyperlink
 * JD-Core Version:    0.7.0.1
 */