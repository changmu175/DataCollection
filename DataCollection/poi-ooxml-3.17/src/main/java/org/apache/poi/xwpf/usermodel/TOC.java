/*   1:    */ package org.apache.poi.xwpf.usermodel;
/*   2:    */ 
/*   3:    */ import java.math.BigInteger;
/*   4:    */ import org.apache.poi.util.Internal;
/*   5:    */ import org.apache.poi.util.LocaleUtil;
/*   6:    */ import org.apache.xmlbeans.impl.xb.xmlschema.SpaceAttribute.Space;
/*   7:    */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTColor;
/*   8:    */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber;
/*   9:    */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFldChar;
/*  10:    */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFonts;
/*  11:    */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHpsMeasure;
/*  12:    */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff;
/*  13:    */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTP;
/*  14:    */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPPr;
/*  15:    */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTParaRPr;
/*  16:    */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTR;
/*  17:    */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRPr;
/*  18:    */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSdtBlock;
/*  19:    */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSdtBlock.Factory;
/*  20:    */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSdtContentBlock;
/*  21:    */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSdtDocPart;
/*  22:    */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSdtEndPr;
/*  23:    */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSdtPr;
/*  24:    */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString;
/*  25:    */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTabStop;
/*  26:    */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTabs;
/*  27:    */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTText;
/*  28:    */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.STFldCharType;
/*  29:    */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.STOnOff;
/*  30:    */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.STTabJc;
/*  31:    */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.STTabTlc;
/*  32:    */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.STTheme;
/*  33:    */ 
/*  34:    */ public class TOC
/*  35:    */ {
/*  36:    */   CTSdtBlock block;
/*  37:    */   
/*  38:    */   public TOC()
/*  39:    */   {
/*  40: 48 */     this(CTSdtBlock.Factory.newInstance());
/*  41:    */   }
/*  42:    */   
/*  43:    */   public TOC(CTSdtBlock block)
/*  44:    */   {
/*  45: 52 */     this.block = block;
/*  46: 53 */     CTSdtPr sdtPr = block.addNewSdtPr();
/*  47: 54 */     CTDecimalNumber id = sdtPr.addNewId();
/*  48: 55 */     id.setVal(new BigInteger("4844945"));
/*  49: 56 */     sdtPr.addNewDocPartObj().addNewDocPartGallery().setVal("Table of contents");
/*  50: 57 */     CTSdtEndPr sdtEndPr = block.addNewSdtEndPr();
/*  51: 58 */     CTRPr rPr = sdtEndPr.addNewRPr();
/*  52: 59 */     CTFonts fonts = rPr.addNewRFonts();
/*  53: 60 */     fonts.setAsciiTheme(STTheme.MINOR_H_ANSI);
/*  54: 61 */     fonts.setEastAsiaTheme(STTheme.MINOR_H_ANSI);
/*  55: 62 */     fonts.setHAnsiTheme(STTheme.MINOR_H_ANSI);
/*  56: 63 */     fonts.setCstheme(STTheme.MINOR_BIDI);
/*  57: 64 */     rPr.addNewB().setVal(STOnOff.OFF);
/*  58: 65 */     rPr.addNewBCs().setVal(STOnOff.OFF);
/*  59: 66 */     rPr.addNewColor().setVal("auto");
/*  60: 67 */     rPr.addNewSz().setVal(new BigInteger("24"));
/*  61: 68 */     rPr.addNewSzCs().setVal(new BigInteger("24"));
/*  62: 69 */     CTSdtContentBlock content = block.addNewSdtContent();
/*  63: 70 */     CTP p = content.addNewP();
/*  64: 71 */     p.setRsidR("00EF7E24".getBytes(LocaleUtil.CHARSET_1252));
/*  65: 72 */     p.setRsidRDefault("00EF7E24".getBytes(LocaleUtil.CHARSET_1252));
/*  66: 73 */     p.addNewPPr().addNewPStyle().setVal("TOCHeading");
/*  67: 74 */     p.addNewR().addNewT().setStringValue("Table of Contents");
/*  68:    */   }
/*  69:    */   
/*  70:    */   @Internal
/*  71:    */   public CTSdtBlock getBlock()
/*  72:    */   {
/*  73: 79 */     return this.block;
/*  74:    */   }
/*  75:    */   
/*  76:    */   public void addRow(int level, String title, int page, String bookmarkRef)
/*  77:    */   {
/*  78: 83 */     CTSdtContentBlock contentBlock = this.block.getSdtContent();
/*  79: 84 */     CTP p = contentBlock.addNewP();
/*  80: 85 */     p.setRsidR("00EF7E24".getBytes(LocaleUtil.CHARSET_1252));
/*  81: 86 */     p.setRsidRDefault("00EF7E24".getBytes(LocaleUtil.CHARSET_1252));
/*  82: 87 */     CTPPr pPr = p.addNewPPr();
/*  83: 88 */     pPr.addNewPStyle().setVal("TOC" + level);
/*  84: 89 */     CTTabs tabs = pPr.addNewTabs();
/*  85: 90 */     CTTabStop tab = tabs.addNewTab();
/*  86: 91 */     tab.setVal(STTabJc.RIGHT);
/*  87: 92 */     tab.setLeader(STTabTlc.DOT);
/*  88: 93 */     tab.setPos(new BigInteger("8290"));
/*  89: 94 */     pPr.addNewRPr().addNewNoProof();
/*  90: 95 */     CTR run = p.addNewR();
/*  91: 96 */     run.addNewRPr().addNewNoProof();
/*  92: 97 */     run.addNewT().setStringValue(title);
/*  93: 98 */     run = p.addNewR();
/*  94: 99 */     run.addNewRPr().addNewNoProof();
/*  95:100 */     run.addNewTab();
/*  96:101 */     run = p.addNewR();
/*  97:102 */     run.addNewRPr().addNewNoProof();
/*  98:103 */     run.addNewFldChar().setFldCharType(STFldCharType.BEGIN);
/*  99:    */     
/* 100:105 */     run = p.addNewR();
/* 101:106 */     run.addNewRPr().addNewNoProof();
/* 102:107 */     CTText text = run.addNewInstrText();
/* 103:108 */     text.setSpace(SpaceAttribute.Space.PRESERVE);
/* 104:    */     
/* 105:110 */     text.setStringValue(" PAGEREF _Toc" + bookmarkRef + " \\h ");
/* 106:111 */     p.addNewR().addNewRPr().addNewNoProof();
/* 107:112 */     run = p.addNewR();
/* 108:113 */     run.addNewRPr().addNewNoProof();
/* 109:114 */     run.addNewFldChar().setFldCharType(STFldCharType.SEPARATE);
/* 110:    */     
/* 111:116 */     run = p.addNewR();
/* 112:117 */     run.addNewRPr().addNewNoProof();
/* 113:118 */     run.addNewT().setStringValue(Integer.toString(page));
/* 114:119 */     run = p.addNewR();
/* 115:120 */     run.addNewRPr().addNewNoProof();
/* 116:121 */     run.addNewFldChar().setFldCharType(STFldCharType.END);
/* 117:    */   }
/* 118:    */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xwpf.usermodel.TOC
 * JD-Core Version:    0.7.0.1
 */