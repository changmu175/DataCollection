/*   1:    */ package org.apache.poi.sl.draw;
/*   2:    */ 
/*   3:    */ import java.awt.Color;
/*   4:    */ import java.awt.Graphics2D;
/*   5:    */ import java.awt.Paint;
/*   6:    */ import java.awt.geom.Line2D;
/*   7:    */ import java.awt.geom.Line2D.Double;
/*   8:    */ import java.awt.geom.Rectangle2D;
/*   9:    */ import org.apache.poi.sl.usermodel.FillStyle;
/*  10:    */ import org.apache.poi.sl.usermodel.GroupShape;
/*  11:    */ import org.apache.poi.sl.usermodel.StrokeStyle;
/*  12:    */ import org.apache.poi.sl.usermodel.StrokeStyle.LineCompound;
/*  13:    */ import org.apache.poi.sl.usermodel.StrokeStyle.LineDash;
/*  14:    */ import org.apache.poi.sl.usermodel.TableCell;
/*  15:    */ import org.apache.poi.sl.usermodel.TableCell.BorderEdge;
/*  16:    */ import org.apache.poi.sl.usermodel.TableShape;
/*  17:    */ import org.apache.poi.util.Internal;
/*  18:    */ 
/*  19:    */ public class DrawTableShape
/*  20:    */   extends DrawShape
/*  21:    */ {
/*  22:    */   @Internal
/*  23:    */   public static final int borderSize = 2;
/*  24:    */   
/*  25:    */   public DrawTableShape(TableShape<?, ?> shape)
/*  26:    */   {
/*  27: 43 */     super(shape);
/*  28:    */   }
/*  29:    */   
/*  30:    */   protected Drawable getGroupShape(Graphics2D graphics)
/*  31:    */   {
/*  32: 47 */     if ((this.shape instanceof GroupShape))
/*  33:    */     {
/*  34: 48 */       DrawFactory df = DrawFactory.getInstance(graphics);
/*  35: 49 */       return df.getDrawable((GroupShape)this.shape);
/*  36:    */     }
/*  37: 51 */     return null;
/*  38:    */   }
/*  39:    */   
/*  40:    */   public void applyTransform(Graphics2D graphics)
/*  41:    */   {
/*  42: 55 */     Drawable d = getGroupShape(graphics);
/*  43: 56 */     if (d != null) {
/*  44: 57 */       d.applyTransform(graphics);
/*  45:    */     } else {
/*  46: 59 */       super.applyTransform(graphics);
/*  47:    */     }
/*  48:    */   }
/*  49:    */   
/*  50:    */   public void draw(Graphics2D graphics)
/*  51:    */   {
/*  52: 64 */     Drawable d = getGroupShape(graphics);
/*  53: 65 */     if (d != null)
/*  54:    */     {
/*  55: 66 */       d.draw(graphics);
/*  56: 67 */       return;
/*  57:    */     }
/*  58: 70 */     TableShape<?, ?> ts = getShape();
/*  59: 71 */     DrawPaint drawPaint = DrawFactory.getInstance(graphics).getPaint(ts);
/*  60: 72 */     int rows = ts.getNumberOfRows();
/*  61: 73 */     int cols = ts.getNumberOfColumns();
/*  62: 76 */     for (int row = 0; row < rows; row++) {
/*  63: 77 */       for (int col = 0; col < cols; col++)
/*  64:    */       {
/*  65: 78 */         TableCell<?, ?> tc = ts.getCell(row, col);
/*  66: 79 */         if ((tc != null) && (!tc.isMerged()))
/*  67:    */         {
/*  68: 83 */           Paint fillPaint = drawPaint.getPaint(graphics, tc.getFillStyle().getPaint());
/*  69: 84 */           graphics.setPaint(fillPaint);
/*  70: 85 */           Rectangle2D cellAnc = tc.getAnchor();
/*  71: 86 */           graphics.fill(cellAnc);
/*  72: 88 */           for (TableCell.BorderEdge edge : TableCell.BorderEdge.values())
/*  73:    */           {
/*  74: 89 */             StrokeStyle stroke = tc.getBorderStyle(edge);
/*  75: 90 */             if (stroke != null)
/*  76:    */             {
/*  77: 93 */               graphics.setStroke(getStroke(stroke));
/*  78: 94 */               Paint linePaint = drawPaint.getPaint(graphics, stroke.getPaint());
/*  79: 95 */               graphics.setPaint(linePaint);
/*  80:    */               
/*  81: 97 */               double x = cellAnc.getX();double y = cellAnc.getY();double w = cellAnc.getWidth();double h = cellAnc.getHeight();
/*  82:    */               Line2D line;
/*  83: 99 */               switch (1.$SwitchMap$org$apache$poi$sl$usermodel$TableCell$BorderEdge[edge.ordinal()])
/*  84:    */               {
/*  85:    */               case 1: 
/*  86:    */               default: 
/*  87:102 */                 line = new Double(x - 2.0D, y + h, x + w + 2.0D, y + h);
/*  88:103 */                 break;
/*  89:    */               case 2: 
/*  90:105 */                 line = new Double(x, y, x, y + h + 2.0D);
/*  91:106 */                 break;
/*  92:    */               case 3: 
/*  93:108 */                 line = new Double(x + w, y, x + w, y + h + 2.0D);
/*  94:109 */                 break;
/*  95:    */               case 4: 
/*  96:111 */                 line = new Double(x - 2.0D, y, x + w + 2.0D, y);
/*  97:    */               }
/*  98:115 */               graphics.draw(line);
/*  99:    */             }
/* 100:    */           }
/* 101:    */         }
/* 102:    */       }
/* 103:    */     }
/* 104:121 */     drawContent(graphics);
/* 105:    */   }
/* 106:    */   
/* 107:    */   public void drawContent(Graphics2D graphics)
/* 108:    */   {
/* 109:125 */     Drawable d = getGroupShape(graphics);
/* 110:126 */     if (d != null)
/* 111:    */     {
/* 112:127 */       d.drawContent(graphics);
/* 113:128 */       return;
/* 114:    */     }
/* 115:131 */     TableShape<?, ?> ts = getShape();
/* 116:132 */     DrawFactory df = DrawFactory.getInstance(graphics);
/* 117:    */     
/* 118:134 */     int rows = ts.getNumberOfRows();
/* 119:135 */     int cols = ts.getNumberOfColumns();
/* 120:137 */     for (int row = 0; row < rows; row++) {
/* 121:138 */       for (int col = 0; col < cols; col++)
/* 122:    */       {
/* 123:139 */         TableCell<?, ?> tc = ts.getCell(row, col);
/* 124:140 */         if (tc != null)
/* 125:    */         {
/* 126:141 */           DrawTextShape dts = df.getDrawable(tc);
/* 127:142 */           dts.drawContent(graphics);
/* 128:    */         }
/* 129:    */       }
/* 130:    */     }
/* 131:    */   }
/* 132:    */   
/* 133:    */   protected TableShape<?, ?> getShape()
/* 134:    */   {
/* 135:150 */     return (TableShape)this.shape;
/* 136:    */   }
/* 137:    */   
/* 138:    */   public void setAllBorders(Object... args)
/* 139:    */   {
/* 140:162 */     TableShape<?, ?> table = getShape();
/* 141:163 */     int rows = table.getNumberOfRows();
/* 142:164 */     int cols = table.getNumberOfColumns();
/* 143:    */     
/* 144:166 */     TableCell.BorderEdge[] edges = { TableCell.BorderEdge.top, TableCell.BorderEdge.left, null, null };
/* 145:167 */     for (int row = 0; row < rows; row++) {
/* 146:168 */       for (int col = 0; col < cols; col++)
/* 147:    */       {
/* 148:169 */         edges[2] = (col == cols - 1 ? TableCell.BorderEdge.right : null);
/* 149:170 */         edges[3] = (row == rows - 1 ? TableCell.BorderEdge.bottom : null);
/* 150:171 */         setEdges(table.getCell(row, col), edges, args);
/* 151:    */       }
/* 152:    */     }
/* 153:    */   }
/* 154:    */   
/* 155:    */   public void setOutsideBorders(Object... args)
/* 156:    */   {
/* 157:184 */     if (args.length == 0) {
/* 158:184 */       return;
/* 159:    */     }
/* 160:186 */     TableShape<?, ?> table = getShape();
/* 161:187 */     int rows = table.getNumberOfRows();
/* 162:188 */     int cols = table.getNumberOfColumns();
/* 163:    */     
/* 164:190 */     TableCell.BorderEdge[] edges = new TableCell.BorderEdge[4];
/* 165:191 */     for (int row = 0; row < rows; row++) {
/* 166:192 */       for (int col = 0; col < cols; col++)
/* 167:    */       {
/* 168:193 */         edges[0] = (col == 0 ? TableCell.BorderEdge.left : null);
/* 169:194 */         edges[1] = (col == cols - 1 ? TableCell.BorderEdge.right : null);
/* 170:195 */         edges[2] = (row == 0 ? TableCell.BorderEdge.top : null);
/* 171:196 */         edges[3] = (row == rows - 1 ? TableCell.BorderEdge.bottom : null);
/* 172:197 */         setEdges(table.getCell(row, col), edges, args);
/* 173:    */       }
/* 174:    */     }
/* 175:    */   }
/* 176:    */   
/* 177:    */   public void setInsideBorders(Object... args)
/* 178:    */   {
/* 179:210 */     if (args.length == 0) {
/* 180:210 */       return;
/* 181:    */     }
/* 182:212 */     TableShape<?, ?> table = getShape();
/* 183:213 */     int rows = table.getNumberOfRows();
/* 184:214 */     int cols = table.getNumberOfColumns();
/* 185:    */     
/* 186:216 */     TableCell.BorderEdge[] edges = new TableCell.BorderEdge[2];
/* 187:217 */     for (int row = 0; row < rows; row++) {
/* 188:218 */       for (int col = 0; col < cols; col++)
/* 189:    */       {
/* 190:219 */         edges[0] = ((col > 0) && (col < cols - 1) ? TableCell.BorderEdge.right : null);
/* 191:220 */         edges[1] = ((row > 0) && (row < rows - 1) ? TableCell.BorderEdge.bottom : null);
/* 192:221 */         setEdges(table.getCell(row, col), edges, args);
/* 193:    */       }
/* 194:    */     }
/* 195:    */   }
/* 196:    */   
/* 197:    */   private static void setEdges(TableCell<?, ?> cell, TableCell.BorderEdge[] edges, Object... args)
/* 198:    */   {
/* 199:234 */     if (cell == null) {
/* 200:235 */       return;
/* 201:    */     }
/* 202:237 */     for (TableCell.BorderEdge be : edges) {
/* 203:238 */       if (be != null) {
/* 204:239 */         if (args.length == 0) {
/* 205:240 */           cell.removeBorder(be);
/* 206:    */         } else {
/* 207:242 */           for (Object o : args) {
/* 208:243 */             if ((o instanceof Double)) {
/* 209:244 */               cell.setBorderWidth(be, ((Double)o).doubleValue());
/* 210:245 */             } else if ((o instanceof Color)) {
/* 211:246 */               cell.setBorderColor(be, (Color)o);
/* 212:247 */             } else if ((o instanceof StrokeStyle.LineDash)) {
/* 213:248 */               cell.setBorderDash(be, (StrokeStyle.LineDash)o);
/* 214:249 */             } else if ((o instanceof StrokeStyle.LineCompound)) {
/* 215:250 */               cell.setBorderCompound(be, (StrokeStyle.LineCompound)o);
/* 216:    */             }
/* 217:    */           }
/* 218:    */         }
/* 219:    */       }
/* 220:    */     }
/* 221:    */   }
/* 222:    */ }



/* Location:           F:\poi-3.17.jar

 * Qualified Name:     org.apache.poi.sl.draw.DrawTableShape

 * JD-Core Version:    0.7.0.1

 */