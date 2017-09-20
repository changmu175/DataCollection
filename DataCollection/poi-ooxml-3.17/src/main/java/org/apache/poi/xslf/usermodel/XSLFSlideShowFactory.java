/*  1:   */ package org.apache.poi.xslf.usermodel;
/*  2:   */ 
/*  3:   */ import java.io.File;
/*  4:   */ import java.io.IOException;
/*  5:   */ import java.io.InputStream;
/*  6:   */ import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
/*  7:   */ import org.apache.poi.openxml4j.opc.OPCPackage;
/*  8:   */ import org.apache.poi.openxml4j.opc.PackageAccess;
/*  9:   */ import org.apache.poi.sl.usermodel.SlideShow;
/* 10:   */ import org.apache.poi.sl.usermodel.SlideShowFactory;
/* 11:   */ import org.apache.poi.util.Internal;
/* 12:   */ 
/* 13:   */ @Internal
/* 14:   */ public class XSLFSlideShowFactory
/* 15:   */   extends SlideShowFactory
/* 16:   */ {
/* 17:   */   public static SlideShow<?, ?> createSlideShow(OPCPackage pkg)
/* 18:   */     throws IOException
/* 19:   */   {
/* 20:   */     try
/* 21:   */     {
/* 22:50 */       return new XMLSlideShow(pkg);
/* 23:   */     }
/* 24:   */     catch (IllegalArgumentException ioe)
/* 25:   */     {
/* 26:53 */       pkg.revert();
/* 27:   */       
/* 28:   */ 
/* 29:   */ 
/* 30:57 */       throw ioe;
/* 31:   */     }
/* 32:   */   }
/* 33:   */   
/* 34:   */   public static SlideShow<?, ?> createSlideShow(File file, boolean readOnly)
/* 35:   */     throws IOException, InvalidFormatException
/* 36:   */   {
/* 37:77 */     OPCPackage pkg = OPCPackage.open(file, readOnly ? PackageAccess.READ : PackageAccess.READ_WRITE);
/* 38:78 */     return createSlideShow(pkg);
/* 39:   */   }
/* 40:   */   
/* 41:   */   public static SlideShow<?, ?> createSlideShow(InputStream stream)
/* 42:   */     throws IOException, InvalidFormatException
/* 43:   */   {
/* 44:96 */     OPCPackage pkg = OPCPackage.open(stream);
/* 45:97 */     return createSlideShow(pkg);
/* 46:   */   }
/* 47:   */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.xslf.usermodel.XSLFSlideShowFactory
 * JD-Core Version:    0.7.0.1
 */