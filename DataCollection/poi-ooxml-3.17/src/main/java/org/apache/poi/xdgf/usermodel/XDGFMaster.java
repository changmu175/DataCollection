/*  1:   */ package org.apache.poi.xdgf.usermodel;
/*  2:   */ 
/*  3:   */ import com.microsoft.schemas.office.visio.x2012.main.MasterType;
/*  4:   */ import org.apache.poi.util.Internal;
/*  5:   */ 
/*  6:   */ public class XDGFMaster
/*  7:   */ {
/*  8:   */   private MasterType _master;
/*  9:   */   protected XDGFMasterContents _content;
/* 10:32 */   protected XDGFSheet _pageSheet = null;
/* 11:   */   
/* 12:   */   public XDGFMaster(MasterType master, XDGFMasterContents content, XDGFDocument document)
/* 13:   */   {
/* 14:36 */     this._master = master;
/* 15:37 */     this._content = content;
/* 16:38 */     content.setMaster(this);
/* 17:40 */     if (master.isSetPageSheet()) {
/* 18:41 */       this._pageSheet = new XDGFPageSheet(master.getPageSheet(), document);
/* 19:   */     }
/* 20:   */   }
/* 21:   */   
/* 22:   */   @Internal
/* 23:   */   protected MasterType getXmlObject()
/* 24:   */   {
/* 25:46 */     return this._master;
/* 26:   */   }
/* 27:   */   
/* 28:   */   public String toString()
/* 29:   */   {
/* 30:51 */     return "<Master ID=\"" + getID() + "\" " + this._content + ">";
/* 31:   */   }
/* 32:   */   
/* 33:   */   public long getID()
/* 34:   */   {
/* 35:55 */     return this._master.getID();
/* 36:   */   }
/* 37:   */   
/* 38:   */   public String getName()
/* 39:   */   {
/* 40:59 */     return this._master.getName();
/* 41:   */   }
/* 42:   */   
/* 43:   */   public XDGFMasterContents getContent()
/* 44:   */   {
/* 45:63 */     return this._content;
/* 46:   */   }
/* 47:   */   
/* 48:   */   public XDGFSheet getPageSheet()
/* 49:   */   {
/* 50:67 */     return this._pageSheet;
/* 51:   */   }
/* 52:   */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xdgf.usermodel.XDGFMaster
 * JD-Core Version:    0.7.0.1
 */