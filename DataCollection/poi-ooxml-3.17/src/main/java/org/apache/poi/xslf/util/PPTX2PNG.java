/*   1:    */ package org.apache.poi.xslf.util;
/*   2:    */ 
/*   3:    */ import java.awt.Dimension;
/*   4:    */ import java.awt.Graphics2D;
/*   5:    */ import java.awt.RenderingHints;
/*   6:    */ import java.awt.image.BufferedImage;
/*   7:    */ import java.io.File;
/*   8:    */ import java.io.PrintStream;
/*   9:    */ import java.util.List;
/*  10:    */ import java.util.Locale;
/*  11:    */ import java.util.Set;
/*  12:    */ import java.util.TreeSet;
/*  13:    */ import javax.imageio.ImageIO;
/*  14:    */ import org.apache.poi.sl.draw.DrawFactory;
/*  15:    */ import org.apache.poi.sl.usermodel.Slide;
/*  16:    */ import org.apache.poi.sl.usermodel.SlideShow;
/*  17:    */ import org.apache.poi.sl.usermodel.SlideShowFactory;
/*  18:    */ 
/*  19:    */ public class PPTX2PNG
/*  20:    */ {
/*  21:    */   static void usage(String error)
/*  22:    */   {
/*  23: 47 */     String msg = "Usage: PPTX2PNG [options] <ppt or pptx file>\n" + (error == null ? "" : new StringBuilder().append("Error: ").append(error).append("\n").toString()) + "Options:\n" + "    -scale <float>   scale factor\n" + "    -slide <integer> 1-based index of a slide to render\n" + "    -format <type>   png,gif,jpg (,null for testing)" + "    -outdir <dir>    output directory, defaults to origin of the ppt/pptx file" + "    -quiet           do not write to console (for normal processing)";
/*  24:    */     
/*  25:    */ 
/*  26:    */ 
/*  27:    */ 
/*  28:    */ 
/*  29:    */ 
/*  30:    */ 
/*  31:    */ 
/*  32:    */ 
/*  33: 57 */     System.out.println(msg);
/*  34:    */   }
/*  35:    */   
/*  36:    */   public static void main(String[] args)
/*  37:    */     throws Exception
/*  38:    */   {
/*  39: 62 */     if (args.length == 0)
/*  40:    */     {
/*  41: 63 */       usage(null);
/*  42: 64 */       return;
/*  43:    */     }
/*  44: 67 */     String slidenumStr = "-1";
/*  45: 68 */     float scale = 1.0F;
/*  46: 69 */     File file = null;
/*  47: 70 */     String format = "png";
/*  48: 71 */     File outdir = null;
/*  49: 72 */     boolean quiet = false;
/*  50: 74 */     for (int i = 0; i < args.length; i++) {
/*  51: 75 */       if (args[i].startsWith("-"))
/*  52:    */       {
/*  53: 76 */         if ("-scale".equals(args[i])) {
/*  54: 77 */           scale = Float.parseFloat(args[(++i)]);
/*  55: 78 */         } else if ("-slide".equals(args[i])) {
/*  56: 79 */           slidenumStr = args[(++i)];
/*  57: 80 */         } else if ("-format".equals(args[i])) {
/*  58: 81 */           format = args[(++i)];
/*  59: 82 */         } else if ("-outdir".equals(args[i])) {
/*  60: 83 */           outdir = new File(args[(++i)]);
/*  61: 84 */         } else if ("-quiet".equals(args[i])) {
/*  62: 85 */           quiet = true;
/*  63:    */         }
/*  64:    */       }
/*  65:    */       else {
/*  66: 88 */         file = new File(args[i]);
/*  67:    */       }
/*  68:    */     }
/*  69: 92 */     if ((file == null) || (!file.exists()))
/*  70:    */     {
/*  71: 93 */       usage("File not specified or it doesn't exist");
/*  72: 94 */       return;
/*  73:    */     }
/*  74: 97 */     if ((format == null) || (!format.matches("^(png|gif|jpg|null)$")))
/*  75:    */     {
/*  76: 98 */       usage("Invalid format given");
/*  77: 99 */       return;
/*  78:    */     }
/*  79:102 */     if (outdir == null) {
/*  80:103 */       outdir = file.getParentFile();
/*  81:    */     }
/*  82:106 */     if ((!"null".equals(format)) && ((outdir == null) || (!outdir.exists()) || (!outdir.isDirectory())))
/*  83:    */     {
/*  84:107 */       usage("Output directory doesn't exist");
/*  85:108 */       return;
/*  86:    */     }
/*  87:111 */     if (scale < 0.0F)
/*  88:    */     {
/*  89:112 */       usage("Invalid scale given");
/*  90:113 */       return;
/*  91:    */     }
/*  92:116 */     if (!quiet) {
/*  93:117 */       System.out.println("Processing " + file);
/*  94:    */     }
/*  95:119 */     SlideShow<?, ?> ss = SlideShowFactory.create(file, null, true);
/*  96:    */     try
/*  97:    */     {
/*  98:121 */       slides = ss.getSlides();
/*  99:    */       
/* 100:123 */       Set<Integer> slidenum = slideIndexes(slides.size(), slidenumStr);
/* 101:125 */       if (slidenum.isEmpty())
/* 102:    */       {
/* 103:126 */         usage("slidenum must be either -1 (for all) or within range: [1.." + slides.size() + "] for " + file); return;
/* 104:    */       }
/* 105:130 */       Dimension pgsize = ss.getPageSize();
/* 106:131 */       width = (int)(pgsize.width * scale);
/* 107:132 */       height = (int)(pgsize.height * scale);
/* 108:134 */       for (Integer slideNo : slidenum)
/* 109:    */       {
/* 110:135 */         Slide<?, ?> slide = (Slide)slides.get(slideNo.intValue());
/* 111:136 */         String title = slide.getTitle();
/* 112:137 */         if (!quiet) {
/* 113:138 */           System.out.println("Rendering slide " + slideNo + (title == null ? "" : new StringBuilder().append(": ").append(title).toString()));
/* 114:    */         }
/* 115:141 */         BufferedImage img = new BufferedImage(width, height, 2);
/* 116:142 */         Graphics2D graphics = img.createGraphics();
/* 117:143 */         DrawFactory.getInstance(graphics).fixFonts(graphics);
/* 118:    */         
/* 119:    */ 
/* 120:146 */         graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
/* 121:147 */         graphics.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
/* 122:148 */         graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
/* 123:149 */         graphics.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
/* 124:    */         
/* 125:151 */         graphics.scale(scale, scale);
/* 126:    */         
/* 127:    */ 
/* 128:154 */         slide.draw(graphics);
/* 129:157 */         if (!"null".equals(format))
/* 130:    */         {
/* 131:158 */           String outname = file.getName().replaceFirst(".pptx?", "");
/* 132:159 */           outname = String.format(Locale.ROOT, "%1$s-%2$04d.%3$s", new Object[] { outname, slideNo, format });
/* 133:160 */           File outfile = new File(outdir, outname);
/* 134:161 */           ImageIO.write(img, format, outfile);
/* 135:    */         }
/* 136:164 */         graphics.dispose();
/* 137:165 */         img.flush();
/* 138:    */       }
/* 139:    */     }
/* 140:    */     finally
/* 141:    */     {
/* 142:    */       List<? extends Slide<?, ?>> slides;
/* 143:    */       int width;
/* 144:    */       int height;
/* 145:168 */       ss.close();
/* 146:    */     }
/* 147:171 */     if (!quiet) {
/* 148:172 */       System.out.println("Done");
/* 149:    */     }
/* 150:    */   }
/* 151:    */   
/* 152:    */   private static Set<Integer> slideIndexes(int slideCount, String range)
/* 153:    */   {
/* 154:177 */     Set<Integer> slideIdx = new TreeSet();
/* 155:178 */     if ("-1".equals(range)) {
/* 156:179 */       for (int i = 0; i < slideCount; i++) {
/* 157:180 */         slideIdx.add(Integer.valueOf(i));
/* 158:    */       }
/* 159:    */     } else {
/* 160:183 */       for (String subrange : range.split(","))
/* 161:    */       {
/* 162:184 */         String[] idx = subrange.split("-");
/* 163:185 */         switch (idx.length)
/* 164:    */         {
/* 165:    */         case 0: 
/* 166:    */         default: 
/* 167:    */           break;
/* 168:    */         case 1: 
/* 169:189 */           int subidx = Integer.parseInt(idx[0]);
/* 170:190 */           if (subrange.contains("-"))
/* 171:    */           {
/* 172:191 */             int startIdx = subrange.startsWith("-") ? 0 : subidx;
/* 173:192 */             int endIdx = subrange.endsWith("-") ? slideCount : Math.min(subidx, slideCount);
/* 174:193 */             for (int i = Math.max(startIdx, 1); i < endIdx; i++) {
/* 175:194 */               slideIdx.add(Integer.valueOf(i - 1));
/* 176:    */             }
/* 177:    */           }
/* 178:    */           else
/* 179:    */           {
/* 180:197 */             slideIdx.add(Integer.valueOf(Math.max(subidx, 1) - 1));
/* 181:    */           }
/* 182:199 */           break;
/* 183:    */         case 2: 
/* 184:202 */           int startIdx = Math.min(Integer.parseInt(idx[0]), slideCount);
/* 185:203 */           int endIdx = Math.min(Integer.parseInt(idx[1]), slideCount);
/* 186:204 */           for (int i = Math.max(startIdx, 1); i < endIdx; i++) {
/* 187:205 */             slideIdx.add(Integer.valueOf(i - 1));
/* 188:    */           }
/* 189:    */         }
/* 190:    */       }
/* 191:    */     }
/* 192:212 */     return slideIdx;
/* 193:    */   }
/* 194:    */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xslf.util.PPTX2PNG
 * JD-Core Version:    0.7.0.1
 */