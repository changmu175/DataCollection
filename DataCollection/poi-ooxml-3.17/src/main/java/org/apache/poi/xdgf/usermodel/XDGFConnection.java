/*   1:    */ package org.apache.poi.xdgf.usermodel;
/*   2:    */ 
/*   3:    */ import com.microsoft.schemas.office.visio.x2012.main.ConnectType;
/*   4:    */ 
/*   5:    */ public class XDGFConnection
/*   6:    */ {
/*   7:    */   public static final int visConnectFromError = -1;
/*   8:    */   public static final int visFromNone = 0;
/*   9:    */   public static final int visLeftEdge = 1;
/*  10:    */   public static final int visCenterEdge = 2;
/*  11:    */   public static final int visRightEdge = 3;
/*  12:    */   public static final int visBottomEdge = 4;
/*  13:    */   public static final int visMiddleEdge = 5;
/*  14:    */   public static final int visTopEdge = 6;
/*  15:    */   public static final int visBeginX = 7;
/*  16:    */   public static final int visBeginY = 8;
/*  17:    */   public static final int visBegin = 9;
/*  18:    */   public static final int visEndX = 10;
/*  19:    */   public static final int visEndY = 11;
/*  20:    */   public static final int visEnd = 12;
/*  21:    */   public static final int visFromAngle = 13;
/*  22:    */   public static final int visFromPin = 14;
/*  23:    */   public static final int visConnectToError = -1;
/*  24:    */   public static final int visToNone = 0;
/*  25:    */   public static final int visGuideX = 1;
/*  26:    */   public static final int visGuideY = 2;
/*  27:    */   public static final int visWholeShape = 3;
/*  28:    */   public static final int visGuideIntersect = 4;
/*  29:    */   public static final int visToAngle = 7;
/*  30:    */   private ConnectType _connect;
/*  31:    */   private XDGFShape _from;
/*  32:    */   private XDGFShape _to;
/*  33:    */   
/*  34:    */   public XDGFConnection(ConnectType connect, XDGFShape from, XDGFShape to)
/*  35:    */   {
/*  36:105 */     this._connect = connect;
/*  37:106 */     this._from = from;
/*  38:107 */     this._to = to;
/*  39:    */   }
/*  40:    */   
/*  41:    */   public XDGFShape getFromShape()
/*  42:    */   {
/*  43:111 */     return this._from;
/*  44:    */   }
/*  45:    */   
/*  46:    */   public XDGFCell getFromCell()
/*  47:    */   {
/*  48:115 */     return this._from.getCell(this._connect.getFromCell());
/*  49:    */   }
/*  50:    */   
/*  51:    */   public String getFromCellName()
/*  52:    */   {
/*  53:119 */     return this._connect.getFromCell();
/*  54:    */   }
/*  55:    */   
/*  56:    */   public XDGFShape getToShape()
/*  57:    */   {
/*  58:123 */     return this._to;
/*  59:    */   }
/*  60:    */   
/*  61:    */   public String getToCellName()
/*  62:    */   {
/*  63:127 */     return this._connect.getToCell();
/*  64:    */   }
/*  65:    */   
/*  66:    */   public Integer getFromPart()
/*  67:    */   {
/*  68:132 */     if (this._connect.isSetFromPart()) {
/*  69:133 */       return Integer.valueOf(this._connect.getFromPart());
/*  70:    */     }
/*  71:135 */     return null;
/*  72:    */   }
/*  73:    */   
/*  74:    */   public Integer getToPart()
/*  75:    */   {
/*  76:144 */     if (this._connect.isSetToPart()) {
/*  77:145 */       return Integer.valueOf(this._connect.getToPart());
/*  78:    */     }
/*  79:147 */     return null;
/*  80:    */   }
/*  81:    */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xdgf.usermodel.XDGFConnection
 * JD-Core Version:    0.7.0.1
 */