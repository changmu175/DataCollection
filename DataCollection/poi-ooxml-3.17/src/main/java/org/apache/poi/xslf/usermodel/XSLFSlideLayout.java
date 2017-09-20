/*   1:    */ package org.apache.poi.xslf.usermodel;
/*   2:    */ 
/*   3:    */ import java.io.IOException;
/*   4:    */ import org.apache.poi.POIXMLDocumentPart;
/*   5:    */ import org.apache.poi.POIXMLTypeLoader;
/*   6:    */ import org.apache.poi.openxml4j.opc.PackagePart;
/*   7:    */ import org.apache.poi.sl.usermodel.MasterSheet;
/*   8:    */ import org.apache.poi.sl.usermodel.Placeholder;
/*   9:    */ import org.apache.poi.util.Internal;
/*  10:    */ import org.apache.xmlbeans.XmlException;
/*  11:    */ import org.apache.xmlbeans.XmlObject;
/*  12:    */ import org.openxmlformats.schemas.presentationml.x2006.main.CTBackground;
/*  13:    */ import org.openxmlformats.schemas.presentationml.x2006.main.CTCommonSlideData;
/*  14:    */ import org.openxmlformats.schemas.presentationml.x2006.main.CTGroupShape;
/*  15:    */ import org.openxmlformats.schemas.presentationml.x2006.main.CTPlaceholder;
/*  16:    */ import org.openxmlformats.schemas.presentationml.x2006.main.CTShape;
/*  17:    */ import org.openxmlformats.schemas.presentationml.x2006.main.CTSlideLayout;
/*  18:    */ import org.openxmlformats.schemas.presentationml.x2006.main.CTSlideLayout.Factory;
/*  19:    */ import org.openxmlformats.schemas.presentationml.x2006.main.STSlideLayoutType.Enum;
/*  20:    */ import org.openxmlformats.schemas.presentationml.x2006.main.SldLayoutDocument;
/*  21:    */ import org.openxmlformats.schemas.presentationml.x2006.main.SldLayoutDocument.Factory;
/*  22:    */ 
/*  23:    */ public class XSLFSlideLayout
/*  24:    */   extends XSLFSheet
/*  25:    */   implements MasterSheet<XSLFShape, XSLFTextParagraph>
/*  26:    */ {
/*  27:    */   private CTSlideLayout _layout;
/*  28:    */   private XSLFSlideMaster _master;
/*  29:    */   
/*  30:    */   XSLFSlideLayout()
/*  31:    */   {
/*  32: 43 */     this._layout = CTSlideLayout.Factory.newInstance();
/*  33:    */   }
/*  34:    */   
/*  35:    */   public XSLFSlideLayout(PackagePart part)
/*  36:    */     throws IOException, XmlException
/*  37:    */   {
/*  38: 50 */     super(part);
/*  39: 51 */     SldLayoutDocument doc = SldLayoutDocument.Factory.parse(getPackagePart().getInputStream(), POIXMLTypeLoader.DEFAULT_XML_OPTIONS);
/*  40:    */     
/*  41: 53 */     this._layout = doc.getSldLayout();
/*  42: 54 */     setCommonSlideData(this._layout.getCSld());
/*  43:    */   }
/*  44:    */   
/*  45:    */   public String getName()
/*  46:    */   {
/*  47: 58 */     return this._layout.getCSld().getName();
/*  48:    */   }
/*  49:    */   
/*  50:    */   @Internal
/*  51:    */   public CTSlideLayout getXmlObject()
/*  52:    */   {
/*  53: 66 */     return this._layout;
/*  54:    */   }
/*  55:    */   
/*  56:    */   protected String getRootElementName()
/*  57:    */   {
/*  58: 71 */     return "sldLayout";
/*  59:    */   }
/*  60:    */   
/*  61:    */   public XSLFSlideMaster getSlideMaster()
/*  62:    */   {
/*  63: 81 */     if (this._master == null) {
/*  64: 82 */       for (POIXMLDocumentPart p : getRelations()) {
/*  65: 83 */         if ((p instanceof XSLFSlideMaster)) {
/*  66: 84 */           this._master = ((XSLFSlideMaster)p);
/*  67:    */         }
/*  68:    */       }
/*  69:    */     }
/*  70: 88 */     if (this._master == null) {
/*  71: 89 */       throw new IllegalStateException("SlideMaster was not found for " + this);
/*  72:    */     }
/*  73: 91 */     return this._master;
/*  74:    */   }
/*  75:    */   
/*  76:    */   public XSLFSlideMaster getMasterSheet()
/*  77:    */   {
/*  78: 96 */     return getSlideMaster();
/*  79:    */   }
/*  80:    */   
/*  81:    */   public XSLFTheme getTheme()
/*  82:    */   {
/*  83:101 */     return getSlideMaster().getTheme();
/*  84:    */   }
/*  85:    */   
/*  86:    */   public boolean getFollowMasterGraphics()
/*  87:    */   {
/*  88:107 */     return this._layout.getShowMasterSp();
/*  89:    */   }
/*  90:    */   
/*  91:    */   protected boolean canDraw(XSLFShape shape)
/*  92:    */   {
/*  93:115 */     if ((shape instanceof XSLFSimpleShape))
/*  94:    */     {
/*  95:116 */       XSLFSimpleShape txt = (XSLFSimpleShape)shape;
/*  96:117 */       CTPlaceholder ph = txt.getCTPlaceholder();
/*  97:118 */       if (ph != null) {
/*  98:119 */         return false;
/*  99:    */       }
/* 100:    */     }
/* 101:122 */     return true;
/* 102:    */   }
/* 103:    */   
/* 104:    */   public XSLFBackground getBackground()
/* 105:    */   {
/* 106:128 */     CTBackground bg = this._layout.getCSld().getBg();
/* 107:129 */     if (bg != null) {
/* 108:130 */       return new XSLFBackground(bg, this);
/* 109:    */     }
/* 110:132 */     return getMasterSheet().getBackground();
/* 111:    */   }
/* 112:    */   
/* 113:    */   public void copyLayout(XSLFSlide slide)
/* 114:    */   {
/* 115:142 */     for (XSLFShape sh : getShapes()) {
/* 116:143 */       if ((sh instanceof XSLFTextShape))
/* 117:    */       {
/* 118:144 */         XSLFTextShape tsh = (XSLFTextShape)sh;
/* 119:145 */         Placeholder ph = tsh.getTextType();
/* 120:146 */         if (ph != null) {
/* 121:148 */           switch (1.$SwitchMap$org$apache$poi$sl$usermodel$Placeholder[ph.ordinal()])
/* 122:    */           {
/* 123:    */           case 1: 
/* 124:    */           case 2: 
/* 125:    */           case 3: 
/* 126:    */             break;
/* 127:    */           default: 
/* 128:155 */             slide.getSpTree().addNewSp().set(tsh.getXmlObject().copy());
/* 129:    */           }
/* 130:    */         }
/* 131:    */       }
/* 132:    */     }
/* 133:    */   }
/* 134:    */   
/* 135:    */   public SlideLayout getType()
/* 136:    */   {
/* 137:166 */     int ordinal = this._layout.getType().intValue() - 1;
/* 138:167 */     return SlideLayout.values()[ordinal];
/* 139:    */   }
/* 140:    */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xslf.usermodel.XSLFSlideLayout
 * JD-Core Version:    0.7.0.1
 */