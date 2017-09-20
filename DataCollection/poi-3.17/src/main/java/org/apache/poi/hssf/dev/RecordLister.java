/*   1:    */ package org.apache.poi.hssf.dev;
/*   2:    */ 
/*   3:    */ import java.io.File;
/*   4:    */ import java.io.IOException;
/*   5:    */ import java.io.InputStream;
/*   6:    */ import java.io.PrintStream;
/*   7:    */ import org.apache.poi.hssf.record.Record;
/*   8:    */ import org.apache.poi.hssf.record.RecordFactory;
/*   9:    */ import org.apache.poi.hssf.record.RecordInputStream;
/*  10:    */ import org.apache.poi.poifs.filesystem.NPOIFSFileSystem;
/*  11:    */ 
/*  12:    */ public class RecordLister
/*  13:    */ {
/*  14:    */   String file;
/*  15:    */   
/*  16:    */   public void run()
/*  17:    */     throws IOException
/*  18:    */   {
/*  19: 53 */     NPOIFSFileSystem fs = new NPOIFSFileSystem(new File(this.file), true);
/*  20:    */     try
/*  21:    */     {
/*  22: 55 */       InputStream din = BiffViewer.getPOIFSInputStream(fs);
/*  23:    */       try
/*  24:    */       {
/*  25: 57 */         RecordInputStream rinp = new RecordInputStream(din);
/*  26: 59 */         while (rinp.hasNextRecord())
/*  27:    */         {
/*  28: 60 */           int sid = rinp.getNextSid();
/*  29: 61 */           rinp.nextRecord();
/*  30:    */           
/*  31: 63 */           int size = rinp.available();
/*  32: 64 */           Class<? extends Record> clz = RecordFactory.getRecordClass(sid);
/*  33:    */           
/*  34: 66 */           System.out.print(formatSID(sid) + " - " + formatSize(size) + " bytes");
/*  35: 72 */           if (clz != null)
/*  36:    */           {
/*  37: 73 */             System.out.print("  \t");
/*  38: 74 */             System.out.print(clz.getName().replace("org.apache.poi.hssf.record.", ""));
/*  39:    */           }
/*  40: 76 */           System.out.println();
/*  41:    */           
/*  42: 78 */           byte[] data = rinp.readRemainder();
/*  43: 79 */           if (data.length > 0)
/*  44:    */           {
/*  45: 80 */             System.out.print("   ");
/*  46: 81 */             System.out.println(formatData(data));
/*  47:    */           }
/*  48:    */         }
/*  49:    */       }
/*  50:    */       finally {}
/*  51:    */     }
/*  52:    */     finally
/*  53:    */     {
/*  54: 88 */       fs.close();
/*  55:    */     }
/*  56:    */   }
/*  57:    */   
/*  58:    */   private static String formatSID(int sid)
/*  59:    */   {
/*  60: 93 */     String hex = Integer.toHexString(sid);
/*  61: 94 */     String dec = Integer.toString(sid);
/*  62:    */     
/*  63: 96 */     StringBuffer s = new StringBuffer();
/*  64: 97 */     s.append("0x");
/*  65: 98 */     for (int i = hex.length(); i < 4; i++) {
/*  66: 99 */       s.append('0');
/*  67:    */     }
/*  68:101 */     s.append(hex);
/*  69:    */     
/*  70:103 */     s.append(" (");
/*  71:104 */     for (int i = dec.length(); i < 4; i++) {
/*  72:105 */       s.append('0');
/*  73:    */     }
/*  74:107 */     s.append(dec);
/*  75:108 */     s.append(")");
/*  76:    */     
/*  77:110 */     return s.toString();
/*  78:    */   }
/*  79:    */   
/*  80:    */   private static String formatSize(int size)
/*  81:    */   {
/*  82:113 */     String hex = Integer.toHexString(size);
/*  83:114 */     String dec = Integer.toString(size);
/*  84:    */     
/*  85:116 */     StringBuffer s = new StringBuffer();
/*  86:117 */     for (int i = hex.length(); i < 3; i++) {
/*  87:118 */       s.append('0');
/*  88:    */     }
/*  89:120 */     s.append(hex);
/*  90:    */     
/*  91:122 */     s.append(" (");
/*  92:123 */     for (int i = dec.length(); i < 3; i++) {
/*  93:124 */       s.append('0');
/*  94:    */     }
/*  95:126 */     s.append(dec);
/*  96:127 */     s.append(")");
/*  97:    */     
/*  98:129 */     return s.toString();
/*  99:    */   }
/* 100:    */   
/* 101:    */   private static String formatData(byte[] data)
/* 102:    */   {
/* 103:132 */     if ((data == null) || (data.length == 0)) {
/* 104:133 */       return "";
/* 105:    */     }
/* 106:136 */     StringBuffer s = new StringBuffer();
/* 107:137 */     if (data.length > 9)
/* 108:    */     {
/* 109:138 */       s.append(byteToHex(data[0]));
/* 110:139 */       s.append(' ');
/* 111:140 */       s.append(byteToHex(data[1]));
/* 112:141 */       s.append(' ');
/* 113:142 */       s.append(byteToHex(data[2]));
/* 114:143 */       s.append(' ');
/* 115:144 */       s.append(byteToHex(data[3]));
/* 116:145 */       s.append(' ');
/* 117:    */       
/* 118:147 */       s.append(" .... ");
/* 119:    */       
/* 120:149 */       s.append(' ');
/* 121:150 */       s.append(byteToHex(data[(data.length - 4)]));
/* 122:151 */       s.append(' ');
/* 123:152 */       s.append(byteToHex(data[(data.length - 3)]));
/* 124:153 */       s.append(' ');
/* 125:154 */       s.append(byteToHex(data[(data.length - 2)]));
/* 126:155 */       s.append(' ');
/* 127:156 */       s.append(byteToHex(data[(data.length - 1)]));
/* 128:    */     }
/* 129:    */     else
/* 130:    */     {
/* 131:158 */       for (int i = 0; i < data.length; i++)
/* 132:    */       {
/* 133:159 */         s.append(byteToHex(data[i]));
/* 134:160 */         s.append(' ');
/* 135:    */       }
/* 136:    */     }
/* 137:164 */     return s.toString();
/* 138:    */   }
/* 139:    */   
/* 140:    */   private static String byteToHex(byte b)
/* 141:    */   {
/* 142:167 */     int i = b;
/* 143:168 */     if (i < 0) {
/* 144:169 */       i += 256;
/* 145:    */     }
/* 146:171 */     String s = Integer.toHexString(i);
/* 147:172 */     if (i < 16) {
/* 148:173 */       return "0" + s;
/* 149:    */     }
/* 150:175 */     return s;
/* 151:    */   }
/* 152:    */   
/* 153:    */   public void setFile(String file)
/* 154:    */   {
/* 155:180 */     this.file = file;
/* 156:    */   }
/* 157:    */   
/* 158:    */   public static void main(String[] args)
/* 159:    */     throws IOException
/* 160:    */   {
/* 161:185 */     if ((args.length == 1) && (!args[0].equals("--help")))
/* 162:    */     {
/* 163:187 */       RecordLister viewer = new RecordLister();
/* 164:    */       
/* 165:189 */       viewer.setFile(args[0]);
/* 166:190 */       viewer.run();
/* 167:    */     }
/* 168:    */     else
/* 169:    */     {
/* 170:194 */       System.out.println("RecordLister");
/* 171:195 */       System.out.println("Outputs the summary of the records in file order");
/* 172:    */       
/* 173:197 */       System.out.println("usage: java org.apache.poi.hssf.dev.RecordLister filename");
/* 174:    */     }
/* 175:    */   }
/* 176:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.dev.RecordLister
 * JD-Core Version:    0.7.0.1
 */