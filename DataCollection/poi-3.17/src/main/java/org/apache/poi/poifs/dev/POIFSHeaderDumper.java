/*   1:    */ package org.apache.poi.poifs.dev;
/*   2:    */ 
/*   3:    */ import java.io.FileInputStream;
/*   4:    */ import java.io.InputStream;
/*   5:    */ import java.io.PrintStream;
/*   6:    */ import org.apache.poi.poifs.common.POIFSBigBlockSize;
/*   7:    */ import org.apache.poi.poifs.property.DirectoryProperty;
/*   8:    */ import org.apache.poi.poifs.property.Property;
/*   9:    */ import org.apache.poi.poifs.property.PropertyTable;
/*  10:    */ import org.apache.poi.poifs.property.RootProperty;
/*  11:    */ import org.apache.poi.poifs.storage.BlockAllocationTableReader;
/*  12:    */ import org.apache.poi.poifs.storage.HeaderBlock;
/*  13:    */ import org.apache.poi.poifs.storage.ListManagedBlock;
/*  14:    */ import org.apache.poi.poifs.storage.RawDataBlockList;
/*  15:    */ import org.apache.poi.poifs.storage.SmallBlockTableReader;
/*  16:    */ import org.apache.poi.util.HexDump;
/*  17:    */ import org.apache.poi.util.IntList;
/*  18:    */ 
/*  19:    */ public class POIFSHeaderDumper
/*  20:    */ {
/*  21:    */   public static void main(String[] args)
/*  22:    */     throws Exception
/*  23:    */   {
/*  24: 50 */     if (args.length == 0)
/*  25:    */     {
/*  26: 51 */       System.err.println("Must specify at least one file to view");
/*  27: 52 */       System.exit(1);
/*  28:    */     }
/*  29: 55 */     for (int j = 0; j < args.length; j++) {
/*  30: 56 */       viewFile(args[j]);
/*  31:    */     }
/*  32:    */   }
/*  33:    */   
/*  34:    */   public static void viewFile(String filename)
/*  35:    */     throws Exception
/*  36:    */   {
/*  37: 61 */     System.out.println("Dumping headers from: " + filename);
/*  38: 62 */     InputStream inp = new FileInputStream(filename);
/*  39:    */     
/*  40:    */ 
/*  41: 65 */     HeaderBlock header_block = new HeaderBlock(inp);
/*  42: 66 */     displayHeader(header_block);
/*  43:    */     
/*  44:    */ 
/*  45: 69 */     POIFSBigBlockSize bigBlockSize = header_block.getBigBlockSize();
/*  46: 70 */     RawDataBlockList data_blocks = new RawDataBlockList(inp, bigBlockSize);
/*  47: 71 */     displayRawBlocksSummary(data_blocks);
/*  48:    */     
/*  49:    */ 
/*  50: 74 */     BlockAllocationTableReader batReader = new BlockAllocationTableReader(header_block.getBigBlockSize(), header_block.getBATCount(), header_block.getBATArray(), header_block.getXBATCount(), header_block.getXBATIndex(), data_blocks);
/*  51:    */     
/*  52:    */ 
/*  53:    */ 
/*  54:    */ 
/*  55:    */ 
/*  56:    */ 
/*  57:    */ 
/*  58: 82 */     displayBATReader("Big Blocks", batReader);
/*  59:    */     
/*  60:    */ 
/*  61: 85 */     PropertyTable properties = new PropertyTable(header_block, data_blocks);
/*  62:    */     
/*  63:    */ 
/*  64:    */ 
/*  65: 89 */     BlockAllocationTableReader sbatReader = SmallBlockTableReader._getSmallDocumentBlockReader(bigBlockSize, data_blocks, properties.getRoot(), header_block.getSBATStart());
/*  66:    */     
/*  67:    */ 
/*  68:    */ 
/*  69:    */ 
/*  70: 94 */     displayBATReader("Small Blocks", sbatReader);
/*  71:    */     
/*  72:    */ 
/*  73: 97 */     displayPropertiesSummary(properties);
/*  74:    */   }
/*  75:    */   
/*  76:    */   public static void displayHeader(HeaderBlock header_block)
/*  77:    */     throws Exception
/*  78:    */   {
/*  79:101 */     System.out.println("Header Details:");
/*  80:102 */     System.out.println(" Block size: " + header_block.getBigBlockSize().getBigBlockSize());
/*  81:103 */     System.out.println(" BAT (FAT) header blocks: " + header_block.getBATArray().length);
/*  82:104 */     System.out.println(" BAT (FAT) block count: " + header_block.getBATCount());
/*  83:105 */     if (header_block.getBATCount() > 0) {
/*  84:106 */       System.out.println(" BAT (FAT) block 1 at: " + header_block.getBATArray()[0]);
/*  85:    */     }
/*  86:107 */     System.out.println(" XBAT (FAT) block count: " + header_block.getXBATCount());
/*  87:108 */     System.out.println(" XBAT (FAT) block 1 at: " + header_block.getXBATIndex());
/*  88:109 */     System.out.println(" SBAT (MiniFAT) block count: " + header_block.getSBATCount());
/*  89:110 */     System.out.println(" SBAT (MiniFAT) block 1 at: " + header_block.getSBATStart());
/*  90:111 */     System.out.println(" Property table at: " + header_block.getPropertyStart());
/*  91:112 */     System.out.println("");
/*  92:    */   }
/*  93:    */   
/*  94:    */   public static void displayRawBlocksSummary(RawDataBlockList data_blocks)
/*  95:    */     throws Exception
/*  96:    */   {
/*  97:116 */     System.out.println("Raw Blocks Details:");
/*  98:117 */     System.out.println(" Number of blocks: " + data_blocks.blockCount());
/*  99:119 */     for (int i = 0; i < Math.min(16, data_blocks.blockCount()); i++)
/* 100:    */     {
/* 101:120 */       ListManagedBlock block = data_blocks.get(i);
/* 102:121 */       byte[] data = new byte[Math.min(48, block.getData().length)];
/* 103:122 */       System.arraycopy(block.getData(), 0, data, 0, data.length);
/* 104:    */       
/* 105:124 */       System.out.println(" Block #" + i + ":");
/* 106:125 */       System.out.println(HexDump.dump(data, 0L, 0));
/* 107:    */     }
/* 108:128 */     System.out.println("");
/* 109:    */   }
/* 110:    */   
/* 111:    */   public static void displayBATReader(String type, BlockAllocationTableReader batReader)
/* 112:    */     throws Exception
/* 113:    */   {
/* 114:132 */     System.out.println("Sectors, as referenced from the " + type + " FAT:");
/* 115:133 */     IntList entries = batReader.getEntries();
/* 116:135 */     for (int i = 0; i < entries.size(); i++)
/* 117:    */     {
/* 118:136 */       int bn = entries.get(i);
/* 119:137 */       String bnS = Integer.toString(bn);
/* 120:138 */       if (bn == -2) {
/* 121:139 */         bnS = "End Of Chain";
/* 122:140 */       } else if (bn == -4) {
/* 123:141 */         bnS = "DI Fat Block";
/* 124:142 */       } else if (bn == -3) {
/* 125:143 */         bnS = "Normal Fat Block";
/* 126:144 */       } else if (bn == -1) {
/* 127:145 */         bnS = "Block Not Used (Free)";
/* 128:    */       }
/* 129:148 */       System.out.println("  Block  # " + i + " -> " + bnS);
/* 130:    */     }
/* 131:151 */     System.out.println("");
/* 132:    */   }
/* 133:    */   
/* 134:    */   public static void displayPropertiesSummary(PropertyTable properties)
/* 135:    */   {
/* 136:155 */     System.out.println("Mini Stream starts at " + properties.getRoot().getStartBlock());
/* 137:156 */     System.out.println("Mini Stream length is " + properties.getRoot().getSize());
/* 138:157 */     System.out.println();
/* 139:    */     
/* 140:159 */     System.out.println("Properties and their block start:");
/* 141:160 */     displayProperties(properties.getRoot(), "");
/* 142:161 */     System.out.println("");
/* 143:    */   }
/* 144:    */   
/* 145:    */   public static void displayProperties(DirectoryProperty prop, String indent)
/* 146:    */   {
/* 147:164 */     String nextIndent = indent + "  ";
/* 148:165 */     System.out.println(indent + "-> " + prop.getName());
/* 149:166 */     for (Property cp : prop) {
/* 150:167 */       if ((cp instanceof DirectoryProperty))
/* 151:    */       {
/* 152:168 */         displayProperties((DirectoryProperty)cp, nextIndent);
/* 153:    */       }
/* 154:    */       else
/* 155:    */       {
/* 156:170 */         System.out.println(nextIndent + "=> " + cp.getName());
/* 157:171 */         System.out.print(nextIndent + "   " + cp.getSize() + " bytes in ");
/* 158:172 */         if (cp.shouldUseSmallBlocks()) {
/* 159:173 */           System.out.print("mini");
/* 160:    */         } else {
/* 161:175 */           System.out.print("main");
/* 162:    */         }
/* 163:177 */         System.out.println(" stream, starts at " + cp.getStartBlock());
/* 164:    */       }
/* 165:    */     }
/* 166:    */   }
/* 167:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.poifs.dev.POIFSHeaderDumper
 * JD-Core Version:    0.7.0.1
 */