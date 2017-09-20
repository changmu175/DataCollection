/*   1:    */ package org.apache.poi.xwpf.usermodel;
/*   2:    */ 
/*   3:    */ import java.util.ArrayList;
/*   4:    */ import java.util.List;
/*   5:    */ import org.apache.xmlbeans.XmlCursor;
/*   6:    */ import org.apache.xmlbeans.XmlObject;
/*   7:    */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTP;
/*   8:    */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTR;
/*   9:    */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSdtBlock;
/*  10:    */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSdtContentBlock;
/*  11:    */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSdtContentRun;
/*  12:    */ import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTbl;
/*  13:    */ 
/*  14:    */ public class XWPFSDTContent
/*  15:    */   implements ISDTContent
/*  16:    */ {
/*  17: 43 */   private List<XWPFParagraph> paragraphs = new ArrayList();
/*  18: 44 */   private List<XWPFTable> tables = new ArrayList();
/*  19: 45 */   private List<XWPFRun> runs = new ArrayList();
/*  20: 46 */   private List<XWPFSDT> contentControls = new ArrayList();
/*  21: 47 */   private List<ISDTContents> bodyElements = new ArrayList();
/*  22:    */   
/*  23:    */   public XWPFSDTContent(CTSdtContentRun sdtRun, IBody part, IRunBody parent)
/*  24:    */   {
/*  25: 50 */     for (CTR ctr : sdtRun.getRArray())
/*  26:    */     {
/*  27: 51 */       XWPFRun run = new XWPFRun(ctr, parent);
/*  28: 52 */       this.runs.add(run);
/*  29: 53 */       this.bodyElements.add(run);
/*  30:    */     }
/*  31:    */   }
/*  32:    */   
/*  33:    */   public XWPFSDTContent(CTSdtContentBlock block, IBody part, IRunBody parent)
/*  34:    */   {
/*  35: 58 */     XmlCursor cursor = block.newCursor();
/*  36: 59 */     cursor.selectPath("./*");
/*  37: 60 */     while (cursor.toNextSelection())
/*  38:    */     {
/*  39: 61 */       XmlObject o = cursor.getObject();
/*  40: 62 */       if ((o instanceof CTP))
/*  41:    */       {
/*  42: 63 */         XWPFParagraph p = new XWPFParagraph((CTP)o, part);
/*  43: 64 */         this.bodyElements.add(p);
/*  44: 65 */         this.paragraphs.add(p);
/*  45:    */       }
/*  46: 66 */       else if ((o instanceof CTTbl))
/*  47:    */       {
/*  48: 67 */         XWPFTable t = new XWPFTable((CTTbl)o, part);
/*  49: 68 */         this.bodyElements.add(t);
/*  50: 69 */         this.tables.add(t);
/*  51:    */       }
/*  52: 70 */       else if ((o instanceof CTSdtBlock))
/*  53:    */       {
/*  54: 71 */         XWPFSDT c = new XWPFSDT((CTSdtBlock)o, part);
/*  55: 72 */         this.bodyElements.add(c);
/*  56: 73 */         this.contentControls.add(c);
/*  57:    */       }
/*  58: 74 */       else if ((o instanceof CTR))
/*  59:    */       {
/*  60: 75 */         XWPFRun run = new XWPFRun((CTR)o, parent);
/*  61: 76 */         this.runs.add(run);
/*  62: 77 */         this.bodyElements.add(run);
/*  63:    */       }
/*  64:    */     }
/*  65: 80 */     cursor.dispose();
/*  66:    */   }
/*  67:    */   
/*  68:    */   public String getText()
/*  69:    */   {
/*  70: 84 */     StringBuilder text = new StringBuilder();
/*  71: 85 */     boolean addNewLine = false;
/*  72: 86 */     for (int i = 0; i < this.bodyElements.size(); i++)
/*  73:    */     {
/*  74: 87 */       Object o = this.bodyElements.get(i);
/*  75: 88 */       if ((o instanceof XWPFParagraph))
/*  76:    */       {
/*  77: 89 */         appendParagraph((XWPFParagraph)o, text);
/*  78: 90 */         addNewLine = true;
/*  79:    */       }
/*  80: 91 */       else if ((o instanceof XWPFTable))
/*  81:    */       {
/*  82: 92 */         appendTable((XWPFTable)o, text);
/*  83: 93 */         addNewLine = true;
/*  84:    */       }
/*  85: 94 */       else if ((o instanceof XWPFSDT))
/*  86:    */       {
/*  87: 95 */         text.append(((XWPFSDT)o).getContent().getText());
/*  88: 96 */         addNewLine = true;
/*  89:    */       }
/*  90: 97 */       else if ((o instanceof XWPFRun))
/*  91:    */       {
/*  92: 98 */         text.append((XWPFRun)o);
/*  93: 99 */         addNewLine = false;
/*  94:    */       }
/*  95:101 */       if ((addNewLine == true) && (i < this.bodyElements.size() - 1)) {
/*  96:102 */         text.append("\n");
/*  97:    */       }
/*  98:    */     }
/*  99:105 */     return text.toString();
/* 100:    */   }
/* 101:    */   
/* 102:    */   private void appendTable(XWPFTable table, StringBuilder text)
/* 103:    */   {
/* 104:110 */     for (XWPFTableRow row : table.getRows())
/* 105:    */     {
/* 106:111 */       List<ICell> cells = row.getTableICells();
/* 107:112 */       for (int i = 0; i < cells.size(); i++)
/* 108:    */       {
/* 109:113 */         ICell cell = (ICell)cells.get(i);
/* 110:114 */         if ((cell instanceof XWPFTableCell)) {
/* 111:115 */           text.append(((XWPFTableCell)cell).getTextRecursively());
/* 112:116 */         } else if ((cell instanceof XWPFSDTCell)) {
/* 113:117 */           text.append(((XWPFSDTCell)cell).getContent().getText());
/* 114:    */         }
/* 115:119 */         if (i < cells.size() - 1) {
/* 116:120 */           text.append("\t");
/* 117:    */         }
/* 118:    */       }
/* 119:123 */       text.append('\n');
/* 120:    */     }
/* 121:    */   }
/* 122:    */   
/* 123:    */   private void appendParagraph(XWPFParagraph paragraph, StringBuilder text)
/* 124:    */   {
/* 125:128 */     for (IRunElement run : paragraph.getRuns()) {
/* 126:129 */       text.append(run);
/* 127:    */     }
/* 128:    */   }
/* 129:    */   
/* 130:    */   public String toString()
/* 131:    */   {
/* 132:134 */     return getText();
/* 133:    */   }
/* 134:    */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xwpf.usermodel.XWPFSDTContent
 * JD-Core Version:    0.7.0.1
 */