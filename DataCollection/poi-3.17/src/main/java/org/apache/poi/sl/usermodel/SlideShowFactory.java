/*   1:    */ package org.apache.poi.sl.usermodel;
/*   2:    */ 
/*   3:    */ import java.io.File;
/*   4:    */ import java.io.FileNotFoundException;
/*   5:    */ import java.io.IOException;
/*   6:    */ import java.io.InputStream;
/*   7:    */ import java.lang.reflect.InvocationTargetException;
/*   8:    */ import java.lang.reflect.Method;
/*   9:    */ import org.apache.poi.EncryptedDocumentException;
/*  10:    */ import org.apache.poi.OldFileFormatException;
/*  11:    */ import org.apache.poi.hssf.record.crypto.Biff8EncryptionKey;
/*  12:    */ import org.apache.poi.poifs.filesystem.DirectoryNode;
/*  13:    */ import org.apache.poi.poifs.filesystem.DocumentFactoryHelper;
/*  14:    */ import org.apache.poi.poifs.filesystem.FileMagic;
/*  15:    */ import org.apache.poi.poifs.filesystem.NPOIFSFileSystem;
/*  16:    */ import org.apache.poi.poifs.filesystem.OfficeXmlFileException;
/*  17:    */ import org.apache.poi.util.IOUtils;
/*  18:    */ 
/*  19:    */ public class SlideShowFactory
/*  20:    */ {
/*  21:    */   public static SlideShow<?, ?> create(NPOIFSFileSystem fs)
/*  22:    */     throws IOException
/*  23:    */   {
/*  24: 48 */     return create(fs, null);
/*  25:    */   }
/*  26:    */   
/*  27:    */   public static SlideShow<?, ?> create(NPOIFSFileSystem fs, String password)
/*  28:    */     throws IOException
/*  29:    */   {
/*  30: 63 */     DirectoryNode root = fs.getRoot();
/*  31:    */     SlideShow localSlideShow;
/*  32: 66 */     if (root.hasEntry("EncryptedPackage"))
/*  33:    */     {
/*  34: 67 */       InputStream stream = null;
/*  35:    */       try
/*  36:    */       {
/*  37: 69 */         stream = DocumentFactoryHelper.getDecryptedStream(fs, password);
/*  38:    */         
/*  39: 71 */         return createXSLFSlideShow(new Object[] { stream });
/*  40:    */       }
/*  41:    */       finally
/*  42:    */       {
/*  43: 73 */         IOUtils.closeQuietly(stream);
/*  44:    */       }
/*  45:    */     }
/*  46: 79 */     boolean passwordSet = false;
/*  47: 80 */     if (password != null)
/*  48:    */     {
/*  49: 81 */       Biff8EncryptionKey.setCurrentUserPassword(password);
/*  50: 82 */       passwordSet = true;
/*  51:    */     }
/*  52:    */     try
/*  53:    */     {
/*  54: 85 */       return createHSLFSlideShow(new Object[] { fs });
/*  55:    */     }
/*  56:    */     finally
/*  57:    */     {
/*  58: 87 */       if (passwordSet) {
/*  59: 88 */         Biff8EncryptionKey.setCurrentUserPassword(null);
/*  60:    */       }
/*  61:    */     }
/*  62:    */   }
/*  63:    */   
/*  64:    */   public static SlideShow<?, ?> create(InputStream inp)
/*  65:    */     throws IOException, EncryptedDocumentException
/*  66:    */   {
/*  67:113 */     return create(inp, null);
/*  68:    */   }
/*  69:    */   
/*  70:    */   public static SlideShow<?, ?> create(InputStream inp, String password)
/*  71:    */     throws IOException, EncryptedDocumentException
/*  72:    */   {
/*  73:137 */     InputStream is = FileMagic.prepareToCheckMagic(inp);
/*  74:138 */     FileMagic fm = FileMagic.valueOf(is);
/*  75:140 */     switch (1.$SwitchMap$org$apache$poi$poifs$filesystem$FileMagic[fm.ordinal()])
/*  76:    */     {
/*  77:    */     case 1: 
/*  78:142 */       NPOIFSFileSystem fs = new NPOIFSFileSystem(is);
/*  79:143 */       return create(fs, password);
/*  80:    */     case 2: 
/*  81:145 */       return createXSLFSlideShow(new Object[] { is });
/*  82:    */     }
/*  83:147 */     throw new IllegalArgumentException("Your InputStream was neither an OLE2 stream, nor an OOXML stream");
/*  84:    */   }
/*  85:    */   
/*  86:    */   public static SlideShow<?, ?> create(File file)
/*  87:    */     throws IOException, EncryptedDocumentException
/*  88:    */   {
/*  89:165 */     return create(file, null);
/*  90:    */   }
/*  91:    */   
/*  92:    */   public static SlideShow<?, ?> create(File file, String password)
/*  93:    */     throws IOException, EncryptedDocumentException
/*  94:    */   {
/*  95:184 */     return create(file, password, false);
/*  96:    */   }
/*  97:    */   
/*  98:    */   public static SlideShow<?, ?> create(File file, String password, boolean readOnly)
/*  99:    */     throws IOException, EncryptedDocumentException
/* 100:    */   {
/* 101:205 */     if (!file.exists()) {
/* 102:206 */       throw new FileNotFoundException(file.toString());
/* 103:    */     }
/* 104:209 */     NPOIFSFileSystem fs = null;
/* 105:    */     try
/* 106:    */     {
/* 107:211 */       fs = new NPOIFSFileSystem(file, readOnly);
/* 108:212 */       return create(fs, password);
/* 109:    */     }
/* 110:    */     catch (OfficeXmlFileException e)
/* 111:    */     {
/* 112:214 */       IOUtils.closeQuietly(fs);
/* 113:215 */       return createXSLFSlideShow(new Object[] { file, Boolean.valueOf(readOnly) });
/* 114:    */     }
/* 115:    */     catch (RuntimeException e)
/* 116:    */     {
/* 117:217 */       IOUtils.closeQuietly(fs);
/* 118:218 */       throw e;
/* 119:    */     }
/* 120:    */   }
/* 121:    */   
/* 122:    */   protected static SlideShow<?, ?> createHSLFSlideShow(Object... args)
/* 123:    */     throws IOException, EncryptedDocumentException
/* 124:    */   {
/* 125:223 */     return createSlideShow("org.apache.poi.hslf.usermodel.HSLFSlideShowFactory", args);
/* 126:    */   }
/* 127:    */   
/* 128:    */   protected static SlideShow<?, ?> createXSLFSlideShow(Object... args)
/* 129:    */     throws IOException, EncryptedDocumentException
/* 130:    */   {
/* 131:227 */     return createSlideShow("org.apache.poi.xslf.usermodel.XSLFSlideShowFactory", args);
/* 132:    */   }
/* 133:    */   
/* 134:    */   protected static SlideShow<?, ?> createSlideShow(String factoryClass, Object[] args)
/* 135:    */     throws IOException, EncryptedDocumentException
/* 136:    */   {
/* 137:    */     try
/* 138:    */     {
/* 139:232 */       Class<?> clazz = Thread.currentThread().getContextClassLoader().loadClass(factoryClass);
/* 140:233 */       Class<?>[] argsClz = new Class[args.length];
/* 141:234 */       int i = 0;
/* 142:235 */       for (Object o : args)
/* 143:    */       {
/* 144:236 */         Class<?> c = o.getClass();
/* 145:237 */         if (Boolean.class.isAssignableFrom(c)) {
/* 146:238 */           c = Boolean.TYPE;
/* 147:239 */         } else if (InputStream.class.isAssignableFrom(c)) {
/* 148:240 */           c = InputStream.class;
/* 149:    */         }
/* 150:242 */         argsClz[(i++)] = c;
/* 151:    */       }
/* 152:244 */       Method m = clazz.getMethod("createSlideShow", argsClz);
/* 153:245 */       return (SlideShow)m.invoke(null, args);
/* 154:    */     }
/* 155:    */     catch (InvocationTargetException e)
/* 156:    */     {
/* 157:247 */       Throwable t = e.getCause();
/* 158:248 */       if ((t instanceof IOException)) {
/* 159:249 */         throw ((IOException)t);
/* 160:    */       }
/* 161:250 */       if ((t instanceof EncryptedDocumentException)) {
/* 162:251 */         throw ((EncryptedDocumentException)t);
/* 163:    */       }
/* 164:252 */       if ((t instanceof OldFileFormatException)) {
/* 165:253 */         throw ((OldFileFormatException)t);
/* 166:    */       }
/* 167:255 */       throw new IOException(t);
/* 168:    */     }
/* 169:    */     catch (Exception e)
/* 170:    */     {
/* 171:258 */       throw new IOException(e);
/* 172:    */     }
/* 173:    */   }
/* 174:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.sl.usermodel.SlideShowFactory
 * JD-Core Version:    0.7.0.1
 */